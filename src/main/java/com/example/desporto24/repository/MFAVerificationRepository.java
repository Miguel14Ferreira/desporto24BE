package com.example.desporto24.repository;

import com.example.desporto24.register.MFA.MFAVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MFAVerificationRepository extends JpaRepository<MFAVerification, Long> {
    Optional<MFAVerification> findByCode(String code);
    Optional<MFAVerification> findByChallengeId(String challengeId);

    @Transactional
    @Modifying
    @Query("""
            update MFAVerification m
               set m.confirmedAt = :confirmedAt
             where m.challengeId = :challengeId
               and m.confirmedAt is null
               and m.expiredAt >= :confirmedAt
            """)
    int confirmIfPending(@Param("challengeId") String challengeId,
                         @Param("confirmedAt") LocalDateTime confirmedAt);

    long deleteByPerfil(String perfil);
}
