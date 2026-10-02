package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.community.CommunityImportMappingRequest;
import com.example.desporto24.model.dto.community.CommunityImportMappingValidationResponse;
import com.example.desporto24.model.dto.community.CommunityImportPreviewResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommunityImportService {

    private static final int SAMPLE_ROWS = 5;
    private static final Set<String> PLAYER_HEADERS = Set.of("player", "jogador", "nome", "name", "participante", "atleta");
    private static final Set<String> DATE_HEADERS = Set.of("data", "date", "dia", "jornada", "gameweek", "round");
    private static final Set<String> GOALS_HEADERS = Set.of("g", "gol", "golo", "golos", "goal", "goals");
    private static final Set<String> ASSISTS_HEADERS = Set.of("a", "assist", "assists", "assistencia", "assistencias");
    private static final Set<String> POINTS_HEADERS = Set.of("p", "pts", "point", "points", "ponto", "pontos");
    private static final Set<String> MVP_HEADERS = Set.of("mvp", "motm", "manofthematch");
    private static final Set<String> RESULT_HEADERS = Set.of("resultado", "result", "wdl", "vde", "winloss");

    private final ProfileRepository profileRepository;
    private final CommunityMemberRepository memberRepository;

    public CommunityImportPreviewResponse preview(
            Long communityId,
            String username,
            MultipartFile file) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        requireAdmin(communityId, actor);
        validateFile(file);

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            DataFormatter formatter = new DataFormatter();
            List<CommunityImportPreviewResponse.SheetPreview> sheets = new ArrayList<>();
            for (Sheet sheet : workbook) {
                List<Row> rows = nonEmptyRows(sheet, formatter);
                List<String> headers = rows.isEmpty() ? List.of() : values(rows.getFirst(), formatter);
                List<List<String>> samples = rows.stream()
                        .skip(1)
                        .limit(SAMPLE_ROWS)
                        .map(row -> values(row, formatter))
                        .toList();

                sheets.add(new CommunityImportPreviewResponse.SheetPreview(
                        sheet.getSheetName(),
                        rows.size(),
                        detectStructure(rows, formatter),
                        headers,
                        samples,
                        suggestColumns(headers, samples)
                ));
            }
            return new CommunityImportPreviewResponse(file.getOriginalFilename(), sheets);
        } catch (IOException | RuntimeException exception) {
            throw new IllegalArgumentException("Não foi possível ler o ficheiro Excel", exception);
        }
    }

    public CommunityImportMappingValidationResponse validateMapping(
            Long communityId,
            String username,
            MultipartFile file,
            CommunityImportMappingRequest request) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        requireAdmin(communityId, actor);
        validateFile(file);

        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheet(request.sheetName());
            if (sheet == null) {
                return new CommunityImportMappingValidationResponse(
                        request.sheetName(), false, 0, 0,
                        List.of("A folha selecionada não existe"), List.of()
                );
            }

            DataFormatter formatter = new DataFormatter();
            List<Row> rows = nonEmptyRows(sheet, formatter);
            int dataRows = Math.max(rows.size() - 1, 0);
            int width = rows.isEmpty() ? 0 : values(rows.getFirst(), formatter).size();
            Set<String> allowedFields = Set.of(
                    "PLAYER", "DATE", "RESULT", "GOALS", "ASSISTS", "POINTS", "MVP", "IGNORE"
            );
            Set<String> seenFields = new java.util.HashSet<>();
            Integer playerColumn = null;
            List<CommunityImportMappingRequest.ColumnMapping> numericMappings = new ArrayList<>();

            for (CommunityImportMappingRequest.ColumnMapping mapping : request.columns()) {
                String field = mapping.field().trim().toUpperCase(Locale.ROOT);
                if (mapping.columnIndex() >= width) {
                    errors.add("A coluna " + mapping.columnIndex() + " não existe na folha");
                    continue;
                }
                if (!allowedFields.contains(field)) {
                    errors.add("Campo não suportado: " + mapping.field());
                } else if (!field.equals("IGNORE") && !seenFields.add(field)) {
                    errors.add("Campo repetido: " + field);
                } else {
                    if (field.equals("PLAYER")) playerColumn = mapping.columnIndex();
                    if (Set.of("GOALS", "ASSISTS", "POINTS", "MVP").contains(field)) {
                        numericMappings.add(mapping);
                    }
                }
            }

            if (playerColumn == null) {
                errors.add("Seleciona uma coluna para identificar o jogador");
            }
            if (dataRows == 0) {
                errors.add("A folha não contém linhas de dados");
            }

            int validRows = 0;
            if (errors.isEmpty()) {
                for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
                    List<String> rowValues = values(rows.get(rowIndex), formatter);
                    int excelRow = rows.get(rowIndex).getRowNum() + 1;
                    String player = valueAt(rowValues, playerColumn);
                    boolean rowValid = true;

                    if (player.isBlank()) {
                        warnings.add("Linha " + excelRow + ": jogador vazio");
                        rowValid = false;
                    }
                    for (CommunityImportMappingRequest.ColumnMapping mapping : numericMappings) {
                        String value = valueAt(rowValues, mapping.columnIndex());
                        if (!value.isBlank() && !isNumber(value)) {
                            warnings.add("Linha " + excelRow + ": " + mapping.field()
                                    + " não é numérico ('" + value + "')");
                            rowValid = false;
                        }
                    }
                    if (rowValid) validRows++;
                }
            }

            return new CommunityImportMappingValidationResponse(
                    request.sheetName(),
                    errors.isEmpty() && validRows > 0,
                    dataRows,
                    validRows,
                    errors,
                    warnings
            );
        } catch (IOException exception) {
            throw new IllegalArgumentException("Não foi possível ler o ficheiro Excel", exception);
        }
    }

    private String valueAt(List<String> values, int index) {
        return index >= 0 && index < values.size() ? values.get(index).trim() : "";
    }

    private boolean isNumber(String value) {
        try {
            Double.parseDouble(value.replace(',', '.'));
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private List<CommunityImportPreviewResponse.ColumnSuggestion> suggestColumns(
            List<String> headers,
            List<List<String>> samples) {
        List<CommunityImportPreviewResponse.ColumnSuggestion> suggestions = new ArrayList<>();
        for (int index = 0; index < headers.size(); index++) {
            String header = headers.get(index);
            String normalized = normalize(header);
            String field = "UNKNOWN";
            double confidence = 0.0;

            if (PLAYER_HEADERS.contains(normalized)) {
                field = "PLAYER";
                confidence = 0.95;
            } else if (DATE_HEADERS.contains(normalized)) {
                field = "DATE";
                confidence = 0.95;
            } else if (GOALS_HEADERS.contains(normalized)) {
                field = "GOALS";
                confidence = 0.90;
            } else if (ASSISTS_HEADERS.contains(normalized)) {
                field = "ASSISTS";
                confidence = 0.90;
            } else if (POINTS_HEADERS.contains(normalized)) {
                field = "POINTS";
                confidence = 0.90;
            } else if (MVP_HEADERS.contains(normalized)) {
                field = "MVP";
                confidence = 0.95;
            } else if (RESULT_HEADERS.contains(normalized)) {
                field = "RESULT";
                confidence = 0.90;
            } else {
                List<String> values = columnValues(samples, index);
                if (!values.isEmpty() && values.stream().allMatch(this::looksLikeDate)) {
                    field = "DATE";
                    confidence = 0.65;
                }
            }
            suggestions.add(new CommunityImportPreviewResponse.ColumnSuggestion(
                    index, header, field, confidence
            ));
        }
        return suggestions;
    }

    private List<String> columnValues(List<List<String>> rows, int index) {
        return rows.stream()
                .filter(row -> index < row.size())
                .map(row -> row.get(index).trim())
                .filter(value -> !value.isBlank())
                .toList();
    }

    private boolean looksLikeDate(String value) {
        return value.matches("\\d{1,2}[-/.]\\d{1,2}([-/ .]\\d{2,4})?")
                || value.matches("\\d{1,2}\\s+[\\p{L}.]+(\\s+\\d{2,4})?");
    }

    private String normalize(String value) {
        return Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "");
    }

    private List<Row> nonEmptyRows(Sheet sheet, DataFormatter formatter) {
        List<Row> rows = new ArrayList<>();
        for (Row row : sheet) {
            if (values(row, formatter).stream().anyMatch(value -> !value.isBlank())) {
                rows.add(row);
            }
        }
        return rows;
    }

    private List<String> values(Row row, DataFormatter formatter) {
        int lastCell = Math.max(row.getLastCellNum(), 0);
        List<String> values = new ArrayList<>(lastCell);
        for (int index = 0; index < lastCell; index++) {
            Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            values.add(cell == null ? "" : formatter.formatCellValue(cell).trim());
        }
        return values;
    }

    private String detectStructure(List<Row> rows, DataFormatter formatter) {
        if (rows.size() < 2) return "UNKNOWN";

        List<String> first = values(rows.getFirst(), formatter);
        long populatedHeaders = first.stream().filter(value -> !value.isBlank()).count();
        if (populatedHeaders < 2) return "SUMMARY";

        int width = first.size();
        long compatibleRows = rows.stream()
                .skip(1)
                .limit(20)
                .filter(row -> values(row, formatter).size() <= width + 1)
                .count();
        long checkedRows = Math.min(rows.size() - 1L, 20L);
        return checkedRows > 0 && compatibleRows * 100 / checkedRows >= 80
                ? "TABULAR"
                : "SUMMARY";
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Seleciona um ficheiro Excel");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase(Locale.ROOT).endsWith(".xlsx")) {
            throw new IllegalArgumentException("O ficheiro deve estar no formato .xlsx");
        }
    }

    private void requireAdmin(Long communityId, Profile actor) {
        CommunityMember membership = memberRepository
                .findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (membership.getRole() != CommunityRole.OWNER
                && membership.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas Owners e Admins podem importar histórico");
        }
    }
}
