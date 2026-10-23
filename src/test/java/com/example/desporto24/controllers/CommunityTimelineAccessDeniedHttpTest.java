package com.example.desporto24.controllers;

import com.example.desporto24.exception.ExceptionHandling;
import com.example.desporto24.model.community.CommunityTimelineEntry;
import com.example.desporto24.service.CommunityTimelineService;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Regressão HTTP: a API de histórico de uma comunidade recusa contas não
 * membros sem expor mensagens, IDs privados ou lançar HTTP 500.
 * Não usa base de dados, contas reais nem serviços externos.
 */
class CommunityTimelineAccessDeniedHttpTest {

    @Test
    void unauthorizedChatHistoryReturns403InsteadOf500() throws Exception {
        CommunityTimelineService service = mock(CommunityTimelineService.class);
        when(service.list(7L, CommunityTimelineEntry.Kind.CHAT, "outsider"))
                .thenThrow(new AccessDeniedException("Only community members can access this content"));

        MockMvc mvc = MockMvcBuilders.standaloneSetup(new CommunityTimelineController(service))
                .setControllerAdvice(new ExceptionHandling())
                .build();

        mvc.perform(get("/api/communities/7/messages")
                        .principal(new UsernamePasswordAuthenticationToken("outsider", null)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatusCode").value(403))
                .andExpect(jsonPath("$.message").value("Não tens permissões suficientes."))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("Only community members"))));

        verify(service).list(7L, CommunityTimelineEntry.Kind.CHAT, "outsider");
    }
}
