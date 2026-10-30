package com.example.desporto24.service;

import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.enumeration.SessionInviteStatus;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.session.JoinSessionResponse;
import com.example.desporto24.enumeration.JoinState;
import com.example.desporto24.service.interfaces.SessionService;
import com.example.desporto24.model.session.SessionInvite;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionInviteRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import com.example.desporto24.model.session.SessionWaitlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionInviteServiceTest {
    @Mock SessionRepository sessions;
    @Mock ProfileRepository profiles;
    @Mock SessionInviteRepository invites;
    @Mock NotificationsRepository notifications;
    @Mock SessionParticipantRepository participants;
    @Mock SessionWaitlistRepository waitlist;
    @Mock SessionService sessionService;

    private SessionInviteService service;
    private Session session;
    private Profile owner;
    private Profile recipient;

    @BeforeEach
    void setup() {
        service = new SessionInviteService(sessions, profiles, invites, notifications, participants, waitlist, sessionService);
        owner = new Profile();
        owner.setId(10L);
        owner.setUsername("organizador");
        recipient = new Profile();
        recipient.setId(20L);
        recipient.setUsername("jogador");
        session = new Session();
        session.setId(1L);
        session.setProfile(owner);
        session.setAudience(SessionAudience.PUBLIC);
        session.setStatus(SessionStatus.OPEN);
        session.setGameDate(Date.from(Instant.now().plusSeconds(7200)));
    }

    private void prepare() {
        when(sessions.findByIdForUpdate(1L)).thenReturn(Optional.of(session));
        lenient().when(profiles.findUserByUsername("organizador")).thenReturn(Optional.of(owner));
    }

    private void assertFailure(HttpStatus status, String player) {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.invite(1L, "organizador", player));
        assertEquals(status, error.getStatusCode());
        verifyNoInteractions(notifications);
    }

    @Test
    void createsPendingInviteAndNotification() {
        prepare();
        when(profiles.findUserByUsernameIgnoreCase("jogador")).thenReturn(Optional.of(recipient));
        when(invites.findBySessionIdAndInviteeIdAndStatus(1L, 20L, SessionInviteStatus.PENDING))
                .thenReturn(Optional.empty());
        when(invites.save(any(SessionInvite.class))).thenAnswer(call -> {
            SessionInvite entity = call.getArgument(0);
            entity.setId(99L);
            return entity;
        });
        assertEquals(99L, service.invite(1L, "organizador", "jogador"));
        ArgumentCaptor<SessionInvite> entity = ArgumentCaptor.forClass(SessionInvite.class);
        verify(invites).save(entity.capture());
        assertEquals(SessionInviteStatus.PENDING, entity.getValue().getStatus());
        assertSame(owner, entity.getValue().getInviter());
        assertSame(recipient, entity.getValue().getInvitee());
        ArgumentCaptor<Notifications> notice = ArgumentCaptor.forClass(Notifications.class);
        verify(notifications).save(notice.capture());
        assertEquals(NotificationType.SESSION_INVITE, notice.getValue().getType());
        assertEquals(1L, notice.getValue().getSessionId());
        assertEquals("jogador", notice.getValue().getUsername());
    }

    private void arrangeSuccessfulInvite() {
        when(profiles.findUserByUsernameIgnoreCase("jogador")).thenReturn(Optional.of(recipient));
        when(invites.save(any(SessionInvite.class))).thenAnswer(call -> {
            SessionInvite value = call.getArgument(0);
            value.setId(99L);
            return value;
        });
    }

    @Test
    void rejectsNonOrganizer() {
        prepare();
        Profile another = new Profile();
        another.setId(50L);
        session.setProfile(another);
        assertFailure(HttpStatus.FORBIDDEN, "jogador");
        verifyNoInteractions(invites);
    }

    @Test
    void rejectsCancelledSession() {
        prepare();
        session.setStatus(SessionStatus.CANCELLED);
        assertFailure(HttpStatus.CONFLICT, "jogador");
        verifyNoInteractions(invites);
    }

    @Test
    void rejectsPastSession() {
        prepare();
        session.setGameDate(Date.from(Instant.now().minusSeconds(60)));
        assertFailure(HttpStatus.CONFLICT, "jogador");
        verifyNoInteractions(invites);
    }

    @Test
    void permitsPrivateSessionForOrganiser() {
        prepare();
        session.setPrivateGame(true);
        arrangeSuccessfulInvite();
        assertEquals(99L, service.invite(1L, "organizador", "jogador"));
    }

    @Test
    void rejectsCompletedSession() {
        prepare();
        session.setStatus(SessionStatus.COMPLETED);
        assertFailure(HttpStatus.CONFLICT, "jogador");
        verifyNoInteractions(invites);
    }

    @Test
    void permitsCommunityOnlySessionForOrganiser() {
        prepare();
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        arrangeSuccessfulInvite();
        assertEquals(99L, service.invite(1L, "organizador", "jogador"));
    }

    @Test
    void rejectsAlreadyJoinedPlayer() {
        prepare();
        when(profiles.findUserByUsernameIgnoreCase("jogador")).thenReturn(Optional.of(recipient));
        when(participants.existsBySessionIdAndProfileIdAndState(1L, 20L, ParticipantState.ACTIVE))
                .thenReturn(true);
        assertFailure(HttpStatus.CONFLICT, "jogador");
        verify(invites, never()).save(any());
    }

    @Test
    void rejectsWaitlistedPlayer() {
        prepare();
        when(profiles.findUserByUsernameIgnoreCase("jogador")).thenReturn(Optional.of(recipient));
        when(waitlist.findBySessionIdAndProfileId(1L, 20L))
                .thenReturn(Optional.of(new SessionWaitlist()));
        assertFailure(HttpStatus.CONFLICT, "jogador");
        verify(invites, never()).save(any());
    }

    @Test
    void rejectsDuplicatePendingInvite() {
        prepare();
        when(profiles.findUserByUsernameIgnoreCase("jogador")).thenReturn(Optional.of(recipient));
        when(invites.findBySessionIdAndInviteeIdAndStatus(1L, 20L, SessionInviteStatus.PENDING))
                .thenReturn(Optional.of(new SessionInvite()));
        assertFailure(HttpStatus.CONFLICT, "jogador");
        verify(invites, never()).save(any());
    }

    @Test
    void rejectsInvitingSelf() {
        prepare();
        when(profiles.findUserByUsernameIgnoreCase("organizador")).thenReturn(Optional.of(owner));
        assertFailure(HttpStatus.BAD_REQUEST, "organizador");
    }

    @Test
    void rejectsBlankRecipient() {
        assertFailure(HttpStatus.BAD_REQUEST, " ");
        verifyNoInteractions(sessions, profiles, invites);
    }
    private SessionInvite pending() {
        SessionInvite invite = new SessionInvite();
        invite.setId(55L);
        invite.setSession(session);
        invite.setInvitee(recipient);
        invite.setInviter(owner);
        invite.setStatus(SessionInviteStatus.PENDING);
        when(sessions.findByIdForUpdate(1L)).thenReturn(Optional.of(session));
        when(profiles.findUserByUsername("jogador")).thenReturn(Optional.of(recipient));
        when(invites.findById(55L)).thenReturn(Optional.of(invite));
        return invite;
    }

    @Test
    void acceptsUsingExistingJoinService() {
        SessionInvite invite = pending();
        JoinSessionResponse joined = new JoinSessionResponse(JoinState.PARTICIPANT, null);
        when(sessionService.joinWithInvitation(1L, 55L, "jogador")).thenReturn(joined);
        assertSame(joined, service.accept(1L, 55L, "jogador"));
        assertEquals(SessionInviteStatus.ACCEPTED, invite.getStatus());
        assertNotNull(invite.getRespondedAt());
        verify(invites).save(invite);
    }

    @Test
    void declinesWithoutJoining() {
        SessionInvite invite = pending();
        service.decline(1L, 55L, "jogador");
        assertEquals(SessionInviteStatus.DECLINED, invite.getStatus());
        assertNotNull(invite.getRespondedAt());
        verifyNoInteractions(sessionService);
    }

    @Test
    void rejectsAlreadyAnsweredInvite() {
        SessionInvite invite = pending();
        invite.setStatus(SessionInviteStatus.DECLINED);
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.accept(1L, 55L, "jogador"));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verifyNoInteractions(sessionService);
    }

    @Test
    void rejectsOtherPlayerResponding() {
        SessionInvite invite = pending();
        invite.setInvitee(owner);
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.decline(1L, 55L, "jogador"));
        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        verify(invites, never()).save(any());
    }

    @Test
    void failedJoinDoesNotMarkInvitationAccepted() {
        SessionInvite invite = pending();
        when(sessionService.joinWithInvitation(1L, 55L, "jogador"))
                .thenThrow(new IllegalStateException("Session already started"));
        assertThrows(IllegalStateException.class, () -> service.accept(1L, 55L, "jogador"));
        assertEquals(SessionInviteStatus.PENDING, invite.getStatus());
        verify(invites, never()).save(any());
    }

    @Test
    void pendingInboxContainsOnlyCurrentUserInvitations() {
        when(profiles.findUserByUsername("jogador")).thenReturn(Optional.of(recipient));
        SessionInvite invite = new SessionInvite();
        invite.setId(55L);
        invite.setSession(session);
        invite.setInviter(owner);
        when(invites.findByInviteeIdAndStatusOrderByCreatedAtDesc(20L, SessionInviteStatus.PENDING))
                .thenReturn(java.util.List.of(invite));

        var result = service.pendingFor("jogador");
        assertEquals(1, result.size());
        assertEquals(55L, result.get(0).id());
        assertEquals(1L, result.get(0).sessionId());
        assertEquals("organizador", result.get(0).inviterUsername());
        assertEquals(0, result.get(0).participantCount());
        assertEquals(SessionStatus.OPEN, result.get(0).status());
        assertNull(result.get(0).city());
        verify(participants).countBySessionIdAndState(1L,ParticipantState.ACTIVE);
        verify(invites).findByInviteeIdAndStatusOrderByCreatedAtDesc(20L, SessionInviteStatus.PENDING);
    }


    @Test
    void pendingInboxShowsGameMetadataWithoutPrivateAddressOrPlayerNames() {
        when(profiles.findUserByUsername("jogador")).thenReturn(Optional.of(recipient));
        SessionInvite invite=new SessionInvite();
        invite.setId(15L);
        invite.setInviter(owner);
        invite.setSession(session);
        invite.setCreatedAt(Instant.parse("2026-10-27T18:00:00Z"));
        session.setCity("Lisboa");
        session.setAddress("Rua Privada 123");
        session.setSport("Futebol");
        session.setMaxParticipants(10);
        session.setExternalConfirmedParticipants(2);
        when(participants.countBySessionIdAndState(1L,ParticipantState.ACTIVE)).thenReturn(5L);
        when(invites.findByInviteeIdAndStatusOrderByCreatedAtDesc(
                20L,SessionInviteStatus.PENDING)).thenReturn(java.util.List.of(invite));
        var pending=service.pendingFor("jogador");
        assertEquals(7,pending.getFirst().participantCount());
        assertEquals(10,pending.getFirst().maxParticipants());
        assertEquals("Futebol",pending.getFirst().sport());
        assertEquals("Lisboa",pending.getFirst().city());
        assertNotNull(pending.getFirst().gameDate());
        assertEquals("organizador",pending.getFirst().inviterUsername());
    }

}
