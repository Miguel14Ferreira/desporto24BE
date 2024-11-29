package com.example.desporto24.service.interfaces;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AuthenticationService {
    Profile authenticate(ProfileDTO profileDTO);
    void terminateSession(String email);
    void signUpProfile(ProfileDTO profileDTO, MultipartFile photo);
    void resetPasswordStepOne(String email);
    void resetPasswordStepTwo(String username, String newPassword, String token);
    void confirmMfaCode(String mfaCode);
    void sendVerificationCode(String username);
}