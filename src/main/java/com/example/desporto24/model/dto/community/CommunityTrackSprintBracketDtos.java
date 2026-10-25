package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

public final class CommunityTrackSprintBracketDtos {
    private CommunityTrackSprintBracketDtos() {}

    /** Default/legacy format remains the independent eight-rider finals. */
    /**
     * One explicitly witnessed UCI 3.2.027 decision per relevant tied-200m group.
     * LAST_100: each rider's last-100m time must give this exact order.
     * DRAW_LOTS: list the witnessed drawn order and why the 100m fallback
     * was unavailable / still tied. Reference is the official record.
     */
    public record TieDecision(String method, String reason, List<String> orderedUsernames,
                              Map<String, BigDecimal> last100Seconds, String reference) {}

    public record Create(@NotBlank @Size(max = 80) String name,
                         String format, Long qualifyingRoundId,
                         List<TieDecision> officialTieDecisions) {
        public Create(String name) { this(name, null, null, null); }
        public Create(String name, String format, Long qualifyingRoundId) {
            this(name, format, qualifyingRoundId, null);
        }
    }

    /** Ranks are supplied only by a separately recorded four-rider classification. */
    public record RankedRider(int position, String username) {}

    public record Match(String phase, String slot, String seriesKey,
                        String riderA, String riderB, int winsA, int winsB,
                        String winner, String status, String riderC,
                        String riderD, List<RankedRider> rankings) {
        public Match(String phase, String slot, String seriesKey,
                     String riderA, String riderB, int winsA, int winsB,
                     String winner, String status) {
            this(phase, slot, seriesKey, riderA, riderB, winsA, winsB,
                 winner, status, null, null, List.of());
        }
        public Match(String phase, String slot, String seriesKey,
                     String riderA, String riderB, int winsA, int winsB,
                     String winner, String status, String riderC) {
            this(phase, slot, seriesKey, riderA, riderB, winsA, winsB,
                 winner, status, riderC, null, List.of());
        }
    }

    public record EndEvent(@NotBlank @Size(min=12,max=255) String reference,
                           @NotBlank @Size(min=12,max=500) String reason) {}

    public record Interruption(String stage, String reference, String reason,
                               String recordedBy, java.time.Instant recordedAt) {}

    public record Overview(Long id, Long seasonId, String name, String format,
                           List<Match> matches, Long qualifyingRoundId,
                           List<String> qualifyingSeeds,
                           List<TieDecision> officialTieDecisions,
                           String tieDecisionRecordedBy,
                           List<CommunityTrackSprintIncidentDtos.View> incidents,
                           Interruption interruption) {
        public Overview(Long id, Long seasonId, String name, String format,
                        List<Match> matches, Long qualifyingRoundId,
                        List<String> qualifyingSeeds, List<TieDecision> tieDecisions,
                        String recordedBy, List<CommunityTrackSprintIncidentDtos.View> incidents) {
            this(id,seasonId,name,format,matches,qualifyingRoundId,qualifyingSeeds,
                tieDecisions,recordedBy,incidents,null);
        }
        public Overview(Long id, Long seasonId, String name, String format,
                        List<Match> matches) {
            this(id, seasonId, name, format, matches, null, List.of(), List.of(), null, List.of());
        }
        public Overview(Long id, Long seasonId, String name, String format,
                        List<Match> matches, Long qualifyingRoundId,
                        List<String> qualifyingSeeds, List<TieDecision> tieDecisions,
                        String recordedBy) {
            this(id,seasonId,name,format,matches,qualifyingRoundId,
                 qualifyingSeeds,tieDecisions,recordedBy,List.of());
        }
        public Overview(Long id, Long seasonId, String name, String format,
                        List<Match> matches, Long qualifyingRoundId,
                        List<String> qualifyingSeeds) {
            this(id, seasonId, name, format, matches, qualifyingRoundId,
                 qualifyingSeeds, List.of(), null, List.of());
        }
    }
}
