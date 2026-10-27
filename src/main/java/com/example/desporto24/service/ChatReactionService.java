package com.example.desporto24.service;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.CommunityTimelineEntry;
import com.example.desporto24.model.dto.chat.ChatReactionSummary;
import com.example.desporto24.model.dto.chat.ChatReactionParticipant;
import com.example.desporto24.model.session.SessionMessage;
import com.example.desporto24.repository.*;
import com.example.desporto24.repository.community.CommunityTimelineEntryRepository;
import com.example.desporto24.repository.session.SessionMessageRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.websocket.ChatReactionChangedEvent;
import com.example.desporto24.websocket.CommunityChatRecipientResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * All reaction reads and writes recheck authenticated chat authorization.
 * Message IDs are validated against the exact conversation before any reaction
 * is read, preventing cross-community/session and direct-message ID probing.
 */
@Service
@RequiredArgsConstructor
public class ChatReactionService {
    private static final Set<String> EMOJIS = Set.of("👍", "❤️", "😂", "🎉", "👏", "🔥");
    private final InboxSummaryService inbox;
    private final ProfileRepository profiles;
    private final ChatRepository directMessages;
    private final SessionMessageRepository sessionMessages;
    private final CommunityTimelineEntryRepository communityMessages;
    private final ChatMessageReactionRepository reactions;
    private final SessionRepository sessions;
    private final SessionParticipantRepository participants;
    private final CommunityChatRecipientResolver communityRecipients;
    private final ApplicationEventPublisher events;

    @Transactional(readOnly = true)
    public List<ChatReactionSummary> summaries(String username, String kind,
                                               String conversationId, List<Long> ids) {
        Profile actor = requireActor(username, kind, conversationId);
        if (ids == null || ids.isEmpty() || ids.size() > 100
                || ids.stream().anyMatch(id -> id == null || id <= 0)) {
            throw badRequest();
        }
        LinkedHashSet<Long> accepted = existingMessageIds(kind, conversationId,
                actor.getUsername(), new LinkedHashSet<>(ids));
        return aggregates(actor.getId(), kind, accepted);
    }

    /**
     * Names are fetched only when a conversation member opens the details
     * dialog, never as part of bulk reaction loads or global websocket events.
     */
    @Transactional(readOnly = true)
    public List<ChatReactionParticipant> participants(String username, String kind,
            String conversationId, Long messageId) {
        Profile actor = requireActor(username, kind, conversationId);
        if (messageId == null || messageId <= 0) throw badRequest();
        if (!existingMessageIds(kind, conversationId, actor.getUsername(),
                Set.of(messageId)).contains(messageId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
        }
        var rows = reactions.findByConversationKindAndMessageIdIn(kind, List.of(messageId));
        if (rows.isEmpty()) return List.of();
        // Bounded names retrieval, without leaking passwords/email or the actor IDs.
        if (rows.size() > 200) rows = rows.subList(0, 200);
        LinkedHashSet<Long> profileIds = rows.stream()
                .map(com.example.desporto24.model.ChatMessageReaction::getProfileId)
                .filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
        Map<Long, Profile> byId = new HashMap<>();
        profiles.findAllById(profileIds).forEach(p -> byId.put(p.getId(), p));
        return rows.stream().map(row -> {
            Profile member = byId.get(row.getProfileId());
            if (member == null || member.getUsername() == null) return null;
            String name = member.getNickname();
            if (name == null || name.isBlank()) name = member.getFullName();
            if (name == null || name.isBlank()) name = member.getUsername();
            return new ChatReactionParticipant(member.getUsername(), name,
                    member.getProfilePicUrl(), row.getEmoji(),
                    actor.getId().equals(member.getId()));
        }).filter(Objects::nonNull)
          .sorted(Comparator.comparing(ChatReactionParticipant::mine).reversed()
                  .thenComparing(ChatReactionParticipant::displayName,
                          String.CASE_INSENSITIVE_ORDER))
          .toList();
    }

    @Transactional
    public ChatReactionSummary update(String username, String kind,
                                      String conversationId, Long messageId, String emoji) {
        Profile actor = requireActor(username, kind, conversationId);
        if (messageId == null || messageId <= 0 || emoji != null && !EMOJIS.contains(emoji)) {
            throw badRequest();
        }
        if (!existingMessageIds(kind, conversationId, actor.getUsername(),
                Set.of(messageId)).contains(messageId)) {
            // The same response for missing, deleted, or inaccessible messages.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
        }
        if (emoji == null) {
            reactions.deleteByProfileIdAndConversationKindAndMessageId(actor.getId(), kind, messageId);
        } else {
            reactions.upsert(actor.getId(), kind, messageId, emoji);
        }
        ChatReactionSummary updated = aggregates(actor.getId(), kind,
                Set.of(messageId)).getFirst();
        events.publishEvent(new ChatReactionChangedEvent(kind, conversationId, messageId,
                updated.counts(), actor.getUsername(), emoji,
                reactionRecipients(kind, conversationId, actor.getUsername(), messageId)));
        return updated;
    }

    private Profile requireActor(String username, String kind, String conversationId) {
        // The existing inbox enforces friendship/blocks and session/community
        // membership before returning even an empty accessible conversation.
        inbox.requireConversationAccess(username, kind, conversationId);
        return profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
    }

    private LinkedHashSet<Long> existingMessageIds(String kind, String conversationId,
            String username, Set<Long> requested) {
        LinkedHashSet<Long> result = new LinkedHashSet<>();
        switch (kind) {
            case "direct" -> {
                for (Chat msg : directMessages.findAllById(requested)) {
                    String from = msg.getUsername1(), to = msg.getUsername2();
                    if (from != null && to != null && (
                            from.equalsIgnoreCase(username) && to.equalsIgnoreCase(conversationId)
                            || to.equalsIgnoreCase(username) && from.equalsIgnoreCase(conversationId))) {
                        result.add(msg.getId());
                    }
                }
            }
            case "session" -> {
                long id = Long.parseLong(conversationId);
                for (SessionMessage msg : sessionMessages.findAllById(requested)) {
                    if (msg.getSession() != null && id == msg.getSession().getId()) {
                        result.add(msg.getId());
                    }
                }
            }
            case "community" -> {
                long id = Long.parseLong(conversationId);
                for (CommunityTimelineEntry msg : communityMessages.findAllById(requested)) {
                    if (msg.getKind() == CommunityTimelineEntry.Kind.CHAT
                            && msg.getCommunity() != null && id == msg.getCommunity().getId()) {
                        result.add(msg.getId());
                    }
                }
            }
            default -> throw badRequest();
        }
        return result;
    }

    private List<ChatReactionSummary> aggregates(Long actorId, String kind, Set<Long> ids) {
        if (ids.isEmpty()) return List.of();
        Map<Long, Map<String, Long>> countByMessage = new HashMap<>();
        Map<Long, String> mine = new HashMap<>();
        for (var reaction : reactions.findByConversationKindAndMessageIdIn(kind, ids)) {
            countByMessage.computeIfAbsent(reaction.getMessageId(), ignored -> new TreeMap<>())
                    .merge(reaction.getEmoji(), 1L, Long::sum);
            if (actorId.equals(reaction.getProfileId())) {
                mine.put(reaction.getMessageId(), reaction.getEmoji());
            }
        }
        return ids.stream().map(id -> new ChatReactionSummary(id,
                Map.copyOf(countByMessage.getOrDefault(id, Map.of())), mine.get(id))).toList();
    }

    private Set<String> reactionRecipients(String kind, String conversationId,
            String actorUsername, Long messageId) {
        switch (kind) {
            case "direct" -> {
                return Set.of(actorUsername, conversationId);
            }
            case "session" -> {
                Long id = Long.parseLong(conversationId);
                Set<String> recipients = new LinkedHashSet<>();
                sessions.findById(id).map(Session::getProfile)
                        .ifPresent(owner -> recipients.add(owner.getUsername()));
                participants.findBySessionIdAndStateOrderByJoinedAtAsc(id, ParticipantState.ACTIVE)
                        .forEach(member -> recipients.add(member.getProfile().getUsername()));
                return recipients;
            }
            case "community" -> {
                return communityRecipients.currentRecipients(Long.parseLong(conversationId));
            }
            default -> throw badRequest();
        }
    }

    private ResponseStatusException badRequest() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message reaction");
    }
}
