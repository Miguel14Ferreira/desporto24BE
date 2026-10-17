package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentTeamMatch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityTournamentTeamMatchRepository extends JpaRepository<CommunityTournamentTeamMatch, Long> {
    boolean existsByTournamentId(Long tournamentId);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    Slice<CommunityTournamentTeamMatch> findByTournamentId(Long tournamentId, Pageable pageable);
}
