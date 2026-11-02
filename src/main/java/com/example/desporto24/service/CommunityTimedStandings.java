package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.CommunityRoundEntryResponse;
import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import com.example.desporto24.model.dto.community.CommunityTimedStandingResponse;
import java.math.BigDecimal;
import java.util.*;

/** Best recorded time in a single swimming or track discipline. */
final class CommunityTimedStandings {
    private CommunityTimedStandings() {}
    private static final class Athlete {
        final Long id;
        Long profileId;
        String username;
        String name;
        BigDecimal best;
        int attempts;
        Athlete(Long id, CommunityRoundEntryResponse entry) {
            this.id=id;
            profileId=entry.profileId();
            username=entry.username();
            name=entry.fullName();
        }
    }

    static boolean valid(CommunityRoundEntryResponse entry, int decimals) {
        BigDecimal time=entry.elapsedSeconds();
        if (time==null) return false;
        BigDecimal normalized=time.stripTrailingZeros();
        return entry.communityPlayerId()!=null && !entry.raceDisqualified()
                && normalized.signum()>0 && normalized.scale()<=decimals
                && normalized.precision()-normalized.scale()<=8;
    }
    static List<CommunityTimedStandingResponse> calculate(List<CommunityRoundResponse> rounds) {
        return calculate(rounds, 2);
    }
    static List<CommunityTimedStandingResponse> calculate(
            List<CommunityRoundResponse> rounds, int decimals) {
        Map<Long,Athlete> byPlayer=new HashMap<>();
        for (CommunityRoundResponse round : rounds) {
            if (round.entries()==null) continue;
            for (CommunityRoundEntryResponse entry : round.entries()) {
                BigDecimal time=entry.elapsedSeconds();
                // Never invent identity, place, or time from unrecorded attempts.
                if (!valid(entry,decimals)) continue;
                Athlete athlete=byPlayer.computeIfAbsent(entry.communityPlayerId(),
                        id->new Athlete(id,entry));
                if (athlete.best==null || time.compareTo(athlete.best)<0) athlete.best=time;
                if (athlete.profileId==null) athlete.profileId=entry.profileId();
                athlete.attempts++;
            }
        }
        List<Athlete> sorted=byPlayer.values().stream().filter(a->a.best!=null)
                .sorted(Comparator.comparing((Athlete a)->a.best)
                        .thenComparing(a->a.id)).toList();
        List<CommunityTimedStandingResponse> standings=new ArrayList<>();
        int rank=0;
        BigDecimal previous=null;
        for (int i=0;i<sorted.size();i++) {
            Athlete a=sorted.get(i);
            if (previous==null || previous.compareTo(a.best)!=0) rank=i+1;
            standings.add(new CommunityTimedStandingResponse(rank,a.id,a.profileId,
                    a.username,a.name,a.best,a.attempts));
            previous=a.best;
        }
        return List.copyOf(standings);
    }
}
