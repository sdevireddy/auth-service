package com.zen.auth.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zen.auth.common.entity.*;
import com.zen.auth.dto.*;
import com.zen.auth.repository.*;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionBundleRepository bundleRepository;
    private final FeatureRepository featureRepository;
    private final RoleFeaturePermissionRepository roleFeaturePermissionRepository;
    private final FieldPermissionRepository fieldPermissionRepository;

    public RoleService(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            PermissionBundleRepository bundleRepository,
            FeatureRepository featureRepository,
            RoleFeaturePermissionRepository roleFeaturePermissionRepository,
            FieldPermissionRepository fieldPermissionRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.bundleRepository = bundleRepository;
        this.featureRepository = featureRepository;
        this.roleFeaturePermissionRepository = roleFeaturePermissionRepository;
        this.fieldPermissionRepository = fieldPermissionRepository;
    }

    @Transactional
    public Roles createRole(RoleCreateDTO dto) {
        Roles role = new Roles();
        role.setName(dto.getName());

        // Set direct Permissions (optional if you're using only bundles/features)
        List<Permissions> permissions = permissionRepository.findAllById(dto.getPermissionIds());
        role.setPermissions(new HashSet<>(permissions));

        // Set Permission Bundles
        List<PermissionBundle> bundles = bundleRepository.findAllById(dto.getBundleIds());
        role.setPermissionBundles(new HashSet<>(bundles));

        // Set Feature Permissions
        Set<RoleFeaturePermission> featurePerms = new HashSet<>();
        for (FeaturePermissionDTO fpd : dto.getFeaturePermissions()) {
            Feature feature = featureRepository.findById(fpd.getFeatureId())
                    .orElseThrow(() -> new RuntimeException("Feature not found: " + fpd.getFeatureId()));
            Permissions perm = permissionRepository.findById(fpd.getPermissionId())
                    .orElseThrow(() -> new RuntimeException("Permission not found: " + fpd.getPermissionId()));

            RoleFeaturePermission rfp = new RoleFeaturePermission();
            rfp.setRole(role);
            rfp.setFeature(feature);
            rfp.setPermission(perm);
            featurePerms.add(rfp);
        }
        role.setFeaturePermissions(featurePerms);

        // Set Field Permissions
        Set<FieldPermission> fieldPerms = new HashSet<>();
        for (FieldPermissionDTO f : dto.getFieldPermissions()) {
            Feature feature = featureRepository.findById(f.getFeatureId())
                    .orElseThrow(() -> new RuntimeException("Feature not found: " + f.getFeatureId()));

            FieldPermission fp = new FieldPermission();
            fp.setRole(role);
            fp.setFeature(feature);
            fp.setFieldName(f.getFieldName());
            fp.setAction(f.getAction());
            fieldPerms.add(fp);
        }
        role.setFieldPermissions(fieldPerms);

        // Save and return role with all associations
        return roleRepository.save(role);
    }
    
    @Transactional
    public Roles updateRole(Long roleId, RoleCreateDTO dto) {
        Roles role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(dto.getName());

        // Clear and update permissions
        role.getPermissions().clear();
        role.getPermissions().addAll(permissionRepository.findAllById(dto.getPermissionIds()));

        role.getPermissionBundles().clear();
        role.getPermissionBundles().addAll(bundleRepository.findAllById(dto.getBundleIds()));

        // Clear and re-assign feature permissions
        roleFeaturePermissionRepository.deleteById(roleId);
        Set<RoleFeaturePermission> featurePerms = new HashSet<>();
        for (FeaturePermissionDTO fpd : dto.getFeaturePermissions()) {
            Feature feature = featureRepository.findById(fpd.getFeatureId())
                .orElseThrow(() -> new RuntimeException("Feature not found"));
            Permissions perm = permissionRepository.findById(fpd.getPermissionId())
                .orElseThrow(() -> new RuntimeException("Permission not found"));

            RoleFeaturePermission rfp = new RoleFeaturePermission();
            rfp.setRole(role);
            rfp.setFeature(feature);
            rfp.setPermission(perm);
            featurePerms.add(rfp);
        }
        role.setFeaturePermissions(featurePerms);

        // Clear and re-assign field permissions
        fieldPermissionRepository.deleteById(roleId);
        Set<FieldPermission> fieldPerms = new HashSet<>();
        for (FieldPermissionDTO f : dto.getFieldPermissions()) {
            Feature feature = featureRepository.findById(f.getFeatureId())
                .orElseThrow(() -> new RuntimeException("Feature not found"));

            FieldPermission fp = new FieldPermission();
            fp.setRole(role);
            fp.setFeature(feature);
            fp.setFieldName(f.getFieldName());
            fp.setAction(f.getAction());
            fieldPerms.add(fp);
        }
        role.setFieldPermissions(fieldPerms);

        return roleRepository.save(role);
    }

    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    public Roles getRoleById(Long id) {
        return roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found"));
    }
    
    

    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found");
        }
        roleRepository.deleteById(id);
    }

}
