package com.example.desporto24.model.dto.community;

/** Per-player racket results, never football WDL points. */
public record CommunityRacketStandingResponse(
        int position,
        Long profileId,
        String username,
        String fullName,
        String profilePicUrl,
        int matches,
        int wins,
        int losses,
        int setsWon,
        int setsLost,
        int setDifference,
        int gamesOrPointsWon,
        int gamesOrPointsLost,
        int gamesOrPointsDifference,
        Integer championshipPoints
) {
    public CommunityRacketStandingResponse(int position, Long profileId,
            String username, String fullName, String profilePicUrl,
            int matches, int wins, int losses, int setsWon, int setsLost,
            int setDifference, int gamesOrPointsWon, int gamesOrPointsLost,
            int gamesOrPointsDifference) {
        this(position, profileId, username, fullName, profilePicUrl,
                matches, wins, losses, setsWon, setsLost, setDifference,
                gamesOrPointsWon, gamesOrPointsLost, gamesOrPointsDifference, null);
    }
}
