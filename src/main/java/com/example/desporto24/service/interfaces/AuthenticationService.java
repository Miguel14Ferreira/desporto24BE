package com.example.desporto24.service.interfaces;

import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.model.dto.auth.MfaConfirmRequest;
import com.example.desporto24.model.dto.auth.RegistrationRequest;
import org.springframework.web.multipart.MultipartFile;

public interface AuthenticationService {
    LoginResponse login(ProfileDTO credentials);
    LoginResponse confirmMfa(MfaConfirmRequest request);
    LoginResponse resendMfa(String challengeId);
    void terminateSession(String username);
    void signUpProfile(RegistrationRequest request, MultipartFile photo);
    void resendRegistrationConfirmation(String username);
    void resetPasswordStepOne(String email);
    void resetPasswordStepTwo(String newPassword, String token);
    void confirmToken(String token);
}
