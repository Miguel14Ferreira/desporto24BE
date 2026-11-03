package com.example.desporto24.model.community;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.Instant;
/** Append-only jury action history. Never delete when revising a decision. */
@Entity @Table(name="community_tournament_relay_decision")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityTournamentRelayDecision {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="tournament_id",nullable=false)
    @OnDelete(action=OnDeleteAction.CASCADE) private CommunityTournament tournament;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="heat_id",nullable=false)
    @OnDelete(action=OnDeleteAction.CASCADE) private CommunityTournamentRelayHeat heat;
    @Column(name="previous_status",nullable=false,length=20) private String previousStatus;
    @Column(name="new_status",nullable=false,length=20) private String newStatus;
    @Column(name="reason",nullable=false,length=500) private String reason;
    @Column(name="actor_profile_id",nullable=false) private Long actorProfileId;
    @Column(name="decided_at",nullable=false) private Instant decidedAt;
}
