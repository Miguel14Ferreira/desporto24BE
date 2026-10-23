package com.example.desporto24.service;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityTimelineEntry;
import com.example.desporto24.model.session.SessionMessage;
import com.example.desporto24.repository.community.CommunityTimelineEntryRepository;
import com.example.desporto24.repository.session.SessionMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageSearchServiceTest {
    @Mock InboxSummaryService inbox;
    @Mock ChatRepository direct;
    @Mock SessionMessageRepository sessions;
    @Mock CommunityTimelineEntryRepository communities;
    @InjectMocks ChatMessageSearchService service;

    private Profile profile(String username) {
        Profile p = new Profile();
        p.setUsername(username);
        return p;
    }

    @Test
    void privateSearchChecksAccessAndReturnsOldMessagesWithQuote() {
        Chat chat = new Chat("bea", "ana", "Bola de treino", null,
                Date.from(Instant.parse("2026-09-01T10:00:00Z")));
        chat.setId(9L);
        chat.setReplyToMessageId(3L);
        chat.setReplyToSender("ana");
        chat.setReplyToText("Quem leva a bola?");
        when(direct.searchConversation(eq("ana"), eq("Bea"), eq("bola"),
                any(PageRequest.class))).thenReturn(
                new PageImpl<>(List.of(chat), PageRequest.of(0, 20), 1));
        var out = service.search("ana", "direct", "Bea", " Bola ", 0);
        verify(inbox).requireConversationAccess("ana", "direct", "Bea");
        assertEquals(1, out.results().size());
        assertEquals(9L, out.results().getFirst().id());
        assertEquals("bea", out.results().getFirst().sender());
        assertEquals(3L, out.results().getFirst().replyToMessageId());
        assertFalse(out.hasMore());
    }

    @Test
    void sessionsAndCommunitiesAreSearchedInTheirOwnConversationOnly() {
        Session session = new Session();
        session.setId(51L);
        var s = SessionMessage.builder().id(4L).session(session)
                .senderProfile(profile("ana")).body("Torneio daqui a uma hora")
                .createdAt(Instant.parse("2026-10-01T10:00:00Z")).build();
        when(sessions.searchConversation(eq(51L), eq("torneio"), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(s)));
        assertEquals("ana", service.search("ana", "session", "51", "torneio", 0)
                .results().getFirst().sender());

        var c = CommunityTimelineEntry.builder().id(10L).kind(CommunityTimelineEntry.Kind.CHAT)
                .community(Community.builder().id(7L).build()).author(profile("bea"))
                .body("Treino de basquetebol").createdAt(Instant.now()).build();
        when(communities.searchConversation(eq(7L),
                eq(CommunityTimelineEntry.Kind.CHAT), eq("treino"),
                any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(c)));
        assertEquals("bea", service.search("ana", "community", "7", "treino", 0)
                .results().getFirst().sender());
    }

    @Test
    void escapesSqlWildcardsAndPaginatesWithoutExposingOtherMessages() {
        when(direct.searchConversation(eq("ana"), eq("bea"), eq("100!%!_!_!!"),
                eq(PageRequest.of(1, 20))))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(1, 20), 47));
        var response = service.search("ana", "direct", "bea", "100%__!", 1);
        assertTrue(response.hasMore());
        assertEquals(1, response.page());
        assertTrue(response.results().isEmpty());
    }

    @Test
    void deniedConversationNeverQueriesMessages() {
        doThrow(new AccessDeniedException("No accessible chat"))
                .when(inbox).requireConversationAccess("ana", "direct", "outsider");
        assertThrows(AccessDeniedException.class,
                () -> service.search("ana", "direct", "outsider", "palavra", 0));
        verifyNoInteractions(direct, sessions, communities);
    }


    @Test
    void fetchesBoundedContextInChronologicalOrderForAllKinds() {
        Chat older = new Chat("ana", "bea", "older", null, new Date());
        older.setId(29L);
        Chat target = new Chat("bea", "ana", "target", null, new Date());
        target.setId(30L);
        when(direct.findConversationContextBefore("ana", "bea", 30L, PageRequest.of(0, 21)))
                .thenReturn(List.of(target, older));
        when(direct.findConversationContextAfter("ana", "bea", 30L, PageRequest.of(0, 20)))
                .thenReturn(List.of());
        assertEquals(List.of(29L, 30L), service.context("ana", "direct", "bea", 30)
                .stream().map(hit -> hit.id()).toList());
        verify(inbox).requireConversationAccess("ana", "direct", "bea");

        var session = SessionMessage.builder().id(4L).senderProfile(profile("ana"))
                .body("old session").createdAt(Instant.now()).build();
        when(sessions.findBySessionIdAndIdLessThanEqualOrderByIdDesc(
                51L, 4L, PageRequest.of(0, 21))).thenReturn(List.of(session));
        when(sessions.findBySessionIdAndIdGreaterThanOrderByIdAsc(
                51L, 4L, PageRequest.of(0, 20))).thenReturn(List.of());
        assertEquals(4L, service.context("ana", "session", "51", 4).getFirst().id());

        var community = CommunityTimelineEntry.builder().id(9L).author(profile("bea"))
                .body("old community").createdAt(Instant.now()).build();
        when(communities.findByCommunityIdAndKindAndIdLessThanEqualOrderByIdDesc(
                7L, CommunityTimelineEntry.Kind.CHAT, 9L, PageRequest.of(0, 21)))
                .thenReturn(List.of(community));
        when(communities.findByCommunityIdAndKindAndIdGreaterThanOrderByIdAsc(
                7L, CommunityTimelineEntry.Kind.CHAT, 9L, PageRequest.of(0, 20)))
                .thenReturn(List.of());
        assertEquals(9L, service.context("ana", "community", "7", 9).getFirst().id());
    }

    @Test
    void contextChecksAuthorizationAndRejectsAnUnrelatedTarget() {
        doThrow(new AccessDeniedException("No accessible chat"))
                .when(inbox).requireConversationAccess("ana", "direct", "outsider");
        assertThrows(AccessDeniedException.class,
                () -> service.context("ana", "direct", "outsider", 19));
        verifyNoInteractions(direct, sessions, communities);
        when(direct.findConversationContextBefore(eq("ana"), eq("bea"), eq(19L), any(PageRequest.class)))
                .thenReturn(List.of());
        when(direct.findConversationContextAfter(eq("ana"), eq("bea"), eq(19L), any(PageRequest.class)))
                .thenReturn(List.of());
        var error = assertThrows(ResponseStatusException.class,
                () -> service.context("ana", "direct", "bea", 19));
        assertEquals(404, error.getStatusCode().value());
        assertThrows(ResponseStatusException.class,
                () -> service.context("ana", "direct", "bea", 0));
    }

    @Test
    void invalidQueryAndPageAreRejectedBeforeDataAccess() {
        for (String query : List.of("", "a", " ", "x".repeat(81))) {
            assertThrows(ResponseStatusException.class,
                    () -> service.search("ana", "direct", "bea", query, 0));
        }
        assertThrows(ResponseStatusException.class,
                () -> service.search("ana", "direct", "bea", "texto", -1));
        assertThrows(ResponseStatusException.class,
                () -> service.search("ana", "direct", "bea", "texto", 51));
        assertThrows(ResponseStatusException.class,
                () -> service.search("ana", "other", "7", "texto", 0));
        verifyNoInteractions(inbox, direct, sessions, communities);
    }
}
