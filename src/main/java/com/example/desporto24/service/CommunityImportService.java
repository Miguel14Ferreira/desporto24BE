package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityPlayer;
import com.example.desporto24.model.community.CommunitySeason;
import com.example.desporto24.model.community.CommunityRound;
import com.example.desporto24.model.community.CommunityRoundEntry;
import com.example.desporto24.model.dto.community.CommunityImportMappingRequest;
import com.example.desporto24.model.dto.community.CommunityImportMappingValidationResponse;
import com.example.desporto24.model.dto.community.CommunityImportNormalizedPreviewResponse;
import com.example.desporto24.model.dto.community.CommunityImportPreviewResponse;
import com.example.desporto24.model.dto.community.CommunityImportPlanResponse;
import com.example.desporto24.model.dto.community.CommunityImportPlayersResultResponse;
import com.example.desporto24.model.dto.community.CommunityImportRoundsPlanResponse;
import com.example.desporto24.model.dto.community.CommunityImportRoundsResultResponse;
import com.example.desporto24.model.dto.community.CommunityImportContextRequest;
import com.example.desporto24.model.dto.community.CommunityImportContextValidationResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.community.CommunitySeasonRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommunityImportService {

    private static final int SAMPLE_ROWS = 5;
    private static final long MAX_IMPORT_FILE_BYTES = 5L * 1024 * 1024;
    private static final int MAX_CSV_ROWS = 20_000;
    private static final int MAX_CSV_COLUMNS = 100;
    private static final Set<String> PLAYER_HEADERS = Set.of("player", "jogador", "nome", "name", "participante", "atleta");
    private static final Set<String> DATE_HEADERS = Set.of("data", "date", "dia", "jornada", "gameweek", "round");
    private static final Set<String> GOALS_HEADERS = Set.of("g", "gol", "golo", "golos", "goal", "goals");
    private static final Set<String> ASSISTS_HEADERS = Set.of("a", "assist", "assists", "assistencia", "assistencias");
    private static final Set<String> POINTS_HEADERS = Set.of("p", "pts", "point", "points", "ponto", "pontos");
    private static final Set<String> MVP_HEADERS = Set.of("mvp", "motm", "manofthematch");
    private static final Set<String> RESULT_HEADERS = Set.of("resultado", "result", "wdl", "vde", "winloss");
    private static final Set<String> WIN_HEADERS = Set.of("v", "w", "win", "wins", "vitoria", "vitorias");
    private static final Set<String> DRAW_HEADERS = Set.of("e", "draw", "draws", "empate", "empates");
    private static final Set<String> LOSS_HEADERS = Set.of("loss", "losses", "derrota", "derrotas");

    private final ProfileRepository profileRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final CommunityPlayerRepository communityPlayerRepository;
    private final CommunitySeasonRepository communitySeasonRepository;
    private final CommunityRoundRepository communityRoundRepository;
    private final CommunityRoundEntryRepository communityRoundEntryRepository;

    public CommunityImportContextValidationResponse validateContext(
            Long communityId,
            String username,
            CommunityImportContextRequest request) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        requireAdmin(communityId, actor);

        CommunitySeason season = communitySeasonRepository.findById(request.seasonId())
                .filter(candidate -> candidate.getCommunity().getId().equals(communityId))
                .orElseThrow(() -> new IllegalArgumentException("A época selecionada não pertence a esta comunidade"));

        return new CommunityImportContextValidationResponse(
                season.getId(),
                season.getName(),
                request.defaultYear(),
                true
        );
    }

    public CommunityImportPreviewResponse preview(
            Long communityId,
            String username,
            MultipartFile file) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado"));
        requireAdmin(communityId, actor);
        validateFile(file);

        try (Workbook workbook = openImportWorkbook(file)) {
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
        try (Workbook workbook = openImportWorkbook(file)) {
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
                    "PLAYER", "DATE", "RESULT", "WIN", "DRAW", "LOSS", "GOALS", "ASSISTS", "POINTS", "MVP", "IGNORE"
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
                    if (Set.of("WIN", "DRAW", "LOSS", "GOALS", "ASSISTS", "POINTS", "MVP").contains(field)) {
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

    public CommunityImportNormalizedPreviewResponse normalizedPreview(
            Long communityId,
            String username,
            MultipartFile file,
            CommunityImportMappingRequest request) {
        CommunityImportMappingValidationResponse validation =
                validateMapping(communityId, username, file, request);
        if (!validation.valid()) {
            throw new IllegalArgumentException("Corrige o mapeamento antes de continuar");
        }

        validateFile(file);
        try (Workbook workbook = openImportWorkbook(file)) {
            Sheet sheet = workbook.getSheet(request.sheetName());
            DataFormatter formatter = new DataFormatter();
            List<Row> sourceRows = nonEmptyRows(sheet, formatter);
            List<CommunityImportNormalizedPreviewResponse.NormalizedRow> normalizedRows =
                    new ArrayList<>();
            List<CommunityImportNormalizedPreviewResponse.SkippedRow> skippedRows =
                    new ArrayList<>();

            for (int rowIndex = 1; rowIndex < sourceRows.size(); rowIndex++) {
                Row row = sourceRows.get(rowIndex);
                List<String> rowValues = values(row, formatter);
                java.util.Map<String, String> mapped = new java.util.HashMap<>();
                for (CommunityImportMappingRequest.ColumnMapping mapping : request.columns()) {
                    String field = mapping.field().trim().toUpperCase(Locale.ROOT);
                    if (!field.equals("IGNORE")) {
                        mapped.put(field, valueAt(rowValues, mapping.columnIndex()));
                    }
                }

                int sourceRow = row.getRowNum() + 1;
                if (mapped.getOrDefault("PLAYER", "").isBlank()) {
                    skippedRows.add(new CommunityImportNormalizedPreviewResponse.SkippedRow(
                            sourceRow, "PLAYER_EMPTY"
                    ));
                    continue;
                }
                if (!numericValuesValid(mapped)) {
                    skippedRows.add(new CommunityImportNormalizedPreviewResponse.SkippedRow(
                            sourceRow, "INVALID_NUMERIC_VALUE"
                    ));
                    continue;
                }

                if (normalizedRows.size() < 50) {
                    normalizedRows.add(new CommunityImportNormalizedPreviewResponse.NormalizedRow(
                            sourceRow,
                            mapped.get("PLAYER"),
                            blankToNull(mapped.get("DATE")),
                            blankToNull(mapped.get("RESULT")),
                            numberOrNull(mapped.get("WIN")),
                            numberOrNull(mapped.get("DRAW")),
                            numberOrNull(mapped.get("LOSS")),
                            numberOrNull(mapped.get("GOALS")),
                            numberOrNull(mapped.get("ASSISTS")),
                            numberOrNull(mapped.get("POINTS")),
                            numberOrNull(mapped.get("MVP"))
                    ));
                }
            }

            int acceptedRows = validation.validRows();
            return new CommunityImportNormalizedPreviewResponse(
                    request.sheetName(),
                    acceptedRows,
                    validation.dataRows() - acceptedRows,
                    normalizedRows,
                    skippedRows.stream().limit(50).toList()
            );
        } catch (IOException exception) {
            throw new IllegalArgumentException("Não foi possível ler o ficheiro Excel", exception);
        }
    }

    public CommunityImportPlanResponse importPlan(
            Long communityId,
            String username,
            MultipartFile file,
            CommunityImportMappingRequest request) {
        CommunityImportNormalizedPreviewResponse preview =
                normalizedPreview(communityId, username, file, request);

        java.util.Map<String, String> players = new java.util.LinkedHashMap<>();
        try (Workbook workbook = openImportWorkbook(file)) {
            Sheet sheet = workbook.getSheet(request.sheetName());
            DataFormatter formatter = new DataFormatter();
            List<Row> sourceRows = nonEmptyRows(sheet, formatter);
            Integer playerColumn = request.columns().stream()
                    .filter(mapping -> mapping.field().trim().equalsIgnoreCase("PLAYER"))
                    .map(CommunityImportMappingRequest.ColumnMapping::columnIndex)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Seleciona uma coluna para identificar o jogador"));

            for (int rowIndex = 1; rowIndex < sourceRows.size(); rowIndex++) {
                List<String> rowValues = values(sourceRows.get(rowIndex), formatter);
                String displayName = valueAt(rowValues, playerColumn);
                if (!displayName.isBlank()) {
                    players.putIfAbsent(normalizePlayerName(displayName), displayName);
                }
            }
        } catch (IOException exception) {
            throw new IllegalArgumentException("Não foi possível ler o ficheiro Excel", exception);
        }

        List<CommunityImportPlanResponse.PlayerPlan> playerPlans = new ArrayList<>();
        int existingPlayers = 0;
        for (java.util.Map.Entry<String, String> player : players.entrySet()) {
            boolean exists = communityPlayerRepository
                    .findByCommunityIdAndNormalizedName(communityId, player.getKey())
                    .isPresent();
            if (exists) existingPlayers++;
            playerPlans.add(new CommunityImportPlanResponse.PlayerPlan(
                    player.getValue(),
                    player.getKey(),
                    exists ? "EXISTING" : "NEW"
            ));
        }

        return new CommunityImportPlanResponse(
                request.sheetName(),
                preview.acceptedRows(),
                preview.skippedRows(),
                playerPlans.size(),
                playerPlans.size() - existingPlayers,
                existingPlayers,
                playerPlans
        );
    }

    public CommunityImportRoundsPlanResponse roundsPlan(
            Long communityId,
            String username,
            MultipartFile file,
            CommunityImportMappingRequest request) {
        CommunityImportMappingValidationResponse validation =
                validateMapping(communityId, username, file, request);
        if (!validation.valid()) {
            throw new IllegalArgumentException("Corrige o mapeamento antes de continuar");
        }

        Integer dateColumn = mappedColumn(request, "DATE",
                "Seleciona uma coluna DATE para agrupar as jornadas");
        Integer playerColumn = mappedColumn(request, "PLAYER",
                "Seleciona uma coluna PLAYER");

        java.util.Map<String, RoundImportAccumulator> rounds = new java.util.LinkedHashMap<>();
        int rowsWithoutRound = 0;
        try (Workbook workbook = openImportWorkbook(file)) {
            Sheet sheet = workbook.getSheet(request.sheetName());
            DataFormatter formatter = new DataFormatter();
            List<Row> sourceRows = nonEmptyRows(sheet, formatter);

            for (int rowIndex = 1; rowIndex < sourceRows.size(); rowIndex++) {
                List<String> rowValues = values(sourceRows.get(rowIndex), formatter);
                String player = valueAt(rowValues, playerColumn);
                if (player.isBlank()) continue;

                java.util.Map<String, String> mapped = mappedValues(request, rowValues);
                if (!numericValuesValid(mapped)) continue;

                String sourceDate = valueAt(rowValues, dateColumn);
                if (sourceDate.isBlank()) {
                    rowsWithoutRound++;
                    continue;
                }

                RoundImportAccumulator round = rounds.computeIfAbsent(
                        sourceDate, ignored -> new RoundImportAccumulator());
                round.players.add(normalizePlayerName(player));
                round.addResult(mapped);
            }
        } catch (IOException exception) {
            throw new IllegalArgumentException("Não foi possível ler o ficheiro Excel", exception);
        }

        int roundsNeedingYear = 0;
        int invalidDates = 0;
        List<CommunityImportRoundsPlanResponse.RoundPlan> roundPlans = new ArrayList<>();
        for (java.util.Map.Entry<String, RoundImportAccumulator> entry : rounds.entrySet()) {
            ParsedImportDate parsedDate = parseImportDate(entry.getKey());
            if (parsedDate.needsYear()) roundsNeedingYear++;
            if (parsedDate.invalid()) invalidDates++;
            RoundImportAccumulator round = entry.getValue();
            roundPlans.add(new CommunityImportRoundsPlanResponse.RoundPlan(
                    entry.getKey(),
                    parsedDate.date(),
                    parsedDate.needsYear(),
                    parsedDate.invalid(),
                    false,
                    round.players.size(),
                    round.wins,
                    round.draws,
                    round.losses,
                    round.unresolvedResults
            ));
        }

        return new CommunityImportRoundsPlanResponse(
                request.sheetName(),
                validation.validRows(),
                roundPlans.size(),
                rowsWithoutRound,
                roundsNeedingYear,
                invalidDates,
                0,
                roundPlans
        );
    }

    public CommunityImportRoundsPlanResponse finalRoundsPlan(
            Long communityId,
            String username,
            MultipartFile file,
            CommunityImportMappingRequest mapping,
            CommunityImportContextRequest context) {
        validateContext(communityId, username, context);
        CommunityImportRoundsPlanResponse plan = roundsPlan(communityId, username, file, mapping);

        if (plan.invalidDates() > 0) {
            throw new IllegalArgumentException("Existem datas inválidas no ficheiro. Corrige-as antes de importar");
        }
        if (plan.roundsNeedingYear() > 0 && context.defaultYear() == null) {
            throw new IllegalArgumentException("Indica o ano das jornadas que não têm ano no ficheiro");
        }

        int duplicateRounds = 0;
        List<CommunityImportRoundsPlanResponse.RoundPlan> resolvedRounds = new ArrayList<>();
        for (CommunityImportRoundsPlanResponse.RoundPlan round : plan.rounds()) {
            String resolvedDate = round.parsedDate();
            if (round.needsYear()) {
                ParsedImportDate resolved = parseImportDateWithYear(round.sourceDate(), context.defaultYear());
                if (resolved.invalid() || resolved.date() == null) {
                    throw new IllegalArgumentException("Não foi possível resolver a data: " + round.sourceDate());
                }
                resolvedDate = resolved.date();
            }

            Instant playedAt = LocalDate.parse(resolvedDate)
                    .atStartOfDay(java.time.ZoneOffset.UTC)
                    .toInstant();
            boolean duplicate = communityRoundRepository.existsByCommunityIdAndSeasonIdAndPlayedAt(
                    communityId, context.seasonId(), playedAt);
            if (duplicate) duplicateRounds++;

            resolvedRounds.add(new CommunityImportRoundsPlanResponse.RoundPlan(
                    round.sourceDate(),
                    resolvedDate,
                    false,
                    false,
                    duplicate,
                    round.players(),
                    round.wins(),
                    round.draws(),
                    round.losses(),
                    round.unresolvedResults()
            ));
        }

        return new CommunityImportRoundsPlanResponse(
                plan.sheetName(),
                plan.acceptedRows(),
                plan.distinctRounds(),
                plan.rowsWithoutRound(),
                0,
                0,
                duplicateRounds,
                resolvedRounds
        );
    }

    private ParsedImportDate parseImportDateWithYear(String value, int year) {
        String trimmed = value.trim();
        if (trimmed.matches("\\d{1,2}[-/.]\\d{1,2}")) {
            String normalized = trimmed.replace('-', '/').replace('.', '/');
            try {
                return new ParsedImportDate(
                        LocalDate.parse(normalized + "/" + year,
                                DateTimeFormatter.ofPattern("d/M/uuuu")).toString(),
                        false,
                        false
                );
            } catch (DateTimeParseException exception) {
                return new ParsedImportDate(null, false, true);
            }
        }

        java.util.regex.Matcher monthName = java.util.regex.Pattern
                .compile("(?iu)^(\\d{1,2})\\s+([\\p{L}.]+)$")
                .matcher(trimmed);
        if (monthName.matches()) {
            Integer month = importMonth(monthName.group(2));
            if (month != null) {
                try {
                    return new ParsedImportDate(
                            LocalDate.of(year, month, Integer.parseInt(monthName.group(1))).toString(),
                            false,
                            false
                    );
                } catch (java.time.DateTimeException exception) {
                    return new ParsedImportDate(null, false, true);
                }
            }
        }
        return new ParsedImportDate(null, false, true);
    }

    @Transactional
    public CommunityImportRoundsResultResponse confirmRounds(
            Long communityId,
            String username,
            MultipartFile file,
            CommunityImportMappingRequest mapping,
            CommunityImportContextRequest context) {
        CommunityImportRoundsPlanResponse plan = finalRoundsPlan(
                communityId, username, file, mapping, context);
        if (plan.rounds().stream().anyMatch(round -> round.unresolvedResults() > 0)) {
            throw new IllegalArgumentException(
                    "Existem jogadores com resultado não resolvido. Corrige o mapeamento antes de importar");
        }

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));
        CommunitySeason season = communitySeasonRepository.findById(context.seasonId())
                .filter(candidate -> candidate.getCommunity().getId().equals(communityId))
                .orElseThrow(() -> new IllegalArgumentException("A época selecionada não pertence a esta comunidade"));

        java.util.Map<String, CommunityImportRoundsPlanResponse.RoundPlan> roundsBySource =
                plan.rounds().stream().collect(java.util.stream.Collectors.toMap(
                        CommunityImportRoundsPlanResponse.RoundPlan::sourceDate,
                        round -> round,
                        (first, ignored) -> first,
                        java.util.LinkedHashMap::new));

        int createdRounds = 0;
        int skippedDuplicates = 0;
        int createdEntries = 0;
        try (Workbook workbook = openImportWorkbook(file)) {
            Sheet sheet = workbook.getSheet(mapping.sheetName());
            DataFormatter formatter = new DataFormatter();
            List<Row> sourceRows = nonEmptyRows(sheet, formatter);
            Integer dateColumn = mappedColumn(mapping, "DATE", "Seleciona uma coluna DATE");
            Integer playerColumn = mappedColumn(mapping, "PLAYER", "Seleciona uma coluna PLAYER");

            java.util.Map<String, List<Row>> rowsByRound = new java.util.LinkedHashMap<>();
            for (int rowIndex = 1; rowIndex < sourceRows.size(); rowIndex++) {
                List<String> values = values(sourceRows.get(rowIndex), formatter);
                String player = valueAt(values, playerColumn);
                String sourceDate = valueAt(values, dateColumn);
                if (player.isBlank() || sourceDate.isBlank()) continue;
                java.util.Map<String, String> mapped = mappedValues(mapping, values);
                if (!numericValuesValid(mapped)) continue;
                rowsByRound.computeIfAbsent(sourceDate, ignored -> new ArrayList<>())
                        .add(sourceRows.get(rowIndex));
            }

            for (java.util.Map.Entry<String, CommunityImportRoundsPlanResponse.RoundPlan> item
                    : roundsBySource.entrySet()) {
                CommunityImportRoundsPlanResponse.RoundPlan roundPlan = item.getValue();
                if (roundPlan.duplicate()) {
                    skippedDuplicates++;
                    continue;
                }

                Instant playedAt = LocalDate.parse(roundPlan.parsedDate())
                        .atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
                CommunityRound round = communityRoundRepository.save(CommunityRound.builder()
                        .community(community)
                        .season(season)
                        .name("Import " + roundPlan.parsedDate())
                        .playedAt(playedAt)
                        .createdAt(Instant.now())
                        .build());
                createdRounds++;

                for (Row row : rowsByRound.getOrDefault(item.getKey(), List.of())) {
                    List<String> values = values(row, formatter);
                    String displayName = valueAt(values, playerColumn).trim();
                    String normalizedName = normalizePlayerName(displayName);
                    CommunityPlayer player = communityPlayerRepository
                            .findByCommunityIdAndNormalizedName(communityId, normalizedName)
                            .orElseGet(() -> communityPlayerRepository.save(CommunityPlayer.builder()
                                    .community(community)
                                    .displayName(displayName)
                                    .normalizedName(normalizedName)
                                    .createdAt(Instant.now())
                                    .build()));

                    java.util.Map<String, String> mapped = mappedValues(mapping, values);
                    CommunityMatchResult result = importedResult(mapped);
                    communityRoundEntryRepository.save(CommunityRoundEntry.builder()
                            .round(round)
                            .communityPlayer(player)
                            .profile(player.getProfile())
                            .result(result)
                            .goals(importedInt(mapped.get("GOALS")))
                            .assists(importedInt(mapped.get("ASSISTS")))
                            .mvp(importedInt(mapped.get("MVP")) > 0)
                            .cleanSheet(false)
                            .build());
                    createdEntries++;
                }
            }
        } catch (IOException exception) {
            throw new IllegalArgumentException("Não foi possível ler o ficheiro Excel", exception);
        }

        return new CommunityImportRoundsResultResponse(
                createdRounds, skippedDuplicates, createdEntries);
    }

    private CommunityMatchResult importedResult(java.util.Map<String, String> mapped) {
        if (RoundImportAccumulator.positive(mapped.get("WIN"))) return CommunityMatchResult.WIN;
        if (RoundImportAccumulator.positive(mapped.get("DRAW"))) return CommunityMatchResult.DRAW;
        if (RoundImportAccumulator.positive(mapped.get("LOSS"))) return CommunityMatchResult.LOSS;
        String result = mapped.get("RESULT");
        if (result != null) {
            return switch (result.trim().toUpperCase(Locale.ROOT)) {
                case "W", "WIN", "V", "VITORIA", "VITÓRIA" -> CommunityMatchResult.WIN;
                case "DRAW", "E", "EMPATE" -> CommunityMatchResult.DRAW;
                case "L", "LOSS", "DERROTA" -> CommunityMatchResult.LOSS;
                default -> throw new IllegalArgumentException("Resultado histórico não reconhecido: " + result);
            };
        }
        throw new IllegalArgumentException("Resultado histórico em falta");
    }

    private int importedInt(String value) {
        if (value == null || value.isBlank()) return 0;
        return (int) Math.round(Double.parseDouble(value.replace(',', '.')));
    }

    private Integer mappedColumn(
            CommunityImportMappingRequest request,
            String field,
            String error) {
        return request.columns().stream()
                .filter(mapping -> mapping.field().trim().equalsIgnoreCase(field))
                .map(CommunityImportMappingRequest.ColumnMapping::columnIndex)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(error));
    }

    private java.util.Map<String, String> mappedValues(
            CommunityImportMappingRequest request,
            List<String> rowValues) {
        java.util.Map<String, String> mapped = new java.util.HashMap<>();
        for (CommunityImportMappingRequest.ColumnMapping mapping : request.columns()) {
            String field = mapping.field().trim().toUpperCase(Locale.ROOT);
            if (!field.equals("IGNORE")) {
                mapped.put(field, valueAt(rowValues, mapping.columnIndex()));
            }
        }
        return mapped;
    }

    private ParsedImportDate parseImportDate(String value) {
        String trimmed = value.trim();
        for (DateTimeFormatter formatter : List.of(
                DateTimeFormatter.ofPattern("d/M/uuuu"),
                DateTimeFormatter.ofPattern("d-M-uuuu"),
                DateTimeFormatter.ofPattern("d.M.uuuu"),
                DateTimeFormatter.ISO_LOCAL_DATE)) {
            try {
                return new ParsedImportDate(LocalDate.parse(trimmed, formatter).toString(), false, false);
            } catch (DateTimeParseException ignored) {
                // Try the next supported format.
            }
        }

        if (trimmed.matches("\\d{1,2}[-/.]\\d{1,2}")) {
            return validDayMonth(trimmed) ? new ParsedImportDate(null, true, false)
                    : new ParsedImportDate(null, false, true);
        }

        java.util.regex.Matcher monthName = java.util.regex.Pattern
                .compile("(?iu)^(\\d{1,2})\\s+([\\p{L}.]+)$")
                .matcher(trimmed);
        if (monthName.matches()) {
            Integer month = importMonth(monthName.group(2));
            if (month != null && validDayMonth(monthName.group(1) + "/" + month)) {
                return new ParsedImportDate(null, true, false);
            }
            return new ParsedImportDate(null, false, true);
        }

        return new ParsedImportDate(null, false, true);
    }

    private boolean validDayMonth(String value) {
        String normalized = value.replace('-', '/').replace('.', '/');
        try {
            LocalDate.parse(normalized + "/2000", DateTimeFormatter.ofPattern("d/M/uuuu"));
            return true;
        } catch (DateTimeParseException exception) {
            return false;
        }
    }

    private Integer importMonth(String value) {
        String month = Normalizer.normalize(value.replace(".", "").trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
        return switch (month) {
            case "jan", "janeiro", "january" -> 1;
            case "fev", "feb", "fevereiro", "february" -> 2;
            case "mar", "marco", "march" -> 3;
            case "abr", "apr", "abril", "april" -> 4;
            case "mai", "may", "maio" -> 5;
            case "jun", "junho", "june" -> 6;
            case "jul", "julho", "july" -> 7;
            case "ago", "aug", "agosto", "august" -> 8;
            case "set", "sep", "sept", "setembro", "september" -> 9;
            case "out", "oct", "outubro", "october" -> 10;
            case "nov", "novembro", "november" -> 11;
            case "dez", "dec", "dezembro", "december" -> 12;
            default -> null;
        };
    }

    private record ParsedImportDate(String date, boolean needsYear, boolean invalid) {}

    private static final class RoundImportAccumulator {
        private final java.util.Set<String> players = new java.util.LinkedHashSet<>();
        private int wins;
        private int draws;
        private int losses;
        private int unresolvedResults;

        private void addResult(java.util.Map<String, String> mapped) {
            if (positive(mapped.get("WIN"))) {
                wins++;
                return;
            }
            if (positive(mapped.get("DRAW"))) {
                draws++;
                return;
            }
            if (positive(mapped.get("LOSS"))) {
                losses++;
                return;
            }

            String result = mapped.get("RESULT");
            if (result == null || result.isBlank()) {
                unresolvedResults++;
                return;
            }

            switch (result.trim().toUpperCase(Locale.ROOT)) {
                case "W", "WIN", "V", "VITORIA", "VITÓRIA" -> wins++;
                case "DRAW", "E", "EMPATE" -> draws++;
                case "L", "LOSS", "DERROTA" -> losses++;
                default -> unresolvedResults++;
            }
        }

        private static boolean positive(String value) {
            if (value == null || value.isBlank()) return false;
            try {
                return Double.parseDouble(value.replace(',', '.')) > 0;
            } catch (NumberFormatException exception) {
                return false;
            }
        }
    }

    @Transactional
    public CommunityImportPlayersResultResponse confirmPlayers(
            Long communityId,
            String username,
            MultipartFile file,
            CommunityImportMappingRequest request) {
        CommunityImportPlanResponse plan = importPlan(communityId, username, file, request);
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));

        int createdPlayers = 0;
        for (CommunityImportPlanResponse.PlayerPlan player : plan.players()) {
            if (communityPlayerRepository
                    .findByCommunityIdAndNormalizedName(communityId, player.normalizedName())
                    .isPresent()) {
                continue;
            }

            communityPlayerRepository.save(CommunityPlayer.builder()
                    .community(community)
                    .displayName(player.displayName())
                    .normalizedName(player.normalizedName())
                    .createdAt(Instant.now())
                    .build());
            createdPlayers++;
        }

        return new CommunityImportPlayersResultResponse(
                plan.distinctPlayers(),
                createdPlayers,
                plan.distinctPlayers() - createdPlayers
        );
    }

    private String normalizePlayerName(String value) {
        return Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }

    private boolean numericValuesValid(java.util.Map<String, String> mapped) {
        for (String field : Set.of("WIN", "DRAW", "LOSS", "GOALS", "ASSISTS", "POINTS", "MVP")) {
            String value = mapped.get(field);
            if (value != null && !value.isBlank() && !isNumber(value)) return false;
        }
        return true;
    }

    private Double numberOrNull(String value) {
        if (value == null || value.isBlank()) return null;
        return Double.parseDouble(value.replace(',', '.'));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
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
            } else if (WIN_HEADERS.contains(normalized)) {
                field = "WIN";
                confidence = 0.90;
            } else if (DRAW_HEADERS.contains(normalized)) {
                field = "DRAW";
                confidence = 0.90;
            } else if (LOSS_HEADERS.contains(normalized)) {
                field = "LOSS";
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

    private Workbook openImportWorkbook(MultipartFile file) throws IOException {
        if (!file.getOriginalFilename().toLowerCase(Locale.ROOT).endsWith(".csv")) {
            return new XSSFWorkbook(file.getInputStream());
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                file.getInputStream(), StandardCharsets.UTF_8))) {
            reader.mark(65_536);
            String firstLine = reader.readLine();
            reader.reset();
            if (firstLine == null) {
                throw new IllegalArgumentException("O ficheiro CSV não contém dados");
            }
            // Portuguese Excel exports often use semicolon; Google Sheets commonly uses comma.
            char separator = firstLine.chars().filter(ch -> ch == ';').count()
                    > firstLine.chars().filter(ch -> ch == ',').count() ? ';' : ',';
            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setDelimiter(separator)
                    .setIgnoreEmptyLines(true)
                    .build();
            try (CSVParser parser = format.parse(reader)) {
                Sheet sheet = workbook.createSheet("CSV");
                int rowIndex = 0;
                for (CSVRecord record : parser) {
                    if (rowIndex >= MAX_CSV_ROWS || record.size() > MAX_CSV_COLUMNS) {
                        throw new IllegalArgumentException("O CSV ultrapassa o limite de linhas ou colunas");
                    }
                    Row row = sheet.createRow(rowIndex++);
                    for (int index = 0; index < record.size(); index++) {
                        String value = record.get(index);
                        if (rowIndex == 1 && index == 0 && value.startsWith("\uFEFF")) {
                            value = value.substring(1);
                        }
                        // String cells: input cannot become an executable spreadsheet formula.
                        row.createCell(index, CellType.STRING).setCellValue(value);
                    }
                }
            }
            return workbook;
        } catch (IOException | RuntimeException exception) {
            try { workbook.close(); } catch (IOException ignored) { }
            throw exception;
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Seleciona um ficheiro Excel ou CSV");
        }
        if (file.getSize() > MAX_IMPORT_FILE_BYTES) {
            throw new IllegalArgumentException("O ficheiro não pode ultrapassar 5 MB");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.toLowerCase(Locale.ROOT).endsWith(".xlsx")
                && !fileName.toLowerCase(Locale.ROOT).endsWith(".csv"))) {
            throw new IllegalArgumentException("O ficheiro deve estar no formato .xlsx ou .csv");
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
