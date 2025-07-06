package com.zen.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.zen.auth.common.entity.Tenant;
//import com.zen.auth.common.entity.ZenTenant;
import com.zen.auth.config.DataSourceManager;
import com.zen.auth.dto.ZenTenantDTO;
import com.zen.auth.filters.TenantContextHolder;
//import com.zen.auth.repository.TenantRepository;
import com.zen.auth.repository.TenantRepository;
import com.zen.auth.utility.CommonUtility;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

//TenantService.java
@Service
@Lazy
public class TenantService {

	@Autowired
	private DataSourceManager dataSourceManager;
	@Autowired
	private FlywayMigrationService flywayMigrationService;
	@Autowired
	private TenantUserService tenantUserService = null;
	@Autowired
	private TenantRepository tenantRepository;

	private static final Random RANDOM = new Random();
	private static Long TENENAT_START_AT = 2006L;

	@Transactional
	public void createTenant(ZenTenantDTO dto) {

		String userName = dto.getUserName().trim().toLowerCase();

		// 1. Check if email is already registered
		if (tenantRepository.findByEmail(userName).isPresent()) {
			throw new IllegalArgumentException("Admin email is already registered with another tenant.");
		}

		// 2. Save tenant info (optional before schema creation, depending on design)
		String padded = String.format("%07d", new Random().nextInt(10_000_000));
		System.out.println("Padded 7-digit: " + padded);

		String tenantName = TenantContextHolder.getTenantId();
		if (tenantName == null) {
			tenantName = CommonUtility.extractTenantsSuffix(userName);
		}
		Tenant tenant = new Tenant();
		tenant.setOrgName(tenantName.trim());
		tenant.setSuffix(padded);
		tenant.setEmail(userName);
		tenant.setAdminUsername(dto.getAdminName()); // or separate username field
		tenant.setCreatedAt(LocalDateTime.now());
		tenant.setUpdatedAt(LocalDateTime.now());
		TenantContextHolder.setTenantId(tenantName +"_"+padded);

		tenantRepository.save(tenant);

		dto.setOrgId(tenant.getTenantId());

		System.out.println("tenant saved correctly");

		// 3. Create schema

		try {

			dataSourceManager.createSchema(tenantName, padded);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create tenant schema", e);
		}

		// 4. Run Flyway migrations
		try {
			flywayMigrationService.runMigrations(tenantName + "_" + padded);
		} catch (Exception e) {
			throw new RuntimeException("Failed to run migrations for tenant schema", e);
		}

		// 5. Create admin user in tenant schema
		try {
			tenantUserService.createUserInTenant(tenantName + "_" + padded, tenant, dto.getPassword());
		} catch (Exception ex) {
			// Optional: rollback tenant entry from master if needed
			ex.printStackTrace();
			tenantRepository.deleteByEmail(userName);
			throw new RuntimeException("Failed to create admin user, rolling back tenant", ex);
		}
	}

	public String getTenantSuffix(String orgName) {
		String name = CommonUtility.extractTenantsSuffix(orgName);
		Optional<Tenant> tenant = tenantRepository.getByOrgName(name);
		return tenant.get().getOrgName() + "_" + tenant.get().getSuffix();
	}
}