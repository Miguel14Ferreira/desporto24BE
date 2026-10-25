package com.example.desporto24.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class ChatMessageMutationPolicyTest {
    private final Instant created = Instant.parse("2026-10-25T12:00:00Z");

    @Test
    void acceptsTextWithin15MinutesWithoutChangingCreationTime() {
        assertEquals("Olá mundo", ChatMessageMutationPolicy.editText(
                " Olá mundo ", created, created.plusSeconds(899), null, false));
    }

    @Test
    void rejectsEditAfterWindowAndMessagesWithUnreliableClock() {
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> ChatMessageMutationPolicy.editText(
                    "x", created, created.plusSeconds(901), null, false)).getStatusCode());
        assertThrows(ResponseStatusException.class,
                () -> ChatMessageMutationPolicy.editText("x", created, created.minusSeconds(1), null, false));
    }

    @Test
    void rejectsDeletedAttachmentsAndStructuredGifPayloads() {
        assertThrows(ResponseStatusException.class,
                () -> ChatMessageMutationPolicy.editText("x", created, created, created, false));
        assertThrows(ResponseStatusException.class,
                () -> ChatMessageMutationPolicy.editText("x", created, created, null, true));
        assertThrows(ResponseStatusException.class,
                () -> ChatMessageMutationPolicy.editText("[JYNLO:GIPHY:1]{}",
                        created, created, null, false));
    }

    @Test
    void validatesTrimmedUnicodeTextAndMaximumLength() {
        assertThrows(ResponseStatusException.class,
                () -> ChatMessageMutationPolicy.editText("  ", created, created, null, false));
        assertThrows(ResponseStatusException.class,
                () -> ChatMessageMutationPolicy.editText("x".repeat(2001), created, created, null, false));
        assertEquals("⚽", ChatMessageMutationPolicy.editText(" ⚽ ", created, created, null, false));
    }

    @Test
    void rejectsRepeatedDeletionButAllowsFirstDeletion() {
        assertDoesNotThrow(() -> ChatMessageMutationPolicy.requireNotDeleted(null));
        assertEquals(HttpStatus.CONFLICT, assertThrows(ResponseStatusException.class,
                () -> ChatMessageMutationPolicy.requireNotDeleted(created)).getStatusCode());
    }
}
