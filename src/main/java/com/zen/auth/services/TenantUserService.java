package com.zen.auth.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zen.auth.common.entity.Tenant;
import com.zen.auth.common.entity.ZenUser;
import com.zen.auth.dto.ZenTenantDTO;
import com.zen.auth.entitymanagers.DynamicTenantManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import org.springframework.stereotype.Service;


import javax.sql.DataSource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
 
@Service
public class TenantUserService {

    private final DynamicTenantManager tenantManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public TenantUserService(DynamicTenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }

    public void createUserInTenant(String orgId, Tenant tenant,String pwd) {
        EntityManager em = tenantManager.getEntityManagerForTenant(orgId);
        System.out.println("username" + tenant.getAdminUsername());
        System.out.println("normalizedEmail" + tenant.getEmail());
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String encodedPassword = "";
            try {
				 encodedPassword = passwordEncoder.encode(pwd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // Sample user creation
            ZenUser user = new ZenUser();
            user.setUsername(tenant.getAdminUsername());
            user.setEmail(tenant.getEmail());
            user.setPassword(encodedPassword);
            user.setFirstLogin(false);
            user.setCreatedAt(Instant.now());
            user.setUpdatedAt(Instant.now());
            em.persist(user);

            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
