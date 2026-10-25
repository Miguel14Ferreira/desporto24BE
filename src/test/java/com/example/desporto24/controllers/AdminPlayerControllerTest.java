package com.example.desporto24.controllers;

import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminPlayerControllerTest {
    private final ProfileRepository repository = mock(ProfileRepository.class);
    private final AdminPlayerController controller = new AdminPlayerController(repository, mock(com.example.desporto24.service.CommunityCreationQuotaService.class));

    @Test
    void blankSearchDoesNotListEveryone() {
        assertTrue(controller.search("  ", 0).players().isEmpty());
        verifyNoInteractions(repository);
    }

    @Test
    void escapesWildcardsAndLimitsResults() {
        Profile profile = new Profile();
        profile.setId(7L);
        profile.setUsername("ana");
        profile.setRole("ROLE_USER");
        profile.setEnabled(true);
        profile.setNotLocked(false);
        when(repository.searchAdminPlayers("a!%!_!!", PageRequest.of(0, 20)))
                .thenReturn(new org.springframework.data.domain.SliceImpl<>(
                        java.util.Collections.nCopies(20, profile), PageRequest.of(0, 20), true));
        var response = controller.search(" A%_! ", 0);
        assertEquals(20, response.players().size());
        assertTrue(response.hasMore());
        assertEquals("ana", response.players().get(0).username());
        assertEquals(false, response.players().get(0).notLocked());
    }

    @Test
    void rejectsNegativePages() {
        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> controller.search("ana", -1));
        verifyNoInteractions(repository);
    }
}
