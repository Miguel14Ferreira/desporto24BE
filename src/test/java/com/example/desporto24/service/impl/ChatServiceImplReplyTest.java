package com.example.desporto24.service.impl;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.UserBlockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplReplyTest {
    @Mock FriendRepository friends;
    @Mock ChatRepository chats;
    @Mock ProfileRepository profiles;
    @Mock NotificationsRepository notifications;
    @Mock ApplicationEventPublisher events;
    @Mock UserBlockService blockService;
    @InjectMocks ChatServiceImpl service;

    @BeforeEach
    void authorizedFriendship() {
        Profile ana = new Profile();
        ana.setId(1L);
        ana.setUsername("ana");
        Profile bea = new Profile();
        bea.setId(2L);
        bea.setUsername("bea");
        when(profiles.findUserByUsername("ana")).thenReturn(Optional.of(ana));
        when(profiles.findUserByUsername("bea")).thenReturn(Optional.of(bea));
        when(friends.findConnection(1L, 2L)).thenReturn(Optional.of(new Friend()));
    }

    private Chat message(String sender, String recipient, String text) {
        Chat chat = new Chat();
        chat.setUsername1(sender);
        chat.setUsername2(recipient);
        chat.setText(text);
        return chat;
    }

    @Test
    void replyToSameConversationCreatesServerOwnedQuoteSnapshot() {
        Chat original = message("bea", "ana", "Vamos jogar?");
        original.setId(40L);
        when(chats.findById(40L)).thenReturn(Optional.of(original));
        Chat reply = message("ana", "bea", "Sim, claro");
        reply.setReplyToMessageId(40L);
        // Malicious/stale client snapshot must never override server-owned data.
        reply.setReplyToSender("another-user");
        reply.setReplyToText("spoofed");
        when(chats.save(any(Chat.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Chat saved = service.sendMessage(reply);
        assertEquals(40L, saved.getReplyToMessageId());
        assertEquals("bea", saved.getReplyToSender());
        assertEquals("Vamos jogar?", saved.getReplyToText());
        verify(chats).save(reply);
        verify(events).publishEvent(any(com.example.desporto24.websocket.ChatMessageCreatedEvent.class));
    }

    @Test
    void ordinaryMessagesStayCompatibleWithoutAQuote() {
        Chat normal = message("ana", "bea", "Olá");
        when(chats.save(any(Chat.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Chat result = service.sendMessage(normal);
        assertNull(result.getReplyToMessageId());
        assertNull(result.getReplyToText());
        verify(chats, never()).findById(any());
    }

    @Test
    void rejectsAnotherConversationWithoutSavingOrDisclosingTheParent() {
        Chat unrelated = message("bea", "carla", "private");
        unrelated.setId(55L);
        when(chats.findById(55L)).thenReturn(Optional.of(unrelated));
        Chat reply = message("ana", "bea", "wrong");
        reply.setReplyToMessageId(55L);
        var thrown = assertThrows(ResponseStatusException.class, () -> service.sendMessage(reply));
        assertEquals(400, thrown.getStatusCode().value());
        verify(chats, never()).save(any());
        verify(events, never()).publishEvent(any());
        verify(notifications, never()).save(any());
    }

    @Test
    void missingAndInvalidParentIdsDoNotCreateMessages() {
        Chat missing = message("ana", "bea", "no parent");
        missing.setReplyToMessageId(99999L);
        when(chats.findById(99999L)).thenReturn(Optional.empty());
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.sendMessage(missing)).getStatusCode().value());
        Chat invalid = message("ana", "bea", "invalid parent");
        invalid.setReplyToMessageId(-1L);
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.sendMessage(invalid)).getStatusCode().value());
        verify(chats, never()).save(any());
        verify(events, never()).publishEvent(any());
    }
}
