package com.example.desporto24.service.impl;

import com.example.desporto24.Chat.ChatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.exception.ChatBlockedException;
import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.UserBlockService;
import com.example.desporto24.service.interfaces.ChatService;
import com.example.desporto24.websocket.ChatMessageCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.example.desporto24.constant.UserImplConstant.CHAT_BLOCKED;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final FriendRepository friendRepository;
    private final ChatRepository chatRepository;
    private final ProfileRepository profileRepository;
    private final NotificationsRepository notificationsRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserBlockService userBlockService;

    @Override
    @Transactional
    public Chat sendMessage(Chat chat) {
        Friend friend = requireFriendship(chat.getUsername1(), chat.getUsername2());
        if (friend.isChatBlocked()) {
            throw new ChatBlockedException(CHAT_BLOCKED);
        }

        Chat saved = saveChatMessage(chat, new Date());
        createChatNotification(saved);
        eventPublisher.publishEvent(new ChatMessageCreatedEvent(saved));
        return saved;
    }

    private Chat saveChatMessage(Chat chat, Date date) {
        chat.setDate(date);
        chatRepository.save(chat);
        return chat;
    }

    private void createChatNotification(Chat chat) {
        Notifications notification = new Notifications();
        notification.setSubject("Nova mensagem de " + chat.getUsername1());
        notification.setMessage(chat.getText());
        notification.setGreeting("");
        notification.setSignature("");
        notification.setCreatedAt(Instant.now().toString());
        notification.setFriendRequest(false);
        notification.setUpdateProfile(false);
        notification.setSessionInvite(false);
        notification.setNormalMessage(true);
        notification.setToken(null);
        notification.setUsername(chat.getUsername2());
        notification.setRelatedUsername(chat.getUsername1());
        notification.setType(NotificationType.CHAT_MESSAGE);
        notification.setSessionId(null);
        notification.setRead(false);
        notificationsRepository.save(notification);
    }

    @Override
    public List<Chat> findChatMessages(String sender, String recipient) {
        requireFriendship(sender, recipient);

        return chatRepository.findConversation(sender, recipient);
    }

    @Override
    public Page<Chat> findChatMessagesPage(String sender, String recipient, int page, int size) {
        requireFriendship(sender, recipient);
        return chatRepository.findConversationPage(sender, recipient,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date").and(Sort.by(Sort.Direction.DESC, "id"))));
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

        userBlockService.ensureInteractionAllowed(first, second);

        return friendRepository.findConnection(first.getId(), second.getId())
                .orElseThrow(() -> new AccessDeniedException("Users are not friends"));
    }
}
