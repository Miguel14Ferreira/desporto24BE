package com.example.desporto24.repository;

import com.example.desporto24.model.RefreshTokenSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RefreshTokenSessionRepository extends JpaRepository<RefreshTokenSession, String> {
    long deleteByExpiresAtBefore(Instant expiredBefore);

    // A compromised family must be revoked in one SQL operation, even on
    // horizontally scaled deployments. Other login families are unaffected.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update RefreshTokenSession t set t.revokedAt = :at " +
            "where t.familyId = :familyId and t.revokedAt is null")
    int revokeFamily(@Param("familyId") String familyId, @Param("at") Instant at);

    /** A family must belong to the authenticated profile to be visible/revocable. */
    @Query("select t from RefreshTokenSession t " +
            "where t.profileId = :profileId and t.familyId is not null " +
            "and t.consumedAt is null and t.revokedAt is null " +
            "and t.expiresAt > :now order by t.expiresAt desc")
    List<RefreshTokenSession> activeForProfile(@Param("profileId") Long profileId,
                                               @Param("now") Instant now);

    boolean existsByProfileIdAndFamilyIdAndConsumedAtIsNullAndRevokedAtIsNullAndExpiresAtAfter(
            Long profileId, String familyId, Instant now);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update RefreshTokenSession t set t.revokedAt = :at " +
            "where t.profileId = :profileId and t.familyId = :familyId " +
            "and t.revokedAt is null")
    int revokeOwnedFamily(@Param("profileId") Long profileId,
                          @Param("familyId") String familyId,
                          @Param("at") Instant at);
}
