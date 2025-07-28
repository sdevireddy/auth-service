package com.zen.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.TenantModule;


@Repository
public interface TenantModuleRepository extends JpaRepository<TenantModule, Long> {
    Optional<TenantModule> findById(Long id);
    Optional<TenantModule> findByName(String name);
    List<TenantModule> findByNameContainingIgnoreCase(String name);
  //  Optional<TenantModule> findByKey(String key);
    Optional<TenantModule> findByIdAndName(Long id, String name);
   // List<TenantModule> findByTenantId(String tenantId);
   // Optional<TenantModule> findByTenantIdAndKey(String tenantId, String key);
}
