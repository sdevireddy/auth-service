package com.zen.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.Permissions;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, Long> {
	
}