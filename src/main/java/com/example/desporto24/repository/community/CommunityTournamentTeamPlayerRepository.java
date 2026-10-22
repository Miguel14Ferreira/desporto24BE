package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentTeamPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;

public interface CommunityTournamentTeamPlayerRepository
        extends JpaRepository<CommunityTournamentTeamPlayer, Long> {
    @EntityGraph(attributePaths = {"profile"})
    List<CommunityTournamentTeamPlayer> findByTournamentIdOrderByTeamIdAscIdAsc(Long tournamentId);

    long countByTeamId(Long teamId);

    boolean existsByTournamentIdAndProfileId(Long tournamentId, Long profileId);

    @EntityGraph(attributePaths = {"team", "profile"})
    Optional<CommunityTournamentTeamPlayer> findByTournamentIdAndProfileId(Long tournamentId, Long profileId);

    Optional<CommunityTournamentTeamPlayer> findByTeamIdAndProfileId(Long teamId, Long profileId);
}
