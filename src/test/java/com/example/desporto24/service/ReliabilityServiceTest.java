package com.example.desporto24.service;

import com.example.desporto24.configuration.ReliabilityPolicyProperties;
import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.DisputeStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.reliability.ReliabilitySummaryResponse;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.session.AttendanceDisputeRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReliabilityServiceTest {

    private SessionParticipantRepository participantRepository;
    private AttendanceDisputeRepository disputeRepository;
    private ReliabilityService service;
    private Profile profile;

    @BeforeEach
    void setUp() {
        participantRepository = mock(SessionParticipantRepository.class);
        disputeRepository = mock(AttendanceDisputeRepository.class);

        ReliabilityPolicyProperties properties = new ReliabilityPolicyProperties();
        properties.setFreeCancelHours(2);
        properties.setLookbackDays(90);
        properties.setLateCancellationWeight(0.5);
        properties.setNoShowWeight(1.0);
        properties.setWarningThreshold(2.0);
        properties.setReducedPriorityThreshold(3.0);
        properties.setRestrictionThreshold(4.0);
        properties.setRestrictionDays(7);
        properties.setAttendanceMarkingDelayMinutes(30);

        service = new ReliabilityService(participantRepository, disputeRepository, properties);

        profile = new Profile();
        profile.setId(7L);
        profile.setUsername("miguel");
    }

    @Test
    void exactlyTwoHoursBeforeStartIsPenaltyFree() {
        Instant start = Instant.parse("2026-10-01T20:00:00Z");

        assertEquals(
                AttendanceStatus.PENDING,
                service.leaveAttendanceStatus(start.minus(Duration.ofHours(2)), start)
        );
    }

    @Test
    void oneSecondInsideTwoHoursIsLateCancellation() {
        Instant start = Instant.parse("2026-10-01T20:00:00Z");

        assertEquals(
                AttendanceStatus.LATE_CANCELLED,
                service.leaveAttendanceStatus(
                        start.minus(Duration.ofHours(2)).plusSeconds(1),
                        start
                )
        );
    }

    @Test
    void leavingAtOrAfterStartIsRejected() {
        Instant start = Instant.parse("2026-10-01T20:00:00Z");

        assertThrows(IllegalStateException.class,
                () -> service.leaveAttendanceStatus(start, start));
        assertThrows(IllegalStateException.class,
                () -> service.leaveAttendanceStatus(start.plusSeconds(1), start));
    }

    @Test
    void twoNoShowsTriggerWarningThreeReducePriorityAndFourRestrictTemporarily() {
        Instant now = Instant.parse("2026-10-10T12:00:00Z");

        when(participantRepository.findReliabilityHistory(eq(7L), any(), eq(now)))
                .thenReturn(List.of(
                        penalty(1L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(4))),
                        penalty(2L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(3)))
                ));
        noDisputes(1L, 2L);

        ReliabilitySummaryResponse two = service.summary(profile, now);
        assertTrue(two.warning());
        assertFalse(two.reducedPriority());
        assertFalse(two.joinRestricted());

        when(participantRepository.findReliabilityHistory(eq(7L), any(), eq(now)))
                .thenReturn(List.of(
                        penalty(1L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(4))),
                        penalty(2L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(3))),
                        penalty(3L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(2)))
                ));
        noDisputes(3L);

        ReliabilitySummaryResponse three = service.summary(profile, now);
        assertTrue(three.reducedPriority());
        assertFalse(three.joinRestricted());

        when(participantRepository.findReliabilityHistory(eq(7L), any(), eq(now)))
                .thenReturn(List.of(
                        penalty(1L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(4))),
                        penalty(2L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(3))),
                        penalty(3L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(2))),
                        penalty(4L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(1)))
                ));
        noDisputes(4L);

        ReliabilitySummaryResponse four = service.summary(profile, now);
        assertTrue(four.joinRestricted());
        assertThrows(AccessDeniedException.class,
                () -> service.requireJoinAllowed(profile, now));
    }

    @Test
    void restrictionExpiresAfterSevenDaysButHistoryStillWarns() {
        Instant now = Instant.parse("2026-10-20T12:00:00Z");
        List<SessionParticipant> history = List.of(
                penalty(1L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(12))),
                penalty(2L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(11))),
                penalty(3L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(10))),
                penalty(4L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(8)))
        );
        when(participantRepository.findReliabilityHistory(eq(7L), any(), eq(now))).thenReturn(history);
        noDisputes(1L, 2L, 3L, 4L);

        ReliabilitySummaryResponse summary = service.summary(profile, now);

        assertTrue(summary.warning());
        assertTrue(summary.reducedPriority());
        assertFalse(summary.joinRestricted());
        assertDoesNotThrow(() -> service.requireJoinAllowed(profile, now));
    }

    @Test
    void openAndAcceptedDisputesExcludeNoShowWeightButRejectedRestoresIt() {
        Instant now = Instant.parse("2026-10-10T12:00:00Z");
        SessionParticipant open = penalty(1L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(1)));
        SessionParticipant accepted = penalty(2L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(1)));
        SessionParticipant rejected = penalty(3L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(1)));

        when(participantRepository.findReliabilityHistory(eq(7L), any(), eq(now)))
                .thenReturn(List.of(open, accepted, rejected));
        when(disputeRepository.existsByParticipantIdAndStatusIn(
                eq(1L), eq(Set.of(DisputeStatus.OPEN, DisputeStatus.ACCEPTED)))).thenReturn(true);
        when(disputeRepository.existsByParticipantIdAndStatusIn(
                eq(2L), eq(Set.of(DisputeStatus.OPEN, DisputeStatus.ACCEPTED)))).thenReturn(true);
        when(disputeRepository.existsByParticipantIdAndStatusIn(
                eq(3L), eq(Set.of(DisputeStatus.OPEN, DisputeStatus.ACCEPTED)))).thenReturn(false);

        ReliabilitySummaryResponse summary = service.summary(profile, now);

        assertEquals(1.0, summary.penaltyScore(), 0.001);
        assertFalse(summary.warning());
    }

    @Test
    void lateCancellationHasHalfTheWeightOfNoShow() {
        Instant now = Instant.parse("2026-10-10T12:00:00Z");
        when(participantRepository.findReliabilityHistory(eq(7L), any(), eq(now)))
                .thenReturn(List.of(
                        penalty(1L, AttendanceStatus.LATE_CANCELLED, now.minus(Duration.ofDays(1))),
                        penalty(2L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(1)))
                ));
        noDisputes(1L, 2L);

        ReliabilitySummaryResponse summary = service.summary(profile, now);

        assertEquals(1.5, summary.penaltyScore(), 0.001);
        assertFalse(summary.warning());
    }

    @Test
    void attendanceRateUsesAttendedAndNoShowCompletedSessions() {
        Instant now = Instant.parse("2026-10-10T12:00:00Z");
        when(participantRepository.findReliabilityHistory(eq(7L), any(), eq(now)))
                .thenReturn(List.of(
                        penalty(1L, AttendanceStatus.ATTENDED, now.minus(Duration.ofDays(4))),
                        penalty(2L, AttendanceStatus.ATTENDED, now.minus(Duration.ofDays(3))),
                        penalty(3L, AttendanceStatus.ATTENDED, now.minus(Duration.ofDays(2))),
                        penalty(4L, AttendanceStatus.NO_SHOW, now.minus(Duration.ofDays(1)))
                ));
        noDisputes(1L, 2L, 3L, 4L);

        ReliabilitySummaryResponse summary = service.summary(profile, now);

        assertEquals(75.0, summary.attendanceRate(), 0.001);
        assertEquals(4, summary.completedSessions());
        assertEquals(1, summary.noShows());
    }

    private SessionParticipant penalty(Long id, AttendanceStatus status, Instant eventAt) {
        Session session = new Session();
        session.setGameDate(Date.from(eventAt));

        SessionParticipant participant = new SessionParticipant();
        participant.setId(id);
        participant.setProfile(profile);
        participant.setSession(session);
        participant.setAttendanceStatus(status);
        participant.setAttendanceRecordedAt(eventAt);
        return participant;
    }

    private void noDisputes(Long... ids) {
        for (Long id : ids) {
            when(disputeRepository.existsByParticipantIdAndStatusIn(
                    eq(id), eq(Set.of(DisputeStatus.OPEN, DisputeStatus.ACCEPTED))))
                    .thenReturn(false);
        }
    }
}
