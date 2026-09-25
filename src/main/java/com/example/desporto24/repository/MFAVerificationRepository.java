package com.example.desporto24.repository;

import com.example.desporto24.register.MFA.MFAVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MFAVerificationRepository extends JpaRepository<MFAVerification, Long> {
    Optional<MFAVerification> findByCode(String code);
    Optional<MFAVerification> findByChallengeId(String challengeId);
}
