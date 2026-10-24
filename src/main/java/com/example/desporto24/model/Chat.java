package com.example.desporto24.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Data
@RequiredArgsConstructor
public class Chat {
    @SequenceGenerator( name = "chat_sequence",
            sequenceName = "chat_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "chat_sequence")
    private Long id;
    private String username1;
    private String username2;
    // Match the authenticated message request and the chat.text Liquibase column.
    @Column(name = "text", length = 2000)
    private String text;
    private String dataDisplay;
    private Date date;
    // Server-owned immutable quote snapshot. References never cross conversations.
    @Column(name = "reply_to_message_id")
    private Long replyToMessageId;
    @Column(name = "reply_to_sender", length = 128)
    private String replyToSender;
    @Column(name = "reply_to_text", length = 2000)
    private String replyToText;
    // Storage keys are secret server-side metadata, not part of message JSON/WebSocket.
    @com.fasterxml.jackson.annotation.JsonIgnore
    @Column(name = "attachment_key", length = 240)
    private String attachmentKey;
    @Column(name = "attachment_name", length = 160)
    private String attachmentName;
    @Column(name = "attachment_mime_type", length = 60)
    private String attachmentMimeType;
    @Column(name = "attachment_size")
    private Long attachmentSize;

    public Chat(String username1, String username2, String text, String dataDisplay,Date date) {
        this.username1 = username1;
        this.username2 = username2;
        this.text = text;
        this.dataDisplay = dataDisplay;
        this.date = date;
    }
}
