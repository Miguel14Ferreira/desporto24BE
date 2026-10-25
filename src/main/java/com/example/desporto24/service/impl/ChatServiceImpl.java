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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.example.desporto24.constant.UserImplConstant.CHAT_BLOCKED;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private com.example.desporto24.service.NotificationPreferencesService preferenceService;

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

        attachVerifiedReply(chat);
        Chat saved = saveChatMessage(chat, new Date());
        createChatNotification(saved);
        eventPublisher.publishEvent(new ChatMessageCreatedEvent(saved));
        return saved;
    }


    /**
     * Resolve quoted messages ONLY after friendship/block authorization and
     * ONLY within the authenticated direct conversation. Never trust quoted
     * author or text from a client. Persist a snapshot so history pagination
     * still renders the quote when the original message is outside the page.
     */
    private void attachVerifiedReply(Chat chat) {
        Long parentId = chat.getReplyToMessageId();
        if (parentId == null) return;
        if (parentId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reply message");
        }
        Chat parent = chatRepository.findById(parentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid reply message"));
        boolean forward = chat.getUsername1().equals(parent.getUsername1())
                && chat.getUsername2().equals(parent.getUsername2());
        boolean reverse = chat.getUsername1().equals(parent.getUsername2())
                && chat.getUsername2().equals(parent.getUsername1());
        if (!forward && !reverse) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reply message");
        }
        chat.setReplyToSender(parent.getUsername1());
        chat.setReplyToText(parent.getText());
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
        if (preferenceService == null ||
                preferenceService.allowsInApp(notification.getUsername(), notification)) {
            notificationsRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public Chat editMessage(String actor, String peer, Long messageId, String text) {
        Chat message = ownedMessage(actor, peer, messageId);
        String normalized = com.example.desporto24.service.ChatMessageMutationPolicy.editText(
                text, message.getDate() == null ? null : message.getDate().toInstant(),
                Instant.now(), message.getDeletedAt(), message.getAttachmentKey() != null);
        message.setText(normalized);
        message.setEditedAt(Instant.now());
        chatRepository.saveAndFlush(message);
        eventPublisher.publishEvent(new ChatMessageCreatedEvent(message));
        return message;
    }

    @Override
    @Transactional
    public Chat deleteMessage(String actor, String peer, Long messageId) {
        Chat message = ownedMessage(actor, peer, messageId);
        com.example.desporto24.service.ChatMessageMutationPolicy.requireNotDeleted(message.getDeletedAt());
        message.setText("");
        message.setDeletedAt(Instant.now());
        chatRepository.saveAndFlush(message);
        // Historical reply snapshots may quote this text. Redact them too.
        chatRepository.redactRepliesToDeleted(message.getId(), "[message deleted]");
        eventPublisher.publishEvent(new ChatMessageCreatedEvent(message));
        return message;
    }

    private Chat ownedMessage(String actor, String peer, Long messageId) {
        Friend friend = requireFriendship(actor, peer);
        if (friend.isChatBlocked()) throw new ChatBlockedException(CHAT_BLOCKED);
        if (messageId == null || messageId <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
        }
        Chat message = chatRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));
        if (!actor.equalsIgnoreCase(message.getUsername1())
                || !peer.equalsIgnoreCase(message.getUsername2())) {
            throw new AccessDeniedException("Only the message author may change this message");
        }
        return message;
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
