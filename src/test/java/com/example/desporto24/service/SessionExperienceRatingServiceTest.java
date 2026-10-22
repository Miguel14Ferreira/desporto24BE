package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.SessionExperienceRatingRequest;
import com.example.desporto24.model.session.SessionExperienceRating;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionExperienceRatingRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
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
class SessionExperienceRatingServiceTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private ProfileRepository profileRepository;
    @Mock private SessionParticipantRepository participantRepository;
    @Mock private SessionExperienceRatingRepository ratingRepository;
    @InjectMocks private SessionExperienceRatingService service;

    @Test
    void registeredPlayerCanRateFinishedGameOnce() {
        Profile player = player();
        Session session = session(Instant.now().minusSeconds(3 * 3600), SessionStatus.OPEN);
        allowRating(session, player);
        when(ratingRepository.findBySessionIdAndProfileId(7L, 12L))
                .thenReturn(Optional.empty());
        when(ratingRepository.save(any(SessionExperienceRating.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.rate(7L, "miguel", new SessionExperienceRatingRequest(5));

        assertEquals(5, result.overallScore());
        assertNotNull(result.updatedAt());
        verify(ratingRepository, times(1)).save(argThat(rating ->
                rating.getProfile() == player
                && rating.getSession() == session
                && rating.getCreatedAt() != null));
    }

    @Test
    void subsequentRatingEditsExistingRecordInsteadOfCreatingAnother() {
        Profile player = player();
        Session session = session(Instant.now().minusSeconds(3 * 3600), SessionStatus.COMPLETED);
        allowRating(session, player);
        Instant created = Instant.now().minusSeconds(100);
        SessionExperienceRating existing = SessionExperienceRating.builder()
                .id(15L).session(session).profile(player)
                .overallScore(2)
                .createdAt(created).updatedAt(created).build();
        when(ratingRepository.findBySessionIdAndProfileId(7L, 12L))
                .thenReturn(Optional.of(existing));
        when(ratingRepository.save(existing)).thenReturn(existing);

        var result = service.rate(7L, "miguel", new SessionExperienceRatingRequest(4));

        assertEquals(4, result.overallScore());
        assertEquals(15L, existing.getId());
        assertEquals(created, existing.getCreatedAt());
        verify(ratingRepository).save(same(existing));
    }

    @Test
    void outsiderCannotRateEvenAfterTheGame() {
        Profile player = player();
        Session session = session(Instant.now().minusSeconds(3 * 3600), SessionStatus.OPEN);
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("miguel")).thenReturn(Optional.of(player));

        assertThrows(AccessDeniedException.class, () ->
                service.rate(7L, "miguel", new SessionExperienceRatingRequest(3)));
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void aRegisteredPlayerWithPendingOrMissedAttendanceCannotRate() {
        Profile player = player();
        Session session = session(Instant.now().minusSeconds(3 * 3600), SessionStatus.COMPLETED);
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("miguel"))
                .thenReturn(Optional.of(player));

        SessionParticipant participant = SessionParticipant.builder()
                .session(session).profile(player)
                .state(ParticipantState.ACTIVE)
                .attendanceStatus(AttendanceStatus.PENDING)
                .build();
        when(participantRepository.findBySessionIdAndProfileIdAndState(
                7L, 12L, ParticipantState.ACTIVE))
                .thenReturn(Optional.of(participant));

        for (AttendanceStatus status : new AttendanceStatus[] {
                AttendanceStatus.PENDING,
                AttendanceStatus.NO_SHOW,
                AttendanceStatus.LATE_CANCELLED
        }) {
            participant.setAttendanceStatus(status);
            // Even a previously recorded NO_SHOW or late cancellation is not attendance.
            participant.setAttendanceRecordedAt(Instant.now());
            var error = assertThrows(AccessDeniedException.class,
                    () -> service.rate(7L, "miguel", new SessionExperienceRatingRequest(4)));
            assertTrue(error.getMessage().contains("confirmed attendance"));
        }
        verifyNoInteractions(ratingRepository);
    }

    @Test
    void attendedFlagWithoutRecordTimestampIsNotProofOfPresence() {
        Profile player = player();
        Session session = session(Instant.now().minusSeconds(3 * 3600), SessionStatus.COMPLETED);
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("miguel"))
                .thenReturn(Optional.of(player));
        when(participantRepository.findBySessionIdAndProfileIdAndState(
                7L, 12L, ParticipantState.ACTIVE))
                .thenReturn(Optional.of(SessionParticipant.builder()
                        .session(session).profile(player)
                        .state(ParticipantState.ACTIVE)
                        .attendanceStatus(AttendanceStatus.ATTENDED)
                        .build()));

        assertThrows(AccessDeniedException.class, () ->
                service.rate(7L, "miguel", new SessionExperienceRatingRequest(5)));
        verifyNoInteractions(ratingRepository);
    }

    @Test
    void aPlayerWhoLeftCannotRateEvenIfPreviouslyAttended() {
        Profile player = player();
        Session session = session(Instant.now().minusSeconds(3 * 3600), SessionStatus.COMPLETED);
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("miguel"))
                .thenReturn(Optional.of(player));

        // The rating query only accepts state ACTIVE, not LEFT or REMOVED.
        assertThrows(AccessDeniedException.class, () ->
                service.rate(7L, "miguel", new SessionExperienceRatingRequest(5)));
        verify(participantRepository).findBySessionIdAndProfileIdAndState(
                7L, 12L, ParticipantState.ACTIVE);
        verifyNoInteractions(ratingRepository);
    }

    @Test
    void cannotRateBeforeGameHasEnded() {
        Profile player = player();
        Session session = session(Instant.now().minusSeconds(30 * 60), SessionStatus.OPEN);
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("miguel")).thenReturn(Optional.of(player));

        assertThrows(IllegalStateException.class, () ->
                service.rate(7L, "miguel", new SessionExperienceRatingRequest(3)));
        verifyNoInteractions(participantRepository, ratingRepository);
    }

    @Test
    void cannotRateCancelledOrNotOccurredGame() {
        Profile player = player();
        Session cancelled = session(Instant.now().minusSeconds(3 * 3600), SessionStatus.CANCELLED);
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(cancelled));
        when(profileRepository.findUserByUsernameIgnoreCase("miguel")).thenReturn(Optional.of(player));
        assertThrows(IllegalStateException.class, () ->
                service.rate(7L, "miguel", new SessionExperienceRatingRequest(3)));

        cancelled.setStatus(SessionStatus.OPEN);
        cancelled.setProblemType(SessionProblemType.NOT_OCCURRED);
        assertThrows(IllegalStateException.class, () ->
                service.rate(7L, "miguel", new SessionExperienceRatingRequest(3)));
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void invalidScoresAreRejectedWithoutDatabaseChanges() {
        for (var invalid : new SessionExperienceRatingRequest[] {
                new SessionExperienceRatingRequest(0),
                new SessionExperienceRatingRequest(6),
                new SessionExperienceRatingRequest(null)
        }) {
            assertThrows(IllegalArgumentException.class, () ->
                    service.rate(7L, "miguel", invalid));
        }
        verifyNoInteractions(sessionRepository, ratingRepository);
    }

    @Test
    void mineReadsOnlyTheCurrentProfilesOwnPrivateRating() {
        Profile player = player();
        when(sessionRepository.existsById(7L)).thenReturn(true);
        when(profileRepository.findUserByUsernameIgnoreCase("miguel")).thenReturn(Optional.of(player));
        when(ratingRepository.findBySessionIdAndProfileId(7L, 12L))
                .thenReturn(Optional.of(SessionExperienceRating.builder()
                        .overallScore(5)
                        .updatedAt(Instant.now()).build()));

        var result = service.mine(7L, "miguel");

        assertTrue(result.isPresent());
        assertEquals(5, result.orElseThrow().overallScore());
        verify(ratingRepository).findBySessionIdAndProfileId(7L, 12L);
    }

    private void allowRating(Session session, Profile player) {
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("miguel")).thenReturn(Optional.of(player));
        when(participantRepository.findBySessionIdAndProfileIdAndState(
                7L, 12L, ParticipantState.ACTIVE))
                .thenReturn(Optional.of(SessionParticipant.builder()
                        .session(session).profile(player)
                        .state(ParticipantState.ACTIVE)
                        .attendanceStatus(AttendanceStatus.ATTENDED)
                        .attendanceRecordedAt(Instant.now())
                        .build()));
    }

    private Profile player() {
        Profile profile = new Profile();
        profile.setId(12L);
        profile.setUsername("miguel");
        return profile;
    }

    private Session session(Instant scheduled, SessionStatus status) {
        Session session = new Session();
        session.setId(7L);
        session.setGameDate(Date.from(scheduled));
        session.setStatus(status);
        session.setEnabled(true);
        return session;
    }
}
