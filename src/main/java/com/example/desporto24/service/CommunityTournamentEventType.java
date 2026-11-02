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

    private static final java.util.Set<String> SWIM_DISCIPLINES = java.util.Set.of(
            "FREE_50", "FREE_100", "FREE_200", "BACK_100",
            "BREAST_100", "FLY_100", "IM_200");

    static String validateDiscipline(String sport, String eventType,
            String requested, TournamentScoringMode scoringMode) {
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
