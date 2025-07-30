package com.zen.auth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zen.auth.common.entity.Feature;
import com.zen.auth.common.entity.Module;
import com.zen.auth.common.entity.Permissions;
import com.zen.auth.common.entity.RoleFeaturePermission;
import com.zen.auth.common.entity.Roles;
import com.zen.auth.common.entity.ZenUser;
import com.zen.auth.dto.ApiResponse;
import com.zen.auth.dto.RoleCreateDTO;
import com.zen.auth.dto.RoleWithPermissionsDTO;
import com.zen.auth.entitymanagers.DynamicTenantManager;
import com.zen.auth.filters.TenantContextHolder;
import com.zen.auth.services.RoleService;

import jakarta.persistence.EntityManager;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private EntityManager commonEntityManager;
    

    @Autowired
    private DynamicTenantManager tenantManager;
    
    @Autowired
    private  RoleService roleService;
    

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Roles>> createRole(@RequestBody RoleCreateDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Role created", roleService.createRole(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Roles>> updateRole(@PathVariable Long id, @RequestBody RoleCreateDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Role updated", roleService.updateRole(id, dto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Roles>>> getAllRoles() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Roles list", roleService.getAllRoles()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Roles>> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Role found", roleService.getRoleById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Role deleted", null));
    }



    @GetMapping("/default")
    public ResponseEntity<ApiResponse<List<RoleWithPermissionsDTO>>> getDefaultRolesWithPermissions(
            @RequestParam String module) {

        try {
            // Step 1: Get Module
            Module moduleEntity = commonEntityManager.createQuery("""
                SELECT m FROM Module m WHERE m.moduleKey = :key
            """, Module.class)
                    .setParameter("key", module)
                    .getSingleResult();

            // Step 2: Get Default Roles for this Module
            List<Roles> roles = commonEntityManager.createQuery("""
                SELECT r FROM Roles r WHERE r.module.id = :moduleId AND r.isDefault = true
            """, Roles.class)
                    .setParameter("moduleId", moduleEntity.getId())
                    .getResultList();

            List<RoleWithPermissionsDTO> roleDTOList = new ArrayList<>();

            for (Roles role : roles) {
                Map<String, Set<String>> featurePermissions = new HashMap<>();

                List<RoleFeaturePermission> rfps = commonEntityManager.createQuery("""
                    SELECT rfp FROM RoleFeaturePermission rfp WHERE rfp.role.id = :roleId
                """, RoleFeaturePermission.class)
                        .setParameter("roleId", role.getId())
                        .getResultList();

                for (RoleFeaturePermission rfp : rfps) {
                    String feature = rfp.getFeature().getName();
                    String permission = rfp.getPermission().getName();
                    featurePermissions.computeIfAbsent(feature, k -> new HashSet<>()).add(permission);
                }

                roleDTOList.add(new RoleWithPermissionsDTO(role.getName(), role.isDefault(), module, featurePermissions));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Default roles fetched successfully", roleDTOList));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Error fetching roles: " + e.getMessage(), null));
        }
    }
    @GetMapping("/module")
    public ResponseEntity<ApiResponse<List<RoleWithPermissionsDTO>>> getRolesByModule(@RequestParam String module) {
        try {
            Module moduleEntity = commonEntityManager.createQuery("""
                SELECT m FROM Module m WHERE m.moduleKey = :key
            """, Module.class).setParameter("key", module).getSingleResult();

            List<Roles> roles = commonEntityManager.createQuery("""
                SELECT r FROM Roles r WHERE r.module.id = :moduleId
            """, Roles.class).setParameter("moduleId", moduleEntity.getId()).getResultList();

            List<RoleWithPermissionsDTO> roleDTOList = new ArrayList<>();

            for (Roles role : roles) {
                Map<String, Set<String>> featurePermissions = new HashMap<>();
                List<RoleFeaturePermission> rfps = commonEntityManager.createQuery("""
                    SELECT rfp FROM RoleFeaturePermission rfp WHERE rfp.role.id = :roleId
                """, RoleFeaturePermission.class).setParameter("roleId", role.getId()).getResultList();

                for (RoleFeaturePermission rfp : rfps) {
                    String feature = rfp.getFeature().getName();
                    String permission = rfp.getPermission().getName();
                    featurePermissions.computeIfAbsent(feature, k -> new HashSet<>()).add(permission);
                }

                roleDTOList.add(new RoleWithPermissionsDTO(role.getName(), role.isDefault(), module, featurePermissions));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Roles fetched successfully", roleDTOList));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Error fetching roles", null));
        }
    }
    
    @PostMapping("/users/{userId}/assign-role")
    public ResponseEntity<ApiResponse<String>> assignRoleToUser(
        @PathVariable Long userId,
        @RequestBody Set<String> roleNames) {
    	   String tenantId = TenantContextHolder.getTenantId();
        try {
            EntityManager em = tenantManager.getEntityManagerForTenant(tenantId);
            ZenUser user = em.find(ZenUser.class, userId);

            if (user == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "User not found", null));
            }

            List<Roles> roles = em.createQuery("""
                SELECT r FROM Roles r WHERE r.name IN :names
            """, Roles.class).setParameter("names", roleNames).getResultList();

            user.getRoles().addAll(roles);
            em.merge(user);

            return ResponseEntity.ok(new ApiResponse<>(true, "Roles assigned successfully", null));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Error assigning roles", null));
        }
    }

    @PostMapping("/custom")
    public ResponseEntity<ApiResponse<String>> createCustomRole(@RequestBody RoleWithPermissionsDTO dto) {
        try {
            Module module = commonEntityManager.createQuery("""
                SELECT m FROM Module m WHERE m.name = :name
            """, Module.class).setParameter("name", dto.getRoleName().split("_")[0]).getSingleResult();

            Roles role = new Roles();
            role.setName(dto.getRoleName());
            role.setModule(module);
            role.setDefault(false);

            Set<RoleFeaturePermission> permissions = new HashSet<>();

            for (Map.Entry<String, Set<String>> entry : dto.getFeaturePermissions().entrySet()) {
                String featureName = entry.getKey();
                List<Feature> features = commonEntityManager.createQuery("""
                    SELECT f FROM Feature f WHERE f.name = :name AND f.module.id = :moduleId
                """, Feature.class).setParameter("name", featureName).setParameter("moduleId", module.getId()).getResultList();

                if (features.isEmpty()) continue;
                Feature feature = features.get(0);

                for (String permName : entry.getValue()) {
                    Permissions perm = commonEntityManager.createQuery("""
                        SELECT p FROM Permissions p WHERE p.name = :name
                    """, Permissions.class).setParameter("name", permName).getSingleResult();

                    RoleFeaturePermission rfp = new RoleFeaturePermission();
                    rfp.setFeature(feature);
                    rfp.setPermission(perm);
                    rfp.setRole(role);
                    permissions.add(rfp);
                }
            }

            role.setFeaturePermissions(permissions);
     	   String tenantId = TenantContextHolder.getTenantId();
               EntityManager em = tenantManager.getEntityManagerForTenant(tenantId);
            em.persist(role);
            return ResponseEntity.ok(new ApiResponse<>(true, "Custom role created successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Error creating custom role", null));
        }
    }

    @GetMapping("/users/{userId}/roles")
    public ResponseEntity<ApiResponse<List<String>>> getRolesOfUser(@PathVariable Long userId) {
 	   String tenantId = TenantContextHolder.getTenantId();
     try {
         EntityManager em = tenantManager.getEntityManagerForTenant(tenantId);
            ZenUser user = em.find(ZenUser.class, userId);

            if (user == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "User not found", null));
            }

            List<String> roles = user.getRoles().stream().map(Roles::getName).toList();
            return ResponseEntity.ok(new ApiResponse<>(true, "Roles fetched", roles));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Error fetching roles", null));
        }
    }


}

