package com.example.desporto24.model.community;

import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "community")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(length = 100)
    private String sport;

    @Column(name = "photo_url", length = 1000)
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunityVisibility visibility;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_profile_id", nullable = false)
    private Profile owner;

    @Column(name = "rankings_enabled", nullable = false)
    private boolean rankingsEnabled;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
