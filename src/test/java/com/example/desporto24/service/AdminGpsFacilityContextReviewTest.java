package com.example.desporto24.service;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminGpsFacilityContextReviewTest {
    private static Venue paco() {
        return Venue.builder().id(285L).name("Polidesportivo Municipal do Paço")
                .city("Maia").district("Porto")
                .address("Rua do Paço, Pedrouços, 4425-678 Maia")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://www.cm-maia.pt/viver/desporto/instalacoes-desportivas-car/"
                        + "instalacoes-desportivas/poi-37/polidesportivo-municipal-do-paco")
                .build();
    }

    private static Venue fojo() {
        return Venue.builder().id(286L).name("Campo de Padel do Fojo")
                .city("Mira").district("Coimbra").address("Fojo, Seixo de Mira, Mira")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://www.cm-mira.pt/node/4084").build();
    }

    @Test
    void twoOfficialVenuesHaveDocumentationOnlyWithoutInferredCoordinates() {
        var maia = paco();
        var mira = fojo();
        var first = AdminPlatformOverviewService.facilityContextReview(maia);
        var second = AdminPlatformOverviewService.facilityContextReview(mira);
        assertNotNull(first);
        assertEquals("DISTINCT_MUNICIPAL_SPORTS_VENUES_NO_POINT", first.issueCode());
        assertTrue(first.officialSourceUrl().startsWith("https://www.cm-maia.pt/"));
        assertTrue(first.relatedSourceUrl().startsWith("https://www.cm-maia.pt/"));
        assertNotNull(second);
        assertEquals("SHARED_COMPLEX_DIFFERENT_SPORTS_NO_POINT", second.issueCode());
        assertEquals("https://www.cm-mira.pt/node/4084", second.officialSourceUrl());
        assertEquals("https://www.cm-mira.pt/node/3308", second.relatedSourceUrl());
        assertNull(maia.getLatitude());
        assertNull(maia.getLongitude());
        assertNull(mira.getLatitude());
        assertNull(mira.getLongitude());
    }

    @Test
    void rejectStaleIdentitySourceOrGpsAndNeverGeneralizeToOtherVenues() {
        var original = paco();
        original.setLatitude(41.0);
        assertNull(AdminPlatformOverviewService.facilityContextReview(original));
        original = paco();
        original.setLongitude(-8.0);
        assertNull(AdminPlatformOverviewService.facilityContextReview(original));
        original = paco();
        original.setName("Polidesportivo Municipal de Pedrouços");
        assertNull(AdminPlatformOverviewService.facilityContextReview(original));
        original = paco();
        original.setAddress("Outra rua");
        assertNull(AdminPlatformOverviewService.facilityContextReview(original));
        original = paco();
        original.setId(99999L);
        assertNull(AdminPlatformOverviewService.facilityContextReview(original));
        original = paco();
        original.setInformationSource(Venue.InformationSource.COMMUNITY);
        assertNull(AdminPlatformOverviewService.facilityContextReview(original));
        original = paco();
        original.setWebsite("https://outro.example.com/");
        assertNull(AdminPlatformOverviewService.facilityContextReview(original));
        original = fojo();
        original.setCity("Vila Nova de Gaia");
        assertNull(AdminPlatformOverviewService.facilityContextReview(original));
        original = fojo();
        original.setDistrict("Porto");
        assertNull(AdminPlatformOverviewService.facilityContextReview(original));
        original = fojo();
        original.setName("Campo de Futebol do Fojo");
        assertNull(AdminPlatformOverviewService.facilityContextReview(original));
        assertNull(AdminPlatformOverviewService.facilityContextReview(null));
    }

    @Test
    void focusedQueueFiltersOrdersAndRequiresCurrentEvidenceWithoutWriting() {
        var repo = mock(VenueRepository.class);
        var service = new AdminPlatformOverviewService(null, null, null, null, repo, null, null);
        var maia = paco();
        var mira = fojo();
        var unrelated = Venue.builder().id(99999L).name("Outra instalação").city("Maia")
                .district("Porto").build();
        when(repo.findAllById(any())).thenReturn(List.of(mira, unrelated, maia));

        var all = service.missingGpsReview("", 0, true);
        assertEquals(List.of(285L, 286L), all.venues().stream()
                .map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        assertNull(all.venues().getFirst().researchCandidate());
        assertNull(all.venues().getFirst().addressReview());
        assertNull(all.venues().getLast().possibleDuplicateReview());
        assertNotNull(all.venues().getFirst().facilityContextReview());
        assertNotNull(all.venues().getLast().facilityContextReview());
        assertFalse(all.hasMore());
        assertEquals(List.of(285L), service.missingGpsReview("Porto", 0, true).venues()
                .stream().map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        assertEquals(List.of(286L), service.missingGpsReview("Coimbra", 0, true).venues()
                .stream().map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        assertTrue(service.missingGpsReview("", 1, true).venues().isEmpty());
        maia.setLongitude(-8.0);
        assertEquals(List.of(286L), service.missingGpsReview("", 0, true).venues()
                .stream().map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        verify(repo, never()).save(any());
        verify(repo, never()).missingGpsReview(any(), any());
    }
}
