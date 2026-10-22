package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.exception.SessionNotFound;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.SessionExperienceRatingRequest;
import com.example.desporto24.model.dto.session.SessionExperienceRatingResponse;
import com.example.desporto24.model.session.SessionExperienceRating;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionExperienceRatingRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Private, optional feedback by a registered player. No public scores or reliability penalties.
 */
@Service
@RequiredArgsConstructor
public class SessionExperienceRatingService {

    private static final Duration GAME_LENGTH = Duration.ofHours(2);

    private final SessionRepository sessionRepository;
    private final ProfileRepository profileRepository;
    private final SessionParticipantRepository participantRepository;
    private final SessionExperienceRatingRepository ratingRepository;

    @Transactional
    public SessionExperienceRatingResponse rate(Long sessionId, String username,
                                                 SessionExperienceRatingRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Ratings are required");
        }
        requireScore(request.overallScore());

        // Serialize updates for the same session so the unique (session, profile) rating remains singular.
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        Profile profile = profile(username);
        requireEligibleSession(session, Instant.now());
        // Only a participant whose presence was explicitly recorded can rate.
        // ACTIVE by itself means they registered, not that they actually played.
        SessionParticipant participant = participantRepository
                .findBySessionIdAndProfileIdAndState(
                        sessionId, profile.getId(), ParticipantState.ACTIVE)
                .orElseThrow(() -> new AccessDeniedException(
                        "Only registered participants can rate this session"));
        if (participant.getAttendanceStatus() != AttendanceStatus.ATTENDED
                || participant.getAttendanceRecordedAt() == null) {
            throw new AccessDeniedException(
                    "Only participants with confirmed attendance can rate this session");
        }

        Instant now = Instant.now();
        SessionExperienceRating rating = ratingRepository
                .findBySessionIdAndProfileId(sessionId, profile.getId())
                .orElseGet(() -> SessionExperienceRating.builder()
                        .session(session)
                        .profile(profile)
                        .createdAt(now)
                        .build());
        rating.setOverallScore(request.overallScore());
        rating.setUpdatedAt(now);

        return response(ratingRepository.save(rating));
    }

    @Transactional(readOnly = true)
    public Optional<SessionExperienceRatingResponse> mine(Long sessionId, String username) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new SessionNotFound("Session not found");
        }
        Profile profile = profile(username);
        return ratingRepository.findBySessionIdAndProfileId(sessionId, profile.getId())
                .map(this::response);
    }

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private void requireEligibleSession(Session session, Instant now) {
        if (session.getStatus() == SessionStatus.CANCELLED
                || session.getProblemType() == SessionProblemType.NOT_OCCURRED
                || Boolean.FALSE.equals(session.getEnabled())
                || session.getGameDate() == null
                || session.getGameDate().toInstant().isAfter(now.minus(GAME_LENGTH))) {
            throw new IllegalStateException("Session is not eligible for post-game ratings");
        }
    }

    private void requireScore(Integer score) {
        if (score == null || score < 1 || score > 5) {
            throw new IllegalArgumentException("Ratings must be integers from 1 to 5");
        }
    }

    private SessionExperienceRatingResponse response(SessionExperienceRating rating) {
        return new SessionExperienceRatingResponse(
                rating.getOverallScore(), rating.getUpdatedAt());
    }
}
