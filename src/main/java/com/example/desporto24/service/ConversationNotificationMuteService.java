package com.example.desporto24.service;

import com.example.desporto24.model.ConversationNotificationMute;
import com.example.desporto24.repository.ConversationNotificationMuteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Locale;

/**
 * Mutes affect only the per-conversation notification channel. Message delivery,
 * access, history and unread inbox cursors are not altered.
 */
@Service
@RequiredArgsConstructor
public class ConversationNotificationMuteService {
    private final ConversationNotificationMuteRepository repository;

    public record ConversationMute(String kind, String id) {}
    public record ChangeMute(String kind, String id, boolean muted) {}

    public static String kind(String raw) {
        if (raw == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid conversation kind");
        String value = raw.toLowerCase(Locale.ROOT);
        if (!List.of("direct", "community", "session", "private").contains(value))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid conversation kind");
        return value;
    }

    public static String key(String kind, String raw) {
        if (raw == null || raw.isBlank() || raw.length() > 128)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid conversation");
        if ("direct".equals(kind)) {
            String normalized = raw.toLowerCase(Locale.ROOT);
            if (!normalized.matches("[a-z0-9._-]{1,128}"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid conversation");
            return normalized;
        }
        try {
            long value = Long.parseLong(raw);
            if (value <= 0) throw new NumberFormatException();
            return Long.toString(value);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid conversation");
        }
    }

    private static String user(String username) {
        return username.toLowerCase(Locale.ROOT);
    }

    @Transactional(readOnly = true)
    public List<ConversationMute> list(String username) {
        return repository.findByUsername(user(username)).stream()
            .map(row -> new ConversationMute(row.getConversationKind(), row.getConversationKey()))
            .toList();
    }

    @Transactional(readOnly = true)
    public boolean muted(String username, String kind, String key) {
        return repository.findByUsernameAndConversationKindAndConversationKey(
            user(username), kind, key).isPresent();
    }

    @Transactional
    public ConversationMute update(String username, String kind, String key, boolean muted) {
        String actor = user(username);
        var existing = repository.findByUsernameAndConversationKindAndConversationKey(actor, kind, key);
        if (muted && existing.isEmpty()) {
            var row = new ConversationNotificationMute();
            row.setUsername(actor);
            row.setConversationKind(kind);
            row.setConversationKey(key);
            repository.save(row);
        } else if (!muted) {
            existing.ifPresent(repository::delete);
        }
        return new ConversationMute(kind, key);
    }
}
