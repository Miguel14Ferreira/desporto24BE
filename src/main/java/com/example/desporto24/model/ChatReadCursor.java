package com.example.desporto24.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chat_read_cursor", schema = "desporto24",
       uniqueConstraints = @UniqueConstraint(
           name = "uq_chat_read_cursor_reader_conversation",
           columnNames = {"profile_id", "conversation_kind", "conversation_key"}))
@Getter
@Setter
public class ChatReadCursor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "conversation_kind", nullable = false, length = 16)
    private String conversationKind;

    @Column(name = "conversation_key", nullable = false, length = 128)
    private String conversationKey;

    @Column(name = "last_read_message_id", nullable = false)
    private Long lastReadMessageId;
}
