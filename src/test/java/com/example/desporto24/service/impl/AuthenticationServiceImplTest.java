package com.example.desporto24.service.impl;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.exception.SMSAlreadyConfirmedException;
import com.example.desporto24.exception.SMSExpiredException;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.model.dto.auth.MfaConfirmRequest;
import com.example.desporto24.register.MFA.MFAVerification;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    private static final String TEST_USERNAME = "fixture-user";
    private static final String TEST_MFA_CODE = "fixture-code";
    private static final String TEST_PASSWORD = "not-a-real-password";
    private static final String TEST_CHALLENGE_ID = TEST_CHALLENGE_ID;
    private static final String TEST_CHALLENGE_HASH = TEST_CHALLENGE_HASH;

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
        Profile profile = activeProfile(true);
        when(profileRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(profile));
        when(passwordEncoder.encode(anyString())).thenReturn(TEST_CHALLENGE_HASH);

        try (MockedStatic<SmsUtils> sms = mockStatic(SmsUtils.class)) {
            LoginResponse response = service().login(credentials());

            assertTrue(response.mfaRequired());
            assertNotNull(response.challengeId());
            assertNull(response.token());
            assertNull(response.profile());
            verifyNoInteractions(jwtTokenProvider);
            verify(mfaVerificationService).saveConfirmationMFA(any());
            sms.verify(() -> SmsUtils.sendSMS(eq("+351"), eq("912345678"), anyString()));
        }
    }

    @Test
    void wrongMfaCodeNeverGeneratesJwt() {
        MFAVerification challenge = challenge(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID)).thenReturn(Optional.of(challenge));
        when(passwordEncoder.matches("WRONG", TEST_CHALLENGE_HASH)).thenReturn(false);

        assertThrows(
                BadCredentialsException.class,
                () -> service().confirmMfa(new MfaConfirmRequest(TEST_CHALLENGE_ID, "wrong"))
        );

        verifyNoInteractions(jwtTokenProvider);
        verify(mfaVerificationService, never()).confirmIfPending(anyString(), any());
    }

    @Test
    void expiredMfaChallengeNeverGeneratesJwt() {
        MFAVerification challenge = challenge(LocalDateTime.now(ZoneOffset.UTC).minusSeconds(1));
        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID)).thenReturn(Optional.of(challenge));

        assertThrows(
                SMSExpiredException.class,
                () -> service().confirmMfa(new MfaConfirmRequest(TEST_CHALLENGE_ID, TEST_MFA_CODE))
        );

        verifyNoInteractions(jwtTokenProvider);
        verify(mfaVerificationService, never()).confirmIfPending(anyString(), any());
    }

    @Test
    void lockedProfileCannotCompleteMfaChallenge() {
        MFAVerification challenge = challenge(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        Profile profile = activeProfile(true);
        profile.setNotLocked(false);

        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID)).thenReturn(Optional.of(challenge));
        when(passwordEncoder.matches(TEST_MFA_CODE, TEST_CHALLENGE_HASH)).thenReturn(true);
        when(profileRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(profile));

        assertThrows(
                com.example.desporto24.exception.AccountDisabledException.class,
                () -> service().confirmMfa(new MfaConfirmRequest(TEST_CHALLENGE_ID, TEST_MFA_CODE))
        );

        verifyNoInteractions(jwtTokenProvider);
        verify(mfaVerificationService, never()).confirmIfPending(anyString(), any());
    }

    @Test
    void confirmedMfaChallengeIsSingleUse() {
        MFAVerification challenge = challenge(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        Profile profile = activeProfile(true);
        ProfileDTO safeProfile = safeProfile();

        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID)).thenReturn(Optional.of(challenge));
        when(passwordEncoder.matches(TEST_MFA_CODE, TEST_CHALLENGE_HASH)).thenReturn(true);
        when(mfaVerificationService.confirmIfPending(eq(TEST_CHALLENGE_ID), any()))
                .thenReturn(true)
                .thenReturn(false);
        when(profileRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(profile));
        when(profileRepository.save(profile)).thenReturn(profile);
        when(jwtTokenProvider.generateJwtToken(any(ProfilePrincipal.class))).thenReturn("jwt-token");
        when(profileMapper.toDto(profile)).thenReturn(safeProfile);

        LoginResponse first = service().confirmMfa(new MfaConfirmRequest(TEST_CHALLENGE_ID, TEST_MFA_CODE));

        assertEquals("jwt-token", first.token());
        assertThrows(
                SMSAlreadyConfirmedException.class,
                () -> service().confirmMfa(new MfaConfirmRequest(TEST_CHALLENGE_ID, TEST_MFA_CODE))
        );
        verify(jwtTokenProvider, times(1)).generateJwtToken(any(ProfilePrincipal.class));
    }

    private AuthenticationServiceImpl service() {
        return new AuthenticationServiceImpl(
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
    }

    private MFAVerification challenge(LocalDateTime expiresAt) {
        return new MFAVerification(
                TEST_CHALLENGE_ID,
                TEST_CHALLENGE_HASH,
                LocalDateTime.now(ZoneOffset.UTC).minusMinutes(1),
                expiresAt,
                TEST_USERNAME
        );
    }

    private Profile activeProfile(boolean mfa) {
        Profile profile = new Profile();
        profile.setUserId("1234567890");
        profile.setUsername(TEST_USERNAME);
        profile.setPhone(AES.encryptString("912345678", profile.getUserId()));
        profile.setIndicativePhone("+351");
        profile.setEnabled(true);
        profile.setNotLocked(true);
        profile.setMFA(mfa);
        return profile;
    }

    private ProfileDTO credentials() {
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername(TEST_USERNAME);
        dto.setPassword(TEST_PASSWORD);
        return dto;
    }

    private ProfileDTO safeProfile() {
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername(TEST_USERNAME);
        return dto;
    }
}
