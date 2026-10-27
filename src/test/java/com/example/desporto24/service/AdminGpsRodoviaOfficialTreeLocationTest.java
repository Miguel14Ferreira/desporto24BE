package com.example.desporto24.service;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminGpsRodoviaOfficialTreeLocationTest {

    private static Venue venue(long id, String name, String page) {
        return Venue.builder().id(id).name(name).city("Braga").district("Braga")
                .address("Complexo Desportivo da Rodovia, 4710-294 Braga")
                .website("https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-" + page)
                .informationSource(Venue.InformationSource.OFFICIAL).build();
    }

    private static Venue first() {
        return venue(288L, "Campo de Futebol N.º1 do Complexo Desportivo da Rodovia", "6223");
    }

    private static Venue basketball() {
        return venue(294L, "Campos de Basquetebol N.º 1 e 2 do Complexo Desportivo da Rodovia", "6231");
    }

    @Test
    void officialTreeReportIsExcludedFromAllPitchCoordinatesAndGeoportalIsResearchOnly() {
        for (var v : List.of(first(), basketball())) {
            var ctx = AdminPlatformOverviewService.facilityContextReview(v);
            assertNotNull(ctx);
            var map = ctx.cartographyReview();
            assertNotNull(map);
            assertEquals("ADJACENT_PARK_AND_SPORTS_BUILDING_NOT_NUMBERED_PITCH", map.issueCode());
            assertEquals("https://www.cm-braga.pt/archive/doc/Parque_Desportivo_da_Rodovia_0.pdf",
                    map.nonPitchTreeReportUrl());
            assertEquals("https://sigportal.cm-braga.pt/WebSIGBraga/", map.officialGeoportalUrl());
            assertNull(v.getLatitude());
            assertNull(v.getLongitude());
            assertNull(AdminPlatformOverviewService.researchCandidate(v),
                    "Document coordinates locate a poplar tree, never a playing field");
        }
    }

    @Test
    void staleNameGpsAndUnrelatedVenuesNeverReceiveFalseCoordinateAlert() {
        var pitch = first();
        pitch.setLongitude(-8.40345);
        assertNull(AdminPlatformOverviewService.facilityContextReview(pitch));
        pitch = first();
        pitch.setLatitude(41.55296);
        assertNull(AdminPlatformOverviewService.facilityContextReview(pitch));
        pitch = first();
        pitch.setName("Outra instalação");
        assertNull(AdminPlatformOverviewService.facilityContextReview(pitch));
        pitch = basketball();
        pitch.setWebsite("https://example.org/other");
        assertNull(AdminPlatformOverviewService.facilityContextReview(pitch));
        pitch = basketball();
        pitch.setId(285L);
        assertNull(AdminPlatformOverviewService.facilityContextReview(pitch));
    }

    @Test
    void districtReviewExposesOnlyCurrentSourcesWithoutDatabaseWrites() {
        var repo = mock(VenueRepository.class);
        var service = new AdminPlatformOverviewService(null, null, null, null, repo, null, null);
        var one = first();
        var two = basketball();
        when(repo.findAllById(any())).thenReturn(List.of(two, one));
        var result = service.missingGpsReview("Braga", 0, true);
        assertEquals(List.of(288L, 294L), result.venues().stream()
                .map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        assertTrue(result.venues().stream().allMatch(
                row -> row.facilityContextReview().cartographyReview().nonPitchTreeReportUrl()
                        .startsWith("https://www.cm-braga.pt/")));
        assertTrue(result.venues().stream().allMatch(row -> row.researchCandidate() == null));
        assertTrue(service.missingGpsReview("Porto", 0, true).venues().isEmpty());
        one.setLatitude(41.55);
        assertEquals(List.of(294L), service.missingGpsReview("Braga", 0, true).venues().stream()
                .map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        verify(repo, never()).save(any());
        verify(repo, never()).missingGpsReview(any(), any());
    }
}
