package com.zen.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.Module;


@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

	  Optional<Module> findByName(String name); // NEW: for lookup by name
	  Optional<Module> findByModuleKey(String moduleKey);
	}