package com.example.desporto24.service;

import com.example.desporto24.configuration.ReliabilityPolicyProperties;
import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.DisputeStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.AttendanceDispute;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.AttendanceDisputeRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReliabilityDisputeServiceTest {

    private SessionParticipantRepository participantRepository;
    private AttendanceDisputeRepository disputeRepository;
    private ProfileRepository profileRepository;
    private SessionRepository sessionRepository;
    private NotificationsRepository notificationsRepository;
    private PilotEventService pilotEventService;
    private ReliabilityService service;

    @BeforeEach
    void setUp() {
        participantRepository = mock(SessionParticipantRepository.class);
        disputeRepository = mock(AttendanceDisputeRepository.class);
        profileRepository = mock(ProfileRepository.class);
        sessionRepository = mock(SessionRepository.class);
        notificationsRepository = mock(NotificationsRepository.class);
        pilotEventService = mock(PilotEventService.class);

        service = new ReliabilityService(
                participantRepository,
                disputeRepository,
                new ReliabilityPolicyProperties(),
                profileRepository,
                sessionRepository,
                notificationsRepository,
                pilotEventService);
    }

    @Test
    void participantCanCreateOpenDisputeForOwnNoShow() {
        Profile participantProfile = profile(7L, "miguel");
        Profile creator = profile(8L, "creator");
        SessionParticipant participant = participant(20L, participantProfile, creator, AttendanceStatus.NO_SHOW);
        participant.setAttendanceReportedBy(creator);

        when(participantRepository.findById(20L)).thenReturn(Optional.of(participant));
        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(participantProfile));
        when(disputeRepository.existsByParticipantIdAndStatus(20L, DisputeStatus.OPEN)).thenReturn(false);
        when(disputeRepository.save(any(AttendanceDispute.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.createDispute(20L, "  Eu estive presente.  ", "miguel");

        var captor = org.mockito.ArgumentCaptor.forClass(AttendanceDispute.class);
        verify(disputeRepository).save(captor.capture());
        AttendanceDispute saved = captor.getValue();
        assertSame(participant, saved.getParticipant());
        assertSame(creator, saved.getReporterProfile());
        assertSame(participantProfile, saved.getDisputingProfile());
        assertEquals("Eu estive presente.", saved.getReason());
        assertEquals(DisputeStatus.OPEN, saved.getStatus());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void anotherUserCannotDisputeSomeoneElsesAttendance() {
        Profile owner = profile(7L, "miguel");
        Profile other = profile(9L, "other");
        SessionParticipant participant = participant(20L, owner, profile(8L, "creator"), AttendanceStatus.NO_SHOW);

        when(participantRepository.findById(20L)).thenReturn(Optional.of(participant));
        when(profileRepository.findUserByUsername("other")).thenReturn(Optional.of(other));

        assertThrows(AccessDeniedException.class,
                () -> service.createDispute(20L, "reason", "other"));

        verify(disputeRepository, never()).save(any());
    }

    @Test
    void onlyNoShowCanBeDisputed() {
        Profile owner = profile(7L, "miguel");
        SessionParticipant participant = participant(20L, owner, profile(8L, "creator"), AttendanceStatus.ATTENDED);

        when(participantRepository.findById(20L)).thenReturn(Optional.of(participant));
        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(owner));

        assertThrows(IllegalStateException.class,
                () -> service.createDispute(20L, "reason", "miguel"));

        verify(disputeRepository, never()).save(any());
    }

    @Test
    void duplicateDisputeIsRejected() {
        Profile owner = profile(7L, "miguel");
        SessionParticipant participant = participant(
                20L,
                owner,
                profile(8L, "creator"),
                AttendanceStatus.NO_SHOW
        );
        AttendanceDispute existing = dispute(30L, DisputeStatus.OPEN);

        when(participantRepository.findById(20L))
                .thenReturn(Optional.of(participant));
        when(profileRepository.findUserByUsername("miguel"))
                .thenReturn(Optional.of(owner));
        when(disputeRepository.findTopByParticipantIdOrderByCreatedAtDesc(20L))
                .thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class,
                () -> service.createDispute(20L, "reason", "miguel"));

        verify(disputeRepository, never()).save(any());
    }

    @Test
    void openDisputesExposeSessionAndParticipantContext() {
        Profile owner = profile(7L, "miguel");
        Profile creator = profile(8L, "creator");
        SessionParticipant participant = participant(
                20L,
                owner,
                creator,
                AttendanceStatus.NO_SHOW
        );
        participant.getSession().setId(10L);
        participant.getSession().setSport("Padel");
        participant.getSession().setCity("Lisboa");
        participant.getSession().setGameDate(
                java.util.Date.from(java.time.Instant.parse("2026-09-20T19:00:00Z"))
        );

        AttendanceDispute dispute = new AttendanceDispute();
        dispute.setId(30L);
        dispute.setParticipant(participant);
        dispute.setDisputingProfile(owner);
        dispute.setReporterProfile(creator);
        dispute.setReason("Estive presente");
        dispute.setStatus(DisputeStatus.OPEN);
        dispute.setCreatedAt(java.time.Instant.parse("2026-09-20T21:00:00Z"));

        when(disputeRepository.findByStatusOrderByCreatedAtAsc(
                eq(DisputeStatus.OPEN),
                any(org.springframework.data.domain.Pageable.class)
        )).thenReturn(List.of(dispute));

        var disputes = service.disputes(DisputeStatus.OPEN);

        assertEquals(1, disputes.size());
        assertEquals(30L, disputes.get(0).id());
        assertEquals("miguel", disputes.get(0).participantUsername());
        assertEquals("creator", disputes.get(0).reporterUsername());
        assertEquals("Padel", disputes.get(0).sport());
    }

    @Test
    void openDisputeCanBeAcceptedOrRejectedButNotResolvedTwice() {
        Profile owner = profile(7L, "miguel");
        Profile creator = profile(8L, "creator");
        SessionParticipant participant = participant(
                20L,
                owner,
                creator,
                AttendanceStatus.NO_SHOW
        );
        participant.getSession().setId(10L);
        participant.getSession().setSport("Padel");

        AttendanceDispute accepted = dispute(30L, DisputeStatus.OPEN);
        accepted.setParticipant(participant);
        accepted.setDisputingProfile(owner);
        accepted.setReporterProfile(creator);

        when(disputeRepository.findById(30L)).thenReturn(Optional.of(accepted));
        when(disputeRepository.save(accepted)).thenReturn(accepted);

        service.resolveDispute(30L, DisputeStatus.ACCEPTED);

        assertEquals(DisputeStatus.ACCEPTED, accepted.getStatus());
        assertNotNull(accepted.getResolvedAt());
        verify(disputeRepository).save(accepted);

        var notificationCaptor =
                org.mockito.ArgumentCaptor.forClass(
                        com.example.desporto24.register.Notifications.Notifications.class
                );
        verify(notificationsRepository).save(notificationCaptor.capture());
        var notification = notificationCaptor.getValue();
        assertEquals("miguel", notification.getUsername());
        assertEquals(10L, notification.getSessionId());
        assertEquals(
                com.example.desporto24.enumeration.NotificationType.ATTENDANCE_DISPUTE_ACCEPTED,
                notification.getType()
        );
        assertTrue(notification.getMessage().contains("foi aceite"));

        assertThrows(IllegalStateException.class,
                () -> service.resolveDispute(30L, DisputeStatus.REJECTED));
    }

    @Test
    void resolutionAcceptsOnlyAcceptedOrRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> service.resolveDispute(30L, DisputeStatus.OPEN));

        verifyNoInteractions(disputeRepository);
    }

    private Profile profile(Long id, String username) {
        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(username);
        return profile;
    }

    private SessionParticipant participant(
            Long id,
            Profile profile,
            Profile creator,
            AttendanceStatus status) {
        Session session = new Session();
        session.setProfile(creator);

        SessionParticipant participant = new SessionParticipant();
        participant.setId(id);
        participant.setProfile(profile);
        participant.setSession(session);
        participant.setAttendanceStatus(status);
        return participant;
    }

    private AttendanceDispute dispute(Long id, DisputeStatus status) {
        AttendanceDispute dispute = new AttendanceDispute();
        dispute.setId(id);
        dispute.setStatus(status);
        return dispute;
    }
}
