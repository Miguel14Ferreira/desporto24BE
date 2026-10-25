package com.example.desporto24.model.dto.community;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CommunityImportDiagnosticTest {
    private final ObjectMapper json = new ObjectMapper();

    @Test
    void stableCodesKeepSourceRowAndDynamicValues() {
        assertEquals(new CommunityImportDiagnostic("IMPORT_DUPLICATE_MATCH", Map.of("row", "7")),
                CommunityImportDiagnostic.fromLegacy(
                        "Linha 7: confronto potencialmente repetido; verifica se é um jogo diferente antes de confirmar"));
        assertEquals(new CommunityImportDiagnostic("IMPORT_DUPLICATE_STANDING",
                        Map.of("row", "4", "name", "ÁGUIAS")),
                CommunityImportDiagnostic.fromLegacy(
                        "Linha 4: ÁGUIAS repetido; mantém-se apenas a primeira ocorrência"));
        assertEquals(new CommunityImportDiagnostic("IMPORT_DUPLICATE_ROUND_PLAYER",
                        Map.of("row", "12", "player", "Joana", "round", "2020/21")),
                CommunityImportDiagnostic.fromLegacy(
                        "Linha 12: jogador Joana repetido na jornada 2020/21; será ignorado (mantida a primeira ocorrência)"));
        assertEquals("IMPORT_INTEGER_REQUIRED", CommunityImportDiagnostic.fromLegacy(
                "Linha 3: GOALS exige um número inteiro válido ('N/A')").code());
    }

    @Test
    void importHttpValidationCodesPreserveOnlyExplicitSourceValues() {
        assertEquals("IMPORT_FILE_TOO_LARGE", CommunityImportDiagnostic.fromLegacy(
                "O ficheiro não pode ultrapassar 10 MB").code());
        assertEquals("IMPORT_FILE_REQUIRED", CommunityImportDiagnostic.fromLegacy(
                "Seleciona um ficheiro Excel").code());
        assertEquals("IMPORT_FILE_FORMAT_UNSUPPORTED", CommunityImportDiagnostic.fromLegacy(
                "Formatos suportados: .xlsx, .xls, .csv e .tsv").code());
        assertEquals(new CommunityImportDiagnostic("IMPORT_ROW_SCORE_INVALID", Map.of("row", "12")),
                CommunityImportDiagnostic.fromLegacy(
                        "Linha 12: resultado inválido (exemplo: 2-1 ou 2:1)"));
        assertEquals(new CommunityImportDiagnostic("IMPORT_SOURCE_DATE_INVALID", Map.of("date", "14/02/2020")),
                CommunityImportDiagnostic.fromLegacy("Não foi possível resolver a data: 14/02/2020"));
    }

    @Test
    void unrecognisedMessagesNeverGetFalseCodesOrLeakArbitraryDetail() {
        assertEquals(new CommunityImportDiagnostic("IMPORT_UNRECOGNIZED", Map.of("row", "9")),
                CommunityImportDiagnostic.fromLegacy("Linha 9: um erro novo do backend"));
        assertEquals(new CommunityImportDiagnostic("IMPORT_UNRECOGNIZED", Map.of()),
                CommunityImportDiagnostic.fromLegacy("Mensagem arbitrária"));
        assertEquals(new CommunityImportDiagnostic("IMPORT_UNRECOGNIZED", Map.of()),
                CommunityImportDiagnostic.fromLegacy(null));
        assertTrue(CommunityImportDiagnostic.fromLegacyMessages(null).isEmpty());
    }

    @Test
    void mappingValidationJsonKeepsLegacyFieldsAndAddsAlignedDetails() {
        var response = new CommunityImportMappingValidationResponse("Dados", false, 3, 2,
                List.of("Campo repetido: PLAYER"),
                List.of("Linha 5: jogador vazio"));
        JsonNode tree = json.valueToTree(response);
        assertEquals("Campo repetido: PLAYER", tree.path("errors").get(0).asText());
        assertEquals("Linha 5: jogador vazio", tree.path("warnings").get(0).asText());
        assertEquals("IMPORT_DUPLICATE_FIELD", tree.path("errorDetails").get(0).path("code").asText());
        assertEquals("PLAYER", tree.path("errorDetails").get(0).path("params").path("field").asText());
        assertEquals("IMPORT_EMPTY_PLAYER", tree.path("warningDetails").get(0).path("code").asText());
        assertEquals("5", tree.path("warningDetails").get(0).path("params").path("row").asText());
    }

    @Test
    void archivedPreviewJsonKeepsOriginalSourceRowsAndScores() {
        var matches = new CommunityHistoricalMatchPreviewResponse(
                "Dados", 2, 2, 1,
                List.of("Linha 3: confronto potencialmente repetido; verifica se é um jogo diferente antes de confirmar"),
                List.of(new CommunityHistoricalMatchPreviewResponse.MatchRow(
                        3, "Águias", "Lobos", 2, 1, "01/10/2020", "Meia-final", true)));
        JsonNode matchTree = json.valueToTree(matches);
        assertEquals("IMPORT_DUPLICATE_MATCH", matchTree.path("warningDetails").get(0).path("code").asText());
        assertEquals("01/10/2020", matchTree.path("rows").get(0).path("sourceDate").asText());
        assertEquals("Meia-final", matchTree.path("rows").get(0).path("sourceRound").asText());
        assertEquals(2, matchTree.path("rows").get(0).path("homeScore").asInt());

        var standings = new CommunityStandingsArchivePreviewResponse(
                "Dados", 2, 1, 1,
                List.of("Linha 4: ÁGUIAS repetido; mantém-se apenas a primeira ocorrência"),
                List.of(new CommunityStandingsArchivePreviewResponse.StandingRow(
                        1, "Águias", 10, 8, 1, 1, 25, 21, 9)));
        JsonNode standingTree = json.valueToTree(standings);
        assertEquals("IMPORT_DUPLICATE_STANDING",
                standingTree.path("warningDetails").get(0).path("code").asText());
        assertEquals(25, standingTree.path("rows").get(0).path("points").asInt());
        assertEquals("Águias", standingTree.path("rows").get(0).path("name").asText());
    }
}
