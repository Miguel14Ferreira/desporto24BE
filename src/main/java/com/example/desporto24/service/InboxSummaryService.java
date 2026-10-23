package com.example.desporto24.service;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityTimelineEntry;
import com.example.desporto24.model.dto.chat.DirectReadReceiptResponse;
import com.example.desporto24.model.dto.chat.InboxReadRequest;
import com.example.desporto24.model.dto.chat.InboxRequest;
import com.example.desporto24.model.dto.chat.InboxSummaryResponse;
import com.example.desporto24.model.session.SessionMessage;
import com.example.desporto24.repository.ChatReadCursorRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTimelineEntryRepository;
import com.example.desporto24.repository.session.SessionMessageRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.service.interfaces.ChatService;
import com.example.desporto24.websocket.DirectChatReadConfirmedEvent;
import com.example.desporto24.websocket.InboxOwnReadUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Per-profile read watermarks, not a copy of chat messages or a global unread
 * notification count. Every lookup rechecks membership against original models.
 */
@Service
@RequiredArgsConstructor
public class InboxSummaryService {
    private final ProfileRepository profiles;
    private final ChatService chatService;
    private final ChatRepository chats;
    private final CommunityRepository communities;
    private final CommunityMemberRepository members;
    private final CommunityTimelineEntryRepository timelines;
    private final SessionRepository sessions;
    private final SessionParticipantRepository participants;
    private final SessionMessageRepository sessionMessages;
    private final ChatReadCursorRepository cursors;
    private final ApplicationEventPublisher events;

    @Transactional(readOnly = true)
    public List<InboxSummaryResponse> summaries(String username, InboxRequest request) {
        Profile actor = requireActor(username);
        // Deduplicate and cap input; inaccessible conversations are omitted,
        // not described with a different status that would disclose their contents.
        Map<String, InboxRequest.Conversation> unique = new LinkedHashMap<>();
        for (var item : request.conversations()) {
            String kind = normalizeKind(item.kind());
            String key = canonicalKey(kind, item.id());
            unique.putIfAbsent(kind + ":" + key, new InboxRequest.Conversation(kind, key));
        }
        List<InboxSummaryResponse> result = new ArrayList<>();
        for (var item : unique.values()) {
            try {
                result.add(summary(actor, item.kind(), item.id()));
            } catch (AccessDeniedException | ResponseStatusException e) {
                // Membership may change between listing and fetching summaries.
                // Never leak the latest message or read state to former members.
            }
        }
        return result;
    }

    /**
     * Recipient-authenticated read receipt for a direct chat, never an inferred
     * delivery acknowledgement. Only authorized friends can see this watermark.
     */
    @Transactional(readOnly = true)
    public DirectReadReceiptResponse peerDirectReadReceipt(String actorUsername, String peerUsername) {
        Profile actor = requireActor(actorUsername);
        String peerKey = canonicalKey("direct", peerUsername);
        Profile peer = profiles.findUserByUsernameIgnoreCase(peerKey)
                .orElseThrow(() -> new AccessDeniedException("No accessible chat"));
        if (Objects.equals(actor.getId(), peer.getId())) {
            throw new AccessDeniedException("No accessible chat");
        }
        // Existing friendship and block checks must pass before the read cursor
        // can be queried or disclosed. Never leak cursors from other chats.
        chatService.getChatInfo(actor.getUsername(), peer.getUsername());
        String readerConversationKey = canonicalKey("direct", actor.getUsername());
        long watermark = cursors.findByProfileIdAndConversationKindAndConversationKey(
                peer.getId(), "direct", readerConversationKey)
                .map(c -> c.getLastReadMessageId()).orElse(0L);
        return new DirectReadReceiptResponse(peerKey, Math.max(0L, watermark));
    }

    @Transactional(readOnly = true)
    public void requireConversationAccess(String username, String kind, String conversationId) {
        String normalized = normalizeKind(kind);
        summary(requireActor(username), normalized, canonicalKey(normalized, conversationId));
    }

    @Transactional
    public InboxSummaryResponse markRead(String username, InboxReadRequest request) {
        Profile actor = requireActor(username);
        String kind = normalizeKind(request.kind());
        String key = canonicalKey(kind, request.id());
        InboxSummaryResponse snapshot = summary(actor, kind, key);
        if (snapshot.latestMessageId() == null) return snapshot;
        long watermark = Math.min(request.lastSeenMessageId(), snapshot.latestMessageId());
        long previous = cursors.findByProfileIdAndConversationKindAndConversationKey(
                actor.getId(), kind, key)
                .map(c -> c.getLastReadMessageId()).orElse(0L);
        if (watermark > previous) {
            // Atomic monotonic upsert: concurrent tabs/devices must never move
            // the read cursor backwards, nor create duplicate rows.
            cursors.advance(actor.getId(), kind, key, watermark);
            // A second tab/device can now refresh only this conversation's
            // server-authoritative unread count without waiting for polling.
            events.publishEvent(new InboxOwnReadUpdatedEvent(
                    actor.getUsername(), kind, key, watermark));
            if ("direct".equals(kind)) {
                // This durable cursor (never an inferred device receipt) is
                // announced only AFTER_COMMIT. The listener rechecks access.
                Profile sender = profiles.findUserByUsernameIgnoreCase(key)
                        .orElseThrow(() -> new AccessDeniedException("No accessible chat"));
                events.publishEvent(new DirectChatReadConfirmedEvent(
                        actor.getUsername(), sender.getUsername(), watermark));
            }
        }
        return summary(actor, kind, key);
    }

    private InboxSummaryResponse summary(Profile actor, String kind, String key) {
        long readId = cursors.findByProfileIdAndConversationKindAndConversationKey(
                actor.getId(), kind, key)
                .map(c -> c.getLastReadMessageId()).orElse(0L);

        return switch (kind) {
            case "direct" -> directSummary(actor, key, readId);
            case "community" -> communitySummary(actor, Long.parseLong(key), key, readId);
            case "session" -> sessionSummary(actor, Long.parseLong(key), key, readId);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid conversation kind");
        };
    }

    private InboxSummaryResponse directSummary(Profile actor, String key, long readId) {
        Profile friend = profiles.findUserByUsernameIgnoreCase(key)
                .orElseThrow(() -> new AccessDeniedException("No accessible chat"));
        // The existing chat service additionally enforces friendship,
        // user blocks and the backend's normal authorization rules.
        chatService.getChatInfo(actor.getUsername(), friend.getUsername());
        var latest = chats.findConversationPage(actor.getUsername(), friend.getUsername(),
                PageRequest.of(0, 1, org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.DESC, "date", "id")))
                .getContent();
        if (latest.isEmpty()) return empty("direct", key);
        Chat message = latest.get(0);
        Date at = message.getDate();
        return new InboxSummaryResponse("direct", key, message.getId(), message.getText(),
                message.getUsername1(), at == null ? null : at.toInstant(),
                chats.countUnread(actor.getUsername(), friend.getUsername(), readId));
    }

    private InboxSummaryResponse communitySummary(Profile actor, Long id, String key, long readId) {
        Community community = communities.findById(id)
                .orElseThrow(() -> new AccessDeniedException("No accessible community"));
        boolean owner = community.getOwner() != null
                && Objects.equals(community.getOwner().getId(), actor.getId());
        if (!owner && !members.existsByCommunityIdAndProfileId(id, actor.getId())) {
            throw new AccessDeniedException("No accessible community");
        }
        var latest = timelines.findByCommunityIdAndKindOrderByIdDesc(
                id, CommunityTimelineEntry.Kind.CHAT, PageRequest.of(0, 1));
        if (latest.isEmpty()) return empty("community", key);
        var entry = latest.get(0);
        return new InboxSummaryResponse("community", key, entry.getId(), entry.getBody(),
                entry.getAuthor().getUsername(), entry.getCreatedAt(),
                timelines.countUnread(id, CommunityTimelineEntry.Kind.CHAT, actor.getId(), readId));
    }

    private InboxSummaryResponse sessionSummary(Profile actor, Long id, String key, long readId) {
        Session session = sessions.findById(id)
                .orElseThrow(() -> new AccessDeniedException("No accessible session"));
        if (!SessionChatService.isChatOpen(session, java.time.Instant.now())) {
            throw new AccessDeniedException("Session chat is no longer available");
        }
        boolean owner = session.getProfile() != null
                && Objects.equals(session.getProfile().getId(), actor.getId());
        if (!owner && !participants.existsBySessionIdAndProfileIdAndState(
                id, actor.getId(), ParticipantState.ACTIVE)) {
            throw new AccessDeniedException("No accessible session");
        }
        var latest = sessionMessages.findBySessionIdOrderByIdDesc(id, PageRequest.of(0, 1));
        if (latest.isEmpty()) return empty("session", key);
        SessionMessage entry = latest.get(0);
        return new InboxSummaryResponse("session", key, entry.getId(), entry.getBody(),
                entry.getSenderProfile().getUsername(), entry.getCreatedAt(),
                sessionMessages.countUnread(id, actor.getId(), readId));
    }

    private InboxSummaryResponse empty(String kind, String key) {
        return new InboxSummaryResponse(kind, key, null, null, null, null, 0);
    }

    private Profile requireActor(String username) {
        return profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Not authenticated"));
    }

    private String normalizeKind(String kind) {
        if (kind == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid kind");
        String normalized = kind.toLowerCase(Locale.ROOT);
        if (!normalized.equals("direct") && !normalized.equals("community")
                && !normalized.equals("session")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid kind");
        }
        return normalized;
    }

    private String canonicalKey(String kind, String value) {
        if (value == null || value.isBlank() || value.length() > 128) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid conversation");
        }
        if (kind.equals("direct")) {
            String username = value.trim().toLowerCase(Locale.ROOT);
            if (!username.matches("[a-z0-9_.@-]{1,128}")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username");
            }
            return username;
        }
        if (!value.matches("[1-9][0-9]{0,17}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid conversation");
        }
        try {
            return Long.toString(Long.parseLong(value));
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid conversation");
        }
    }
}
