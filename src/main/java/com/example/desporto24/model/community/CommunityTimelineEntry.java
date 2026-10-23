package com.example.desporto24.model.community;

import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/** Text-only initial community feed and chat. Always scoped to one community. */
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
