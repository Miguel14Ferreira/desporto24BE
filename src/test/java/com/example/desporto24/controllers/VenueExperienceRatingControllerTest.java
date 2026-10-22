package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.venue.VenueExperienceRatingRequest;
import com.example.desporto24.model.dto.venue.VenueExperienceRatingResponse;
import com.example.desporto24.model.dto.venue.VenueRatingSummaryResponse;
import com.example.desporto24.service.VenueExperienceRatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueExperienceRatingControllerTest {
    @Mock VenueExperienceRatingService service;
    @Mock Authentication authentication;
    @InjectMocks VenueExperienceRatingController controller;

    @Test
    void savingAStarRatingUsesTheActualSignedInPlayer() {
        when(authentication.getName()).thenReturn("player-one");
        var request=new VenueExperienceRatingRequest(4);
        var response=new VenueExperienceRatingResponse(10L, 4, Instant.now());
        when(service.rate(7L,"player-one",request)).thenReturn(response);

        assertSame(response, controller.rate(7L,request,authentication));
        verify(service).rate(7L,"player-one",request);
    }

    @Test
    void onlyMyOwnRatingIsReturnedOrNoContentWhenAbsent() {
        when(authentication.getName()).thenReturn("player-one");
        when(service.mine(7L,"player-one")).thenReturn(Optional.empty());
        assertEquals(204,controller.mine(7L,authentication).getStatusCode().value());
        verify(service).mine(7L,"player-one");
    }

    @Test
    void publicEndpointNeverReturnsRawPlayerReviews() {
        var response=new VenueRatingSummaryResponse(10L,3,4.7);
        when(service.summary(10L)).thenReturn(response);
        assertSame(response,controller.summary(10L));
    }
}
