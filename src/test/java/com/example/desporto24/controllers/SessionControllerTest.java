package com.example.desporto24.controllers;

import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.SessionOutcome;
import com.example.desporto24.model.dto.session.CreateSessionRequest;
import com.example.desporto24.model.dto.session.MySessionsResponse;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.model.dto.session.SessionParticipantResponse;
import com.example.desporto24.model.dto.session.ReportSessionOutcomeRequest;
import com.example.desporto24.enumeration.AttendanceStatus;
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
    void discoverUsesAuthenticatedIdentityWhenAvailable() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("miguel");
        when(sessionService.discover(any(), eq("miguel"))).thenReturn(List.of());

        List<?> response = controller.discover(
                null, null, null, null, null, null, null,
                null, null, null, null, null, null, authentication
        );

        assertTrue(response.isEmpty());
        verify(sessionService).discover(any(), eq("miguel"));
        verify(sessionService, never()).discover(any());
    }

    @Test
    void anonymousDiscoverRemainsPublicAndDoesNotRecordIdentity() {
        when(sessionService.discover(any())).thenReturn(List.of());

        List<?> response = controller.discover(
                null, null, null, null, null, null, null,
                null, null, null, null, null, null, null
        );

        assertTrue(response.isEmpty());
        verify(sessionService).discover(any());
        verify(sessionService, never()).discover(any(), anyString());
    }

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
    void seriesUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");
        when(sessionService.series(1L, "miguel")).thenReturn(List.of());

        List<?> response = controller.series(1L, authentication);

        assertTrue(response.isEmpty());
        verify(sessionService).series(1L, "miguel");
    }

    @Test
    void participantsUsesAuthenticatedIdentity() {
        SessionParticipantResponse participant = new SessionParticipantResponse(
                5L,
                "player",
                AttendanceStatus.PENDING,
                Instant.parse("2026-09-25T18:00:00Z")
        );
        when(authentication.getName()).thenReturn("miguel");
        when(sessionService.participants(1L, "miguel")).thenReturn(List.of(participant));

        List<SessionParticipantResponse> response = controller.participants(1L, authentication);

        assertEquals(List.of(participant), response);
        verify(sessionService).participants(1L, "miguel");
    }

    @Test
    void reportOutcomeUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");

        ResponseEntity<Void> response = controller.reportOutcome(
                1L,
                new ReportSessionOutcomeRequest(SessionOutcome.OCCURRED),
                authentication
        );

        assertEquals(204, response.getStatusCode().value());
        verify(sessionService).reportOutcome(1L, SessionOutcome.OCCURRED, "miguel");
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
