package com.example.desporto24.model.dto.community;

import java.time.Instant;
import com.example.desporto24.enumeration.TournamentParticipantStatus;

public record CommunityTournamentParticipantResponse(
        Long profileId,
        String username,
        String displayName,
        Instant joinedAt,
        TournamentParticipantStatus status
) {
    // Keep source compatibility for existing callers of the four-field response.
    public CommunityTournamentParticipantResponse(Long profileId, String username,
                                                   String displayName, Instant joinedAt) {
        this(profileId, username, displayName, joinedAt, TournamentParticipantStatus.CONFIRMED);
    }
}
