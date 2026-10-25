package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;
import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import com.example.desporto24.model.dto.community.CommunityTrackSprintBracketDtos.Match;
import com.example.desporto24.model.dto.community.CommunityTrackSprintBracketDtos.RankedRider;
import java.util.*;

/**
 * Exact Olympic sprint 24-starter topology, UCI Part III 3.2.050 (2026),
 * including three distinct repechage stages. Read-only: no heat is generated
 * and no rider progresses without a recorded unambiguous WIN/LOSS.
 */
final class CommunityTrackSprintOlympic24 {
    private CommunityTrackSprintOlympic24() {}
    private static String normalized(String s) {
        return s == null ? null : s.trim().toLowerCase(Locale.ROOT);
    }
    static boolean isOlympicSlot(String slot) {
        return slot != null && (slot.matches("A(?:[1-9]|1[0-2])")
            || slot.matches("U[1-4]") || slot.matches("C[1-8]")
            || slot.matches("V[1-4]") || slot.matches("E[1-6]")
            || slot.matches("W[1-2]") || "F58".equals(slot));
    }
    static boolean isRepechage(String slot) {
        return slot.matches("[UVW][1-4]");
    }
    static String phase(String slot) {
        if (slot.startsWith("A")) return "SPRINT_THIRTY_SECOND_FINAL";
        if (slot.startsWith("U")) return "SPRINT_REPECHAGE_1_32";
        if (slot.startsWith("C")) return "SPRINT_SIXTEENTH_FINAL";
        if (slot.startsWith("V")) return "SPRINT_REPECHAGE_1_16";
        if (slot.startsWith("E")) return "SPRINT_EIGHTH_FINAL";
        if (slot.startsWith("W")) return "SPRINT_REPECHAGE_1_8";
        if ("F58".equals(slot)) return "SPRINT_PLACES_5_8";
        return CommunityTrackSprintBracketPlanner.phase(slot);
    }
    private static String key(long id, String slot) {return "SP" + id + "-" + slot;}

    static List<Match> project(long bracketId, List<CommunityRoundResponse> rounds, List<String> seeds) {
        return project(bracketId, rounds, seeds, Map.of());
    }
    static List<Match> project(long bracketId, List<CommunityRoundResponse> rounds,
            List<String> seeds, Map<String,List<String>> reruns) {
        return project(bracketId,rounds,seeds,reruns,Map.of());
    }
    static List<Match> project(long bracketId, List<CommunityRoundResponse> rounds,
            List<String> seeds, Map<String,List<String>> reruns,
            Map<String,String> awardedWinners) {
        return project(bracketId,rounds,seeds,reruns,awardedWinners,Set.of());
    }
    static List<Match> project(long bracketId,List<CommunityRoundResponse> rounds,
            List<String> seeds,Map<String,List<String>> reruns,
            Map<String,String> awardedWinners,Set<String> excludedRiders) {
        return project(bracketId,rounds,seeds,reruns,awardedWinners,excludedRiders,Map.of());
    }
    static List<Match> project(long bracketId,List<CommunityRoundResponse> rounds,
            List<String> seeds,Map<String,List<String>> reruns,
            Map<String,String> awardedWinners,Set<String> excludedRiders,
            Map<String,Map<Integer,String>> decidedHeats) {
        if (seeds == null || seeds.size() != 24 || seeds.stream().anyMatch(x->x==null||x.isBlank())
                || seeds.stream().map(CommunityTrackSprintOlympic24::normalized).distinct().count()!=24)
            throw new IllegalArgumentException("Quadro olímpico exige 24 ciclistas distintos");
        Map<String,Match> m=new LinkedHashMap<>();
        // 1/32: 1 v 24, 2 v 23 ... 12 v 13.
        for (int i=1;i<=12;i++)
            add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"A"+i,normalized(seeds.get(i-1)),
                    normalized(seeds.get(24-i)),null);
        // 1/32 repechage: four heats of THREE defeated riders, table order.
        int[][] u={{1,8,9},{2,7,10},{3,6,11},{4,5,12}};
        for (int i=0;i<4;i++)
            add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"U"+(i+1),lost(m,"A"+u[i][0],excludedRiders),lost(m,"A"+u[i][1],excludedRiders),
                    lost(m,"A"+u[i][2],excludedRiders));
        // 1/16: 8 two-up matches; first four vs winners of repechages.
        for (int i=1;i<=8;i++) {
            String a=won(m,"A"+i);
            String b=i<=4?won(m,"U"+(5-i)):won(m,"A"+(17-i));
            add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"C"+i,a,b,null);
        }
        // 1/16 repechage: pair the eight losses (1/8,2/7,3/6,4/5).
        for(int i=1;i<=4;i++)
            add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"V"+i,lost(m,"C"+i,excludedRiders),lost(m,"C"+(9-i),excludedRiders),null);
        // 1/8: first four vs repechage winners, fifth & sixth from original 1/16 winners.
        for(int i=1;i<=6;i++)
            add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"E"+i,won(m,"C"+i),
                    i<=4?won(m,"V"+(5-i)):won(m,"C"+(13-i)),null);
        // 1/8 repechage: THREE-up, losers of (1,4,5) and (2,3,6).
        add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"W1",lost(m,"E1",excludedRiders),lost(m,"E4",excludedRiders),lost(m,"E5",excludedRiders));
        add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"W2",lost(m,"E2",excludedRiders),lost(m,"E3",excludedRiders),lost(m,"E6",excludedRiders));
        add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"Q1",won(m,"E1"),won(m,"W2"),null);
        add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"Q2",won(m,"E2"),won(m,"W1"),null);
        add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"Q3",won(m,"E3"),won(m,"E6"),null);
        add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"Q4",won(m,"E4"),won(m,"E5"),null);
        addPlaces(m,bracketId,rounds,excludedRiders);
        add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"S1",won(m,"Q1"),won(m,"Q4"),null);
        add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"S2",won(m,"Q2"),won(m,"Q3"),null);
        add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"B",lost(m,"S1",excludedRiders),lost(m,"S2",excludedRiders),null);
        add(m,bracketId,rounds,reruns,awardedWinners,decidedHeats,"G",won(m,"S1"),won(m,"S2"),null);
        return List.copyOf(m.values());
    }
    /**
     * The four quarterfinal losers must race one explicit, four-up classification.
     * No position is awarded using qualifying rank or losing heat time.
     */
    private static void addPlaces(Map<String,Match> matches, long bracketId,
            List<CommunityRoundResponse> rounds,Set<String> excludedRiders) {
        String slot="F58", key=key(bracketId,slot), phase=phase(slot);
        String a=lost(matches,"Q1",excludedRiders), b=lost(matches,"Q2",excludedRiders);
        String c=lost(matches,"Q3",excludedRiders), d=lost(matches,"Q4",excludedRiders);
        List<CommunityRoundResponse> result=rounds.stream()
                .filter(r->key.equals(r.sprintSeriesKey())).toList();
        boolean ready=a!=null&&b!=null&&c!=null&&d!=null
                && Set.of(a,b,c,d).size()==4;
        if (result.isEmpty()) {
            matches.put(slot,new Match(phase,slot,key,a,b,0,0,null,
                    ready?"READY":"WAITING_PREVIOUS",c,d,List.of()));
            return;
        }
        boolean conflict=!ready || result.size()!=1;
        List<RankedRider> ranks=new ArrayList<>();
        if(!conflict) {
            CommunityRoundResponse round=result.get(0);
            conflict=!"TRACK_SPRINT_5_8".equals(round.eventType())
                    || !phase.equals(round.eventDiscipline())
                    || !Integer.valueOf(1).equals(round.sprintHeatNumber())
                    || round.entries().size()!=4;
            if(!conflict) {
                Set<String> actual=new HashSet<>();
                Set<Integer> positions=new HashSet<>();
                for(var entry:round.entries()) {
                    String rider=normalized(entry.username());
                    Integer position=entry.finishPosition();
                    if(rider==null||!actual.add(rider)||position==null
                            ||position<5||position>8||!positions.add(position)
                            ||entry.result()!=CommunityMatchResult.DRAW) {
                        conflict=true;
                        break;
                    }
                    ranks.add(new RankedRider(position,rider));
                }
                if(!actual.equals(Set.of(a,b,c,d))
                        ||!positions.equals(Set.of(5,6,7,8)))conflict=true;
            }
        }
        if(conflict)ranks=List.of();
        else ranks.sort(Comparator.comparingInt(RankedRider::position));
        matches.put(slot,new Match(phase,slot,key,a,b,0,0,null,
                conflict?"CONFLICT":"FINISHED",c,d,List.copyOf(ranks)));
    }

    private static String won(Map<String,Match> m,String slot) {
        Match x=m.get(slot);
        return x!=null&&"FINISHED".equals(x.status())?x.winner():null;
    }
    private static String lost(Map<String,Match> m,String slot) {
        return lost(m,slot,Set.of());
    }
    private static String lost(Map<String,Match> m,String slot,Set<String> excludedRiders) {
        Match x=m.get(slot);
        if(x==null||!"FINISHED".equals(x.status())||x.riderC()!=null)return null;
        String loser=x.winner().equals(x.riderA())?x.riderB():x.riderA();
        // A rider disqualified from competition cannot re-enter any repechage.
        return excludedRiders.contains(loser)?null:loser;
    }
    private static void add(Map<String,Match> m,long id,List<CommunityRoundResponse> rounds,
            Map<String,List<String>> reruns, Map<String,String> awardedWinners,
            Map<String,Map<Integer,String>> decidedHeats,
            String slot,String a,String b,String c) {
        String key=key(id,slot), phase=phase(slot);
        List<String> restart=reruns.get(key);
        boolean twoRiderRerun=restart!=null;
        if(twoRiderRerun) {
            if(!(slot.matches("U[1-4]") || slot.matches("W[1-2]"))
                    || a==null || b==null || c==null
                    || Set.of(a,b,c).size()!=3 || restart.size()!=2
                    || restart.get(0)==null || restart.get(1)==null
                    || restart.get(0).equals(restart.get(1))
                    || !Set.of(a,b,c).containsAll(restart))
                throw new IllegalStateException("Reinício oficial incompatível com repescagem");
            a=restart.get(0); b=restart.get(1); c=null;
        }
        List<CommunityRoundResponse> heats=rounds.stream()
            .filter(r->key.equals(r.sprintSeriesKey()))
            .sorted(Comparator.comparing(r->r.sprintHeatNumber()==null?99:r.sprintHeatNumber()))
            .toList();
        boolean three=slot.startsWith("U")||slot.startsWith("W");
        boolean bestThree=slot.startsWith("Q")||slot.startsWith("S")
                ||slot.equals("B")||slot.equals("G");
        boolean ready=a!=null&&b!=null&&(!three||c!=null||twoRiderRerun);
        Map<Integer,String> decided=decidedHeats.getOrDefault(key,Map.of());
        String officialSeriesWinner=awardedWinners.get(key);
        if(officialSeriesWinner!=null && bestThree) {
            if(!ready||c!=null||twoRiderRerun
                    ||!slot.matches("Q[1-4]|S[1-2]|B|G")
                    ||!Set.of(a,b).contains(officialSeriesWinner))
                throw new IllegalStateException("Veredito oficial BO3 incompatível com confronto");
            // Evaluate only the genuine previously recorded heat wins.
            Map<String,Match> before=new HashMap<>();
            if(heats.isEmpty()&&decided.isEmpty())
                before.put(slot,new Match(phase,slot,key,a,b,0,0,null,"READY",null));
            else addBestThreeWithDecisions(before,slot,phase,key,a,b,heats,decided);
            Match original=before.get(slot);
            if(!Set.of("READY","IN_PROGRESS").contains(original.status()))
                throw new IllegalStateException("Não é possível substituir uma série BO3 concluída");
            m.put(slot,new Match(phase,slot,key,a,b,original.winsA(),original.winsB(),
                    officialSeriesWinner,"FINISHED",null));
            return;
        }
        if (!decided.isEmpty()) {
            if(!bestThree||!ready||c!=null||twoRiderRerun)
                throw new IllegalStateException("Decisão de manga fora de uma série BO3 válida");
            addBestThreeWithDecisions(m,slot,phase,key,a,b,heats,decided);
            return;
        }
        if (!ready && heats.isEmpty()) {
            m.put(slot,new Match(phase,slot,key,a,b,0,0,null,"WAITING_PREVIOUS",c));
            return;
        }
        if(heats.isEmpty()) {
            String officialWinner = awardedWinners.get(key);
            if (officialWinner != null) {
                // Separate, document-backed commissaires decision, NOT a
                // completed physical heat. Only single-heat two-up matches.
                if (!ready || c != null || twoRiderRerun
                        || !slot.matches("(A(?:[1-9]|1[0-2])|C[1-8]|E[1-6]|V[1-4])")
                        || !Set.of(a,b).contains(officialWinner))
                    throw new IllegalStateException("Resultado oficial atribuído fora de uma manga válida");
                m.put(slot,new Match(phase,slot,key,a,b,
                        officialWinner.equals(a)?1:0,officialWinner.equals(b)?1:0,
                        officialWinner,"FINISHED",null));
                return;
            }
            m.put(slot,new Match(phase,slot,key,a,b,0,0,null,"READY",c));
            return;
        }
        Set<String> expected=new HashSet<>();
        if(a!=null)expected.add(a);
        if(b!=null)expected.add(b);
        if(c!=null)expected.add(c);
        boolean conflict=!ready||expected.size()!=(three&&!twoRiderRerun?3:2)
                ||heats.size()>(bestThree?3:1);
        int winsA=0,winsB=0;
        String winner=null;
        for (int i=0;i<heats.size();i++) {
            CommunityRoundResponse heat=heats.get(i);
            String kind=three||slot.startsWith("V")?"TRACK_SPRINT_REPECHAGE":"TRACK_SPRINT_HEAT";
            if(!kind.equals(heat.eventType())||!phase.equals(heat.eventDiscipline())
                    ||heat.sprintHeatNumber()==null||heat.sprintHeatNumber()!=i+1
                    ||heat.entries().size()!=(three&&!twoRiderRerun?3:2)) { conflict=true;break; }
            Set<String> actual=new HashSet<>();
            String raceWinner=null;
            for (var entry:heat.entries()) {
                String person=normalized(entry.username());
                if(person==null||person.isBlank()||!actual.add(person)) {conflict=true;break;}
                if(entry.result()==CommunityMatchResult.WIN) {
                    if(raceWinner!=null)conflict=true;
                    raceWinner=person;
                } else if(entry.result()!=CommunityMatchResult.LOSS)conflict=true;
            }
            if(conflict||!actual.equals(expected)||raceWinner==null
                ||(i>0&&(winsA==2||winsB==2))) {conflict=true;break;}
            if(raceWinner.equals(a))winsA++;
            else if(raceWinner.equals(b))winsB++;
            else winner=raceWinner; // third rider, only in single-heat repechages.
            if(!bestThree)winner=raceWinner;
        }
        if(bestThree && !conflict)
            winner=winsA==2?a:winsB==2?b:null;
        m.put(slot,new Match(phase,slot,key,a,b,winsA,winsB,
                conflict?null:winner,conflict?"CONFLICT":winner==null?"IN_PROGRESS":"FINISHED",c));
    }


    /**
     * A jury-awarded heat is evidence separate from a raced CommunityRound.
     * Progress only after two valid heat wins, with no gaps or double entries.
     */
    private static void addBestThreeWithDecisions(Map<String,Match> matches,
            String slot,String phase,String key,String a,String b,
            List<CommunityRoundResponse> raced,Map<Integer,String> official) {
        Map<Integer,CommunityRoundResponse> racedByNumber=new HashMap<>();
        boolean conflict=false;
        for(var heat:raced) {
            Integer n=heat.sprintHeatNumber();
            if(n==null||n<1||n>3||racedByNumber.putIfAbsent(n,heat)!=null)
                conflict=true;
        }
        Set<Integer> numbers=new HashSet<>(racedByNumber.keySet());
        for(var entry:official.entrySet()) {
            Integer n=entry.getKey();
            if(n==null||n<1||n>3||!numbers.add(n))
                conflict=true;
        }
        if(numbers.isEmpty()||numbers.size()>3)conflict=true;
        int winsA=0,winsB=0;
        for(int n=1;n<=numbers.size()&&!conflict;n++) {
            if(!numbers.contains(n)||winsA==2||winsB==2){conflict=true;break;}
            String heatWinner=null;
            if(official.containsKey(n)) {
                heatWinner=normalized(official.get(n));
            } else {
                var heat=racedByNumber.get(n);
                if(heat==null||!"TRACK_SPRINT_HEAT".equals(heat.eventType())
                        ||!phase.equals(heat.eventDiscipline())
                        ||heat.entries().size()!=2){conflict=true;break;}
                Set<String> seen=new HashSet<>();
                int losses=0;
                for(var entry:heat.entries()) {
                    String person=normalized(entry.username());
                    if(person==null||!seen.add(person)){conflict=true;break;}
                    if(entry.result()==CommunityMatchResult.WIN) {
                        if(heatWinner!=null)conflict=true;
                        heatWinner=person;
                    } else if(entry.result()==CommunityMatchResult.LOSS) losses++;
                    else conflict=true;
                }
                if(!seen.equals(Set.of(a,b))||losses!=1)conflict=true;
            }
            if(conflict||heatWinner==null||!Set.of(a,b).contains(heatWinner)){
                conflict=true;break;
            }
            if(heatWinner.equals(a))winsA++;else winsB++;
        }
        String winner=winsA==2?a:winsB==2?b:null;
        matches.put(slot,new Match(phase,slot,key,a,b,winsA,winsB,
                conflict?null:winner,
                conflict?"CONFLICT":winner==null?"IN_PROGRESS":"FINISHED",null));
    }

    static void validateNewHeat(long id,String seriesKey,String phase,String eventType,
            List<CommunityRoundResponse> rounds,List<CommunityRoundEntryRequest> entrants,
            List<String> seeds) {
        validateNewHeat(id, seriesKey, phase, eventType, rounds, entrants, seeds, Map.of());
    }
    static void validateNewHeat(long id,String seriesKey,String phase,String eventType,
            List<CommunityRoundResponse> rounds,List<CommunityRoundEntryRequest> entrants,
            List<String> seeds,Map<String,List<String>> reruns) {
        validateNewHeat(id,seriesKey,phase,eventType,rounds,entrants,seeds,reruns,Map.of());
    }
    static void validateNewHeat(long id,String seriesKey,String phase,String eventType,
            List<CommunityRoundResponse> rounds,List<CommunityRoundEntryRequest> entrants,
            List<String> seeds,Map<String,List<String>> reruns,
            Map<String,String> awardedWinners) {
        validateNewHeat(id,seriesKey,phase,eventType,rounds,entrants,seeds,reruns,
                awardedWinners,Set.of());
    }
    static void validateNewHeat(long id,String seriesKey,String phase,String eventType,
            List<CommunityRoundResponse> rounds,List<CommunityRoundEntryRequest> entrants,
            List<String> seeds,Map<String,List<String>> reruns,
            Map<String,String> awardedWinners,Set<String> excludedRiders) {
        validateNewHeat(id,seriesKey,phase,eventType,rounds,entrants,seeds,reruns,
                awardedWinners,excludedRiders,Map.of());
    }
    static void validateNewHeat(long id,String seriesKey,String phase,String eventType,
            List<CommunityRoundResponse> rounds,List<CommunityRoundEntryRequest> entrants,
            List<String> seeds,Map<String,List<String>> reruns,
            Map<String,String> awardedWinners,Set<String> excludedRiders,
            Map<String,Map<Integer,String>> decidedHeats) {
        Match m=project(id,rounds,seeds,reruns,awardedWinners,excludedRiders,decidedHeats).stream()
                .filter(x->x.seriesKey().equals(seriesKey)).findFirst()
                .orElseThrow(()->new IllegalArgumentException("Confronto fora do quadro olímpico"));
        if(!m.phase().equals(phase) || ("F58".equals(m.slot())
                ? !"TRACK_SPRINT_5_8".equals(eventType)
                : isRepechage(m.slot())
                    ? !"TRACK_SPRINT_REPECHAGE".equals(eventType)
                    : !"TRACK_SPRINT_HEAT".equals(eventType)))
            throw new IllegalArgumentException("Tipo ou fase incompatível com o quadro olímpico");
        if(!"READY".equals(m.status())&&!"IN_PROGRESS".equals(m.status()))
            throw new IllegalArgumentException("Confronto não disponível ou já terminado");
        Set<String> expected=new HashSet<>(List.of(m.riderA(),m.riderB()));
        if(m.riderC()!=null)expected.add(m.riderC());
        if(m.riderD()!=null)expected.add(m.riderD());
        Set<String> actual=new HashSet<>();
        for(var entry:entrants) {
            String p=normalized(entry.username());
            if(p==null||p.isBlank()||!actual.add(p))
                throw new IllegalArgumentException("Ciclistas inválidos");
        }
        if(actual.size()!=entrants.size() || !actual.equals(expected))
            throw new IllegalArgumentException("Ciclistas não correspondem ao quadro olímpico");
        if("F58".equals(m.slot()))validatePlacings(entrants);
    }

    static void validatePlacings(List<CommunityRoundEntryRequest> entries) {
        if(entries==null || entries.size()!=4)
            throw new IllegalArgumentException("Classificação olímpica exige quatro ciclistas");
        Set<Integer> places=new HashSet<>();
        for(var e:entries) {
            if(e.result()!=CommunityMatchResult.DRAW || e.finishPosition()==null
                    ||e.finishPosition()<5 || e.finishPosition()>8
                    ||!places.add(e.finishPosition())
                    ||e.team()!=null&&!e.team().isBlank()
                    ||e.goals()!=null&&e.goals()!=0
                    ||e.assists()!=null&&e.assists()!=0 || e.mvp() || e.cleanSheet()
                    ||e.elapsedSeconds()!=null || e.bestLapSeconds()!=null
                    ||e.lapsCompleted()!=null || e.performanceValue()!=null
                    ||e.waveScore1()!=null || e.waveScore2()!=null)
                throw new IllegalArgumentException(
                    "Indica posições únicas 5, 6, 7 e 8, sem inventar um vencedor");
        }
        if(!places.equals(Set.of(5,6,7,8)))
            throw new IllegalArgumentException("Faltam classificações do 5.º ao 8.º");
    }

    /** All immediate child rounds, to preserve historical lineage on deletion. */
    static List<String> nextSlots(String slot) {
        if(slot.matches("A(?:[1-9]|1[0-2])")) {
            int i=Integer.parseInt(slot.substring(1));
            int u=i==1||i==8||i==9?1:i==2||i==7||i==10?2
                    :i==3||i==6||i==11?3:4;
            return List.of("U"+u,"C"+(i<=8?i:17-i));
        }
        if(slot.matches("U[1-4]"))return List.of("C"+(5-Integer.parseInt(slot.substring(1))));
        if(slot.matches("C[1-8]")) {
            int i=Integer.parseInt(slot.substring(1));
            return List.of("V"+(Math.min(i,9-i)),"E"+(i<=6?i:i==7?6:5));
        }
        if(slot.matches("V[1-4]"))return List.of("E"+(5-Integer.parseInt(slot.substring(1))));
        if(slot.matches("E[1-6]")) {
            int i=Integer.parseInt(slot.substring(1));
            return List.of(i==1||i==4||i==5?"W1":"W2",
                    "Q"+(i==1?1:i==2?2:i==3||i==6?3:4));
        }
        if(slot.matches("W[1-2]"))return List.of(slot.equals("W1")?"Q2":"Q1");
        return switch(slot) {
            case "Q1","Q4"->List.of("S1","F58");
            case "Q2","Q3"->List.of("S2","F58");
            case "S1","S2"->List.of("B","G");
            default->List.of();
        };
    }
}
