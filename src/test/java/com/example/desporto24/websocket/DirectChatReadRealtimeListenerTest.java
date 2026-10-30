package com.example.desporto24.websocket;

import com.example.desporto24.service.interfaces.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectChatReadRealtimeListenerTest {
    @Mock ChatRealtimeGateway gateway;
    @Mock ChatService chatService;
    @InjectMocks DirectChatReadRealtimeListener listener;

    @Test
    void sendsOnlyPersistedConfirmedWatermarkToOriginalSender() {
        listener.onReadConfirmed(new DirectChatReadConfirmedEvent("bea", "ana", 42));
        verify(chatService).getChatInfo("bea", "ana");
        verify(gateway).publishDirectReadReceipt("ana", "bea", 42L);
        verifyNoMoreInteractions(gateway);
    }

    @Test
    void deniedOrBlockedPeerGetsNoNotification() {
        doThrow(new AccessDeniedException("Blocked"))
                .when(chatService).getChatInfo("bea", "ana");
        assertDoesNotThrow(() -> listener.onReadConfirmed(
                new DirectChatReadConfirmedEvent("bea", "ana", 42)));
        verifyNoInteractions(gateway);
    }

    @Test
    void invalidEventDoesNotQueryFriendshipOrDiscloseState() {
        listener.onReadConfirmed(new DirectChatReadConfirmedEvent("bea", "ana", 0));
        listener.onReadConfirmed(new DirectChatReadConfirmedEvent("bea", "bea", 99));
        listener.onReadConfirmed(null);
        verifyNoInteractions(chatService, gateway);
    }

    @Test
    void listenerRunsStrictlyAfterCommittedRead() throws Exception {
        var method = DirectChatReadRealtimeListener.class.getMethod(
                "onReadConfirmed", DirectChatReadConfirmedEvent.class);
        var annotation = method.getAnnotation(TransactionalEventListener.class);
        assertNotNull(annotation);
        assertEquals(TransactionPhase.AFTER_COMMIT, annotation.phase());
    }
}
