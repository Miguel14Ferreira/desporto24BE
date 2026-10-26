package com.example.desporto24.service;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import com.example.desporto24.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VenuePagedSearchServiceTest {
    private final VenueRepository repository = mock(VenueRepository.class);
    private final VenueService service = new VenueService(
            repository, mock(ProfileRepository.class), mock(SessionDistanceCalculator.class));

    @Test
    void normalizesAccentsAndReturnsASmallPageInsteadOfReadingEverything() {
        Venue venue = Venue.builder().id(7L).name("Pavilhão da Estação")
                .city("Lisboa").address("Rua A").sports("Futsal").build();
        when(repository.suggest("pavilhao", "estacao", "futsal", "",
                PageRequest.of(1, 39)))
                .thenReturn(new SliceImpl<>(List.of(venue), PageRequest.of(1, 39), true));
        var page = service.suggest("PAVILHÃO", "Estação", "Futsal", 1);

        assertEquals(1, page.page());
        assertTrue(page.hasMore());
        assertEquals(List.of(7L), page.venues().stream().map(v -> v.id()).toList());
        verify(repository).suggest("pavilhao", "estacao", "futsal", "", PageRequest.of(1, 39));
        verify(repository, never()).findAllByOrderByNameAsc();
    }

    @Test
    void treatsLikeWildcardsAsLiteralText() {
        when(repository.suggest("campo!_!%", "", "", "",
                PageRequest.of(0, 39)))
                .thenReturn(new SliceImpl<>(List.of()));
        service.suggest("Campo_%", null, null, 0);
        verify(repository).suggest("campo!_!%", "", "", "", PageRequest.of(0, 39));
    }

    @Test
    void rejectsInvalidPagesAndLongSearchTermsBeforeDatabaseAccess() {
        assertThrows(IllegalArgumentException.class, () -> service.suggest(null, null, null, -1));
        assertThrows(IllegalArgumentException.class, () -> service.suggest(null, null, null, 1001));
        assertThrows(IllegalArgumentException.class, () -> service.suggest("x".repeat(121), null, null, 0));
        assertThrows(IllegalArgumentException.class, () ->
                service.suggest("", "", "Padel", 0, "Paris"));
        verifyNoInteractions(repository);
    }

    @Test
    void passesBothAutonomousRegionsToPaginatedRepositoryWithoutReadingAllVenues() {
        when(repository.suggest("", "", "", "Açores", PageRequest.of(0, 39)))
                .thenReturn(new SliceImpl<>(List.of()));
        when(repository.suggest("", "", "", "Madeira", PageRequest.of(0, 39)))
                .thenReturn(new SliceImpl<>(List.of()));
        service.suggest(null, null, null, 0, "ACORES");
        service.suggest(null, null, null, 0, "madeira");
        verify(repository).suggest("", "", "", "Açores", PageRequest.of(0, 39));
        verify(repository).suggest("", "", "", "Madeira", PageRequest.of(0, 39));
        verify(repository, never()).findAllByOrderByNameAsc();
    }

    @Test
    void normalizesAccentedDistrictAndFiltersInTheDatabase() {
        when(repository.suggest("", "", "padel", "Évora", PageRequest.of(0, 39)))
                .thenReturn(new SliceImpl<>(List.of()));
        service.suggest("", "", "Padel", 0, "ÉVORA");
        verify(repository).suggest("", "", "padel", "Évora", PageRequest.of(0, 39));
        verify(repository, never()).findAllByOrderByNameAsc();
    }
}
