package com.zen.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.RecordPermission;

@Repository
public interface RecordPermissionRepository extends JpaRepository<RecordPermission, Long> {
    boolean existsByUserIdAndRecordTypeAndRecordId(Long userId, String entityType, Long recordId);
    void deleteByUserIdAndRecordTypeAndRecordId(Long userId, String entityType, Long recordId);
	//boolean existsByUserIdAndEntityTypeAndRecordId(Long userId, String entityType, Long recordId);
	//void deleteByUserIdAndEntityTypeAndRecordId(Long userId, String entityType, Long recordId);
}