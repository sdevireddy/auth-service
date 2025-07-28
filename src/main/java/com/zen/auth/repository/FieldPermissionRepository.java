package com.zen.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.Feature;
import com.zen.auth.common.entity.FieldPermission;

@Repository
public interface FieldPermissionRepository extends JpaRepository<FieldPermission, Long> {

	@Query("SELECT fp.fieldName FROM FieldPermission fp WHERE fp.role.id = :roleId AND fp.module = :module AND fp.feature = :feature AND fp.action = :action")
	List<String> findAllowedFields(@Param("roleId") Long roleId, @Param("module") Module module,
			@Param("feature") Feature feature, @Param("action") String action);

	@Query("SELECT f.fieldName FROM FieldPermission f WHERE f.role.id = :roleId AND f.module.name = :module AND f.feature.name = :feature AND f.action = :action")
	List<String> findFieldNames(@Param("roleId") Long roleId,
	                            @Param("module") String module,
	                            @Param("feature") String feature,
	                            @Param("action") String action);
}
