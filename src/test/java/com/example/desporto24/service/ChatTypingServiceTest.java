package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.websocket.CommunityChatRecipientResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatTypingServiceTest {
    @Mock InboxSummaryService inbox;
    @Mock ProfileRepository profiles;
    @Mock SessionRepository sessions;
    @Mock SessionParticipantRepository participants;
    @Mock CommunityChatRecipientResolver communities;
    @InjectMocks ChatTypingService service;

    @Test void privateTypingRequiresAuthorizedFriendship() {
        Profile bea = new Profile();
        bea.setUsername("Bea");
        when(profiles.findUserByUsernameIgnoreCase("bea")).thenReturn(Optional.of(bea));
        var result = service.authorizedRecipients("ana", "direct", "bea");
        verify(inbox).requireConversationAccess("ana", "direct", "bea");
        assertEquals("bea", result.conversationId());
        assertEquals(Set.of("Bea"), result.usernames());
    }

    @Test void denialDoesNotLookUpRecipients() {
        doThrow(new AccessDeniedException("Forbidden"))
                .when(inbox).requireConversationAccess("ana", "community", "7");
        assertThrows(AccessDeniedException.class,
                () -> service.authorizedRecipients("ana", "community", "7"));
        verifyNoInteractions(communities, sessions, participants, profiles);
    }

    @Test void communityTypingUsesCurrentMembershipNotCachedUsers() {
        when(communities.currentRecipients(7L)).thenReturn(Set.of("ana", "bea"));
        var result = service.authorizedRecipients("ana", "community", "7");
        assertEquals("7", result.conversationId());
        assertEquals(Set.of("ana", "bea"), result.usernames());
    }

    @Test void completedSessionDoesNotPublishTyping() {
        Session session = new Session();
        session.setStatus(SessionStatus.COMPLETED);
        when(sessions.findById(41L)).thenReturn(Optional.of(session));
        assertThrows(ResponseStatusException.class,
                () -> service.authorizedRecipients("ana", "session", "41"));
        verifyNoInteractions(participants);
    }

    @Test void openSessionTypingIncludesOrganizerWithoutAnonymousRecipients() {
        Profile owner = new Profile();
        owner.setUsername("ana");
        Session session = new Session();
        session.setStatus(SessionStatus.OPEN);
        session.setProfile(owner);
        when(sessions.findById(41L)).thenReturn(Optional.of(session));
        var result = service.authorizedRecipients("ana", "session", "41");
        assertEquals(Set.of("ana"), result.usernames());
        verify(participants).findBySessionIdAndStateOrderByJoinedAtAsc(41L, ParticipantState.ACTIVE);
    }

    @Test void invalidConversationsAreRejectedWithoutDatabaseAccess() {
        assertThrows(ResponseStatusException.class,
                () -> service.authorizedRecipients("ana", "direct", ""));
        verifyNoInteractions(inbox, profiles, sessions, participants, communities);
    }
}
