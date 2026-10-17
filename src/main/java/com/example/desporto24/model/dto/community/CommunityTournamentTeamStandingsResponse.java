package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.TournamentStatus;
import java.util.List;

public record CommunityTournamentTeamStandingsResponse(
        Long tournamentId,
        TournamentStatus status,
        long totalMatches,
        int completedMatches,
        int winPoints,
        int drawPoints,
        int lossPoints,
        List<CommunityTournamentTeamStandingRowResponse> standings
) {
}
