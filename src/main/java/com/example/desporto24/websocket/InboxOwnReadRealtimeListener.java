package com.example.desporto24.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/** Send to the reader's authenticated sockets only after the read transaction commits. */
@Component
@RequiredArgsConstructor
public class InboxOwnReadRealtimeListener {
    private final ChatRealtimeGateway gateway;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCommittedRead(InboxOwnReadUpdatedEvent event) {
        if (event == null || event.username() == null || event.username().isBlank()
                || event.conversationId() == null || event.conversationId().isBlank()
                || event.lastReadMessageId() <= 0
                || !("direct".equals(event.kind()) || "community".equals(event.kind())
                     || "session".equals(event.kind()))) return;
        try {
            gateway.publishOwnInboxRead(
                    event.username(), event.kind(), event.conversationId(),
                    event.lastReadMessageId());
        } catch (RuntimeException ignored) {
            // Notification is best-effort. The committed cursor and the
            // existing periodic summaries are the authoritative recovery path.
        }
    }
}
