package com.example.desporto24.repository;

import com.example.desporto24.model.ConversationNotificationMute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConversationNotificationMuteRepository extends JpaRepository<ConversationNotificationMute, Long> {
    List<ConversationNotificationMute> findByUsername(String username);
    Optional<ConversationNotificationMute> findByUsernameAndConversationKindAndConversationKey(
        String username, String conversationKind, String conversationKey);
}
