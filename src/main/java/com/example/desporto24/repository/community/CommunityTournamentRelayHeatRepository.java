package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentRelayHeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;

public interface CommunityTournamentRelayHeatRepository
        extends JpaRepository<CommunityTournamentRelayHeat,Long> {
    @EntityGraph(attributePaths={"team"})
    List<CommunityTournamentRelayHeat> findByTournamentIdOrderByHeatNumberAscTeamIdAsc(Long tournamentId);
    Optional<CommunityTournamentRelayHeat> findByTournamentIdAndTeamIdAndHeatNumber(
            Long tournamentId,Long teamId,int heatNumber);
}
