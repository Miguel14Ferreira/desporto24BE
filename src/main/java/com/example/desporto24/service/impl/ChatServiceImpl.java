package com.example.desporto24.service.impl;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.exception.ChatBlockedException;
import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.interfaces.ChatService;
import com.example.desporto24.service.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
    private final ProfileRepository profileRepository;

    @Override
    public Chat sendMessage(Chat chat) {
        Friend friend = requireFriendship(chat.getUsername1(), chat.getUsername2());
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
        requireFriendship(sender, recipient);

        List<Chat> chats = new ArrayList<>();
        chats.addAll(chatRepository.findByUsername1AndUsername2(sender, recipient));
        chats.addAll(chatRepository.findByUsername1AndUsername2(recipient, sender));
        chats.sort(Comparator.comparing(Chat::getDate));
        return chats;
    }

    @Override
    public Friend getChatInfo(String firstUsername, String secondUsername) {
        return requireFriendship(firstUsername, secondUsername);
    }

    private Friend requireFriendship(String firstUsername, String secondUsername) {
        Profile first = profileRepository.findUserByUsername(firstUsername)
                .orElseThrow(() -> new AccessDeniedException("Friendship not found"));
        Profile second = profileRepository.findUserByUsername(secondUsername)
                .orElseThrow(() -> new AccessDeniedException("Friendship not found"));

        return friendRepository.findByProfile1OrProfile2(first, first).stream()
                .filter(friend ->
                        (friend.getProfile1().getId().equals(first.getId())
                                && friend.getProfile2().getId().equals(second.getId()))
                        || (friend.getProfile1().getId().equals(second.getId())
                                && friend.getProfile2().getId().equals(first.getId())))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Users are not friends"));
    }
}
