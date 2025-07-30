package com.zen.auth.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.auth.common.entity.Module;
import com.zen.auth.common.entity.RoleFeaturePermission;
import com.zen.auth.common.entity.Roles;
import com.zen.auth.common.entity.ZenUser;
import com.zen.auth.config.ModulePermissionRegistry;
import com.zen.auth.entitymanagers.DynamicTenantManager;
import com.zen.auth.filters.TenantContextHolder;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

//com.zen.auth.services.RoleProvisioningService.java
@Service
public class RoleProvisioningService {

 @Autowired 
 private EntityManager commonEntityManager;
 @Autowired 
 private DynamicTenantManager tenantManager;
 
 
 @Transactional
public void createTenantRoles(String tenantId, String moduleKey, ZenUser user, Set<String> rolesToAssign) {
    try {
        TenantContextHolder.setTenantId(tenantId);
        // 🔄 Use this line if you want an EntityManager for DB work
        EntityManager em = tenantManager.getEntityManagerForTenant(tenantId);

        // ✅ Now execute logic like copying roles
        copyRolesFromTemplate(tenantId, moduleKey, user, rolesToAssign);

    } finally {
    	TenantContextHolder.clear(); // ✅ Always clear after thread-bound usage
    }
}

 public void copyRolesFromTemplate(String tenantId, String moduleKey, ZenUser user, Set<String> rolesToAssign) {
	    EntityManager tenantEm = tenantManager.getEntityManagerForTenant(tenantId);

	    // Fetch from common schema
	    Module commonModule = commonEntityManager.createQuery("""
	        SELECT m FROM Module m WHERE m.moduleKey = :moduleKey
	    """, Module.class).setParameter("moduleKey", moduleKey).getSingleResult();

	    List<Roles> templates = commonEntityManager.createQuery("""
	        SELECT r FROM Roles r WHERE r.module.id = :moduleId
	    """, Roles.class).setParameter("moduleId", commonModule.getId()).getResultList();

	    List<String> validPermissions = ModulePermissionRegistry.getPermissionsForModule(moduleKey.toLowerCase());

	    for (Roles template : templates) {
	        Roles tenantRole = new Roles();
	        tenantRole.setName(template.getName());
	        tenantRole.setDefault(template.isDefault());
	        tenantRole.setModule(commonModule);

	        List<RoleFeaturePermission> rfpTemplates = commonEntityManager.createQuery("""
	            SELECT rfp FROM RoleFeaturePermission rfp
	            JOIN FETCH rfp.permission
	            WHERE rfp.role.id = :roleId
	        """, RoleFeaturePermission.class).setParameter("roleId", template.getId()).getResultList();

	        Set<RoleFeaturePermission> newRfps = new HashSet<>();
	        for (RoleFeaturePermission rfp : rfpTemplates) {
	            if (validPermissions.contains(rfp.getPermission().getName())) {
	                RoleFeaturePermission newRfp = new RoleFeaturePermission();
	                newRfp.setFeature(rfp.getFeature()); // assumes features are also synced
	                newRfp.setPermission(rfp.getPermission());
	                newRfp.setRole(tenantRole);
	                newRfps.add(newRfp);
	            }
	        }

	        tenantRole.setFeaturePermissions(newRfps);
	        tenantEm.persist(tenantRole);

	        // ✅ Assign role to user if requested
	        if (rolesToAssign.contains(template.getName())) {
	            user.getRoles().add(tenantRole);
	        }
	    }

	    tenantEm.merge(user);
	}


 
 public void copyDefaultRolesForModules(String tenantId, List<String> moduleKeys) {
	    EntityManager tenantEm = tenantManager.getEntityManagerForTenant(tenantId);

	    for (String moduleKey : moduleKeys) {
	        Module commonModule = commonEntityManager.createQuery("""
	            SELECT m FROM Module m WHERE m.moduleKey = :moduleKey
	        """, Module.class).setParameter("moduleKey", moduleKey).getSingleResult();

	        List<Roles> roleTemplates = commonEntityManager.createQuery("""
	            SELECT r FROM Roles r WHERE r.module.id = :moduleId
	        """, Roles.class).setParameter("moduleId", commonModule.getId()).getResultList();

	        List<String> validPermissions = ModulePermissionRegistry.getPermissionsForModule(moduleKey);

	        for (Roles template : roleTemplates) {
	            Roles tenantRole = new Roles();
	            tenantRole.setName(template.getName());
	            tenantRole.setDefault(template.isDefault());
	            tenantRole.setModule(commonModule);
	            tenantEm.persist(tenantRole);

	            List<RoleFeaturePermission> templatePerms = commonEntityManager.createQuery("""
	                SELECT rfp FROM RoleFeaturePermission rfp
	                JOIN FETCH rfp.permission
	                WHERE rfp.role.id = :roleId
	            """, RoleFeaturePermission.class).setParameter("roleId", template.getId()).getResultList();

	            for (RoleFeaturePermission templateRfp : templatePerms) {
	                if (validPermissions.contains(templateRfp.getPermission().getName())) {
	                    RoleFeaturePermission rfp = new RoleFeaturePermission();
	                    rfp.setRole(tenantRole);
	                    rfp.setFeature(templateRfp.getFeature());
	                    rfp.setPermission(templateRfp.getPermission());
	                    tenantEm.persist(rfp);
	                }
	            }
	        }
	    }
	}


	/*
	 * public void copyRolesFromTemplate(String tenantId, String moduleKey, ZenUser
	 * user, EntityManager em) { // Fetch from common `role_templates`
	 * List<RoleTemplate> templates = roleTemplateRepo.findByModuleKey(moduleKey);
	 * 
	 * for (RoleTemplate template : templates) { Roles tenantRole = new Roles();
	 * tenantRole.setName(template.getName());
	 * tenantRole.setDefaultRole(template.isDefaultRole());
	 * tenantRole.setModule(template.getModule());
	 * 
	 * Set<RoleFeaturePermission> featurePerms =
	 * template.getFeaturePermissions().stream().map(fp -> { RoleFeaturePermission
	 * rfp = new RoleFeaturePermission(); rfp.setRole(tenantRole);
	 * rfp.setFeature(fp.getFeature()); rfp.setPermission(fp.getPermission());
	 * return rfp; }).collect(Collectors.toSet());
	 * 
	 * tenantRole.setFeaturePermissions(featurePerms); em.persist(tenantRole);
	 * 
	 * // Assign to user if it's an admin role if
	 * (template.getName().endsWith("_Admin")) { user.getRoles().add(tenantRole); }
	 * } }
	 */

}
