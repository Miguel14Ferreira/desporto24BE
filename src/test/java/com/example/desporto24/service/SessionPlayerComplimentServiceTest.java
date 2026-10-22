package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.PlayerComplimentType;
import com.example.desporto24.enumeration.SessionProblemType;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.PlayerComplimentRequest;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.session.SessionPlayerCompliment;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionPlayerComplimentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionPlayerComplimentServiceTest {
    @Mock SessionRepository sessionRepository;
    @Mock ProfileRepository profileRepository;
    @Mock SessionParticipantRepository participantRepository;
    @Mock SessionPlayerComplimentRepository complimentRepository;
    @InjectMocks SessionPlayerComplimentService service;

    @Test
    void twoVerifiedPlayersMayExchangeOnePositiveCompliment() {
        Session s = game(Instant.now().minusSeconds(3 * 3600));
        Profile sender = player(12L), receiver = player(13L);
        allowSessionAndSender(s, sender);
        when(profileRepository.findUserByUsernameIgnoreCase("receiver")).thenReturn(Optional.of(receiver));
        when(participantRepository.findBySessionIdAndProfileIdAndState(7L, 12L, ParticipantState.ACTIVE))
                .thenReturn(Optional.of(attended(sender, AttendanceStatus.ATTENDED, Instant.now())));
        when(participantRepository.findBySessionIdAndProfileIdAndState(7L, 13L, ParticipantState.ACTIVE))
                .thenReturn(Optional.of(attended(receiver, AttendanceStatus.ATTENDED, Instant.now())));
        when(complimentRepository.findBySessionIdAndSenderIdAndRecipientId(7L, 12L, 13L))
                .thenReturn(Optional.empty());
        when(complimentRepository.save(any(SessionPlayerCompliment.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var result = service.give(7L, "receiver", "player", new PlayerComplimentRequest(PlayerComplimentType.FAIR_PLAY));

        assertEquals(12L, result.senderProfileId());
        assertEquals(13L, result.recipientProfileId());
        assertEquals(PlayerComplimentType.FAIR_PLAY, result.type());
        verify(complimentRepository).save(argThat(c ->
                c.getSession() == s && c.getSender() == sender && c.getRecipient() == receiver
                && c.getCreatedAt() != null && c.getUpdatedAt() != null));
    }

    @Test
    void sameSenderAndReceiverAndGameOnlyUpdatesExistingCompliment() {
        Session s = game(Instant.now().minusSeconds(3 * 3600));
        Profile sender = player(12L), receiver = player(13L);
        allowSessionAndSender(s, sender);
        when(profileRepository.findUserByUsernameIgnoreCase("receiver")).thenReturn(Optional.of(receiver));
        when(participantRepository.findBySessionIdAndProfileIdAndState(7L, 12L, ParticipantState.ACTIVE))
                .thenReturn(Optional.of(attended(sender, AttendanceStatus.ATTENDED, Instant.now())));
        when(participantRepository.findBySessionIdAndProfileIdAndState(7L, 13L, ParticipantState.ACTIVE))
                .thenReturn(Optional.of(attended(receiver, AttendanceStatus.ATTENDED, Instant.now())));
        Instant first = Instant.now().minusSeconds(900);
        var existing = SessionPlayerCompliment.builder().id(31L).session(s).sender(sender)
                .recipient(receiver).type(PlayerComplimentType.FAIR_PLAY)
                .createdAt(first).updatedAt(first).build();
        when(complimentRepository.findBySessionIdAndSenderIdAndRecipientId(7L, 12L, 13L))
                .thenReturn(Optional.of(existing));
        when(complimentRepository.save(existing)).thenReturn(existing);

        var changed = service.give(7L, "receiver", "player",
                new PlayerComplimentRequest(PlayerComplimentType.GREAT_TEAMMATE));

        assertEquals(PlayerComplimentType.GREAT_TEAMMATE, changed.type());
        assertEquals(first, existing.getCreatedAt());
        assertEquals(31L, existing.getId());
        verify(complimentRepository).save(same(existing));
    }

    @Test
    void noSelfCompliments() {
        Session s = game(Instant.now().minusSeconds(3 * 3600));
        allowSessionAndSender(s, player(12L));
        assertThrows(IllegalArgumentException.class, () -> service.give(
                7L, "player", "player", new PlayerComplimentRequest(PlayerComplimentType.FAIR_PLAY)));
        verifyNoInteractions(participantRepository, complimentRepository);
    }

    @Test
    void missingOrInvalidTypeCannotBeSaved() {
        assertThrows(IllegalArgumentException.class, () -> service.give(7L, "receiver", "player", null));
        assertThrows(IllegalArgumentException.class, () -> service.give(
                7L, "receiver", "player", new PlayerComplimentRequest(null)));
        assertThrows(IllegalArgumentException.class, () -> service.give(
                7L, "", "player", new PlayerComplimentRequest(PlayerComplimentType.FAIR_PLAY)));
        verifyNoInteractions(sessionRepository, complimentRepository);
    }

    @Test
    void bothSenderAndRecipientMustHaveConfirmedAttendance() {
        Session s = game(Instant.now().minusSeconds(3 * 3600));
        Profile sender = player(12L), receiver = player(13L);
        allowSessionAndSender(s, sender);
        when(profileRepository.findUserByUsernameIgnoreCase("receiver")).thenReturn(Optional.of(receiver));
        when(participantRepository.findBySessionIdAndProfileIdAndState(7L, 12L, ParticipantState.ACTIVE))
                .thenReturn(Optional.of(attended(sender, AttendanceStatus.ATTENDED, Instant.now())));
        var recipient = attended(receiver, AttendanceStatus.PENDING, Instant.now());
        when(participantRepository.findBySessionIdAndProfileIdAndState(7L, 13L, ParticipantState.ACTIVE))
                .thenReturn(Optional.of(recipient));

        for (AttendanceStatus status : new AttendanceStatus[]{
                AttendanceStatus.PENDING, AttendanceStatus.NO_SHOW, AttendanceStatus.LATE_CANCELLED}) {
            recipient.setAttendanceStatus(status);
            assertThrows(AccessDeniedException.class, () -> service.give(
                    7L, 13L, "player", new PlayerComplimentRequest(PlayerComplimentType.FAIR_PLAY)));
        }
        verifyNoInteractions(complimentRepository);
    }

    @Test
    void senderAttendanceMustHaveRecordedTimestamp() {
        Session s = game(Instant.now().minusSeconds(3 * 3600));
        Profile sender = player(12L);
        allowSessionAndSender(s, sender);
        when(profileRepository.findUserByUsernameIgnoreCase("receiver")).thenReturn(Optional.of(receiver));
        when(participantRepository.findBySessionIdAndProfileIdAndState(7L, 12L, ParticipantState.ACTIVE))
                .thenReturn(Optional.of(attended(sender, AttendanceStatus.ATTENDED, null)));
        assertThrows(AccessDeniedException.class, () -> service.give(
                7L, "receiver", "player", new PlayerComplimentRequest(PlayerComplimentType.FAIR_PLAY)));
        verifyNoInteractions(complimentRepository);
    }

    @Test
    void futureCancelledAndNotOccurredSessionsAreRejected() {
        Session s = game(Instant.now().minusSeconds(30 * 60));
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(s));
        var request = new PlayerComplimentRequest(PlayerComplimentType.POSITIVE_ENERGY);
        assertThrows(IllegalStateException.class, () -> service.give(7L, "receiver", "player", request));
        s.setGameDate(Date.from(Instant.now().minusSeconds(3 * 3600)));
        s.setStatus(SessionStatus.CANCELLED);
        assertThrows(IllegalStateException.class, () -> service.give(7L, "receiver", "player", request));
        s.setStatus(SessionStatus.COMPLETED);
        s.setProblemType(SessionProblemType.NOT_OCCURRED);
        assertThrows(IllegalStateException.class, () -> service.give(7L, "receiver", "player", request));
        verifyNoInteractions(profileRepository, complimentRepository);
    }

    @Test
    void sentAndReceivedRemainPrivateToAuthenticatedProfile() {
        Profile sender = player(12L), receiver = player(13L);
        when(sessionRepository.existsById(7L)).thenReturn(true);
        when(profileRepository.findUserByUsernameIgnoreCase("player")).thenReturn(Optional.of(sender));
        when(complimentRepository.findBySessionIdAndSenderIdOrderByUpdatedAtDesc(7L, 12L))
                .thenReturn(List.of(SessionPlayerCompliment.builder().sender(sender).recipient(receiver)
                        .type(PlayerComplimentType.FAIR_PLAY).updatedAt(Instant.now()).build()));
        when(complimentRepository.findBySessionIdAndRecipientIdOrderByUpdatedAtDesc(7L, 12L))
                .thenReturn(List.of());

        assertEquals(1, service.sent(7L, "player").size());
        assertTrue(service.received(7L, "player").isEmpty());
        verify(complimentRepository).findBySessionIdAndSenderIdOrderByUpdatedAtDesc(7L, 12L);
        verify(complimentRepository).findBySessionIdAndRecipientIdOrderByUpdatedAtDesc(7L, 12L);
    }

    private void allowSessionAndSender(Session session, Profile sender) {
        when(sessionRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(session));
        when(profileRepository.findUserByUsernameIgnoreCase("player")).thenReturn(Optional.of(sender));
    }

    private static Session game(Instant start) {
        var s = new Session();
        s.setId(7L);
        s.setGameDate(Date.from(start));
        s.setStatus(SessionStatus.COMPLETED);
        s.setEnabled(true);
        return s;
    }

    private static Profile player(long id) {
        var p = new Profile();
        p.setId(id);
        return p;
    }

    private static SessionParticipant attended(Profile p, AttendanceStatus status, Instant recordedAt) {
        return SessionParticipant.builder().profile(p).state(ParticipantState.ACTIVE)
                .attendanceStatus(status).attendanceRecordedAt(recordedAt).build();
    }
}
