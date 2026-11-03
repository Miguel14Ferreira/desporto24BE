package com.example.desporto24.model.community;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="community_tournament_relay_finalist",uniqueConstraints=@UniqueConstraint(
        name="uk_tournament_relay_finalist",columnNames={"tournament_id","team_id"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class CommunityTournamentRelayFinalist {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="tournament_id",nullable=false)
    @OnDelete(action=OnDeleteAction.CASCADE) private CommunityTournament tournament;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="team_id",nullable=false)
    @OnDelete(action=OnDeleteAction.CASCADE) private CommunityTournamentTeam team;
    @Column(name="seed",nullable=false) private int seed;
    @Column(name="qualifying_seconds",nullable=false,precision=11,scale=3)
    private BigDecimal qualifyingSeconds;
    @Column(name="published_at",nullable=false) private Instant publishedAt;
}
