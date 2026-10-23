package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.CommunityRacketStandingGroupResponse;
import com.example.desporto24.model.dto.community.CommunityRacketStandingResponse;
import com.example.desporto24.model.dto.community.CommunityRoundEntryResponse;
import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import com.example.desporto24.model.dto.community.CommunitySetScore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Community-only racket standings derived from completed, structured matches.
 * Sports and singles/doubles have independent tables. No championship points,
 * no cross-sport totals, no retroactive interpretation of legacy W/D/L.
 */
final class CommunityRacketStandings {
    private CommunityRacketStandings() {}

    private record GroupKey(String sport, String format) {}
    private static final class PlayerTotals {
        final Long profileId;
        String username;
        String name;
        String picture;
        int wins;
        int losses;
        int setsWon;
        int setsLost;
        int scored;
        int conceded;

        PlayerTotals(CommunityRoundEntryResponse entry) {
            profileId = entry.profileId();
            updateIdentity(entry);
        }

        void updateIdentity(CommunityRoundEntryResponse entry) {
            if (entry.username() != null) username = entry.username();
            if (entry.fullName() != null && !entry.fullName().isBlank()) name = entry.fullName();
            if (entry.profilePicUrl() != null) picture = entry.profilePicUrl();
        }

        int matches() { return wins + losses; }
        int setsDifference() { return setsWon - setsLost; }
        int scoreDifference() { return scored - conceded; }
        String name() { return name != null ? name : username != null ? username : "Jogador"; }
    }

    private static final Comparator<PlayerTotals> ORDER =
            Comparator.comparingInt((PlayerTotals p) -> p.wins).reversed()
                    .thenComparing(Comparator.comparingInt(PlayerTotals::setsDifference).reversed())
                    .thenComparing(Comparator.comparingInt((PlayerTotals p) -> p.setsWon).reversed())
                    .thenComparing(Comparator.comparingInt(PlayerTotals::scoreDifference).reversed())
                    .thenComparing(Comparator.comparingInt((PlayerTotals p) -> p.scored).reversed())
                    .thenComparing(PlayerTotals::name, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(p -> p.profileId);

    static List<CommunityRacketStandingGroupResponse> calculate(List<CommunityRoundResponse> rounds) {
        Map<GroupKey, Map<Long, PlayerTotals>> groups = new LinkedHashMap<>();
        for (CommunityRoundResponse round : rounds) {
            if (round == null || !CommunityRacketResults.isSupported(round.sport())
                    || (!"SINGLES".equals(round.matchFormat())
                    && !"DOUBLES".equals(round.matchFormat()))
                    || round.setScores() == null || round.setScores().isEmpty()
                    || round.setScores().size() > 5 || round.entries() == null) continue;

            int expected = "SINGLES".equals(round.matchFormat()) ? 1 : 2;
            List<CommunityRoundEntryResponse> a = new ArrayList<>();
            List<CommunityRoundEntryResponse> b = new ArrayList<>();
            Set<Long> identities = new HashSet<>();
            boolean valid = true;
            for (CommunityRoundEntryResponse player : round.entries()) {
                if (player == null || player.profileId() == null
                        || !identities.add(player.profileId())) { valid = false; break; }
                if ("A".equals(player.team())) a.add(player);
                else if ("B".equals(player.team())) b.add(player);
                else { valid = false; break; }
            }
            if (!valid || a.size() != expected || b.size() != expected) continue;

            int setsA = 0, setsB = 0, scoreA = 0, scoreB = 0;
            for (CommunitySetScore score : round.setScores()) {
                if (score == null || score.sideA() == null || score.sideB() == null
                        || score.sideA() < 0 || score.sideB() < 0
                        || score.sideA() > 99 || score.sideB() > 99
                        || score.sideA().equals(score.sideB())) {
                    valid = false; break;
                }
                if (score.sideA() > score.sideB()) setsA++; else setsB++;
                scoreA += score.sideA();
                scoreB += score.sideB();
            }
            if (!valid || setsA == setsB) continue;
            boolean aWon = setsA > setsB;
            // All recorded player outcomes must agree with the actual match score.
            if (a.stream().anyMatch(p -> !"WIN".equals(p.result().name()) == aWon)
                    || b.stream().anyMatch(p -> !"WIN".equals(p.result().name()) != aWon)) {
                continue;
            }
            GroupKey key = new GroupKey(round.sport().trim().toLowerCase(Locale.ROOT),
                    round.matchFormat());
            Map<Long, PlayerTotals> players = groups.computeIfAbsent(key, ignored -> new HashMap<>());
            recordSide(players, a, aWon, setsA, setsB, scoreA, scoreB);
            recordSide(players, b, !aWon, setsB, setsA, scoreB, scoreA);
        }

        List<CommunityRacketStandingGroupResponse> response = new ArrayList<>();
        groups.entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<GroupKey, ?> e) -> e.getKey().sport)
                        .thenComparing(e -> e.getKey().format))
                .forEach(group -> {
                    List<PlayerTotals> ordered = group.getValue().values().stream().sorted(ORDER).toList();
                    List<CommunityRacketStandingResponse> rows = new ArrayList<>();
                    int previousPosition = 0;
                    for (int index = 0; index < ordered.size(); index++) {
                        PlayerTotals p = ordered.get(index);
                        if (index == 0 || sportingCompare(ordered.get(index - 1), p) != 0) {
                            previousPosition = index + 1;
                        }
                        rows.add(new CommunityRacketStandingResponse(
                                previousPosition, p.profileId, p.username, p.name(), p.picture,
                                p.matches(), p.wins, p.losses, p.setsWon, p.setsLost,
                                p.setsDifference(), p.scored, p.conceded, p.scoreDifference()));
                    }
                    response.add(new CommunityRacketStandingGroupResponse(
                            group.getKey().sport(), group.getKey().format(), List.copyOf(rows)));
                });
        return List.copyOf(response);
    }

    private static int sportingCompare(PlayerTotals a, PlayerTotals b) {
        return Comparator.comparingInt((PlayerTotals p) -> p.wins)
                .thenComparingInt(PlayerTotals::setsDifference)
                .thenComparingInt(p -> p.setsWon)
                .thenComparingInt(PlayerTotals::scoreDifference)
                .thenComparingInt(p -> p.scored)
                .compare(a, b);
    }

    private static void recordSide(Map<Long, PlayerTotals> players,
            List<CommunityRoundEntryResponse> entries, boolean won,
            int setsWon, int setsLost, int scored, int conceded) {
        for (CommunityRoundEntryResponse entry : entries) {
            PlayerTotals p = players.computeIfAbsent(entry.profileId(),
                    ignored -> new PlayerTotals(entry));
            p.updateIdentity(entry);
            if (won) p.wins++; else p.losses++;
            p.setsWon += setsWon;
            p.setsLost += setsLost;
            p.scored += scored;
            p.conceded += conceded;
        }
    }
}
