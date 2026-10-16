package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentMatch;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CommunityTournamentMatchRepository extends JpaRepository<CommunityTournamentMatch, Long> {

    List<CommunityTournamentMatch> findByTournamentIdOrderByRoundNumberAscIdAsc(Long tournamentId);

    boolean existsByTournamentId(Long tournamentId);

    long countByTournamentId(Long tournamentId);

    @EntityGraph(attributePaths = {"homeParticipant", "awayParticipant"})
    Slice<CommunityTournamentMatch> findByTournamentIdAndStatus(
            Long tournamentId, TournamentMatchStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"homeParticipant.profile", "awayParticipant.profile"})
    Slice<CommunityTournamentMatch> findByTournamentId(Long tournamentId, Pageable pageable);
}
