package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.venue.VenueExperienceRatingRequest;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.model.venue.VenueExperienceRating;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.venue.VenueExperienceRatingRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class VenueExperienceRatingServiceTest {
    @Mock SessionRepository sessionRepository;
    @Mock ProfileRepository profileRepository;
    @Mock SessionParticipantRepository participantRepository;
    @Mock VenueRepository venueRepository;
    @Mock VenueExperienceRatingRepository ratingRepository;
    @InjectMocks VenueExperienceRatingService service;

    @Test
    void confirmedAttendeeCanRateVenueAndIsUniquelyIdentified() {
        Session session = game(Instant.now().minusSeconds(3 * 3600), SessionStatus.COMPLETED);
        Profile player = player();
        permit(session, player, AttendanceStatus.ATTENDED, Instant.now());
        when(ratingRepository.findByVenueIdAndProfileId(100L, 12L))
                .thenReturn(Optional.empty());
        when(ratingRepository.save(any(VenueExperienceRating.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.rate(7L, "miguel", new VenueExperienceRatingRequest(5));

        assertEquals(100L, result.venueId());
        assertEquals(5, result.score());
        verify(ratingRepository).save(argThat(r ->
                r.getVenue() == session.getVenue()
                && r.getSession() == session
                && r.getProfile() == player
                && r.getScore() == 5
                && r.getCreatedAt() != null && r.getUpdatedAt() != null));
    }

    @Test
    void newGameAtSameVenueUpdatesOneExistingPlayerRating() {
        Session session = game(Instant.now().minusSeconds(4 * 3600), SessionStatus.OPEN);
        Profile player = player();
        permit(session, player, AttendanceStatus.ATTENDED, Instant.now());
        Instant original = Instant.now().minusSeconds(6000);
        VenueExperienceRating old = VenueExperienceRating.builder()
                .id(99L).venue(session.getVenue()).profile(player)
                .score(2).createdAt(original).updatedAt(original).build();
        when(ratingRepository.findByVenueIdAndProfileId(100L, 12L))
                .thenReturn(Optional.of(old));
        when(ratingRepository.save(old)).thenReturn(old);

        var result = service.rate(7L, "miguel", new VenueExperienceRatingRequest(4));

        assertEquals(4, result.score());
        assertEquals(99L, old.getId());
        assertEquals(original, old.getCreatedAt());
        assertSame(session, old.getSession());
        verify(ratingRepository).save(same(old));
    }

    @Test
    void pendingNoShowAndLateCancellationCannotRateVenue() {
        Session session = game(Instant.now().minusSeconds(3 * 3600), SessionStatus.COMPLETED);
        Profile player = player();
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("miguel")).thenReturn(Optional.of(player));
        SessionParticipant participation = SessionParticipant.builder()
                .profile(player).session(session).state(ParticipantState.ACTIVE)
                .attendanceStatus(AttendanceStatus.PENDING).attendanceRecordedAt(Instant.now()).build();
        when(participantRepository.findBySessionIdAndProfileIdAndState(
                7L, 12L, ParticipantState.ACTIVE)).thenReturn(Optional.of(participation));

        for (AttendanceStatus status : new AttendanceStatus[]{
                AttendanceStatus.PENDING, AttendanceStatus.NO_SHOW,
                AttendanceStatus.LATE_CANCELLED}) {
            participation.setAttendanceStatus(status);
            assertThrows(AccessDeniedException.class,
                    () -> service.rate(7L, "miguel", new VenueExperienceRatingRequest(5)));
        }
        verifyNoInteractions(ratingRepository);
    }

    @Test
    void attendedWithoutRecordingTimestampCannotRateVenue() {
        Session session = game(Instant.now().minusSeconds(3 * 3600), SessionStatus.OPEN);
        Profile player = player();
        permit(session, player, AttendanceStatus.ATTENDED, null);
        assertThrows(AccessDeniedException.class,
                () -> service.rate(7L, "miguel", new VenueExperienceRatingRequest(5)));
        verifyNoInteractions(ratingRepository);
    }

    @Test
    void outsiderIsNotAllowedToRate() {
        Session session = game(Instant.now().minusSeconds(3 * 3600), SessionStatus.OPEN);
        Profile player = player();
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("miguel")).thenReturn(Optional.of(player));
        assertThrows(AccessDeniedException.class,
                () -> service.rate(7L, "miguel", new VenueExperienceRatingRequest(3)));
        verifyNoInteractions(ratingRepository);
    }

    @Test
    void onlyAnOccurredFinishedSessionWithRegisteredVenueCanBeRated() {
        Session session = game(Instant.now().minusSeconds(15 * 60), SessionStatus.OPEN);
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        assertThrows(IllegalStateException.class,
                () -> service.rate(7L, "miguel", new VenueExperienceRatingRequest(5)));
        session.setStatus(SessionStatus.CANCELLED);
        assertThrows(IllegalStateException.class,
                () -> service.rate(7L, "miguel", new VenueExperienceRatingRequest(5)));
        session.setStatus(SessionStatus.COMPLETED);
        session.setProblemType(SessionProblemType.NOT_OCCURRED);
        assertThrows(IllegalStateException.class,
                () -> service.rate(7L, "miguel", new VenueExperienceRatingRequest(5)));
        session.setProblemType(null);
        session.setGameDate(Date.from(Instant.now().minusSeconds(4*3600)));
        session.setVenue(null); // Google Maps / GPS-only session has no registered facility.
        assertThrows(IllegalStateException.class,
                () -> service.rate(7L, "miguel", new VenueExperienceRatingRequest(5)));
        verifyNoInteractions(ratingRepository, profileRepository);
    }

    @Test
    void invalidScoresCannotReachRepositories() {
        for(Integer score : new Integer[]{null, 0, 6}) {
            assertThrows(IllegalArgumentException.class,
                    () -> service.rate(7L, "miguel", new VenueExperienceRatingRequest(score)));
        }
        verifyNoInteractions(ratingRepository, sessionRepository, profileRepository);
    }

    @Test
    void ownRatingReadsOnlyOwnVenueAndProfile() {
        Session session = game(Instant.now().minusSeconds(3 * 3600), SessionStatus.COMPLETED);
        Profile player = player();
        when(sessionRepository.findById(7L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("miguel")).thenReturn(Optional.of(player));
        when(ratingRepository.findByVenueIdAndProfileId(100L, 12L))
                .thenReturn(Optional.of(VenueExperienceRating.builder()
                        .venue(session.getVenue()).profile(player)
                        .score(4).updatedAt(Instant.now()).build()));
        var result=service.mine(7L,"miguel").orElseThrow();
        assertEquals(100L,result.venueId());
        assertEquals(4,result.score());
        verify(ratingRepository).findByVenueIdAndProfileId(100L,12L);
    }

    @Test
    void fewerThanThreeUniqueRatingsRevealNeitherCountNorAverage() {
        when(venueRepository.existsById(100L)).thenReturn(true);
        when(ratingRepository.countByVenueId(100L)).thenReturn(2L);
        var summary=service.summary(100L);
        assertEquals(0,summary.ratingCount());
        assertNull(summary.averageScore());
        verify(ratingRepository,never()).averageScoreForVenue(any());
    }

    @Test
    void publicAggregateAppearsAtThreeDifferentReviewers() {
        when(venueRepository.existsById(100L)).thenReturn(true);
        when(ratingRepository.countByVenueId(100L)).thenReturn(3L);
        when(ratingRepository.averageScoreForVenue(100L)).thenReturn(4.333333);
        var result=service.summary(100L);
        assertEquals(3,result.ratingCount());
        assertEquals(4.3,result.averageScore());
    }

    @Test
    void unknownVenueNeverGetsAggregate() {
        assertThrows(IllegalArgumentException.class, () -> service.summary(12345L));
        verifyNoInteractions(ratingRepository);
    }

    private void permit(Session session, Profile player, AttendanceStatus status, Instant recorded) {
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("miguel")).thenReturn(Optional.of(player));
        when(participantRepository.findBySessionIdAndProfileIdAndState(
                7L, 12L, ParticipantState.ACTIVE)).thenReturn(Optional.of(SessionParticipant.builder()
                        .session(session).profile(player).state(ParticipantState.ACTIVE)
                        .attendanceStatus(status).attendanceRecordedAt(recorded).build()));
    }

    private static Profile player(){
        Profile p=new Profile();
        p.setId(12L);
        p.setUsername("miguel");
        return p;
    }
    private static Session game(Instant start, SessionStatus status){
        Venue v=new Venue();
        v.setId(100L);
        Session session=new Session();
        session.setId(7L);
        session.setVenue(v);
        session.setGameDate(Date.from(start));
        session.setStatus(status);
        session.setEnabled(true);
        return session;
    }
}
