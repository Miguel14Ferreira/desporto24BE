package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.community.CommunityHistoricalMatchPreviewResponse;
import com.example.desporto24.model.dto.community.CommunityImportMappingRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse historical individual/team match exports WITHOUT creating participants,
 * matches or live competition fixtures. This is a preview-only stage.
 */
@Service
@RequiredArgsConstructor
public class CommunityHistoricalMatchPreviewService {
    private static final int MAX_ROWS = 2_000;
    private static final int MAX_SCORE = 999;
    private static final Pattern COMBINED_SCORE =
            Pattern.compile("^\\s*(\\d{1,3})\\s*[-:xX–]\\s*(\\d{1,3})\\s*$");

    private final ProfileRepository profileRepository;
    private final CommunityMemberRepository memberRepository;

    public CommunityHistoricalMatchPreviewResponse preview(Long communityId, String username,
            MultipartFile file, CommunityImportMappingRequest mapping) {
        requireManager(communityId, username);
        validateFile(file);
        Map<String, Integer> columns = validateMapping(mapping);

        try (Workbook workbook = CommunityImportWorkbookReader.open(file)) {
            Sheet sheet = workbook.getSheet(mapping.sheetName());
            if (sheet == null) throw new IllegalArgumentException("A folha selecionada não existe");
            DataFormatter formatter = new DataFormatter();
            List<Row> nonEmpty = new ArrayList<>();
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
                    nonEmpty.add(row);
                    if (nonEmpty.size() > MAX_ROWS + 1) {
                        throw new IllegalArgumentException("O histórico suporta até 2 000 partidas");
                    }
                }
            }
            if (nonEmpty.size() < 2) {
                throw new IllegalArgumentException("A folha não contém partidas para analisar");
            }
            if (columns.values().stream().anyMatch(index -> index >= nonEmpty.getFirst().getLastCellNum())) {
                throw new IllegalArgumentException("Uma coluna selecionada não existe no cabeçalho");
            }

            List<CommunityHistoricalMatchPreviewResponse.MatchRow> parsed = new ArrayList<>();
            List<String> warnings = new ArrayList<>();
            Set<MatchIdentity> seen = new HashSet<>();
            int candidates = 0;

            for (int index = 1; index < nonEmpty.size(); index++) {
                Row row = nonEmpty.get(index);
                int line = row.getRowNum() + 1;
                String home = cell(row, columns.get("HOME"), formatter).trim();
                String away = cell(row, columns.get("AWAY"), formatter).trim();
                if (home.isBlank() || away.isBlank() || home.length() > 120 || away.length() > 120) {
                    throw new IllegalArgumentException("Linha " + line
                            + ": indica os dois adversários (máximo 120 caracteres cada)");
                }
                String homeKey = normalize(home), awayKey = normalize(away);
                if (homeKey.equals(awayKey)) {
                    throw new IllegalArgumentException("Linha " + line
                            + ": a mesma pessoa/equipa não pode jogar contra si própria");
                }

                int homeScore, awayScore;
                if (columns.containsKey("SCORE")) {
                    String value = cell(row, columns.get("SCORE"), formatter);
                    Matcher match = COMBINED_SCORE.matcher(value);
                    if (!match.matches()) throw new IllegalArgumentException("Linha " + line
                            + ": resultado inválido (exemplo: 2-1 ou 2:1)");
                    homeScore = parseScore(match.group(1), line);
                    awayScore = parseScore(match.group(2), line);
                } else {
                    homeScore = parseScore(cell(row, columns.get("HOME_SCORE"), formatter).trim(), line);
                    awayScore = parseScore(cell(row, columns.get("AWAY_SCORE"), formatter).trim(), line);
                }
                String date = columns.containsKey("DATE") ?
                        cell(row, columns.get("DATE"), formatter).trim() : "";
                String round = columns.containsKey("ROUND") ?
                        cell(row, columns.get("ROUND"), formatter).trim() : "";
                if (date.length() > 80 || round.length() > 80) {
                    throw new IllegalArgumentException("Linha " + line
                            + ": data ou jornada demasiado longa");
                }

                // Never silently drop two fixtures: repeated opponents may have played
                // twice on the same day. Flag likely duplicates for human review.
                MatchIdentity key = new MatchIdentity(
                        homeKey.compareTo(awayKey) <= 0 ? homeKey : awayKey,
                        homeKey.compareTo(awayKey) <= 0 ? awayKey : homeKey,
                        normalize(date), normalize(round));
                boolean duplicate = !seen.add(key);
                if (duplicate) {
                    candidates++;
                    warnings.add("Linha " + line + ": confronto potencialmente repetido; "
                            + "verifica se é um jogo diferente antes de confirmar");
                }
                parsed.add(new CommunityHistoricalMatchPreviewResponse.MatchRow(
                        line, home, away, homeScore, awayScore, date, round, duplicate));
            }
            return new CommunityHistoricalMatchPreviewResponse(
                    sheet.getSheetName(), nonEmpty.size() - 1, parsed.size(),
                    candidates, List.copyOf(warnings), List.copyOf(parsed));
        } catch (IOException exception) {
            throw new IllegalArgumentException("Não foi possível ler o ficheiro histórico", exception);
        }
    }

    private record MatchIdentity(String first, String second, String date, String round) {}

    private Map<String, Integer> validateMapping(CommunityImportMappingRequest mapping) {
        if (mapping == null || mapping.sheetName() == null || mapping.sheetName().isBlank()
                || mapping.columns() == null || mapping.columns().isEmpty()) {
            throw new IllegalArgumentException("Seleciona uma folha e as colunas dos jogos");
        }
        Map<String, Integer> result = new HashMap<>();
        Set<Integer> indices = new HashSet<>();
        for (CommunityImportMappingRequest.ColumnMapping column : mapping.columns()) {
            if (column == null || column.columnIndex() < 0 || column.columnIndex() >= 128
                    || column.field() == null) {
                throw new IllegalArgumentException("Mapeamento de colunas inválido");
            }
            String field = column.field().trim().toUpperCase(Locale.ROOT);
            if (field.equals("IGNORE")) continue;
            if (!Set.of("HOME", "AWAY", "HOME_SCORE", "AWAY_SCORE", "SCORE", "DATE", "ROUND")
                    .contains(field)) {
                throw new IllegalArgumentException("Campo de partida não suportado: " + field);
            }
            if (!indices.add(column.columnIndex()) || result.putIfAbsent(field, column.columnIndex()) != null) {
                throw new IllegalArgumentException("Coluna ou campo duplicado no mapeamento");
            }
        }
        if (!result.containsKey("HOME") || !result.containsKey("AWAY")) {
            throw new IllegalArgumentException("Mapeia as duas pessoas/equipas do confronto");
        }
        boolean combined = result.containsKey("SCORE");
        boolean homeScore = result.containsKey("HOME_SCORE"), awayScore = result.containsKey("AWAY_SCORE");
        if ((combined && (homeScore || awayScore))
                || (!combined && (!homeScore || !awayScore))) {
            throw new IllegalArgumentException("Escolhe SCORE ou ambas as colunas HOME_SCORE e AWAY_SCORE");
        }
        return result;
    }

    private int parseScore(String value, int line) {
        try {
            int score = Integer.parseInt(value);
            if (score < 0 || score > MAX_SCORE) throw new NumberFormatException("Out of range");
            return score;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Linha " + line
                    + ": o resultado deve ser inteiro entre 0 e 999", exception);
        }
    }

    private String cell(Row row, Integer index, DataFormatter formatter) {
        if (index == null) return "";
        Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        return cell == null ? "" : formatter.formatCellValue(cell);
    }

    private String normalize(String value) {
        return Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Seleciona um ficheiro");
        String filename = file.getOriginalFilename();
        if (filename == null || filename.length() > 255) {
            throw new IllegalArgumentException("Nome de ficheiro inválido");
        }
        String lower = filename.toLowerCase(Locale.ROOT);
        if (!lower.endsWith(".csv") && !lower.endsWith(".tsv")
                && !lower.endsWith(".xls") && !lower.endsWith(".xlsx")) {
            throw new IllegalArgumentException("Suportamos Excel, CSV e TSV");
        }
        if (file.getSize() > 10L * 1024 * 1024) {
            throw new IllegalArgumentException("O ficheiro não pode ultrapassar 10 MB");
        }
    }

    private void requireManager(Long communityId, String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Utilizador não encontrado"));
        CommunityMember member = memberRepository.findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new AccessDeniedException("Não pertences a esta comunidade"));
        if (member.getRole() != CommunityRole.OWNER && member.getRole() != CommunityRole.ADMIN) {
            throw new AccessDeniedException("Apenas gestores podem analisar partidas históricas");
        }
    }
}
