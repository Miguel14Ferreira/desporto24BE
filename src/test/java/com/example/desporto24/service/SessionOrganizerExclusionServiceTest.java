package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.OrganizerExclusionReason;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionOrganizerExclusion;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionOrganizerExclusionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class SessionOrganizerExclusionServiceTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private SessionParticipantRepository participantRepository;
    @Mock private ProfileRepository profileRepository;
    @Mock private SessionOrganizerExclusionRepository exclusionRepository;
    @InjectMocks private SessionOrganizerExclusionService service;

    @Test
    void organizerExplicitlyExcludesAfterRecordedNoShowOnlyOnce() {
        Profile organizer = profile(1L, "organizer");
        Profile player = profile(2L, "player");
        Session session = session(organizer);
        SessionParticipant participant = membership(session, player, AttendanceStatus.NO_SHOW);
        participant.setAttendanceRecordedAt(Instant.now());

        when(sessionRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(organizer));
        when(participantRepository.findById(21L)).thenReturn(Optional.of(participant));
        when(exclusionRepository.existsByOrganizerIdAndExcludedId(1L, 2L))
                .thenReturn(false, true);

        assertTrue(service.excludeAfterNoShow(10L, 21L, "organizer"));
        assertTrue(service.excludeAfterNoShow(10L, 21L, "organizer"));
        verify(exclusionRepository).save(argThat(entry ->
                entry.getOrganizer() == organizer && entry.getExcluded() == player
                && entry.getCreatedAt() != null));
        verify(exclusionRepository, times(1)).save(any());
    }

    @Test
    void aMissingOrUnrecordedNoShowNeverTriggersAnExclusion() {
        Profile organizer = profile(1L, "organizer");
        Profile player = profile(2L, "player");
        Session session = session(organizer);
        when(sessionRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(organizer));

        SessionParticipant participant = membership(session, player, AttendanceStatus.PENDING);
        when(participantRepository.findById(21L)).thenReturn(Optional.of(participant));
        assertThrows(IllegalStateException.class,
                () -> service.excludeAfterNoShow(10L, 21L, "organizer"));

        participant.setAttendanceStatus(AttendanceStatus.NO_SHOW);
        assertThrows(IllegalStateException.class,
                () -> service.excludeAfterNoShow(10L, 21L, "organizer"));
        verifyNoInteractions(exclusionRepository);
    }

    @Test
    void otherUsersCannotExcludeOrRemoveAnyoneFromAnOrganizersSessions() {
        Profile owner = profile(1L, "owner");
        Session session = session(owner);
        when(sessionRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(session));
        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("intruder"))
                .thenReturn(Optional.of(profile(3L, "intruder")));

        assertThrows(AccessDeniedException.class,
                () -> service.excludeAfterNoShow(10L, 21L, "intruder"));
        assertThrows(AccessDeniedException.class,
                () -> service.remove(10L, 21L, "intruder"));
        assertThrows(AccessDeniedException.class,
                () -> service.status(10L, 21L, "intruder"));
        verifyNoInteractions(participantRepository, exclusionRepository);
    }

    @Test
    void organizerCanReverseExclusionAndCheckItsStatus() {
        Profile organizer = profile(1L, "organizer");
        Session session = session(organizer);
        SessionParticipant participant = membership(session, profile(2L, "player"), AttendanceStatus.NO_SHOW);

        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(organizer));
        when(participantRepository.findById(21L)).thenReturn(Optional.of(participant));
        when(exclusionRepository.existsByOrganizerIdAndExcludedId(1L, 2L)).thenReturn(true);

        assertTrue(service.status(10L, 21L, "organizer"));
        assertFalse(service.remove(10L, 21L, "organizer"));
        verify(exclusionRepository).deleteByOrganizerIdAndExcludedId(1L, 2L);
    }

    @Test
    void cannotExcludeParticipantFromDifferentSession() {
        Profile organizer = profile(1L, "organizer");
        Session session = session(organizer);
        Session other = new Session();
        other.setId(99L);
        SessionParticipant participant = membership(other, profile(2L, "player"), AttendanceStatus.NO_SHOW);
        participant.setAttendanceRecordedAt(Instant.now());

        when(sessionRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(organizer));
        when(participantRepository.findById(21L)).thenReturn(Optional.of(participant));

        assertThrows(IllegalArgumentException.class,
                () -> service.excludeAfterNoShow(10L, 21L, "organizer"));
        verifyNoInteractions(exclusionRepository);
    }


    @Test
    void organizerCanExcludeAttendeeForDisrespectWithoutRecordingAFalseNoShow() {
        Profile organizer = profile(1L, "organizer");
        Profile player = profile(2L, "player");
        Session session = session(organizer);
        SessionParticipant participant = membership(session, player, AttendanceStatus.ATTENDED);
        participant.setAttendanceRecordedAt(Instant.now());

        when(sessionRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(organizer));
        when(participantRepository.findById(21L)).thenReturn(Optional.of(participant));

        assertTrue(service.excludeForReason(10L, 21L, "organizer", OrganizerExclusionReason.DISRESPECTFUL_BEHAVIOUR));
        verify(exclusionRepository).save(argThat(entry ->
                entry.getOrganizer() == organizer && entry.getExcluded() == player
                && entry.getReason() == OrganizerExclusionReason.DISRESPECTFUL_BEHAVIOUR
                && entry.getCreatedAt() != null));
        assertEquals(AttendanceStatus.ATTENDED, participant.getAttendanceStatus());
    }

    @Test
    void organizerCanActOnMisconductWithoutChangingPendingAttendance() {
        Profile organizer = profile(1L, "organizer");
        Session session = session(organizer);
        SessionParticipant participant = membership(session, profile(2L, "player"), AttendanceStatus.PENDING);
        when(sessionRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(organizer));
        when(participantRepository.findById(21L)).thenReturn(Optional.of(participant));

        assertTrue(service.excludeForReason(10L, 21L, "organizer", OrganizerExclusionReason.ABUSIVE_BEHAVIOUR));
        assertEquals(AttendanceStatus.PENDING, participant.getAttendanceStatus());
    }

    @Test
    void organizerCanUpdatePrivateReasonWithoutCreatingDuplicateExclusions() {
        Profile organizer = profile(1L, "organizer");
        Profile player = profile(2L, "player");
        Session session = session(organizer);
        SessionParticipant participant = membership(session, player, AttendanceStatus.ATTENDED);
        var existing = SessionOrganizerExclusion.builder().id(31L)
                .organizer(organizer).excluded(player).reason(OrganizerExclusionReason.OTHER)
                .createdAt(Instant.parse("2026-10-01T10:00:00Z")).build();
        when(sessionRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(organizer));
        when(participantRepository.findById(21L)).thenReturn(Optional.of(participant));
        when(exclusionRepository.findByOrganizerIdAndExcludedId(1L, 2L)).thenReturn(Optional.of(existing));

        assertTrue(service.excludeForReason(10L, 21L, "organizer", OrganizerExclusionReason.UNSPORTING_CONDUCT));
        assertEquals(31L, existing.getId());
        assertEquals(OrganizerExclusionReason.UNSPORTING_CONDUCT, existing.getReason());
        assertEquals(Instant.parse("2026-10-01T10:00:00Z"), existing.getCreatedAt());
        verify(exclusionRepository, times(1)).save(same(existing));
    }

    @Test
    void cannotUseANoShowReasonForPresentPlayer() {
        Profile organizer = profile(1L, "organizer");
        Session session = session(organizer);
        SessionParticipant participant = membership(session, profile(2L, "player"), AttendanceStatus.ATTENDED);
        when(sessionRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(organizer));
        when(participantRepository.findById(21L)).thenReturn(Optional.of(participant));
        assertThrows(IllegalStateException.class, () -> service.excludeForReason(
                10L, 21L, "organizer", OrganizerExclusionReason.NO_SHOW));
        verifyNoInteractions(exclusionRepository);
    }

    @Test
    void outsiderCannotUseBehaviourReasonToBanAnyone() {
        Profile owner = profile(1L, "owner");
        when(sessionRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(session(owner)));
        when(profileRepository.findUserByUsernameIgnoreCase("outsider"))
                .thenReturn(Optional.of(profile(3L, "outsider")));
        assertThrows(AccessDeniedException.class, () -> service.excludeForReason(
                10L, 21L, "outsider", OrganizerExclusionReason.ABUSIVE_BEHAVIOUR));
        verifyNoInteractions(participantRepository, exclusionRepository);
    }

    @Test
    void organizerCannotExcludeThemselfOrAPlayerFromAnotherSession() {
        Profile owner = profile(1L, "owner");
        Session game = session(owner);
        var participant = membership(game, owner, AttendanceStatus.PENDING);
        when(sessionRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(game));
        when(profileRepository.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(participantRepository.findById(21L)).thenReturn(Optional.of(participant));

        assertThrows(IllegalArgumentException.class, () -> service.excludeForReason(
                10L, 21L, "owner", OrganizerExclusionReason.OTHER));
        Session other = new Session();
        other.setId(22L);
        participant.setProfile(profile(2L, "player"));
        participant.setSession(other);
        assertThrows(IllegalArgumentException.class, () -> service.excludeForReason(
                10L, 21L, "owner", OrganizerExclusionReason.OTHER));
        verifyNoInteractions(exclusionRepository);
    }

    private Profile profile(Long id, String username) {
        Profile p = new Profile();
        p.setId(id);
        p.setUsername(username);
        return p;
    }

    private Session session(Profile organizer) {
        Session s = new Session();
        s.setId(10L);
        s.setProfile(organizer);
        return s;
    }

    private SessionParticipant membership(Session session, Profile profile, AttendanceStatus attendance) {
        SessionParticipant p = new SessionParticipant();
        p.setId(21L);
        p.setSession(session);
        p.setProfile(profile);
        p.setAttendanceStatus(attendance);
        return p;
    }
}
