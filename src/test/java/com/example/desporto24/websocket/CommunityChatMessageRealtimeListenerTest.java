package com.example.desporto24.websocket;

import com.example.desporto24.model.dto.community.CommunityTimelineEntryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityChatMessageRealtimeListenerTest {
    @Mock ChatRealtimeGateway gateway;
    @Mock CommunityChatRecipientResolver recipientResolver;
    @InjectMocks CommunityChatMessageRealtimeListener listener;

    private static CommunityTimelineEntryResponse message(Long communityId) {
        return new CommunityTimelineEntryResponse(
                44L, communityId, "CHAT", "member", "Bora jogar",
                Instant.parse("2026-10-23T09:00:00Z"));
    }

    @Test
    void removedMemberNeverReceivesEventFromStalePostingSnapshot() {
        var response = message(7L);
        when(recipientResolver.currentRecipients(7L))
                .thenReturn(Set.of("owner", "active", "joinedAfterPosting"));
        var event = new CommunityChatMessageCreatedEvent(
                response, Set.of("owner", "active", "removedMember"));

        listener.onCommunityMessage(event);

        verify(gateway).publishCommunityChatMessage(Set.of("owner", "active"), response);
        verifyNoMoreInteractions(gateway);
    }

    @Test
    void neverDeliversAcrossCommunitiesOrToRemovedUsers() {
        var response = message(9L);
        when(recipientResolver.currentRecipients(9L)).thenReturn(Set.of("differentCommunityMember"));

        listener.onCommunityMessage(new CommunityChatMessageCreatedEvent(
                response, Set.of("formerMember")));

        verify(gateway).publishCommunityChatMessage(Set.of(), response);
    }

    @Test
    void membershipLookupFailureFailsClosedWithoutSendingAnything() {
        when(recipientResolver.currentRecipients(7L))
                .thenThrow(new IllegalStateException("Database temporarily unavailable"));

        assertDoesNotThrow(() -> listener.onCommunityMessage(
                new CommunityChatMessageCreatedEvent(message(7L), Set.of("formerMember"))));
        verifyNoInteractions(gateway);
    }

    @Test
    void rejectsInvalidEventWithoutQueryingMembership() {
        listener.onCommunityMessage(new CommunityChatMessageCreatedEvent(null, Set.of("member")));
        listener.onCommunityMessage(new CommunityChatMessageCreatedEvent(message(7L), null));
        verifyNoInteractions(recipientResolver, gateway);
    }

    @Test
    void eventDeliveryRemainsAfterSuccessfulCommit() throws Exception {
        TransactionalEventListener annotation = CommunityChatMessageRealtimeListener.class
                .getMethod("onCommunityMessage", CommunityChatMessageCreatedEvent.class)
                .getAnnotation(TransactionalEventListener.class);
        assertNotNull(annotation);
        assertEquals(TransactionPhase.AFTER_COMMIT, annotation.phase());
    }
}
