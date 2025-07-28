package com.zen.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.auth.common.entity.PermissionBundle;
import com.zen.auth.repository.PermissionBundleRepository;

@Service
public class PermissionBundleService {

    @Autowired
    private PermissionBundleRepository bundleRepository;

    public PermissionBundle getBundleByName(String bundleName) {
        return bundleRepository.findByName(bundleName)
                .orElseThrow(() -> new IllegalArgumentException("Bundle not found"));
    }

    public void assignBundleToRole(Long roleId, String bundleName) {
        PermissionBundle bundle = getBundleByName(bundleName);
        // Logic to assign bundle’s permissions/features to role
    }
}
