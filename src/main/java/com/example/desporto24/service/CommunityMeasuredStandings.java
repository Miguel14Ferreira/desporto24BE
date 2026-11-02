package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.*;
import java.math.BigDecimal;
import java.util.*;

/** Ranking by highest recorded, valid, positive performance in one field discipline. */
final class CommunityMeasuredStandings {
    private CommunityMeasuredStandings() {}

    private static final class Athlete {
        final Long id;
        Long profileId;
        String username;
        String name;
        BigDecimal best;
        int marks;
        Athlete(Long id, CommunityRoundEntryResponse entry) {
            this.id=id;
            this.profileId=entry.profileId();
            this.username=entry.username();
            this.name=entry.fullName();
        }
    }

    static boolean valid(CommunityRoundEntryResponse entry) {
        BigDecimal mark=entry.performanceValue();
        return entry.communityPlayerId()!=null && !entry.raceDisqualified()
                && mark!=null && mark.signum()>0 && mark.scale()<=3
                && mark.precision()-mark.scale()<=6;
    }

    static List<CommunityMeasuredStandingResponse> calculate(List<CommunityRoundResponse> rounds) {
        Map<Long,Athlete> byPlayer=new HashMap<>();
        for (CommunityRoundResponse round:rounds) {
            if (round.entries()==null) continue;
            for (CommunityRoundEntryResponse entry:round.entries()) {
                if (!valid(entry)) continue;
                BigDecimal mark=entry.performanceValue();
                Athlete athlete=byPlayer.computeIfAbsent(entry.communityPlayerId(),
                        id->new Athlete(id,entry));
                if (athlete.best==null || mark.compareTo(athlete.best)>0) athlete.best=mark;
                if (athlete.profileId==null) athlete.profileId=entry.profileId();
                athlete.marks++;
            }
        }
        List<Athlete> sorted=byPlayer.values().stream()
                .sorted(Comparator.comparing((Athlete a)->a.best).reversed()
                        .thenComparing(a->a.id)).toList();
        List<CommunityMeasuredStandingResponse> result=new ArrayList<>();
        int position=0;
        BigDecimal previous=null;
        for (int i=0;i<sorted.size();i++) {
            Athlete a=sorted.get(i);
            if (previous==null || previous.compareTo(a.best)!=0) position=i+1;
            result.add(new CommunityMeasuredStandingResponse(position,a.id,a.profileId,
                    a.username,a.name,a.best,a.marks));
            previous=a.best;
        }
        return List.copyOf(result);
    }
}
