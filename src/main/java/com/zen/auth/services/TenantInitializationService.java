package com.zen.auth.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.auth.common.entity.ZenUser;

import jakarta.persistence.EntityManager;

@Service
public class TenantInitializationService {

    @Autowired 
    private RoleProvisioningService roleProvisioningService;

    public void assignDefaultAdminRoles(ZenUser adminUser, List<String> selectedModules, EntityManager em, String tenantId) {
		/*
		 * for (String moduleKey : selectedModules) {
		 * roleProvisioningService.copyRolesFromTemplate(tenantId, moduleKey, adminUser,
		 * em); } em.merge(adminUser);
		 */
    }
}
