package com.example.desporto24.service;

import com.example.desporto24.model.dto.venue.OpenStreetMapVenueImportRequest;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Regression: only the explicitly supplied district is persisted. */
class VenueOpenStreetMapDistrictTest {
    private final VenueRepository repository = mock(VenueRepository.class);
    private final VenueService service = new VenueService(repository, mock(ProfileRepository.class));

    private void acceptingSave() {
        when(repository.save(any(Venue.class))).thenAnswer(call -> {
            Venue v = call.getArgument(0);
            if (v.getId() == null) v.setId(100L);
            return v;
        });
    }

    @Test
    void importsExplicitDistrictWithoutGuessingFromCity() {
        acceptingSave();
        var request = new OpenStreetMapVenueImportRequest(
                "OSM:way/123", "Campo Municipal", "Cascais", "Lisboa",
                "Rua do Desporto", "Futsal", "Campo",
                Venue.EnvironmentType.OUTDOOR, 38.7, -9.1);
        service.importOpenStreetMapVenues(List.of(request));
        var saved = org.mockito.ArgumentCaptor.forClass(Venue.class);
        verify(repository).save(saved.capture());
        assertEquals("Cascais", saved.getValue().getCity());
        assertEquals("Lisboa", saved.getValue().getDistrict());
    }

    @Test
    void nullDistrictDoesNotEraseAReviewedExistingValue() {
        Venue existing = Venue.builder()
                .id(9L).district("Setúbal").informationSource(Venue.InformationSource.OPENSTREETMAP)
                .sourceReference("OSM:way/8").build();
        when(repository.findByInformationSourceAndSourceReference(
                Venue.InformationSource.OPENSTREETMAP, "OSM:way/8"))
                .thenReturn(Optional.of(existing));
        acceptingSave();
        var request = new OpenStreetMapVenueImportRequest(
                "OSM:way/8", "Pavilhão", "Almada", null,
                "Rua do Campo", "Basquetebol", "Pavilhão",
                Venue.EnvironmentType.INDOOR, 38.68, -9.15);
        service.importOpenStreetMapVenues(List.of(request));
        assertEquals("Setúbal", existing.getDistrict());
    }
}
