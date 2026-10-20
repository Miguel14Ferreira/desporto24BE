package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.venue.CreateVenueRequest;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

    @Mock private VenueRepository venueRepository;
    @Mock private ProfileRepository profileRepository;
    @Spy private SessionDistanceCalculator distanceCalculator = new SessionDistanceCalculator();

    @InjectMocks
    private VenueService service;

    @Test
    void exposesPersistedDistrictAlongsideCityInVenueResponses() {
        var venue = Venue.builder()
                .id(321L)
                .name("Pavilhão do Porto")
                .city("Matosinhos")
                .district("Porto")
                .address("Morada de teste")
                .build();

        var response = service.toResponse(venue);

        assertEquals("Matosinhos", response.city());
        assertEquals("Porto", response.district());
    }

    @Test
    void createsReusableVenueWithAmenities() {
        Profile creator = new Profile();
        creator.setId(1L);
        creator.setUsername("TMF14");

        when(profileRepository.findUserByUsernameIgnoreCase("TMF14"))
                .thenReturn(Optional.of(creator));
        when(venueRepository.save(any(Venue.class)))
                .thenAnswer(invocation -> {
                    Venue venue = invocation.getArgument(0);
                    venue.setId(9L);
                    return venue;
                });

        var response = service.create(
                new CreateVenueRequest(
                        "Pavilhão Central",
                        "Lisboa",
                        "Rua do Desporto 10",
                        "Futsal",
                        "Pavilhão",
                        Venue.OwnershipType.PUBLIC,
                        "Madeira",
                        Venue.EnvironmentType.BOTH,
                        true,
                        true,
                        "MALE;FEMALE;ADAPTED",
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        500,
                        true,
                        true,
                        "Bar, aluguer de material",
                        "Metro e autocarros próximos",
                        "Seg-Sex 08:00-22:00; Sáb-Dom 09:00-20:00",
                        "25€/hora",
                        "https://pavilhao.example",
                        "+351 210 000 000",
                        true,
                        true,
                        true,
                        38.72,
                        -9.14
                ),
                "TMF14"
        );

        assertEquals(9L, response.id());
        assertEquals("Pavilhão Central", response.name());
        assertEquals("Pavilhão", response.venueType());
        assertEquals("PUBLIC", response.ownershipType());
        assertEquals("Madeira", response.surfaceType());
        assertEquals("BOTH", response.environmentType());
        assertEquals("MALE;FEMALE;ADAPTED", response.changingRoomTypes());
        assertEquals(Boolean.TRUE, response.toilets());
        assertEquals(Boolean.TRUE, response.lockers());
        assertEquals(Boolean.TRUE, response.showers());
        assertEquals(Boolean.TRUE, response.stands());
        assertEquals(Boolean.TRUE, response.drinkingFountain());
        assertEquals(Boolean.TRUE, response.accessible());
        assertEquals(Boolean.TRUE, response.firstAid());
        assertEquals(Boolean.TRUE, response.aed());
        assertEquals(Boolean.TRUE, response.medicalStation());
        assertEquals(Boolean.TRUE, response.lifeguard());
        assertEquals(Boolean.TRUE, response.staffOnSite());
        assertEquals(Boolean.TRUE, response.securitySurveillance());
        assertEquals(Boolean.TRUE, response.cafe());
        assertEquals(Boolean.TRUE, response.restaurant());
        assertEquals(Boolean.TRUE, response.vendingMachines());
        assertEquals(Boolean.TRUE, response.sportsShop());
        assertEquals(Boolean.TRUE, response.wifi());
        assertEquals(Boolean.TRUE, response.phoneCharging());
        assertEquals(500, response.nearbyRadiusMeters());
        assertEquals(Boolean.TRUE, response.nearbyPharmacy());
        assertEquals(Boolean.TRUE, response.nearbyAtm());
        assertEquals("Bar, aluguer de material", response.services());
        assertEquals("Metro e autocarros próximos", response.transport());
        assertEquals("Seg-Sex 08:00-22:00; Sáb-Dom 09:00-20:00", response.openingHours());
        assertEquals("25€/hora", response.priceInfo());
        assertEquals("https://pavilhao.example", response.website());
        assertEquals("+351 210 000 000", response.contact());
        assertEquals("COMMUNITY", response.informationSource());
        assertTrue(response.indoor());
        assertTrue(response.changingRooms());
        assertTrue(response.parking());
        assertEquals(Boolean.TRUE, response.freeParking());
        assertTrue(response.lighting());
    }

    @Test
    void findsNearbyVenuesAcrossCityBoundariesWithoutLoadingTheWholeCatalog() {
        Venue close = Venue.builder().id(11L).name("Campo Perto").city("Oeiras")
                .address("Rua A").sports("Futsal;Padel").latitude(38.721).longitude(-9.14).build();
        Venue closest = Venue.builder().id(12L).name("Campo Mais Perto").city("Lisboa")
                .address("Rua B").sports("Padel").latitude(38.7201).longitude(-9.14).build();
        Venue wrongSport = Venue.builder().id(13L).name("Campo Sem Padel").city("Lisboa")
                .address("Rua C").sports("Ténis").latitude(38.72).longitude(-9.14).build();
        Venue outsideCircle = Venue.builder().id(14L).name("Campo Longe").city("Loures")
                .address("Rua D").sports("Padel").latitude(38.80).longitude(-9.06).build();
        Venue missingLocation = Venue.builder().id(17L).name("Campo Sem GPS")
                .city("Lisboa").address("Rua E").sports("Padel").build();
        when(venueRepository.findByLatitudeBetweenAndLongitudeBetweenOrderByNameAsc(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(List.of(close, outsideCircle, wrongSport, closest, missingLocation));

        var result = service.search("Porto", "padel", 38.72, -9.14, 10.0);

        assertEquals(List.of(12L, 11L), result.stream().map(r -> r.id()).toList());
        verify(venueRepository, never()).findAllByOrderByNameAsc();
        verify(venueRepository, never()).findByCityContainingIgnoreCaseOrderByNameAsc(any());
    }

    @Test
    void rejectsIncompleteOrInvalidNearbySearchParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> service.search(null, "Padel", 38.72, null, 25.0));
        assertThrows(IllegalArgumentException.class,
                () -> service.search(null, "Padel", null, -9.14, 25.0));
        assertThrows(IllegalArgumentException.class,
                () -> service.search(null, "Padel", null, null, 25.0));
        assertThrows(IllegalArgumentException.class,
                () -> service.search(null, "Padel", 91.0, -9.14, 25.0));
        assertThrows(IllegalArgumentException.class,
                () -> service.search(null, "Padel", 38.72, -9.14, -1.0));
        assertThrows(IllegalArgumentException.class,
                () -> service.search(null, "Padel", 38.72, -9.14, Double.NaN));
    }

    @Test
    void searchesAcrossTheDateLineWithoutMissingNearbyVenues() {
        Venue nearby = Venue.builder().id(15L).name("Campo Internacional")
                .city("Ilha").address("Rua A").sports("Padel")
                .latitude(0.0).longitude(-179.9).build();
        when(venueRepository.findByLatitudeBetweenAndLongitudeBetweenOrderByNameAsc(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                // The venue belongs to the second longitude range, across +180/-180.
                .thenReturn(List.of(), List.of(nearby));

        var results = service.search(null, "Padel", 0.0, 179.9, 50.0);

        assertEquals(1, results.size());
        assertEquals(15L, results.getFirst().id());
        org.mockito.Mockito.verify(venueRepository, org.mockito.Mockito.times(2))
                .findByLatitudeBetweenAndLongitudeBetweenOrderByNameAsc(
                        anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void usesTheDefaultRadiusWhenOnlyCoordinatesAreSpecified() {
        Venue nearby = Venue.builder().id(16L).name("Campo")
                .city("Lisboa").address("Rua A").sports("Padel")
                .latitude(38.72).longitude(-9.14).build();
        when(venueRepository.findByLatitudeBetweenAndLongitudeBetweenOrderByNameAsc(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(List.of(nearby));

        assertEquals(16L, service.search(null, "Padel", 38.72, -9.14, null).getFirst().id());
    }
    @Test
    void matchesSportExactlyIgnoringCaseAndWhitespace() {
        Venue matching = Venue.builder()
                .name("Campo Padel")
                .city("Lisboa")
                .address("Rua A")
                .sports("  Padel  ")
                .build();
        Venue partial = Venue.builder()
                .name("Campo Padelball")
                .city("Lisboa")
                .address("Rua B")
                .sports("Padelball")
                .build();

        when(venueRepository.findAllByOrderByNameAsc())
                .thenReturn(List.of(matching, partial));

        var results = service.search(null, " PADEL ");

        assertEquals(1, results.size());
        assertEquals("Campo Padel", results.getFirst().name());
    }

    @Test
    void matchesAnySportFromMultiSportVenue() {
        Venue venue = Venue.builder()
                .name("Complexo Multi")
                .city("Lisboa")
                .address("Rua A")
                .sports("Futsal; Padel; Ténis")
                .build();

        when(venueRepository.findAllByOrderByNameAsc())
                .thenReturn(List.of(venue));

        var results = service.search(null, "padel");

        assertEquals(1, results.size());
        assertEquals("Complexo Multi", results.getFirst().name());
    }

    @Test
    void excludesVenuesWithoutSportsWhenFilteringBySport() {
        Venue matching = Venue.builder()
                .name("Campo Padel")
                .city("Lisboa")
                .address("Rua A")
                .sports("Padel")
                .build();
        Venue unspecified = Venue.builder()
                .name("Recinto sem modalidade")
                .city("Lisboa")
                .address("Rua B")
                .sports(null)
                .build();

        when(venueRepository.findByCityContainingIgnoreCaseOrderByNameAsc("lis"))
                .thenReturn(List.of(matching, unspecified));

        var results = service.search("lis", "PADEL");

        assertEquals(1, results.size());
        assertEquals("Campo Padel", results.getFirst().name());
    }

    @Test
    void searchesImportedNationalVenuesByCityAndSport() {
        Venue porto = Venue.builder()
                .id(261L)
                .name("Piscina Municipal da Constituição")
                .city("Porto")
                .address("Rua do Almirante Leote do Rêgo")
                .sports("Natação")
                .informationSource(Venue.InformationSource.OPENSTREETMAP)
                .build();
        Venue coimbra = Venue.builder()
                .id(262L)
                .name("Pavilhão Municipal Multidesportos Mário Mexia")
                .city("Coimbra")
                .address("Praça Heróis do Ultramar")
                .sports("Andebol;Basquetebol;Voleibol;Futsal;Patinagem;Ginástica")
                .informationSource(Venue.InformationSource.OPENSTREETMAP)
                .build();

        when(venueRepository.findByCityContainingIgnoreCaseOrderByNameAsc("porto"))
                .thenReturn(List.of(porto));
        when(venueRepository.findByCityContainingIgnoreCaseOrderByNameAsc("coimbra"))
                .thenReturn(List.of(coimbra));

        var swimming = service.search(" Porto ", "natação");
        var basketball = service.search("Coimbra", "Basquetebol");
        var swimmingInCoimbra = service.search("Coimbra", "Natação");

        assertEquals(1, swimming.size());
        assertEquals(261L, swimming.getFirst().id());
        assertEquals("OPENSTREETMAP", swimming.getFirst().informationSource());
        assertEquals(1, basketball.size());
        assertEquals(262L, basketball.getFirst().id());
        assertTrue(swimmingInCoimbra.isEmpty());
    }
}
