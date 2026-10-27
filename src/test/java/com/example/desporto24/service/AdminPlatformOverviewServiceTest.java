package com.example.desporto24.service;

import com.example.desporto24.controllers.AdminPlatformOverviewController;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.enumeration.TournamentStatus;
import com.example.desporto24.enumeration.UserReportStatus;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTournamentRepository;
import com.example.desporto24.repository.moderation.UserReportRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import com.example.desporto24.model.venue.Venue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminPlatformOverviewServiceTest {
    @Mock ProfileRepository profiles;
    @Mock SessionRepository sessions;
    @Mock CommunityRepository communities;
    @Mock CommunityTournamentRepository tournaments;
    @Mock VenueRepository venues;
    @Mock UserReportRepository reports;
    @Mock OnlinePresenceService onlinePresence;
    @InjectMocks AdminPlatformOverviewService service;

    @Test
    void readsIndependentLiveCountsAndDoesNotFabricateOnlinePresence() {
        when(profiles.countRegisteredProfiles()).thenReturn(110L);
        when(profiles.countEnabledRegisteredProfiles()).thenReturn(107L);
        when(profiles.countRegistrationsSince(any(Date.class))).thenReturn(9L);
        when(profiles.countRecentLoginsSince(any(Date.class))).thenReturn(14L);
        when(sessions.count()).thenReturn(120L);
        when(sessions.countUpcomingSessions(any(Date.class))).thenReturn(24L);
        when(sessions.countUpcomingSessionsBetween(any(Date.class), any(Date.class)))
                .thenReturn(10L);
        when(sessions.countByStatus(SessionStatus.COMPLETED)).thenReturn(70L);
        when(sessions.countByStatus(SessionStatus.CANCELLED)).thenReturn(8L);
        when(communities.count()).thenReturn(15L);
        when(tournaments.count()).thenReturn(18L);
        when(tournaments.countByStatus(TournamentStatus.IN_PROGRESS)).thenReturn(3L);
        when(venues.count()).thenReturn(900L);
        when(reports.countByStatus(UserReportStatus.OPEN)).thenReturn(4L);
        when(onlinePresence.countOnlineAt(any())).thenReturn(6L);

        var snapshot = service.snapshot();

        assertEquals(110L, snapshot.registeredUsers());
        assertEquals(107L, snapshot.enabledUsers());
        assertEquals(9L, snapshot.registrationsLast7Days());
        assertEquals(14L, snapshot.loginsLast15Minutes());
        assertEquals(6L, snapshot.onlineUsers(), "Presence comes from a separate heartbeat");
        assertEquals(120L, snapshot.totalSessions());
        assertEquals(24L, snapshot.upcomingSessions());
        assertEquals(10L, snapshot.sessionsNext7Days());
        assertEquals(70L, snapshot.completedSessions());
        assertEquals(8L, snapshot.cancelledSessions());
        assertEquals(15L, snapshot.communities());
        assertEquals(18L, snapshot.tournaments());
        assertEquals(3L, snapshot.activeTournaments());
        assertEquals(900L, snapshot.venues());
        assertEquals(4L, snapshot.openUserReports());
        assertNotNull(snapshot.generatedAt());
        verify(profiles, never()).findAll();
        verify(sessions, never()).findAll();
        verify(onlinePresence).countOnlineAt(any());
    }


    @Test
    void venueQualityCountsRealMissingFieldsWithoutLoadingEntities() {
        when(venues.qualityByDistrict()).thenReturn(List.of(
                new Object[]{"Lisboa", 100L, 12L, 25L, 38L, 4L},
                new Object[]{null, 8L, 7L, 8L, 8L, 8L},
                new Object[]{"Porto", 50L, 3L, 4L, 10L, 0L}
        ));

        var quality = service.venueQuality();

        assertEquals(158L, quality.venues());
        assertEquals(22L, quality.withoutSport());
        assertEquals(37L, quality.withoutCoordinates());
        assertEquals(56L, quality.withoutAddress());
        assertEquals(12L, quality.withoutAddressAndCoordinates());
        assertEquals(List.of("Lisboa", "Sem distrito", "Porto"),
                quality.districts().stream()
                        .map(AdminPlatformOverviewService.VenueDistrictCoverage::district).toList());
        assertNotNull(quality.generatedAt());
        verify(venues).qualityByDistrict();
        verify(venues, never()).findAll();
        verify(venues, never()).findAllByOrderByNameAsc();
    }

    @Test
    void missingGpsQueuePaginatesWithoutLeakingPersonalDataOrChangingVenues() throws Exception {
        var candidate = Venue.builder().id(27L).name("Campo Municipal").city("Sintra")
                .district("Lisboa").address("Rua do Campo")
                .sourceReference("OSM:WAY:12345")
                .informationSource(Venue.InformationSource.OPENSTREETMAP).build();
        when(venues.missingGpsReview("Lisboa", PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of(candidate), PageRequest.of(0, 20), true));
        var result = service.missingGpsReview("Lisboa", 0);
        assertEquals(27L, result.venues().getFirst().id());
        assertEquals("Rua do Campo", result.venues().getFirst().address());
        assertEquals("OSM:WAY:12345", result.venues().getFirst().sourceReference());
        assertTrue(result.hasMore());
        verify(venues, never()).findAll();
        verify(venues, never()).save(any());
        var fields = java.util.Arrays.stream(AdminPlatformOverviewService.VenueGpsReviewItem.class
                .getRecordComponents()).map(java.lang.reflect.RecordComponent::getName).toList();
        assertFalse(fields.contains("contact"));
        assertFalse(fields.contains("latitude"));
        assertFalse(fields.contains("longitude"));
        assertFalse(fields.contains("submittedByUsername"));
        var method = AdminPlatformOverviewController.class
                .getMethod("missingGpsReview", String.class, int.class, boolean.class);
        assertArrayEquals(new String[]{"/venue-gps-review"},
                method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class).value());
        assertEquals("hasAnyRole('ADMIN', 'SUPER_ADMIN')",
                AdminPlatformOverviewController.class.getAnnotation(PreAuthorize.class).value());
    }

    @Test
    void missingGpsReviewIncludesSafeOfficialWebsiteOnlyAsUnverifiedResearchLead() {
        var good = Venue.builder().id(277L).name("Pavilhão Desportivo Municipal")
                .city("Santarém").informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://viversantarem.pt/pavilhoes-desportivos/").build();
        var insecure = Venue.builder().id(287L).name("Pista")
                .city("Braga").informationSource(Venue.InformationSource.OFFICIAL)
                .website("http://cm-braga.pt/").build();
        var userInfo = Venue.builder().id(288L).name("Falso")
                .city("Braga").informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://trusted.pt@attacker.test/").build();
        var community = Venue.builder().id(289L).name("Comunitário")
                .city("Braga").informationSource(Venue.InformationSource.COMMUNITY)
                .website("https://example.org/").build();
        when(venues.missingGpsReview("", PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of(good, insecure, userInfo, community),
                        PageRequest.of(0, 20), false));
        var rows = service.missingGpsReview(null, 0).venues();
        assertEquals("https://viversantarem.pt/pavilhoes-desportivos/", rows.get(0).sourceWebsite());
        assertNull(rows.get(1).sourceWebsite());
        assertNull(rows.get(2).sourceWebsite());
        assertNull(rows.get(3).sourceWebsite());
        assertNull(AdminPlatformOverviewService.safeOfficialSourceWebsite("javascript:alert(1)"));
        assertNull(AdminPlatformOverviewService.safeOfficialSourceWebsite("https://localhost/"));
        assertNull(AdminPlatformOverviewService.safeOfficialSourceWebsite("https://127.0.0.1/"));
        assertNull(AdminPlatformOverviewService.safeOfficialSourceWebsite("https://source.pt/#fragment"));
        assertNull(AdminPlatformOverviewService.safeOfficialSourceWebsite("https://source.pt/\\nnext"));
        verify(venues, never()).save(any());
    }

    @Test
    void onlyExactExistingEvoraPoolsShowUnapprovedOfficialSiteResearchCandidate() {
        var official = Venue.builder().id(273L).name("Piscinas Municipais de Évora")
                .city("Évora").district("Évora")
                .address("Avenida Engenheiro Arantes de Oliveira, 7000-758 Évora")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://www.cm-evora.pt/visitante/lazer-visitar/piscinas-municipais/")
                .build();
        var sameNameWrongId = Venue.builder().id(9999L).name("Piscinas Municipais de Évora")
                .city("Évora").district("Évora").address(official.getAddress())
                .informationSource(Venue.InformationSource.OFFICIAL).website(official.getWebsite())
                .build();
        var unrelatedMunicipal = Venue.builder().id(272L).name("Complexo Desportivo de Évora")
                .city("Évora").district("Évora")
                .informationSource(Venue.InformationSource.OFFICIAL).build();
        when(venues.missingGpsReview("Évora", PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of(official, sameNameWrongId, unrelatedMunicipal)));
        var rows = service.missingGpsReview("Évora", 0).venues();
        var lead = rows.getFirst().researchCandidate();
        assertNotNull(lead);
        assertEquals(273, rows.getFirst().id());
        assertEquals(38.573234, lead.latitude(), 0.0000001);
        assertEquals(-7.928876, lead.longitude(), 0.0000001);
        assertEquals("SITE_REFERENCE_NOT_VERIFIED_ENTRANCE", lead.precision());
        assertTrue(lead.officialSourceUrl().startsWith("https://www.cm-evora.pt/"));
        assertTrue(lead.secondarySourceUrl().startsWith("https://www.outdooractive.pt/"));
        assertNull(rows.get(1).researchCandidate());
        assertNull(rows.get(2).researchCandidate());
        verify(venues, never()).save(any());
    }

    @Test
    void candidateNeverAppearsIfExistingGpsIdentityOrSourceChanges() {
        var good = Venue.builder().id(273L).name("Piscinas Municipais de Évora")
                .city("Évora").district("Évora").address("Av. Engenheiro Arantes de Oliveira")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://www.cm-evora.pt/visitante/lazer-visitar/piscinas-municipais/")
                .build();
        assertNotNull(AdminPlatformOverviewService.researchCandidate(good));
        good.setLatitude(38.573234);
        assertNull(AdminPlatformOverviewService.researchCandidate(good));
        good.setLatitude(null);
        good.setLongitude(-7.928876);
        assertNull(AdminPlatformOverviewService.researchCandidate(good));
        good.setLongitude(null);
        good.setName("Piscinas Municipais de outra localidade");
        assertNull(AdminPlatformOverviewService.researchCandidate(good));
        good.setName("Piscinas Municipais de Évora");
        good.setWebsite("https://unknown.example/");
        assertNull(AdminPlatformOverviewService.researchCandidate(good));
        good.setWebsite("https://www.cm-evora.pt/visitante/lazer-visitar/piscinas-municipais/");
        good.setInformationSource(Venue.InformationSource.COMMUNITY);
        assertNull(AdminPlatformOverviewService.researchCandidate(good));
    }

    @Test
    void santaremSacapeitoShowsSiteLeadOnlyOnExactExistingUnlocatedVenue() {
        var official = Venue.builder().id(276L).name("Piscinas Municipais do Sacapeito")
                .city("Santarém").district("Santarém")
                .address("Praceta Cidade de Badajoz, Urbanização do Sacapeito, 2005-213 Santarém")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://viversantarem.pt/instalacoes-desportivas/")
                .build();
        var duplicate = Venue.builder().id(9276L).name(official.getName())
                .city(official.getCity()).district(official.getDistrict())
                .address(official.getAddress())
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website(official.getWebsite()).build();
        when(venues.missingGpsReview("Santarém", PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of(official, duplicate)));
        var rows = service.missingGpsReview("Santarém", 0).venues();
        var candidate = rows.getFirst().researchCandidate();
        assertNotNull(candidate);
        assertEquals(39.2250178, candidate.latitude(), 0.00000001);
        assertEquals(-8.6886809, candidate.longitude(), 0.00000001);
        assertEquals("SITE_REFERENCE_NOT_VERIFIED_ENTRANCE", candidate.precision());
        assertEquals("https://www.visitesantarem.pt/lista/piscinas-municipais-do-sacapeito/",
                candidate.officialSourceUrl());
        assertEquals("https://www.openstreetmap.org/way/1172748137",
                candidate.secondarySourceUrl());
        assertNull(rows.get(1).researchCandidate());
        verify(venues, never()).save(any());
    }

    @Test
    void santaremSiteCandidateDisappearsWhenCoordinatesOrIdentityChanged() {
        var venue = Venue.builder().id(276L).name("Piscinas Municipais do Sacapeito")
                .city("Santarém").district("Santarém")
                .address("Praceta Cidade de Badajoz, Urbanização do Sacapeito, 2005-213 Santarém")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://viversantarem.pt/instalacoes-desportivas/")
                .build();
        assertNotNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setLatitude(39.2250178);
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setLatitude(null);
        venue.setLongitude(-8.6886809);
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setLongitude(null);
        venue.setAddress("Rua diferente, Santarém");
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setAddress("Praceta Cidade de Badajoz, Santarém");
        venue.setWebsite("http://viversantarem.pt/instalacoes-desportivas/");
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setWebsite("https://viversantarem.pt/instalacoes-desportivas/");
        venue.setDistrict("Lisboa");
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setDistrict("Santarém");
        venue.setInformationSource(Venue.InformationSource.COMMUNITY);
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        assertNull(AdminPlatformOverviewService.researchCandidate(null));
    }

    @Test
    void arroiosAddressDiscrepancyIsReadOnlyForExactSavedIdAndIdentity() {
        var original = Venue.builder().id(284L).name("Polidesportivo de Arroios — Joaquim Campos")
                .city("Lisboa").district("Lisboa")
                .address("Rua do Saco 1A, Mercado do Forno Tijolo, Lisboa")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/")
                .build();
        var duplicate = Venue.builder().id(9284L).name(original.getName())
                .city("Lisboa").district("Lisboa").address(original.getAddress())
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website(original.getWebsite()).build();
        when(venues.missingGpsReview("Lisboa", PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of(original, duplicate)));
        var rows = service.missingGpsReview("Lisboa", 0).venues();
        assertNotNull(rows.getFirst().addressReview());
        var lead = rows.getFirst().addressReview();
        assertEquals("ADMIN_OFFICE_ADDRESS_VS_VENUE_LOCATION_REVIEW", lead.issueCode());
        assertTrue(lead.venueLocationToCheck().contains("Rua Maria da Fonte"));
        assertTrue(lead.venueLocationToCheck().contains("entrada por confirmar"));
        assertEquals("https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/",
                lead.equipmentSourceUrl());
        assertEquals("https://jfarroios.pt/equipamentos/mercados/mercado-do-forno-do-tijolo/",
                lead.marketSourceUrl());
        assertNull(rows.getFirst().researchCandidate());
        assertNull(rows.get(1).addressReview());
        verify(venues, never()).save(any());
    }

    @Test
    void arroiosWarningDisappearsAfterAnyRelevantCorrectionOrIdentityChange() {
        var venue = Venue.builder().id(284L).name("Polidesportivo de Arroios — Joaquim Campos")
                .city("Lisboa").district("Lisboa")
                .address("Rua do Saco 1A, Mercado do Forno Tijolo, Lisboa")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/")
                .build();
        assertNotNull(AdminPlatformOverviewService.addressReview(venue));
        venue.setAddress("Mercado do Forno do Tijolo, Rua Maria da Fonte, Lisboa");
        assertNull(AdminPlatformOverviewService.addressReview(venue));
        venue.setAddress("Rua do Saco 1A, Mercado do Forno Tijolo, Lisboa");
        venue.setLatitude(38.721);
        assertNull(AdminPlatformOverviewService.addressReview(venue));
        venue.setLatitude(null);
        venue.setLongitude(-9.132);
        assertNull(AdminPlatformOverviewService.addressReview(venue));
        venue.setLongitude(null);
        venue.setDistrict("Porto");
        assertNull(AdminPlatformOverviewService.addressReview(venue));
        venue.setDistrict("Lisboa");
        venue.setWebsite("https://example.org/evil");
        assertNull(AdminPlatformOverviewService.addressReview(venue));
        venue.setWebsite("https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/");
        venue.setInformationSource(Venue.InformationSource.COMMUNITY);
        assertNull(AdminPlatformOverviewService.addressReview(venue));
        assertNull(AdminPlatformOverviewService.addressReview(null));
    }

    @Test
    void choupalSiteHintUsesOnlyExactChouposId281AndOfficialSavedAddress() {
        var venue = Venue.builder().id(281L).name("Campo Municipal dos Choupos")
                .city("Porto").district("Porto").address("Rua dos Plátanos, 4100-414 Porto")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://www.agoraporto.pt/noticias/desporto/desporto/rmid").build();
        var lead = AdminPlatformOverviewService.researchCandidate(venue);
        assertNotNull(lead);
        assertEquals(41.17345, lead.latitude(), 0.0000001);
        assertEquals(-8.65816, lead.longitude(), 0.0000001);
        assertEquals("SITE_REFERENCE_NOT_VERIFIED_ENTRANCE", lead.precision());
        assertEquals("https://www.agoraporto.pt/grandes-campos-municipais/polidesportivo-dos-choupos",
                lead.officialSourceUrl());
        assertEquals("https://www.openstreetmap.org/way/449975768", lead.secondarySourceUrl());
        assertNull(AdminPlatformOverviewService.addressReview(venue));
        assertFalse(AdminPlatformOverviewService.visoDuplicateReviewEligible(venue));

        venue.setLatitude(41.17345);
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setLatitude(null);
        venue.setLongitude(-8.65816);
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setLongitude(null);
        venue.setAddress("Rua desconhecida");
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setAddress("Rua dos Plátanos, 4100-414 Porto");
        venue.setId(8281L);
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setId(281L);
        venue.setWebsite("https://fake.example/");
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
        venue.setWebsite("https://www.agoraporto.pt/noticias/desporto/desporto/rmid");
        venue.setInformationSource(Venue.InformationSource.COMMUNITY);
        assertNull(AdminPlatformOverviewService.researchCandidate(venue));
    }

    @Test
    void visoPotentialDuplicateRequiresBothLiveRecordsToMatchAndNeverCopiesGps() {
        var official = Venue.builder().id(280L).name("Campo Municipal do Viso")
                .city("Porto").district("Porto").address("Rua Direita do Viso, 4250 Porto")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://www.agoraporto.pt/noticias/desporto/desporto/rmid").build();
        var imported = Venue.builder().id(2623L).name("Campo Sintético do Viso")
                .city("Porto").district("Porto").informationSource(Venue.InformationSource.OPENSTREETMAP)
                .latitude(41.1819461).longitude(-8.6450944).build();
        when(venues.findById(2623L)).thenReturn(java.util.Optional.of(imported));
        when(venues.missingGpsReview("Porto", PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of(official)));
        var row = service.missingGpsReview("Porto", 0).venues().getFirst();
        assertNull(row.researchCandidate());
        assertNull(row.addressReview());
        assertNotNull(row.possibleDuplicateReview());
        var potential = row.possibleDuplicateReview();
        assertEquals("POTENTIAL_SAME_FACILITY_REVIEW", potential.issueCode());
        assertEquals(2623L, potential.relatedVenueId());
        assertEquals("Campo Sintético do Viso", potential.relatedVenueName());
        assertTrue(potential.officialSourceUrl().startsWith("https://www.agoraporto.pt/"));
        assertEquals("https://www.openstreetmap.org/way/334849388", potential.mapSourceUrl());
        assertNull(official.getLatitude());
        assertNull(official.getLongitude());
        verify(venues, never()).save(any());
    }

    @Test
    void visoDuplicateReviewCannotBeMisappliedToOtherOrChangedRecords() {
        var original = Venue.builder().id(280L).name("Campo Municipal do Viso")
                .city("Porto").district("Porto").address("Rua Direita do Viso, 4250 Porto")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://www.agoraporto.pt/noticias/desporto/desporto/rmid").build();
        assertTrue(AdminPlatformOverviewService.visoDuplicateReviewEligible(original));
        original.setId(999L);
        assertFalse(AdminPlatformOverviewService.visoDuplicateReviewEligible(original));
        original.setId(280L);
        original.setLatitude(41.18);
        assertFalse(AdminPlatformOverviewService.visoDuplicateReviewEligible(original));
        original.setLatitude(null);
        original.setLongitude(-8.645);
        assertFalse(AdminPlatformOverviewService.visoDuplicateReviewEligible(original));
        original.setLongitude(null);
        original.setAddress("Rua errada");
        assertFalse(AdminPlatformOverviewService.visoDuplicateReviewEligible(original));
        original.setAddress("Rua Direita do Viso, 4250 Porto");
        original.setWebsite("https://other.example/");
        assertFalse(AdminPlatformOverviewService.visoDuplicateReviewEligible(original));
        original.setWebsite("https://www.agoraporto.pt/noticias/desporto/desporto/rmid");
        original.setInformationSource(Venue.InformationSource.COMMUNITY);
        assertFalse(AdminPlatformOverviewService.visoDuplicateReviewEligible(original));
        assertFalse(AdminPlatformOverviewService.visoDuplicateReviewEligible(null));

        // Correct official record but changed external OSM identity -> no warning.
        original.setInformationSource(Venue.InformationSource.OFFICIAL);
        var changed = Venue.builder().id(2623L).name("Campo de outro lugar")
                .city("Porto").district("Porto").informationSource(Venue.InformationSource.OPENSTREETMAP)
                .latitude(41.1819461).longitude(-8.6450944).build();
        when(venues.findById(2623L)).thenReturn(java.util.Optional.of(changed));
        when(venues.missingGpsReview("Porto", PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of(original)));
        assertNull(service.missingGpsReview("Porto", 0).venues().getFirst().possibleDuplicateReview());
        verify(venues, never()).save(any());
    }

    @Test
    void evidenceOnlyReviewIsBoundedToCurrentVerifiedLeadsAndHasStableOrder() {
        var evora = Venue.builder().id(273L).name("Piscinas Municipais de Évora")
                .city("Évora").district("Évora")
                .address("Avenida Engenheiro Arantes de Oliveira, 7000-758 Évora")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://www.cm-evora.pt/visitante/lazer-visitar/piscinas-municipais/").build();
        var arroios = Venue.builder().id(284L).name("Polidesportivo de Arroios — Joaquim Campos")
                .city("Lisboa").district("Lisboa")
                .address("Rua do Saco 1A, Mercado do Forno Tijolo, Lisboa")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://jfarroios.pt/equipamentos/polidesportivo-de-arroios/").build();
        var stale = Venue.builder().id(281L).name("Campo Municipal dos Choupos")
                .city("Porto").district("Porto").latitude(41.17345).longitude(-8.65816)
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://www.agoraporto.pt/noticias/desporto/desporto/rmid").build();
        var unrelated = Venue.builder().id(9999L).name("Qualquer espaço")
                .city("Porto").district("Porto").build();
        when(venues.findAllById(any())).thenReturn(List.of(arroios, stale, unrelated, evora));

        var first = service.missingGpsReview("", 0, true);
        assertEquals(2, first.venues().size());
        assertEquals(List.of(273L, 284L), first.venues().stream()
                .map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        assertNotNull(first.venues().getFirst().researchCandidate());
        assertNotNull(first.venues().getLast().addressReview());
        assertFalse(first.hasMore());

        var lisboa = service.missingGpsReview("Lisboa", 0, true);
        assertEquals(List.of(284L), lisboa.venues().stream()
                .map(AdminPlatformOverviewService.VenueGpsReviewItem::id).toList());
        assertEquals(0, lisboa.page());

        var emptyPage = service.missingGpsReview("", 1, true);
        assertTrue(emptyPage.venues().isEmpty());
        assertFalse(emptyPage.hasMore());
        verify(venues, never()).missingGpsReview(any(), any());
        verify(venues, never()).save(any());
    }

    @Test
    void evidenceOnlyVisoRequiresFreshMatchingOsmRecord() {
        var viso = Venue.builder().id(280L).name("Campo Municipal do Viso")
                .city("Porto").district("Porto")
                .address("Rua Direita do Viso, 4250 Porto")
                .informationSource(Venue.InformationSource.OFFICIAL)
                .website("https://www.agoraporto.pt/noticias/desporto/desporto/rmid").build();
        var osm = Venue.builder().id(2623L).name("Campo Sintético do Viso")
                .city("Porto").district("Porto").informationSource(Venue.InformationSource.OPENSTREETMAP)
                .latitude(41.1819461).longitude(-8.6450944).build();
        when(venues.findAllById(any())).thenReturn(List.of(viso));
        when(venues.findById(2623L)).thenReturn(java.util.Optional.of(osm),
                java.util.Optional.empty());
        var first = service.missingGpsReview("Porto", 0, true);
        assertEquals(1, first.venues().size());
        assertNotNull(first.venues().getFirst().possibleDuplicateReview());
        assertNull(first.venues().getFirst().researchCandidate());
        var second = service.missingGpsReview("Porto", 0, true);
        assertTrue(second.venues().isEmpty());
        assertNull(viso.getLatitude());
        verify(venues, never()).save(any());
    }

    @Test
    void focusModePreservesNormalQueueAndRejectsUnsafeRequestsBeforeDatabaseReads() {
        when(venues.missingGpsReview("", PageRequest.of(0, 20)))
                .thenReturn(new SliceImpl<>(List.of()));
        assertTrue(service.missingGpsReview(null, 0, false).venues().isEmpty());
        verify(venues).missingGpsReview("", PageRequest.of(0, 20));
        verify(venues, never()).findAllById(any());
        assertThrows(IllegalArgumentException.class, () -> service.missingGpsReview("Lisboa", -1, true));
        assertThrows(IllegalArgumentException.class, () -> service.missingGpsReview("Sem distrito", 0, true));
        assertThrows(IllegalArgumentException.class, () -> service.missingGpsReview("Lisboa", 1001, true));
        verifyNoMoreInteractions(venues);
    }

    @Test
    void missingGpsQueueRejectsUnsafePageBeforeDatabaseAccess() {
        assertThrows(IllegalArgumentException.class, () -> service.missingGpsReview("Lisboa", -1));
        assertThrows(IllegalArgumentException.class, () -> service.missingGpsReview("Lisboa", 1001));
        assertThrows(IllegalArgumentException.class, () -> service.missingGpsReview("Sem distrito", 0));
        verify(venues, never()).missingGpsReview(any(), any());
    }

    @Test
    void qualityEndpointInheritsAdminOnlyPolicyAndHasNoVenueIdentifiers() throws Exception {
        var mapping = AdminPlatformOverviewController.class
                .getMethod("venueQuality")
                .getAnnotation(org.springframework.web.bind.annotation.GetMapping.class);
        assertArrayEquals(new String[]{"/venue-quality"}, mapping.value());
        assertEquals("hasAnyRole('ADMIN', 'SUPER_ADMIN')",
                AdminPlatformOverviewController.class.getAnnotation(PreAuthorize.class).value());
        var fields = java.util.Arrays.stream(
                AdminPlatformOverviewService.VenueDistrictCoverage.class.getRecordComponents())
                .map(java.lang.reflect.RecordComponent::getName).toList();
        assertFalse(fields.contains("id"));
        assertFalse(fields.contains("address"));
        assertFalse(fields.contains("latitude"));
        assertFalse(fields.contains("longitude"));
    }

    @Test
    void overviewIsAdminOnlyAndReturnsNoPersonalFields() {
        var policy = AdminPlatformOverviewController.class.getAnnotation(PreAuthorize.class);
        assertNotNull(policy);
        assertEquals("hasAnyRole('ADMIN', 'SUPER_ADMIN')", policy.value());
        var names = java.util.Arrays.stream(
                AdminPlatformOverviewService.PlatformOverview.class.getRecordComponents())
                .map(java.lang.reflect.RecordComponent::getName).toList();
        assertFalse(names.contains("username"));
        assertFalse(names.contains("email"));
        assertFalse(names.contains("profileId"));
    }
}
