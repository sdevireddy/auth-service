package com.zen.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.RoleModulePermission;

@Repository
public interface RoleModulePermissionRepository extends JpaRepository<RoleModulePermission, Long> {
	
}