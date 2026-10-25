package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.CommunityTrackSprintBracketDtos;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Pure UCI 3.2.048 bis stage and only the expressly specified tied positions.
 * Does not grant medals, unlisted placements, results or UCI points.
 */
final class CommunityTrackSprintInterruption {
    private CommunityTrackSprintInterruption() {}

    static Map<String,CommunityTrackSprintBracketDtos.Match> indexed(
            List<CommunityTrackSprintBracketDtos.Match> matches) {
        if(matches.stream().anyMatch(m->"CONFLICT".equals(m.status())))
            throw new IllegalStateException("Resolve os confrontos inconsistentes");
        return matches.stream().collect(Collectors.toMap(
                CommunityTrackSprintBracketDtos.Match::slot,m->m));
    }

    static String stage(List<CommunityTrackSprintBracketDtos.Match> matches) {
        Map<String,CommunityTrackSprintBracketDtos.Match> slots=indexed(matches);
        boolean quarters=java.util.stream.IntStream.rangeClosed(1,4)
                .allMatch(i->isFinished(slots.get("Q"+i)));
        boolean semis=java.util.stream.IntStream.rangeClosed(1,2)
                .allMatch(i->isFinished(slots.get("S"+i)));
        if(semis && !quarters)
            throw new IllegalStateException("Meias-finais sem quartos concluídos");
        if(semis && isFinished(slots.get("B")) && isFinished(slots.get("G")))
            throw new IllegalStateException("A competição já terminou");
        return !quarters?"BEFORE_QUARTERS":!semis?"AFTER_QUARTERS":"AFTER_SEMIS";
    }

    static List<CommunityTrackSprintBracketDtos.InterruptionPlacement> placements(
            String stage, List<CommunityTrackSprintBracketDtos.Match> matches) {
        Map<String,CommunityTrackSprintBracketDtos.Match> slots=indexed(matches);
        List<CommunityTrackSprintBracketDtos.InterruptionPlacement> placements=new ArrayList<>();
        if("BEFORE_QUARTERS".equals(stage)) return List.of();
        if("AFTER_QUARTERS".equals(stage)) {
            for(int i=1;i<=4;i++) {
                var m=slots.get("Q"+i);
                if(!isFinished(m)) throw new IllegalStateException("Quartos incompletos");
                placements.add(new CommunityTrackSprintBracketDtos.InterruptionPlacement(
                        m.winner(),4));
            }
        } else if("AFTER_SEMIS".equals(stage)) {
            for(int i=1;i<=2;i++) {
                var m=slots.get("S"+i);
                if(!isFinished(m)) throw new IllegalStateException("Meias-finais incompletas");
                String loser=m.winner().equals(m.riderA())?m.riderB():m.riderA();
                placements.add(new CommunityTrackSprintBracketDtos.InterruptionPlacement(
                        m.winner(),2));
                placements.add(new CommunityTrackSprintBracketDtos.InterruptionPlacement(loser,4));
            }
        } else throw new IllegalStateException("Fase de interrupção desconhecida");
        Set<String> distinct=new HashSet<>();
        if(placements.stream().anyMatch(x->x.username()==null || !distinct.add(x.username())))
            throw new IllegalStateException("Ciclistas repetidos na decisão");
        return List.copyOf(placements);
    }

    private static boolean isFinished(CommunityTrackSprintBracketDtos.Match m) {
        return m!=null && "FINISHED".equals(m.status())
                && m.winner()!=null && (m.winner().equals(m.riderA())
                || m.winner().equals(m.riderB()));
    }
}
