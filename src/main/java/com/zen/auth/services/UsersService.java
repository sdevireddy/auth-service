package com.zen.auth.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.auth.common.entity.Tenant;
import com.zen.auth.common.entity.ZenUser;
import com.zen.auth.entitymanagers.DynamicTenantManager;
import com.zen.auth.filters.TenantContextHolder;
import com.zen.auth.repository.TenantRepository;
import com.zen.auth.utility.CommonUtility;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

@Service
public class UsersService {
	
    private final DynamicTenantManager tenantManager;
    @Autowired
    private TenantRepository tenenatRepository;

    private final TenantRepository tenantRepository;

    public UsersService(DynamicTenantManager tenantManager, TenantRepository tenantRepository) {
        this.tenantManager = tenantManager;
        this.tenantRepository = tenantRepository;
    }

    public Optional<ZenUser> findByEmailOrUsername(String compoundUsername) {
        String[] parts = compoundUsername.split("\\|");
        if (parts.length != 2 || compoundUsername.isBlank()) {
            throw new IllegalArgumentException("Invalid login format. Expected tenantId|username.");
        }

        String tenantId = parts[0];
        String emailOrUsername = parts[1];
        String ema = CommonUtility.extractTenantsSuffix(tenantId);

        Optional<Tenant> tenant = tenantRepository.getByOrgName(ema);
        if (tenant.isEmpty()) {
            throw new IllegalArgumentException("Tenant not found: " + ema);
        }
        String fullTenantName = ema + "_" + tenant.get().getSuffix();
        TenantContextHolder.setTenantId(fullTenantName);

        EntityManager em = tenantManager.getEntityManagerForTenant(fullTenantName);
        try {
            ZenUser user = em.createQuery(
                    "SELECT u FROM ZenUser u WHERE u.username = :val OR u.email = :val",
                    ZenUser.class
            )
                    .setParameter("val", emailOrUsername)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}
