package com.example.desporto24.model.community;

import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/** Community-scoped posts with optional media; chat remains text-only. */
@Entity
@Table(name = "community_timeline_entry", indexes = {
        @Index(name = "idx_community_timeline_kind_recent",
                columnList = "community_id,kind,created_at,id")
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityTimelineEntry {
    public enum Kind { POST, CHAT }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_profile_id", nullable = false)
    private Profile author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private Kind kind;

    @Column(nullable = false, length = 2000)
    private String body;

    @Column(name = "image_url", length = 1200)
    private String imageUrl;

    @Column(name = "image_storage_key", length = 800)
    private String imageStorageKey;

    @Column(name = "video_url", length = 1200)
    private String videoUrl;

    @Column(name = "video_storage_key", length = 800)
    private String videoStorageKey;

    // CHAT-only content is stored in the dedicated PRIVATE R2 bucket.
    // Never expose the storage key or reuse the public POST media fields.
    @Column(name = "attachment_key", length = 240)
    private String attachmentKey;
    @Column(name = "attachment_name", length = 160)
    private String attachmentName;
    @Column(name = "attachment_mime_type", length = 60)
    private String attachmentMimeType;
    @Column(name = "attachment_size")
    private Long attachmentSize;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Nullable snapshots survive original message deletion and pagination.
    @Column(name = "reply_to_message_id")
    private Long replyToMessageId;
    @Column(name = "reply_to_sender", length = 128)
    private String replyToSender;
    @Column(name = "reply_to_text", length = 2000)
    private String replyToText;
}
