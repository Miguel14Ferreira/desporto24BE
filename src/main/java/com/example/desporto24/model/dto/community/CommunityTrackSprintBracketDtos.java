package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public final class CommunityTrackSprintBracketDtos {
    private CommunityTrackSprintBracketDtos() {}

    /** Default/legacy format remains the independent eight-rider finals. */
    public record Create(@NotBlank @Size(max = 80) String name,
                         String format, Long qualifyingRoundId) {
        public Create(String name) { this(name, null, null); }
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

    public record Overview(Long id, Long seasonId, String name, String format,
                           List<Match> matches, Long qualifyingRoundId,
                           List<String> qualifyingSeeds) {
        public Overview(Long id, Long seasonId, String name, String format,
                        List<Match> matches) {
            this(id, seasonId, name, format, matches, null, List.of());
        }
    }
}
