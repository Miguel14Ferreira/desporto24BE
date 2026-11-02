package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.*;
import java.math.BigDecimal;
import java.util.*;

/** Provisional team ranking by best recorded relay time. Equal times tie. */
final class CommunityRelayStandings {
    private CommunityRelayStandings() {}
    static List<CommunityRelayStandingResponse> calculate(List<CommunityRelayHeatResponse> heats) {
        Map<Long,List<CommunityRelayHeatResponse>> byTeam=new HashMap<>();
        for(var heat:heats) {
            if(heat.teamId()==null || heat.elapsedSeconds()==null
                    || heat.elapsedSeconds().signum()<=0 || heat.runnerProfileIds()==null
                    || heat.runnerProfileIds().size()!=4
                    || new HashSet<>(heat.runnerProfileIds()).size()!=4) continue;
            byTeam.computeIfAbsent(heat.teamId(),id->new ArrayList<>()).add(heat);
        }
        record Best(Long id,String name,BigDecimal seconds,int heats,List<Long> runners) {}
        List<Best> sorted=byTeam.entrySet().stream().map(entry->{
            var times=entry.getValue();
            var best=times.stream().min(Comparator
                    .comparing(CommunityRelayHeatResponse::elapsedSeconds)
                    .thenComparing(CommunityRelayHeatResponse::id)).orElseThrow();
            return new Best(entry.getKey(),best.teamName(),best.elapsedSeconds(),
                    times.size(),List.copyOf(best.runnerProfileIds()));
        }).sorted(Comparator.comparing(Best::seconds).thenComparing(Best::id)).toList();
        List<CommunityRelayStandingResponse> results=new ArrayList<>();
        BigDecimal previous=null;int rank=0;
        for(int i=0;i<sorted.size();i++) {
            Best b=sorted.get(i);
            if(previous==null || previous.compareTo(b.seconds())!=0)rank=i+1;
            results.add(new CommunityRelayStandingResponse(rank,b.id(),b.name(),b.seconds(),
                    b.heats(),b.runners()));
            previous=b.seconds();
        }
        return List.copyOf(results);
    }
}
