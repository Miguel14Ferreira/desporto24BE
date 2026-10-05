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
import java.util.Optional;

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
        int participantCount = Math.toIntExact(participantRepository.countBySessionIdAndState(
                session.getId(), ParticipantState.ACTIVE));
        return toSummary(session, distanceKm, viewer, participantCount);
    }

    public SessionSummaryResponse toSummary(
            Session session,
            Double distanceKm,
            Profile viewer,
            int participantCount) {
        boolean joinedByViewer = viewer != null && participantRepository
                .existsBySessionIdAndProfileIdAndState(
                        session.getId(), viewer.getId(), ParticipantState.ACTIVE);
        boolean waitlistedByViewer = viewer != null && waitlistRepository
                .findBySessionIdAndProfileId(session.getId(), viewer.getId())
                .isPresent();
        return toSummary(
                session,
                distanceKm,
                participantCount,
                joinedByViewer,
                waitlistedByViewer);
    }

    public SessionSummaryResponse toSummary(
            Session session,
            Double distanceKm,
            int participantCount,
            boolean joinedByViewer,
            boolean waitlistedByViewer) {
        return new SessionSummaryResponse(
                session.getId(),
                session.getSport(),
                session.getCity(),
                session.getGameDate().toInstant(),
                distanceKm,
                participantCount,
                session.getMaxParticipants() == null ? 0 : session.getMaxParticipants(),
                session.getPriceAmount() == null ? BigDecimal.ZERO : session.getPriceAmount(),
                Boolean.TRUE.equals(session.getPrivateGame()),
                session.getStatus() == null ? SessionStatus.OPEN : session.getStatus(),
                session.getAudience() == null ? SessionAudience.PUBLIC : session.getAudience(),
                joinedByViewer,
                waitlistedByViewer,
                session.getRecurrenceSeriesId()
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

        Integer waitlistPosition = waitlistEntry
                .map(entry -> Math.toIntExact(
                        waitlistRepository.countEntriesAhead(
                                session.getId(),
                                entry.getPriorityTier(),
                                entry.getCreatedAt()
                        ) + 1
                ))
                .orElse(null);

        boolean canSeeExactAddress = creator || participant;
        int participantCount = Math.toIntExact(participantRepository.countBySessionIdAndState(
                session.getId(), ParticipantState.ACTIVE));

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
                session.getVenue() == null ? null : venueService.toResponse(session.getVenue()),
                session.getAudience() == null ? SessionAudience.PUBLIC : session.getAudience(),
                session.getRecurrenceSeriesId(),
                session.getProblemType(),
                creator ? session.getProblemDetails() : null,
                session.getCommunity() == null ? null : session.getCommunity().getId()
        );
    }
}
