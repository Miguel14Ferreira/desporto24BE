package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.community.CommunityImportMappingRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Uses real, generated XLSX bytes through the production workbook parser.
 * Only the preview service is called; no archive, tournament or fixture writes.
 */
@ExtendWith(MockitoExtension.class)
class CommunityHistoricalMatchXlsxPreviewTest {
    @Mock private ProfileRepository profiles;
    @Mock private CommunityMemberRepository members;
    @InjectMocks private CommunityHistoricalMatchPreviewService service;

    private void authorize(CommunityRole role) {
        Profile user = new Profile();
        user.setId(8L);
        when(profiles.findUserByUsernameIgnoreCase("organizer"))
                .thenReturn(Optional.of(user));
        CommunityMember member = new CommunityMember();
        member.setRole(role);
        when(members.findByCommunityIdAndProfileId(7L, 8L))
                .thenReturn(Optional.of(member));
    }

    private CommunityImportMappingRequest combined() {
        return new CommunityImportMappingRequest("Torneio 2024", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "HOME"),
                new CommunityImportMappingRequest.ColumnMapping(1, "AWAY"),
                new CommunityImportMappingRequest.ColumnMapping(2, "SCORE"),
                new CommunityImportMappingRequest.ColumnMapping(3, "DATE"),
                new CommunityImportMappingRequest.ColumnMapping(4, "ROUND")
        ));
    }

    private MockMultipartFile workbookWithKnockoutMatches() throws IOException {
        try (var workbook = new XSSFWorkbook(); var output = new ByteArrayOutputStream()) {
            workbook.createSheet("Notas").createRow(0).createCell(0)
                    .setCellValue("Não importar esta folha");
            var sheet = workbook.createSheet("Torneio 2024");
            var headers = sheet.createRow(0);
            String[] names = {"Casa", "Fora", "Resultado", "Data", "Jornada"};
            for (int i = 0; i < names.length; i++) headers.createCell(i).setCellValue(names[i]);
            // Deliberately leave physical Excel row 2 blank.
            String[][] matches = {
                    {"Águias", "Lobos", "3-0", "2024-06-01", "Quartos de final"},
                    {"CNG", "Tigres", "2-0", "2024-06-02", "Quartos de final"},
                    {"Leões", "Dragões", "1-0", "2024-06-03", "Quartos de final"},
                    {"Raios", "Falcões", "2-0", "2024-06-04", "Quartos de final"},
                    {"Águias", "CNG", "1-0", "2024-06-05", "Meias-finais"},
                    {"Leões", "Raios", "2-0", "2024-06-06", "Meias-finais"},
                    {"Águias", "Leões", "1-0", "2024-06-10", "Final"},
                    // Potential repeat: same opponents, swapped home/away, ISO vs PT date.
                    {"LEÕES", "aguias", "0-1", "10/06/2024", "FINAL"}
            };
            for (int n = 0; n < matches.length; n++) {
                var row = sheet.createRow(n + 2);
                for (int col = 0; col < matches[n].length; col++) {
                    row.createCell(col).setCellValue(matches[n][col]);
                }
            }
            workbook.write(output);
            return new MockMultipartFile("file", "torneio-historico.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    output.toByteArray());
        }
    }

    @Test
    void previewsActualXlsxBytesWithKnockoutStagesOriginalRowsAndSuspectedReplay() throws Exception {
        authorize(CommunityRole.OWNER);
        var preview = service.preview(7L, "organizer", workbookWithKnockoutMatches(), combined());

        assertEquals("Torneio 2024", preview.sheetName());
        assertEquals(8, preview.sourceRows());
        assertEquals(8, preview.acceptedRows());
        assertEquals(8, preview.rows().size());
        assertEquals(1, preview.potentialDuplicates());
        // Physical sourceRow numbers, not ordinal positions after dropping blank rows.
        assertEquals(3, preview.rows().getFirst().sourceRow());
        assertEquals(10, preview.rows().getLast().sourceRow());
        assertEquals("Águias", preview.rows().getFirst().home());
        assertEquals("Quartos de final", preview.rows().getFirst().sourceRound());
        assertEquals("Meias-finais", preview.rows().get(4).sourceRound());
        assertEquals("Final", preview.rows().get(6).sourceRound());
        assertEquals("2024-06-10", preview.rows().get(6).sourceDate());
        assertEquals("10/06/2024", preview.rows().get(7).sourceDate());
        assertEquals(0, preview.rows().get(7).homeScore());
        assertEquals(1, preview.rows().get(7).awayScore());
        assertFalse(preview.rows().get(6).potentialDuplicate());
        assertTrue(preview.rows().get(7).potentialDuplicate());
        assertTrue(preview.warnings().getFirst().contains("Linha 10"));
        verify(profiles).findUserByUsernameIgnoreCase("organizer");
        verify(members).findByCommunityIdAndProfileId(7L, 8L);
        verifyNoMoreInteractions(profiles, members);
    }

    @Test
    void previewsActualXlsxNumericScoreCellsAndKeepsOriginalAccents() throws Exception {
        authorize(CommunityRole.ADMIN);
        MockMultipartFile file;
        try (var workbook = new XSSFWorkbook(); var output = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("Torneio 2024");
            var header = sheet.createRow(0);
            for (int i = 0; i < 5; i++) header.createCell(i).setCellValue("Coluna " + i);
            var match = sheet.createRow(1);
            match.createCell(0).setCellValue("Águias");
            match.createCell(1).setCellValue("São João");
            match.createCell(2).setCellValue(0);
            match.createCell(3).setCellValue(3);
            match.createCell(4).setCellValue("Meia-final");
            workbook.write(output);
            file = new MockMultipartFile("file", "historico.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    output.toByteArray());
        }
        var mapping = new CommunityImportMappingRequest("Torneio 2024", List.of(
                new CommunityImportMappingRequest.ColumnMapping(0, "HOME"),
                new CommunityImportMappingRequest.ColumnMapping(1, "AWAY"),
                new CommunityImportMappingRequest.ColumnMapping(2, "HOME_SCORE"),
                new CommunityImportMappingRequest.ColumnMapping(3, "AWAY_SCORE"),
                new CommunityImportMappingRequest.ColumnMapping(4, "ROUND")
        ));
        var result = service.preview(7L, "organizer", file, mapping);
        assertEquals(1, result.acceptedRows());
        assertEquals("Águias", result.rows().getFirst().home());
        assertEquals("São João", result.rows().getFirst().away());
        assertEquals(0, result.rows().getFirst().homeScore());
        assertEquals(3, result.rows().getFirst().awayScore());
        assertEquals("Meia-final", result.rows().getFirst().sourceRound());
    }

    @Test
    void rejectsWrongSheetWithoutDroppingOrImportingHistoricalMatches() throws Exception {
        authorize(CommunityRole.OWNER);
        var incorrect = new CommunityImportMappingRequest("Folha inexistente", combined().columns());
        var failure = assertThrows(IllegalArgumentException.class, () ->
                service.preview(7L, "organizer", workbookWithKnockoutMatches(), incorrect));
        assertTrue(failure.getMessage().contains("folha selecionada"));
    }

    @Test
    void prohibitsNonManagerFromReadingHistoricalXlsxPreview() throws Exception {
        authorize(CommunityRole.MEMBER);
        assertThrows(AccessDeniedException.class, () ->
                service.preview(7L, "organizer", workbookWithKnockoutMatches(), combined()));
        verifyNoMoreInteractions(profiles, members);
    }

    @Test
    void rejectsFormulaInXlsxScoreInsteadOfExecutingOrGuessingAnOutcome() throws Exception {
        authorize(CommunityRole.OWNER);
        MockMultipartFile file;
        try (var workbook = new XSSFWorkbook(); var output = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("Torneio 2024");
            var headers = sheet.createRow(0);
            for (int i = 0; i < 5; i++) headers.createCell(i).setCellValue("Col " + i);
            var row = sheet.createRow(1);
            row.createCell(0).setCellValue("Águias");
            row.createCell(1).setCellValue("Lobos");
            row.createCell(2).setCellFormula("1+1");
            row.createCell(3).setCellValue("2024-06-10");
            row.createCell(4).setCellValue("Final");
            workbook.write(output);
            file = new MockMultipartFile("file", "formulas.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    output.toByteArray());
        }
        var failure = assertThrows(IllegalArgumentException.class, () ->
                service.preview(7L, "organizer", file, combined()));
        assertTrue(failure.getMessage().contains("Linha 2"));
    }
}
