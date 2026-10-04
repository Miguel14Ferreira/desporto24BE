package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.venue.CreateVenueSportConditionRequest;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.model.venue.VenueSportCondition;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.venue.VenueRepository;
import com.example.desporto24.repository.venue.VenueSportConditionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VenueSportConditionServiceTest {

    @Mock private VenueRepository venueRepository;
    @Mock private VenueSportConditionRepository venueSportConditionRepository;
    @Mock private ProfileRepository profileRepository;

    @InjectMocks
    private VenueSportConditionService service;

    @Test
    void createsCommunitySportCondition() {
        Venue venue = Venue.builder().id(9L).name("Pavilhão Central").build();

        Profile creator = new Profile();
        creator.setId(1L);
        creator.setUsername("TMF14");

        when(venueRepository.findById(9L)).thenReturn(Optional.of(venue));
        when(profileRepository.findUserByUsernameIgnoreCase("TMF14"))
                .thenReturn(Optional.of(creator));
        when(venueSportConditionRepository.findByVenueIdAndSportIgnoreCase(9L, "Futsal"))
                .thenReturn(Optional.empty());
        when(venueSportConditionRepository.save(any(VenueSportCondition.class)))
                .thenAnswer(invocation -> {
                    VenueSportCondition condition = invocation.getArgument(0);
                    condition.setId(12L);
                    return condition;
                });

        var response = service.create(
                9L,
                new CreateVenueSportConditionRequest(
                        " Futsal ",
                        " Balizas fixas e piso adequado a futsal "
                ),
                "TMF14"
        );

        assertEquals(12L, response.id());
        assertEquals("Futsal", response.sport());
        assertEquals("Balizas fixas e piso adequado a futsal", response.conditions());
        assertEquals("COMMUNITY", response.informationSource());
        assertEquals("TMF14", response.createdByUsername());
    }
}
