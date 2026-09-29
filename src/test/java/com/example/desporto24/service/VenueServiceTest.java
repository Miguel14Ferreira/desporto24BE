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
                        "Madeira",
                        true,
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
        assertEquals("Madeira", response.surfaceType());
        assertEquals("COMMUNITY", response.informationSource());
        assertTrue(response.indoor());
        assertTrue(response.changingRooms());
        assertTrue(response.parking());
        assertTrue(response.lighting());
    }
}
