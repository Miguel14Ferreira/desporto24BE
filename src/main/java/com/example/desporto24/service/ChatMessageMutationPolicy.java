package com.example.desporto24.service;

import java.time.Duration;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Server-side invariant for all private/community/session message mutations. */
public final class ChatMessageMutationPolicy {
    private ChatMessageMutationPolicy() {}

    public static String editText(String proposed, Instant createdAt,
                                  Instant now, Instant deletedAt, boolean hasAttachment) {
        if (deletedAt != null || hasAttachment) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This message cannot be edited");
        }
        if (createdAt == null || now.isBefore(createdAt)
                || Duration.between(createdAt, now).compareTo(Duration.ofMinutes(15)) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "15-minute edit window expired");
        }
        if (proposed == null || proposed.isBlank() || proposed.length() > 2000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid edited message");
        }
        String normalized = proposed.trim();
        // GIPHY embeds must be created through the existing picker, not forged via edit.
        if (normalized.startsWith("[JYNLO:GIPHY:1]")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Structured GIF messages cannot be edited");
        }
        return normalized;
    }

    public static void requireNotDeleted(Instant deletedAt) {
        if (deletedAt != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Message already deleted");
        }
    }
}
