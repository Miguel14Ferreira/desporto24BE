package com.example.desporto24.service;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminGpsBragaRodoviaContextReviewTest {
    private static Venue pitch(long id, int number, String page) {
        return Venue.builder().id(id)
                .name("Campo de Futebol N.º" + number + " do Complexo Desportivo da Rodovia")
                .city("Braga").district("Braga")
                .address("Complexo Desportivo da Rodovia, 4710-294 Braga")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://www.cm-braga.pt/pt/1201/viver/desporto/equipamentos-desportivas/item/item-1-" + page)
                .build();
    }

    private static Venue first() { return pitch(288L, 1, "6223"); }
    private static Venue third() { return pitch(289L, 3, "6224"); }

    @Test
    void separateNumberedFieldsHaveTheirOwnOfficialSourcesNotSharedGps() {
        var first = first();
        var third = third();
        var review1 = AdminPlatformOverviewService.facilityContextReview(first);
        var review3 = AdminPlatformOverviewService.facilityContextReview(third);
        assertNotNull(review1);
        assertNotNull(review3);
        assertEquals("SHARED_COMPLEX_NUMBERED_FIELDS_NO_POINT", review1.issueCode());
        assertEquals(review1.issueCode(), review3.issueCode());
        assertTrue(review1.officialSourceUrl().endsWith("/item/item-1-6223"));
        assertTrue(review1.relatedSourceUrl().endsWith("/item/item-1-6224"));
        assertTrue(review3.officialSourceUrl().endsWith("/item/item-1-6224"));
        assertTrue(review3.relatedSourceUrl().endsWith("/item/item-1-6223"));
        assertNull(first.getLatitude());
        assertNull(first.getLongitude());
        assertNull(third.getLatitude());
        assertNull(third.getLongitude());
        assertNull(AdminPlatformOverviewService.researchCandidate(first));
        assertNull(AdminPlatformOverviewService.researchCandidate(third));
    }

    @Test
    void exactSavedIdentitiesAndAbsentGpsAreMandatory() {
        var venue = first();
        venue.setLongitude(-8.40);
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = first();
        venue.setLatitude(41.55);
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = first();
        venue.setId(290L);
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = first();
        venue.setName("Campo de Futebol N.º3 do Complexo Desportivo da Rodovia");
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = first();
        venue.setAddress("Avenida João Paulo II");
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = first();
        venue.setWebsite("https://unverified.example/");
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = first();
        venue.setInformationSource(Venue.InformationSource.OPENSTREETMAP);
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = third();
        venue.setCity("Guimarães");
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
        venue = third();
        venue.setDistrict("Porto");
        assertNull(AdminPlatformOverviewService.facilityContextReview(venue));
    }

    @Test
    void focusQueueKeepsBothDistinctWithDistrictOrderingAndNoWrites() {
        var repo = mock(VenueRepository.class);
        var service = new AdminPlatformOverviewService(null, null, null, null, repo, null, null);
        var one = first();
        var three = third();
        when(repo.findAllById(any())).thenReturn(List.of(three, one));

        var all = service.missingGpsReview("Braga", 0, true);
        assertEquals(List.of(288L, 289L), all.venues().stream()
                .map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        assertEquals("SHARED_COMPLEX_NUMBERED_FIELDS_NO_POINT",
                all.venues().getFirst().facilityContextReview().issueCode());
        assertNull(all.venues().getFirst().researchCandidate());
        assertNull(all.venues().getLast().possibleDuplicateReview());
        assertTrue(service.missingGpsReview("Porto", 0, true).venues().isEmpty());
        assertTrue(service.missingGpsReview("Braga", 1, true).venues().isEmpty());
        one.setLongitude(-8.42);
        assertEquals(List.of(289L), service.missingGpsReview("Braga", 0, true).venues().stream()
                .map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        verify(repo, never()).save(any());
        verify(repo, never()).missingGpsReview(any(), any());
    }
}
