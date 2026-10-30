package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.model.session.SessionWaitlist;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SessionViewMapper {

    private final SessionParticipantRepository participantRepository;
    private final SessionWaitlistRepository waitlistRepository;
    private final VenueService venueService;

    public SessionSummaryResponse toSummary(Session session, Double distanceKm) {
        return toSummary(session, distanceKm, null);
    }

    public SessionSummaryResponse toSummary(Session session, Double distanceKm, Profile viewer) {
        return toSummary(session, distanceKm, viewer, null);
    }

    public SessionSummaryResponse toSummary(
            Session session,
            Double distanceKm,
            Profile viewer,
            Map<Long, Long> participantCounts) {
        boolean joinedByViewer = viewer != null && participantRepository
                .existsBySessionIdAndProfileIdAndState(
                        session.getId(), viewer.getId(), ParticipantState.ACTIVE);
        boolean waitlistedByViewer = viewer != null && waitlistRepository
                .findBySessionIdAndProfileId(session.getId(), viewer.getId())
                .isPresent();

        return new SessionSummaryResponse(
                session.getId(),
                session.getSport(),
                session.getCity(),
                session.getGameDate().toInstant(),
                distanceKm,
                SessionCapacity.totalConfirmed(session, participantCounts == null
                        ? participantRepository.countBySessionIdAndState(
                                session.getId(), ParticipantState.ACTIVE)
                        : participantCounts.getOrDefault(session.getId(), 0L)),
                session.getMaxParticipants() == null ? 0 : session.getMaxParticipants(),
                session.getPriceAmount() == null ? BigDecimal.ZERO : session.getPriceAmount(),
                Boolean.TRUE.equals(session.getPrivateGame()),
                session.getStatus() == null ? SessionStatus.OPEN : session.getStatus(),
                session.getAudience() == null ? SessionAudience.PUBLIC : session.getAudience(),
                joinedByViewer,
                waitlistedByViewer,
                session.getRecurrenceSeriesId(),
                session.getCommunity() == null ? null : session.getCommunity().getId(),
                session.getSeasonCompetition() == null ? null
                        : session.getSeasonCompetition().getCommunity().getId()
        );
    }

    public SessionSummaryResponse toSummary(
            Session session,
            Double distanceKm,
            Map<Long, Long> participantCounts,
            Set<Long> joinedSessionIds,
            Set<Long> waitlistedSessionIds) {
        return new SessionSummaryResponse(
                session.getId(),
                session.getSport(),
                session.getCity(),
                session.getGameDate().toInstant(),
                distanceKm,
                SessionCapacity.totalConfirmed(session,
                        participantCounts.getOrDefault(session.getId(), 0L)),
                session.getMaxParticipants() == null ? 0 : session.getMaxParticipants(),
                session.getPriceAmount() == null ? BigDecimal.ZERO : session.getPriceAmount(),
                Boolean.TRUE.equals(session.getPrivateGame()),
                session.getStatus() == null ? SessionStatus.OPEN : session.getStatus(),
                session.getAudience() == null ? SessionAudience.PUBLIC : session.getAudience(),
                joinedSessionIds.contains(session.getId()),
                waitlistedSessionIds.contains(session.getId()),
                session.getRecurrenceSeriesId(),
                session.getCommunity() == null ? null : session.getCommunity().getId(),
                session.getSeasonCompetition() == null ? null
                        : session.getSeasonCompetition().getCommunity().getId()
        );
    }

    public SessionDetailResponse toDetail(Session session, Profile viewer) {
        boolean creator = viewer != null
                && session.getProfile() != null
                && session.getProfile().getId().equals(viewer.getId());

        boolean participant = viewer != null && participantRepository
                .existsBySessionIdAndProfileIdAndState(
                        session.getId(), viewer.getId(), ParticipantState.ACTIVE);

        Optional<SessionWaitlist> waitlistEntry = viewer == null
                ? Optional.empty()
                : waitlistRepository.findBySessionIdAndProfileId(session.getId(), viewer.getId());

        Integer waitlistPosition = waitlistEntry.map(entry -> {
            List<SessionWaitlist> ordered = waitlistRepository
                    .findBySessionIdOrderByPriorityTierAscCreatedAtAsc(session.getId());
            int index = ordered.stream()
                    .map(SessionWaitlist::getId)
                    .toList()
                    .indexOf(entry.getId());
            return index < 0 ? null : index + 1;
        }).orElse(null);

        boolean canSeeExactAddress = creator || participant;
        int participantCount = SessionCapacity.totalConfirmed(session,
                participantRepository.countBySessionIdAndState(session.getId(), ParticipantState.ACTIVE));
        boolean publicAudience = session.getAudience() == SessionAudience.PUBLIC
                || (session.getAudience() == null && session.getCommunity() == null);
        boolean canSharePublicly = publicAudience
                && !Boolean.TRUE.equals(session.getPrivateGame())
                && !Boolean.FALSE.equals(session.getEnabled());

        return new SessionDetailResponse(
                session.getId(),
                session.getSport(),
                session.getCity(),
                session.getGameDate().toInstant(),
                null,
                participantCount,
                session.getMaxParticipants() == null ? 0 : session.getMaxParticipants(),
                session.getPriceAmount() == null ? BigDecimal.ZERO : session.getPriceAmount(),
                Boolean.TRUE.equals(session.getPrivateGame()),
                session.getStatus() == null ? SessionStatus.OPEN : session.getStatus(),
                session.getProfile() == null ? null : session.getProfile().getUsername(),
                canSeeExactAddress ? session.getAddress() : null,
                canSeeExactAddress,
                creator || participant,
                waitlistEntry.isPresent(),
                waitlistPosition,
                session.getGamePhoto(),
                // The venue response includes its street address, GPS coordinates and ID.
                // Never reveal the exact meeting point through this nested DTO
                // while the top-level session address is intentionally hidden.
                canSeeExactAddress && session.getVenue() != null
                        ? venueService.toResponse(session.getVenue()) : null,
                session.getAudience() == null ? SessionAudience.PUBLIC : session.getAudience(),
                session.getRecurrenceSeriesId(),
                session.getProblemType(),
                creator ? session.getProblemDetails() : null,
                session.getCommunity() == null ? null : session.getCommunity().getId(),
                canSharePublicly,
                session.getExternalConfirmedParticipants() == null
                        ? 0 : session.getExternalConfirmedParticipants(),
                session.getRegistrationOpeningMode() == null
                        ? com.example.desporto24.enumeration.RegistrationOpeningMode.IMMEDIATE
                        : session.getRegistrationOpeningMode(),
                session.getRegistrationOpensAt(),
                TeamBalancingSportPolicy.isEligible(session),
                session.getPlayFormat(),
                // This may specify an entry gate, so keep it private just like
                // the precise street and nested venue GPS coordinates.
                canSeeExactAddress ? session.getMeetingPointInstructions() : null,
                SessionMinimumPlayers.required(session)
        );
    }
}
