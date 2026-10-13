package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.session.SessionExperienceRatingRequest;
import com.example.desporto24.model.dto.session.SessionExperienceRatingResponse;
import com.example.desporto24.service.SessionExperienceRatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionExperienceRatingControllerTest {
    @Mock private SessionExperienceRatingService service;
    @Mock private Authentication authentication;
    @InjectMocks private SessionExperienceRatingController controller;

    @Test
    void ratingUsesAuthenticatedProfile() {
        when(authentication.getName()).thenReturn("miguel");
        var request = new SessionExperienceRatingRequest(5);
        var expected = new SessionExperienceRatingResponse(5, Instant.now());
        when(service.rate(7L, "miguel", request)).thenReturn(expected);

        assertSame(expected, controller.rate(7L, request, authentication));
        verify(service).rate(7L, "miguel", request);
    }

    @Test
    void absentPersonalRatingReturnsNoContent() {
        when(authentication.getName()).thenReturn("miguel");
        when(service.mine(7L, "miguel")).thenReturn(Optional.empty());

        var response = controller.mine(7L, authentication);

        assertEquals(204, response.getStatusCode().value());
        verify(service).mine(7L, "miguel");
    }
}
