package com.example.desporto24.controllers;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.exception.ExceptionHandling;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import com.example.desporto24.service.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Real Spring MVC import endpoints and production Excel/CSV/TSV parsing, but
 * synthetic user, mocked repositories and no database or production traffic.
 * No confirmation is ever permitted to persist an invalid import.
 */
@ExtendWith(MockitoExtension.class)
class CommunityImportRealFilesHttpTest {
    private static final String MATCH_MAPPING = """
            {"sheetName":"Dados","columns":[
              {"columnIndex":0,"field":"HOME"},
              {"columnIndex":1,"field":"AWAY"},
              {"columnIndex":2,"field":"SCORE"},
              {"columnIndex":3,"field":"DATE"},
              {"columnIndex":4,"field":"ROUND"}]}""";
    private static final String STANDINGS_MAPPING = """
            {"sheetName":"Dados","columns":[
              {"columnIndex":1,"field":"NAME"},
              {"columnIndex":2,"field":"POINTS"}]}""";
    private static final String MATCH_CONTEXT = """
            {"title":"Histórico QA","entryType":"TEAM"}""";
    private static final String STANDINGS_CONTEXT = """
            {"title":"Classificação QA","entryType":"TEAM"}""";
    private static final String MATCH_ROUTE = "/api/communities/7/competition/import/matches/";
    private static final String STANDINGS_ROUTE = "/api/communities/7/competition/import/standings/";

    @Mock private ProfileRepository profiles;
    @Mock private CommunityMemberRepository members;
    @Mock private CommunityRepository communities;
    @Mock private CommunitySeasonRepository seasons;
    @Mock private CommunityHistoricalMatchArchiveRepository matchArchives;
    @Mock private CommunityHistoricalMatchArchiveRowRepository matchRows;
    @Mock private CommunityTournamentRepository tournaments;
    @Mock private CommunityStandingsArchiveRepository standingArchives;
    @Mock private CommunityStandingsArchiveRowRepository standingRows;
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        CommunityHistoricalMatchPreviewService matchPreview =
                new CommunityHistoricalMatchPreviewService(profiles, members);
        CommunityHistoricalMatchArchiveService matchArchive =
                new CommunityHistoricalMatchArchiveService(matchPreview, profiles, members,
                        communities, matchArchives, tournaments, matchRows);
        CommunityStandingsArchiveService standingArchive =
                new CommunityStandingsArchiveService(profiles, members, communities,
                        seasons, standingArchives, standingRows);
        CommunityController controller = new CommunityController(
                mock(CommunityManagementService.class),
                mock(CommunityCompetitionService.class),
                mock(CommunityHistoricalPlayerLinkService.class),
                mock(CommunityImportService.class),
                standingArchive, matchPreview, matchArchive,
                mock(CommunitySessionService.class),
                mock(com.example.desporto24.service.CommunitySessionResultService.class));
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandling())
                .build();
    }

    private void authorize(CommunityRole role) {
        Profile actor = new Profile();
        actor.setId(11L);
        when(profiles.findUserByUsernameIgnoreCase("organizer")).thenReturn(Optional.of(actor));
        CommunityMember member = new CommunityMember();
        member.setRole(role);
        when(members.findByCommunityIdAndProfileId(7L, 11L)).thenReturn(Optional.of(member));
    }

    @AfterEach
    void assertNoArchiveWrites() {
        verifyNoInteractions(communities, seasons, matchArchives, matchRows, tournaments,
                standingArchives, standingRows);
    }

    private static MockMultipartFile file(String name, byte[] bytes) {
        return new MockMultipartFile("file", name, "application/octet-stream", bytes);
    }

    private static MockMultipartFile text(String name, String data) {
        return file(name, data.getBytes(StandardCharsets.UTF_8));
    }

    private static MockMultipartFile jsonPart(String name, String json) {
        return new MockMultipartFile(name, name + ".json", MediaType.APPLICATION_JSON_VALUE,
                json.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] excel(boolean legacy) throws IOException {
        try (Workbook workbook = legacy ? new HSSFWorkbook() : new XSSFWorkbook();
             var buffer = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("Dados");
            String[] columns = {"Casa", "Fora", "Resultado", "Data", "Jornada"};
            var header = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }
            var row = sheet.createRow(1);
            String[] data = {"Águias", "Lobos", "2-1", "2020-06-10", "Final"};
            for (int i = 0; i < data.length; i++) {
                row.createCell(i).setCellValue(data[i]);
            }
            workbook.write(buffer);
            return buffer.toByteArray();
        }
    }

    private void matches(String action, MockMultipartFile source, String mapping, String context)
            throws Exception {
        var request = multipart(MATCH_ROUTE + action).file(source)
                .file(jsonPart("mapping", mapping))
                .principal(new TestingAuthenticationToken("organizer", "test"));
        if (context != null) request.file(jsonPart("context", context));
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.acceptedRows").value(1))
                .andExpect(jsonPath("$.rows[0].home").value("Águias"))
                .andExpect(jsonPath("$.rows[0].away").value("Lobos"))
                .andExpect(jsonPath("$.rows[0].homeScore").value(2))
                .andExpect(jsonPath("$.rows[0].awayScore").value(1))
                .andExpect(jsonPath("$.rows[0].sourceRound").value("Final"));
    }

    @Test
    void parsesActualCsvAndTsvThroughHttpPreviewWithoutPersisting() throws Exception {
        authorize(CommunityRole.OWNER);
        matches("preview", text("fixture.csv",
                "Casa;Fora;Resultado;Data;Jornada\nÁguias;Lobos;2-1;2020-06-10;Final\n"),
                MATCH_MAPPING, null);
        matches("preview", text("fixture.tsv",
                "Casa\tFora\tResultado\tData\tJornada\nÁguias\tLobos\t2-1\t2020-06-10\tFinal\n"),
                MATCH_MAPPING, null);
    }

    @Test
    void parsesActualXlsxAndLegacyXlsThroughHttpPreviewWithoutPersisting() throws Exception {
        authorize(CommunityRole.ADMIN);
        matches("preview", file("fixture.xlsx", excel(false)), MATCH_MAPPING, null);
        matches("preview", file("fixture.xls", excel(true)), MATCH_MAPPING, null);
    }

    @Test
    void previewsHistoricalStandingsThroughRealCsvAndTsvHttp() throws Exception {
        authorize(CommunityRole.OWNER);
        for (var source : new MockMultipartFile[]{
                text("standings.csv", "Pos;Equipa;Pts\n1;Águias;17\n"),
                text("standings.tsv", "Pos\tEquipa\tPts\n1\tÁguias\t17\n")
        }) {
            mvc.perform(multipart(STANDINGS_ROUTE + "preview")
                            .file(source).file(jsonPart("mapping", STANDINGS_MAPPING))
                            .principal(new TestingAuthenticationToken("organizer", "test")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.acceptedRows").value(1))
                    .andExpect(jsonPath("$.rows[0].name").value("Águias"))
                    .andExpect(jsonPath("$.rows[0].points").value(17));
        }
    }

    @Test
    void malformedCsvScoreReturnsStructured400AndCannotCreateHistory() throws Exception {
        authorize(CommunityRole.OWNER);
        mvc.perform(multipart(MATCH_ROUTE + "preview")
                        .file(text("wrong.csv",
                                "Casa;Fora;Resultado;Data;Jornada\nÁguias;Lobos;2-a;2020-06-10;Final\n"))
                        .file(jsonPart("mapping", MATCH_MAPPING))
                        .principal(new TestingAuthenticationToken("organizer", "test")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatusCode").value(400))
                .andExpect(jsonPath("$.code").value("IMPORT_ROW_SCORE_INVALID"))
                .andExpect(jsonPath("$.params.row").value("2"));
    }

    @Test
    void malformedCsvCannotPartiallyConfirmAnyHistoricalMatch() throws Exception {
        authorize(CommunityRole.OWNER);
        mvc.perform(multipart(MATCH_ROUTE + "confirm")
                        .file(text("wrong.csv",
                                "Casa;Fora;Resultado;Data;Jornada\nÁguias;Lobos;bad;2020-06-10;Final\n"))
                        .file(jsonPart("mapping", MATCH_MAPPING))
                        .file(jsonPart("context", MATCH_CONTEXT))
                        .principal(new TestingAuthenticationToken("organizer", "test")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("IMPORT_ROW_SCORE_INVALID"));
    }

    @Test
    void invalidStandingsNeverCreateAnArchiveOrOverwriteResults() throws Exception {
        authorize(CommunityRole.OWNER);
        mvc.perform(multipart(STANDINGS_ROUTE + "confirm")
                        .file(text("bad.tsv", "Pos\tEquipa\tPts\n1\tÁguias\tINVALID\n"))
                        .file(jsonPart("mapping", STANDINGS_MAPPING))
                        .file(jsonPart("context", STANDINGS_CONTEXT))
                        .principal(new TestingAuthenticationToken("organizer", "test")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatusCode").value(400));
    }

    @Test
    void corruptedXlsxIsLocalizedReadFailureNotServerError() throws Exception {
        authorize(CommunityRole.OWNER);
        mvc.perform(multipart(MATCH_ROUTE + "preview")
                        .file(text("broken.xlsx", "This is not an Excel document"))
                        .file(jsonPart("mapping", MATCH_MAPPING))
                        .principal(new TestingAuthenticationToken("organizer", "test")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("IMPORT_FILE_READ_FAILED"));
        mvc.perform(multipart(STANDINGS_ROUTE + "preview")
                        .file(text("broken.xlsx", "This is not an Excel document"))
                        .file(jsonPart("mapping", STANDINGS_MAPPING))
                        .principal(new TestingAuthenticationToken("organizer", "test")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("IMPORT_FILE_READ_FAILED"));
    }

    @Test
    void rejectsUnsupportedAndOversizedFilesWithStableCodes() throws Exception {
        authorize(CommunityRole.OWNER);
        mvc.perform(multipart(MATCH_ROUTE + "preview")
                        .file(text("invalid.txt", "not a spreadsheet"))
                        .file(jsonPart("mapping", MATCH_MAPPING))
                        .principal(new TestingAuthenticationToken("organizer", "test")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("IMPORT_FILE_FORMAT_UNSUPPORTED"));
        mvc.perform(multipart(MATCH_ROUTE + "preview")
                        .file(file("oversize.csv", new byte[10 * 1024 * 1024 + 1]))
                        .file(jsonPart("mapping", MATCH_MAPPING))
                        .principal(new TestingAuthenticationToken("organizer", "test")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("IMPORT_FILE_TOO_LARGE"));
    }

    @Test
    void rejectsBinaryAndInvalidUtf8CsvWithSafeStableReadCode() throws Exception {
        authorize(CommunityRole.OWNER);
        for (byte[] data : new byte[][]{
                new byte[]{'A', 0, 'B'},
                new byte[]{'A', (byte) 0xC3, (byte) 0x28}
        }) {
            mvc.perform(multipart(MATCH_ROUTE + "preview")
                            .file(file("invalid.csv", data))
                            .file(jsonPart("mapping", MATCH_MAPPING))
                            .principal(new TestingAuthenticationToken("organizer", "test")))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("IMPORT_FILE_READ_FAILED"));
        }
    }

    @Test
    void wrongSheetIsStructured400WithSourceDiagnostics() throws Exception {
        authorize(CommunityRole.OWNER);
        String missing = MATCH_MAPPING.replace("\"Dados\"", "\"Missing\"");
        mvc.perform(multipart(MATCH_ROUTE + "preview")
                        .file(text("fixture.csv",
                                "Casa;Fora;Resultado;Data;Jornada\nÁguias;Lobos;2-1;2020-06-10;Final\n"))
                        .file(jsonPart("mapping", missing))
                        .principal(new TestingAuthenticationToken("organizer", "test")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("IMPORT_SHEET_MISSING"));
    }

    @Test
    void memberCannotInspectPrivateHistoricalFiles() throws Exception {
        authorize(CommunityRole.MEMBER);
        mvc.perform(multipart(MATCH_ROUTE + "preview")
                        .file(text("fixture.csv",
                                "Casa;Fora;Resultado;Data;Jornada\nÁguias;Lobos;2-1;2020-06-10;Final\n"))
                        .file(jsonPart("mapping", MATCH_MAPPING))
                        .principal(new TestingAuthenticationToken("organizer", "test")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Não tens permissões suficientes."));
    }
}
