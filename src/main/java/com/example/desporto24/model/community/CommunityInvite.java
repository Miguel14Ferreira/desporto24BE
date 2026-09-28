package com.example.desporto24.model.community;

import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "community_invite")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_profile_id", nullable = false)
    private Profile invitedProfile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by_profile_id", nullable = false)
    private Profile invitedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunityInviteStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
