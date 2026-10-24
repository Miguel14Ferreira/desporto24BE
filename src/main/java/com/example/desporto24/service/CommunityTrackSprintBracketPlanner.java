package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import com.example.desporto24.model.dto.community.CommunityTrackSprintBracketDtos.Match;
import java.util.*;

/**
 * Pure, read-only progression preview for a manually seeded eight-rider Sprint
 * knockout. No result is fabricated and no next heat is ever created.
 *
 * UCI seeded quarterfinal slots: Q1/Q4 feed S1; Q2/Q3 feed S2.
 * Winners of S1/S2 feed G (gold);
 * losers feed B (bronze). Other tournament sizes need a separate format.
 */
final class CommunityTrackSprintBracketPlanner {
    private CommunityTrackSprintBracketPlanner() {}
    private static final List<String> SLOTS = List.of("Q1","Q2","Q3","Q4","S1","S2","B","G");
    private static String norm(String s) { return s == null ? null : s.trim().toLowerCase(Locale.ROOT); }

    static String seriesKey(long bracketId, String slot) {
        if (!SLOTS.contains(slot)) throw new IllegalArgumentException("Posição Sprint inválida");
        return "SP" + bracketId + "-" + slot;
    }

    static List<Match> project(long bracketId, List<CommunityRoundResponse> rounds) {
        Map<String, Match> matches = new LinkedHashMap<>();
        for (String slot : SLOTS) {
            String phase = phase(slot);
            String a = null, b = null;
            if ("S1".equals(slot) || "S2".equals(slot)) {
                // UCI 2026 QF bracket positions: winners Q1-v-Q4, Q2-v-Q3.
                a = advanced(matches.get("S1".equals(slot) ? "Q1" : "Q2"), false);
                b = advanced(matches.get("S1".equals(slot) ? "Q4" : "Q3"), false);
            } else if ("G".equals(slot) || "B".equals(slot)) {
                boolean consolation = "B".equals(slot);
                a = advanced(matches.get("S1"), consolation);
                b = advanced(matches.get("S2"), consolation);
            }
            String key = seriesKey(bracketId, slot);
            List<CommunityRoundResponse> heats = rounds.stream()
                    .filter(r -> key.equals(r.sprintSeriesKey()))
                    .sorted(Comparator.comparing(r -> r.sprintHeatNumber() == null
                            ? Integer.MAX_VALUE : r.sprintHeatNumber()))
                    .toList();
            matches.put(slot, examine(phase, slot, key, a, b, heats));
        }

        // A rider cannot start in more than one quarterfinal of the same draw.
        Set<String> riders = new HashSet<>();
        Set<String> duplicateSlots = new HashSet<>();
        for (String slot : SLOTS.subList(0, 4)) {
            Match m = matches.get(slot);
            if (m.riderA() == null || m.riderB() == null) continue;
            for (String player : List.of(m.riderA(), m.riderB())) {
                if (!riders.add(player)) {
                    duplicateSlots.add(slot);
                    for (String other : SLOTS.subList(0, 4)) {
                        Match candidate = matches.get(other);
                        if (!slot.equals(other) && (player.equals(candidate.riderA())
                                || player.equals(candidate.riderB()))) duplicateSlots.add(other);
                    }
                }
            }
        }
        if (!duplicateSlots.isEmpty()) {
            for (String slot : duplicateSlots) {
                Match old = matches.get(slot);
                matches.put(slot, new Match(old.phase(), old.slot(), old.seriesKey(),
                        old.riderA(), old.riderB(), old.winsA(), old.winsB(), null, "CONFLICT"));
            }
            // Never return trusted descendants when any source Q is inconsistent.
            for (String slot : SLOTS.subList(4, 8)) {
                Match old = matches.get(slot);
                matches.put(slot, new Match(old.phase(), old.slot(), old.seriesKey(),
                        old.riderA(), old.riderB(), old.winsA(), old.winsB(), null, "CONFLICT"));
            }
        }
        return List.copyOf(matches.values());
    }

    static void validateNewHeat(long bracketId, String seriesKey, String phase,
            List<CommunityRoundResponse> rounds, List<CommunityRoundEntryRequest> entrants) {
        List<Match> list = project(bracketId, rounds);
        Match match = list.stream().filter(m -> m.seriesKey().equals(seriesKey))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Série fora do quadro Sprint"));
        if (!match.phase().equals(phase))
            throw new IllegalArgumentException("Fase incompatível com a série do quadro Sprint");
        if (!"READY".equals(match.status()) && !"IN_PROGRESS".equals(match.status()))
            throw new IllegalArgumentException("O confronto está fechado ou aguarda apuramento");
        if (entrants.size() != 2)
            throw new IllegalArgumentException("O confronto exige dois ciclistas");
        Set<String> pair = new HashSet<>();
        for (var entry : entrants) {
            String player = norm(entry.username());
            if (player == null || player.isBlank() || !pair.add(player))
                throw new IllegalArgumentException("Ciclistas inválidos");
        }
        if (match.riderA() != null || match.riderB() != null) {
            if (!pair.equals(Set.of(match.riderA(), match.riderB())))
                throw new IllegalArgumentException("Os ciclistas não correspondem ao quadro Sprint");
        }
        if (match.slot().startsWith("Q")) {
            for (Match other : list.subList(0, 4)) {
                if (other.slot().equals(match.slot())) continue;
                if (pair.contains(other.riderA()) || pair.contains(other.riderB()))
                    throw new IllegalArgumentException("Um ciclista só pode disputar um quarto de final");
            }
        }
    }

    private static Match examine(String phase, String slot, String key,
            String expectedA, String expectedB, List<CommunityRoundResponse> heats) {
        boolean sourceNeeded = !slot.startsWith("Q");
        boolean sourcesReady = !sourceNeeded || expectedA != null && expectedB != null
                && !expectedA.equals(expectedB);
        if (heats.isEmpty()) {
            return new Match(phase, slot, key, expectedA, expectedB, 0, 0,
                    null, sourcesReady ? "READY" : "WAITING_PREVIOUS");
        }
        String firstA = null, firstB = null;
        int winsA = 0, winsB = 0;
        boolean conflict = !sourcesReady;
        for (int i=0; i<heats.size(); i++) {
            CommunityRoundResponse round = heats.get(i);
            if (!"TRACK_SPRINT_HEAT".equals(round.eventType()) ||
                    !phase.equals(round.eventDiscipline()) || round.sprintHeatNumber() == null ||
                    round.sprintHeatNumber() != i+1 || round.entries().size() != 2 || i >= 3) {
                conflict = true; break;
            }
            var ea = round.entries().get(0);
            var eb = round.entries().get(1);
            String ra = norm(ea.username()), rb = norm(eb.username());
            if (ra == null || rb == null || ra.isBlank() || rb.isBlank() || ra.equals(rb) ||
                    ea.result() == eb.result() ||
                    !((ea.result() == CommunityMatchResult.WIN && eb.result() == CommunityMatchResult.LOSS)
                      || (ea.result() == CommunityMatchResult.LOSS && eb.result() == CommunityMatchResult.WIN))) {
                conflict = true; break;
            }
            if (firstA == null) { firstA=ra; firstB=rb; }
            if (!Set.of(firstA, firstB).equals(Set.of(ra, rb))) {conflict=true; break;}
            if (i>0 && (winsA >= 2 || winsB >= 2)) {conflict=true; break;}
            if ((ea.result() == CommunityMatchResult.WIN ? ra : rb).equals(firstA)) winsA++;
            else winsB++;
        }
        if (firstA != null && sourcesReady && sourceNeeded &&
                !Set.of(expectedA, expectedB).equals(Set.of(firstA, firstB))) conflict=true;
        String a = sourceNeeded ? expectedA : firstA;
        String b = sourceNeeded ? expectedB : firstB;
        String winner = conflict ? null : winsA==2 ? firstA : winsB==2 ? firstB : null;
        return new Match(phase, slot, key, a, b, winsA, winsB,
                winner, conflict ? "CONFLICT" : winner != null ? "FINISHED" : "IN_PROGRESS");
    }

    private static String advanced(Match match, boolean loser) {
        if (match == null || !"FINISHED".equals(match.status()) || match.winner()==null)
            return null;
        if (!loser) return match.winner();
        return match.winner().equals(match.riderA()) ? match.riderB() : match.riderA();
    }

    static String phase(String slot) {
        return slot.startsWith("Q") ? "SPRINT_QUARTER_FINAL"
                : slot.startsWith("S") ? "SPRINT_SEMI_FINAL"
                : slot.equals("B") ? "SPRINT_BRONZE_FINAL" : "SPRINT_GOLD_FINAL";
    }
}
