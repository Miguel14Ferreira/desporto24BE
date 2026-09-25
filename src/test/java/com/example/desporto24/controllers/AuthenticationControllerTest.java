package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.service.interfaces.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController controller;

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
