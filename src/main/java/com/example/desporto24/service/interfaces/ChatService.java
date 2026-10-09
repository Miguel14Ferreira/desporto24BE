package com.example.desporto24.service.interfaces;

import com.example.desporto24.model.Chat;
import org.springframework.data.domain.Page;
import com.example.desporto24.model.Friend;

import java.util.List;

public interface ChatService {
    Chat sendMessage(Chat chat);
    List<Chat> findChatMessages(String sender, String recipient);
    Page<Chat> findChatMessagesPage(String sender, String recipient, int page, int size);
    Friend getChatInfo(String sender, String recipient);
}