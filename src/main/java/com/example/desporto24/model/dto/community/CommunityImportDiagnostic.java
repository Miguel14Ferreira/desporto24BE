package com.example.desporto24.model.dto.community;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Additive, language-independent diagnostic metadata for historical imports.
 *
 * The existing errors/warnings arrays remain unchanged for older clients.
 * This adapter is intentionally strict: an unfamiliar legacy message is never
 * guessed to be a known code. Producers can emit structured diagnostics
 * directly once their legacy string responses have been migrated.
 */
public record CommunityImportDiagnostic(String code, Map<String, String> params) {

    private record Rule(String code, Pattern pattern, List<String> names) {
        Rule(String code, String regex, String... names) {
            this(code, Pattern.compile(regex), List.of(names));
        }

        CommunityImportDiagnostic match(String message) {
            Matcher matcher = pattern.matcher(message);
            if (!matcher.matches()) return null;
            Map<String, String> args = new LinkedHashMap<>();
            for (int i = 0; i < names.size(); i++) {
                args.put(names.get(i), matcher.group(i + 1));
            }
            return new CommunityImportDiagnostic(code, args);
        }
    }

    private static final List<Rule> RULES = List.of(
            new Rule("IMPORT_SHEET_MISSING", "A folha selecionada não existe"),
            new Rule("IMPORT_COLUMN_MISSING", "A coluna (\\d+) não existe na folha", "column"),
            new Rule("IMPORT_UNSUPPORTED_FIELD", "Campo não suportado: (.+)", "field"),
            new Rule("IMPORT_DUPLICATE_FIELD", "Campo repetido: (.+)", "field"),
            new Rule("IMPORT_PLAYER_COLUMN_REQUIRED", "Seleciona uma coluna para identificar o jogador"),
            new Rule("IMPORT_EMPTY_SHEET", "A folha não contém linhas de dados"),
            new Rule("IMPORT_DUPLICATE_MATCH",
                    "Linha (\\d+): confronto potencialmente repetido; verifica se é um jogo diferente antes de confirmar",
                    "row"),
            new Rule("IMPORT_DUPLICATE_ROUND_PLAYER",
                    "Linha (\\d+): jogador (.+) repetido na jornada (.+); será ignorado \\(mantida a primeira ocorrência\\)",
                    "row", "player", "round"),
            new Rule("IMPORT_DUPLICATE_STANDING",
                    "Linha (\\d+): (.+) repetido; mantém-se apenas a primeira ocorrência",
                    "row", "name"),
            new Rule("IMPORT_EMPTY_PLAYER", "Linha (\\d+): jogador vazio", "row"),
            new Rule("IMPORT_INTEGER_REQUIRED",
                    "Linha (\\d+): (\\w+) exige um número inteiro válido \\('([\\s\\S]*)'\\)",
                    "row", "field", "value"),
            new Rule("IMPORT_CONFLICTING_RESULTS",
                    "Linha (\\d+): os resultados V/E/D ou o campo RESULT são contraditórios", "row")
    );
    private static final Pattern SOURCE_ROW = Pattern.compile("^Linha (\\d+)(?::|$).*");

    public CommunityImportDiagnostic {
        params = params == null ? Map.of() : Map.copyOf(params);
    }

    public static CommunityImportDiagnostic fromLegacy(String text) {
        if (text == null || text.isBlank()) {
            return new CommunityImportDiagnostic("IMPORT_UNRECOGNIZED", Map.of());
        }
        String message = text.trim();
        for (Rule rule : RULES) {
            CommunityImportDiagnostic diagnostic = rule.match(message);
            if (diagnostic != null) return diagnostic;
        }
        Matcher sourceRow = SOURCE_ROW.matcher(message);
        return new CommunityImportDiagnostic("IMPORT_UNRECOGNIZED",
                sourceRow.matches() ? Map.of("row", sourceRow.group(1)) : Map.of());
    }

    public static List<CommunityImportDiagnostic> fromLegacyMessages(List<String> messages) {
        return messages == null ? List.of()
                : messages.stream().map(CommunityImportDiagnostic::fromLegacy).toList();
    }
}
