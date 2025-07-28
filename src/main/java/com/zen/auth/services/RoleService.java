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
}
