package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionViewMapperPrivacyTest {

    private final SessionParticipantRepository participantRepository = mock(SessionParticipantRepository.class);
    private final SessionWaitlistRepository waitlistRepository = mock(SessionWaitlistRepository.class);
    private final VenueService venueService = mock(VenueService.class);
    private final SessionViewMapper mapper = new SessionViewMapper(
            participantRepository,
            waitlistRepository,
            venueService
    );

    @Test
    void nonParticipantCannotSeeExactAddress() {
        Profile creator = profile(1L, "creator");
        Profile viewer = profile(2L, "viewer");
        Session session = session(creator);

        when(participantRepository.countBySessionIdAndState(10L, ParticipantState.ACTIVE)).thenReturn(1L);
        when(participantRepository.existsBySessionIdAndProfileIdAndState(
                10L, 2L, ParticipantState.ACTIVE)).thenReturn(false);
        when(waitlistRepository.findBySessionIdAndProfileId(10L, 2L)).thenReturn(java.util.Optional.empty());

        SessionDetailResponse response = mapper.toDetail(session, viewer);

        assertNull(response.address());
        assertNotNull(response.city());
        assertFalse(response.canSeeExactAddress());
    }

    @Test
    void detailIncludesRecurrenceSeriesId() {
        Profile creator = profile(1L, "creator");
        Session session = session(creator);
        session.setRecurrenceSeriesId("series-123");

        when(participantRepository.countBySessionIdAndState(10L, ParticipantState.ACTIVE)).thenReturn(1L);
        when(waitlistRepository.findBySessionIdAndProfileId(10L, 1L))
                .thenReturn(java.util.Optional.empty());

        SessionDetailResponse response = mapper.toDetail(session, creator);

        assertEquals("series-123", response.recurrenceSeriesId());
    }

    @Test
    void detailIncludesReportedProblemType() {
        Profile creator = profile(1L, "creator");
        Session session = session(creator);
        session.setProblemType(SessionProblemType.VENUE_PROBLEM);

        when(participantRepository.countBySessionIdAndState(10L, ParticipantState.ACTIVE)).thenReturn(1L);
        when(waitlistRepository.findBySessionIdAndProfileId(10L, 1L))
                .thenReturn(java.util.Optional.empty());

        SessionDetailResponse response = mapper.toDetail(session, creator);

        assertEquals(SessionProblemType.VENUE_PROBLEM, response.problemType());
    }

    @Test
    void creatorCanSeeReportedProblemDetails() {
        Profile creator = profile(1L, "creator");
        Session session = session(creator);
        session.setProblemType(SessionProblemType.OTHER);
        session.setProblemDetails("Balneario sem agua quente");

        when(participantRepository.countBySessionIdAndState(10L, ParticipantState.ACTIVE)).thenReturn(1L);
        when(waitlistRepository.findBySessionIdAndProfileId(10L, 1L))
                .thenReturn(java.util.Optional.empty());

        SessionDetailResponse response = mapper.toDetail(session, creator);

        assertEquals("Balneario sem agua quente", response.problemDetails());
    }

    @Test
    void nonCreatorCannotSeeReportedProblemDetails() {
        Profile creator = profile(1L, "creator");
        Profile viewer = profile(2L, "viewer");
        Session session = session(creator);
        session.setProblemType(SessionProblemType.OTHER);
        session.setProblemDetails("Balneario sem agua quente");

        when(participantRepository.countBySessionIdAndState(10L, ParticipantState.ACTIVE)).thenReturn(1L);
        when(participantRepository.existsBySessionIdAndProfileIdAndState(
                10L, 2L, ParticipantState.ACTIVE)).thenReturn(false);
        when(waitlistRepository.findBySessionIdAndProfileId(10L, 2L))
                .thenReturn(java.util.Optional.empty());

        SessionDetailResponse response = mapper.toDetail(session, viewer);

        assertNull(response.problemDetails());
    }

    @Test
    void creatorCanSeeExactAddress() {
        Profile creator = profile(1L, "creator");
        Session session = session(creator);

        when(participantRepository.countBySessionIdAndState(10L, ParticipantState.ACTIVE)).thenReturn(1L);
        when(waitlistRepository.findBySessionIdAndProfileId(10L, 1L)).thenReturn(java.util.Optional.empty());

        SessionDetailResponse response = mapper.toDetail(session, creator);

        assertEquals("Rua do Campo", response.address());
        assertTrue(response.canSeeExactAddress());
    }

    private Profile profile(Long id, String username) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }

    private Session session(Profile creator) {
        Session session = new Session();
        session.setId(10L);
        session.setSport("Futebol");
        session.setCity("Cacem");
        session.setAddress("Rua do Campo");
        session.setGameDate(Date.from(java.time.Instant.parse("2026-10-01T19:00:00Z")));
        session.setMaxParticipants(10);
        session.setPriceAmount(BigDecimal.valueOf(5));
        session.setPrivateGame(false);
        session.setStatus(SessionStatus.OPEN);
        session.setProfile(creator);
        return session;
    }
}
