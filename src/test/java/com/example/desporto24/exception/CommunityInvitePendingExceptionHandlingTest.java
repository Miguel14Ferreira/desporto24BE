package com.example.desporto24.exception;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommunityInvitePendingExceptionHandlingTest {

    @RestController
    static class InviteFixture {
        @PostMapping("/test/invite")
        void invite() {
            throw new CommunityInvitePendingException();
        }
    }

    @Test
    void pendingInviteRespondsWithConflictAndActionableMessage() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new InviteFixture())
                .setControllerAdvice(new ExceptionHandling()).build();
        mvc.perform(post("/test/invite"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.httpStatusCode").value(409))
                .andExpect(jsonPath("$.message").value(
                        "Já existe um convite pendente para este utilizador. Aguarda a aceitação."));
    }
}
