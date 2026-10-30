package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.CommunityPlayer;
import com.example.desporto24.model.community.CommunityStandingsArchive;
import com.example.desporto24.model.community.CommunityStandingsArchiveRow;
import com.example.desporto24.model.dto.session.SessionTeamPlayerResponse;
import com.example.desporto24.model.dto.session.SessionTeamSuggestionResponse;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.community.CommunityStandingsArchiveRepository;
import com.example.desporto24.repository.community.CommunityStandingsArchiveRowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Builds an opt-in football team suggestion from the most recent INDIVIDUAL
 * Excel standings snapshot. This service only reads data; saving the assignment
 * remains an explicit organiser action in SessionTeamSuggestionService.
 *
 * Historical sheet names are NOT automatically assumed to be Jynlo identities:
 * an archive row is used only for an unambiguous alias match to one ACTIVE
 * participant (username, profile name, nickname or an explicitly linked
 * CommunityPlayer). Unknown participants receive the known players' mean,
 * clearly marked as having no individual ranking.
 */
@Service
@RequiredArgsConstructor
public class CommunityExcelTeamBalancer {
    private static final int EXACT_PLAYER_LIMIT = 16;

    private final CommunityStandingsArchiveRepository archives;
    private final CommunityStandingsArchiveRowRepository rows;
    private final CommunityPlayerRepository communityPlayers;

    public SessionTeamSuggestionResponse suggest(Session session, List<SessionParticipant> confirmed) {
        Long communityId = session.getCommunity().getId();
        CommunityStandingsArchive archive = archives
                .findFirstByCommunityIdAndEntryTypeOrderByCreatedAtDescIdDesc(communityId, "INDIVIDUAL")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                        "Importa primeiro uma classificação individual Excel nesta comunidade"));

        Map<String, CommunityStandingsArchiveRow> standings = new HashMap<>();
        for (CommunityStandingsArchiveRow row : rows.findByArchiveIdOrderByPositionAscIdAsc(archive.getId())) {
            standings.put(row.getNormalizedName(), row);
        }
        Map<Long, Set<String>> linkedNames = new HashMap<>();
        for (CommunityPlayer player : communityPlayers.findByCommunityIdOrderByDisplayNameAsc(communityId)) {
            if (player.getProfile() != null && player.getProfile().getId() != null) {
                linkedNames.computeIfAbsent(player.getProfile().getId(), ignored -> new HashSet<>())
                        .add(player.getNormalizedName());
            }
        }

        List<Candidate> candidates = new ArrayList<>();
        Map<Long, Integer> claimCount = new HashMap<>();
        for (SessionParticipant participant : confirmed) {
            Profile profile = participant.getProfile();
            Set<String> aliases = new HashSet<>(linkedNames.getOrDefault(profile.getId(), Set.of()));
            addAlias(aliases, profile.getUsername());
            addAlias(aliases, profile.getFullName());
            addAlias(aliases, profile.getNickname());

            Set<CommunityStandingsArchiveRow> matches = new HashSet<>();
            for (String alias : aliases) {
                CommunityStandingsArchiveRow row = standings.get(alias);
                if (row != null) matches.add(row);
            }
            // Multiple distinct rows could belong to different people. Never guess.
            CommunityStandingsArchiveRow match = matches.size() == 1
                    ? matches.iterator().next() : null;
            if (match != null) claimCount.merge(match.getId(), 1, Integer::sum);
            candidates.add(new Candidate(participant, match));
        }

        int known = 0;
        double knownTotal = 0;
        for (Candidate c : candidates) {
            if (c.match() != null && claimCount.get(c.match().getId()) == 1) {
                known++;
                knownTotal += c.match().getPoints();
            }
        }
        if (known == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A classificação Excel não tem jogadores ligados aos participantes desta sessão");
        }

        double mean = knownTotal / known;
        List<Seed> seeds = new ArrayList<>();
        for (Candidate c : candidates) {
            CommunityStandingsArchiveRow match = c.match();
            boolean matched = match != null && claimCount.get(match.getId()) == 1;
            double score = matched ? match.getPoints() : mean;
            seeds.add(new Seed(c.participant(), score, matched ? match : null));
        }
        // Stable ordering makes equal-scoring suggestions repeatable.
        seeds.sort(Comparator.comparing((Seed p) ->
                p.participant().getProfile().getUsername().toLowerCase(Locale.ROOT))
                .thenComparing(p -> p.participant().getProfile().getId()));

        boolean[] isA = balance(seeds);
        List<SessionTeamPlayerResponse> a = new ArrayList<>();
        List<SessionTeamPlayerResponse> b = new ArrayList<>();
        double scoreA = 0, scoreB = 0;
        for (int i = 0; i < seeds.size(); i++) {
            Seed seed = seeds.get(i);
            CommunityStandingsArchiveRow rank = seed.row();
            SessionTeamPlayerResponse entry = new SessionTeamPlayerResponse(
                    seed.participant().getProfile().getUsername(),
                    round(seed.score()), rank != null,
                    rank == null || rank.getPlayed() == null ? 0 : rank.getPlayed(),
                    rank == null || rank.getWins() == null ? 0 : rank.getWins(),
                    rank == null || rank.getGoalsFor() == null ? 0 : rank.getGoalsFor(),
                    0);
            if (isA[i]) {
                a.add(entry);
                scoreA += seed.score();
            } else {
                b.add(entry);
                scoreB += seed.score();
            }
        }
        return new SessionTeamSuggestionResponse(List.copyOf(a), List.copyOf(b),
                round(scoreA), round(scoreB), known, seeds.size());
    }

    private static void addAlias(Set<String> aliases, String name) {
        if (name != null && !name.isBlank()) aliases.add(normalizeName(name));
    }

    private static String normalizeName(String value) {
        return Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    /**
     * Exact minimum average-points difference for up to 16 confirmed players
     * (10 players = only 252 five-vs-five partitions). For larger gatherings,
     * a deterministic greedy assignment with pairwise exchanges keeps costs
     * bounded. Team sizes always differ by at most one.
     */
    static boolean[] balanceScores(List<Double> points) {
        int n = points.size(), target = (n + 1) / 2;
        if (n < 2) throw new IllegalArgumentException("São necessários dois jogadores");
        boolean[] best = new boolean[n];
        if (n <= EXACT_PLAYER_LIMIT) {
            double bestDiff = Double.POSITIVE_INFINITY;
            int possible = 1 << n;
            for (int mask = 0; mask < possible; mask++) {
                if (Integer.bitCount(mask) != target) continue;
                boolean[] proposed = new boolean[n];
                double sumA = 0, sumB = 0;
                for (int i = 0; i < n; i++) {
                    proposed[i] = (mask & (1 << i)) != 0;
                    if (proposed[i]) sumA += points.get(i);
                    else sumB += points.get(i);
                }
                double diff = Math.abs(sumA / target - sumB / (n - target));
                if (diff < bestDiff - 1e-9) {
                    bestDiff = diff;
                    best = proposed;
                }
            }
            return best;
        }

        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) order.add(i);
        order.sort(Comparator.comparingDouble((Integer i) -> points.get(i)).reversed()
                .thenComparingInt(i -> i));
        double sumA = 0, sumB = 0;
        int countA = 0, countB = 0;
        for (int i : order) {
            boolean toA = countA < target && (countB == n - target
                    || sumA / Math.max(1, countA) <= sumB / Math.max(1, countB));
            best[i] = toA;
            if (toA) { countA++; sumA += points.get(i); }
            else { countB++; sumB += points.get(i); }
        }
        for (int pass = 0; pass < 3; pass++) {
            boolean improved = false;
            for (int i = 0; i < n && !improved; i++) {
                if (!best[i]) continue;
                for (int j = 0; j < n; j++) {
                    if (best[j]) continue;
                    double nextA = sumA - points.get(i) + points.get(j);
                    double nextB = sumB - points.get(j) + points.get(i);
                    if (Math.abs(nextA / countA - nextB / countB) + 1e-9
                            < Math.abs(sumA / countA - sumB / countB)) {
                        best[i] = false;
                        best[j] = true;
                        sumA = nextA;
                        sumB = nextB;
                        improved = true;
                        break;
                    }
                }
            }
            if (!improved) break;
        }
        return best;
    }

    private static boolean[] balance(List<Seed> seeds) {
        return balanceScores(seeds.stream().map(Seed::score).toList());
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private record Candidate(SessionParticipant participant, CommunityStandingsArchiveRow match) {}
    private record Seed(SessionParticipant participant, double score, CommunityStandingsArchiveRow row) {}
}
