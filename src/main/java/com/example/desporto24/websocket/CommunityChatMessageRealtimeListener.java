package com.example.desporto24.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CommunityChatMessageRealtimeListener {
    private final ChatRealtimeGateway gateway;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCommunityMessage(CommunityChatMessageCreatedEvent event) {
        gateway.publishCommunityChatMessage(event.recipients(), event.message());
    }
}
