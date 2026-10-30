package com.example.desporto24.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ChatMessageRealtimeListener {

    private final ChatRealtimeGateway chatRealtimeGateway;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onChatMessageCreated(ChatMessageCreatedEvent event) {
        if (event == null || event.message() == null) return;
        var message = event.message();
        String sender = message.getUsername1();
        String recipient = message.getUsername2();
        // Message state is committed before delivery. The original sender's
        // other authenticated sockets must receive the same server-owned ID,
        // so their thread updates without polling or inventing a new message.
        if (recipient != null && !recipient.isBlank()) {
            chatRealtimeGateway.publishChatMessage(recipient, message);
        }
        if (sender != null && !sender.isBlank()
                && !sender.equalsIgnoreCase(recipient)) {
            chatRealtimeGateway.publishChatMessage(sender, message);
        }
    }
}
