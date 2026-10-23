package com.example.desporto24.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommunityChatMessageRealtimeListener {
    private final ChatRealtimeGateway gateway;
    private final CommunityChatRecipientResolver recipientResolver;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCommunityMessage(CommunityChatMessageCreatedEvent event) {
        if (event == null || event.message() == null || event.recipients() == null) {
            return;
        }

        try {
            // Use a new read-only transaction: a member removed after the POST
            // began must not receive the message through a still-open socket.
            Set<String> authorizedNow = new HashSet<>(
                    recipientResolver.currentRecipients(event.message().communityId()));
            // Do not widen the original event's audience to people who joined later.
            authorizedNow.retainAll(event.recipients());
            gateway.publishCommunityChatMessage(authorizedNow, event.message());
        } catch (RuntimeException ignored) {
            // If membership cannot be checked, fail closed for realtime delivery.
            // The committed message remains available via authorized HTTP history.
        }
    }
}
