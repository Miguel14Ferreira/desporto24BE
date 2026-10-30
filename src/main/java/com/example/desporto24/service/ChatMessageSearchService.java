package com.example.desporto24.service;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.model.Chat;
import com.example.desporto24.model.community.CommunityTimelineEntry;
import com.example.desporto24.model.dto.chat.ChatSearchHit;
import com.example.desporto24.model.dto.chat.ChatSearchResponse;
import com.example.desporto24.model.session.SessionMessage;
import com.example.desporto24.repository.community.CommunityTimelineEntryRepository;
import com.example.desporto24.repository.session.SessionMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

/**
 * Read-only message search, including history outside the currently loaded UI
 * page. Authorization is exactly the same as for persistent inbox snapshots.
 *
 * Input is bounded; SQL LIKE wildcards are escaped so search is literal.
 */
@Service
@RequiredArgsConstructor
public class ChatMessageSearchService {
    private static final int PAGE_SIZE = 20;
    private static final int MAX_PAGE = 50;
    private final InboxSummaryService inbox;
    private final ChatRepository direct;
    private final SessionMessageRepository sessions;
    private final CommunityTimelineEntryRepository communities;

    @Transactional(readOnly = true)
    public ChatSearchResponse search(String username, String kind,
                                     String id, String query, int page) {
        if (kind == null || id == null || query == null || page < 0 || page > MAX_PAGE) {
            throw invalidSearch();
        }
        String term = query.strip();
        if (term.length() < 2 || term.length() > 80) throw invalidSearch();
        String normalizedKind = kind.toLowerCase(Locale.ROOT);
        if (!normalizedKind.equals("direct") && !normalizedKind.equals("session")
                && !normalizedKind.equals("community")) throw invalidSearch();

        // No query or message text is returned before this membership check.
        inbox.requireConversationAccess(username, normalizedKind, id);
        String escaped = term.toLowerCase(Locale.ROOT)
                .replace("!", "!!").replace("%", "!%").replace("_", "!_");
        PageRequest paging = PageRequest.of(page, PAGE_SIZE);
        Page<ChatSearchHit> matches = switch (normalizedKind) {
            case "direct" -> direct.searchConversation(username, id, escaped, paging)
                    .map(this::directHit);
            case "session" -> sessions.searchConversation(Long.parseLong(id), escaped, paging)
                    .map(this::sessionHit);
            case "community" -> communities.searchConversation(Long.parseLong(id),
                    CommunityTimelineEntry.Kind.CHAT, escaped, paging)
                    .map(this::communityHit);
            default -> throw invalidSearch();
        };
        return new ChatSearchResponse(matches.getContent(), matches.hasNext(), page);
    }


    /** Up to 20 earlier messages, the original and 20 newer messages. */
    @Transactional(readOnly = true)
    public java.util.List<ChatSearchHit> context(String username, String kind,
                                                  String id, long targetMessageId) {
        if (kind == null || id == null || targetMessageId <= 0) throw invalidSearch();
        String normalizedKind = kind.toLowerCase(Locale.ROOT);
        if (!normalizedKind.equals("direct") && !normalizedKind.equals("session")
                && !normalizedKind.equals("community")) throw invalidSearch();
        // Validate access before fetching any message or revealing target existence.
        inbox.requireConversationAccess(username, normalizedKind, id);
        PageRequest older = PageRequest.of(0, 21), newer = PageRequest.of(0, 20);
        return switch (normalizedKind) {
            case "direct" -> window(
                    direct.findConversationContextBefore(username, id, targetMessageId, older)
                            .stream().map(this::directHit).toList(),
                    direct.findConversationContextAfter(username, id, targetMessageId, newer)
                            .stream().map(this::directHit).toList(), targetMessageId);
            case "session" -> window(
                    sessions.findBySessionIdAndIdLessThanEqualOrderByIdDesc(
                            Long.parseLong(id), targetMessageId, older)
                            .stream().map(this::sessionHit).toList(),
                    sessions.findBySessionIdAndIdGreaterThanOrderByIdAsc(
                            Long.parseLong(id), targetMessageId, newer)
                            .stream().map(this::sessionHit).toList(), targetMessageId);
            case "community" -> window(
                    communities.findByCommunityIdAndKindAndIdLessThanEqualOrderByIdDesc(
                            Long.parseLong(id), CommunityTimelineEntry.Kind.CHAT, targetMessageId, older)
                            .stream().map(this::communityHit).toList(),
                    communities.findByCommunityIdAndKindAndIdGreaterThanOrderByIdAsc(
                            Long.parseLong(id), CommunityTimelineEntry.Kind.CHAT, targetMessageId, newer)
                            .stream().map(this::communityHit).toList(), targetMessageId);
            default -> throw invalidSearch();
        };
    }

    private java.util.List<ChatSearchHit> window(
            java.util.List<ChatSearchHit> before, java.util.List<ChatSearchHit> after, long id) {
        if (before.stream().noneMatch(hit -> hit.id() == id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not in conversation");
        var list = new java.util.ArrayList<ChatSearchHit>(before);
        list.addAll(after);
        list.sort(java.util.Comparator.comparingLong(ChatSearchHit::id));
        return list;
    }

    private ChatSearchHit directHit(Chat msg) {
        return new ChatSearchHit(msg.getId(), msg.getUsername1(), msg.getText(),
                msg.getDate() == null ? null : msg.getDate().toInstant(),
                msg.getReplyToMessageId(), msg.getReplyToSender(), msg.getReplyToText());
    }

    private ChatSearchHit sessionHit(SessionMessage msg) {
        return new ChatSearchHit(msg.getId(), msg.getSenderProfile().getUsername(),
                msg.getBody(), msg.getCreatedAt(), msg.getReplyToMessageId(),
                msg.getReplyToSender(), msg.getReplyToText());
    }

    private ChatSearchHit communityHit(CommunityTimelineEntry msg) {
        return new ChatSearchHit(msg.getId(), msg.getAuthor().getUsername(),
                msg.getBody(), msg.getCreatedAt(), msg.getReplyToMessageId(),
                msg.getReplyToSender(), msg.getReplyToText());
    }

    private ResponseStatusException invalidSearch() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid chat search");
    }
}
