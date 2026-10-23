package com.example.desporto24.websocket;

import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.session.SessionMessageResponse;
import com.example.desporto24.model.dto.community.CommunityTimelineEntryResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.utility.JWTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ChatRealtimeGateway extends TextWebSocketHandler {

    private final JWTokenProvider jwtTokenProvider;
    private final ProfileRepository profileRepository;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public ChatRealtimeGateway(
            JWTokenProvider jwtTokenProvider,
            ProfileRepository profileRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.profileRepository = profileRepository;
    }

    private final ConcurrentHashMap<String, Set<WebSocketSession>> sessionsByUsername =
            new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> usernameBySessionId =
            new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode payload = objectMapper.readTree(message.getPayload());
        String type = payload.path("type").asText("");

        if (!"AUTH".equals(type)) {
            if (!usernameBySessionId.containsKey(session.getId())) {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Authentication required"));
            }
            return;
        }

        String token = payload.path("token").asText("");
        authenticate(session, token);
    }

    private void authenticate(WebSocketSession session, String token) throws IOException {
        try {
            String username = jwtTokenProvider.getSubject(token);
            Profile profile = profileRepository.findUserByUsername(username).orElse(null);

            boolean profileActive = profile != null
                    && !Boolean.FALSE.equals(profile.getNotLocked())
                    && !Boolean.FALSE.equals(profile.getEnabled());

            boolean currentTokenVersion = profile != null
                    && profile.getTokenVersion() == jwtTokenProvider.getTokenVersion(token);

            if (!profileActive
                    || !currentTokenVersion
                    || !jwtTokenProvider.isTokenValid(username, token)) {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
                return;
            }

            usernameBySessionId.put(session.getId(), username);
            sessionsByUsername
                    .computeIfAbsent(username, ignored -> new CopyOnWriteArraySet<>())
                    .add(session);

            send(session, objectMapper.createObjectNode()
                    .put("type", "AUTHENTICATED")
                    .put("username", username)
                    .toString());
        } catch (RuntimeException exception) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
        }
    }

    public void publishChatMessage(String username, Chat chat) {
        Set<WebSocketSession> sessions = sessionsByUsername.get(username);
        if (sessions == null || sessions.isEmpty()) {
            return;
        }

        try {
            String payload = objectMapper.createObjectNode()
                    .put("type", "CHAT_MESSAGE")
                    .set("message", objectMapper.valueToTree(chat))
                    .toString();

            for (WebSocketSession session : sessions) {
                if (!session.isOpen()) {
                    continue;
                }
                try {
                    send(session, payload);
                } catch (IOException ignored) {
                    // The persisted chat message remains the source of truth.
                }
            }
        } catch (RuntimeException ignored) {
            // Realtime delivery must never roll back a persisted chat message.
        }
    }

    public void publishSessionChatMessage(
            Set<String> usernames,
            SessionMessageResponse message) {
        if (usernames == null || usernames.isEmpty() || message == null) {
            return;
        }

        try {
            String payload = objectMapper.createObjectNode()
                    .put("type", "SESSION_CHAT_MESSAGE")
                    .set("message", objectMapper.valueToTree(message))
                    .toString();

            for (String username : usernames) {
                Set<WebSocketSession> sessions = sessionsByUsername.get(username);
                if (sessions == null || sessions.isEmpty()) {
                    continue;
                }

                for (WebSocketSession session : sessions) {
                    if (!session.isOpen()) {
                        continue;
                    }
                    try {
                        send(session, payload);
                    } catch (IOException ignored) {
                        // Persisted session chat remains the source of truth.
                    }
                }
            }
        } catch (RuntimeException ignored) {
            // Realtime delivery must never roll back a persisted message.
        }
    }

    /**
     * Send to individual authenticated member sockets, never to a public
     * community topic. Newly removed members are not included in recipients.
     */
    public void publishCommunityChatMessage(
            Set<String> usernames, CommunityTimelineEntryResponse message) {
        if (usernames == null || usernames.isEmpty() || message == null) return;
        try {
            String payload = objectMapper.createObjectNode()
                    .put("type", "COMMUNITY_CHAT_MESSAGE")
                    .set("message", objectMapper.valueToTree(message))
                    .toString();
            for (String username : usernames) {
                Set<WebSocketSession> sessions = sessionsByUsername.get(username);
                if (sessions == null) continue;
                for (WebSocketSession session : sessions) {
                    if (!session.isOpen()) continue;
                    try {
                        send(session, payload);
                    } catch (IOException ignored) {
                        // History remains authoritative when a socket fails.
                    }
                }
            }
        } catch (RuntimeException ignored) {
            // Never roll back a committed message because of realtime failure.
        }
    }

    public void publishChatReaction(Set<String> usernames, ChatReactionChangedEvent reaction) {
        if (usernames == null || usernames.isEmpty() || reaction == null) return;
        try {
            String payload = objectMapper.createObjectNode()
                    .put("type", "CHAT_REACTION_CHANGED")
                    .set("reaction", objectMapper.valueToTree(reaction))
                    .toString();
            for (String username : usernames) {
                Set<WebSocketSession> sockets = sessionsByUsername.get(username);
                if (sockets == null) continue;
                for (WebSocketSession socket : sockets) {
                    if (!socket.isOpen()) continue;
                    try { send(socket, payload); } catch (IOException ignored) { }
                }
            }
        } catch (RuntimeException ignored) { }
    }

    private void send(WebSocketSession session, String payload) throws IOException {
        synchronized (session) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(payload));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String username = usernameBySessionId.remove(session.getId());
        if (username == null) {
            return;
        }

        Set<WebSocketSession> sessions = sessionsByUsername.get(username);
        if (sessions == null) {
            return;
        }

        sessions.remove(session);
        if (sessions.isEmpty()) {
            sessionsByUsername.remove(username, sessions);
        }
    }
}
