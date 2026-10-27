package com.example.desporto24.model.session;

import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.CommunityPlayer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

/** A real-world person requesting to attend, not a fictitious Jynlo profile. */
@Entity
@Table(name="session_guest_request",uniqueConstraints={
    @UniqueConstraint(name="uq_session_guest_name",columnNames={"session_id","normalized_name"}),
    @UniqueConstraint(name="uq_session_guest_receipt",columnNames={"receipt_hash"})
})
@Getter @Setter
public class SessionGuestRequest {
    public enum State { PENDING, APPROVED, REJECTED, LINKED, CANCELLED }
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="session_id",nullable=false)
    private Session session;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="community_player_id")
    private CommunityPlayer communityPlayer;
    @Column(name="display_name",nullable=false,length=120)
    private String displayName;
    @Column(name="normalized_name",nullable=false,length=120)
    private String normalizedName;
    @Column(name="receipt_hash",nullable=false,length=64)
    private String receiptHash;
    @Column(name="note",length=160)
    private String note;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=16)
    private State status=State.PENDING;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="linked_profile_id")
    private com.example.desporto24.model.Profile linkedProfile;
    @Column(name="team_code",length=1)
    private String teamCode;
    @Column(name="created_at",nullable=false)
    private Instant createdAt;
    @Column(name="reviewed_at")
    private Instant reviewedAt;
}
