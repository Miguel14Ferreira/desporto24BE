package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.model.dto.auth.MfaResendRequest;
import com.example.desporto24.model.dto.auth.PasswordResetInitiateRequest;
import com.example.desporto24.model.dto.auth.PasswordResetConfirmRequest;
import com.example.desporto24.model.dto.auth.RegistrationRequest;
import com.example.desporto24.service.interfaces.AuthenticationService;
import com.example.desporto24.security.SensitiveActionRateLimiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
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

    @InjectMocks
    private AuthenticationController controller;

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
                "miguel@example.com"
        );

        var response = controller.registerUser(request, null);

        assertEquals(OK, response.getStatusCode());
        org.mockito.Mockito.verify(authenticationService).signUpProfile(request, null);
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
    void passwordResetConfirmUsesTokenAndNewPasswordFromBody() {
        PasswordResetConfirmRequest request =
                new PasswordResetConfirmRequest("reset-token", "new-password-123");

        var response = controller.resetPassword(request);

        assertEquals(OK, response.getStatusCode());
        org.mockito.Mockito.verify(authenticationService)
                .resetPasswordStepTwo("new-password-123", "reset-token");
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
