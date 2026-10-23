package com.example.desporto24.service;

import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.websocket.CommunityChatRecipientResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/** Permission-checked ephemeral typing audience. No database mutations. */
@Service
@RequiredArgsConstructor
public class ChatTypingService {
    private final InboxSummaryService inbox;
    private final ProfileRepository profiles;
    private final SessionRepository sessions;
    private final SessionParticipantRepository participants;
    private final CommunityChatRecipientResolver communityRecipients;

    public record Audience(String conversationId, Set<String> usernames) {}

    @Transactional(readOnly = true)
    public Audience authorizedRecipients(String actor, String kind, String conversationId) {
        if (actor == null || actor.isBlank() || kind == null || conversationId == null
                || conversationId.isBlank() || conversationId.length() > 128)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        String normalized = kind.toLowerCase(Locale.ROOT);
        // Reuse inbox authorization (friendship / blocks / membership).
        inbox.requireConversationAccess(actor, normalized, conversationId);
        return switch (normalized) {
            case "direct" -> {
                var peer = profiles.findUserByUsernameIgnoreCase(conversationId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
                yield new Audience(peer.getUsername().toLowerCase(Locale.ROOT),
                        Set.of(peer.getUsername()));
            }
            case "session" -> {
                long id = Long.parseLong(conversationId);
                var session = sessions.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
                if (!SessionChatService.isChatOpen(session, Instant.now()))
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                Set<String> names = new LinkedHashSet<>();
                if (session.getProfile() != null) names.add(session.getProfile().getUsername());
                participants.findBySessionIdAndStateOrderByJoinedAtAsc(id, ParticipantState.ACTIVE)
                        .forEach(p -> names.add(p.getProfile().getUsername()));
                yield new Audience(Long.toString(id), names);
            }
            case "community" -> {
                long id = Long.parseLong(conversationId);
                yield new Audience(Long.toString(id), communityRecipients.currentRecipients(id));
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        };
    }
}
