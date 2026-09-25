package com.example.desporto24.service.interfaces;

import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.model.dto.auth.MfaConfirmRequest;
import org.springframework.web.multipart.MultipartFile;

public interface AuthenticationService {
    LoginResponse login(ProfileDTO credentials);
    LoginResponse confirmMfa(MfaConfirmRequest request);
    void terminateSession(ProfileDTO profileDTO);
    void signUpProfile(ProfileDTO profileDTO, MultipartFile photo);
    void resetPasswordStepOne(String email);
    void resetPasswordStepTwo(String newPassword, String token);
    void sendVerificationCode(String username);
    void confirmToken(String token);
}
