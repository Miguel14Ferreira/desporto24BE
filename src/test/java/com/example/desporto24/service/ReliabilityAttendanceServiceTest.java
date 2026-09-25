package com.example.desporto24.service;

import com.example.desporto24.configuration.ReliabilityPolicyProperties;
import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.AttendanceDisputeRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReliabilityAttendanceServiceTest {

    private SessionParticipantRepository participantRepository;
    private AttendanceDisputeRepository disputeRepository;
    private ProfileRepository profileRepository;
    private SessionRepository sessionRepository;
    private ReliabilityService service;

    @BeforeEach
    void setUp() {
        participantRepository = mock(SessionParticipantRepository.class);
        disputeRepository = mock(AttendanceDisputeRepository.class);
        profileRepository = mock(ProfileRepository.class);
        sessionRepository = mock(SessionRepository.class);

        ReliabilityPolicyProperties properties = new ReliabilityPolicyProperties();
        properties.setAttendanceMarkingDelayMinutes(30);

        service = new ReliabilityService(
                participantRepository,
                disputeRepository,
                properties,
                profileRepository,
                sessionRepository);
    }

    @Test
    void onlyCreatorCanRecordAttendance() {
        Profile creator = profile(1L, "creator");
        Profile other = profile(2L, "other");
        Session session = session(10L, creator, Instant.now().minus(Duration.ofHours(1)));

        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsername("other")).thenReturn(Optional.of(other));

        assertThrows(AccessDeniedException.class, () ->
                service.recordAttendance(10L, 20L, AttendanceStatus.NO_SHOW, "other"));

        verify(participantRepository, never()).save(any());
    }

    @Test
    void attendanceCannotBeRecordedBeforeThirtyMinuteDelay() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator, Instant.now().minus(Duration.ofMinutes(10)));
        SessionParticipant participant = participant(20L, session);

        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsername("creator")).thenReturn(Optional.of(creator));
        when(participantRepository.findById(20L)).thenReturn(Optional.of(participant));

        assertThrows(IllegalStateException.class, () ->
                service.recordAttendance(10L, 20L, AttendanceStatus.ATTENDED, "creator"));

        verify(participantRepository, never()).save(any());
    }

    @Test
    void attendanceAcceptsOnlyAttendedOrNoShow() {
        assertThrows(IllegalArgumentException.class, () ->
                service.recordAttendance(10L, 20L, AttendanceStatus.PENDING, "creator"));

        assertThrows(IllegalArgumentException.class, () ->
                service.recordAttendance(10L, 20L, AttendanceStatus.LATE_CANCELLED, "creator"));

        verifyNoInteractions(sessionRepository, profileRepository, participantRepository);
    }

    @Test
    void creatorCanRecordAttendanceAfterDelayAndAuditIsStored() {
        Profile creator = profile(1L, "creator");
        Session session = session(10L, creator, Instant.now().minus(Duration.ofMinutes(31)));
        SessionParticipant participant = participant(20L, session);

        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsername("creator")).thenReturn(Optional.of(creator));
        when(participantRepository.findById(20L)).thenReturn(Optional.of(participant));

        Instant before = Instant.now();
        service.recordAttendance(10L, 20L, AttendanceStatus.NO_SHOW, "creator");
        Instant after = Instant.now();

        assertEquals(AttendanceStatus.NO_SHOW, participant.getAttendanceStatus());
        assertSame(creator, participant.getAttendanceReportedBy());
        assertNotNull(participant.getAttendanceRecordedAt());
        assertFalse(participant.getAttendanceRecordedAt().isBefore(before));
        assertFalse(participant.getAttendanceRecordedAt().isAfter(after));
        verify(participantRepository).save(participant);
    }

    private Profile profile(Long id, String username) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }

    private Session session(Long id, Profile creator, Instant start) {
        Session session = new Session();
        session.setId(id);
        session.setProfile(creator);
        session.setGameDate(Date.from(start));
        return session;
    }

    private SessionParticipant participant(Long id, Session session) {
        SessionParticipant participant = new SessionParticipant();
        participant.setId(id);
        participant.setSession(session);
        participant.setAttendanceStatus(AttendanceStatus.PENDING);
        return participant;
    }
}
