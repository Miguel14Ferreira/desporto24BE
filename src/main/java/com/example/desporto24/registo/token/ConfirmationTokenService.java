package com.example.desporto24.registo.token;

import com.example.desporto24.changePassword.UserChangePasswordRequest;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.login.LoginRequest;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }
    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepository.findByToken(token);
        }
    public int setConfirmedAt(String token){
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
