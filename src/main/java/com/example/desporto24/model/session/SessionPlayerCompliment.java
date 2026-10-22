package com.example.desporto24.model.session;

import com.example.desporto24.enumeration.PlayerComplimentType;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/** One private, editable, positive compliment per giver/recipient/session. */
@Entity
@Table(name = "session_player_compliment",
        uniqueConstraints = @UniqueConstraint(name = "uk_player_compliment_triplet",
                columnNames = {"session_id", "sender_profile_id", "recipient_profile_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionPlayerCompliment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_profile_id", nullable = false)
    private Profile sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_profile_id", nullable = false)
    private Profile recipient;

    @Enumerated(EnumType.STRING)
    @Column(name = "compliment_type", nullable = false, length = 32)
    private PlayerComplimentType type;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
