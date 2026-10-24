package com.example.desporto24.websocket;

import com.example.desporto24.model.Chat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageRealtimeListenerTest {
    @Mock ChatRealtimeGateway gateway;
    @InjectMocks ChatMessageRealtimeListener listener;

    private Chat committedMessage(String sender, String recipient) {
        Chat chat = new Chat(sender, recipient, "Olá no outro dispositivo", "",
                new Date(1771632000000L));
        chat.setId(42L);
        return chat;
    }

    @Test
    void committedMessageReachesReceiverAndOriginalSendersAuthenticatedSockets() {
        Chat message = committedMessage("ana", "bea");
        listener.onChatMessageCreated(new ChatMessageCreatedEvent(message));

        var order = inOrder(gateway);
        order.verify(gateway).publishChatMessage("bea", message);
        order.verify(gateway).publishChatMessage("ana", message);
        verifyNoMoreInteractions(gateway);
        assertEquals(42L, message.getId());
    }

    @Test
    void selfChatCannotTriggerDuplicatePublication() {
        Chat message = committedMessage("ana", "ANA");
        listener.onChatMessageCreated(new ChatMessageCreatedEvent(message));

        verify(gateway).publishChatMessage("ANA", message);
        verifyNoMoreInteractions(gateway);
    }

    @Test
    void incompleteEventsDoNotBroadcast() {
        listener.onChatMessageCreated(null);
        listener.onChatMessageCreated(new ChatMessageCreatedEvent(null));
        verifyNoInteractions(gateway);
    }

    @Test
    void distributionOccursOnlyAfterTransactionCommit() throws Exception {
        var method = ChatMessageRealtimeListener.class.getMethod(
                "onChatMessageCreated", ChatMessageCreatedEvent.class);
        var annotation = method.getAnnotation(TransactionalEventListener.class);
        assertNotNull(annotation);
        assertEquals(TransactionPhase.AFTER_COMMIT, annotation.phase());
    }
}
