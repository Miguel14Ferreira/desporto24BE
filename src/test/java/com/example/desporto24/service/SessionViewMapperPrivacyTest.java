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
    void outsiderCannotRecoverHiddenAddressFromLinkedVenue() {
        Profile creator = profile(1L, "creator");
        Profile outsider = profile(2L, "outsider");
        Session session = session(creator);
        var venue = mock(com.example.desporto24.model.venue.Venue.class);
        session.setVenue(venue);

        SessionDetailResponse response = mapper.toDetail(session, outsider);

        assertNull(response.address());
        assertNull(response.venue(),
                "A linked venue would expose its address, coordinates and public ID");
        assertFalse(response.canSeeExactAddress());
        verifyNoInteractions(venueService);
    }

    @Test
    void approvedParticipantStillReceivesLinkedVenueAndAddress() {
        Profile creator = profile(1L, "creator");
        Profile participant = profile(2L, "joined");
        Session session = session(creator);
        var venue = mock(com.example.desporto24.model.venue.Venue.class);
        var venueResponse = mock(com.example.desporto24.model.dto.venue.VenueResponse.class);
        session.setVenue(venue);
        when(participantRepository.existsBySessionIdAndProfileIdAndState(
                10L, 2L, ParticipantState.ACTIVE)).thenReturn(true);
        when(venueService.toResponse(venue)).thenReturn(venueResponse);

        SessionDetailResponse response = mapper.toDetail(session, participant);

        assertEquals("Rua do Campo", response.address());
        assertSame(venueResponse, response.venue());
        assertTrue(response.canSeeExactAddress());
        verify(venueService).toResponse(venue);
    }

    @Test
    void detailAndSummariesIncludeConfirmedPlayersOutsideTheApp() {
        Session session = session(profile(1L, "creator"));
        session.setExternalConfirmedParticipants(7);
        when(participantRepository.countBySessionIdAndState(10L, ParticipantState.ACTIVE))
                .thenReturn(1L);

        assertEquals(8, mapper.toDetail(session, null).participantCount());
        assertEquals(8, mapper.toSummary(session, null).participantCount());
        assertEquals(8, mapper.toSummary(session, null, null, java.util.Map.of(10L, 1L))
                .participantCount());
    }

    @Test
    void detailExposesExternalConfirmedCountSeparatelyFromTotal() {
        Session session = session(profile(1L, "creator"));
        session.setExternalConfirmedParticipants(7);
        when(participantRepository.countBySessionIdAndState(10L, ParticipantState.ACTIVE))
                .thenReturn(1L);

        SessionDetailResponse detail = mapper.toDetail(session, null);

        assertEquals(7, detail.externalConfirmedParticipants());
        assertEquals(8, detail.participantCount());
        assertEquals(10, detail.maxParticipants());
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
    void meetingPointIsVisibleOnlyToCreatorAndConfirmedParticipant() {
        Profile creator = profile(1L, "creator");
        Profile participant = profile(2L, "participant");
        Profile outsider = profile(3L, "outsider");
        Session session = session(creator);
        session.setMeetingPointInstructions("Entrada lateral, junto aos balneários");

        when(participantRepository.existsBySessionIdAndProfileIdAndState(
                10L, 2L, ParticipantState.ACTIVE)).thenReturn(true);
        assertEquals("Entrada lateral, junto aos balneários",
                mapper.toDetail(session, creator).meetingPointInstructions());
        assertEquals("Entrada lateral, junto aos balneários",
                mapper.toDetail(session, participant).meetingPointInstructions());
        assertNull(mapper.toDetail(session, outsider).meetingPointInstructions());
        assertNull(mapper.toDetail(session, null).meetingPointInstructions());
    }

    @Test
    void meetingPointIsNeverPublicForWaitlistedPlayers() {
        Profile creator = profile(1L, "creator");
        Profile viewer = profile(4L, "waitlisted");
        Session session = session(creator);
        session.setMeetingPointInstructions("Portão 3");
        // Waitlisted is not ACTIVE; even the waitlist detail cannot leak.
        assertNull(mapper.toDetail(session, viewer).meetingPointInstructions());
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

    @Test
    void publicSessionDetailAllowsSharing() {
        Session session = session(profile(1L, "creator"));
        session.setAudience(com.example.desporto24.enumeration.SessionAudience.PUBLIC);

        assertTrue(mapper.toDetail(session, null).canSharePublicly());
    }

    @Test
    void communityOnlyAndLegacyCommunitySessionsDoNotAllowPublicSharing() {
        Session session = session(profile(1L, "creator"));
        session.setCommunity(mock(com.example.desporto24.model.community.Community.class));
        session.setAudience(com.example.desporto24.enumeration.SessionAudience.COMMUNITY_ONLY);
        assertFalse(mapper.toDetail(session, null).canSharePublicly());

        session.setAudience(null);
        assertFalse(mapper.toDetail(session, null).canSharePublicly());
    }

    @Test
    void explicitlyPublicCommunitySessionCanBeShared() {
        Session session = session(profile(1L, "creator"));
        session.setCommunity(mock(com.example.desporto24.model.community.Community.class));
        session.setAudience(com.example.desporto24.enumeration.SessionAudience.PUBLIC);

        assertTrue(mapper.toDetail(session, null).canSharePublicly());
    }

    @Test
    void disabledOrPrivateSessionCannotBeShared() {
        Session session = session(profile(1L, "creator"));
        session.setAudience(com.example.desporto24.enumeration.SessionAudience.PUBLIC);
        session.setEnabled(false);
        assertFalse(mapper.toDetail(session, null).canSharePublicly());

        session.setEnabled(true);
        session.setPrivateGame(true);
        assertFalse(mapper.toDetail(session, null).canSharePublicly());
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
