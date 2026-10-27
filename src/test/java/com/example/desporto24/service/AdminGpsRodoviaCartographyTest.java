package com.example.desporto24.service;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminGpsRodoviaCartographyTest {
    private static final String MUNICIPAL =
            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-";

    private static Venue field(long id, String name, String item) {
        return Venue.builder().id(id).name(name).city("Braga").district("Braga")
                .address("Complexo Desportivo da Rodovia, 4710-294 Braga")
                .website(MUNICIPAL + item).informationSource(Venue.InformationSource.OFFICIAL)
                .build();
    }

    private static List<Venue> allRodovia() {
        return List.of(
                field(288L, "Campo de Futebol N.º1 do Complexo Desportivo da Rodovia", "6223"),
                field(289L, "Campo de Futebol N.º3 do Complexo Desportivo da Rodovia", "6224"),
                field(290L, "Campo de Futebol 7 N.º2 do Complexo Desportivo da Rodovia", "6225"),
                field(291L, "Campo de Futebol 7 N.º4 do Complexo Desportivo da Rodovia", "6226"),
                field(292L, "Campo de Futebol N.º 5 do Complexo Desportivo da Rodovia", "6229"),
                field(293L, "Campo de Futebol N.º 6 do Complexo Desportivo da Rodovia", "6230"),
                field(294L, "Campos de Basquetebol N.º 1 e 2 do Complexo Desportivo da Rodovia", "6231"));
    }

    @Test
    void everyNumberedBragaReviewShowsTheSameNonPitchMapReferencesWithoutCoordinates() {
        for (Venue venue : allRodovia()) {
            var lead = AdminPlatformOverviewService.facilityContextReview(venue);
            assertNotNull(lead, "Research for " + venue.getId());
            var map = lead.cartographyReview();
            assertNotNull(map);
            assertEquals("ADJACENT_PARK_AND_SPORTS_BUILDING_NOT_NUMBERED_PITCH", map.issueCode());
            assertEquals("https://juventude.cm-braga.pt/wp-content/uploads/2023/06/"
                    + "Roteiro-Desportivo-e-Lazer-2020.pdf", map.municipalGuideUrl());
            assertEquals("https://mapcarta.com/pt/W106454979", map.parkAreaUrl());
            assertEquals("https://mapcarta.com/pt/W884706818", map.sportsBuildingUrl());
            assertNull(venue.getLatitude());
            assertNull(venue.getLongitude());
            assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        }
    }

    @Test
    void reviewCannotLeakOntoOtherVenuesOrStaleSavedIdentities() {
        var maia = field(285L, "Polidesportivo Municipal do Paço", "6223");
        assertNull(AdminPlatformOverviewService.facilityContextReview(maia));
        var first = allRodovia().getFirst();
        first.setLongitude(-8.4);
        assertNull(AdminPlatformOverviewService.facilityContextReview(first));
        var third = allRodovia().get(1);
        third.setLatitude(41.5);
        assertNull(AdminPlatformOverviewService.facilityContextReview(third));
        var fifth = allRodovia().get(4);
        fifth.setName("Outro campo");
        assertNull(AdminPlatformOverviewService.facilityContextReview(fifth));
        var basketball = allRodovia().getLast();
        basketball.setId(295L);
        assertNull(AdminPlatformOverviewService.facilityContextReview(basketball));
        basketball = allRodovia().getLast();
        basketball.setWebsite("https://other.example.org/");
        assertNull(AdminPlatformOverviewService.facilityContextReview(basketball));
        basketball = allRodovia().getLast();
        basketball.setInformationSource(Venue.InformationSource.COMMUNITY);
        assertNull(AdminPlatformOverviewService.facilityContextReview(basketball));
    }

    @Test
    void focusQueueOnlyReportsPersistedCurrentDocumentsAndNeverWrites() {
        var repository = mock(VenueRepository.class);
        var service = new AdminPlatformOverviewService(null, null, null, null, repository, null, null);
        var records = allRodovia();
        when(repository.findAllById(any())).thenReturn(records);
        var focused = service.missingGpsReview("Braga", 0, true);
        assertEquals(List.of(288L, 289L, 290L, 291L, 292L, 293L, 294L),
                focused.venues().stream().map(AdminPlatformOverviewService.VenueGpsReviewItem::id)
                        .toList());
        assertTrue(focused.venues().stream().allMatch(
                row -> row.facilityContextReview().cartographyReview() != null));
        assertTrue(focused.venues().stream().allMatch(row -> row.researchCandidate() == null));
        assertTrue(service.missingGpsReview("Porto", 0, true).venues().isEmpty());
        records.getFirst().setLatitude(41.5);
        assertEquals(6, service.missingGpsReview("Braga", 0, true).venues().size());
        verify(repository, never()).save(any());
        verify(repository, never()).missingGpsReview(any(), any());
    }
}
