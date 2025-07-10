package com.zen.auth.services;

import com.zen.auth.common.entity.Tenant;
import com.zen.auth.common.entity.ZenUser;
import com.zen.auth.entitymanagers.DynamicTenantManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TenantUserService {

    private static final Logger log = LoggerFactory.getLogger(TenantUserService.class);

    private final DynamicTenantManager tenantManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public TenantUserService(DynamicTenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }

    public void createUserInTenant(String orgId, Tenant tenant, String pwd) {
        log.debug("🔧 Creating user in tenant schema: {}", orgId);

        EntityManager em = tenantManager.getEntityManagerForTenant(orgId);
        EntityTransaction tx = em.getTransaction();

        log.debug("👤 Admin username: {}", tenant.getAdminUsername());
        log.debug("📧 Admin email: {}", tenant.getEmail());

        try {
            tx.begin();

            String encodedPassword;
            try {
                encodedPassword = passwordEncoder.encode(pwd);
                log.debug("🔐 Password encoded successfully for user: {}", tenant.getAdminUsername());
            } catch (Exception e) {
                log.error("❌ Password encoding failed", e);
                throw new RuntimeException("Failed to encode password", e);
            }

            ZenUser user = new ZenUser();
            user.setUsername(tenant.getAdminUsername());
            user.setEmail(tenant.getEmail());
            user.setPassword(encodedPassword);
            user.setFirstLogin(false);
            user.setCreatedAt(Instant.now());
            user.setUpdatedAt(Instant.now());

            em.persist(user);
            tx.commit();

            log.info("✅ User '{}' created successfully in tenant '{}'", tenant.getAdminUsername(), orgId);

        } catch (Exception ex) {
            tx.rollback();
            log.error("❌ Failed to create user in tenant '{}', transaction rolled back", orgId, ex);
            throw ex;
        } finally {
            em.close();
            log.debug("🔚 EntityManager closed for tenant '{}'", orgId);
        }
    }
}
