package com.example.desporto24.service;

import com.example.desporto24.model.ConversationNotificationMute;
import com.example.desporto24.repository.ConversationNotificationMuteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationNotificationMuteServiceTest {
    @Mock ConversationNotificationMuteRepository repository;
    @InjectMocks ConversationNotificationMuteService service;

    @Test void normalizesAndRejectsInvalidConversationReferences() {
        assertEquals("direct", ConversationNotificationMuteService.kind("DIRECT"));
        assertEquals("bea", ConversationNotificationMuteService.key("direct", "Bea"));
        assertEquals("9", ConversationNotificationMuteService.key("session", "0009"));
        assertThrows(ResponseStatusException.class, () -> ConversationNotificationMuteService.kind("admin"));
        assertThrows(ResponseStatusException.class, () -> ConversationNotificationMuteService.key("community", "../9"));
        assertThrows(ResponseStatusException.class, () -> ConversationNotificationMuteService.key("direct", "x/y"));
        assertThrows(ResponseStatusException.class, () -> ConversationNotificationMuteService.key("private", "0"));
    }

    @Test void storesAndClearsOnlyTheSelectedUsersConversation() {
        var stored = new ConversationNotificationMute();
        stored.setUsername("bea");
        stored.setConversationKind("direct");
        stored.setConversationKey("ana");
        when(repository.findByUsernameAndConversationKindAndConversationKey("bea", "direct", "ana"))
            .thenReturn(Optional.empty(), Optional.of(stored));
        service.update("BEA", "direct", "ana", true);
        verify(repository).save(argThat(m -> m.getUsername().equals("bea")
            && m.getConversationKind().equals("direct") && m.getConversationKey().equals("ana")));
        service.update("BEA", "direct", "ana", false);
        verify(repository).delete(stored);
    }

    @Test void onlyReturnsAuthenticatedAccountMutes() {
        var muted = new ConversationNotificationMute();
        muted.setConversationKind("community");
        muted.setConversationKey("12");
        when(repository.findByUsername("bea")).thenReturn(List.of(muted));
        assertEquals(List.of(new ConversationNotificationMuteService.ConversationMute("community","12")),
            service.list("BEA"));
        verify(repository).findByUsername("bea");
    }
}
