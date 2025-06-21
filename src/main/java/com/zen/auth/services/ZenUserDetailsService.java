package com.zen.auth.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zen.auth.common.entity.Tenant;
import com.zen.auth.common.entity.ZenUser;
import com.zen.auth.entitymanagers.DynamicTenantManager;
import com.zen.auth.filters.ZenUserDetails;
import com.zen.auth.repository.TenantRepository;
import com.zen.auth.utility.CommonUtility;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

@Service
public class ZenUserDetailsService implements UserDetailsService {

    private final DynamicTenantManager tenantManager;
    @Autowired
    private TenantRepository tenenatRepository;

    public ZenUserDetailsService(DynamicTenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }

    @Override
    public UserDetails loadUserByUsername(String compoundUsername) throws UsernameNotFoundException {
        // Compound format: tenantId|username
        String[] parts = compoundUsername.split("\\|");
        if (compoundUsername.isBlank()) {
            throw new UsernameNotFoundException("Invalid login format. Expected tenantId|username.");
        }

        String tenantId =  parts[0];
        String usernameOrEmail = parts[1];
        EntityManager em =null;
        String ema = CommonUtility.extractTenantsSuffix(tenantId);
        System.out.println("Organization id " + tenantId);
        Optional<Tenant> tenant = tenenatRepository.getByOrgName(ema);
        if (tenant.isPresent()) {
         em = tenantManager.getEntityManagerForTenant(ema + "_"+ tenant.get().getSuffix());
        } else {
        	 em = tenantManager.getEntityManagerForTenant(tenantId + "_"+ usernameOrEmail);
        }
        try {
            ZenUser user;
            try {
                user = em.createQuery("SELECT u FROM ZenUser u WHERE u.email = :val", ZenUser.class)
                    .setParameter("val", tenantId)
                    .getSingleResult();
            } catch (NoResultException e) {
                throw new UsernameNotFoundException("User not found in tenant: " + tenantId);
            }

            return new ZenUserDetails(user);

        } finally {
            em.close();
        }
    }

}



