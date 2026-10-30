package com.example.desporto24.websocket;

import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.ChatTypingService;
import com.example.desporto24.utility.JWTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.Optional;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * WebSocket authentication must enforce the same suspension status as the HTTP
 * JWT filter and bind each socket to exactly one identity.
 */
class ChatRealtimeGatewaySecurityTest {

    @Test
    void suspendedAccountCannotAuthenticateRealtimeChat() throws Exception {
        JWTokenProvider tokens = mock(JWTokenProvider.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        ChatRealtimeGateway gateway = new ChatRealtimeGateway(
                tokens, profiles, mock(ChatTypingService.class));
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.getId()).thenReturn("socket-1");
        Profile profile = new Profile();
        profile.setUsername("alice");
        profile.setEnabled(true);
        profile.setNotLocked(true);
        profile.setModerationSuspendedAt(Instant.now());
        when(tokens.getSubject("token-alice")).thenReturn("alice");
        when(profiles.findUserByUsername("alice")).thenReturn(Optional.of(profile));

        gateway.handleTextMessage(session, new TextMessage(
                "{\"type\":\"AUTH\",\"token\":\"token-alice\"}"));

        verify(session).close(any(CloseStatus.class));
        verify(tokens, never()).isTokenValid(any(), any());
        verify(session, never()).sendMessage(any(TextMessage.class));
    }

    @Test
    void secondAuthenticationCannotRebindExistingSocketToAnotherAccount() throws Exception {
        JWTokenProvider tokens = mock(JWTokenProvider.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        ChatRealtimeGateway gateway = new ChatRealtimeGateway(
                tokens, profiles, mock(ChatTypingService.class));
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.getId()).thenReturn("socket-2");
        when(session.isOpen()).thenReturn(true);
        Profile alice = new Profile();
        alice.setUsername("alice");
        alice.setEnabled(true);
        alice.setNotLocked(true);
        alice.setTokenVersion(7L);
        when(tokens.getSubject("token-alice")).thenReturn("alice");
        when(tokens.getTokenVersion("token-alice")).thenReturn(7L);
        when(tokens.isTokenValid("alice", "token-alice")).thenReturn(true);
        when(profiles.findUserByUsername("alice")).thenReturn(Optional.of(alice));

        gateway.handleTextMessage(session, new TextMessage(
                "{\"type\":\"AUTH\",\"token\":\"token-alice\"}"));
        verify(session).sendMessage(any(TextMessage.class));

        gateway.handleTextMessage(session, new TextMessage(
                "{\"type\":\"AUTH\",\"token\":\"token-bob\"}"));
        verify(session).close(any(CloseStatus.class));
        verify(tokens, never()).getSubject("token-bob");
        verify(profiles, never()).findUserByUsername("bob");
    }

    @Test
    void logoutRevokesSocketBeforeDeliveringPrivateMessage() throws Exception {
        JWTokenProvider tokens = mock(JWTokenProvider.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        ChatRealtimeGateway gateway = new ChatRealtimeGateway(
                tokens, profiles, mock(ChatTypingService.class));
        WebSocketSession socket = mock(WebSocketSession.class);
        when(socket.getId()).thenReturn("socket-logout");
        when(socket.isOpen()).thenReturn(true);
        Profile profile = new Profile();
        profile.setUsername("alice");
        profile.setNotLocked(true);
        profile.setEnabled(true);
        profile.setTokenVersion(7L);
        when(tokens.getSubject("token-alice")).thenReturn("alice");
        when(tokens.getTokenVersion("token-alice")).thenReturn(7L);
        when(tokens.isTokenValid("alice", "token-alice")).thenReturn(true);
        when(profiles.findUserByUsername("alice")).thenReturn(Optional.of(profile));

        gateway.handleTextMessage(socket,
                new TextMessage("{\"type\":\"AUTH\",\"token\":\"token-alice\"}"));
        verify(socket, times(1)).sendMessage(any(TextMessage.class)); // AUTHENTICATED only

        // Equivalent to the persisted tokenVersion increment on logout/reset.
        profile.setTokenVersion(8L);
        gateway.publishChatReaction(Set.of("alice"),
                new ChatReactionChangedEvent("direct", "bob", 17L,
                        Map.of("👍", 1L), "bob", "👍", Set.of("alice")));

        verify(socket, times(1)).sendMessage(any(TextMessage.class));
        var closed = org.mockito.ArgumentCaptor.forClass(CloseStatus.class);
        verify(socket).close(closed.capture());
        // 1003 stops Angular from retrying forever with an invalid token.
        assertEquals(CloseStatus.NOT_ACCEPTABLE.getCode(), closed.getValue().getCode());
    }

    @Test
    void suspendedConnectedUserCannotSendTypingEvents() throws Exception {
        JWTokenProvider tokens = mock(JWTokenProvider.class);
        ProfileRepository profiles = mock(ProfileRepository.class);
        ChatTypingService typing = mock(ChatTypingService.class);
        ChatRealtimeGateway gateway = new ChatRealtimeGateway(tokens, profiles, typing);
        WebSocketSession socket = mock(WebSocketSession.class);
        when(socket.getId()).thenReturn("socket-suspend");
        when(socket.isOpen()).thenReturn(true);
        Profile profile = new Profile();
        profile.setUsername("alice");
        profile.setEnabled(true);
        profile.setNotLocked(true);
        profile.setTokenVersion(1L);
        when(tokens.getSubject("token-alice")).thenReturn("alice");
        when(tokens.getTokenVersion("token-alice")).thenReturn(1L);
        when(tokens.isTokenValid("alice", "token-alice")).thenReturn(true);
        when(profiles.findUserByUsername("alice")).thenReturn(Optional.of(profile));

        gateway.handleTextMessage(socket,
                new TextMessage("{\"type\":\"AUTH\",\"token\":\"token-alice\"}"));
        profile.setModerationSuspendedAt(Instant.now());

        gateway.handleTextMessage(socket, new TextMessage(
                "{\"type\":\"CHAT_TYPING\",\"kind\":\"direct\",\"id\":\"bob\",\"active\":true}"));

        verifyNoInteractions(typing);
        verify(socket).close(any(CloseStatus.class));
        verify(socket, times(1)).sendMessage(any(TextMessage.class));
    }
}
