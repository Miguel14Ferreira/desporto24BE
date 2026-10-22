package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.exception.SessionNotFound;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.venue.VenueExperienceRatingRequest;
import com.example.desporto24.model.dto.venue.VenueExperienceRatingResponse;
import com.example.desporto24.model.dto.venue.VenueRatingSummaryResponse;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.model.venue.VenueExperienceRating;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.venue.VenueExperienceRatingRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Venue feedback is independent from session stars and never rates people.
 * A distinct player counts once per venue, and ratings only follow confirmed attendance.
 */
@Service
@RequiredArgsConstructor
public class VenueExperienceRatingService {
    private static final Duration GAME_LENGTH = Duration.ofHours(2);
    private static final int MINIMUM_PUBLIC_REVIEWS = 3;

    private final SessionRepository sessionRepository;
    private final ProfileRepository profileRepository;
    private final SessionParticipantRepository participantRepository;
    private final VenueRepository venueRepository;
    private final VenueExperienceRatingRepository ratingRepository;

    @Transactional
    public VenueExperienceRatingResponse rate(Long sessionId, String username,
                                               VenueExperienceRatingRequest request) {
        if (request == null || request.score() == null
                || request.score() < 1 || request.score() > 5) {
            throw new IllegalArgumentException("Venue rating must be between 1 and 5");
        }

        // Share the same per-session lock as the existing post-game rating.
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        requireFinishedGame(session);
        Venue venue = session.getVenue();
        if (venue == null) {
            throw new IllegalStateException("Only sessions linked to a registered venue can rate it");
        }

        Profile player = profile(username);
        SessionParticipant participant = participantRepository
                .findBySessionIdAndProfileIdAndState(
                        sessionId, player.getId(), ParticipantState.ACTIVE)
                .orElseThrow(() -> new AccessDeniedException(
                        "Only registered participants can rate the venue"));
        if (participant.getAttendanceStatus() != AttendanceStatus.ATTENDED
                || participant.getAttendanceRecordedAt() == null) {
            throw new AccessDeniedException(
                    "Confirmed attendance is required to rate the venue");
        }

        Instant now = Instant.now();
        VenueExperienceRating rating = ratingRepository
                .findByVenueIdAndProfileId(venue.getId(), player.getId())
                .orElseGet(() -> VenueExperienceRating.builder()
                        .venue(venue).profile(player).createdAt(now).build());
        rating.setSession(session);
        rating.setScore(request.score());
        rating.setUpdatedAt(now);
        VenueExperienceRating saved = ratingRepository.save(rating);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Optional<VenueExperienceRatingResponse> mine(Long sessionId, String username) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFound("Session not found"));
        if (session.getVenue() == null) return Optional.empty();
        Profile player = profile(username);
        return ratingRepository.findByVenueIdAndProfileId(
                session.getVenue().getId(), player.getId()).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public VenueRatingSummaryResponse summary(Long venueId) {
        if (!venueRepository.existsById(venueId)) {
            throw new IllegalArgumentException("Recinto não encontrado");
        }
        long count = ratingRepository.countByVenueId(venueId);
        // Do not expose an average OR the count for fewer than three unique people.
        if (count < MINIMUM_PUBLIC_REVIEWS) {
            return new VenueRatingSummaryResponse(venueId, 0, null);
        }
        Double average = ratingRepository.averageScoreForVenue(venueId);
        if (average == null || !Double.isFinite(average)) {
            throw new IllegalStateException("Inconsistent venue rating statistics");
        }
        double rounded = BigDecimal.valueOf(average)
                .setScale(1, RoundingMode.HALF_UP).doubleValue();
        return new VenueRatingSummaryResponse(venueId, count, rounded);
    }

    private void requireFinishedGame(Session session) {
        if (session.getStatus() == SessionStatus.CANCELLED
                || session.getProblemType() == SessionProblemType.NOT_OCCURRED
                || Boolean.FALSE.equals(session.getEnabled())
                || session.getGameDate() == null
                || session.getGameDate().toInstant().isAfter(Instant.now().minus(GAME_LENGTH))) {
            throw new IllegalStateException("Session is not eligible for post-game venue ratings");
        }
    }

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private VenueExperienceRatingResponse toResponse(VenueExperienceRating rating) {
        return new VenueExperienceRatingResponse(
                rating.getVenue().getId(), rating.getScore(), rating.getUpdatedAt());
    }
}
