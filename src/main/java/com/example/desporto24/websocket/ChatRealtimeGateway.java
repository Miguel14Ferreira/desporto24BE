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
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ChatRealtimeGateway extends TextWebSocketHandler {

    private final JWTokenProvider jwtTokenProvider;
    private final ProfileRepository profileRepository;
    private final com.example.desporto24.service.ChatTypingService typingService;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public ChatRealtimeGateway(
            JWTokenProvider jwtTokenProvider,
            ProfileRepository profileRepository,
            com.example.desporto24.service.ChatTypingService typingService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.profileRepository = profileRepository;
        this.typingService = typingService;
    }

    private final ConcurrentHashMap<String, Set<WebSocketSession>> sessionsByUsername =
            new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> usernameBySessionId =
            new ConcurrentHashMap<>();

    // In-memory, socket-scoped abuse protection; never persisted.
    private final ConcurrentHashMap<String, Long> lastTypingAtBySocket = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode payload = objectMapper.readTree(message.getPayload());
        String type = payload.path("type").asText("");

        if (!"AUTH".equals(type)) {
            String actor = usernameBySessionId.get(session.getId());
            if (actor == null) {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Authentication required"));
                return;
            }
            if ("CHAT_TYPING".equals(type)) handleTyping(session, actor, payload);
            return;
        }

        // One socket is bound to one account. Re-authentication on the same
        // connection must never leave this socket subscribed to two users.
        if (usernameBySessionId.containsKey(session.getId())) {
            session.close(CloseStatus.POLICY_VIOLATION.withReason("Reconnect to change account"));
            return;
        }
        String token = payload.path("token").asText("");
        authenticate(session, token);
    }


    /** Broadcast only authenticated, authorized, rate-limited ephemeral presence. */
    private void handleTyping(WebSocketSession origin, String actor, JsonNode payload) {
        if (payload.toString().length() > 256
                || !payload.path("active").isBoolean()) return;
        String kind = payload.path("kind").asText("");
        String id = payload.path("id").asText("");
        if (kind.length() > 16 || id.isBlank() || id.length() > 128) return;
        boolean active = payload.path("active").asBoolean();
        long now = System.currentTimeMillis();
        // At most one typing event per socket per second, including STOP.
        Long prev = lastTypingAtBySocket.put(origin.getId(), now);
        if (prev != null && now - prev < 1000) return;
        try {
            var audience = typingService.authorizedRecipients(actor, kind, id);
            String wire = objectMapper.createObjectNode()
                    .put("type", "CHAT_TYPING")
                    .set("typing", objectMapper.createObjectNode()
                            .put("kind", kind.toLowerCase(Locale.ROOT))
                            .put("conversationId", "direct".equalsIgnoreCase(kind)
                                    ? actor.toLowerCase(Locale.ROOT) : audience.conversationId())
                            .put("username", actor)
                            .put("active", active))
                    .toString();
            for (String username : audience.usernames()) {
                if (username.equalsIgnoreCase(actor)) continue;
                Set<WebSocketSession> sockets = sessionsByUsername.get(username);
                if (sockets == null) continue;
                for (WebSocketSession recipient : sockets) {
                    try { send(recipient, wire); } catch (IOException ignored) { }
                }
            }
        } catch (RuntimeException ignored) {
            // Fail closed: never disclose a conversation's existence or its users.
        }
    }

    private void authenticate(WebSocketSession session, String token) throws IOException {
        try {
            String username = jwtTokenProvider.getSubject(token);
            Profile profile = profileRepository.findUserByUsername(username).orElse(null);

            boolean profileActive = profile != null
                    && profile.getModerationSuspendedAt() == null
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

    /**
     * A server-confirmed direct read cursor, sent only to the authenticated
     * original sender. The caller must revalidate friendship/blocks. This
     * socket event is emitted after the cursor transaction commits.
     */
    public void publishDirectReadReceipt(
            String senderUsername, String readerUsername, long lastReadMessageId) {
        if (senderUsername == null || readerUsername == null
                || senderUsername.equalsIgnoreCase(readerUsername)
                || lastReadMessageId <= 0) return;
        Set<WebSocketSession> sockets = sessionsByUsername.get(senderUsername);
        if (sockets == null || sockets.isEmpty()) return;
        try {
            String payload = objectMapper.createObjectNode()
                    .put("type", "CHAT_READ_RECEIPT")
                    .set("receipt", objectMapper.createObjectNode()
                            .put("peerUsername", readerUsername)
                            .put("lastReadMessageId", lastReadMessageId))
                    .toString();
            for (WebSocketSession socket : sockets) {
                if (!socket.isOpen()) continue;
                try { send(socket, payload); } catch (IOException ignored) { }
            }
        } catch (RuntimeException ignored) {
            // Polling the persisted GET remains the recovery path.
        }
    }

    /**
     * Own read events are private: the original reader already passed the
     * authenticated HTTP access check. Send only to their authenticated
     * WebSocket sessions (not to community topics or message recipients).
     */
    public void publishOwnInboxRead(
            String username, String kind, String conversationId, long lastReadMessageId) {
        if (username == null || username.isBlank()
                || !("direct".equals(kind) || "community".equals(kind)
                    || "session".equals(kind))
                || conversationId == null || conversationId.isBlank()
                || lastReadMessageId <= 0) return;
        Set<WebSocketSession> sockets = sessionsByUsername.get(username);
        if (sockets == null || sockets.isEmpty()) return;
        try {
            String payload = objectMapper.createObjectNode()
                    .put("type", "INBOX_OWN_READ_UPDATED")
                    .set("read", objectMapper.createObjectNode()
                            .put("kind", kind)
                            .put("conversationId", conversationId)
                            .put("lastReadMessageId", lastReadMessageId))
                    .toString();
            for (WebSocketSession socket : sockets) {
                if (!socket.isOpen()) continue;
                try { send(socket, payload); } catch (IOException ignored) { }
            }
        } catch (RuntimeException ignored) {
            // WebSocket failures never roll back the committed cursor.
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
            // Project only public reaction fields. The domain event also
            // holds private recipients for server-side fan-out.
            var details = objectMapper.createObjectNode()
                    .put("kind", reaction.kind())
                    .put("conversationId", reaction.conversationId())
                    .put("messageId", reaction.messageId())
                    .put("actorUsername", reaction.actorUsername());
            details.set("counts", objectMapper.valueToTree(reaction.counts()));
            if (reaction.emoji() == null) details.putNull("emoji");
            else details.put("emoji", reaction.emoji());
            String payload = objectMapper.createObjectNode()
                    .put("type", "CHAT_REACTION_CHANGED")
                    .set("reaction", details)
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
        lastTypingAtBySocket.remove(session.getId());
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
