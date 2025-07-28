package com.zen.auth.repository;


import com.zen.auth.common.entity.RoleFeaturePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoleFeaturePermissionRepository extends JpaRepository<RoleFeaturePermission, Long> {
    List<RoleFeaturePermission> findByRoleId(Long roleId);
}

