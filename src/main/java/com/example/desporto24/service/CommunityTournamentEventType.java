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
}
