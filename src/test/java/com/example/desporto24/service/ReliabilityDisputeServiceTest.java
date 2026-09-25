package com.example.desporto24.service;

import com.example.desporto24.configuration.ReliabilityPolicyProperties;
import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.DisputeStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.AttendanceDispute;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.AttendanceDisputeRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReliabilityDisputeServiceTest {

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

        service = new ReliabilityService(
                participantRepository,
                disputeRepository,
                new ReliabilityPolicyProperties(),
                profileRepository,
                sessionRepository);
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
    void duplicateOpenDisputeIsRejected() {
        Profile owner = profile(7L, "miguel");
        SessionParticipant participant = participant(20L, owner, profile(8L, "creator"), AttendanceStatus.NO_SHOW);

        when(participantRepository.findById(20L)).thenReturn(Optional.of(participant));
        when(profileRepository.findUserByUsername("miguel")).thenReturn(Optional.of(owner));
        when(disputeRepository.existsByParticipantIdAndStatus(20L, DisputeStatus.OPEN)).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> service.createDispute(20L, "reason", "miguel"));

        verify(disputeRepository, never()).save(any());
    }

    @Test
    void openDisputeCanBeAcceptedOrRejectedButNotResolvedTwice() {
        AttendanceDispute accepted = dispute(30L, DisputeStatus.OPEN);
        when(disputeRepository.findById(30L)).thenReturn(Optional.of(accepted));
        when(disputeRepository.save(accepted)).thenReturn(accepted);

        service.resolveDispute(30L, DisputeStatus.ACCEPTED);

        assertEquals(DisputeStatus.ACCEPTED, accepted.getStatus());
        assertNotNull(accepted.getResolvedAt());
        verify(disputeRepository).save(accepted);

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
