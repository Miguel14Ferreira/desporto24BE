package com.example.desporto24.controllers;

import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.dto.session.CreateSessionRequest;
import com.example.desporto24.model.dto.session.MySessionsResponse;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.service.interfaces.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @Mock private SessionService sessionService;
    @Mock private Authentication authentication;

    @InjectMocks private SessionController controller;

    @Test
    void createUsesAuthenticatedIdentityInsteadOfClientUsername() {
        Instant gameDate = Instant.parse("2026-10-01T19:00:00Z");
        CreateSessionRequest request = new CreateSessionRequest(
                "Futebol", gameDate, "Cacem", "Rua do Campo",
                10, BigDecimal.valueOf(5), false, null, 38.76, -9.30
        );
        SessionDetailResponse detail = detail("Rua do Campo", true);

        when(authentication.getName()).thenReturn("miguel");
        when(sessionService.create(request, "miguel")).thenReturn(detail);

        ResponseEntity<SessionDetailResponse> response = controller.create(request, authentication);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(detail, response.getBody());
        verify(sessionService).create(request, "miguel");
    }

    @Test
    void mineUsesAuthenticatedIdentity() {
        MySessionsResponse result = new MySessionsResponse(List.of(), List.of(), List.of());
        when(authentication.getName()).thenReturn("miguel");
        when(sessionService.mine("miguel")).thenReturn(result);

        MySessionsResponse response = controller.mine(authentication);

        assertSame(result, response);
        verify(sessionService).mine("miguel");
        verifyNoMoreInteractions(sessionService);
    }

    private SessionDetailResponse detail(String address, boolean canSeeExactAddress) {
        return new SessionDetailResponse(
                1L,
                "Futebol",
                "Cacem",
                Instant.parse("2026-10-01T19:00:00Z"),
                null,
                1,
                10,
                BigDecimal.valueOf(5),
                false,
                SessionStatus.OPEN,
                "miguel",
                address,
                canSeeExactAddress,
                true,
                false,
                null,
                null
        );
    }
}
