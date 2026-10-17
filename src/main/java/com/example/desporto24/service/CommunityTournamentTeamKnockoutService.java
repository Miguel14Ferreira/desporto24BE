package com.example.desporto24.service;

import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentTeam;
import com.example.desporto24.model.community.CommunityTournamentTeamMatch;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamMatchRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A single-elimination draw. All writes are made by callers under the tournament
 * pessimistic lock and inside their transaction. Byes are inferred solely from the
 * original first-round pairings; no phantom matches or fake results are persisted.
 */
@Service
@RequiredArgsConstructor
public class CommunityTournamentTeamKnockoutService {
    private static final int MAX_TEAMS = 32;
    private final CommunityTournamentTeamRepository teams;
    private final CommunityTournamentTeamMatchRepository matches;
    private final CommunityTournamentRepository tournaments;

    public List<CommunityTournamentTeamMatch> firstRound(
            CommunityTournament tournament, List<CommunityTournamentTeam> registered) {
        if (tournament.getFormat() != TournamentFormat.KNOCKOUT
                || registered.size() < 2 || registered.size() > MAX_TEAMS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sorteio de eliminatórias inválido");
        }
        List<CommunityTournamentTeam> draw = new ArrayList<>(registered);
        Collections.shuffle(draw, new SecureRandom());
        int firstMatches = registered.size() - Integer.highestOneBit(registered.size() - 1);
        List<CommunityTournamentTeamMatch> result = new ArrayList<>();
        for (int i = 0; i < firstMatches; i++) {
            result.add(game(tournament, draw.get(i * 2), draw.get(i * 2 + 1), 1));
        }
        return result;
    }

    /**
     * Invoked AFTER flushing a newly scored current-round fixture. Advances at
     * most once. A final result atomically snapshots the winner and closes the cup.
     */
    public void advanceAfterResult(CommunityTournament tournament) {
        if (tournament.getFormat() != TournamentFormat.KNOCKOUT
                || tournament.getStatus() != TournamentStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Eliminatória fora de curso");
        }
        var slice = matches.findByTournamentId(tournament.getId(),
                PageRequest.of(0, MAX_TEAMS, Sort.by("roundNumber").ascending().and(Sort.by("id"))));
        List<CommunityTournamentTeamMatch> played = slice.getContent();
        if (slice.hasNext() || played.isEmpty() || played.size() >= MAX_TEAMS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Quadro eliminatório inconsistente");
        }
        int currentRound = played.get(played.size() - 1).getRoundNumber();
        List<CommunityTournamentTeam> winners = new ArrayList<>();
        Set<Long> participants = new HashSet<>();
        for (var fixture : played) {
            if (fixture.getRoundNumber() != currentRound) continue;
            Long first = fixture.getHomeTeam().getId();
            Long second = fixture.getAwayTeam().getId();
            if (first.equals(second) || !participants.add(first) || !participants.add(second)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Uma equipa não pode disputar dois jogos na mesma ronda");
            }
            if (fixture.getStatus() != TournamentMatchStatus.COMPLETED) {
                return; // The round has not finished.
            }
            Integer home = fixture.getHomeScore(), away = fixture.getAwayScore();
            if (home == null || away == null || home.equals(away)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Todos os jogos eliminatórios exigem um vencedor");
            }
            winners.add(home > away ? fixture.getHomeTeam() : fixture.getAwayTeam());
        }
        if (currentRound == 1) {
            List<CommunityTournamentTeam> roster = teams.findByTournamentIdOrderByIdAsc(tournament.getId());
            for (var team : roster) {
                if (!participants.contains(team.getId())) winners.add(team); // First-round bye.
            }
        }
        if (winners.size() == 1) {
            var winner = winners.get(0);
            tournament.setChampionTeamId(winner.getId());
            tournament.setChampionTeamName(winner.getName());
            tournament.setChampionTied(false);
            tournament.setChampionPoints(null); // No league points in a knockout.
            tournament.setCompletedAt(Instant.now());
            tournament.setStatus(TournamentStatus.COMPLETED);
            tournaments.save(tournament);
            return;
        }
        if (winners.size() < 2 || winners.size() > MAX_TEAMS
                || Integer.bitCount(winners.size()) != 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Número inválido de equipas apuradas");
        }
        List<CommunityTournamentTeamMatch> next = new ArrayList<>();
        for (int i = 0; i < winners.size(); i += 2) {
            next.add(game(tournament, winners.get(i), winners.get(i + 1), currentRound + 1));
        }
        matches.saveAll(next);
    }

    private CommunityTournamentTeamMatch game(CommunityTournament tournament,
                                              CommunityTournamentTeam home,
                                              CommunityTournamentTeam away, int round) {
        return CommunityTournamentTeamMatch.builder()
                .tournament(tournament).homeTeam(home).awayTeam(away)
                .roundNumber(round).build();
    }
}
