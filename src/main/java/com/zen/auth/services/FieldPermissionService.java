package com.zen.auth.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.auth.repository.FieldPermissionRepository;

@Service
public class FieldPermissionService {

    @Autowired
    private FieldPermissionRepository fieldPermissionRepository;

    public List<String> getAllowedFields(Long roleId, String moduleName, String featureName, String action) {
        return fieldPermissionRepository.findFieldNames(roleId, moduleName, featureName, action);
    }
}
