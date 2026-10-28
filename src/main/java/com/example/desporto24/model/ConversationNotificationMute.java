package com.example.desporto24.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "conversation_notification_mute", schema = "desporto24",
       uniqueConstraints = @UniqueConstraint(name = "uq_conversation_notification_mute",
           columnNames = {"username", "conversation_kind", "conversation_key"}))
@Getter @Setter
public class ConversationNotificationMute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String username;

    @Column(name = "conversation_kind", nullable = false, length = 16)
    private String conversationKind;

    @Column(name = "conversation_key", nullable = false, length = 128)
    private String conversationKey;
}
