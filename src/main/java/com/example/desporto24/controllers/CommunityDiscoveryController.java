package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityDiscoveryResponse;
import com.example.desporto24.repository.community.CommunityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/communities")
@PreAuthorize("isAuthenticated()")
public class CommunityDiscoveryController {
    private final CommunityRepository repository;

    public CommunityDiscoveryController(CommunityRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}/presentation")
    public CommunityDiscoveryResponse presentation(@PathVariable Long id) {
        var community = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Community not found"));
        return new CommunityDiscoveryResponse(
                community.getId(),
                community.getName(),
                community.getDescription(),
                community.getSport(),
                community.getPhotoUrl(),
                community.getVisibility()
        );
    }

    @GetMapping("/search")
    public SearchResponse search(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String sport,
            @RequestParam(defaultValue = "0") int page) {
        if (page < 0 || name.length() > 100 || sport.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid search filters");
        }
        String nameTerm = name.trim().toLowerCase(Locale.ROOT);
        String sportTerm = sport.trim().toLowerCase(Locale.ROOT);
        // An unfiltered browse only discovers public communities.
        // Explicit searches retain existing visibility behaviour.
        var results = nameTerm.isEmpty() && sportTerm.isEmpty()
                ? repository.discoverPublicPresentations(PageRequest.of(page, 20))
                : repository.searchPresentations(
                        escape(nameTerm), escape(sportTerm), PageRequest.of(page, 20));
        return new SearchResponse(results.getContent(), page, results.hasNext());
    }

    private String escape(String value) {
        return value.replace("!", "!!").replace("%", "!%").replace("_", "!_");
    }

    public record SearchResponse(List<CommunityDiscoveryResponse> communities, int page, boolean hasMore) {}
}
