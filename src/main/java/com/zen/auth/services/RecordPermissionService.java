package com.zen.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.auth.common.entity.RecordPermission;
import com.zen.auth.common.entity.ZenUser;
import com.zen.auth.repository.RecordPermissionRepository;

import com.zen.auth.repository.UsersRepository;

@Service
public class RecordPermissionService {

    @Autowired
    private RecordPermissionRepository recordPermissionRepository;

    @Autowired
    private UsersRepository usersRepository;

    public boolean hasAccess(Long userId, String entityType, Long recordId) {
        return recordPermissionRepository.existsByUserIdAndRecordTypeAndRecordId(userId, entityType, recordId);
    }

    public void grantAccess(Long userId, String entityType, Long recordId, String permission) {
        ZenUser user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RecordPermission access = new RecordPermission(user, entityType, recordId, permission);
        recordPermissionRepository.save(access);
    }

    public void revokeAccess(Long userId, String entityType, Long recordId) {
        recordPermissionRepository.deleteByUserIdAndRecordTypeAndRecordId(userId, entityType, recordId);
    }
}
