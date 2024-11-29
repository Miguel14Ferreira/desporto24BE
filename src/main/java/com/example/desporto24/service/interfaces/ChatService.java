package com.example.desporto24.service.interfaces;

import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Friend;

import java.util.List;

public interface ChatService {
    Chat sendMessage(Chat chat);
    List<Chat> findChatMessages(String sender, String recipient);
    Friend getChatInfo(String sender, String recipient);
}