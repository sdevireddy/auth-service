package com.zen.auth.services;

import com.zen.auth.common.entity.Tenant;
import com.zen.auth.config.DataSourceManager;
import com.zen.auth.dto.ZenTenantDTO;
import com.zen.auth.filters.TenantContextHolder;
import com.zen.auth.repository.TenantRepository;
import com.zen.auth.utility.CommonUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Lazy
public class TenantService {

    @Autowired
    private DataSourceManager dataSourceManager;

    @Autowired
    private FlywayMigrationService flywayMigrationService;

    @Autowired
    private TenantUserService tenantUserService;

    @Autowired
    private TenantRepository tenantRepository;

    private static final Random RANDOM = new Random();

    @Transactional
    public void createTenant(ZenTenantDTO dto) {
        String userName = dto.getUserName().trim().toLowerCase();

        // 1️⃣ Check email already exists
        if (tenantRepository.findByEmail(userName).isPresent()) {
            throw new IllegalArgumentException("Admin email is already registered with another tenant.");
        }

        // 2️⃣ Generate padded 7-digit suffix
        String paddedSuffix = String.format("%07d", RANDOM.nextInt(10_000_000));
        System.out.println("📛 Padded 7-digit: " + paddedSuffix);

        // 3️⃣ Determine base tenant name
        String tenantBaseName = TenantContextHolder.getTenantId();
        if (tenantBaseName == null) {
            tenantBaseName = CommonUtility.extractTenantsSuffix(userName);
        }

        String fullTenantId = tenantBaseName + "_" + paddedSuffix;
        TenantContextHolder.setTenantId(fullTenantId);

        // 4️⃣ Save tenant info
        Tenant tenant = new Tenant();
        tenant.setOrgName(tenantBaseName);
        tenant.setSuffix(paddedSuffix);
        tenant.setEmail(userName);
        tenant.setAdminUsername(dto.getAdminName());
        tenant.setCreatedAt(LocalDateTime.now());
        tenant.setUpdatedAt(LocalDateTime.now());

        tenantRepository.save(tenant);
        dto.setOrgId(tenant.getTenantId());

        System.out.println("✅ Tenant saved correctly: " + fullTenantId);

        // 5️⃣ Create schema
        try {
            dataSourceManager.createSchema(tenantBaseName, paddedSuffix);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tenant schema", e);
        }

        // 6️⃣ Run Flyway migrations
        try {
            flywayMigrationService.runMigrations(fullTenantId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to run migrations for tenant schema", e);
        }

        // 7️⃣ Create admin user in schema
        try {
            tenantUserService.createUserInTenant(fullTenantId, tenant, dto.getPassword());
        } catch (Exception ex) {
            tenantRepository.deleteByEmail(userName);
            throw new RuntimeException("Failed to create admin user, rolling back tenant", ex);
        }
    }

    public String getTenantSuffix(String orgName) {
        String baseName = CommonUtility.extractTenantsSuffix(orgName);
        Optional<Tenant> tenantOpt = tenantRepository.getByOrgName(baseName);
        return tenantOpt.map(t -> t.getOrgName() + "_" + t.getSuffix())
                        .orElseThrow(() -> new IllegalArgumentException("Tenant not found for: " + orgName));
    }
}
