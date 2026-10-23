package com.example.desporto24.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** One chosen emoji per authenticated user and persisted message. */
@Entity
@Table(name = "chat_message_reaction", schema = "desporto24",
        uniqueConstraints = @UniqueConstraint(name = "uq_chat_reaction_actor_message",
                columnNames = {"profile_id", "conversation_kind", "message_id"}),
        indexes = @Index(name = "idx_chat_reaction_lookup",
                columnList = "conversation_kind,message_id"))
@Getter @Setter
public class ChatMessageReaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "conversation_kind", nullable = false, length = 16)
    private String conversationKind;

    @Column(name = "message_id", nullable = false)
    private Long messageId;

    @Column(nullable = false, length = 16)
    private String emoji;
}
