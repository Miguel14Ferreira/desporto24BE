package com.example.desporto24.service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Set;

/** World Skate Speed Rulebook 2026, article 115. Suggestions are NEVER judge decisions. */
public final class CommunityInlineSpeed2026 {
    private CommunityInlineSpeed2026() {}

    public record Skater(long playerId, BigDecimal seconds, Integer position, boolean disqualified) {}
    public record Heat(long roundId, String eventType, int number, List<Skater> skaters) {}
    public record Proposal(String targetStage, List<Long> candidatePlayerIds,
                           int expectedCount, boolean ready, String warning) {}

    public static String discipline(String type) {
        if (type == null) return null;
        if (type.startsWith("INLINE_TRACK_500D_")) return "TRACK_500D";
        if (type.startsWith("INLINE_ROAD_LAP_")) return "ROAD_LAP";
        return null;
    }

    public static String stage(String type) {
        if (discipline(type) == null) return null;
        if (type.endsWith("_FIRST_ROUND")) return "FIRST";
        if (type.endsWith("_QUARTER_HEAT")) return "QUARTER";
        if (type.endsWith("_SEMI_HEAT")) return "SEMI";
        if (type.endsWith("_FINAL_A")) return "FINAL";
        // World Skate 2026 has no Final B for these two events. Older records remain readable.
        return null;
    }

    public static int maximumHeats(String stage) {
        return switch (stage) {
            case "FIRST" -> 64;
            case "QUARTER" -> 4;
            case "SEMI" -> 2;
            case "FINAL" -> 1;
            default -> throw new IllegalArgumentException("Fase desconhecida");
        };
    }

    public static Proposal propose(List<Heat> all, String sourceStage, String discipline) {
        if (!"TRACK_500D".equals(discipline) && !"ROAD_LAP".equals(discipline))
            throw new IllegalArgumentException("Prova desconhecida");
        if (!List.of("FIRST", "QUARTER", "SEMI").contains(sourceStage))
            throw new IllegalArgumentException("Fase sem progressão");
        List<Heat> heats = all.stream().filter(h -> sourceStage.equals(stage(h.eventType()))
                && discipline.equals(discipline(h.eventType()))).toList();
        String next = sourceStage.equals("FIRST") ? "QUARTER"
                : sourceStage.equals("QUARTER") ? "SEMI" : "FINAL";
        if (heats.isEmpty()) return new Proposal(next, List.of(), 0, false, "Sem mangas associadas");
        Set<Long> unique = new HashSet<>();
        for (Heat heat : heats) {
            if (heat.skaters() == null || heat.skaters().isEmpty()
                    || heat.number() < 1 || heat.number() > maximumHeats(sourceStage))
                return new Proposal(next, List.of(), 0, false, "Manga incompleta");
            for (Skater skater : heat.skaters())
                if (skater.playerId() <= 0 || !unique.add(skater.playerId()))
                    return new Proposal(next, List.of(), 0, false, "Participante repetido entre mangas");
        }
        if (sourceStage.equals("FIRST")) {
            List<Skater> valid = heats.stream().flatMap(h -> h.skaters().stream())
                    .filter(s -> !s.disqualified()).toList();
            if (valid.size() < 8 || valid.stream().anyMatch(s -> s.seconds() == null
                    || s.seconds().signum() <= 0))
                return new Proposal(next, List.of(), 0, false, "Faltam tempos oficiais");
            // The registered field size, not the number of valid finishes, controls whether
            // quarter-finals are required. Excluded skaters still count as registered.
            int registered = unique.size();
            int wanted = registered < 16 ? 8 : 16;
            next = registered < 16 ? "SEMI" : "QUARTER";
            if (valid.size() < wanted)
                return new Proposal(next, List.of(), wanted, false, "Faltam tempos classificáveis");
            List<Skater> ordered = valid.stream()
                    .sorted(Comparator.comparing(Skater::seconds).thenComparingLong(Skater::playerId))
                    .toList();
            List<Long> chosen = new ArrayList<>();
            for (int i = 0; i < wanted; i++) chosen.add(ordered.get(i).playerId());
            String warning = "Confirmação dos juízes obrigatória";
            if (wanted == 16 && ordered.size() > 17
                    && ordered.get(15).seconds().compareTo(ordered.get(17).seconds()) == 0) {
                // More than two skaters at the 16th-place boundary: promoting
                // just the 17th by player ID would be an arbitrary sporting result.
                return new Proposal(next, List.of(), wanted, false,
                        "Empate múltiplo no limite do 16.º: decisão de desempate do júri necessária");
            }
            if (wanted == 16 && ordered.size() > 16
                    && ordered.get(15).seconds().compareTo(ordered.get(16).seconds()) == 0) {
                chosen.add(ordered.get(16).playerId());
                warning = "Empate no limite 16.º/17.º: rever com o juiz (art. 115)";
            } else if (wanted < ordered.size()
                    && ordered.get(wanted - 1).seconds().compareTo(ordered.get(wanted).seconds()) == 0) {
                warning = "Empate no tempo de corte: decisão do juiz necessária";
            }
            if ("ROAD_LAP".equals(discipline))
                warning += "; sistema de qualificação de estrada sujeito ao juiz e comissão técnica";
            return new Proposal(next, List.copyOf(chosen), wanted, true, warning);
        }
        int expectedHeats = sourceStage.equals("QUARTER") ? 4 : 2;
        int expected = sourceStage.equals("QUARTER") ? 8 : 4;
        if (heats.size() != expectedHeats
                || heats.stream().map(Heat::number).distinct().count() != expectedHeats)
            return new Proposal(next, List.of(), expected, false, "Ainda faltam mangas desta fase");
        List<Long> chosen = new ArrayList<>();
        for (Heat heat : heats.stream().sorted(Comparator.comparingInt(Heat::number)).toList()) {
            List<Skater> eligible = heat.skaters().stream().filter(s -> !s.disqualified()).toList();
            if (eligible.stream().filter(s -> Integer.valueOf(1).equals(s.position())).count() != 1
                    || eligible.stream().filter(s -> Integer.valueOf(2).equals(s.position())).count() != 1)
                return new Proposal(next, List.of(), expected, false, "Posições 1 e 2 exigem validação dos juízes");
            eligible.stream().filter(s -> Integer.valueOf(1).equals(s.position())
                    || Integer.valueOf(2).equals(s.position()))
                    .sorted(Comparator.comparingInt(Skater::position))
                    .forEach(s -> chosen.add(s.playerId()));
        }
        return new Proposal(next, List.copyOf(chosen), expected, true,
                "Apuramento sugerido, sujeito à ratificação dos juízes");
    }

    /** Revalidate saved decisions after any edits to source heats or elapsed times. */
    public static String fingerprint(List<Heat> heats) {
        StringBuilder input = new StringBuilder();
        heats.stream().sorted(Comparator.comparingLong(Heat::roundId)).forEach(h -> {
            input.append(h.roundId()).append('|').append(h.eventType()).append('|').append(h.number());
            h.skaters().stream().sorted(Comparator.comparingLong(Skater::playerId)).forEach(s ->
                    input.append(';').append(s.playerId()).append(':').append(s.position())
                            .append(':').append(s.seconds() == null ? "-" : s.seconds().toPlainString())
                            .append(':').append(s.disqualified()));
        });
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(input.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponível", e);
        }
    }
}
