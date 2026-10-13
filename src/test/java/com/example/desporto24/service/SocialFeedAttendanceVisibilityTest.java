package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.social.ProfileFollowRepository;
import com.example.desporto24.repository.social.SocialPostCommentRepository;
import com.example.desporto24.repository.social.SocialPostLikeRepository;
import com.example.desporto24.repository.social.SocialPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SocialFeedAttendanceVisibilityTest {

    private ProfileRepository profiles;
    private SessionParticipantRepository participants;
    private SocialFeedService service;

    @BeforeEach
    void setUp() {
        profiles = mock(ProfileRepository.class);
        participants = mock(SessionParticipantRepository.class);
        service = new SocialFeedService(
                mock(SocialPostRepository.class),
                mock(ProfileFollowRepository.class),
                mock(SocialPostLikeRepository.class),
                mock(SocialPostCommentRepository.class),
                profiles,
                mock(SessionRepository.class),
                participants
        );
    }

    private Profile profile(long id, String username) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }

    private SessionParticipant attended() {
        SessionParticipant participant = new SessionParticipant();
        participant.setAttendanceStatus(AttendanceStatus.ATTENDED);
        participant.setAttendanceRecordedAt(Instant.now());
        return participant;
    }

    @Test
    void ownerCanHideAttendanceWithoutDeletingTheParticipation() {
        Profile owner = profile(7L, "owner");
        SessionParticipant participant = attended();
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(participants.findBySessionIdAndProfileId(3L, 7L))
                .thenReturn(Optional.of(participant));

        service.hideAttendanceActivity(3L, "owner");

        assertTrue(participant.isFeedHidden());
        assertEquals(AttendanceStatus.ATTENDED, participant.getAttendanceStatus());
        assertNotNull(participant.getAttendanceRecordedAt());
        verify(participants).save(participant);
        verify(participants, never()).delete(any());
    }

    @Test
    void repeatedHideIsIdempotent() {
        Profile owner = profile(7L, "owner");
        SessionParticipant participant = attended();
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(participants.findBySessionIdAndProfileId(3L, 7L))
                .thenReturn(Optional.of(participant));

        service.hideAttendanceActivity(3L, "owner");
        service.hideAttendanceActivity(3L, "owner");

        verify(participants, times(1)).save(participant);
    }

    @Test
    void cannotHideAnotherPlayersAttendance() {
        Profile viewer = profile(8L, "viewer");
        when(profiles.findUserByUsernameIgnoreCase("viewer")).thenReturn(Optional.of(viewer));
        when(participants.findBySessionIdAndProfileId(3L, 8L))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.hideAttendanceActivity(3L, "viewer"));

        verify(participants, never()).save(any());
        verify(participants, never()).findBySessionIdAndProfileId(3L, 7L);
    }

    @Test
    void cannotHideAnAttendanceThatWasNotConfirmed() {
        Profile owner = profile(7L, "owner");
        SessionParticipant participant = new SessionParticipant();
        participant.setAttendanceStatus(AttendanceStatus.PENDING);
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(participants.findBySessionIdAndProfileId(3L, 7L))
                .thenReturn(Optional.of(participant));

        assertThrows(ResponseStatusException.class,
                () -> service.hideAttendanceActivity(3L, "owner"));

        assertFalse(participant.isFeedHidden());
        verify(participants, never()).save(any());
    }
}
