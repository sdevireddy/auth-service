package com.zen.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zen.auth.common.entity.ZenUser;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<ZenUser, Long> {
    Optional<ZenUser> findByUsername(String username);
    Optional<ZenUser> findByEmail(String email);
}

