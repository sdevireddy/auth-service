package com.zen.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.Feature;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {

    @Query("SELECT f FROM Feature f WHERE f.module.moduleKey = :moduleKey")
    List<Feature> findByModuleKey(@Param("moduleKey") String moduleKey);
}
