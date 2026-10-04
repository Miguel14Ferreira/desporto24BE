package com.example.desporto24.controllers;

import com.example.desporto24.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/players")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminPlayerController {
    private static final int PAGE_SIZE = 20;
    private final ProfileRepository profileRepository;

    @GetMapping
    public PlayerSearchResponse search(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page) {
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page");
        }
        String term = name.trim().toLowerCase(Locale.ROOT);
        if (term.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Search is too long");
        }
        if (term.isEmpty()) {
            return new PlayerSearchResponse(List.of(), page, false);
        }
        String escaped = term.replace("!", "!!").replace("%", "!%").replace("_", "!_");
        var results = profileRepository.searchAdminPlayers(
                escaped, PageRequest.of(page, PAGE_SIZE));
        var players = results.getContent().stream()
                .map(profile -> new PlayerSummary(
                        profile.getId(), profile.getUsername(), profile.getFullName(),
                        profile.getNickname(), profile.getRole(),
                        profile.getEnabled(), profile.getNotLocked()))
                .toList();
        return new PlayerSearchResponse(players, page, results.hasNext());
    }

    public record PlayerSummary(
            Long id, String username, String fullName, String nickname,
            String role, Boolean enabled, Boolean notLocked) {}
    public record PlayerSearchResponse(List<PlayerSummary> players, int page, boolean hasMore) {}
}
