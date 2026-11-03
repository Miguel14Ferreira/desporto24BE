package com.example.desporto24.repository.community;
import com.example.desporto24.model.community.CommunityTournamentRelayDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CommunityTournamentRelayDecisionRepository extends JpaRepository<CommunityTournamentRelayDecision,Long> {
    List<CommunityTournamentRelayDecision> findByTournamentIdOrderByIdAsc(Long tournamentId);
}
