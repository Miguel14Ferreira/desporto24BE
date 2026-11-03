package com.example.desporto24.model.community;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.math.BigDecimal;
import java.time.Instant;

/** One observed collective time and the four runners in leg order. */
@Entity
@Table(name="community_tournament_relay_heat",
       uniqueConstraints=@UniqueConstraint(name="uk_tournament_relay_stage_heat",
           columnNames={"tournament_id","team_id","stage","heat_number"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityTournamentRelayHeat {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="tournament_id",nullable=false)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private CommunityTournament tournament;
    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="team_id",nullable=false)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private CommunityTournamentTeam team;
    @Column(name="stage",nullable=false,length=20)
    @Builder.Default private String stage="QUALIFICATION";
    @Column(name="decision_status",nullable=false,length=20)
    @Builder.Default private String decisionStatus="VALID";
    @Column(name="heat_number",nullable=false)
    private int heatNumber;
    @Column(name="elapsed_seconds",nullable=false,precision=11,scale=3)
    private BigDecimal elapsedSeconds;
    @Column(name="leg1_profile_id",nullable=false) private Long leg1ProfileId;
    @Column(name="leg2_profile_id",nullable=false) private Long leg2ProfileId;
    @Column(name="leg3_profile_id",nullable=false) private Long leg3ProfileId;
    @Column(name="leg4_profile_id",nullable=false) private Long leg4ProfileId;
    @Column(name="recorded_at",nullable=false) private Instant recordedAt;
}
