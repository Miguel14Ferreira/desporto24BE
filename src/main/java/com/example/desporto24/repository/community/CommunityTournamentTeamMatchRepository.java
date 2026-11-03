package com.example.desporto24.repository.community;

import com.example.desporto24.model.community.CommunityTournamentTeamMatch;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityTournamentTeamMatchRepository extends JpaRepository<CommunityTournamentTeamMatch, Long> {
    boolean existsByTournamentId(Long tournamentId);
    boolean existsByTournamentIdAndStatus(Long tournamentId, TournamentMatchStatus status);
    long countByTournamentId(Long tournamentId);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    Slice<CommunityTournamentTeamMatch> findByTournamentIdAndStatus(
            Long tournamentId, TournamentMatchStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    Slice<CommunityTournamentTeamMatch> findByTournamentId(Long tournamentId, Pageable pageable);

    /** Fetch only this team's fixtures. A tournament can contain almost 1,000
     * matches, so the dedicated profile must not scan all tournament pages. */
    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    @Query("select match from CommunityTournamentTeamMatch match " +
            "where match.tournament.id = :tournamentId " +
            "and (match.homeTeam.id = :teamId or match.awayTeam.id = :teamId)")
    Slice<CommunityTournamentTeamMatch> findByTournamentAndTeam(
            @Param("tournamentId") Long tournamentId,
            @Param("teamId") Long teamId, Pageable pageable);

    boolean existsByTournamentIdAndGroupNumberIsNull(Long tournamentId);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    Slice<CommunityTournamentTeamMatch> findByTournamentIdAndGroupNumberIsNull(
            Long tournamentId, Pageable pageable);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    Slice<CommunityTournamentTeamMatch> findByTournamentIdAndGroupNumberIsNotNull(
            Long tournamentId, Pageable pageable);
}
