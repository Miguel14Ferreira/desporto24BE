package com.example.desporto24.service.impl;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.register.MFA.MFAVerificationService;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.LoginAttemptService;
import com.example.desporto24.service.interfaces.ProfileService;
import com.example.desporto24.utility.JWTokenProvider;
import com.example.desporto24.utility.SmsUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock private LoginAttemptService loginAttemptService;
    @Mock private ProfileService profileService;
    @Mock private ProfileRepository profileRepository;
    @Mock private ConfirmationTokenService confirmationTokenService;
    @Mock private EmailService emailService;
    @Mock private NotificationsRepository notificationsRepository;
    @Mock private MFAVerificationService mfaVerificationService;
    @Mock private PasswordHashService passwordHashService;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private JWTokenProvider jwtTokenProvider;
    @Mock private ProfileMapper profileMapper;

    @Test
    void mfaEnabledLoginDoesNotGenerateJwtUntilChallengeIsConfirmed() {
        AuthenticationServiceImpl service = new AuthenticationServiceImpl(
                loginAttemptService,
                profileService,
                profileRepository,
                confirmationTokenService,
                emailService,
                notificationsRepository,
                mfaVerificationService,
                passwordHashService,
                passwordEncoder,
                jwtTokenProvider,
                profileMapper
        );

        Profile profile = activeProfile(true);
        ProfileDTO credentials = credentials();
        ProfileDTO safeProfile = new ProfileDTO();
        safeProfile.setUsername("miguel");

        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(profile));
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$challenge-hash");
        when(profileMapper.toDto(profile)).thenReturn(safeProfile);

        try (MockedStatic<SmsUtils> sms = mockStatic(SmsUtils.class)) {
            LoginResponse response = service.login(credentials);

            assertTrue(response.mfaRequired());
            assertNotNull(response.challengeId());
            assertNull(response.token());
            verifyNoInteractions(jwtTokenProvider);
            verify(mfaVerificationService).saveConfirmationMFA(any());
            sms.verify(() -> SmsUtils.sendSMS("+351", "912345678", anyString()));
        }
    }

    private Profile activeProfile(boolean mfa) {
        Profile profile = new Profile();
        profile.setUserId("1234567890");
        profile.setUsername("miguel");
        profile.setPhone(AES.encryptString("912345678", profile.getUserId()));
        profile.setIndicativePhone("+351");
        profile.setEnabled(true);
        profile.setNotLocked(true);
        profile.setMFA(mfa);
        return profile;
    }

    private ProfileDTO credentials() {
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername("miguel");
        dto.setPassword("secret");
        return dto;
    }
}
