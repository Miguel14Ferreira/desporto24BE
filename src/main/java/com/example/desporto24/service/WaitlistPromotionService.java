package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WaitlistPromotionService {

    private final SessionParticipantRepository participantRepository;
    private final SessionWaitlistRepository waitlistRepository;
    private final SessionRepository sessionRepository;
    private final SessionNotificationService sessionNotificationService;
    private final PilotEventService pilotEventService;

    @Transactional
    public Optional<SessionParticipant> promoteNext(Session session, Instant now) {
        if (session.getStatus() == SessionStatus.CANCELLED
                || session.getStatus() == SessionStatus.COMPLETED) {
            return Optional.empty();
        }

        // A promotion cannot consume a place already reserved for an external player.
        long active = participantRepository.countBySessionIdAndState(
                session.getId(), ParticipantState.ACTIVE);
        if (SessionCapacity.remainingPlaces(session, active) == 0) {
            recomputeStatus(session, now);
            return Optional.empty();
        }

        Optional<SessionParticipant> promoted = waitlistRepository
                .findFirstBySessionIdOrderByPriorityTierAscCreatedAtAsc(session.getId())
                .map(entry -> {
                    waitlistRepository.delete(entry);

                    SessionParticipant participant = participantRepository
                            .findBySessionIdAndProfileId(session.getId(), entry.getProfile().getId())
                            .orElseGet(SessionParticipant::new);

                    participant.setSession(session);
                    participant.setProfile(entry.getProfile());
                    participant.setState(ParticipantState.ACTIVE);
                    participant.setAttendanceStatus(AttendanceStatus.PENDING);
                    participant.setJoinedAt(now);
                    participant.setExitedAt(null);
                    participant.setAttendanceReportedBy(null);
                    participant.setAttendanceRecordedAt(null);
                    participant.setTeamCode(null);

                    SessionParticipant saved = participantRepository.save(participant);
                    pilotEventService.recordSessionJoined(entry.getProfile(), session.getId());
                    sessionNotificationService.notifyWaitlistPromoted(session, entry.getProfile());
                    return saved;
                });

        recomputeStatus(session, now);
        return promoted;
    }

    private void recomputeStatus(Session session, Instant now) {
        int max = session.getMaxParticipants() == null ? 0 : session.getMaxParticipants();
        long active = participantRepository.countBySessionIdAndState(
                session.getId(), ParticipantState.ACTIVE);
        session.setStatus(max > 0 && SessionCapacity.remainingPlaces(session, active) == 0
                ? SessionStatus.FULL : SessionStatus.OPEN);
        session.setUpdatedAt(now);
        sessionRepository.save(session);
    }
}
