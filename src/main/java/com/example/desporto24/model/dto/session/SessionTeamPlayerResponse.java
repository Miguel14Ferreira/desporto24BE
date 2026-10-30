package com.example.desporto24.model.dto.session;

/** Existing username is preserved; only guest rows have displayName/guest set. */
public record SessionTeamPlayerResponse(
        String username,
        double strengthScore,
        boolean statsAvailable,
        int games,
        int wins,
        int goals,
        int assists,
        String displayName,
        boolean guest
) {
    public SessionTeamPlayerResponse(String username,double strengthScore,boolean statsAvailable,
                                     int games,int wins,int goals,int assists){
        this(username,strengthScore,statsAvailable,games,wins,goals,assists,username,false);
    }
}
