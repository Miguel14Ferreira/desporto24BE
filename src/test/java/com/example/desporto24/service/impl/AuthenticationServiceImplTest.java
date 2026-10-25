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
import com.example.desporto24.security.DistributedAuthRateLimiter;
import java.time.Duration;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
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
                eq("Recupera a tua palavra-passe | Reset your password — Jynlo"), html.capture());
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
    void unknownLoginNameUsesGenericFailureAndCountsTowardsThrottle() {
        ProfileDTO credentials = credentials();
        credentials.setUsername("  missing-user  ");
        when(profileRepository.findUserByUsernameIgnoreCase("missing-user"))
                .thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> service().login(credentials));
        verify(loginAttemptService).requireAttemptAllowed("missing-user");
        verify(loginAttemptService).recordMissingAccountAttempt("missing-user");
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void blockedNameIsRejectedBeforeDatabaseLookup() {
        ProfileDTO credentials = credentials();
        org.mockito.Mockito.doThrow(new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.TOO_MANY_REQUESTS))
                .when(loginAttemptService).requireAttemptAllowed(TEST_USERNAME);

        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> service().login(credentials));
        verify(profileRepository, never()).findUserByUsernameIgnoreCase(anyString());
        verifyNoInteractions(jwtTokenProvider);
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
    void passwordLoginAcceptsCaseInsensitiveEmailAndUsesCanonicalUsernameForRateLimits() {
        Profile profile = activeProfile(false);
        profile.setEmail("fixture@example.test");
        ProfileDTO input = credentials();
        input.setUsername("FiXtUrE@ExAmPlE.TeSt");
        when(profileRepository.findTop2ByEmailIgnoreCase("FiXtUrE@ExAmPlE.TeSt"))
                .thenReturn(java.util.List.of(profile));
        when(profileRepository.save(profile)).thenReturn(profile);
        when(jwtTokenProvider.generateJwtToken(any(ProfilePrincipal.class))).thenReturn("token-email");
        when(profileMapper.toDto(profile)).thenReturn(safeProfile());

        LoginResponse result = service().login(input);

        assertEquals("token-email", result.token());
        verify(loginAttemptService).requireAttemptAllowed("FiXtUrE@ExAmPlE.TeSt");
        verify(loginAttemptService).handleLoginAttempt(TEST_USERNAME, TEST_PASSWORD, profile);
        verify(profileRepository, never()).findAll(any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void phoneLoginAcceptsNationalAndInternationalFormattingOfEncryptedNumber() {
        for (String phone : new String[]{"912345678", "912 345 678", "+351 912 345 678",
                "00351 912 345 678"}) {
            reset(profileRepository, jwtTokenProvider, profileMapper, loginAttemptService);
            Profile profile = activeProfile(false);
            ProfileDTO input = credentials();
            input.setUsername(phone);
            when(profileRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                    .thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.List.of(profile)));
            when(profileRepository.save(profile)).thenReturn(profile);
            when(jwtTokenProvider.generateJwtToken(any(ProfilePrincipal.class))).thenReturn("token-phone");
            when(profileMapper.toDto(profile)).thenReturn(safeProfile());

            LoginResponse result = service().login(input);

            assertEquals("token-phone", result.token(), phone);
            verify(loginAttemptService).requireAttemptAllowed(phone);
            verify(loginAttemptService).handleLoginAttempt(TEST_USERNAME, TEST_PASSWORD, profile);
        }
    }

    @Test
    void ambiguousPhoneAcrossCountryCodesCannotSelectAnAccount() {
        Profile portugal = activeProfile(false);
        portugal.setId(1L);
        Profile abroad = activeProfile(false);
        abroad.setId(2L);
        abroad.setUsername("different-user");
        abroad.setIndicativePhone("44");
        ProfileDTO input = credentials();
        input.setUsername("912345678");
        when(profileRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(
                        java.util.List.of(portugal, abroad)));

        assertThrows(BadCredentialsException.class, () -> service().login(input));

        verifyNoInteractions(jwtTokenProvider);
        verify(loginAttemptService, never()).handleLoginAttempt(anyString(), anyString(), any());
    }

    @Test
    void phoneLikeUsernameCannotSilentlyAuthenticateAnotherAccountsPhone() {
        Profile usernameOwner = activeProfile(false);
        usernameOwner.setId(1L);
        usernameOwner.setUsername("912345678");
        usernameOwner.setPhone(null);
        Profile phoneOwner = activeProfile(false);
        phoneOwner.setId(2L);
        ProfileDTO input = credentials();
        input.setUsername("912345678");
        when(profileRepository.findUserByUsernameIgnoreCase("912345678"))
                .thenReturn(Optional.of(usernameOwner));
        when(profileRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(
                        java.util.List.of(phoneOwner)));

        assertThrows(BadCredentialsException.class, () -> service().login(input));

        verifyNoInteractions(jwtTokenProvider);
        verify(loginAttemptService, never()).handleLoginAttempt(anyString(), anyString(), any());
    }

    @Test
    void emailLikeUsernameCollisionFailsClosedWithoutIssuingJwt() {
        Profile usernameOwner = activeProfile(false);
        usernameOwner.setId(1L);
        Profile emailOwner = activeProfile(false);
        emailOwner.setId(2L);
        ProfileDTO input = credentials();
        input.setUsername("someone@example.test");
        when(profileRepository.findUserByUsernameIgnoreCase("someone@example.test"))
                .thenReturn(Optional.of(usernameOwner));
        when(profileRepository.findTop2ByEmailIgnoreCase("someone@example.test"))
                .thenReturn(java.util.List.of(emailOwner));

        assertThrows(BadCredentialsException.class, () -> service().login(input));

        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void legacyCaseFoldedDuplicateEmailNeverAuthenticatesArbitrarily() {
        Profile first = activeProfile(false);
        first.setId(1L);
        Profile second = activeProfile(false);
        second.setId(2L);
        second.setUsername("second-account");
        ProfileDTO input = credentials();
        input.setUsername("fixture@example.test");
        when(profileRepository.findTop2ByEmailIgnoreCase("fixture@example.test"))
                .thenReturn(java.util.List.of(first, second));

        assertThrows(BadCredentialsException.class, () -> service().login(input));

        verifyNoInteractions(jwtTokenProvider);
        verify(loginAttemptService, never()).handleLoginAttempt(anyString(), anyString(), any());
    }

    @Test
    void unknownEmailAndUnknownPhoneCountAgainstLoginAttemptLimiter() {
        for (String unknown : new String[]{"nobody@example.test", "+447700900999"}) {
            reset(profileRepository, loginAttemptService);
            ProfileDTO input = credentials();
            input.setUsername(unknown);
            if (unknown.startsWith("+")) {
                when(profileRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                        .thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.List.of()));
            }
            assertThrows(BadCredentialsException.class, () -> service().login(input));
            verify(loginAttemptService).requireAttemptAllowed(unknown);
            verify(loginAttemptService).recordMissingAccountAttempt(unknown);
            verifyNoInteractions(jwtTokenProvider);
        }
    }

    @Test
    void emailLoginPreservesExistingMfaChallengeWithoutIssuingJwt() {
        Profile profile = activeProfile(true);
        profile.setMfaSecret(AES.encryptString("JBSWY3DPEHPK3PXP", profile.getUserId()));
        ProfileDTO input = credentials();
        input.setUsername("fixture@example.test");
        when(profileRepository.findTop2ByEmailIgnoreCase("fixture@example.test"))
                .thenReturn(java.util.List.of(profile));

        LoginResponse result = service().login(input);

        assertTrue(result.mfaRequired());
        assertNull(result.token());
        verify(mfaVerificationService).saveConfirmationMFA(any(MFAVerification.class));
        verify(loginAttemptService).handleLoginAttempt(TEST_USERNAME, TEST_PASSWORD, profile);
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void incorrectCountryCodeCannotMatchUnrelatedEncryptedPhone() {
        ProfileDTO input = credentials();
        input.setUsername("+44912345678");
        when(profileRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(
                        java.util.List.of(activeProfile(false))));

        assertThrows(BadCredentialsException.class, () -> service().login(input));
        verify(loginAttemptService).recordMissingAccountAttempt("+44912345678");
        verifyNoInteractions(jwtTokenProvider);
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
                eq("Confirma o teu email | Confirm your email — Jynlo"), html.capture());
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
    void resendConfirmationCarriesCommunityInviteAcrossDevices() {
        Profile pending = new Profile();
        pending.setUsername(TEST_USERNAME);
        pending.setEnabled(false);
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername(TEST_USERNAME);
        dto.setEmail("fixture@example.test");
        when(profileRepository.findUserByUsernameIgnoreCase(TEST_USERNAME))
                .thenReturn(Optional.of(pending));
        when(profileMapper.toDto(pending)).thenReturn(dto);

        service().resendRegistrationConfirmation(TEST_USERNAME, null, 42L);

        verify(confirmationTokenService).deleteByProfile(TEST_USERNAME);
        org.mockito.ArgumentCaptor<String> html = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq("fixture@example.test"), anyString(), html.capture());
        assertTrue(html.getValue().contains("returnCommunityId=42"));
        assertFalse(html.getValue().contains("returnSessionId="));
    }

    @Test
    void sessionLinkTakesPrecedenceOverCommunityLink() {
        Profile pending = new Profile();
        pending.setUsername(TEST_USERNAME);
        pending.setEnabled(false);
        ProfileDTO dto = new ProfileDTO();
        dto.setUsername(TEST_USERNAME);
        dto.setEmail("fixture@example.test");
        when(profileRepository.findUserByUsernameIgnoreCase(TEST_USERNAME))
                .thenReturn(Optional.of(pending));
        when(profileMapper.toDto(pending)).thenReturn(dto);

        service().resendRegistrationConfirmation(TEST_USERNAME, 7L, 42L);

        org.mockito.ArgumentCaptor<String> html = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(emailService).send(eq("fixture@example.test"), anyString(), html.capture());
        assertTrue(html.getValue().contains("returnSessionId=7"));
        assertFalse(html.getValue().contains("returnCommunityId="));
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
    void passwordLoginWithEnabledMfaButMissingSecretFailsClosed() {
        Profile profile = activeProfile(true);
        profile.setMfaSecret(null);
        when(profileRepository.findUserByUsernameIgnoreCase(TEST_USERNAME))
                .thenReturn(Optional.of(profile));

        ResponseStatusException denied = assertThrows(ResponseStatusException.class,
                () -> service().login(credentials()));

        assertEquals(HttpStatus.FORBIDDEN, denied.getStatusCode());
        assertTrue(profile.getMFA(), "A missing secret must NEVER disable MFA");
        verify(profileRepository, never()).save(any(Profile.class));
        verifyNoInteractions(jwtTokenProvider, mfaVerificationService);
    }

    @Test
    void passwordLoginWithEnabledMfaButBlankSecretFailsClosed() {
        Profile profile = activeProfile(true);
        profile.setMfaSecret("   ");
        when(profileRepository.findUserByUsernameIgnoreCase(TEST_USERNAME))
                .thenReturn(Optional.of(profile));

        ResponseStatusException denied = assertThrows(ResponseStatusException.class,
                () -> service().login(credentials()));

        assertEquals(HttpStatus.FORBIDDEN, denied.getStatusCode());
        assertTrue(profile.getMFA());
        verify(profileRepository, never()).save(any(Profile.class));
        verifyNoInteractions(jwtTokenProvider, mfaVerificationService);
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
        Profile profile = activeProfile(true);
        String totpSecret = "JBSWY3DPEHPK3PXP";
        profile.setMfaSecret(AES.encryptString(totpSecret, profile.getUserId()));

        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID)).thenReturn(Optional.of(challenge));
        when(profileRepository.findUserByUsername(TEST_USERNAME)).thenReturn(Optional.of(profile));
        when(totpService.verify(totpSecret, "wrong")).thenReturn(false);

        assertThrows(
                BadCredentialsException.class,
                () -> service().confirmMfa(new MfaConfirmRequest(TEST_CHALLENGE_ID, "wrong"))
        );

        verify(totpService).verify(totpSecret, "wrong");
        verifyNoInteractions(jwtTokenProvider);
        verify(mfaVerificationService, never()).confirmIfPending(anyString(), any());
    }

    @Test
    void distributedMfaLimitBlocksBeforeEvenCheckingAuthenticatorCode() {
        MFAVerification challenge = challenge(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        Profile profile = activeProfile(true);
        profile.setMfaSecret(AES.encryptString("JBSWY3DPEHPK3PXP", profile.getUserId()));
        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID))
                .thenReturn(Optional.of(challenge));
        when(profileRepository.findUserByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(profile));
        DistributedAuthRateLimiter shared = mock(DistributedAuthRateLimiter.class);
        doThrow(new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS))
                .when(shared).reserveAttempt("mfa-confirm", TEST_USERNAME, 5, Duration.ofMinutes(15));
        AuthenticationServiceImpl instance = service();
        ReflectionTestUtils.setField(instance, "distributedAttempts", shared);

        ResponseStatusException blocked = assertThrows(ResponseStatusException.class,
                () -> instance.confirmMfa(new MfaConfirmRequest(TEST_CHALLENGE_ID, "123456")));

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, blocked.getStatusCode());
        verifyNoInteractions(totpService, jwtTokenProvider);
        verify(mfaVerificationService, never()).confirmIfPending(anyString(), any());
    }

    @Test
    void mfaChallengeRejectsSixthGuessWithoutCallingTotp() {
        MFAVerification challenge = challenge(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        Profile profile = activeProfile(true);
        String secret = "JBSWY3DPEHPK3PXP";
        profile.setMfaSecret(AES.encryptString(secret, profile.getUserId()));
        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID))
                .thenReturn(Optional.of(challenge));
        when(profileRepository.findUserByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(profile));
        when(totpService.verify(secret, "wrong")).thenReturn(false);

        AuthenticationServiceImpl instance = service();
        for (int i = 0; i < 5; i++) {
            assertThrows(BadCredentialsException.class,
                    () -> instance.confirmMfa(new MfaConfirmRequest(TEST_CHALLENGE_ID, "wrong")));
        }
        ResponseStatusException blocked = assertThrows(ResponseStatusException.class,
                () -> instance.confirmMfa(new MfaConfirmRequest(TEST_CHALLENGE_ID, "wrong")));
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, blocked.getStatusCode());
        verify(totpService, times(5)).verify(secret, "wrong");
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void newMfaChallengeCannotBypassTheSameAccountsFiveGuessBudget() {
        MFAVerification first = challenge(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        MFAVerification second = challenge(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        second.setChallengeId("another-challenge");
        Profile profile = activeProfile(true);
        String secret = "JBSWY3DPEHPK3PXP";
        profile.setMfaSecret(AES.encryptString(secret, profile.getUserId()));
        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID))
                .thenReturn(Optional.of(first));
        when(mfaVerificationService.getByChallengeId("another-challenge"))
                .thenReturn(Optional.of(second));
        when(profileRepository.findUserByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(profile));
        when(totpService.verify(secret, "wrong")).thenReturn(false);

        AuthenticationServiceImpl instance = service();
        for (int i = 0; i < 3; i++) {
            assertThrows(BadCredentialsException.class,
                    () -> instance.confirmMfa(new MfaConfirmRequest(TEST_CHALLENGE_ID, "wrong")));
        }
        for (int i = 0; i < 2; i++) {
            assertThrows(BadCredentialsException.class,
                    () -> instance.confirmMfa(new MfaConfirmRequest("another-challenge", "wrong")));
        }
        ResponseStatusException blocked = assertThrows(ResponseStatusException.class,
                () -> instance.confirmMfa(new MfaConfirmRequest("another-challenge", "wrong")));

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, blocked.getStatusCode());
        verify(totpService, times(5)).verify(secret, "wrong");
        verifyNoInteractions(jwtTokenProvider);
        verify(mfaVerificationService, never()).confirmIfPending(anyString(), any());
    }

    @Test
    void resendingMfaChallengeConsumesOldChallengeAndCreatesNewOne() {
        MFAVerification first = challenge(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        Profile profile = activeProfile(true);
        profile.setMfaSecret(AES.encryptString("JBSWY3DPEHPK3PXP", profile.getUserId()));
        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID))
                .thenReturn(Optional.of(first));
        when(profileRepository.findUserByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(profile));
        when(mfaVerificationService.confirmIfPending(eq(TEST_CHALLENGE_ID), any()))
                .thenReturn(true);

        LoginResponse rotated = service().resendMfa(TEST_CHALLENGE_ID);

        assertTrue(rotated.mfaRequired());
        assertNotNull(rotated.challengeId());
        assertNotEquals(TEST_CHALLENGE_ID, rotated.challengeId());
        assertNull(rotated.token());
        verify(mfaVerificationService).confirmIfPending(eq(TEST_CHALLENGE_ID), any());
        verify(mfaVerificationService).saveConfirmationMFA(argThat(challenge ->
                rotated.challengeId().equals(challenge.getChallengeId())));
    }

    @Test
    void differentResendChallengeIdDoesNotResetAccountCooldown() {
        MFAVerification first = challenge(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        MFAVerification second = challenge(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        second.setChallengeId("another-challenge");
        Profile profile = activeProfile(true);
        profile.setMfaSecret(AES.encryptString("JBSWY3DPEHPK3PXP", profile.getUserId()));
        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID))
                .thenReturn(Optional.of(first));
        when(mfaVerificationService.getByChallengeId("another-challenge"))
                .thenReturn(Optional.of(second));
        when(profileRepository.findUserByUsername(TEST_USERNAME))
                .thenReturn(Optional.of(profile));
        when(mfaVerificationService.confirmIfPending(eq(TEST_CHALLENGE_ID), any()))
                .thenReturn(true);
        AuthenticationServiceImpl instance = service();

        instance.resendMfa(TEST_CHALLENGE_ID);
        ResponseStatusException blocked = assertThrows(ResponseStatusException.class,
                () -> instance.resendMfa("another-challenge"));

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, blocked.getStatusCode());
        verify(mfaVerificationService, times(1)).saveConfirmationMFA(any(MFAVerification.class));
        verify(mfaVerificationService, times(1)).confirmIfPending(anyString(), any());
    }

    @Test
    void alreadyConfirmedMfaChallengeCannotBeResent() {
        MFAVerification used = challenge(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5));
        used.setConfirmedAt(LocalDateTime.now(ZoneOffset.UTC));
        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID))
                .thenReturn(Optional.of(used));

        assertThrows(SMSAlreadyConfirmedException.class, () -> service().resendMfa(TEST_CHALLENGE_ID));

        verifyNoInteractions(profileRepository);
        verify(mfaVerificationService, never()).saveConfirmationMFA(any(MFAVerification.class));
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
        String totpSecret = "JBSWY3DPEHPK3PXP";
        profile.setMfaSecret(AES.encryptString(totpSecret, profile.getUserId()));
        ProfileDTO safeProfile = safeProfile();

        when(mfaVerificationService.getByChallengeId(TEST_CHALLENGE_ID)).thenReturn(Optional.of(challenge));
        when(totpService.verify(totpSecret, TEST_MFA_CODE)).thenReturn(true);
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
        verify(totpService, times(2)).verify(totpSecret, TEST_MFA_CODE);
        verify(mfaVerificationService, times(2)).confirmIfPending(eq(TEST_CHALLENGE_ID), any());
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
