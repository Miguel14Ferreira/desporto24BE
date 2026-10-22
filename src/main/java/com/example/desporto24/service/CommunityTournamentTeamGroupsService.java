package com.example.desporto24.service;

import com.example.desporto24.enumeration.TournamentFormat;
import com.example.desporto24.enumeration.TournamentMatchStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.model.community.CommunityTournament;
import com.example.desporto24.model.community.CommunityTournamentTeam;
import com.example.desporto24.model.community.CommunityTournamentTeamMatch;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.community.CommunityTournamentTeamMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Three or four real teams per group, one round-robin, then two qualifiers
 * per group enter a single-elimination bracket. Nothing in this service
 * changes ordinary team leagues or cups.
 */
@Service
@RequiredArgsConstructor
public class CommunityTournamentTeamGroupsService {
    private static final Set<Integer> ALLOWED_SIZES = Set.of(4, 6, 8, 12, 16, 24, 32);
    private static final int MAX_MATCHES = 128;
    public static final int FIRST_KNOCKOUT_ROUND = 100;

    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityTournamentTeamMatchRepository matches;

    public static boolean supportsCount(int teams) {
        return ALLOWED_SIZES.contains(teams);
    }

    /** Called under the tournament row lock, with all squads already validated. */
    public List<CommunityTournamentTeamMatch> draw(
            CommunityTournament tournament, List<CommunityTournamentTeam> registered) {
        if (tournament.getFormat() != TournamentFormat.GROUP_KNOCKOUT
                || !supportsCount(registered.size())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Fases de grupos: cria 4, 6, 8, 12, 16, 24 ou 32 equipas");
        }
        int groupSize = (registered.size() == 4 || registered.size() == 8
                || registered.size() == 16 || registered.size() == 32) ? 4 : 3;
        int groupCount = registered.size() / groupSize;
        List<CommunityTournamentTeam> pool = new ArrayList<>(registered);
        Collections.shuffle(pool, new SecureRandom());
        List<CommunityTournamentTeamMatch> fixtures = new ArrayList<>();
        for (int group = 0; group < groupCount; group++) {
            List<CommunityTournamentTeam> members = pool.subList(group * groupSize,
                    (group + 1) * groupSize);
            // Berger pairings: nobody plays twice in a group round, including a bye for three.
            int[][] pairings = groupSize == 3
                    ? new int[][] {{0, 1, 1}, {1, 2, 2}, {0, 2, 3}}
                    : new int[][] {{0, 3, 1}, {1, 2, 1}, {0, 2, 2},
                                    {3, 1, 2}, {0, 1, 3}, {2, 3, 3}};
            for (int[] pairing : pairings) {
                fixtures.add(CommunityTournamentTeamMatch.builder()
                        .tournament(tournament)
                        .homeTeam(members.get(pairing[0]))
                        .awayTeam(members.get(pairing[1]))
                        .roundNumber(pairing[2])
                        .groupNumber(group + 1)
                        .build());
            }
        }
        return fixtures;
    }

    /**
     * After a group match is flushed under the tournament row lock, transition
     * exactly once, only when *all* group fixtures have valid completed scores.
     * Ties after all sporting criteria are resolved by a random draw exactly
     * once at the transition; the selected pairing is then persisted.
     */
    public void advanceWhenGroupsComplete(CommunityTournament tournament) {
        if (tournament.getFormat() != TournamentFormat.GROUP_KNOCKOUT
                || tournament.getStatus() != TournamentStatus.IN_PROGRESS) return;
        var fixtures = allFixtures(tournament.getId());
        if (fixtures.stream().anyMatch(m -> m.getGroupNumber() == null)) return;
        var groupGames = fixtures.stream().filter(m -> m.getGroupNumber() != null).toList();
        if (groupGames.isEmpty() || groupGames.stream()
                .anyMatch(m -> m.getStatus() != TournamentMatchStatus.COMPLETED)) return;
        var groups = calculate(tournament, groupGames, true);
        if (groups.isEmpty()) throw invalid();
        List<CommunityTournamentTeamMatch> bracket = new ArrayList<>();
        if (groups.size() == 1) {
            var first = groups.get(0).rows();
            bracket.add(knockout(tournament, first.get(0).team, first.get(1).team));
        } else {
            if (Integer.bitCount(groups.size()) != 1) throw invalid();
            for (int g = 0; g < groups.size(); g += 2) {
                var first = groups.get(g).rows();
                var second = groups.get(g + 1).rows();
                bracket.add(knockout(tournament, first.get(0).team, second.get(1).team));
                bracket.add(knockout(tournament, second.get(0).team, first.get(1).team));
            }
        }
        matches.saveAll(bracket);
    }

    @Transactional(readOnly = true)
    public List<GroupOverview> groups(Long communityId, Long tournamentId, String username) {
        var actor = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador não encontrado"));
        if (!members.existsByCommunityIdAndProfileId(communityId, actor.getId())) {
            throw new AccessDeniedException("Não pertences a esta comunidade");
        }
        var tournament = tournaments.findById(tournamentId)
                .orElseThrow(this::invalid);
        if (!tournament.getCommunity().getId().equals(communityId)
                || tournament.getFormat() != TournamentFormat.GROUP_KNOCKOUT) throw invalid();

        List<CommunityTournamentTeamMatch> fixtures = allFixtures(tournamentId);
        boolean bracketCreated = fixtures.stream().anyMatch(m -> m.getGroupNumber() == null);
        Set<Long> qualifiers = new HashSet<>();
        if (bracketCreated) {
            fixtures.stream().filter(m -> m.getGroupNumber() == null
                    && m.getRoundNumber() == FIRST_KNOCKOUT_ROUND).forEach(m -> {
                qualifiers.add(m.getHomeTeam().getId());
                qualifiers.add(m.getAwayTeam().getId());
            });
        }
        return calculate(tournament, fixtures.stream().filter(m -> m.getGroupNumber() != null)
                .toList(), false).stream().map(group -> {
            var ranking = new ArrayList<GroupStanding>();
            int place = 1;
            for (var row : group.rows()) {
                ranking.add(new GroupStanding(place++, row.team.getId(), row.team.getName(),
                        row.played, row.won, row.drawn, row.lost, row.goalsFor, row.goalsAgainst,
                        row.goalsFor - row.goalsAgainst, row.points,
                        bracketCreated && qualifiers.contains(row.team.getId())));
            }
            return new GroupOverview(group.groupNumber(), group.completed(), group.total(),
                    bracketCreated, List.copyOf(ranking));
        }).toList();
    }

    private CommunityTournamentTeamMatch knockout(CommunityTournament tournament,
            CommunityTournamentTeam first, CommunityTournamentTeam second) {
        if (first.getId().equals(second.getId())) throw invalid();
        return CommunityTournamentTeamMatch.builder().tournament(tournament)
                .homeTeam(first).awayTeam(second)
                .roundNumber(FIRST_KNOCKOUT_ROUND).build();
    }

    private List<CommunityTournamentTeamMatch> allFixtures(Long id) {
        var slice = matches.findByTournamentId(id, PageRequest.of(0, MAX_MATCHES,
                Sort.by("roundNumber").ascending().and(Sort.by("id"))));
        if (slice.hasNext()) throw invalid();
        return slice.getContent();
    }

    private List<CalculatedGroup> calculate(CommunityTournament tournament,
            List<CommunityTournamentTeamMatch> games, boolean drawFullTies) {
        Map<Integer, Map<Long, Row>> standings = new LinkedHashMap<>();
        Map<Integer, Integer> completed = new HashMap<>();
        Map<Integer, Integer> total = new HashMap<>();
        for (var game : games) {
            Integer group = game.getGroupNumber();
            if (group == null || group < 1 || group > 8) throw invalid();
            total.merge(group, 1, Integer::sum);
            var table = standings.computeIfAbsent(group, unused -> new LinkedHashMap<>());
            var home = table.computeIfAbsent(game.getHomeTeam().getId(),
                    unused -> new Row(game.getHomeTeam()));
            var away = table.computeIfAbsent(game.getAwayTeam().getId(),
                    unused -> new Row(game.getAwayTeam()));
            if (home == away) throw invalid();
            if (game.getStatus() != TournamentMatchStatus.COMPLETED) continue;
            Integer hs = game.getHomeScore(), as = game.getAwayScore();
            if (hs == null || as == null || hs < 0 || as < 0 || hs > 999 || as > 999) throw invalid();
            completed.merge(group, 1, Integer::sum);
            home.played++; away.played++;
            home.goalsFor += hs; home.goalsAgainst += as;
            away.goalsFor += as; away.goalsAgainst += hs;
            if (hs > as) {
                home.won++; away.lost++;
                home.points += tournament.getWinPoints();
                away.points += tournament.getLossPoints();
            } else if (hs < as) {
                away.won++; home.lost++;
                away.points += tournament.getWinPoints();
                home.points += tournament.getLossPoints();
            } else {
                home.drawn++; away.drawn++;
                home.points += tournament.getDrawPoints();
                away.points += tournament.getDrawPoints();
            }
        }
        Comparator<Row> sporting = Comparator.comparingLong((Row r) -> r.points).reversed()
                .thenComparing(Comparator.comparingInt((Row r) -> r.goalsFor - r.goalsAgainst).reversed())
                .thenComparing(Comparator.comparingInt((Row r) -> r.goalsFor).reversed())
                .thenComparing(Comparator.comparingInt((Row r) -> r.won).reversed());
        List<CalculatedGroup> result = new ArrayList<>();
        for (var entry : standings.entrySet()) {
            List<Row> rows = new ArrayList<>(entry.getValue().values());
            if (rows.size() < 3 || rows.size() > 4
                    || total.get(entry.getKey()) != rows.size() * (rows.size() - 1) / 2) {
                throw invalid();
            }
            if (drawFullTies) Collections.shuffle(rows, new SecureRandom());
            rows.sort(sporting.thenComparing(row -> drawFullTies ? 0L : row.team.getId()));
            result.add(new CalculatedGroup(entry.getKey(), rows,
                    completed.getOrDefault(entry.getKey(), 0), total.get(entry.getKey())));
        }
        result.sort(Comparator.comparingInt(CalculatedGroup::groupNumber));
        return result;
    }

    private ResponseStatusException invalid() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Fase de grupos inconsistente");
    }

    private static final class Row {
        private final CommunityTournamentTeam team;
        private int played, won, drawn, lost, goalsFor, goalsAgainst;
        private long points;
        private Row(CommunityTournamentTeam team) { this.team = team; }
    }
    private record CalculatedGroup(int groupNumber, List<Row> rows, int completed, int total) {}

    public record GroupOverview(int groupNumber, int completedMatches, int totalMatches,
            boolean knockoutPublished, List<GroupStanding> standings) {}
    public record GroupStanding(int position, Long teamId, String teamName, int played, int won,
            int drawn, int lost, int goalsFor, int goalsAgainst, int goalDifference, long points,
            boolean qualified) {}
}
