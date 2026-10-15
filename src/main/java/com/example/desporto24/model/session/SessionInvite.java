package com.example.desporto24.model.session;

import com.example.desporto24.enumeration.SessionInviteStatus;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "session_invite", schema = "desporto24")
@Getter
@Setter
@NoArgsConstructor
public class SessionInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inviter_profile_id", nullable = false)
    private Profile inviter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invitee_profile_id", nullable = false)
    private Profile invitee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private SessionInviteStatus status = SessionInviteStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "responded_at")
    private Instant respondedAt;
}
