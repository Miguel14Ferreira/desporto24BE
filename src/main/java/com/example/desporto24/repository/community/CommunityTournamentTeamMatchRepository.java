package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentTeamMatch;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityTournamentTeamMatchRepository extends JpaRepository<CommunityTournamentTeamMatch, Long> {
    boolean existsByTournamentId(Long tournamentId);
    boolean existsByTournamentIdAndStatus(Long tournamentId, TournamentMatchStatus status);
    long countByTournamentId(Long tournamentId);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    Slice<CommunityTournamentTeamMatch> findByTournamentIdAndStatus(
            Long tournamentId, TournamentMatchStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    Slice<CommunityTournamentTeamMatch> findByTournamentId(Long tournamentId, Pageable pageable);

    boolean existsByTournamentIdAndGroupNumberIsNull(Long tournamentId);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    Slice<CommunityTournamentTeamMatch> findByTournamentIdAndGroupNumberIsNull(
            Long tournamentId, Pageable pageable);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    Slice<CommunityTournamentTeamMatch> findByTournamentIdAndGroupNumberIsNotNull(
            Long tournamentId, Pageable pageable);
}
