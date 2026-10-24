package com.example.desporto24.service;

import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.dto.community.CommunityRoundEntryRequest;

/**
 * Structured event variants for sports that contain incompatible event types.
 * An event type describes the contest, not its scoring or federation rules.
 * Historical records keep null; new records for these sports require a choice.
 */
final class CommunityRoundEventType {
    private CommunityRoundEventType() {}

    private static final Map<String, List<String>> TYPES = Map.ofEntries(
            Map.entry("BMX", List.of("RACE", "FREESTYLE")),
            Map.entry("Basquetebol", List.of("BASKETBALL_FINAL")),
            Map.entry("Basquetebol 3x3", List.of("BASKETBALL_3X3_FINAL")),
            Map.entry("Basebol", List.of("BASEBALL_RUNS")),
            Map.entry("Softbol", List.of("SOFTBALL_RUNS")),
            Map.entry("Corfebol", List.of("KORFBALL_REG", "KORFBALL_GOLDEN")),
            Map.entry("Lacrosse", List.of("LACROSSE_M_REG", "LACROSSE_M_OT")),
            Map.entry("Futebol Americano", List.of("AMFOOTBALL_SCORE")),
            Map.entry("Flag Football", List.of("FLAGFOOTBALL_SCORE")),
            Map.entry("Curling", List.of("CURLING_REG", "CURLING_EXTRA")),
            Map.entry("Petanca", List.of("PETANQUE_13", "PETANQUE_11")),
            Map.entry("Bowling", List.of("BOWLING_TENPIN")),
            Map.entry("Bilhar", List.of("POOL_9_RACKS")),
            Map.entry("Esgrima", List.of("FENCING_POOL_5", "FENCING_DE_15")),
            Map.entry("Teqball", List.of("TEQBALL_SINGLES", "TEQBALL_DOUBLES")),
            Map.entry("Xadrez", List.of("CHESS_GAME")),
            Map.entry("Musculação", List.of("STRENGTH_TRAINING")),
            Map.entry("Muay Thai", List.of("IFMA_FULL_WP", "IFMA_FULL_KO",
                    "IFMA_FULL_RSCS", "IFMA_FULL_RSCI")),
            Map.entry("Kickboxing", List.of("WAKO_POINT_FIGHTING", "WAKO_LIGHT_CONTACT",
                    "WAKO_KICK_LIGHT", "WAKO_FULL_CONTACT", "WAKO_LOW_KICK", "WAKO_K1")),
            Map.entry("Boxe", List.of("BOXING_WB_POINTS", "BOXING_WB_KO", "BOXING_WB_RSC")),
            Map.entry("Jiu-Jitsu Brasileiro", List.of("BJJ_IBJJF_GI_SUBMISSION", "BJJ_IBJJF_GI_POINTS")),
            Map.entry("Taekwondo", List.of("TKD_KYORUGI_BEST3", "TKD_POOMSAE_RECOGNIZED",
                    "TKD_POOMSAE_FREESTYLE")),
            Map.entry("Karaté", List.of("KARATE_KUMITE_POINTS", "KARATE_KUMITE_SENSHU",
                    "KARATE_KUMITE_HANTEI", "KARATE_KATA_JUDGES")),
            Map.entry("Judo", List.of("JUDO_IPPON", "JUDO_AWASETE_IPPON",
                    "JUDO_WAZA_ARI", "JUDO_YUKO", "JUDO_HANSOKU_MAKE")),
            Map.entry("Cricket", List.of("CRICKET_T20")),
            Map.entry("Golfe", List.of("GOLF_STROKE_9", "GOLF_STROKE_18")),
            Map.entry("Voleibol", List.of("VOLLEYBALL_FINAL")),
            Map.entry("Voleibol de Praia", List.of("BEACH_VOLLEY_FINAL")),
            Map.entry("Futevólei", List.of(
                    "FOOTVOLLEY_SINGLE_18", "FOOTVOLLEY_BEST_OF_3", "FOOTVOLLEY_SINGLE_15")),
            Map.entry("Andebol", List.of("HANDBALL_FINAL")),
            Map.entry("Rugby", List.of("RUGBY_FINAL")),
            Map.entry("Rugby Sevens", List.of("RUGBY_SEVENS_FINAL")),
            Map.entry("Futsal", List.of("FUTSAL_FINAL")),
            Map.entry("Hóquei em Campo", List.of("FIELD_HOCKEY_FINAL")),
            Map.entry("Hóquei em Patins", List.of("ROLLER_HOCKEY_FINAL")),
            Map.entry("Hóquei no Gelo", List.of(
                    "ICE_HOCKEY_REG", "ICE_HOCKEY_OT", "ICE_HOCKEY_SO")),
            Map.entry("Polo Aquático", List.of("WATER_POLO_FINAL")),
            Map.entry("Atletismo", List.of("TRACK", "JUMP", "THROW")),
            Map.entry("Snowboard", List.of("RACE", "FREESTYLE")),
            Map.entry("Surf", List.of("HEAT")),
            Map.entry("Bodyboard", List.of("BODYBOARD_HEAT")),
            Map.entry("Ginástica Artística", List.of("APPARATUS")),
            Map.entry("Skate", List.of("SKATE_PARK", "SKATE_STREET")),
            Map.entry("Parkour", List.of("PARKOUR_SPEED", "PARKOUR_FREESTYLE")),
            Map.entry("Breaking", List.of("BREAKING_BATTLE")),
            Map.entry("Trampolim", List.of("TRAMPOLINE_INDIVIDUAL")),
            Map.entry("Ginástica Rítmica", List.of(
                    "RHYTHMIC_APPARATUS", "RHYTHMIC_GROUP_5", "RHYTHMIC_GROUP_3_2")),
            Map.entry("Natação Artística", List.of(
                    "ARTISTIC_SOLO_TECHNICAL", "ARTISTIC_SOLO_FREE",
                    "ARTISTIC_DUET_TECH", "ARTISTIC_DUET_FREE",
                    "ARTISTIC_TEAM_TECH", "ARTISTIC_TEAM_FREE"))
    );

    static String validate(String sport, String requested) {
        List<String> allowed = TYPES.get(sport);
        if (allowed == null) {
            if (requested != null && !requested.isBlank())
                throw new IllegalArgumentException(
                        "Este desporto ainda não tem tipos de prova definidos");
            return null;
        }
        if (requested == null || requested.isBlank()) {
            throw new IllegalArgumentException("Seleciona o tipo de prova para " + sport);
        }
        String type = requested.trim();
        if (!allowed.contains(type))
            throw new IllegalArgumentException(
                    "Tipo de prova inválido para a modalidade selecionada");
        return type;
    }

    /**
     * Field/freestyle event results have not yet been modeled; never allow
     * arbitrary WDL, football stats or teams to silently become a ranking.
     */
    static void validateEntries(String sport, String type,
            List<CommunityRoundEntryRequest> entries) {
        if ("STRENGTH_TRAINING".equals(type)) {
            validateStrengthTraining(sport, entries);
            return;
        }
        if (CommunityGroupRoutineResults.isGroupEvent(type)
                || CommunityBasketballResults.isBasketballEvent(type)
                || CommunityVolleyballResults.isVolleyballEvent(type)
                || CommunityTeamFinalResults.isTeamFinalEvent(type)) return;
        if (CommunityGolfStrokeResults.isGolfStrokeEvent(type)) {
            CommunityGolfStrokeResults.validate(sport, type, entries);
            return;
        }
        if (CommunityMuayThaiResults.isMuayThaiEvent(type)) {
            CommunityMuayThaiResults.validate(sport, type, entries);
            return;
        }
        if (CommunityKickboxingResults.isKickboxingEvent(type)) {
            CommunityKickboxingResults.validate(sport, type, entries);
            return;
        }
        if (CommunityBoxingResults.isBoxingEvent(type)) {
            CommunityBoxingResults.validate(sport, type, entries);
            return;
        }
        if (CommunityBjjResults.isBjjEvent(type)) {
            CommunityBjjResults.validate(sport, type, entries);
            return;
        }
        if (CommunityTaekwondoResults.isTaekwondoEvent(type)) {
            CommunityTaekwondoResults.validate(sport, type, entries);
            return;
        }
        if (CommunityKarateResults.isKarateEvent(type)) {
            CommunityKarateResults.validate(sport, type, entries);
            return;
        }
        if (CommunityJudoBoutResults.isJudoBout(type)) {
            CommunityJudoBoutResults.validate(sport, entries);
            return;
        }
        if ("CHESS_GAME".equals(type)) {
            validateChessGame(sport, entries);
            return;
        }
        boolean neutral = isNeutralEvent(sport, type);
        // Judging returns a winner for one Breaking battle, NOT a scalar score.
        // Generic season W/D/L already excludes neutral events (including this one).
        if ("BREAKING_BATTLE".equals(type)) {
            if (entries.size() != 2
                    || entries.stream().filter(e -> e.result() == CommunityMatchResult.WIN).count() != 1
                    || entries.stream().filter(e -> e.result() == CommunityMatchResult.LOSS).count() != 1) {
                throw new IllegalArgumentException(
                        "Uma batalha de Breaking exige dois participantes, um vencedor e um derrotado");
            }
        }
        for (CommunityRoundEntryRequest e : entries) {
            if ("BREAKING_BATTLE".equals(type)) {
                if (e.team() != null && !e.team().isBlank()
                        || e.goals() != null && e.goals() != 0
                        || e.assists() != null && e.assists() != 0
                        || e.mvp() || e.cleanSheet()
                        || e.performanceValue() != null
                        || e.waveScore1() != null || e.waveScore2() != null) {
                    throw new IllegalArgumentException(
                            "Breaking usa apenas a decisão da batalha, sem pontos ou estatísticas de futebol");
                }
                continue;
            }
            java.math.BigDecimal value = e.performanceValue();
            if (!neutral) {
                if (value != null || e.waveScore1() != null || e.waveScore2() != null)
                    throw new IllegalArgumentException(
                            "Esta modalidade não suporta estas marcas ou notas");
                if ("Parkour".equals(sport) && "PARKOUR_SPEED".equals(type)
                        && (e.result() != CommunityMatchResult.DRAW
                        || e.team() != null && !e.team().isBlank())) {
                    throw new IllegalArgumentException(
                            "Parkour Speed usa tempos e posições, não equipas nem vitórias de jogos");
                }
                continue;
            }
            if ("HEAT".equals(type) || "BODYBOARD_HEAT".equals(type)) {
                if (value != null)
                    throw new IllegalArgumentException("Baterias de ondas usam notas por onda");
                validateWave(e.waveScore1());
                validateWave(e.waveScore2());
            } else {
                if (e.waveScore1() != null || e.waveScore2() != null)
                    throw new IllegalArgumentException("Notas por onda só são permitidas nas baterias suportadas");
                if (value != null) {
                    int allowedDecimals = "ARTISTIC_SOLO_TECHNICAL".equals(type)
                            || "ARTISTIC_SOLO_FREE".equals(type) ? 4 : 3;
                    if (value.scale() > allowedDecimals
                            || value.precision() - value.scale() > 6 || value.signum() < 0)
                        throw new IllegalArgumentException(
                                "A marca ou nota deve ser positiva, até 3 casas decimais");
                    if ("FREESTYLE".equals(type) || "APPARATUS".equals(type)
                            || "SKATE_PARK".equals(type) || "PARKOUR_FREESTYLE".equals(type)) {
                        if (value.compareTo(new java.math.BigDecimal("100")) > 0)
                            throw new IllegalArgumentException(
                                    "A nota final registada deve estar entre 0 e 100");
                    } else if ("RHYTHMIC_APPARATUS".equals(type)
                            || "ARTISTIC_SOLO_TECHNICAL".equals(type)
                            || "ARTISTIC_SOLO_FREE".equals(type)) {
                        // Organizer-supplied final score. No invented federation cap or jury formula.
                        // The database only imposes a six-integer-digit technical limit.
                    } else if ("TRAMPOLINE_INDIVIDUAL".equals(type)) {
                        // Technical storage guard, NOT an invented federation maximum.
                        if (value.compareTo(new java.math.BigDecimal("9999.99")) > 0
                                || value.stripTrailingZeros().scale() > 2)
                            throw new IllegalArgumentException(
                                    "A nota final individual de Trampolim deve ter até duas casas decimais");
                    } else if ("SKATE_STREET".equals(type)) {
                        // Community-entered final score, not an automatic run/trick calculation.
                        if (value.compareTo(new java.math.BigDecimal("300")) > 0)
                            throw new IllegalArgumentException(
                                    "A nota final de Skate Street deve estar entre 0 e 300");
                    } else if (value.signum() <= 0) {
                        throw new IllegalArgumentException(
                                "A marca em metros deve ser superior a zero");
                    }
                }
            }
            if (e.result() != CommunityMatchResult.DRAW
                    || (e.team() != null && !e.team().isBlank())
                    || e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()) {
                throw new IllegalArgumentException(
                        "Este tipo de prova ainda só permite registar participantes, sem resultados calculados");
            }
        }
    }

    /**
     * Musculação is a non-competitive training activity. The legacy DRAW enum
     * is a storage-neutral placeholder, NEVER a drawn contest or league point.
     * Entry metrics are deliberately absent until a dedicated workout model exists.
     */
    private static void validateStrengthTraining(String sport,
            List<CommunityRoundEntryRequest> entries) {
        if (!"Musculação".equals(sport) || entries == null || entries.isEmpty())
            throw new IllegalArgumentException(
                    "Um treino de musculação exige pelo menos um participante");
        Set<String> usernames = new HashSet<>();
        for (CommunityRoundEntryRequest e : entries) {
            if (e == null || e.username() == null || e.username().isBlank()
                    || !usernames.add(e.username().trim().toLowerCase(Locale.ROOT))
                    || e.result() != CommunityMatchResult.DRAW
                    || e.team() != null && !e.team().isBlank()
                    || e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.performanceValue() != null
                    || e.waveScore1() != null || e.waveScore2() != null)
                throw new IllegalArgumentException(
                        "Musculação regista apenas participantes; sem vencedor, empate, notas ou métricas de outros desportos");
        }
    }

    /**
     * A played chess game has one White and one Black, with a reciprocal
     * 1-0, 0-1 or half-half result. Exceptional arbiter awards and forfeits
     * need a separate contract: do not misrepresent them as played games.
     */
    private static void validateChessGame(String sport,
            List<CommunityRoundEntryRequest> entries) {
        if (!"Xadrez".equals(sport) || entries.size() != 2)
            throw new IllegalArgumentException("Xadrez exige duas pessoas: Brancas e Pretas");
        CommunityRoundEntryRequest white = entries.stream()
                .filter(e -> "WHITE".equals(e.team())).findFirst().orElse(null);
        CommunityRoundEntryRequest black = entries.stream()
                .filter(e -> "BLACK".equals(e.team())).findFirst().orElse(null);
        if (white == null || black == null)
            throw new IllegalArgumentException("Seleciona exatamente um jogador por cor");
        boolean validResult =
                white.result() == CommunityMatchResult.WIN && black.result() == CommunityMatchResult.LOSS
                || white.result() == CommunityMatchResult.LOSS && black.result() == CommunityMatchResult.WIN
                || white.result() == CommunityMatchResult.DRAW && black.result() == CommunityMatchResult.DRAW;
        if (!validResult)
            throw new IllegalArgumentException("Resultado de Xadrez inválido: 1-0, 0-1 ou ½-½");
        for (CommunityRoundEntryRequest e : entries) {
            if (e.goals() != null && e.goals() != 0
                    || e.assists() != null && e.assists() != 0
                    || e.mvp() || e.cleanSheet()
                    || e.finishPosition() != null || e.elapsedSeconds() != null
                    || e.bestLapSeconds() != null || e.lapsCompleted() != null
                    || e.performanceValue() != null
                    || e.waveScore1() != null || e.waveScore2() != null) {
                throw new IllegalArgumentException(
                        "Xadrez apenas admite cor das peças e resultado; sem estatísticas de outras modalidades");
            }
        }
    }

    private static void validateWave(java.math.BigDecimal value) {
        if (value == null) return;
        if (value.signum() < 0 || value.scale() > 3
                || value.compareTo(java.math.BigDecimal.TEN) > 0)
            throw new IllegalArgumentException("Nota de onda: 0 a 10, até 3 casas decimais");
    }

    static boolean isRaceEvent(String sport, String eventType) {
        if (sport == null) return false;
        if ("Atletismo".equals(sport)) return "TRACK".equals(eventType);
        if ("Snowboard".equals(sport)) return "RACE".equals(eventType);
        if ("Parkour".equals(sport)) return "PARKOUR_SPEED".equals(eventType);
        if ("BMX".equals(sport)) return eventType == null || "RACE".equals(eventType);
        return CommunityRaceMetrics.isRaceSport(sport) && eventType == null;
    }

    static boolean supportsRaceChampionship(String sport) {
        return CommunityRaceMetrics.isRaceSport(sport)
                || "Atletismo".equals(sport) || "Snowboard".equals(sport);
    }

    static boolean isNeutralEvent(String sport, String eventType) {
        return eventType != null && !isRaceEvent(sport, eventType);
    }
}
