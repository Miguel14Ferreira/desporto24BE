package com.example.desporto24.controllers;

import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.chat.ChatMessageRequest;
import com.example.desporto24.service.interfaces.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ChatController controller;

    @Test
    void legacyChatHistoryCannotChooseUnrelatedParticipants() {
        when(authentication.getName()).thenReturn("miguel");

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.security.access.AccessDeniedException.class,
                () -> controller.getChat("ana", "joao", authentication)
        );
    }

    @Test
    void legacyChatSendCannotSpoofSender() {
        when(authentication.getName()).thenReturn("miguel");
        ChatMessageRequest request = new ChatMessageRequest("Olá");
        Chat saved = new Chat();
        saved.setUsername1("miguel");
        saved.setUsername2("ana");
        saved.setText("Olá");
        when(chatService.sendMessage(org.mockito.ArgumentMatchers.any(Chat.class)))
                .thenReturn(saved);

        controller.sendChatMessage("miguel", "ana", request, authentication);

        ArgumentCaptor<Chat> captor = ArgumentCaptor.forClass(Chat.class);
        verify(chatService).sendMessage(captor.capture());
        assertEquals("miguel", captor.getValue().getUsername1());
        assertEquals("ana", captor.getValue().getUsername2());
        assertEquals("Olá", captor.getValue().getText());
    }

    @Test
    void authenticatedChatHistoryUsesLoggedInIdentity() {
        when(authentication.getName()).thenReturn("miguel");
        when(chatService.findChatMessages("miguel", "ana")).thenReturn(List.of());

        var response = controller.getMyChat("ana", authentication);

        assertEquals(200, response.getStatusCode().value());
        verify(chatService).findChatMessages("miguel", "ana");
    }

    @Test
    void authenticatedSendCannotSpoofSender() {
        when(authentication.getName()).thenReturn("miguel");
        Chat saved = new Chat();
        saved.setUsername1("miguel");
        saved.setUsername2("ana");
        saved.setText("Olá!");
        when(chatService.sendMessage(org.mockito.ArgumentMatchers.any(Chat.class)))
                .thenReturn(saved);

        var response = controller.sendMyChatMessage(
                "ana",
                new ChatMessageRequest("  Olá!  "),
                authentication
        );

        assertEquals(200, response.getStatusCode().value());

        ArgumentCaptor<Chat> captor = ArgumentCaptor.forClass(Chat.class);
        verify(chatService).sendMessage(captor.capture());
        assertEquals("miguel", captor.getValue().getUsername1());
        assertEquals("ana", captor.getValue().getUsername2());
        assertEquals("Olá!", captor.getValue().getText());
    }

    @Test
    void authenticatedChatInfoDoesNotExposeProfiles() {
        when(authentication.getName()).thenReturn("miguel");
        Friend friend = new Friend();
        friend.setChatBlocked(true);
        friend.setWhoBlocked("ana");
        when(chatService.getChatInfo("miguel", "ana")).thenReturn(friend);

        var response = controller.getMyChatInfo("ana", authentication);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(true, response.getBody().chatBlocked());
        assertEquals("ana", response.getBody().whoBlocked());
    }
}
