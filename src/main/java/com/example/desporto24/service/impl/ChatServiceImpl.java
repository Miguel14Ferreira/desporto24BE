package com.example.desporto24.service.impl;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.exception.ChatBlockedException;
import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.service.interfaces.ChatService;
import com.example.desporto24.service.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.example.desporto24.constant.UserImplConstant.CHAT_BLOCKED;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ProfileService profileService;
    private final FriendRepository friendRepository;
    private final ChatRepository chatRepository;

    @Override
    public Chat sendMessage(Chat chat) {
        ProfileDTO sender = profileService.findUserByUsername(chat.getUsername1());
        ProfileDTO recipient = profileService.findUserByUsername(chat.getUsername2());

        Friend friend = friendRepository.findByProfile1AndProfile2(sender, recipient);
        if (friend.isChatBlocked()) {
            throw new ChatBlockedException(CHAT_BLOCKED);
        }

        return saveChatMessage(chat, new Date());
    }

    private Chat saveChatMessage(Chat chat, Date date) {
        chat.setDate(date);
        chatRepository.save(chat);
        return chat;
    }

    @Override
    public List<Chat> findChatMessages(String sender, String recipient) {
        List<Chat> chats = new ArrayList<>();
        chats.addAll(chatRepository.findByUsername1AndUsername2(sender, recipient));
        chats.addAll(chatRepository.findByUsername1AndUsername2(recipient, sender));
        chats.sort(Comparator.comparing(Chat::getDate));
        return chats;
    }

    @Override
    public Friend getChatInfo(String firstUsername, String secondUsername) {
        ProfileDTO profile1 = profileService.findUserByUsername(firstUsername);
        ProfileDTO profile2 = profileService.findUserByUsername(secondUsername);

        return friendRepository.findByProfile1AndProfile2(profile1, profile2);
    }
}
