package com.example.desporto24.registo.MFA;

import com.example.desporto24.registo.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MFAVerificationRepository extends JpaRepository<MFAVerification, Long> {
    Optional<MFAVerification> findByCode(String token);
    @Transactional
    @Modifying
    @Query("UPDATE MFAVerification c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.code = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);
}
