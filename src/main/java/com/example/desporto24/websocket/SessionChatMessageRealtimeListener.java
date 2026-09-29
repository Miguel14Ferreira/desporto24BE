package com.example.desporto24.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SessionChatMessageRealtimeListener {

    private final ChatRealtimeGateway chatRealtimeGateway;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSessionChatMessageCreated(SessionChatMessageCreatedEvent event) {
        chatRealtimeGateway.publishSessionChatMessage(
                event.recipients(),
                event.message()
        );
    }
}
