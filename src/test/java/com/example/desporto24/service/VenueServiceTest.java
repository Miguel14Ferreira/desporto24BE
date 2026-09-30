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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

    @Mock private VenueRepository venueRepository;
    @Mock private ProfileRepository profileRepository;

    @InjectMocks
    private VenueService service;

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
                        "Bar, aluguer de material",
                        "Metro e autocarros próximos",
                        "Seg-Sex 08:00-22:00; Sáb-Dom 09:00-20:00",
                        "25€/hora",
                        "https://pavilhao.example",
                        "+351 210 000 000",
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
        assertTrue(response.lighting());
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

        when(venueRepository.findAllByOrderByNameAsc())
                .thenReturn(List.of(matching, unspecified));

        var results = service.search("lis", "PADEL");

        assertEquals(1, results.size());
        assertEquals("Campo Padel", results.getFirst().name());
    }
}
