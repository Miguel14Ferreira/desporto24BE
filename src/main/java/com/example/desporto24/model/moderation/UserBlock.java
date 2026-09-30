package com.example.desporto24.model.moderation;

import com.example.desporto24.model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "user_block",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_block_pair",
                columnNames = {"blocker_profile_id", "blocked_profile_id"}
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blocker_profile_id", nullable = false)
    private Profile blocker;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blocked_profile_id", nullable = false)
    private Profile blocked;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
