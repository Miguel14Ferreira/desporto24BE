package com.example.desporto24.service.interfaces;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

public interface AuthenticationService {
    Profile authenticate(ProfileDTO profileDTO);
    void terminateSession(ProfileDTO profileDTO);
    void signUpProfile(ProfileDTO profileDTO, MultipartFile photo);
    void resetPasswordStepOne(String email);
    void resetPasswordStepTwo(String newPassword, String token);
    void confirmMfaCode(String mfaCode);
    void sendVerificationCode(String username);
    void confirmToken(String token);
}