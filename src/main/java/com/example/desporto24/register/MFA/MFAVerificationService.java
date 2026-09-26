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

    public boolean confirmIfPending(String challengeId, LocalDateTime confirmedAt) {
        return repository.confirmIfPending(challengeId, confirmedAt) == 1;
    }
}
