package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "community_season")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunitySeason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "champion_finalized", nullable = false)
    private boolean championFinalized;

    @Column(name = "champion_username", length = 100)
    private String championUsername;

    @Column(name = "champion_display_name", length = 255)
    private String championDisplayName;

    @Column(name = "champion_points")
    private Integer championPoints;
}
