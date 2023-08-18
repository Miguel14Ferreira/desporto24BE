package com.example.desporto24.update.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateTokenService {
    private final UpdateTokenRepository confirmationTokenRepository;
    public void saveConfirmationToken(UpdateToken token) {
        confirmationTokenRepository.save(token);
    }
    public Optional<UpdateToken> getToken(String token){
        return confirmationTokenRepository.findByToken(token);
        }
    public int setConfirmedAt(String token){
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
