package com.example.desporto24.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChatReactionRealtimeListener {
    private final ChatRealtimeGateway gateway;
    private final CommunityChatRecipientResolver communityRecipients;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onChanged(ChatReactionChangedEvent event) {
        try {
            Set<String> recipients = new HashSet<>(event.recipients());
            if ("community".equals(event.kind())) {
                // Never publish a private community reaction to recently removed members.
                recipients.retainAll(communityRecipients.currentRecipients(
                        Long.parseLong(event.conversationId())));
            }
            gateway.publishChatReaction(recipients, event);
        } catch (RuntimeException ignored) {
            // A failed realtime notification must never undo a committed reaction.
        }
    }
}
