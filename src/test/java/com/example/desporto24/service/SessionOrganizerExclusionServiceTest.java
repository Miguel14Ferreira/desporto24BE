package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
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
