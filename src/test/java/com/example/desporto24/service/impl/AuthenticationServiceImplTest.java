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
import com.example.desporto24.model.dto.auth.RegistrationRequest;
import com.example.desporto24.register.MFA.MFAVerification;
import com.example.desporto24.register.MFA.MFAVerificationService;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.register.token.ConfirmationToken;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.security.TotpService;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.LoginAttemptService;
import com.example.desporto24.service.PilotEventService;
import com.example.desporto24.service.interfaces.ProfileService;
import com.example.desporto24.utility.JWTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    private static final String TEST_USERNAME = "fixture-user";
    private static final String TEST_MFA_CODE = "123456";
    private static final String TEST_PASSWORD = "not-a-real-password";
    private static final String TEST_CHALLENGE_ID = "challenge-id-fixture";
    private static final String TEST_CHALLENGE_HASH = "$2a$fixture-hash";

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
    @Mock private TotpService totpService;
    @Mock private PilotEventService pilotEventService;

    @Test
    void logoutInvalidatesExistingJwtVersion() {
        Profile profile = activeProfile(false);
        profile.setTokenVersion(4L);
        when(profileRepository.findUserByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(profile));
        when(profileRepository.save(profile)).thenReturn(profile);

        service().terminateSession(TEST_USERNAME);

        assertEquals(5L, profile.getTokenVersion());
        assertEquals(
                com.example.desporto24.model.Status.OFFLINE,
                profile.getStatusId()
        );
        verify(profileRepository).save(profile);
    }

    @Test
    void passwordResetEmailUsesBilingualTemplateAndSavedToken() {
        ProfileDTO profile = new ProfileDTO();
        profile.setUsername("<fixture-user>");
        profile.setEmail("fixture@example.test");
        when(profileService.findUserByEmail("fixture@example.test")).thenReturn(profile);

        service().resetPasswordStepOne("fixture@example.test");

        org.mockito.ArgumentCaptor<ConfirmationToken> token =
                org.mockito.ArgumentCaptor.forClass(ConfirmationToken.class);
        verify(confirmationTokenService).saveConfirmationToken(token.capture());
        org.mockito.ArgumentCaptor<String> html = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq("fixture@example.test"),
                eq("Recupera a tua palavra-passe | Reset your password — Desporto24"), html.capture());
        assertEquals("<fixture-user>", token.getValue().getProfile());
        assertTrue(html.getValue().contains("https://example.test/login/resetPassword/" + token.getValue().getToken()));
        assertTrue(html.getValue().contains("Definir palavra-passe / Reset password"));
        assertTrue(html.getValue().contains("15 minutos"));
        assertTrue(html.getValue().contains("15 minutes"));
        assertTrue(html.getValue().contains("&lt;fixture-user&gt;"));
        assertFalse(html.getValue().contains("<fixture-user>"));
        assertTrue(html.getValue().contains("If you did not make this request"));
    }

    @Test
    void passwordResetInvalidatesExistingJwtVersion() {
        ConfirmationToken token = mock(ConfirmationToken.class);
        Profile profile = activeProfile(false);
        profile.setTokenVersion(2L);

        when(confirmationTokenService.getToken("reset-token"))
                .thenReturn(Optional.of(token));
        when(token.getConfirmedAt()).thenReturn(null);
        when(token.getExpiredAt())
                .thenReturn(LocalDateTime.now().plusMinutes(5));
        when(token.getProfile()).thenReturn(TEST_USERNAME);
        when(profileRepository.findUserByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(profile));
        when(passwordHashService.encode("new-password"))
                .thenReturn("encoded-new-password");
        when(profileRepository.save(profile)).thenReturn(profile);

        service().resetPasswordStepTwo(
                "new-password",
                "reset-token"
        );

        assertEquals(3L, profile.getTokenVersion());
        assertEquals("encoded-new-password", profile.getPassword());
        verify(profileRepository).save(profile);
    }

    @Test
    void verifiedPasswordResetUnlocksTemporarilyBlockedAccount() {
        ConfirmationToken token = mock(ConfirmationToken.class);
        Profile profile = activeProfile(false);
        profile.setNotLocked(false);
        profile.setTokenVersion(7L);
        profile.setLoginAttempts(3);

        when(confirmationTokenService.getToken("recovery-token"))
                .thenReturn(Optional.of(token));
        when(token.getConfirmedAt()).thenReturn(null);
        when(token.getExpiredAt())
                .thenReturn(LocalDateTime.now().plusMinutes(5));
        when(token.getProfile()).thenReturn(TEST_USERNAME);
        when(profileRepository.findUserByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(profile));
        when(passwordHashService.encode("recovered-password"))
                .thenReturn("encoded-recovered-password");
        when(profileRepository.save(profile)).thenReturn(profile);

        service().resetPasswordStepTwo(
                "recovered-password",
                "recovery-token"
        );

        assertTrue(profile.getNotLocked());
        assertEquals(0, profile.getLoginAttempts());
        assertEquals(com.example.desporto24.model.Status.OFFLINE, profile.getStatusId());
        assertEquals(8L, profile.getTokenVersion());
        assertEquals("encoded-recovered-password", profile.getPassword());
        verify(profileRepository).save(profile);
    }

    @Test
    void loginAcceptsUsernameWithDifferentLetterCase() {
        Profile profile = activeProfile(false);
        ProfileDTO credentials = credentials();
        credentials.setUsername("FiXtUrE-UsEr");
        ProfileDTO safeProfile = safeProfile();

        when(profileRepository.findUserByUsernameIgnoreCase("FiXtUrE-UsEr"))
                .thenReturn(Optional.of(profile));
        when(profileRepository.save(profile)).thenReturn(profile);
        when(jwtTokenProvider.generateJwtToken(any(ProfilePrincipal.class)))
                .thenReturn("jwt-token");
        when(profileMapper.toDto(profile)).thenReturn(safeProfile);

        LoginResponse response = service().login(credentials);

        assertEquals("jwt-token", response.token());
        verify(loginAttemptService)
                .handleLoginAttempt(TEST_USERNAME, TEST_PASSWORD, profile);
    }

    @Test
    void registrationInitializesSecureAccountDefaults() {
        RegistrationRequest request = new RegistrationRequest(
                TEST_USERNAME,
                TEST_PASSWORD,
                "Fixture",
                "1990-01-01",
                "Portugal",
                "351",
                "912345678",
                "outro",
                "fixture@example.test",
                true,
                42L
        );

        when(profileRepository.findUserByUsernameIgnoreCase(TEST_USERNAME)).thenReturn(Optional.empty());
        when(profileRepository.findUserByEmail("fixture@example.test")).thenReturn(Optional.empty());
        when(passwordHashService.encode(TEST_PASSWORD)).thenReturn("bcrypt-password");
        when(profileMapper.toEntity(any(ProfileDTO.class))).thenAnswer(invocation -> {
            ProfileDTO dto = invocation.getArgument(0);
            Profile profile = new Profile();
            profile.setUserId(dto.getUserId());
            profile.setUsername(dto.getUsername());
            profile.setPassword(dto.getPassword());
            profile.setNotLocked(dto.getNotLocked());
            profile.setEnabled(dto.getEnabled());
            profile.setMFA(dto.getMFA());
            profile.setRole(dto.getRole());
            profile.setAuthorities(dto.getAuthorities());
            return profile;
        });
        when(profileRepository.saveAndFlush(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service().signUpProfile(request, null);

        verify(profileRepository).saveAndFlush(argThat(profile ->
                profile.getUserId() != null
                        && !profile.getUserId().isBlank()
                        && "bcrypt-password".equals(profile.getPassword())
                        && Boolean.TRUE.equals(profile.getNotLocked())
                        && Boolean.FALSE.equals(profile.getEnabled())
                        && Boolean.FALSE.equals(profile.getMFA())
                        && "ROLE_USER".equals(profile.getRole())
                        && profile.getAuthorities() != null
                        && profile.getAuthorities().length > 0
                        && profile.getTermsAcceptedAt() != null
                        && "2026-10-01".equals(profile.getTermsVersion())
                        && "2026-10-01".equals(profile.getPrivacyVersion())
        ));
        org.mockito.ArgumentCaptor<String> html = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq("fixture@example.test"),
                eq("Confirma o teu email | Confirm your email — Desporto24"), html.capture());
        assertTrue(html.getValue().contains("Confirmar email / Confirm email"));
        assertTrue(html.getValue().contains("15 minutos"));
        assertTrue(html.getValue().contains("15 minutes"));
        assertTrue(html.getValue().contains("https://example.test/?confirmRegistration="));
        assertTrue(html.getValue().contains("returnSessionId=42"));
    }

    @Test
    void resendConfirmationPreservesSharedSessionDestination() {
        Profile pending = new Profile();
        pending.setUsername(TEST_USERNAME);
        pending.setEnabled(false);
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername(TEST_USERNAME);
        dto.setEmail("fixture@example.test");
        when(profileRepository.findUserByUsernameIgnoreCase(TEST_USERNAME))
                .thenReturn(Optional.of(pending));
        when(profileMapper.toDto(pending)).thenReturn(dto);

        service().resendRegistrationConfirmation(TEST_USERNAME, 42L);

        verify(confirmationTokenService).deleteByProfile(TEST_USERNAME);
        org.mockito.ArgumentCaptor<String> html = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq("fixture@example.test"), anyString(), html.capture());
        assertTrue(html.getValue().contains("returnSessionId=42"));
    }

    @Test
    void legacyResendConfirmationDoesNotRequireSessionDestination() {
        Profile pending = new Profile();
        pending.setUsername(TEST_USERNAME);
        pending.setEnabled(false);
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername(TEST_USERNAME);
        dto.setEmail("fixture@example.test");
        when(profileRepository.findUserByUsernameIgnoreCase(TEST_USERNAME))
                .thenReturn(Optional.of(pending));
        when(profileMapper.toDto(pending)).thenReturn(dto);

        service().resendRegistrationConfirmation(TEST_USERNAME);

        org.mockito.ArgumentCaptor<String> html = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq("fixture@example.test"), anyString(), html.capture());
        assertTrue(html.getValue().contains("confirmRegistration="));
        assertFalse(html.getValue().contains("returnSessionId="));
    }

    @Test
    void previewRegistrationRecordsAttributedPilotEventImmediately() {
        RegistrationRequest request = new RegistrationRequest(
                TEST_USERNAME,
                TEST_PASSWORD,
                "Fixture",
                "1990-01-01",
                "Portugal",
                "351",
                "912345678",
                "outro",
                "fixture@example.test",
        true
        );

        when(profileRepository.findUserByUsernameIgnoreCase(TEST_USERNAME)).thenReturn(Optional.empty());
        when(profileRepository.findUserByEmail("fixture@example.test")).thenReturn(Optional.empty());
        when(profileMapper.toEntity(any(ProfileDTO.class))).thenAnswer(invocation -> {
            ProfileDTO dto = invocation.getArgument(0);
            Profile profile = new Profile();
            profile.setId(42L);
            profile.setUserId(dto.getUserId());
            profile.setUsername(dto.getUsername());
            profile.setPassword(dto.getPassword());
            profile.setNotLocked(dto.getNotLocked());
            profile.setEnabled(dto.getEnabled());
            profile.setMFA(dto.getMFA());
            profile.setRole(dto.getRole());
            profile.setAuthorities(dto.getAuthorities());
            return profile;
        });
        when(profileRepository.saveAndFlush(any(Profile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AuthenticationServiceImpl service = service();
        ReflectionTestUtils.setField(service, "previewMode", true);

        service.signUpProfile(request, null, "  pilot-lisbon-2026  ");

        verify(pilotEventService).recordRegistrationCompleted(
                argThat(profile -> Long.valueOf(42L).equals(profile.getId())),
                eq("  pilot-lisbon-2026  ")
        );
        verify(emailService, never()).send(anyString(), anyString());
        verify(emailService, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void confirmedRegistrationRecordsPilotEvent() {
        ConfirmationToken token = mock(ConfirmationToken.class);
        Profile profile = activeProfile(false);
        profile.setEnabled(false);
        profile.setId(42L);

        when(confirmationTokenService.getToken("registration-token"))
                .thenReturn(Optional.of(token));
        when(token.getConfirmedAt()).thenReturn(null);
        when(token.getExpiredAt()).thenReturn(LocalDateTime.now().plusMinutes(5));
        when(token.getProfile()).thenReturn(TEST_USERNAME);
        when(profileRepository.findUserByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(profile));
        when(profileRepository.save(profile)).thenReturn(profile);

        service().confirmToken("registration-token");

        assertTrue(profile.getEnabled());
        verify(pilotEventService).recordRegistrationCompleted(profile);
    }

    @Test
    void registrationRejectsUsernameThatOnlyDiffersByCase() {
        ProfileDTO candidate = safeProfile();
        candidate.setUsername("FiXtUrE-UsEr");
        candidate.setEmail("fixture@example.test");

        when(profileRepository.findUserByUsernameIgnoreCase("FiXtUrE-UsEr"))
                .thenReturn(Optional.of(activeProfile(false)));
        when(profileRepository.findUserByEmail("fixture@example.test"))
                .thenReturn(Optional.empty());

        assertThrows(
                com.example.desporto24.exception.UsernameExistException.class,
                () -> service().validateNewUsernameEmail(candidate)
        );
    }

    @Test
    void mfaEnabledLoginDoesNotGenerateJwtUntilChallengeIsConfirmed() {
        Profile profile = activeProfile(true);
        // Configured MFA uses an authenticator secret, not an SMS challenge.
        profile.setMfaSecret(AES.encryptString("JBSWY3DPEHPK3PXP", profile.getUserId()));
        when(profileRepository.findUserByUsernameIgnoreCase(TEST_USERNAME)).thenReturn(Optional.of(profile));

        LoginResponse response = service().login(credentials());

        assertTrue(response.mfaRequired());
        assertNotNull(response.challengeId());
        assertNotNull(response.challengeExpiresAt());
        assertNull(response.token());
        assertNull(response.profile());
        verify(mfaVerificationService).saveConfirmationMFA(argThat(challenge ->
                response.challengeId().equals(challenge.getChallengeId())
                        && TEST_USERNAME.equals(challenge.getPerfil())
                        && challenge.getExpiredAt() != null));
        verifyNoInteractions(jwtTokenProvider, totpService, passwordEncoder);
        verify(profileRepository, never()).save(any(Profile.class));
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
                profileMapper,
                totpService,
                pilotEventService
        );
        ReflectionTestUtils.setField(service, "uiAppUrl", "https://example.test");
        return service;
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
