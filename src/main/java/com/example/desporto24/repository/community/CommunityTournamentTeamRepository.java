package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommunityTournamentTeamRepository extends JpaRepository<CommunityTournamentTeam, Long> {
    List<CommunityTournamentTeam> findByTournamentIdOrderByIdAsc(Long tournamentId);
    boolean existsByTournamentIdAndNormalizedName(Long tournamentId, String normalizedName);
    long countByTournamentId(Long tournamentId);
}
