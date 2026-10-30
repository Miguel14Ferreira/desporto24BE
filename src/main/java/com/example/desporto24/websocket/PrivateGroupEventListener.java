package com.example.desporto24.websocket;

import com.example.desporto24.service.PrivateGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/** Publish only after the group/membership/message transaction commits. */
@Component @RequiredArgsConstructor
public class PrivateGroupEventListener {
    private final ChatRealtimeGateway gateway;
    @TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
    public void notifyGroup(PrivateGroupService.GroupEvent event) {
        gateway.publishPrivateGroupEvent(event.usernames(),event.groupId(),event.message());
    }
}
