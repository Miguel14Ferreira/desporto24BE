package com.example.desporto24.controllers;

import com.example.desporto24.configuration.SnidVenueImportData;
import com.example.desporto24.repository.venue.VenueRepository;
import com.example.desporto24.service.VenueService;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminVenueQualityControllerTest {
    private final VenueRepository repository = mock(VenueRepository.class);
    private final AdminVenueImportController target = new AdminVenueImportController(
            mock(VenueService.class), repository, mock(SnidVenueImportData.class),
            mock(TaskExecutor.class));

    @Test void returnsReadOnlyAggregateCounts() {
        when(repository.count()).thenReturn(228L);
        when(repository.qualityByCity(PageRequest.of(0, 50))).thenReturn(
                List.<Object[]>of(new Object[]{"Alcabideche", 43L, 35L, 40L, 0L}));
        when(repository.coverageByDistrict()).thenReturn(
                List.<Object[]>of(new Object[]{"Lisboa", 12L, 1L, 0L},
                                  new Object[]{null, 216L, 168L, 1L}));
        var report = target.venueQuality(50);
        assertEquals(228L, report.get("total"));
        assertEquals(169L, report.get("withoutSports"));
        assertEquals(216L, report.get("withoutDistrict"));
        assertEquals(1L, report.get("withoutCoordinates"));
        assertEquals(1, ((List<?>) report.get("cities")).size());
        verify(repository).qualityByCity(PageRequest.of(0, 50));
        verify(repository, never()).findAllByOrderByNameAsc();
    }

    @Test void validatesLimitWithoutAccessingDatabase() {
        assertThrows(ResponseStatusException.class, () -> target.venueQuality(0));
        assertThrows(ResponseStatusException.class, () -> target.venueQuality(101));
        verifyNoInteractions(repository);
    }
}
