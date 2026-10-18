package com.example.desporto24.service;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Offline paging contract regression. No PostgreSQL or Render connections.
 */
class VenueSuggestionPagingTest {
    private VenueRepository repository;
    private VenueService service;

    @BeforeEach
    void setUp() {
        repository = mock(VenueRepository.class);
        service = new VenueService(repository, mock(ProfileRepository.class));
    }

    private Venue venue(long id, String name, String city) {
        return Venue.builder().id(id).name(name).city(city)
                .address("Rua do Desporto").sports("Padel; Ténis").build();
    }

    @Test
    void pagesAreBoundedAndHaveMoreUsesOneLookaheadRow() {
        List<Venue> fortyOne = IntStream.rangeClosed(1, 41)
                .mapToObj(i -> venue(i, "Campo " + i, "Lisboa")).toList();
        when(repository.findSuggestions(eq("campo"), eq("lisboa"), eq("padel"), any(Pageable.class)))
                .thenReturn(fortyOne);

        var response = service.suggest("  CAMPO  ", " LISBOA ", " PADEL ", 2);
        assertEquals(2, response.page());
        assertTrue(response.hasMore());
        assertEquals(40, response.venues().size());
        assertEquals(1L, response.venues().getFirst().id());
        assertEquals(40L, response.venues().getLast().id());

        var pageCaptor = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findSuggestions(eq("campo"), eq("lisboa"), eq("padel"),
                pageCaptor.capture());
        assertEquals(2, pageCaptor.getValue().getPageNumber());
        assertEquals(41, pageCaptor.getValue().getPageSize());
        verify(repository, never()).findAllByOrderByNameAsc();
    }

    @Test
    void lastAndEmptyPagesDoNotClaimMoreResults() {
        when(repository.findSuggestions(eq(""), eq(""), eq(""), any(Pageable.class)))
                .thenReturn(List.of(venue(99L, "Campo Único", "Porto")), List.of());
        var last = service.suggest(null, null, null, 0);
        assertEquals(1, last.venues().size());
        assertFalse(last.hasMore());
        var empty = service.suggest("", "", "", 1);
        assertEquals(1, empty.page());
        assertTrue(empty.venues().isEmpty());
        assertFalse(empty.hasMore());
    }

    @Test
    void refusesUnboundedPageIndicesWithoutReadingRepository() {
        assertThrows(IllegalArgumentException.class, () -> service.suggest("", "", "", -1));
        assertThrows(IllegalArgumentException.class, () -> service.suggest("", "", "", 1001));
        verifyNoInteractions(repository);
    }
}
