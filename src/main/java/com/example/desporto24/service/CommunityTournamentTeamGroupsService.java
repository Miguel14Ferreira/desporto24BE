package com.example.desporto24.service;

import com.example.desporto24.enumeration.*;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.*;

/**
 * One round robin per group, followed by a 4- or 8-team knockout.
 * Called under the tournament pessimistic lock for all writes.
 * Draw order is persisted before play, and serves as the final, published
 * tie-break criterion if all sporting criteria remain equal.
 */
@Service
@RequiredArgsConstructor
public class CommunityTournamentTeamGroupsService {
    private static final int MAX_TEAMS = 32;
    // With 32 teams in two groups: 2 x (16 * 15 / 2) = 240 games at most.
    private static final int MAX_GROUP_FIXTURES = 240;
    private final ProfileRepository profiles;
    private final CommunityMemberRepository members;
    private final CommunityTournamentRepository tournaments;
    private final CommunityTournamentTeamRepository teams;
    private final CommunityTournamentTeamMatchRepository matches;

    /** The caller validates all team rosters and the tournament's DRAFT status. */
    public List<CommunityTournamentTeamMatch> draw(
            CommunityTournament tournament, List<CommunityTournamentTeam> registered) {
        int groups = count(tournament);
        if (registered.size() < groups * 2 || registered.size() > MAX_TEAMS) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Cria pelo menos duas equipas por grupo (máximo 32 equipas)");
        }
        List<CommunityTournamentTeam> shuffled = new ArrayList<>(registered);
        Collections.shuffle(shuffled, new SecureRandom());
        Map<Integer, List<CommunityTournamentTeam>> grouped = new TreeMap<>();
        for (int group = 1; group <= groups; group++) grouped.put(group, new ArrayList<>());
        for (int i = 0; i < shuffled.size(); i++) {
            var team = shuffled.get(i);
            int group = i % groups + 1;
            team.setGroupNumber(group);
            team.setGroupDrawOrder(i);
            grouped.get(group).add(team);
        }
        teams.saveAll(shuffled);
        List<CommunityTournamentTeamMatch> fixtures = new ArrayList<>();
        for (var entry : grouped.entrySet()) {
            int group = entry.getKey();
            List<CommunityTournamentTeam> seats = new ArrayList<>(entry.getValue());
            if (seats.size() % 2 != 0) seats.add(null);
            for (int round = 0; round < seats.size() - 1; round++) {
                for (int i = 0; i < seats.size() / 2; i++) {
                    var first = seats.get(i);
                    var second = seats.get(seats.size() - i - 1);
                    if (first == null || second == null) continue;
                    boolean reverse = (round + i) % 2 != 0;
                    fixtures.add(CommunityTournamentTeamMatch.builder()
                            .tournament(tournament).groupNumber(group).roundNumber(round + 1)
                            .homeTeam(reverse ? second : first)
                            .awayTeam(reverse ? first : second).build());
                }
                var last = seats.remove(seats.size() - 1);
                seats.add(1, last);
            }
        }
        return fixtures;
    }

    @Transactional(readOnly = true)
    public CommunityTournamentTeamGroupsResponse standings(Long communityId, Long tournamentId, String username) {
        var actor = profiles.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        if (!members.existsByCommunityIdAndProfileId(communityId, actor.getId())) {
            throw new AccessDeniedException("Não pertences a esta comunidade");
        }
        var tournament = tournaments.findById(tournamentId)
                .orElseThrow(this::notFound);
        if (!tournament.getCommunity().getId().equals(communityId)) throw notFound();
        count(tournament);
        List<CommunityTournamentTeamMatch> fixtures = groupFixtures(tournamentId);
        Map<Integer, List<Row>> tables = tables(tournament, fixtures, false);
        boolean qualified = matches.existsByTournamentIdAndGroupNumberIsNull(tournamentId);
        List<CommunityTournamentTeamGroupResponse> result = new ArrayList<>();
        tables.forEach((number, rows) -> {
            List<CommunityTournamentTeamStandingRowResponse> ranking = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) ranking.add(rows.get(i).response(i + 1));
            result.add(new CommunityTournamentTeamGroupResponse(number, List.copyOf(ranking)));
        });
        return new CommunityTournamentTeamGroupsResponse(tournamentId, qualified, List.copyOf(result));
    }

    /** Called after the last scored group match is flushed, under the tournament row lock. */
    public void advanceAfterGroupResult(CommunityTournament tournament) {
        int groups = count(tournament);
        if (matches.existsByTournamentIdAndGroupNumberIsNull(tournament.getId())) return;
        List<CommunityTournamentTeamMatch> fixtures = groupFixtures(tournament.getId());
        if (fixtures.isEmpty() || fixtures.stream().anyMatch(m -> m.getStatus() != TournamentMatchStatus.COMPLETED)) {
            return;
        }
        Map<Integer, List<Row>> standings = tables(tournament, fixtures, true);
        if (standings.size() != groups) throw invalid("Grupos incompletos");
        Map<Long, CommunityTournamentTeam> byId = new HashMap<>();
        for (var team : teams.findByTournamentIdOrderByIdAsc(tournament.getId())) {
            byId.put(team.getId(), team);
        }
        List<CommunityTournamentTeam> seeded = new ArrayList<>();
        if (groups == 2) {
            seeded.add(byId.get(standings.get(1).get(0).teamId));
            seeded.add(byId.get(standings.get(2).get(1).teamId));
            seeded.add(byId.get(standings.get(2).get(0).teamId));
            seeded.add(byId.get(standings.get(1).get(1).teamId));
        } else {
            // A1-B2, C1-D2 / B1-A2, D1-C2: semifinalists cannot meet
            // a team from their own group before the final.
            for (int[] slot : new int[][]{{1,0},{2,1},{3,0},{4,1},
                    {2,0},{1,1},{4,0},{3,1}}) {
                seeded.add(byId.get(standings.get(slot[0]).get(slot[1]).teamId));
            }
        }
        if (seeded.stream().anyMatch(Objects::isNull)
                || seeded.stream().map(CommunityTournamentTeam::getId).distinct().count() != seeded.size()) {
            throw invalid("Apuramento duplicado ou incompleto");
        }
        List<CommunityTournamentTeamMatch> opening = new ArrayList<>();
        for (int i = 0; i < seeded.size(); i += 2) {
            opening.add(CommunityTournamentTeamMatch.builder()
                    .tournament(tournament).roundNumber(1)
                    .homeTeam(seeded.get(i)).awayTeam(seeded.get(i + 1)).build());
        }
        matches.saveAll(opening);
    }

    private Map<Integer, List<Row>> tables(
            CommunityTournament tournament, List<CommunityTournamentTeamMatch> fixtures,
            boolean requireComplete) {
        int groupCount = count(tournament);
        Map<Integer, List<Row>> result = new TreeMap<>();
        Map<Long, Row> byTeam = new HashMap<>();
        for (int g = 1; g <= groupCount; g++) result.put(g, new ArrayList<>());
        List<CommunityTournamentTeam> allTeams = teams.findByTournamentIdOrderByIdAsc(tournament.getId());
        if (allTeams.size() < groupCount * 2 || allTeams.size() > MAX_TEAMS) {
            throw invalid("Número inválido de equipas nos grupos");
        }
        for (var team : allTeams) {
            Integer g = team.getGroupNumber();
            if (g == null || g < 1 || g > groupCount || team.getGroupDrawOrder() == null) {
                throw invalid("Sorteio de grupos inconsistente");
            }
            Row row = new Row(team);
            result.get(g).add(row);
            if (byTeam.put(team.getId(), row) != null) throw invalid("Equipa repetida");
        }
        for (var rows : result.values()) if (rows.size() < 2) throw invalid("Grupo incompleto");
        Set<String> pairs = new HashSet<>();
        for (var match : fixtures) {
            Row home = byTeam.get(match.getHomeTeam().getId());
            Row away = byTeam.get(match.getAwayTeam().getId());
            if (home == null || away == null || home == away || home.group != away.group
                    || !Objects.equals(match.getGroupNumber(), home.group)
                    || !pairs.add(home.group + ":" + Math.min(home.teamId, away.teamId)
                            + ":" + Math.max(home.teamId, away.teamId))) {
                throw invalid("Confrontos da fase de grupos inconsistentes");
            }
            if (match.getStatus() != TournamentMatchStatus.COMPLETED) continue;
            Integer h = match.getHomeScore(), a = match.getAwayScore();
            if (h == null || a == null || h < 0 || a < 0 || h > 999 || a > 999) {
                throw invalid("Marcador inválido na fase de grupos");
            }
            home.played++; away.played++;
            home.goalsFor += h; home.goalsAgainst += a;
            away.goalsFor += a; away.goalsAgainst += h;
            if (h > a) {
                home.won++; away.lost++;
                home.points += tournament.getWinPoints();
                away.points += tournament.getLossPoints();
            } else if (h < a) {
                away.won++; home.lost++;
                away.points += tournament.getWinPoints();
                home.points += tournament.getLossPoints();
            } else {
                home.drawn++; away.drawn++;
                home.points += tournament.getDrawPoints();
                away.points += tournament.getDrawPoints();
            }
        }
        for (var rows : result.values()) {
            int n = rows.size();
            long expected = (long)n * (n - 1) / 2;
            if (requireComplete && (rows.stream().anyMatch(r -> r.played != n - 1)
                    || pairs.stream().filter(p -> p.startsWith(rows.get(0).group + ":")).count() != expected)) {
                throw invalid("Faltam confrontos neste grupo");
            }
            Comparator<Row> gd = Comparator.comparingInt(Row::difference).reversed();
            Comparator<Row> scored = Comparator.comparingInt((Row x) -> x.goalsFor).reversed();
            Comparator<Row> wins = Comparator.comparingInt((Row x) -> x.won).reversed();
            TournamentTeamTieBreakPriority preference = tournament.getTeamTieBreakPriority() == null
                    ? TournamentTeamTieBreakPriority.GOAL_DIFFERENCE_FIRST : tournament.getTeamTieBreakPriority();
            Comparator<Row> sporting = switch (preference) {
                case GOALS_SCORED_FIRST -> scored.thenComparing(gd).thenComparing(wins);
                case WINS_FIRST -> wins.thenComparing(gd).thenComparing(scored);
                case GOAL_DIFFERENCE_FIRST -> gd.thenComparing(scored).thenComparing(wins);
            };
            // Final criterion: persisted random draw order, never team ID or guessed scores.
            rows.sort(Comparator.comparingLong((Row x) -> x.points).reversed()
                    .thenComparing(sporting).thenComparingInt(x -> x.drawOrder));
        }
        return result;
    }

    private List<CommunityTournamentTeamMatch> groupFixtures(Long tournamentId) {
        var slice = matches.findByTournamentIdAndGroupNumberIsNotNull(tournamentId,
                PageRequest.of(0, MAX_GROUP_FIXTURES + 1, Sort.by("id")));
        if (slice.hasNext()) throw invalid("Demasiados confrontos");
        return slice.getContent();
    }

    private int count(CommunityTournament tournament) {
        if (tournament.getFormat() != TournamentFormat.GROUP_KNOCKOUT
                || tournament.getEntryType() != TournamentEntryType.TEAM
                || tournament.getGroupCount() == null
                || (tournament.getGroupCount() != 2 && tournament.getGroupCount() != 4)) {
            throw invalid("Configuração de grupos inválida");
        }
        return tournament.getGroupCount();
    }

    private ResponseStatusException invalid(String detail) {
        return new ResponseStatusException(HttpStatus.CONFLICT, detail);
    }
    private ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneio não encontrado");
    }

    private static final class Row {
        private final Long teamId;
        private final String teamName;
        private final int group;
        private final int drawOrder;
        private int played, won, drawn, lost, goalsFor, goalsAgainst;
        private long points;
        private Row(CommunityTournamentTeam team) {
            this.teamId = team.getId();
            this.teamName = team.getName();
            this.group = team.getGroupNumber();
            this.drawOrder = team.getGroupDrawOrder();
        }
        private int difference() { return goalsFor - goalsAgainst; }
        private CommunityTournamentTeamStandingRowResponse response(int position) {
            return new CommunityTournamentTeamStandingRowResponse(position, teamId,
                    teamName, played, won, drawn, lost, goalsFor, goalsAgainst,
                    difference(), points);
        }
    }
}
