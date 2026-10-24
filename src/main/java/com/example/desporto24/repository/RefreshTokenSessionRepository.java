package com.example.desporto24.repository;

import com.example.desporto24.model.RefreshTokenSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface RefreshTokenSessionRepository extends JpaRepository<RefreshTokenSession, String> {
    long deleteByExpiresAtBefore(Instant expiredBefore);
}
