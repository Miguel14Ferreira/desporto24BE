package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.OrganizerExclusionReason;
import com.example.desporto24.exception.SessionNotFound;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.session.SessionOrganizerExclusion;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionOrganizerExclusionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Private organizer-owned future enrollment choice, including recorded no-shows and conduct.
 * Does not alter current memberships, friendships or global reliability.
 */
@Service
@RequiredArgsConstructor
public class SessionOrganizerExclusionService {

    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository participantRepository;
    private final ProfileRepository profileRepository;
    private final SessionOrganizerExclusionRepository exclusionRepository;

    @Transactional
    public boolean excludeAfterNoShow(Long sessionId, Long participantId, String username) {
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile organizer = organizer(session, username);
        SessionParticipant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));

        if (!sessionId.equals(participant.getSession().getId())) {
            throw new IllegalArgumentException("Participant is not in this session");
        }
        if (organizer.getId().equals(participant.getProfile().getId())) {
            throw new IllegalArgumentException("Organizer cannot exclude themselves");
        }
        if (participant.getAttendanceStatus() != AttendanceStatus.NO_SHOW
                || participant.getAttendanceRecordedAt() == null) {
            throw new IllegalStateException("Only a recorded no-show permits this option");
        }
        if (!exclusionRepository.existsByOrganizerIdAndExcludedId(
                organizer.getId(), participant.getProfile().getId())) {
            exclusionRepository.save(SessionOrganizerExclusion.builder()
                    .organizer(organizer)
                    .excluded(participant.getProfile())
                    .reason(OrganizerExclusionReason.NO_SHOW)
                    .createdAt(Instant.now())
                    .build());
        }
        return true;
    }

    /**
     * A recorded absence is still required for NO_SHOW. Other reasons reflect a
     * private organizer decision about their own future sessions only.
     */
    @Transactional
    public boolean excludeForReason(Long sessionId, Long participantId, String username,
                                    OrganizerExclusionReason reason) {
        if (reason == null) {
            throw new IllegalArgumentException("Select a reason for future exclusion");
        }
        if (reason == OrganizerExclusionReason.NO_SHOW) {
            return excludeAfterNoShow(sessionId, participantId, username);
        }
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile organizer = organizer(session, username);
        SessionParticipant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));
        if (participant.getSession() == null
                || !sessionId.equals(participant.getSession().getId())
                || participant.getProfile() == null) {
            throw new IllegalArgumentException("Participant is not in this session");
        }
        if (organizer.getId().equals(participant.getProfile().getId())) {
            throw new IllegalArgumentException("Organizer cannot exclude themselves");
        }
        SessionOrganizerExclusion entry = exclusionRepository
                .findByOrganizerIdAndExcludedId(organizer.getId(), participant.getProfile().getId())
                .orElseGet(() -> SessionOrganizerExclusion.builder()
                        .organizer(organizer)
                        .excluded(participant.getProfile())
                        .createdAt(Instant.now())
                        .build());
        entry.setReason(reason);
        exclusionRepository.save(entry);
        return true;
    }

    @Transactional
    public boolean remove(Long sessionId, Long participantId, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile organizer = organizer(session, username);
        SessionParticipant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));
        if (!sessionId.equals(participant.getSession().getId())) {
            throw new IllegalArgumentException("Participant is not in this session");
        }
        exclusionRepository.deleteByOrganizerIdAndExcludedId(
                organizer.getId(), participant.getProfile().getId());
        return false;
    }

    @Transactional(readOnly = true)
    public boolean status(Long sessionId, Long participantId, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile organizer = organizer(session, username);
        SessionParticipant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));
        if (!sessionId.equals(participant.getSession().getId())) {
            throw new IllegalArgumentException("Participant is not in this session");
        }
        return exclusionRepository.existsByOrganizerIdAndExcludedId(
                organizer.getId(), participant.getProfile().getId());
    }

    private Profile organizer(Session session, String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (session.getProfile() == null
                || !session.getProfile().getId().equals(actor.getId())) {
            throw new AccessDeniedException("Only the organizer can manage future exclusions");
        }
        return actor;
    }
}
