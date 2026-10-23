package com.example.desporto24.websocket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InboxOwnReadRealtimeListenerTest {
    @Mock ChatRealtimeGateway gateway;
    @InjectMocks InboxOwnReadRealtimeListener listener;

    @Test
    void sendsOnlyToAuthenticatedReadersSocketsForAllInboxKinds() {
        for (String kind : new String[]{"direct", "community", "session"}) {
            listener.onCommittedRead(new InboxOwnReadUpdatedEvent("ana", kind,
                    "direct".equals(kind) ? "bea" : "7", 42));
        }
        verify(gateway).publishOwnInboxRead("ana", "direct", "bea", 42);
        verify(gateway).publishOwnInboxRead("ana", "community", "7", 42);
        verify(gateway).publishOwnInboxRead("ana", "session", "7", 42);
        verifyNoMoreInteractions(gateway);
    }

    @Test
    void malformedEventsCannotBeBroadcast() {
        listener.onCommittedRead(null);
        listener.onCommittedRead(new InboxOwnReadUpdatedEvent("ana", "direct", "bea", 0));
        listener.onCommittedRead(new InboxOwnReadUpdatedEvent("ana", "unknown", "bea", 42));
        listener.onCommittedRead(new InboxOwnReadUpdatedEvent("ana", "direct", "", 42));
        listener.onCommittedRead(new InboxOwnReadUpdatedEvent(null, "direct", "bea", 42));
        verifyNoInteractions(gateway);
    }

    @Test
    void deliveryFailureDoesNotUndoCommittedCursor() {
        doThrow(new IllegalStateException("offline"))
                .when(gateway).publishOwnInboxRead("ana", "direct", "bea", 42);
        assertDoesNotThrow(() -> listener.onCommittedRead(
                new InboxOwnReadUpdatedEvent("ana", "direct", "bea", 42)));
    }

    @Test
    void strictlyAfterCommit() throws Exception {
        var annotation = InboxOwnReadRealtimeListener.class.getMethod(
                "onCommittedRead", InboxOwnReadUpdatedEvent.class)
                .getAnnotation(TransactionalEventListener.class);
        assertNotNull(annotation);
        assertEquals(TransactionPhase.AFTER_COMMIT, annotation.phase());
    }
}
