package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CommunityImportService {

    private static final int SAMPLE_ROWS = 5;

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
                        samples
                ));
            }
            return new CommunityImportPreviewResponse(file.getOriginalFilename(), sheets);
        } catch (IOException | RuntimeException exception) {
            throw new IllegalArgumentException("Não foi possível ler o ficheiro Excel", exception);
        }
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
