package com.example.desporto24.service;

import com.example.desporto24.model.dto.community.CommunityRoundResponse;
import com.example.desporto24.model.dto.community.CommunityTrackSprintBracketDtos.TieDecision;
import java.math.BigDecimal;
import java.util.*;

/**
 * UCI 3.2.027: tied flying-200 m times are ordered by independently recorded
 * final-100 m times, or by an EXPLICIT documented drawing of lots where
 * final-100 m times are unavailable/still tied. Never use username ordering
 * as an athletic result. Previous untied and DQ behaviour is preserved.
 */
final class CommunityTrackSprintQualifying16 {
    private CommunityTrackSprintQualifying16() {}

    static List<String> seeds(CommunityRoundResponse qualifying) {
        return seeds(qualifying, 16);
    }

    static List<String> seeds(CommunityRoundResponse qualifying, int starters) {
        return seeds(qualifying, starters, List.of());
    }

    static List<String> seeds(CommunityRoundResponse qualifying, int starters,
                              List<TieDecision> decisions) {
        if (starters != 16 && starters != 24 && starters != 28)
            throw new IllegalArgumentException("Formato de Sprint não suportado");
        if (qualifying == null || !"Ciclismo de Pista".equals(qualifying.sport())
                || !"TRACK_FLYING_200".equals(qualifying.eventType())
                || qualifying.entries() == null)
            throw new IllegalArgumentException("Escolhe uma qualificação de 200 m de Ciclismo de Pista");
        record Entry(String username, BigDecimal time) {}
        Set<String> unique = new HashSet<>();
        List<Entry> valid = new ArrayList<>();
        for (var e : qualifying.entries()) {
            if (e.username() == null || e.username().isBlank())
                throw new IllegalArgumentException("Há um participante da qualificação sem conta");
            String username = id(e.username());
            if (!unique.add(username))
                throw new IllegalArgumentException("Um ciclista está duplicado nos 200 m");
            if (e.raceDisqualified()) continue;
            if (e.elapsedSeconds() == null || e.elapsedSeconds().signum() <= 0)
                continue; // DNF / no valid time cannot qualify.
            valid.add(new Entry(username, e.elapsedSeconds()));
        }
        valid.sort(Comparator.comparing(Entry::time).thenComparing(Entry::username));
        if (valid.size() < starters)
            throw new IllegalArgumentException("A qualificação exige " + starters + " tempos de 200 m válidos");

        List<TieDecision> official = decisions == null ? List.of() : decisions;
        Set<Integer> used = new HashSet<>();
        for (int start=0;start<valid.size();) {
            int end=start+1;
            while (end<valid.size() && valid.get(end).time().compareTo(valid.get(start).time())==0) end++;
            if (end-start>1 && start<starters) {
                Set<String> group = new HashSet<>();
                for (int i=start;i<end;i++) group.add(valid.get(i).username());
                int matched=-1;
                for (int i=0;i<official.size();i++) {
                    TieDecision d=official.get(i);
                    if (d==null || d.orderedUsernames()==null) continue;
                    List<String> names=d.orderedUsernames().stream()
                            .map(CommunityTrackSprintQualifying16::id).toList();
                    if (names.size()==group.size() && new HashSet<>(names).equals(group)) {
                        if (matched>=0)
                            throw new IllegalArgumentException("O mesmo empate tem decisões oficiais repetidas");
                        matched=i;
                    }
                }
                if (matched<0)
                    throw new IllegalArgumentException("Empate nos 200 m: regista a decisão oficial dos últimos 100 m ou do sorteio");
                if (!used.add(matched))
                    throw new IllegalArgumentException("Decisão oficial duplicada");
                TieDecision d=official.get(matched);
                List<String> names=d.orderedUsernames().stream()
                        .map(CommunityTrackSprintQualifying16::id).toList();
                if (names.stream().distinct().count()!=names.size())
                    throw new IllegalArgumentException("O desempate contém ciclistas repetidos");
                if (d.reference()==null || d.reference().trim().length()<12
                        || d.reference().trim().length()>255)
                    throw new IllegalArgumentException("Regista a referência oficial do desempate (12–255 caracteres)");
                Map<String,BigDecimal> last100=d.last100Seconds()==null
                        ? Map.of():d.last100Seconds();
                Map<String,BigDecimal> normalizedTimes=new HashMap<>();
                for (var t:last100.entrySet()) {
                    String name=id(t.getKey());
                    if (normalizedTimes.putIfAbsent(name,t.getValue())!=null)
                        throw new IllegalArgumentException("Tempo repetido dos últimos 100 m");
                }
                if ("LAST_100".equals(d.method())) {
                    if (normalizedTimes.size()!=group.size() || !normalizedTimes.keySet().equals(group))
                        throw new IllegalArgumentException("Indica os tempos oficiais dos últimos 100 m de todos os empatados");
                    for (String n: names) {
                        BigDecimal time=normalizedTimes.get(n);
                        if (time==null || time.signum()<=0 || time.scale()>3
                                || time.compareTo(valid.get(start).time())>=0)
                            throw new IllegalArgumentException("Tempo dos últimos 100 m inválido");
                    }
                    List<String> sorted=names.stream().sorted(Comparator
                            .comparing((String n)->normalizedTimes.get(n))).toList();
                    if (!names.equals(sorted))
                        throw new IllegalArgumentException("A ordem não corresponde aos tempos oficiais dos últimos 100 m");
                    for (int i=1;i<sorted.size();i++)
                        if (normalizedTimes.get(sorted.get(i)).compareTo(normalizedTimes.get(sorted.get(i-1)))==0)
                            throw new IllegalArgumentException("Últimos 100 m ainda empatados: é necessária decisão por sorteio");
                    if (d.reason()!=null && !d.reason().isBlank())
                        throw new IllegalArgumentException("O critério dos 100 m não admite motivo de sorteio");
                } else if ("DRAW_LOTS".equals(d.method())) {
                    if ("LAST_100_UNAVAILABLE".equals(d.reason())) {
                        if (!last100.isEmpty())
                            throw new IllegalArgumentException("Não indiques tempos dos últimos 100 m se não foram registados");
                    } else if ("LAST_100_STILL_TIED".equals(d.reason())) {
                        if (normalizedTimes.size()!=group.size() || !normalizedTimes.keySet().equals(group))
                            throw new IllegalArgumentException("Documenta os tempos dos últimos 100 m ainda empatados");
                        BigDecimal common=null;
                        for (String n:names) {
                            BigDecimal time=normalizedTimes.get(n);
                            if (time==null || time.signum()<=0 || time.scale()>3
                                    || time.compareTo(valid.get(start).time())>=0
                                    || common!=null && time.compareTo(common)!=0)
                                throw new IllegalArgumentException("Os últimos 100 m não demonstram um empate total");
                            common=time;
                        }
                    } else throw new IllegalArgumentException(
                            "Indica se o sorteio ocorreu por falta de tempos dos 100 m ou empate persistente");
                } else throw new IllegalArgumentException("Critério oficial de desempate desconhecido");
                Map<String,Entry> byName=new HashMap<>();
                for (int i=start;i<end;i++) byName.put(valid.get(i).username(),valid.get(i));
                for (int i=0;i<names.size();i++)
                    valid.set(start+i,byName.get(names.get(i)));
            }
            start=end;
        }
        if (used.size()!=official.size())
            throw new IllegalArgumentException("Há decisões de desempate não aplicáveis à qualificação");
        return valid.subList(0,starters).stream().map(Entry::username).toList();
    }

    private static String id(String name) {
        if (name==null || name.isBlank())
            throw new IllegalArgumentException("Identifica todos os ciclistas no desempate");
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
