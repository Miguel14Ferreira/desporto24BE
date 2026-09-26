package com.example.desporto24.controllers;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.DisputeStatus;
import com.example.desporto24.model.dto.reliability.AttendanceDisputeRequest;
import com.example.desporto24.model.dto.reliability.AttendanceUpdateRequest;
import com.example.desporto24.model.dto.reliability.DisputeResolutionRequest;
import com.example.desporto24.model.dto.reliability.ReliabilitySummaryResponse;
import com.example.desporto24.model.dto.reliability.AttendanceHistoryResponse;
import com.example.desporto24.model.dto.reliability.AttendanceDisputeResponse;
import com.example.desporto24.service.ReliabilityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReliabilityControllerTest {

    @Mock private ReliabilityService reliabilityService;
    @Mock private Authentication authentication;
    @InjectMocks private ReliabilityController controller;

    @Test
    void attendanceUsesAuthenticatedCreatorIdentity() {
        when(authentication.getName()).thenReturn("creator");
        AttendanceUpdateRequest request = new AttendanceUpdateRequest(AttendanceStatus.NO_SHOW);

        controller.updateAttendance(10L, 20L, request, authentication);

        verify(reliabilityService).recordAttendance(
                10L, 20L, AttendanceStatus.NO_SHOW, "creator");
    }

    @Test
    void attendanceHistoryUsesAuthenticatedIdentity() {
        when(authentication.getName()).thenReturn("miguel");
        AttendanceHistoryResponse item = new AttendanceHistoryResponse(
                20L,
                10L,
                "Padel",
                "Lisboa",
                Instant.parse("2026-09-20T19:00:00Z"),
                AttendanceStatus.NO_SHOW,
                Instant.parse("2026-09-20T20:00:00Z"),
                null,
                true
        );
        when(reliabilityService.attendanceHistory("miguel"))
                .thenReturn(List.of(item));

        var response = controller.attendanceHistory(authentication);

        assertEquals(1, response.size());
        assertEquals(20L, response.get(0).participantId());
        verify(reliabilityService).attendanceHistory("miguel");
    }

    @Test
    void adminDisputeListUsesRequestedStatus() {
        AttendanceDisputeResponse dispute = new AttendanceDisputeResponse(
                30L,
                20L,
                10L,
                "Padel",
                "Lisboa",
                Instant.parse("2026-09-20T19:00:00Z"),
                "miguel",
                "creator",
                "Estive presente",
                DisputeStatus.OPEN,
                Instant.parse("2026-09-20T21:00:00Z"),
                null
        );
        when(reliabilityService.disputes(DisputeStatus.OPEN))
                .thenReturn(List.of(dispute));

        var response = controller.disputes(DisputeStatus.OPEN);

        assertEquals(1, response.size());
        assertEquals(30L, response.get(0).id());
        verify(reliabilityService).disputes(DisputeStatus.OPEN);
    }

    @Test
    void disputeUsesAuthenticatedParticipantIdentity() {
        when(authentication.getName()).thenReturn("miguel");
        AttendanceDisputeRequest request = new AttendanceDisputeRequest(
                20L, "Eu estive presente.");
        
        controller.createDispute(request, authentication);

        verify(reliabilityService).createDispute(
                20L, "Eu estive presente.", "miguel");
    }

    @Test
    void adminCanResolveDispute() {
        DisputeResolutionRequest request = new DisputeResolutionRequest(DisputeStatus.ACCEPTED);

        controller.resolveDispute(30L, request);

        verify(reliabilityService).resolveDispute(30L, DisputeStatus.ACCEPTED);
    }

    @Test
    void disputeResolutionEndpointRequiresAdminRole() throws Exception {
        Method method = ReliabilityController.class.getMethod(
                "resolveDispute",
                Long.class,
                DisputeResolutionRequest.class);

        PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);

        assertNotNull(preAuthorize);
        assertEquals("hasAnyRole('ADMIN', 'SUPER_ADMIN')", preAuthorize.value());
    }

    @Test
    void profileSummaryDelegatesByUsername() {
        ReliabilitySummaryResponse expected = new ReliabilitySummaryResponse(
                94.0, 12, 1, 0, 1.0, false, false, false);
        when(reliabilityService.summaryByUsername("miguel")).thenReturn(expected);

        ReliabilitySummaryResponse actual = controller.summary("miguel");

        assertSame(expected, actual);
    }
}
