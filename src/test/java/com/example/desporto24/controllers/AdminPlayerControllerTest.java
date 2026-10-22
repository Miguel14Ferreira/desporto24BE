package com.example.desporto24.controllers;

import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.model.community.CommunityRoundEntry;
import com.example.desporto24.enumeration.CommunityMatchResult;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminPlayerControllerTest {
    private final ProfileRepository repository = mock(ProfileRepository.class);
    private final CommunityRoundEntryRepository entries = mock(CommunityRoundEntryRepository.class);
    private final AdminPlayerController controller = new AdminPlayerController(repository, entries);

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
    void aggregatesLinkedPlayerRoundStatisticsWithoutUsingSearchResults() {
        when(repository.existsById(7L)).thenReturn(true);
        when(entries.findEntriesForProfile(7L)).thenReturn(List.of(
                CommunityRoundEntry.builder().result(CommunityMatchResult.WIN)
                        .goals(3).assists(1).mvp(true).cleanSheet(false).build(),
                CommunityRoundEntry.builder().result(CommunityMatchResult.LOSS)
                        .goals(0).assists(2).mvp(false).cleanSheet(true).build(),
                CommunityRoundEntry.builder().result(CommunityMatchResult.DRAW)
                        .goals(1).assists(0).mvp(false).cleanSheet(false).build()
        ));
        var stats = controller.statistics(7L);
        assertEquals(3, stats.games());
        assertEquals(1, stats.wins());
        assertEquals(1, stats.draws());
        assertEquals(1, stats.losses());
        assertEquals(4, stats.goals());
        assertEquals(3, stats.assists());
        assertEquals(1, stats.mvps());
        assertEquals(1, stats.cleanSheets());
        assertEquals(33.33, stats.winRate());
        verify(entries).findEntriesForProfile(7L);
    }

    @Test
    void unknownPlayerDoesNotQueryStats() {
        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> controller.statistics(999L));
        verifyNoInteractions(entries);
    }

    @Test
    void invalidProfileIdDoesNotQueryPlayerRecords() {
        var exception = assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> controller.statistics(-1L));
        assertEquals(404, exception.getStatusCode().value());
        verifyNoInteractions(repository, entries);
    }

    @Test
    void existingPlayerWithoutRoundsHasZeroStatistics() {
        when(repository.existsById(7L)).thenReturn(true);
        when(entries.findEntriesForProfile(7L)).thenReturn(List.of());
        var stats = controller.statistics(7L);
        assertEquals(0, stats.games());
        assertEquals(0.0, stats.winRate());
    }

    @Test
    void rejectsNegativePages() {
        assertThrows(org.springframework.web.server.ResponseStatusException.class,
                () -> controller.search("ana", -1));
        verifyNoInteractions(repository);
    }
}
