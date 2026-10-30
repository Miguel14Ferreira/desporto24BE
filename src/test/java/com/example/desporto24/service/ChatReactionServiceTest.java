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
    void participantsExposeOnlyProfileDisplayFieldsToAuthorizedConversationMember() {
        actor();
        Chat msg = new Chat();
        msg.setId(17L);
        msg.setUsername1("ana");
        msg.setUsername2("bea");
        when(direct.findAllById(any())).thenReturn(List.of(msg));
        ChatMessageReaction own = new ChatMessageReaction();
        own.setProfileId(8L);
        own.setMessageId(17L);
        own.setEmoji("❤️");
        ChatMessageReaction other = new ChatMessageReaction();
        other.setProfileId(9L);
        other.setMessageId(17L);
        other.setEmoji("👍");
        when(reactions.findByConversationKindAndMessageIdIn(eq("direct"), any()))
                .thenReturn(List.of(other, own));
        Profile ana = new Profile();
        ana.setId(8L);
        ana.setUsername("ana");
        ana.setNickname("Ana");
        ana.setEmail("private@example.invalid");
        Profile bea = new Profile();
        bea.setId(9L);
        bea.setUsername("bea");
        bea.setFullName("Beatriz");
        bea.setProfilePicUrl("https://example.invalid/avatar.png");
        when(profiles.findAllById(any())).thenReturn(List.of(ana, bea));

        var list = service.participants("ana", "direct", "bea", 17L);
        assertEquals(2, list.size());
        assertTrue(list.getFirst().mine());
        assertEquals("ana", list.getFirst().username());
        assertEquals("Ana", list.getFirst().displayName());
        assertEquals("❤️", list.getFirst().emoji());
        assertFalse(list.get(1).mine());
        assertEquals("Beatriz", list.get(1).displayName());
        assertEquals("https://example.invalid/avatar.png", list.get(1).profilePicUrl());
        verify(authorization).requireConversationAccess("ana", "direct", "bea");
        verify(profiles).findAllById(any());
    }

    @Test
    void participantsNeverRevealUsersFromUnrelatedDirectMessage() {
        actor();
        Chat other = new Chat();
        other.setId(99L);
        other.setUsername1("ana");
        other.setUsername2("carla");
        when(direct.findAllById(any())).thenReturn(List.of(other));
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.participants("ana", "direct", "bea", 99L))
                .getStatusCode().value());
        verifyNoInteractions(reactions);
    }

    @Test
    void participantsCheckConversationAclBeforeAnyProfileOrReactionLookup() {
        doThrow(new org.springframework.security.access.AccessDeniedException("Blocked"))
                .when(authorization).requireConversationAccess("ana", "direct", "bea");
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
                () -> service.participants("ana", "direct", "bea", 12L));
        verifyNoInteractions(profiles, direct, reactions);
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
