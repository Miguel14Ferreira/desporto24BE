package com.example.desporto24.controllers;

import com.example.desporto24.exception.ExceptionHandling;
import com.example.desporto24.model.dto.community.UpdateCommunityFeaturesRequest;
import com.example.desporto24.service.CommunityCompetitionService;
import com.example.desporto24.service.CommunityHistoricalMatchArchiveService;
import com.example.desporto24.service.CommunityHistoricalMatchPreviewService;
import com.example.desporto24.service.CommunityHistoricalPlayerLinkService;
import com.example.desporto24.service.CommunityImportService;
import com.example.desporto24.service.CommunityManagementService;
import com.example.desporto24.service.CommunitySessionService;
import com.example.desporto24.service.CommunityStandingsArchiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * HTTP endpoint and controller-advice contract: role denials are 403, not 500.
 * Uses the real CommunityController and ExceptionHandling, but mock services.
 * Does NOT assert the JWT filter or Spring Security's filter chain;
 * those require separate filter-chain integration tests.
 */
class CommunityAuthorizationHttpTest {

    private CommunityManagementService service;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        service = mock(CommunityManagementService.class);
        var controller = new CommunityController(
                service,
                mock(CommunityCompetitionService.class),
                mock(CommunityHistoricalPlayerLinkService.class),
                mock(CommunityImportService.class),
                mock(CommunityStandingsArchiveService.class),
                mock(CommunityHistoricalMatchPreviewService.class),
                mock(CommunityHistoricalMatchArchiveService.class),
                mock(CommunitySessionService.class),
                mock(com.example.desporto24.service.CommunitySessionResultService.class)
        );
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandling())
                .build();
    }

    private Authentication as(String username) {
        return new UsernamePasswordAuthenticationToken(username, null, List.of());
    }

    @Test
    void nonmemberCannotReadFullCommunityDetailsAndSees403WithoutPrivateMessage() throws Exception {
        when(service.get(12L, "outsider"))
                .thenThrow(new SecurityException("Internal group membership detail"));

        mvc.perform(get("/api/communities/12").principal(as("outsider")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatusCode").value(403))
                .andExpect(jsonPath("$.message").value("Não tens permissões suficientes."));

        verify(service).get(12L, "outsider");
    }

    @Test
    void memberCannotReadPendingJoinQueueWhileManagerCanReadIt() throws Exception {
        when(service.pendingJoinRequests(12L, "member"))
                .thenThrow(new SecurityException("Not a group manager"));
        when(service.pendingJoinRequests(12L, "owner"))
                .thenReturn(List.of());

        mvc.perform(get("/api/communities/12/join-requests").principal(as("member")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatusCode").value(403));

        mvc.perform(get("/api/communities/12/join-requests").principal(as("owner")))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service).pendingJoinRequests(12L, "member");
        verify(service).pendingJoinRequests(12L, "owner");
    }

    @Test
    void memberCannotApprovePendingJoinRequest() throws Exception {
        when(service.acceptJoinRequest(12L, 99L, "member"))
                .thenThrow(new SecurityException("Not authorized"));

        mvc.perform(post("/api/communities/12/join-requests/99/accept")
                        .principal(as("member")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatusCode").value(403));

        verify(service).acceptJoinRequest(12L, 99L, "member");
    }

    @Test
    void memberCannotChangeOptionalFeatures() throws Exception {
        when(service.updateFeatures(eq(12L), eq("member"),
                any(UpdateCommunityFeaturesRequest.class)))
                .thenThrow(new SecurityException("Not authorized"));

        mvc.perform(put("/api/communities/12/features")
                        .principal(as("member"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"rankingsEnabled":true,
                                 "individualTournamentsEnabled":false,
                                 "teamTournamentsEnabled":false}
                                """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatusCode").value(403));

        verify(service).updateFeatures(eq(12L), eq("member"),
                any(UpdateCommunityFeaturesRequest.class));
    }
}
