package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionOutcome;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Session;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/** Atomic, idempotent kickoff check; legacy sessions with null minimum are untouched. */
@Service
@RequiredArgsConstructor
public class SessionMinimumClosureService {
    private final SessionRepository sessions;
    private final SessionParticipantRepository participants;
    private final SessionNotificationService notifications;

    @Transactional
    public boolean closeIfUnderMinimum(Long sessionId, Instant now) {
        Session session = sessions.findByIdForUpdate(sessionId).orElse(null);
        if (session == null || session.getMinParticipants() == null
                || (session.getStatus() != SessionStatus.OPEN
                    && session.getStatus() != SessionStatus.FULL)
                || session.getGameDate() == null
                || session.getGameDate().toInstant().isAfter(now)) return false;

        long registered = participants.countBySessionIdAndState(sessionId, ParticipantState.ACTIVE);
        int confirmed = SessionCapacity.totalConfirmed(session, registered);
        int required = SessionMinimumPlayers.required(session);
        if (confirmed >= required) return false;

        session.setStatus(SessionStatus.CANCELLED);
        session.setOutcome(SessionOutcome.NOT_OCCURRED);
        session.setProblemType(SessionProblemType.NOT_OCCURRED);
        session.setProblemDetails("Mínimo de " + required + " jogadores não atingido");
        session.setUpdatedAt(now);
        sessions.save(session);
        notifications.notifyInsufficientPlayers(session, required);
        return true;
    }
}
