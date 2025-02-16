package com.oma.mecash.security_service.repository;

import com.oma.mecash.security_service.model.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findByEmailIgnoreCase(String email);
    Optional<AuthUser> findByEmail(String email);
}
