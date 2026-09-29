package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.venue.CreateVenueRequest;
import com.example.desporto24.model.dto.venue.VenueResponse;
import com.example.desporto24.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;

    @GetMapping
    public List<VenueResponse> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String sport) {
        return venueService.search(city, sport);
    }

    @GetMapping("/{id}")
    public VenueResponse get(@PathVariable Long id) {
        return venueService.get(id);
    }

    @PostMapping
    public ResponseEntity<VenueResponse> create(
            @RequestBody @Valid CreateVenueRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(venueService.create(request, authentication.getName()));
    }
}
