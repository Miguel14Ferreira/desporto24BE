package com.example.desporto24.service;

import com.example.desporto24.enumeration.TournamentEntryType;
import com.example.desporto24.enumeration.TournamentScoringMode;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * Explicit, opt-in metadata for event types whose current tournament result
 * engine is known to be compatible. This is not a federation rulebook.
 * Legacy tournaments and API clients keep eventType=null.
 */
final class CommunityTournamentEventType {
    private CommunityTournamentEventType() {}

    private static final Map<String, String> POSITION_RACES = Map.of(
            "BMX", "RACE",
            "Ciclismo de Estrada", "ROAD_RACE",
            "Ciclismo de Pista", "TRACK_SCRATCH"
    );

    static String validate(String sport, String requested, TournamentEntryType entryType,
                           TournamentScoringMode scoringMode) {
        if (scoringMode == TournamentScoringMode.GOLF_GROSS_STROKES) {
            if ("Golfe".equals(sport) && entryType == TournamentEntryType.INDIVIDUAL
                    && CommunityGolfStrokeResults.isGolfStrokeEvent(requested)) return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Golfe exige stroke play individual de 9 ou 18 buracos");
        }
        if (scoringMode == TournamentScoringMode.ROWING_BEST_TIME) {
            if ("Remo".equals(sport) && entryType == TournamentEntryType.INDIVIDUAL
                    && "ROWING_SINGLE".equals(requested)) return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Remo exige prova individual de skiff 1x cronometrada");
        }
        if (scoringMode == TournamentScoringMode.CANOE_BEST_TIME) {
            if ("Canoagem Velocidade".equals(sport) && entryType == TournamentEntryType.INDIVIDUAL
                    && "CANOE_SPRINT".equals(requested)) return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Canoagem Velocidade exige uma prova individual K1 cronometrada");
        }
        if (scoringMode == TournamentScoringMode.RELAY_BEST_TIME) {
            if ("Atletismo".equals(sport) && entryType == TournamentEntryType.TEAM
                    && "TRACK_RELAY".equals(requested)) return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "As estafetas exigem equipas e tipo de prova TRACK_RELAY");
        }
        if (scoringMode == TournamentScoringMode.TRACK_BEST_TIME) {
            if ("Atletismo".equals(sport) && entryType == TournamentEntryType.INDIVIDUAL
                    && "TRACK".equals(requested)) return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Seleciona uma prova individual de atletismo de pista");
        }
        if (scoringMode == TournamentScoringMode.FIELD_BEST_MARK) {
            if ("Atletismo".equals(sport) && entryType == TournamentEntryType.INDIVIDUAL
                    && ("JUMP".equals(requested) || "THROW".equals(requested))) return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Seleciona uma prova individual de saltos ou lançamentos");
        }
        if (scoringMode == TournamentScoringMode.SWIM_BEST_TIME) {
            if ("Natação".equals(sport) && entryType == TournamentEntryType.INDIVIDUAL
                    && "SWIM_LCM".equals(requested)) return "SWIM_LCM";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Seleciona uma prova individual de natação em piscina de 50 metros");
        }
        if (requested == null) return null;
        final String type = requested.trim();
        if (type.isEmpty() || type.length() > 64)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de prova inválido");

        if (entryType == TournamentEntryType.INDIVIDUAL
                && scoringMode == TournamentScoringMode.RACE_POINTS
                && type.equals(POSITION_RACES.get(sport))) return type;
        if ("Xadrez".equals(sport) && "CHESS_GAME".equals(type)
                && entryType == TournamentEntryType.INDIVIDUAL
                && scoringMode == TournamentScoringMode.CHESS_HALF_POINTS) return type;

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Este tipo de prova ainda não tem regras de torneio compatíveis com a modalidade");
    }

    private static final java.util.Set<String> TRACK_DISCIPLINES = java.util.Set.of(
            "TRACK_100","TRACK_200","TRACK_400","TRACK_800","TRACK_1500",
            "TRACK_5000","TRACK_10000","TRACK_HURDLES_100","TRACK_HURDLES_110",
            "TRACK_HURDLES_400","TRACK_STEEPLE_3000");

    private static final java.util.Set<String> CANOE_DISCIPLINES =
            java.util.Set.of("K1_200", "K1_500", "K1_1000");
    private static final java.util.Set<String> ROWING_DISCIPLINES =
            java.util.Set.of("SINGLE_500", "SINGLE_1000", "SINGLE_2000");

    private static final java.util.Set<String> SWIM_DISCIPLINES = java.util.Set.of(
            "FREE_50", "FREE_100", "FREE_200", "BACK_100",
            "BREAST_100", "FLY_100", "IM_200");

    private static final java.util.Map<String, java.util.Set<String>> FIELD_DISCIPLINES =
            java.util.Map.of(
                    "JUMP", java.util.Set.of("LONG_JUMP","TRIPLE_JUMP","HIGH_JUMP","POLE_VAULT"),
                    "THROW", java.util.Set.of("SHOT_PUT","DISCUS","JAVELIN","HAMMER"));

    static String validateDiscipline(String sport, String eventType,
            String requested, TournamentScoringMode scoringMode) {
        if (scoringMode == TournamentScoringMode.GOLF_GROSS_STROKES) {
            if ("Golfe".equals(sport) && CommunityGolfStrokeResults.isGolfStrokeEvent(eventType)
                    && requested == null) return null;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Golfe stroke play não admite disciplina adicional");
        }
        if (scoringMode == TournamentScoringMode.ROWING_BEST_TIME) {
            if ("Remo".equals(sport) && "ROWING_SINGLE".equals(eventType)
                    && requested != null && ROWING_DISCIPLINES.contains(requested)) return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Escolhe skiff 1x 500 m, 1000 m ou 2000 m, sem misturar distâncias");
        }
        if (scoringMode == TournamentScoringMode.CANOE_BEST_TIME) {
            if ("Canoagem Velocidade".equals(sport) && "CANOE_SPRINT".equals(eventType)
                    && requested != null && CANOE_DISCIPLINES.contains(requested)) return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Escolhe K1 200 m, K1 500 m ou K1 1000 m, sem misturar provas");
        }
        if (scoringMode == TournamentScoringMode.RELAY_BEST_TIME) {
            if ("Atletismo".equals(sport) && "TRACK_RELAY".equals(eventType)
                    && ("RELAY_4X100".equals(requested) || "RELAY_4X400".equals(requested)))
                return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Seleciona 4×100 m ou 4×400 m, sem misturar provas");
        }
        if (scoringMode == TournamentScoringMode.TRACK_BEST_TIME) {
            if ("Atletismo".equals(sport) && "TRACK".equals(eventType)
                    && requested != null && TRACK_DISCIPLINES.contains(requested)) return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Seleciona uma das onze provas individuais de pista suportadas");
        }
        if (scoringMode == TournamentScoringMode.FIELD_BEST_MARK) {
            if ("Atletismo".equals(sport)
                    && FIELD_DISCIPLINES.getOrDefault(eventType, java.util.Set.of()).contains(requested))
                return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Escolhe uma disciplina válida de salto ou lançamento");
        }
        if (scoringMode == TournamentScoringMode.SWIM_BEST_TIME) {
            if ("Natação".equals(sport) && "SWIM_LCM".equals(eventType)
                    && requested != null && SWIM_DISCIPLINES.contains(requested)) return requested;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Seleciona uma das sete disciplinas de natação disponíveis");
        }
        if (requested != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Este campeonato não suporta essa disciplina de resultados");
        return null;
    }
}
