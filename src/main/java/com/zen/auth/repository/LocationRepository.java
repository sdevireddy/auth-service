package com.zen.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {}