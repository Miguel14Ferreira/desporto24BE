package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.*;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.Normalizer;
import java.time.Instant;
import java.util.*;

/**
 * Historical ranking snapshots are separate from current community standings.
 * We intentionally never generate fake rounds or override points from source data.
 */
@Service
@RequiredArgsConstructor
public class CommunityStandingsArchiveService {
    private static final int MAX_ROWS = 2_000;
    private static final int MAX_METRIC = 1_000_000;
    private final ProfileRepository profileRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final CommunitySeasonRepository seasonRepository;
    private final CommunityStandingsArchiveRepository archiveRepository;
    private final CommunityStandingsArchiveRowRepository rowRepository;

    public CommunityStandingsArchivePreviewResponse preview(Long communityId, String username,
            MultipartFile file, CommunityImportMappingRequest mapping) {
        requireManager(communityId, username);
        return parse(file, mapping);
    }

    @Transactional
    public CommunityStandingsArchiveResponse confirm(Long communityId, String username,
            MultipartFile file, CommunityImportMappingRequest mapping,
            CommunityStandingsArchiveConfirmRequest context) {
        requireManager(communityId, username);
        if (context == null || context.title() == null
                || context.title().trim().length() < 2
                || context.title().trim().length() > 120) {
            throw new IllegalArgumentException("Indica um título entre 2 e 120 caracteres");
        }
        String entryType = context.entryType() == null ? "" :
                context.entryType().trim().toUpperCase(Locale.ROOT);
        if (!Set.of("INDIVIDUAL", "TEAM").contains(entryType)) {
            throw new IllegalArgumentException("Escolhe classificação individual ou por equipas");
        }

        CommunityStandingsArchivePreviewResponse plan = parse(file, mapping);
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));
        CommunitySeason season = null;
        if (context.seasonId() != null) {
            season = seasonRepository.findById(context.seasonId())
                    .filter(item -> item.getCommunity().getId().equals(communityId))
                    .orElseThrow(() -> new IllegalArgumentException(
                            "A época histórica selecionada não pertence a esta comunidade"));
        }

        CommunityStandingsArchive archive = archiveRepository.save(CommunityStandingsArchive.builder()
                .community(community)
                .season(season)
                .title(context.title().trim())
                .entryType(entryType)
                .sourceFilename(file.getOriginalFilename())
                .createdByUsername(username)
                .createdAt(Instant.now())
                .build());

        List<CommunityStandingsArchiveRow> rows = plan.rows().stream().map(row ->
                CommunityStandingsArchiveRow.builder()
                        .archive(archive)
                        .position(row.position())
                        .displayName(row.name())
                        .normalizedName(normalizeName(row.name()))
                        .played(row.played())
                        .wins(row.wins())
                        .draws(row.draws())
                        .losses(row.losses())
                        .points(row.points())
                        .goalsFor(row.goalsFor())
                        .goalsAgainst(row.goalsAgainst())
                        .build()).toList();
        rowRepository.saveAll(rows);
        return response(archive, plan.rows());
    }

    @Transactional(readOnly = true)
    public List<CommunityStandingsArchiveSummaryResponse> list(Long communityId, String username) {
        requireMember(communityId, username);
        return archiveRepository.findByCommunityIdOrderByCreatedAtDescIdDesc(
                        communityId, PageRequest.of(0, 50))
                .stream().map(archive -> new CommunityStandingsArchiveSummaryResponse(
                        archive.getId(), archive.getTitle(), archive.getEntryType(),
                        archive.getSeason() == null ? null : archive.getSeason().getId(),
                        archive.getSourceFilename(), archive.getCreatedAt()
                )).toList();
    }

    @Transactional(readOnly = true)
    public CommunityStandingsArchiveResponse detail(Long communityId, Long archiveId, String username) {
        requireMember(communityId, username);
        CommunityStandingsArchive archive = archiveRepository.findByIdAndCommunityId(archiveId, communityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Classificação histórica não encontrada"));
        List<CommunityStandingsArchivePreviewResponse.StandingRow> rows =
                rowRepository.findByArchiveIdOrderByPositionAscIdAsc(archiveId)
                        .stream().map(row -> new CommunityStandingsArchivePreviewResponse.StandingRow(
                                row.getPosition(), row.getDisplayName(), row.getPlayed(),
                                row.getWins(), row.getDraws(), row.getLosses(), row.getPoints(),
                                row.getGoalsFor(), row.getGoalsAgainst()
                        )).toList();
        return response(archive, rows);
    }

    private CommunityStandingsArchiveResponse response(CommunityStandingsArchive archive,
            List<CommunityStandingsArchivePreviewResponse.StandingRow> rows) {
        return new CommunityStandingsArchiveResponse(
                archive.getId(), archive.getTitle(), archive.getEntryType(),
                archive.getSeason() == null ? null : archive.getSeason().getId(),
                archive.getSourceFilename(), archive.getCreatedAt(), rows
        );
    }

    private CommunityStandingsArchivePreviewResponse parse(MultipartFile file,
            CommunityImportMappingRequest mapping) {
        validateFile(file);
        if (mapping == null || mapping.sheetName() == null
                || mapping.columns() == null || mapping.columns().isEmpty()) {
            throw new IllegalArgumentException("Seleciona uma folha e mapeia as colunas da classificação");
        }
        Map<String, Integer> columns = new HashMap<>();
        Set<Integer> usedIndexes = new HashSet<>();
        for (CommunityImportMappingRequest.ColumnMapping column : mapping.columns()) {
            if (column == null || column.columnIndex() < 0 || column.columnIndex() >= 128
                    || column.field() == null) {
                throw new IllegalArgumentException("Mapeamento de colunas inválido");
            }
            String field = canonicalField(column.field());
            if (field.equals("IGNORE")) continue;
            if (!usedIndexes.add(column.columnIndex()) || columns.putIfAbsent(field, column.columnIndex()) != null) {
                throw new IllegalArgumentException("Coluna ou campo repetido no mapeamento");
            }
        }
        if (!columns.containsKey("NAME") || !columns.containsKey("POINTS")) {
            throw new IllegalArgumentException("Mapeia o jogador/equipa e os pontos originais da classificação");
        }

        try (Workbook workbook = CommunityImportWorkbookReader.open(file)) {
            Sheet sheet = workbook.getSheet(mapping.sheetName());
            if (sheet == null) throw new IllegalArgumentException("A folha escolhida não existe");
            DataFormatter formatter = new DataFormatter();
            List<Row> source = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getLastCellNum() > 128) {
                    throw new IllegalArgumentException("A folha ultrapassa 128 colunas");
                }
                boolean populated = false;
                for (Cell cell : row) {
                    if (!formatter.formatCellValue(cell).trim().isBlank()) {
                        populated = true;
                        break;
                    }
                }
                if (populated) {
                    source.add(row);
                    if (source.size() > MAX_ROWS + 1) {
                        throw new IllegalArgumentException("Uma classificação suporta até 2 000 linhas");
                    }
                }
            }
            if (source.size() < 2) throw new IllegalArgumentException("A folha não tem linhas de classificação");

            int width = Math.max(0, source.getFirst().getLastCellNum());
            if (columns.values().stream().anyMatch(index -> index >= width)) {
                throw new IllegalArgumentException("Uma coluna escolhida não existe no cabeçalho");
            }

            List<CommunityStandingsArchivePreviewResponse.StandingRow> result = new ArrayList<>();
            List<String> warnings = new ArrayList<>();
            Set<String> seenNames = new HashSet<>();
            int duplicated = 0;
            for (int index = 1; index < source.size(); index++) {
                Row row = source.get(index);
                String name = cell(row, columns.get("NAME"), formatter).trim();
                int sourceRow = row.getRowNum() + 1;
                if (name.isBlank() || name.length() > 120) {
                    throw new IllegalArgumentException("Linha " + sourceRow
                            + ": jogador/equipa vazio ou com mais de 120 caracteres");
                }
                String normalized = normalizeName(name);
                if (!seenNames.add(normalized)) {
                    duplicated++;
                    warnings.add("Linha " + sourceRow + ": " + name
                            + " repetido; mantém-se apenas a primeira ocorrência");
                    continue;
                }
                Integer rank = metric(row, columns.get("POSITION"), formatter, "Posição", sourceRow, false);
                int position = rank == null ? result.size() + 1 : rank;
                if (position < 1) throw new IllegalArgumentException("Linha " + sourceRow + ": posição inválida");
                Integer points = metric(row, columns.get("POINTS"), formatter, "Pontos", sourceRow, true);
                if (points == null) {
                    throw new IllegalArgumentException("Linha " + sourceRow + ": os pontos são obrigatórios");
                }
                result.add(new CommunityStandingsArchivePreviewResponse.StandingRow(
                        position, name,
                        metric(row, columns.get("PLAYED"), formatter, "Jogos", sourceRow, false),
                        metric(row, columns.get("WINS"), formatter, "Vitórias", sourceRow, false),
                        metric(row, columns.get("DRAWS"), formatter, "Empates", sourceRow, false),
                        metric(row, columns.get("LOSSES"), formatter, "Derrotas", sourceRow, false),
                        points,
                        metric(row, columns.get("GOALS_FOR"), formatter, "Marcados", sourceRow, false),
                        metric(row, columns.get("GOALS_AGAINST"), formatter, "Sofridos", sourceRow, false)
                ));
            }
            if (result.isEmpty()) throw new IllegalArgumentException("Sem jogadores/equipas válidos");
            return new CommunityStandingsArchivePreviewResponse(
                    sheet.getSheetName(), source.size() - 1, result.size(), duplicated,
                    warnings, List.copyOf(result)
            );
        } catch (IOException exception) {
            throw new IllegalArgumentException("Não foi possível ler o ficheiro histórico", exception);
        }
    }

    private String canonicalField(String raw) {
        String field = raw.trim().toUpperCase(Locale.ROOT);
        return switch (field) {
            case "PLAYER", "TEAM", "NAME" -> "NAME";
            case "RANK", "POSITION" -> "POSITION";
            case "GAMES", "PLAYED" -> "PLAYED";
            case "WIN", "WINS" -> "WINS";
            case "DRAW", "DRAWS" -> "DRAWS";
            case "LOSS", "LOSSES" -> "LOSSES";
            case "GOALS", "GOALS_FOR" -> "GOALS_FOR";
            case "GOALS_AGAINST" -> "GOALS_AGAINST";
            case "POINTS", "IGNORE" -> field;
            default -> throw new IllegalArgumentException("Campo histórico não suportado: " + raw);
        };
    }

    private Integer metric(Row row, Integer index, DataFormatter formatter,
            String label, int sourceRow, boolean allowNegative) {
        if (index == null) return null;
        String value = cell(row, index, formatter).trim();
        if (value.isBlank()) return null;
        try {
            int number = Integer.parseInt(value);
            if ((!allowNegative && number < 0) || number > MAX_METRIC || number < -MAX_METRIC) {
                throw new NumberFormatException("Fora de limites");
            }
            return number;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Linha " + sourceRow + ": " + label
                    + " deve ser um número inteiro válido", exception);
        }
    }

    private String cell(Row row, int index, DataFormatter formatter) {
        Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        return cell == null ? "" : formatter.formatCellValue(cell);
    }

    private String normalizeName(String value) {
        return Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Seleciona um ficheiro histórico");
        String filename = file.getOriginalFilename();
        if (filename == null || filename.length() > 255) {
            throw new IllegalArgumentException("Nome de ficheiro inválido");
        }
        String lower = filename.toLowerCase(Locale.ROOT);
        if (!lower.endsWith(".csv") && !lower.endsWith(".tsv")
                && !lower.endsWith(".xls") && !lower.endsWith(".xlsx")) {
            throw new IllegalArgumentException("Formatos suportados: Excel, CSV e TSV");
        }
        if (file.getSize() > 10L * 1024 * 1024) {
            throw new IllegalArgumentException("O ficheiro não pode ultrapassar 10 MB");
        }
    }

    private CommunityMember membership(Long communityId, String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador não encontrado"));
        return memberRepository.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
    }

    private void requireManager(Long communityId, String username) {
        CommunityRole role = membership(communityId, username).getRole();
        if (role != CommunityRole.OWNER && role != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas gestores podem importar classificações históricas");
        }
    }

    private void requireMember(Long communityId, String username) {
        membership(communityId, username);
    }
}
