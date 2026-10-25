package com.example.desporto24.model.session;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "session_message")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_profile_id", nullable = false)
    private Profile senderProfile;

    @Column(nullable = false, length = 2000)
    private String body;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Immutable server-owned quote; parent must belong to this session.
    @Column(name = "reply_to_message_id")
    private Long replyToMessageId;
    @Column(name = "reply_to_sender", length = 128)
    private String replyToSender;
    @Column(name = "reply_to_text", length = 2000)
    private String replyToText;

    @Column(name = "attachment_key", length = 240)
    private String attachmentKey;
    @Column(name = "attachment_name", length = 160)
    private String attachmentName;
    @Column(name = "attachment_mime_type", length = 60)
    private String attachmentMimeType;
    @Column(name = "attachment_size")
    private Long attachmentSize;
}
