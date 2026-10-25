package com.example.desporto24.controllers;

import com.example.desporto24.exception.EmailNotFoundException;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.model.dto.auth.MfaResendRequest;
import com.example.desporto24.model.dto.auth.PasswordResetInitiateRequest;
import com.example.desporto24.model.dto.auth.PasswordResetConfirmRequest;
import com.example.desporto24.model.dto.auth.RegistrationRequest;
import com.example.desporto24.service.interfaces.AuthenticationService;
import com.example.desporto24.security.SensitiveActionRateLimiter;
import com.example.desporto24.security.RefreshOriginValidator;
import com.example.desporto24.security.RefreshTokenRotationService;
import com.example.desporto24.utility.JWTokenProvider;
import com.example.desporto24.model.dto.auth.DeviceSessionResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private Authentication authentication;

    @Mock
    private SensitiveActionRateLimiter sensitiveActionRateLimiter;

    @Mock
    private RefreshOriginValidator refreshOriginValidator;

    @Mock private RefreshTokenRotationService refreshTokenRotationService;
    @Mock private JWTokenProvider jwtTokens;

    @InjectMocks
    private AuthenticationController controller;

    @Test
    void refreshRejectsUnexpectedOriginBeforeLookingUpRefreshCookie() {
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN))
                .when(refreshOriginValidator).requireAllowedOrigin("https://attacker.example");

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.refresh("stolen-or-cross-site-cookie", "https://attacker.example")
        );
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verifyNoInteractions(authenticationService);
    }

    @Test
    void refreshWithExpectedOriginContinuesToRotateSecureCookie() {
        ProfileDTO profile = credentials();
        when(authenticationService.refresh("old-token"))
                .thenReturn(new LoginResponse(false, null, null, "jwt", profile));
        when(authenticationService.createRefreshToken("miguel")).thenReturn("new-token");

        ResponseEntity<LoginResponse> response = controller.refresh("old-token", "https://d24sports.com");

        assertEquals(OK, response.getStatusCode());
        verify(refreshOriginValidator).requireAllowedOrigin("https://d24sports.com");
        verify(authenticationService).refresh("old-token");
        verify(refreshTokenRotationService).rotate("old-token", "new-token", "miguel");
        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookie);
        assertTrue(cookie.contains("HttpOnly"));
        assertTrue(cookie.contains("Secure"));
        assertTrue(cookie.contains("SameSite=None"));
        verify(refreshTokenRotationService).rotate("old-token", "new-token", "miguel");
    }

    @Test
    void replayRejectionDoesNotReturnAnAccessTokenOrCookie() {
        ProfileDTO profile = credentials();
        when(authenticationService.refresh("replayed-cookie"))
                .thenReturn(new LoginResponse(false, null, null, "new-access-jwt", profile));
        when(authenticationService.createRefreshToken("miguel")).thenReturn("candidate");
        doThrow(new org.springframework.security.authentication.BadCredentialsException("Already used"))
                .when(refreshTokenRotationService).rotate("replayed-cookie", "candidate", "miguel");

        assertThrows(org.springframework.security.authentication.BadCredentialsException.class,
                () -> controller.refresh("replayed-cookie", "https://d24sports.com"));
    }

    @Test
    void refreshWithoutCookieIsStillRejectedAfterOriginValidation() {
        assertThrows(org.springframework.security.authentication.BadCredentialsException.class,
                () -> controller.refresh(null, "https://d24sports.com"));
        verify(refreshOriginValidator).requireAllowedOrigin("https://d24sports.com");
        verifyNoInteractions(authenticationService);
    }

    @Test
    void registrationUsesWhitelistedRequest() {
        RegistrationRequest request = new RegistrationRequest(
                "miguel",
                "password-123",
                "Miguel",
                "1990-01-01",
                "Portugal",
                "351",
                "912345678",
                "masculino",
                "miguel@example.com",
                true
        );

        var response = controller.registerUser(request, null, "pilot-lisbon-2026");

        assertEquals(OK, response.getStatusCode());
        org.mockito.Mockito.verify(authenticationService)
                .signUpProfile(request, null, "pilot-lisbon-2026");
    }

    @Test
    void resendConfirmationForwardsOptionalSessionDestination() {
        var response = controller.resendRegistrationConfirmation("miguel", 42L);

        assertEquals(OK, response.getStatusCode());
        org.mockito.Mockito.verify(authenticationService)
                .resendRegistrationConfirmation("miguel", 42L, null);
    }

    @Test
    void resendConfirmationForwardsCommunityDestination() {
        var response = controller.resendRegistrationConfirmation("miguel", null, 42L);
        assertEquals(OK, response.getStatusCode());
        org.mockito.Mockito.verify(authenticationService)
                .resendRegistrationConfirmation("miguel", null, 42L);
    }

    @Test
    void resendConfirmationSupportsLegacyCallersWithoutSessionDestination() {
        var response = controller.resendRegistrationConfirmation("miguel");

        assertEquals(OK, response.getStatusCode());
        org.mockito.Mockito.verify(authenticationService)
                .resendRegistrationConfirmation("miguel", null, null);
    }

    @Test
    void passwordResetInitiateUsesEmailFromTypedRequest() {
        PasswordResetInitiateRequest request =
                new PasswordResetInitiateRequest("miguel@example.com");

        var response = controller.initiatePasswordReset(request);

        assertEquals(OK, response.getStatusCode());
        org.mockito.Mockito.verify(authenticationService)
                .resetPasswordStepOne("miguel@example.com");
    }

    @Test
    void passwordResetInitiateDoesNotRevealUnknownEmail() {
        PasswordResetInitiateRequest request =
                new PasswordResetInitiateRequest("unknown@example.com");

        org.mockito.Mockito.doThrow(new EmailNotFoundException("not found"))
                .when(authenticationService)
                .resetPasswordStepOne("unknown@example.com");

        var response = controller.initiatePasswordReset(request);

        assertEquals(OK, response.getStatusCode());
        org.mockito.Mockito.verify(authenticationService)
                .resetPasswordStepOne("unknown@example.com");
    }

    @Test
    void passwordResetConfirmUsesTokenAndNewPasswordFromBody() {
        PasswordResetConfirmRequest request =
                new PasswordResetConfirmRequest("reset-token", "new-password-123");

        var response = controller.resetPassword(request);

        assertEquals(OK, response.getStatusCode());
        org.mockito.Mockito.verify(authenticationService)
                .resetPasswordStepTwo("new-password-123", "reset-token");
    }

    @Test
    void deviceListingUsesOnlyAuthenticatedIdentityAndBoundJwt() {
        when(authentication.getName()).thenReturn("miguel");
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtTokens.getAccessTokenFamilyId("access")).thenReturn("family-123");
        var sessions = List.of(new DeviceSessionResponse(
                "family-123", "Windows · Chrome", Instant.now(), Instant.now().plusSeconds(300), true));
        when(refreshTokenRotationService.activeSessions("miguel", "family-123"))
                .thenReturn(sessions);

        assertEquals(sessions,
                controller.listDeviceSessions(authentication, "Bearer access"));
    }

    @Test
    void deviceRevocationChecksOriginAndDoesNotRequireAllDeviceLogout() {
        when(authentication.getName()).thenReturn("miguel");
        when(authentication.isAuthenticated()).thenReturn(true);
        var result = controller.revokeDeviceSession(
                "other-family", authentication, "https://d24sports.com", "Bearer access");

        assertEquals(204, result.getStatusCode().value());
        assertNull(result.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        verify(refreshOriginValidator).requireAllowedOrigin("https://d24sports.com");
        verify(refreshTokenRotationService).revokeDevice("miguel", "other-family");
    }

    @Test
    void currentDeviceRevocationClearsRefreshCookie() {
        when(authentication.getName()).thenReturn("miguel");
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtTokens.getAccessTokenFamilyId("access")).thenReturn("current-family");

        var response = controller.revokeDeviceSession("current-family", authentication,
                "https://d24sports.com", "Bearer access");

        assertEquals(204, response.getStatusCode().value());
        assertTrue(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE).contains("Max-Age=0"));
    }

    @Test
    void revokeOtherDevicesKeepsCurrentCookieAndRequiresCurrentFamily() {
        when(authentication.getName()).thenReturn("miguel");
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtTokens.getAccessTokenFamilyId("access")).thenReturn("bound-current");

        var result = controller.revokeOtherDeviceSessions(
                authentication, "https://d24sports.com", "Bearer access");

        assertEquals(204, result.getStatusCode().value());
        assertNull(result.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        verify(refreshOriginValidator).requireAllowedOrigin("https://d24sports.com");
        verify(refreshTokenRotationService).revokeOtherDevices("miguel", "bound-current");
    }

    @Test
    void unauthenticatedCallerCannotRevokeOtherDevices() {
        assertThrows(ResponseStatusException.class,
                () -> controller.revokeOtherDeviceSessions(
                        null, "https://d24sports.com", "Bearer token"));
        verifyNoInteractions(refreshTokenRotationService);
    }

    @Test
    void revokeOtherDevicesRejectsUntrustedOrigin() {
        when(authentication.isAuthenticated()).thenReturn(true);
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN))
                .when(refreshOriginValidator).requireAllowedOrigin("https://attacker.invalid");

        assertThrows(ResponseStatusException.class,
                () -> controller.revokeOtherDeviceSessions(
                        authentication, "https://attacker.invalid", "Bearer token"));
        verifyNoInteractions(refreshTokenRotationService);
    }

    @Test
    void unauthenticatedCallerCannotListOrRevokeSessions() {
        assertThrows(ResponseStatusException.class,
                () -> controller.listDeviceSessions(null, null));
        assertThrows(ResponseStatusException.class,
                () -> controller.revokeDeviceSession(
                        "any", null, "https://d24sports.com", "Bearer token"));
        verifyNoInteractions(refreshTokenRotationService);
    }

    @Test
    void logoutUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        var response = controller.terminateSession(authentication);

        assertEquals(204, response.getStatusCode().value());
        org.mockito.Mockito.verify(authenticationService).terminateSession("miguel");
    }

    @Test
    void resendMfaReturnsFreshChallenge() {
        Instant expiresAt = Instant.parse("2026-09-25T12:05:00Z");
        LoginResponse challenge = new LoginResponse(true, "challenge-456", expiresAt, null, null);
        when(authenticationService.resendMfa("challenge-123")).thenReturn(challenge);

        ResponseEntity<LoginResponse> response = controller.resendMfaToken(
                new MfaResendRequest("challenge-123")
        );

        assertEquals(ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("challenge-456", response.getBody().challengeId());
        assertNull(response.getBody().token());
    }

    @Test
    void mfaDisabledLoginReturnsTokenImmediately() {
        ProfileDTO credentials = credentials();
        LoginResponse login = new LoginResponse(false, null, null, "jwt-token", credentials);
        when(authenticationService.login(credentials)).thenReturn(login);

        ResponseEntity<LoginResponse> response = controller.login(credentials);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().mfaRequired());
        assertEquals("jwt-token", response.getBody().token());
    }

    @Test
    void mfaEnabledLoginReturnsChallengeWithoutToken() {
        ProfileDTO credentials = credentials();
        Instant expiresAt = Instant.parse("2026-09-25T12:00:00Z");
        LoginResponse login = new LoginResponse(true, "challenge-123", expiresAt, null, credentials);
        when(authenticationService.login(credentials)).thenReturn(login);

        ResponseEntity<LoginResponse> response = controller.login(credentials);

        assertEquals(ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().mfaRequired());
        assertEquals("challenge-123", response.getBody().challengeId());
        assertNull(response.getBody().token());
        assertNull(response.getHeaders().getFirst("Jwt-Token"));
    }

    private ProfileDTO credentials() {
        ProfileDTO credentials = new ProfileDTO();
        credentials.setUsername("miguel");
        credentials.setPassword("secret");
        return credentials;
    }
}
