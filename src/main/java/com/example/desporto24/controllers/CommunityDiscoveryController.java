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
import java.util.function.Function;
import java.util.stream.Collectors;

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
        var community = repository.findWithSportsById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Community not found"));
        return new CommunityDiscoveryResponse(
                community.getId(),
                community.getName(),
                community.getDescription(),
                community.getSport(),
                community.getPhotoUrl(),
                community.getVisibility(),
                community.getSports(),
                community.getPhotoZoom(), community.getPhotoFocusX(), community.getPhotoFocusY()
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
        // Browse and filtered searches share one safe presentation projection:
        // PUBLIC and PRIVATE communities are discoverable, but never expose
        // member lists, chats, sessions or competition data to non-members.
        var results = repository.searchPresentations(
                escape(nameTerm), escape(sportTerm), PageRequest.of(page, 20));
        // Enrich only the current page, preserving the original sort order and page boundaries.
        var presentations = results.getContent();
        if (presentations.isEmpty()) {
            return new SearchResponse(presentations, page, results.hasNext());
        }
        var ids = presentations.stream().map(CommunityDiscoveryResponse::id).toList();
        var byId = repository.findAllWithSportsByIdIn(ids).stream()
                .collect(Collectors.toMap(community -> community.getId(), Function.identity()));
        var enriched = presentations.stream()
                .map(item -> item.withSports(
                        byId.containsKey(item.id()) ? byId.get(item.id()).getSports() : null))
                .toList();
        return new SearchResponse(enriched, page, results.hasNext());
    }

    private String escape(String value) {
        return value.replace("!", "!!").replace("%", "!%").replace("_", "!_");
    }

    public record SearchResponse(List<CommunityDiscoveryResponse> communities, int page, boolean hasMore) {}
}
