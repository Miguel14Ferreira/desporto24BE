package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CommunityTournamentMatchRepository extends JpaRepository<CommunityTournamentMatch, Long> {

    List<CommunityTournamentMatch> findByTournamentIdOrderByRoundNumberAscIdAsc(Long tournamentId);

    boolean existsByTournamentId(Long tournamentId);

    @EntityGraph(attributePaths = {"homeParticipant.profile", "awayParticipant.profile"})
    Slice<CommunityTournamentMatch> findByTournamentId(Long tournamentId, Pageable pageable);
}
