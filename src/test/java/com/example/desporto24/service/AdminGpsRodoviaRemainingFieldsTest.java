package com.example.desporto24.service;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminGpsRodoviaRemainingFieldsTest {

    private static final String MUNICIPAL =
            "https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-";
    private static final String PARK =
            "https://www.cm-braga.pt/pt/1201/viver/ambiente/espacos-verdes/item/item-1-722";

    private static Venue venue(long id, String name, String page) {
        return Venue.builder().id(id).name(name).city("Braga").district("Braga")
                .address("Complexo Desportivo da Rodovia, 4710-294 Braga")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website(MUNICIPAL + page).build();
    }

    private static Venue football7Two() {
        return venue(290L, "Campo de Futebol 7 N.º2 do Complexo Desportivo da Rodovia", "6225");
    }

    private static Venue football7Four() {
        return venue(291L, "Campo de Futebol 7 N.º4 do Complexo Desportivo da Rodovia", "6226");
    }

    private static Venue footballFive() {
        return venue(292L, "Campo de Futebol N.º 5 do Complexo Desportivo da Rodovia", "6229");
    }

    private static Venue footballSix() {
        return venue(293L, "Campo de Futebol N.º 6 do Complexo Desportivo da Rodovia", "6230");
    }

    private static Venue basketballPair() {
        return venue(294L, "Campos de Basquetebol N.º 1 e 2 do Complexo Desportivo da Rodovia", "6231");
    }

    @Test
    void eachExistingFootballPitchKeepsItsOwnMunicipalPageAndPeer() {
        var records = List.of(football7Two(), football7Four(), footballFive(), footballSix());
        var pages = List.of("6225", "6226", "6229", "6230");
        var peerPages = List.of("6226", "6225", "6230", "6229");
        for (int i = 0; i < records.size(); i++) {
            var v = records.get(i);
            var lead = AdminPlatformOverviewService.facilityContextReview(v);
            assertNotNull(lead, "Named field " + v.getId());
            assertEquals("SHARED_COMPLEX_NUMBERED_FIELDS_NO_POINT", lead.issueCode());
            assertEquals(MUNICIPAL + pages.get(i), lead.officialSourceUrl());
            assertEquals(MUNICIPAL + peerPages.get(i), lead.relatedSourceUrl());
            assertNull(v.getLatitude());
            assertNull(v.getLongitude());
            assertNull(AdminPlatformOverviewService.researchCandidate(v),
                    "A municipal identity page is not a verified GPS site point");
        }
    }

    @Test
    void combinedBasketballRecordDoesNotInferTwoExistingDatabaseRecordsOrGps() {
        var v = basketballPair();
        var lead = AdminPlatformOverviewService.facilityContextReview(v);
        assertNotNull(lead);
        assertEquals("GROUPED_BASKETBALL_COURTS_NO_POINT", lead.issueCode());
        assertEquals(MUNICIPAL + "6231", lead.officialSourceUrl());
        assertEquals(PARK, lead.relatedSourceUrl());
        assertNull(v.getLatitude());
        assertNull(v.getLongitude());
        assertNull(AdminPlatformOverviewService.researchCandidate(v));
    }

    @Test
    void changingSavedNameNumberSourceLocationOrGpsInvalidatesInvestigation() {
        var v = football7Two();
        v.setName("Campo de Futebol 7 N.º4 do Complexo Desportivo da Rodovia");
        assertNull(AdminPlatformOverviewService.facilityContextReview(v));
        v = football7Four();
        v.setWebsite(MUNICIPAL + "6225");
        assertNull(AdminPlatformOverviewService.facilityContextReview(v));
        v = footballFive();
        v.setId(289L);
        assertNull(AdminPlatformOverviewService.facilityContextReview(v));
        v = footballSix();
        v.setLongitude(-8.4);
        assertNull(AdminPlatformOverviewService.facilityContextReview(v));
        v = footballSix();
        v.setLatitude(41.5);
        assertNull(AdminPlatformOverviewService.facilityContextReview(v));
        v = footballSix();
        v.setDistrict("Porto");
        assertNull(AdminPlatformOverviewService.facilityContextReview(v));
        v = basketballPair();
        v.setName("Campo de Basquetebol N.º 1 do Complexo Desportivo da Rodovia");
        assertNull(AdminPlatformOverviewService.facilityContextReview(v));
        v = basketballPair();
        v.setInformationSource(Venue.InformationSource.COMMUNITY);
        assertNull(AdminPlatformOverviewService.facilityContextReview(v));
        v = basketballPair();
        v.setAddress("Outra morada");
        assertNull(AdminPlatformOverviewService.facilityContextReview(v));
        v = basketballPair();
        v.setCity("Guimarães");
        assertNull(AdminPlatformOverviewService.facilityContextReview(v));
    }

    @Test
    void reviewedQueueIncludesOnlyExactCurrentBragaIdsInSortedOrderWithoutWrites() {
        var repo = mock(VenueRepository.class);
        var service = new AdminPlatformOverviewService(null, null, null, null, repo, null, null);
        var records = List.of(basketballPair(), footballSix(), footballFive(),
                football7Four(), football7Two());
        when(repo.findAllById(any())).thenReturn(records);
        var response = service.missingGpsReview("Braga", 0, true);
        assertEquals(List.of(290L, 291L, 292L, 293L, 294L),
                response.venues().stream().map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        assertFalse(response.hasMore());
        assertEquals(0, response.page());
        assertNull(response.venues().getFirst().researchCandidate());
        assertNull(response.venues().getLast().researchCandidate());
        assertTrue(service.missingGpsReview("Coimbra", 0, true).venues().isEmpty());
        assertTrue(service.missingGpsReview("Braga", 1, true).venues().isEmpty());
        records.getFirst().setLatitude(41.5);
        assertEquals(4, service.missingGpsReview("Braga", 0, true).venues().size());
        verify(repo, never()).save(any());
        verify(repo, never()).missingGpsReview(any(), any());
    }
}
