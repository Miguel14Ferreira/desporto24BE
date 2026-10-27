package com.example.desporto24.service;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminGpsRodoviaNumberingPlanTest {
    private static final String MUNICIPAL =
            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-";

    private static Venue field(long id, String name, String item) {
        return Venue.builder().id(id).name(name).city("Braga").district("Braga")
                .address("Complexo Desportivo da Rodovia, 4710-294 Braga")
                .website(MUNICIPAL + item).informationSource(Venue.InformationSource.OFFICIAL)
                .build();
    }

    private static List<Venue> allFields() {
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
    void onSitePhotoIsOnlyAReviewClueAndFederationSupportsOnlyFieldOne() {
        for (var venue : allFields()) {
            var ctx = AdminPlatformOverviewService.facilityContextReview(venue);
            assertNotNull(ctx);
            var numbering = ctx.numberingPlanReview();
            assertNotNull(numbering);
            assertEquals("NUMBERED_SIGN_SCHEMATIC_WITHOUT_GEOREFERENCE", numbering.issueCode());
            assertTrue(numbering.signageGalleryUrl().startsWith("https://fr.tripadvisor.ch/"));
            if (venue.getId() == 288L) {
                assertEquals("https://afbraga.fpf.pt/DesktopModules/"
                        + "InstitutionalDocuments/DownloadFiles.ashx?IdDoc=184789",
                        numbering.fieldOneScheduleUrl());
            } else {
                assertNull(numbering.fieldOneScheduleUrl(),
                        "The federation schedule must not imply verification of a different field");
            }
            assertNull(venue.getLatitude());
            assertNull(venue.getLongitude());
            assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        }
    }

    @Test
    void noNumberingEvidenceForStaleVenuesExistingGpsOrOtherMunicipalities() {
        var venue = allFields().getFirst();
        venue.setLatitude(41.5);
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = allFields().get(1);
        venue.setLongitude(-8.4);
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = allFields().get(2);
        venue.setName("Campo de Futebol 7 N.º4 do Complexo Desportivo da Rodovia");
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = allFields().get(3);
        venue.setWebsite(MUNICIPAL + "6225");
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = allFields().get(4);
        venue.setInformationSource(Venue.InformationSource.COMMUNITY);
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = allFields().get(5);
        venue.setDistrict("Porto");
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = allFields().getLast();
        venue.setId(123456L);
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
    }

    @Test
    void focusQueueMaintainsSeparateIdsAndDoesNotModifyDatabase() {
        var repo = mock(VenueRepository.class);
        var service = new AdminPlatformOverviewService(null, null, null, null, repo, null, null);
        var data = allFields();
        when(repo.findAllById(any())).thenReturn(data.reversed());

        var braga = service.missingGpsReview("Braga", 0, true);
        assertEquals(List.of(288L, 289L, 290L, 291L, 292L, 293L, 294L),
                braga.venues().stream().map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        assertTrue(braga.venues().stream().allMatch(
                row -> row.facilityContextReview().numberingPlanReview() != null));
        assertTrue(braga.venues().stream().allMatch(row -> row.researchCandidate() == null));
        assertTrue(service.missingGpsReview("Porto", 0, true).venues().isEmpty());
        assertTrue(service.missingGpsReview("Braga", 1, true).venues().isEmpty());
        data.get(3).setLongitude(-8.4);
        assertEquals(6, service.missingGpsReview("Braga", 0, true).venues().size());
        verify(repo, never()).save(any());
        verify(repo, never()).missingGpsReview(any(), any());
    }
}
