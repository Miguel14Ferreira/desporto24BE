package com.example.desporto24.websocket;

import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.ChatTypingService;
import com.example.desporto24.utility.JWTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatReactionRealtimePayloadTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @SuppressWarnings("unchecked")
    void doesNotSerializePrivateCommunityRecipientsIntoBrowserWebSocketPayload() throws Exception {
        ChatRealtimeGateway gateway = new ChatRealtimeGateway(
                mock(JWTokenProvider.class), mock(ProfileRepository.class),
                mock(ChatTypingService.class));
        WebSocketSession socket = mock(WebSocketSession.class);
        when(socket.isOpen()).thenReturn(true);
        ConcurrentHashMap<String, Set<WebSocketSession>> sessions =
                (ConcurrentHashMap<String, Set<WebSocketSession>>) ReflectionTestUtils.getField(
                        gateway, "sessionsByUsername");
        assertNotNull(sessions);
        sessions.put("ana", Set.of(socket));
        var event = new ChatReactionChangedEvent("community", "7", 42L,
                Map.of("👍", 2L, "😂", 1L), "ana", "👍",
                Set.of("ana", "other-private-member", "another-private-member"));
        gateway.publishChatReaction(Set.of("ana"), event);

        var captor = org.mockito.ArgumentCaptor.forClass(TextMessage.class);
        verify(socket).sendMessage(captor.capture());
        JsonNode envelope = mapper.readTree(captor.getValue().getPayload());
        assertEquals("CHAT_REACTION_CHANGED", envelope.path("type").asText());
        JsonNode reaction = envelope.path("reaction");
        assertEquals("community", reaction.path("kind").asText());
        assertEquals("7", reaction.path("conversationId").asText());
        assertEquals(42L, reaction.path("messageId").asLong());
        assertEquals(2, reaction.path("counts").path("👍").asInt());
        assertFalse(reaction.has("recipients"));
        assertFalse(captor.getValue().getPayload().contains("other-private-member"));
        assertFalse(captor.getValue().getPayload().contains("another-private-member"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void deletionBroadcastsNullEmojiButNotInternalRecipients() throws Exception {
        ChatRealtimeGateway gateway = new ChatRealtimeGateway(
                mock(JWTokenProvider.class), mock(ProfileRepository.class),
                mock(ChatTypingService.class));
        WebSocketSession socket = mock(WebSocketSession.class);
        when(socket.isOpen()).thenReturn(true);
        ConcurrentHashMap<String, Set<WebSocketSession>> sessions =
                (ConcurrentHashMap<String, Set<WebSocketSession>>) ReflectionTestUtils.getField(
                        gateway, "sessionsByUsername");
        assertNotNull(sessions);
        sessions.put("ana", Set.of(socket));
        gateway.publishChatReaction(Set.of("ana"), new ChatReactionChangedEvent(
                "direct", "bea", 1L, Map.of(), "ana", null, Set.of("ana", "bea")));

        var captor = org.mockito.ArgumentCaptor.forClass(TextMessage.class);
        verify(socket).sendMessage(captor.capture());
        JsonNode reaction = mapper.readTree(captor.getValue().getPayload()).path("reaction");
        assertTrue(reaction.has("emoji"));
        assertTrue(reaction.get("emoji").isNull());
        assertFalse(reaction.has("recipients"));
    }
}
