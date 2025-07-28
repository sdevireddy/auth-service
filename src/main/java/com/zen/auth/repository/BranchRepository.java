package com.zen.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {}
