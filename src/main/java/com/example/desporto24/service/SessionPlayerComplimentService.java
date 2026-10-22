package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.exception.SessionNotFound;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.PlayerComplimentRequest;
import com.example.desporto24.model.dto.session.PlayerComplimentResponse;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.session.SessionPlayerCompliment;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionPlayerComplimentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Positive feedback only. No numerical player scores, free-text abuse or public ranking.
 * Only two players with confirmed attendance at the same concluded session may interact.
 */
@Service
@RequiredArgsConstructor
public class SessionPlayerComplimentService {
    private static final Duration GAME_LENGTH = Duration.ofHours(2);

    private final SessionRepository sessionRepository;
    private final ProfileRepository profileRepository;
    private final SessionParticipantRepository participantRepository;
    private final SessionPlayerComplimentRepository complimentRepository;

    @Transactional
    public PlayerComplimentResponse give(Long sessionId, String recipientUsername, String username,
                                         PlayerComplimentRequest request) {
        if (request == null || request.type() == null) {
            throw new IllegalArgumentException("Choose a positive compliment");
        }
        if (recipientUsername == null || recipientUsername.isBlank()) {
            throw new IllegalArgumentException("Invalid recipient");
        }

        // Existing session lock prevents duplicate concurrent compliments for the same game.
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        if (session.getStatus() == SessionStatus.CANCELLED
                || session.getProblemType() == SessionProblemType.NOT_OCCURRED
                || Boolean.FALSE.equals(session.getEnabled())
                || session.getGameDate() == null
                || session.getGameDate().toInstant().isAfter(Instant.now().minus(GAME_LENGTH))) {
            throw new IllegalStateException("Session is not eligible for post-game compliments");
        }

        Profile sender = profile(username);
        Profile recipient = profile(recipientUsername);
        if (sender.getId().equals(recipient.getId())) {
            throw new IllegalArgumentException("Cannot compliment yourself");
        }
        SessionParticipant giver = attended(sessionId, sender.getId());
        SessionParticipant receiver = attended(sessionId, recipient.getId());
        if (giver.getProfile() == null || receiver.getProfile() == null) {
            throw new IllegalStateException("Participants must have a registered profile");
        }

        Instant now = Instant.now();
        SessionPlayerCompliment compliment = complimentRepository
                .findBySessionIdAndSenderIdAndRecipientId(sessionId, sender.getId(), recipient.getId())
                .orElseGet(() -> SessionPlayerCompliment.builder()
                        .session(session)
                        .sender(sender)
                        .recipient(receiver.getProfile())
                        .createdAt(now)
                        .build());
        compliment.setType(request.type());
        compliment.setUpdatedAt(now);
        return response(complimentRepository.save(compliment));
    }

    @Transactional(readOnly = true)
    public List<PlayerComplimentResponse> sent(Long sessionId, String username) {
        requireSession(sessionId);
        Profile current = profile(username);
        return complimentRepository.findBySessionIdAndSenderIdOrderByUpdatedAtDesc(sessionId, current.getId())
                .stream().map(this::response).toList();
    }

    @Transactional(readOnly = true)
    public List<PlayerComplimentResponse> received(Long sessionId, String username) {
        requireSession(sessionId);
        Profile current = profile(username);
        return complimentRepository.findBySessionIdAndRecipientIdOrderByUpdatedAtDesc(sessionId, current.getId())
                .stream().map(this::response).toList();
    }

    private SessionParticipant attended(Long sessionId, Long profileId) {
        SessionParticipant participant = participantRepository
                .findBySessionIdAndProfileIdAndState(sessionId, profileId, ParticipantState.ACTIVE)
                .orElseThrow(() -> new AccessDeniedException(
                        "Only players with confirmed attendance can exchange compliments"));
        if (participant.getAttendanceStatus() != AttendanceStatus.ATTENDED
                || participant.getAttendanceRecordedAt() == null) {
            throw new AccessDeniedException("Confirmed attendance is required for both players");
        }
        return participant;
    }

    private void requireSession(Long sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new SessionNotFound("Session not found");
        }
    }

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private PlayerComplimentResponse response(SessionPlayerCompliment entity) {
        return new PlayerComplimentResponse(entity.getSender().getId(), entity.getRecipient().getId(),
                entity.getType(), entity.getUpdatedAt());
    }
}
