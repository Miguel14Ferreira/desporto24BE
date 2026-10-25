package com.example.desporto24.controllers;

import com.example.desporto24.exception.ExceptionHandling;
import com.example.desporto24.service.CommunityImportService;
import com.example.desporto24.service.CommunityManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Import HTTP failures are scoped to spreadsheet routes. No real DB, file upload
 * or changes to history are required by these regression tests.
 */
@ExtendWith(MockitoExtension.class)
class CommunityImportHttpErrorTest {
    @Mock private CommunityImportService imports;
    @Mock private CommunityManagementService groups;
    @InjectMocks private CommunityController controller;
    private MockMvc mvc;
    private final TestingAuthenticationToken auth = new TestingAuthenticationToken("organizer", "test");

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandling())
                .build();
    }

    @Test
    void missingSpreadsheetFileIs400WithLegacyShapeAndStableCode() throws Exception {
        when(imports.preview(eq(7L), eq("organizer"), any()))
                .thenThrow(new IllegalArgumentException("Seleciona um ficheiro Excel"));
        mvc.perform(multipart("/api/communities/7/competition/import/preview")
                        .file(new MockMultipartFile("file", "vazio.csv", "text/csv", new byte[]{1}))
                        .principal(auth))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatusCode").value(400))
                .andExpect(jsonPath("$.message").value("Seleciona um ficheiro Excel"))
                .andExpect(jsonPath("$.code").value("IMPORT_FILE_REQUIRED"))
                .andExpect(jsonPath("$.params").isMap());
    }

    @Test
    void sourceRowValidationPreservesRowNumberAndDoesNotConfirmImport() throws Exception {
        when(imports.preview(eq(7L), eq("organizer"), any()))
                .thenThrow(new IllegalArgumentException("Linha 12: resultado inválido (exemplo: 2-1 ou 2:1)"));
        mvc.perform(multipart("/api/communities/7/competition/import/preview")
                        .file(new MockMultipartFile("file", "jogos.csv", "text/csv", new byte[]{1}))
                        .principal(auth))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("IMPORT_ROW_SCORE_INVALID"))
                .andExpect(jsonPath("$.params.row").value("12"));
        verify(imports, never()).confirmRounds(any(), any(), any(), any(), any());
    }

    @Test
    void unknownValidationErrorIs400WithoutInventingACode() throws Exception {
        when(imports.validateContext(eq(7L), eq("organizer"), any()))
                .thenThrow(new IllegalArgumentException("Linha 9: uma nova validação"));
        mvc.perform(post("/api/communities/7/competition/import/context/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"seasonId\":1}")
                        .principal(auth))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("IMPORT_UNRECOGNIZED"))
                .andExpect(jsonPath("$.params.row").value("9"))
                .andExpect(jsonPath("$.message").value("Linha 9: uma nova validação"));
    }

    @Test
    void permissionsRemain403AndDoNotExposeInternalReason() throws Exception {
        when(imports.preview(eq(7L), eq("organizer"), any()))
                .thenThrow(new AccessDeniedException("Private membership detail"));
        mvc.perform(multipart("/api/communities/7/competition/import/preview")
                        .file(new MockMultipartFile("file", "jogos.csv", "text/csv", new byte[]{1}))
                        .principal(auth))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Não tens permissões suficientes."));
    }

    @Test
    void unrelatedCommunityErrorsKeepExistingGlobalHandler() throws Exception {
        when(groups.get(eq(7L), eq("organizer")))
                .thenThrow(new IllegalArgumentException("Unrelated community error"));
        mvc.perform(get("/api/communities/7").principal(auth))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").doesNotExist());
    }
}
