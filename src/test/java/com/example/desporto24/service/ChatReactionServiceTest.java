package com.example.desporto24.service;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.model.*;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.session.SessionMessage;
import com.example.desporto24.repository.*;
import com.example.desporto24.repository.community.CommunityTimelineEntryRepository;
import com.example.desporto24.repository.session.*;
import com.example.desporto24.websocket.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ChatReactionServiceTest {
    @Mock InboxSummaryService authorization;
    @Mock ProfileRepository profiles;
    @Mock ChatRepository direct;
    @Mock SessionMessageRepository sessionMessages;
    @Mock CommunityTimelineEntryRepository communityMessages;
    @Mock ChatMessageReactionRepository reactions;
    @Mock SessionRepository sessions;
    @Mock SessionParticipantRepository participants;
    @Mock CommunityChatRecipientResolver communityRecipients;
    @Mock ApplicationEventPublisher events;
    @InjectMocks ChatReactionService service;

    private Profile actor() {
        Profile actor = new Profile();
        actor.setId(8L);
        actor.setUsername("ana");
        when(profiles.findUserByUsernameIgnoreCase("ana")).thenReturn(Optional.of(actor));
        return actor;
    }

    @Test
    void oneDirectReactionPersistsAndBroadcastsOnlyToTheTwoChatUsers() {
        actor();
        Chat msg = new Chat();
        msg.setId(17L);
        msg.setUsername1("ana");
        msg.setUsername2("bea");
        when(direct.findAllById(any())).thenReturn(List.of(msg));
        ChatMessageReaction persisted = new ChatMessageReaction();
        persisted.setProfileId(8L);
        persisted.setMessageId(17L);
        persisted.setEmoji("👍");
        when(reactions.findByConversationKindAndMessageIdIn(eq("direct"), any()))
                .thenReturn(List.of(persisted));
        var result = service.update("ana", "direct", "bea", 17L, "👍");
        assertEquals("👍", result.myEmoji());
        assertEquals(1L, result.counts().get("👍"));
        verify(reactions).upsert(8L, "direct", 17L, "👍");
        ArgumentCaptor<ChatReactionChangedEvent> event =
                ArgumentCaptor.forClass(ChatReactionChangedEvent.class);
        verify(events).publishEvent(event.capture());
        assertEquals(Set.of("ana", "bea"), event.getValue().recipients());
        assertEquals(Map.of("👍", 1L), event.getValue().counts());
    }

    @Test
    void inaccessibleDirectMessageIdsNeverReturnSomeoneElsesReactions() {
        actor();
        Chat other = new Chat();
        other.setId(18L);
        other.setUsername1("ana");
        other.setUsername2("carla");
        when(direct.findAllById(any())).thenReturn(List.of(other));
        assertTrue(service.summaries("ana", "direct", "bea", List.of(18L)).isEmpty());
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.update("ana", "direct", "bea", 18L, "❤️")).getStatusCode().value());
        verifyNoInteractions(reactions, events);
    }

    @Test
    void communityPostsAndOtherCommunityMessagesCannotReceiveChatReactions() {
        actor();
        Community group = Community.builder().id(9L).build();
        CommunityTimelineEntry post = CommunityTimelineEntry.builder()
                .id(21L).kind(CommunityTimelineEntry.Kind.POST).community(group).build();
        when(communityMessages.findAllById(any())).thenReturn(List.of(post));
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.update("ana", "community", "9", 21L, "😂")).getStatusCode().value());
        post.setKind(CommunityTimelineEntry.Kind.CHAT);
        post.setCommunity(Community.builder().id(10L).build());
        assertTrue(service.summaries("ana", "community", "9", List.of(21L)).isEmpty());
        verifyNoInteractions(reactions, events);
    }

    @Test
    void onlyExactSessionMessageIdsAreAggregatedInOneBatch() {
        actor();
        Session inSession = new Session();
        inSession.setId(10L);
        Session elsewhere = new Session();
        elsewhere.setId(11L);
        SessionMessage a = SessionMessage.builder().id(100L).session(inSession).build();
        SessionMessage b = SessionMessage.builder().id(101L).session(elsewhere).build();
        when(sessionMessages.findAllById(any())).thenReturn(List.of(a, b));
        ChatMessageReaction count = new ChatMessageReaction();
        count.setMessageId(100L);
        count.setProfileId(33L);
        count.setEmoji("🎉");
        when(reactions.findByConversationKindAndMessageIdIn(eq("session"), any()))
                .thenReturn(List.of(count));
        var result = service.summaries("ana", "session", "10", List.of(100L, 101L));
        assertEquals(1, result.size());
        assertEquals(100L, result.getFirst().messageId());
        assertEquals(1L, result.getFirst().counts().get("🎉"));
        assertNull(result.getFirst().myEmoji());
        verify(reactions, times(1)).findByConversationKindAndMessageIdIn(eq("session"), any());
    }

    @Test
    void invalidEmojiAndOversizedBatchStopBeforeDatabaseModification() {
        actor();
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.update("ana", "direct", "bea", 12L, "⚠️")).getStatusCode().value());
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.summaries("ana", "direct", "bea",
                        Collections.nCopies(101, 2L))).getStatusCode().value());
        verifyNoInteractions(reactions, events);
    }

    @Test
    void accessControlRunsBeforeAnySourceMessageLookup() {
        doThrow(new org.springframework.security.access.AccessDeniedException("Denied"))
                .when(authorization).requireConversationAccess("ana", "community", "5");
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> service.summaries("ana", "community", "5", List.of(9L)));
        verifyNoInteractions(communityMessages, reactions, events);
    }
}
