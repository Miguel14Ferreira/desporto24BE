package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public final class CommunityTrackSprintBracketDtos {
    private CommunityTrackSprintBracketDtos() {}

    public record Create(@NotBlank @Size(max = 80) String name) {}

    public record Match(String phase, String slot, String seriesKey,
                        String riderA, String riderB, int winsA, int winsB,
                        String winner, String status) {}

    public record Overview(Long id, Long seasonId, String name, String format,
                           List<Match> matches) {}
}
