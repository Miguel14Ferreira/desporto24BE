package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.venue.VenueSearchPageResponse;
import com.example.desporto24.service.VenueMapLinkService;
import com.example.desporto24.service.VenuePhotoService;
import com.example.desporto24.service.VenueService;
import com.example.desporto24.service.VenueSportConditionService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VenuePagedSearchControllerTest {
    private final VenueService service = mock(VenueService.class);
    private final VenueController controller = new VenueController(
            service, mock(VenueMapLinkService.class), mock(VenuePhotoService.class),
            mock(VenueSportConditionService.class));

    @Test
    void forwardsFiltersAndPageWithoutChangingLegacySearch() {
        when(service.suggest("  Pavilhão ", " Lisboa ", "Futsal", 2, null))
                .thenReturn(new VenueSearchPageResponse(List.of(), 2, false));
        var response = controller.suggest("  Pavilhão ", " Lisboa ", "Futsal", 2);
        assertEquals(2, response.page());
        verify(service).suggest("  Pavilhão ", " Lisboa ", "Futsal", 2, null);
        verify(service, never()).search(any(), any(), any(), any(), any());
    }

    @Test
    void forwardsDistrictToBackendInsteadOfLoadingAllVenues() {
        controller.suggest("Campo", "", "Padel", 0, "Setúbal");
        verify(service).suggest("Campo", "", "Padel", 0, "Setúbal");
    }
}
