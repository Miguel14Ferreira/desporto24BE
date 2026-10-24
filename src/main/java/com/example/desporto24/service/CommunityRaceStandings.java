package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.*;

import java.util.*;
import java.util.stream.Collectors;

/** Community-defined, non-federation scoring; raw finishing positions never change. */
final class CommunityRaceStandings {
    private CommunityRaceStandings() {}

    private static final class Tally {
        final Long id;
        Long profileId;
        String username;
        String name;
        String photo;
        int races;
        int wins;
        int podiums;
        int bestFinish = Integer.MAX_VALUE;
        int points;
        int penalties;

        Tally(CommunityRoundEntryResponse entry) {
            id = entry.communityPlayerId();
            profileId = entry.profileId();
            username = entry.username();
            name = entry.fullName();
            photo = entry.profilePicUrl();
        }
    }

    static List<Integer> parsePoints(String csv) {
        if (csv == null || csv.isBlank()) throw new IllegalArgumentException("Indica a pontuação por posição");
        String[] parts = csv.trim().split(",", -1);
        if (parts.length > 20) throw new IllegalArgumentException("Só são permitidas as primeiras 20 posições");
        List<Integer> points = new ArrayList<>();
        int last = Integer.MAX_VALUE;
        for (String token : parts) {
            String trimmed = token.trim();
            if (!trimmed.matches("[0-9]{1,4}"))
                throw new IllegalArgumentException("Pontuação inválida por posição");
            int point = Integer.parseInt(trimmed);
            if (point > 1000 || point > last)
                throw new IllegalArgumentException("Os pontos devem ser decrescentes, entre 0 e 1000");
            points.add(point);
            last = point;
        }
        return List.copyOf(points);
    }

    static List<CommunityRaceStandingGroupResponse> calculate(
            List<CommunityRoundResponse> rounds,
            List<CommunityRaceRuleResponse> rules) {
        Map<String, CommunityRaceRuleResponse> bySport = new HashMap<>();
        for (CommunityRaceRuleResponse rule : rules)
            bySport.put(rule.sport().trim().toLowerCase(Locale.ROOT), rule);
        Map<String, Map<Long, Tally>> totals = new LinkedHashMap<>();
        Map<String, String> display = new HashMap<>();
        for (CommunityRoundResponse round : rounds) {
            // Swimming pool results have distance/stroke-specific times. Do not combine
            // them in a sport-wide race championship or inherit generic positions.
            if (round != null && ("Natação".equals(round.sport())
                    && "SWIM_LCM".equals(round.eventType())
                    || "Natação em Águas Abertas".equals(round.sport())
                    && "OWS_INDIVIDUAL".equals(round.eventType())
                    || "Orientação".equals(round.sport())
                    && "FOOT_ORIENTEERING".equals(round.eventType()))) continue;
            if (round == null || !CommunityRoundEventType.isRaceEvent(round.sport(), round.eventType())
                    || round.entries() == null) continue;
            String sport = round.sport().trim().toLowerCase(Locale.ROOT);
            CommunityRaceRuleResponse rule = bySport.get(sport);
            List<Integer> points = rule == null ? List.of() : rule.pointsByPlace();
            Set<Integer> occupied = new HashSet<>();
            boolean valid = true;
            for (CommunityRoundEntryResponse entry : round.entries()) {
                if (entry.finishPosition() != null &&
                        (entry.finishPosition() < 1 || !occupied.add(entry.finishPosition()))) {
                    valid = false; break;
                }
            }
            if (!valid) continue;
            // A road-cycling time trial is a separately timed discipline and
            // must never silently count toward a road-race points table.
            if (("Ciclismo de Estrada".equals(round.sport())
                    && "TIME_TRIAL".equals(round.eventType()))
                    || ("Ciclismo de Pista".equals(round.sport())
                    && "TRACK_TIME_TRIAL".equals(round.eventType()))) continue;
            display.putIfAbsent(sport, round.sport().trim());
            Map<Long, Tally> players = totals.computeIfAbsent(sport, unused -> new HashMap<>());
            for (CommunityRoundEntryResponse entry : round.entries()) {
                Integer place = entry.finishPosition();
                if (place == null || entry.communityPlayerId() == null) continue;
                Tally person = players.computeIfAbsent(entry.communityPlayerId(), ignored -> new Tally(entry));
                person.races++;
                // DSQ remains in participation history but earns no podium or
                // championship points. Original finishing position stays intact.
                if (entry.raceDisqualified()) continue;
                if (place == 1) person.wins++;
                if (place <= 3) person.podiums++;
                person.bestFinish = Math.min(person.bestFinish, place);
                if (rule != null) {
                    int base = place <= points.size() ? points.get(place - 1) : 0;
                    int penalty = Math.max(0, entry.racePenaltyPoints());
                    person.penalties += penalty;
                    person.points += Math.max(0, base - penalty);
                }
            }
        }
        List<CommunityRaceStandingGroupResponse> result = new ArrayList<>();
        for (String sport : totals.keySet().stream().sorted().toList()) {
            CommunityRaceRuleResponse rule = bySport.get(sport);
            List<Tally> ranked = totals.get(sport).values().stream()
                    .sorted((a, b) -> {
                        int compare = sportCompare(a, b, rule);
                        if (compare != 0) return -compare;
                        compare = String.CASE_INSENSITIVE_ORDER.compare(
                                a.name == null ? "" : a.name, b.name == null ? "" : b.name);
                        return compare != 0 ? compare : a.id.compareTo(b.id);
                    }).toList();
            List<CommunityRaceStandingResponse> rows = new ArrayList<>();
            int position = 0;
            for (int i = 0; i < ranked.size(); i++) {
                Tally t = ranked.get(i);
                if (i == 0 || sportCompare(ranked.get(i - 1), t, rule) != 0) position = i + 1;
                rows.add(new CommunityRaceStandingResponse(position, t.id, t.profileId,
                        t.username, t.name, t.photo, t.races, t.wins, t.podiums,
                        t.bestFinish == Integer.MAX_VALUE ? null : t.bestFinish,
                        rule == null ? null : t.points,
                        t.penalties));
            }
            result.add(new CommunityRaceStandingGroupResponse(
                    display.get(sport), rule, List.copyOf(rows)));
        }
        return List.copyOf(result);
    }

    /** Comparisons are ascending; sort negates this to get strongest first. */
    private static int sportCompare(Tally a, Tally b, CommunityRaceRuleResponse rule) {
        int c = rule == null ? 0 : Integer.compare(a.points, b.points);
        if (c != 0) return c;
        c = Integer.compare(a.wins, b.wins);
        if (c != 0) return c;
        c = Integer.compare(a.podiums, b.podiums);
        if (c != 0) return c;
        c = Integer.compare(b.bestFinish, a.bestFinish);
        return c;
    }
}
