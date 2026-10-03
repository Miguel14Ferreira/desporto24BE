package com.example.desporto24.model.community;

import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "community_join_request")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityJoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_profile_id", nullable = false)
    private Profile requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunityInviteStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
