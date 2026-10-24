package com.example.desporto24.repository;

import com.example.desporto24.model.RefreshTokenSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface RefreshTokenSessionRepository extends JpaRepository<RefreshTokenSession, String> {
    long deleteByExpiresAtBefore(Instant expiredBefore);

    // A compromised family must be revoked in one SQL operation, even on
    // horizontally scaled deployments. Other login families are unaffected.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update RefreshTokenSession t set t.revokedAt = :at " +
            "where t.familyId = :familyId and t.revokedAt is null")
    int revokeFamily(@Param("familyId") String familyId, @Param("at") Instant at);
}
