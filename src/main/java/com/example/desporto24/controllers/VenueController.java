package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.venue.CreateVenueRequest;
import com.example.desporto24.model.dto.venue.CreateVenueSportConditionRequest;
import com.example.desporto24.model.dto.venue.VenuePhotoResponse;
import com.example.desporto24.model.dto.venue.VenueResponse;
import com.example.desporto24.model.dto.venue.VenueSearchPageResponse;
import com.example.desporto24.model.dto.venue.VenueSportConditionResponse;
import com.example.desporto24.model.dto.venue.ResolveVenueMapLinkRequest;
import com.example.desporto24.model.dto.venue.ResolveVenueMapLinkResponse;
import com.example.desporto24.service.VenueMapLinkService;
import com.example.desporto24.service.VenuePhotoService;
import com.example.desporto24.service.VenueService;
import com.example.desporto24.service.VenueSportConditionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;
    private final VenueMapLinkService venueMapLinkService;
    private final VenuePhotoService venuePhotoService;
    private final VenueSportConditionService venueSportConditionService;

    @GetMapping
    public List<VenueResponse> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double radiusKm) {
        return venueService.search(city, sport, latitude, longitude, radiusKm);
    }

    /**
     * Bounded search for autocomplete. The legacy GET /api/venues remains unchanged.
     * Filters are optional for the first page, but no request returns an unlimited list.
     */
    @GetMapping("/suggest")
    public VenueSearchPageResponse suggest(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String sport,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String district) {
        return venueService.suggest(name, city, sport, page, district);
    }

    /** Retain the old Java caller signature; HTTP clients use the district-aware endpoint. */
    public VenueSearchPageResponse suggest(String name, String city, String sport, int page) {
        return suggest(name, city, sport, page, null);
    }

    @PostMapping("/resolve-map-link")
    public ResolveVenueMapLinkResponse resolveMapLink(
            @RequestBody @Valid ResolveVenueMapLinkRequest request) {
        return venueMapLinkService.resolve(request.url());
    }

    @GetMapping("/{id}")
    public VenueResponse get(@PathVariable Long id) {
        return venueService.get(id);
    }

    @GetMapping("/{id}/photos")
    public List<VenuePhotoResponse> listPhotos(@PathVariable Long id) {
        return venuePhotoService.listPhotos(id);
    }

    @GetMapping("/{id}/sport-conditions")
    public List<VenueSportConditionResponse> listSportConditions(@PathVariable Long id) {
        return venueSportConditionService.list(id);
    }

    @PostMapping
    public ResponseEntity<VenueResponse> create(
            @RequestBody @Valid CreateVenueRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(venueService.create(request, authentication.getName()));
    }

    @PostMapping(value = "/{id}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VenuePhotoResponse> addPhoto(
            @PathVariable Long id,
            @RequestPart("photo") MultipartFile photo,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(venuePhotoService.addPhoto(id, authentication.getName(), photo));
    }

    @PostMapping("/{id}/sport-conditions")
    public ResponseEntity<VenueSportConditionResponse> addSportCondition(
            @PathVariable Long id,
            @RequestBody @Valid CreateVenueSportConditionRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(venueSportConditionService.create(id, request, authentication.getName()));
    }
}
