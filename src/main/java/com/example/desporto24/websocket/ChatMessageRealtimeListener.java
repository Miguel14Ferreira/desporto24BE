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
        chatRealtimeGateway.publishChatMessage(
                event.message().getUsername2(),
                event.message()
        );
    }
}
