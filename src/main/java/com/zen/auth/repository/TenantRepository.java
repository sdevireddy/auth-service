package com.zen.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.Tenant;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByAdminUsername(String email);
    boolean existsByTenantId(Long tenantId);
	Optional<Tenant> findByEmail(String normalizedEmail);
	void deleteByEmail(String normalizedEmail);
	Optional<Tenant> getByOrgName(String name);
}
