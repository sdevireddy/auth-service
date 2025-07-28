package com.zen.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.PermissionBundle;

@Repository
public interface PermissionBundleRepository extends JpaRepository<PermissionBundle, Long> {
    Optional<PermissionBundle> findByName(String name);
}
