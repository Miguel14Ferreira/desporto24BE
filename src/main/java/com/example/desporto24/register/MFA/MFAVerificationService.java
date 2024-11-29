package com.example.desporto24.register.MFA;

import com.example.desporto24.repository.MFAVerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MFAVerificationService {
    private final MFAVerificationRepository MFAverificationRepository;

    public void saveConfirmationMFA(MFAVerification code) {
        MFAverificationRepository.save(code);
    }
    public Optional<MFAVerification> getCode(String code){
        return MFAverificationRepository.findByCode(code);
    }
    public void setConfirmedAt(String code){
        MFAverificationRepository.updateConfirmedAt(
                code, LocalDateTime.now());
    }
}
