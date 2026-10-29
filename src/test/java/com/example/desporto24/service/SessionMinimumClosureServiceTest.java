package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionOutcome;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Session;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionMinimumClosureServiceTest {
    private final SessionRepository sessions = mock(SessionRepository.class);
    private final SessionParticipantRepository participants = mock(SessionParticipantRepository.class);
    private final SessionNotificationService notifications = mock(SessionNotificationService.class);
    private SessionMinimumClosureService closure;
    private final Instant kickoff = Instant.parse("2026-10-29T19:00:00Z");

    @BeforeEach void setUp() {
        closure = new SessionMinimumClosureService(sessions, participants, notifications);
    }

    private Session session(Integer minimum, int external) {
        Session s = Session.builder()
                .id(41L).sport("Futsal").maxParticipants(10).minParticipants(minimum)
                .externalConfirmedParticipants(external).gameDate(Date.from(kickoff))
                .status(SessionStatus.OPEN).build();
        when(sessions.findByIdForUpdate(41L)).thenReturn(Optional.of(s));
        return s;
    }

    @Test void doesNotCloseBeforeKickoffOrAReadyGame() {
        Session s = session(6, 2);
        when(participants.countBySessionIdAndState(41L, ParticipantState.ACTIVE)).thenReturn(4L);
        assertFalse(closure.closeIfUnderMinimum(41L, kickoff.minusSeconds(1)));
        assertFalse(closure.closeIfUnderMinimum(41L, kickoff.plusSeconds(1)));
        assertEquals(SessionStatus.OPEN, s.getStatus());
        verify(sessions, never()).save(any());
        verifyNoInteractions(notifications);
    }

    @Test void automaticallyClosesShortGameAndNotifiesOnlyOnce() {
        Session s = session(10, 2);
        when(participants.countBySessionIdAndState(41L, ParticipantState.ACTIVE)).thenReturn(2L);
        assertTrue(closure.closeIfUnderMinimum(41L, kickoff));
        assertEquals(SessionStatus.CANCELLED, s.getStatus());
        assertEquals(SessionOutcome.NOT_OCCURRED, s.getOutcome());
        assertEquals(SessionProblemType.NOT_OCCURRED, s.getProblemType());
        verify(notifications).notifyInsufficientPlayers(s, 10);
        assertFalse(closure.closeIfUnderMinimum(41L, kickoff.plusSeconds(60)));
        verify(notifications, times(1)).notifyInsufficientPlayers(s, 10);
    }

    @Test void legacySessionsWithoutConfiguredMinimumAreNeverAutoCancelled() {
        Session s = session(null, 0);
        assertFalse(closure.closeIfUnderMinimum(41L, kickoff.plusSeconds(3600)));
        assertEquals(SessionStatus.OPEN, s.getStatus());
        verifyNoInteractions(notifications);
    }
}
