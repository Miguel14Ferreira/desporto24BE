package com.example.desporto24.service;

import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentTeamTieBreakPriority;
import com.example.desporto24.model.community.CommunityTournamentTeam;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamStandingRowResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentTeamStandingsResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Recalculate exclusively from real team results; never touch community statistics. */
@Service
@RequiredArgsConstructor
public class CommunityTournamentTeamStandingsService {
    private static final int MAX_TEAMS = 32;
    private static final int MAX_MATCHES = MAX_TEAMS * (MAX_TEAMS - 1);

    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityTournamentTeamRepository teams;
    private final CommunityTournamentTeamMatchRepository matches;

    @Transactional(readOnly = true)
    public CommunityTournamentTeamStandingsResponse standings(
            Long communityId, Long tournamentId, String username) {
        var actor = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        if (!members.existsByCommunityIdAndProfileId(communityId, actor.getId())) {
            throw new AccessDeniedException("Não pertences a esta comunidade");
        }
        var tournament = tournaments.findById(tournamentId)
                .orElseThrow(this::notFound);
        if (!tournament.getCommunity().getId().equals(communityId)) throw notFound();
        if (tournament.getFormat() != TournamentFormat.LEAGUE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "As eliminatórias usam o quadro de confrontos, não uma classificação de liga");
        }
        if (tournament.getEntryType() != TournamentEntryType.TEAM) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esta classificação é exclusiva de torneios por equipas");
        }

        List<CommunityTournamentTeam> entrants = teams.findByTournamentIdOrderByIdAsc(tournamentId);
        if (entrants.size() > MAX_TEAMS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Demasiadas equipas na liga");
        }
        List<Row> rows = new ArrayList<>();
        Map<Long, Row> byTeamId = new HashMap<>();
        for (var team : entrants) {
            Row row = new Row(team);
            rows.add(row);
            byTeamId.put(team.getId(), row);
        }

        var completed = matches.findByTournamentIdAndStatus(tournamentId,
                TournamentMatchStatus.COMPLETED,
                PageRequest.of(0, MAX_MATCHES + 1, Sort.by("id")));
        if (completed.hasNext() || completed.getNumberOfElements() > MAX_MATCHES) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Demasiados confrontos na liga");
        }
        for (var match : completed.getContent()) {
            Row home = byTeamId.get(match.getHomeTeam().getId());
            Row away = byTeamId.get(match.getAwayTeam().getId());
            Integer homeScore = match.getHomeScore();
            Integer awayScore = match.getAwayScore();
            if (home == null || away == null || home == away
                    || homeScore == null || awayScore == null
                    || homeScore < 0 || awayScore < 0 || homeScore > 999 || awayScore > 999) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Confronto concluído com dados inconsistentes");
            }
            home.played++;
            away.played++;
            home.goalsFor += homeScore;
            home.goalsAgainst += awayScore;
            away.goalsFor += awayScore;
            away.goalsAgainst += homeScore;
            if (homeScore > awayScore) {
                home.won++;
                away.lost++;
                home.points += tournament.getWinPoints();
                away.points += tournament.getLossPoints();
            } else if (homeScore < awayScore) {
                away.won++;
                home.lost++;
                away.points += tournament.getWinPoints();
                home.points += tournament.getLossPoints();
            } else {
                home.drawn++;
                away.drawn++;
                home.points += tournament.getDrawPoints();
                away.points += tournament.getDrawPoints();
            }
        }

        // Points always come first. Configure only the order of sporting tie-breakers.
        // Team ID is a stable DISPLAY ordering, never a sporting tie-break or champion decision.
        Comparator<Row> difference = Comparator.comparingInt(Row::goalDifference).reversed();
        Comparator<Row> scored = Comparator.comparingInt((Row row) -> row.goalsFor).reversed();
        Comparator<Row> wins = Comparator.comparingInt((Row row) -> row.won).reversed();
        TournamentTeamTieBreakPriority priority = tournament.getTeamTieBreakPriority() == null
                ? TournamentTeamTieBreakPriority.GOAL_DIFFERENCE_FIRST
                : tournament.getTeamTieBreakPriority();
        Comparator<Row> sporting = switch (priority) {
            case GOALS_SCORED_FIRST -> scored.thenComparing(difference).thenComparing(wins);
            case WINS_FIRST -> wins.thenComparing(difference).thenComparing(scored);
            case GOAL_DIFFERENCE_FIRST -> difference.thenComparing(scored).thenComparing(wins);
        };
        rows.sort(Comparator.comparingLong((Row row) -> row.points).reversed()
                .thenComparing(sporting)
                .thenComparing(row -> row.teamId));
        List<CommunityTournamentTeamStandingRowResponse> table = new ArrayList<>();
        int position = 1;
        for (Row row : rows) {
            table.add(new CommunityTournamentTeamStandingRowResponse(position++, row.teamId,
                    row.teamName, row.played, row.won, row.drawn, row.lost,
                    row.goalsFor, row.goalsAgainst, row.goalDifference(), row.points));
        }

        return new CommunityTournamentTeamStandingsResponse(tournamentId, tournament.getStatus(),
                matches.countByTournamentId(tournamentId), completed.getNumberOfElements(),
                tournament.getWinPoints(), tournament.getDrawPoints(), tournament.getLossPoints(),
                List.copyOf(table));
    }

    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado");
    }

    private static final class Row {
        private final Long teamId;
        private final String teamName;
        private int played;
        private int won;
        private int drawn;
        private int lost;
        private int goalsFor;
        private int goalsAgainst;
        private long points;

        private Row(CommunityTournamentTeam team) {
            teamId = team.getId();
            teamName = team.getName();
        }

        private int goalDifference() {
            return goalsFor - goalsAgainst;
        }
    }
}
