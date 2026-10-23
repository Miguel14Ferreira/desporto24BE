package com.example.desporto24.service;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.Chat;
import com.example.desporto24.model.ChatReadCursor;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityTimelineEntry;
import com.example.desporto24.model.dto.chat.DirectReadReceiptResponse;
import com.example.desporto24.model.dto.chat.InboxReadRequest;
import com.example.desporto24.model.dto.chat.InboxRequest;
import com.example.desporto24.model.dto.chat.InboxSummaryResponse;
import com.example.desporto24.repository.ChatReadCursorRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTimelineEntryRepository;
import com.example.desporto24.repository.session.SessionMessageRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.service.interfaces.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InboxSummaryServiceTest {
    @Mock ProfileRepository profiles;
    @Mock ChatService chatService;
    @Mock ChatRepository chats;
    @Mock CommunityRepository communities;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTimelineEntryRepository timelines;
    @Mock SessionRepository sessions;
    @Mock SessionParticipantRepository participants;
    @Mock SessionMessageRepository sessionMessages;
    @Mock ChatReadCursorRepository cursors;
    @InjectMocks InboxSummaryService service;

    private Profile profile(long id, String username) {
        Profile p = new Profile();
        p.setId(id);
        p.setUsername(username);
        return p;
    }

    private void actor() {
        when(profiles.findUserByUsernameIgnoreCase("ana"))
                .thenReturn(Optional.of(profile(1, "ana")));
    }

    private InboxRequest request(InboxRequest.Conversation... items) {
        return new InboxRequest(List.of(items));
    }

    private InboxRequest.Conversation reference(String kind, String id) {
        return new InboxRequest.Conversation(kind, id);
    }

    @Test
    void directSummaryProvidesPreviewAndPersistentUnreadCountWithoutLeakingOtherMessages() {
        actor();
        when(profiles.findUserByUsernameIgnoreCase("bea"))
                .thenReturn(Optional.of(profile(2, "Bea")));
        Chat last = new Chat("Bea", "ana", "Olá", "", Date.from(Instant.parse("2026-10-23T08:00:00Z")));
        last.setId(45L);
        when(chats.findConversationPage(eq("ana"), eq("Bea"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(last)));
        ChatReadCursor cursor = new ChatReadCursor();
        cursor.setLastReadMessageId(40L);
        when(cursors.findByProfileIdAndConversationKindAndConversationKey(1L, "direct", "bea"))
                .thenReturn(Optional.of(cursor));
        when(chats.countUnread("ana", "Bea", 40L)).thenReturn(3L);

        List<InboxSummaryResponse> values = service.summaries("ana",
                request(reference("direct", "Bea"), reference("direct", "bea")));
        assertEquals(1, values.size(), "Canonical refs are deduplicated");
        assertEquals(45L, values.get(0).latestMessageId());
        assertEquals("Olá", values.get(0).latestText());
        assertEquals(3, values.get(0).unreadCount());
        verify(chatService).getChatInfo("ana", "Bea");
    }

    @Test
    void removedCommunityMemberCannotSeePreviewNorAdvanceCursor() {
        actor();
        when(communities.findById(7L))
                .thenReturn(Optional.of(Community.builder()
                        .id(7L).owner(profile(90, "owner")).build()));
        var result = service.summaries("ana", request(reference("community", "7")));
        assertTrue(result.isEmpty());
        assertThrows(AccessDeniedException.class,
                () -> service.markRead("ana", new InboxReadRequest("community", "7", 999L)));
        verifyNoInteractions(timelines);
        verify(cursors, never()).advance(anyLong(), anyString(), anyString(), anyLong());
    }

    @Test
    void outsiderCannotSeeSessionMessagesOrMoveTheReadCursor() {
        actor();
        Session session = new Session();
        session.setId(8L);
        session.setGameDate(java.util.Date.from(Instant.now().plusSeconds(3600)));
        session.setProfile(profile(90, "organizer"));
        when(sessions.findById(8L)).thenReturn(Optional.of(session));
        assertTrue(service.summaries("ana", request(reference("session", "8"))).isEmpty());
        assertThrows(AccessDeniedException.class,
                () -> service.markRead("ana", new InboxReadRequest("session", "8", 1L)));
        verify(participants, atLeastOnce())
                .existsBySessionIdAndProfileIdAndState(8L, 1L, ParticipantState.ACTIVE);
        verifyNoInteractions(sessionMessages);
    }

    @Test
    void readCursorNeverAdvancesBeyondTheLatestAuthorizedMessage() {
        actor();
        when(communities.findById(7L)).thenReturn(Optional.of(Community.builder()
                .id(7L).owner(profile(1, "ana")).build()));
        CommunityTimelineEntry last = CommunityTimelineEntry.builder()
                .id(12L).body("Olá").createdAt(Instant.now())
                .community(Community.builder().id(7L).build())
                .author(profile(90, "someone")).kind(CommunityTimelineEntry.Kind.CHAT).build();
        when(timelines.findByCommunityIdAndKindOrderByIdDesc(eq(7L),
                eq(CommunityTimelineEntry.Kind.CHAT), any(Pageable.class)))
                .thenReturn(List.of(last));
        when(timelines.countUnread(eq(7L), eq(CommunityTimelineEntry.Kind.CHAT), eq(1L), anyLong()))
                .thenReturn(2L);
        var result = service.markRead("ana", new InboxReadRequest("community", "7", Long.MAX_VALUE));
        verify(cursors).advance(1L, "community", "7", 12L);
        assertEquals(12L, result.latestMessageId());
    }

    @Test
    void authorizedDirectReceiptReadsOnlyThePeerCursorForThisConversation() {
        actor();
        when(profiles.findUserByUsernameIgnoreCase("bea"))
                .thenReturn(Optional.of(profile(2L, "Bea")));
        ChatReadCursor cursor = new ChatReadCursor();
        cursor.setLastReadMessageId(55L);
        when(cursors.findByProfileIdAndConversationKindAndConversationKey(
                2L, "direct", "ana")).thenReturn(Optional.of(cursor));

        DirectReadReceiptResponse receipt = service.peerDirectReadReceipt("ana", "BEA");
        assertEquals("bea", receipt.peerUsername());
        assertEquals(55L, receipt.lastReadMessageId());
        verify(chatService).getChatInfo("ana", "Bea");
        verify(cursors).findByProfileIdAndConversationKindAndConversationKey(2L, "direct", "ana");
        verifyNoInteractions(chats);
    }

    @Test
    void absentReceiptDoesNotPretendMessageWasRead() {
        actor();
        when(profiles.findUserByUsernameIgnoreCase("bea"))
                .thenReturn(Optional.of(profile(2L, "bea")));
        assertEquals(0L, service.peerDirectReadReceipt("ana", "bea").lastReadMessageId());
    }

    @Test
    void deniedOrBlockedDirectChatNeverLeaksPeerCursor() {
        actor();
        when(profiles.findUserByUsernameIgnoreCase("bea"))
                .thenReturn(Optional.of(profile(2L, "bea")));
        doThrow(new AccessDeniedException("Chat blocked"))
                .when(chatService).getChatInfo("ana", "bea");
        assertThrows(AccessDeniedException.class,
                () -> service.peerDirectReadReceipt("ana", "bea"));
        verifyNoInteractions(cursors);
    }

    @Test
    void malformedPeerAndSelfReceiptAreDeniedBeforeLookingUpCursor() {
        actor();
        assertThrows(ResponseStatusException.class,
                () -> service.peerDirectReadReceipt("ana", "../../admin"));
        when(profiles.findUserByUsernameIgnoreCase("ana"))
                .thenReturn(Optional.of(profile(1L, "ana")));
        assertThrows(AccessDeniedException.class,
                () -> service.peerDirectReadReceipt("ana", "ana"));
        verifyNoInteractions(cursors);
    }

    @Test
    void rejectsUnknownKindsAndMalformedIdsWithoutQueryingMessages() {
        actor();
        assertThrows(ResponseStatusException.class,
                () -> service.summaries("ana", request(reference("unknown", "7"))));
        assertThrows(ResponseStatusException.class,
                () -> service.summaries("ana", request(reference("session", "1 OR 1=1"))));
        verifyNoInteractions(chats, timelines, sessionMessages);
    }
}
