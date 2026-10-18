package com.example.desporto24.service;

import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** No database access: verifies aggregation of the read-only district query. */
class VenueCoverageReportTest {
    @Test
    void retainsUnknownDistrictAndReportsMissingCoordinatesAndSports() {
        VenueRepository repo = mock(VenueRepository.class);
        VenueService service = new VenueService(repo, mock(ProfileRepository.class));
        when(repo.countCoverageByDistrict()).thenReturn(List.of(
                new Object[] {"(sem distrito)", 3L, 2L, 1L, 0L},
                new Object[] {"Lisboa", 7L, 1L, 2L, 4L},
                new Object[] {"Porto", 5L, 0L, 0L, 2L}
        ));
        var result = service.coverage();
        assertEquals(15L, result.totalVenues());
        assertEquals(3L, result.withoutDistrict());
        assertEquals(3L, result.withoutCoordinates());
        assertEquals(3L, result.withoutSports());
        assertEquals(3, result.districts().size());
        assertEquals("Lisboa", result.districts().get(1).district());
        assertEquals(4L, result.districts().get(1).openStreetMapVenues());
        verify(repo).countCoverageByDistrict();
        verify(repo, never()).findAllByOrderByNameAsc();
    }

    @Test
    void reportsAnEmptyCatalogWithoutInventingDistrictCoverage() {
        VenueRepository repo = mock(VenueRepository.class);
        when(repo.countCoverageByDistrict()).thenReturn(List.of());
        var result = new VenueService(repo, mock(ProfileRepository.class)).coverage();
        assertEquals(0L, result.totalVenues());
        assertTrue(result.districts().isEmpty());
    }
}
