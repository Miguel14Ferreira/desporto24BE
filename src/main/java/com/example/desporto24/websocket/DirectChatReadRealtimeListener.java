package com.example.desporto24.websocket;

import com.example.desporto24.service.interfaces.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/** Push only after commit, and re-check friendship/block rules at delivery. */
@Component
@RequiredArgsConstructor
public class DirectChatReadRealtimeListener {
    private final ChatRealtimeGateway gateway;
    private final ChatService chatService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReadConfirmed(DirectChatReadConfirmedEvent event) {
        if (event == null || event.readerUsername() == null
                || event.senderUsername() == null || event.lastReadMessageId() <= 0
                || event.readerUsername().equalsIgnoreCase(event.senderUsername())) return;
        try {
            chatService.getChatInfo(event.readerUsername(), event.senderUsername());
            gateway.publishDirectReadReceipt(
                    event.senderUsername(), event.readerUsername(), event.lastReadMessageId());
        } catch (RuntimeException ignored) {
            // An expired friendship, a blocked contact or an unavailable socket
            // must never leak the watermark or roll back the committed read.
        }
    }
}
