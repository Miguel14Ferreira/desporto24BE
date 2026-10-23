package com.example.desporto24.controllers;

import com.example.desporto24.exception.ExceptionHandling;
import com.example.desporto24.model.dto.chat.DirectReadReceiptResponse;
import com.example.desporto24.service.InboxSummaryService;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InboxDirectReadReceiptHttpTest {
    private final InboxSummaryService service = mock(InboxSummaryService.class);
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new InboxController(service))
            .setControllerAdvice(new ExceptionHandling()).build();

    private UsernamePasswordAuthenticationToken loggedIn() {
        return new UsernamePasswordAuthenticationToken("ana", null);
    }

    @Test
    void authorizedRequestReturnsOnlyRecipientWatermark() throws Exception {
        when(service.peerDirectReadReceipt("ana", "bea"))
                .thenReturn(new DirectReadReceiptResponse("bea", 15L));
        mvc.perform(get("/api/messages/inbox/direct/bea/receipt").principal(loggedIn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peerUsername").value("bea"))
                .andExpect(jsonPath("$.lastReadMessageId").value(15))
                .andExpect(jsonPath("$.latestText").doesNotExist());
    }

    @Test
    void deniedFriendshipReturns403WithoutDisclosingCursor() throws Exception {
        when(service.peerDirectReadReceipt("ana", "bea"))
                .thenThrow(new AccessDeniedException("Blocked"));
        mvc.perform(get("/api/messages/inbox/direct/bea/receipt").principal(loggedIn()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Não tens permissões suficientes."))
                .andExpect(jsonPath("$.lastReadMessageId").doesNotExist());
    }
}
