package com.example.desporto24.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Regression: intentional 403/429 statuses must never reach the generic 500
 * handler. No database, user session, or external services are involved.
 */
class ResponseStatusExceptionHandlingTest {

    @RestController
    static class StatusFixture {
        @GetMapping("/test/status/forbidden")
        void forbidden() {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Origem não autorizada para renovar a sessão");
        }

        @GetMapping("/test/status/community-chat-denied")
        void communityChatDenied() {
            throw new AccessDeniedException("Only community members can access this content");
        }

        @GetMapping("/test/status/throttled")
        void throttled() {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS, "Demasiadas tentativas de login");
        }
    }

    private MockMvc mvc() {
        return MockMvcBuilders.standaloneSetup(new StatusFixture())
                .setControllerAdvice(new ExceptionHandling())
                .build();
    }

    @Test
    void forbiddenOriginReturns403InsteadOf500() throws Exception {
        mvc().perform(get("/test/status/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatusCode").value(403))
                .andExpect(jsonPath("$.message")
                        .value("Origem não autorizada para renovar a sessão"));
    }

    @Test
    void nonMemberChatReadReturns403WithoutExposingInternalMembershipReason() throws Exception {
        mvc().perform(get("/test/status/community-chat-denied"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatusCode").value(403))
                .andExpect(jsonPath("$.message").value("Não tens permissões suficientes."));
    }

    @Test
    void temporaryLoginThrottleReturns429InsteadOf500() throws Exception {
        mvc().perform(get("/test/status/throttled"))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.httpStatusCode").value(429))
                .andExpect(jsonPath("$.message").value("Demasiadas tentativas de login"));
    }
}
