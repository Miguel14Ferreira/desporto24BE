package com.example.desporto24.service;

import com.example.desporto24.model.community.*;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityInlineProgressionRulesTest {
    private static final Community COMMUNITY = Community.builder().id(12L).build();
    private static CommunityRound round(long id, String stage, Long seasonId, String at) {
        return CommunityRound.builder().id(id).community(COMMUNITY)
                .season(seasonId == null ? null
                        : CommunitySeason.builder().id(seasonId).build())
                .sport("Patinagem de Velocidade").eventType(stage)
                .playedAt(Instant.parse(at)).build();
    }
    private static CommunityRoundEntry entrant(long id, boolean disqualified) {
        return CommunityRoundEntry.builder()
                .communityPlayer(CommunityPlayer.builder().id(id).build())
                .raceDisqualified(disqualified).build();
    }
    private static void validate(CommunityRound target, CommunityRound source,
            List<CommunityRoundEntry> originEntries, List<CommunityRoundEntry> targetEntries) {
        CommunityInlineProgressionRules.validate(target, List.of(source), targetEntries,
                Map.of(source.getId(), originEntries));
    }

    @Test void trackAndRoadProgression2026MustFollowStagesAndDistance() {
        assertTrue(CommunityInlineProgressionRules.allowedTransition(
                "INLINE_TRACK_500D_FIRST_ROUND", "INLINE_TRACK_500D_QUARTER_HEAT"));
        assertTrue(CommunityInlineProgressionRules.allowedTransition(
                "INLINE_TRACK_500D_FIRST_ROUND", "INLINE_TRACK_500D_SEMI_HEAT"));
        assertTrue(CommunityInlineProgressionRules.allowedTransition(
                "INLINE_TRACK_500D_QUARTER_HEAT", "INLINE_TRACK_500D_SEMI_HEAT"));
        assertTrue(CommunityInlineProgressionRules.allowedTransition(
                "INLINE_ROAD_LAP_SEMI_HEAT", "INLINE_ROAD_LAP_FINAL_A"));
        for (String to : List.of("INLINE_TRACK_500D_FINAL_B",
                "INLINE_ROAD_LAP_SEMI_HEAT", "INLINE_TRACK_500D_FINAL_A")) {
            assertFalse(CommunityInlineProgressionRules.allowedTransition(
                    "INLINE_TRACK_500D_FIRST_ROUND", to));
        }
        assertFalse(CommunityInlineProgressionRules.allowedTransition(null, null));
        assertFalse(CommunityInlineProgressionRules.allowedTransition(
                "INLINE_TRACK_500D_SEMI_HEAT", "INLINE_TRACK_500D_FINAL_B"));
    }

    @Test void manualConfirmationRequiresSharedIdentitySeasonAndRealSource() {
        var source = round(1, "INLINE_TRACK_500D_QUARTER_HEAT", 11L, "2026-10-24T10:00:00Z");
        var target = round(2, "INLINE_TRACK_500D_SEMI_HEAT", 11L, "2026-10-24T11:00:00Z");
        assertDoesNotThrow(() -> validate(target, source, List.of(entrant(10, false),
                entrant(20, false)), List.of(entrant(20, false))));
        assertThrows(IllegalArgumentException.class,
                () -> validate(target, source, List.of(entrant(10, false)),
                        List.of(entrant(20, false))));
        assertThrows(IllegalArgumentException.class,
                () -> validate(target, source, List.of(entrant(20, true)),
                        List.of(entrant(20, false))));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityInlineProgressionRules.validate(target, List.of(source, source),
                        List.of(entrant(20, false)),
                        Map.of(1L, List.of(entrant(20, false)))));
        var otherSeason = round(3, "INLINE_TRACK_500D_QUARTER_HEAT", 12L,
                "2026-10-24T10:00:00Z");
        assertThrows(IllegalArgumentException.class,
                () -> validate(target, otherSeason, List.of(entrant(20, false)),
                        List.of(entrant(20, false))));
        var futureSource = round(4, "INLINE_TRACK_500D_QUARTER_HEAT", 11L,
                "2026-10-24T12:00:00Z");
        assertThrows(IllegalArgumentException.class,
                () -> validate(target, futureSource, List.of(entrant(20, false)),
                        List.of(entrant(20, false))));
        var crossCircuit = round(5, "INLINE_ROAD_LAP_QUARTER_HEAT", 11L,
                "2026-10-24T10:00:00Z");
        assertThrows(IllegalArgumentException.class,
                () -> validate(target, crossCircuit, List.of(entrant(20, false)),
                        List.of(entrant(20, false))));
    }

    @Test void noFinalBMayBeLinkedWhileHistoricalFinalBRemainsReadable() {
        var source = round(1, "INLINE_TRACK_500D_SEMI_HEAT", null, "2026-10-24T10:00:00Z");
        var legacyFinalB = round(2, "INLINE_TRACK_500D_FINAL_B", null,
                "2026-10-24T11:00:00Z");
        assertThrows(IllegalArgumentException.class,
                () -> validate(legacyFinalB, source,
                        List.of(entrant(1, false)), List.of(entrant(1, false))));
        assertTrue(CommunityRoundEventType.isRaceEvent(
                "Patinagem de Velocidade", "INLINE_TRACK_500D_FINAL_B"));
        assertThrows(IllegalArgumentException.class, () -> CommunityRoundEventType.validate(
                "Patinagem de Velocidade", "INLINE_TRACK_500D_FINAL_B"));
    }
}
