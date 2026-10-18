package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.venue.OpenStreetMapVenueImportRequest;
import com.example.desporto24.model.dto.venue.VenueResponse;
import com.example.desporto24.model.dto.venue.VenueCoverageReport;
import com.example.desporto24.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/venues")
public class AdminVenueImportController {

    private final VenueService venueService;

    /** Read-only aggregates; /api/admin/** is protected by SecurityConfiguration. */
    @GetMapping("/coverage")
    public VenueCoverageReport coverage() {
        return venueService.coverage();
    }

    @PostMapping("/imports/openstreetmap")
    public List<VenueResponse> importOpenStreetMapVenues(
            @RequestBody @Valid List<OpenStreetMapVenueImportRequest> requests) {
        return venueService.importOpenStreetMapVenues(requests);
    }
}
