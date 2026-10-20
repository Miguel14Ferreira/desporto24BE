package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.community.CommunitySeasonRepository;
import com.example.desporto24.repository.community.CommunityRoundRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import jakarta.persistence.EntityManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunityImportWorkbookReaderTest {

    private MockMultipartFile file(String name, String content) {
        return new MockMultipartFile("file", name, "text/plain",
                content.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void readsPortugueseSemicolonCsvWithBomAndQuotedValues() throws Exception {
        var csv = file("historico.csv",
                "\uFEFFJogador;Data;Golos;Notas\n\"Ana; Maria\";17/10/2026;2;\"Golo, do meio\"\n");
        try (Workbook workbook = CommunityImportWorkbookReader.open(csv)) {
            var sheet = workbook.getSheet("Dados");
            assertEquals("Jogador", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("Ana; Maria", sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals("Golo, do meio", sheet.getRow(1).getCell(3).getStringCellValue());
        }
    }

    @Test
    void readsCsvWithEmbeddedLinebreakAndKeepsPotentialFormulasLiteral() throws Exception {
        var csv = file("scores.csv",
                "Player,Notes,Goals\nAlex,\"First line\nSecond line\",3\n=HYPERLINK(\"oops\"),regular,0\n");
        try (Workbook workbook = CommunityImportWorkbookReader.open(csv)) {
            var sheet = workbook.getSheet("Dados");
            assertEquals("First line\nSecond line", sheet.getRow(1).getCell(1).getStringCellValue());
            assertEquals("=HYPERLINK(\"oops\")", sheet.getRow(2).getCell(0).getStringCellValue());
            assertEquals(org.apache.poi.ss.usermodel.CellType.STRING,
                    sheet.getRow(2).getCell(0).getCellType());
        }
    }

    @Test
    void readsTsvExportAndOldExcelBinaryWithoutReformatting() throws Exception {
        try (Workbook tsv = CommunityImportWorkbookReader.open(
                file("season.tsv", "Jogador\tGolos\nAna\t4\n"))) {
            assertEquals("4", tsv.getSheetAt(0).getRow(1).getCell(1).getStringCellValue());
        }
        try (var source = new HSSFWorkbook(); var output = new ByteArrayOutputStream()) {
            source.createSheet("Epoca 2024").createRow(0).createCell(0).setCellValue("Jogador");
            source.write(output);
            var upload = new MockMultipartFile("file", "antigo.xls",
                    "application/vnd.ms-excel", output.toByteArray());
            try (Workbook parsed = CommunityImportWorkbookReader.open(upload)) {
                assertEquals("Jogador", parsed.getSheet("Epoca 2024")
                        .getRow(0).getCell(0).getStringCellValue());
            }
        }
    }

    @Test
    void refusesBinaryOrMalformedUtf8Content() {
        var malformed = new MockMultipartFile("file", "history.csv", "text/csv",
                new byte[]{(byte) 0xc3, (byte) 0x28});
        assertThrows(IllegalArgumentException.class,
                () -> CommunityImportWorkbookReader.open(malformed));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityImportWorkbookReader.open(file("history.csv", "A,B\n\0,x")));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityImportWorkbookReader.open(file("history.pdf", "plain text")));
    }

    @Test
    void protectsAgainstExcessiveColumnsAndOversizeCells() {
        var tooManyColumns = file("many.csv", "Player," + "X,".repeat(128) + "\n");
        assertThrows(IllegalArgumentException.class,
                () -> CommunityImportWorkbookReader.open(tooManyColumns));
        var hugeCell = file("huge.tsv", "Player\n" + "a".repeat(32_001));
        assertThrows(IllegalArgumentException.class,
                () -> CommunityImportWorkbookReader.open(hugeCell));
    }

    @Test
    void existingCommunityPreviewRecognizesCsvAndPreservesSheetSuggestions() {
        var profileRepo = mock(ProfileRepository.class);
        var memberRepo = mock(CommunityMemberRepository.class);
        var owner = mock(Profile.class);
        var membership = mock(CommunityMember.class);
        when(owner.getId()).thenReturn(5L);
        when(membership.getRole()).thenReturn(CommunityRole.OWNER);
        when(profileRepo.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(owner));
        when(memberRepo.findByCommunityIdAndProfileId(7L, 5L)).thenReturn(Optional.of(membership));

        var service = new CommunityImportService(profileRepo, memberRepo,
                mock(CommunityRepository.class), mock(CommunityPlayerRepository.class),
                mock(CommunitySeasonRepository.class), mock(CommunityRoundRepository.class),
                mock(CommunityRoundEntryRepository.class), mock(EntityManager.class));

        var preview = service.preview(7L, "organizer",
                file("history.csv", "Jogador;Data;Golos\nAna;17/10/2026;2\n"));

        assertEquals("history.csv", preview.fileName());
        assertEquals(1, preview.sheets().size());
        assertEquals("Dados", preview.sheets().getFirst().name());
        assertEquals("TABULAR", preview.sheets().getFirst().structure());
        assertEquals("Ana", preview.sheets().getFirst().sampleRows().getFirst().getFirst());
        assertFalse(preview.sheets().getFirst().columnSuggestions().isEmpty());
    }

    @Test
    void rejectsUnexpectedFormatsAndOversizeUploadsBeforeParsing() {
        var profileRepo = mock(ProfileRepository.class);
        var memberRepo = mock(CommunityMemberRepository.class);
        var owner = mock(Profile.class);
        var membership = mock(CommunityMember.class);
        when(owner.getId()).thenReturn(5L);
        when(membership.getRole()).thenReturn(CommunityRole.ADMIN);
        when(profileRepo.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(owner));
        when(memberRepo.findByCommunityIdAndProfileId(7L, 5L)).thenReturn(Optional.of(membership));

        var service = new CommunityImportService(profileRepo, memberRepo,
                mock(CommunityRepository.class), mock(CommunityPlayerRepository.class),
                mock(CommunitySeasonRepository.class), mock(CommunityRoundRepository.class),
                mock(CommunityRoundEntryRepository.class), mock(EntityManager.class));

        assertThrows(IllegalArgumentException.class,
                () -> service.preview(7L, "organizer", file("history.pdf", "data")));

        var huge = mock(org.springframework.web.multipart.MultipartFile.class);
        when(huge.isEmpty()).thenReturn(false);
        when(huge.getOriginalFilename()).thenReturn("history.csv");
        when(huge.getSize()).thenReturn(10L * 1024 * 1024 + 1);
        assertThrows(IllegalArgumentException.class,
                () -> service.preview(7L, "organizer", huge));
    }
}
