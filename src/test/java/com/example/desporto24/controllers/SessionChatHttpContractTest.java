package com.example.desporto24.controllers;

import com.example.desporto24.exception.ExceptionHandling;
import com.example.desporto24.model.dto.session.SessionMessageResponse;
import com.example.desporto24.service.SessionChatService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * HTTP regression for session chats using the real controller and exception
 * advice, with a mocked service. No QA users, databases or production writes.
 */
class SessionChatHttpContractTest {

    private MockMvc controller(SessionChatService service) {
        return MockMvcBuilders.standaloneSetup(new SessionChatController(service))
                .setControllerAdvice(new ExceptionHandling())
                .build();
    }

    private UsernamePasswordAuthenticationToken actor(String username) {
        return new UsernamePasswordAuthenticationToken(username, null);
    }

    @Test
    void activeCreatorCanReadTheirAuthorizedSessionMessages() throws Exception {
        SessionChatService service = mock(SessionChatService.class);
        when(service.messages(41L, "creator")).thenReturn(List.<SessionMessageResponse>of());

        controller(service).perform(get("/api/sessions/41/messages")
                        .principal(actor("creator")))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(service).messages(41L, "creator");
    }

    @Test
    void nonParticipantCannotReadOrPostMessages() throws Exception {
        SessionChatService service = mock(SessionChatService.class);
        when(service.messages(41L, "outsider"))
                .thenThrow(new AccessDeniedException("Session chat is limited to participants"));
        when(service.post(41L, "hello", "outsider", null))
                .thenThrow(new AccessDeniedException("Session chat is limited to participants"));

        MockMvc mvc = controller(service);
        mvc.perform(get("/api/sessions/41/messages")
                        .principal(actor("outsider")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatusCode").value(403))
                .andExpect(jsonPath("$.message").value("Não tens permissões suficientes."));

        mvc.perform(post("/api/sessions/41/messages")
                        .principal(actor("outsider"))
                        .contentType("application/json")
                        .content("{\"body\":\"hello\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatusCode").value(403))
                .andExpect(jsonPath("$.message").value("Não tens permissões suficientes."));

        verify(service).messages(41L, "outsider");
        verify(service).post(41L, "hello", "outsider", null);
    }

    @Test
    void expiredSessionHistoryIsForbiddenAndPostReturnsConflict() throws Exception {
        SessionChatService service = mock(SessionChatService.class);
        when(service.messages(41L, "creator"))
                .thenThrow(new AccessDeniedException("Session chat is available only before the game"));
        when(service.post(41L, "late message", "creator", null))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT,
                        "Session chat is available only before the game"));

        MockMvc mvc = controller(service);
        mvc.perform(get("/api/sessions/41/messages")
                        .principal(actor("creator")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatusCode").value(403))
                .andExpect(jsonPath("$.message").value("Não tens permissões suficientes."));

        mvc.perform(post("/api/sessions/41/messages")
                        .principal(actor("creator"))
                        .contentType("application/json")
                        .content("{\"body\":\"late message\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.httpStatusCode").value(409));
        verify(service).messages(41L, "creator");
        verify(service).post(41L, "late message", "creator", null);
    }
}
