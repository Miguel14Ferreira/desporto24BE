package com.example.desporto24.service;

import com.example.desporto24.model.venue.Venue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminGpsOuteiroResearchTest {
    private Venue existing() {
        return Venue.builder()
                .id(283L).name("Campo Municipal do Outeiro")
                .city("Porto").district("Porto")
                .address("Rua D. Afonso Henriques, Paranhos, Porto")
                .website("https://www.agoraporto.pt/noticias/desporto/desporto/rmid")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .build();
    }

    @Test
    void candidateIsExplicitlyResearchOnly() {
        var venue = existing();
        var candidate = AdminPlatformOverviewService.researchCandidate(venue);
        assertNotNull(candidate);
        assertEquals(41.17589, candidate.latitude(), 0.00000001);
        assertEquals(-8.59259, candidate.longitude(), 0.00000001);
        assertEquals("SITE_REFERENCE_NOT_VERIFIED_ENTRANCE", candidate.precision());
        assertEquals("https://www.agoraporto.pt/grandes-campos-municipais/campo-municipal-do-outeiro",
                candidate.officialSourceUrl());
        assertEquals("https://mapcarta.com/pt/W1298753441", candidate.secondarySourceUrl());
        assertNull(venue.getLatitude());
        assertNull(venue.getLongitude());
    }

    @Test
    void changesToIdentityOrCoordinatesInvalidateCandidate() {
        var venue = existing();
        venue.setLongitude(-8.59259);
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setLongitude(null);
        venue.setAddress("Outra rua");
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue = existing();
        venue.setName("Outro Campo");
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue = existing();
        venue.setDistrict("Maia");
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue = existing();
        venue.setId(8283L);
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue = existing();
        venue.setInformationSource(Venue.InformationSource.COMMUNITY);
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
    }
}
