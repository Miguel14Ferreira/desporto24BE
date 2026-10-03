package com.example.desporto24.service;

import com.example.desporto24.email.EmailSender;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityInviteEmailListenerTest {

    @Test
    void sendsBilingualInvitationToRecipientWithSafeNamesAndCommunityLink() {
        EmailSender sender = mock(EmailSender.class);
        CommunityInviteEmailListener listener = new CommunityInviteEmailListener(
                sender, "https://d24sports.com/");
        listener.onCommunityInviteCreated(new CommunityInviteCreatedEvent(
                7L, "friend@example.test", "owner<script>", "CNG & Friends"));
        ArgumentCaptor<String> html = ArgumentCaptor.forClass(String.class);
        verify(sender).send(eq("friend@example.test"), html.capture());
        String body = html.getValue();
        assertTrue(body.contains("owner&lt;script&gt;"));
        assertTrue(body.contains("CNG &amp; Friends"));
        assertFalse(body.contains("<script>"));
        assertTrue(body.contains("href=\"https://d24sports.com/menu/communities\""));
        assertTrue(body.contains("Ver convite / View invitation"));
        assertTrue(body.contains("lang=\"en\""));
    }

    @Test
    void emailFailureDoesNotTurnSavedInvitationIntoFailedRequest() {
        EmailSender sender = mock(EmailSender.class);
        doThrow(new IllegalStateException("Email unavailable")).when(sender).send(anyString(), anyString());
        CommunityInviteEmailListener listener = new CommunityInviteEmailListener(
                sender, "https://d24sports.com");
        assertDoesNotThrow(() -> listener.onCommunityInviteCreated(
                new CommunityInviteCreatedEvent(7L, "friend@example.test", "owner", "CNG")));
    }

    @Test
    void skipsRecipientWithoutEmail() {
        EmailSender sender = mock(EmailSender.class);
        CommunityInviteEmailListener listener = new CommunityInviteEmailListener(
                sender, "https://d24sports.com");
        listener.onCommunityInviteCreated(new CommunityInviteCreatedEvent(7L, null, "owner", "CNG"));
        listener.onCommunityInviteCreated(new CommunityInviteCreatedEvent(8L, " ", "owner", "CNG"));
        verifyNoInteractions(sender);
    }
}
