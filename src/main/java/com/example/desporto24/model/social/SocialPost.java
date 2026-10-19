package com.example.desporto24.model.social;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "social_post")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Profile author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "image_url", length = 1200)
    private String imageUrl;

    @Column(name = "image_storage_key", length = 800)
    private String imageStorageKey;

    @Column(name = "video_url", length = 1200)
    private String videoUrl;

    @Column(name = "video_storage_key", length = 800)
    private String videoStorageKey;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
