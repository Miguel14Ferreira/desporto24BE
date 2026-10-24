package com.example.desporto24.model.social;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "social_story")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class SocialStory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Profile author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;
    @Column(length = 180)
    private String caption;
    @Column(name = "media_url", nullable = false, length = 1200)
    private String mediaUrl;
    @Column(name = "media_storage_key", nullable = false, length = 800)
    private String mediaStorageKey;
    @Column(name = "is_video", nullable = false)
    private boolean video;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
}
