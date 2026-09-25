package com.example.desporto24.register.MFA;

import com.example.desporto24.repository.MFAVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MFAVerificationService {

    private final MFAVerificationRepository repository;

    public void saveConfirmationMFA(MFAVerification verification) {
        repository.save(verification);
    }

    public Optional<MFAVerification> getCode(String code) {
        return repository.findByCode(code);
    }

    public Optional<MFAVerification> getByChallengeId(String challengeId) {
        return repository.findByChallengeId(challengeId);
    }

    public void setConfirmedAt(String challengeId, LocalDateTime confirmedAt) {
        MFAVerification verification = repository.findByChallengeId(challengeId)
                .orElseThrow(() -> new IllegalStateException("MFA challenge not found"));
        verification.setConfirmedAt(confirmedAt);
        repository.save(verification);
    }
}
