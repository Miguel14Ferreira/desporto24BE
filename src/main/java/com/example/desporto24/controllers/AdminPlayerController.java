package com.example.desporto24.controllers;

import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityRoundEntryRepository;
import com.example.desporto24.enumeration.CommunityMatchResult;
import com.example.desporto24.service.CommunityCreationQuotaService;
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
    private final CommunityRoundEntryRepository roundEntryRepository;
    private final CommunityCreationQuotaService communityCreationQuota;

    @GetMapping("/{id}/community-limit")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public CommunityCreationQuotaService.Quota communityLimit(@PathVariable Long id) {
        var profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        return communityCreationQuota.quota(profile);
    }

    public record ChangeCommunityLimitRequest(Integer limit, String reason) {}

    @PutMapping("/{id}/community-limit")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public CommunityCreationQuotaService.Quota changeCommunityLimit(
            @PathVariable Long id,
            @RequestBody ChangeCommunityLimitRequest request,
            org.springframework.security.core.Authentication authentication) {
        var actor = profileRepository.findUserByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem autorização"));
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido obrigatório");
        }
        return communityCreationQuota.updateOverride(id, actor.getId(), request.limit(), request.reason());
    }

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
                        profile.getEnabled(), profile.getNotLocked(), profile.getModerationSuspendedAt()))
                .toList();
        return new PlayerSearchResponse(players, page, results.hasNext());
    }

    // Sport statistics are deliberately opt-in, avoiding an expensive query for
    // every row in the paginated administration search.
    @GetMapping("/{id}/stats")
    public PlayerStatsResponse statistics(@PathVariable Long id) {
        if (id == null || id <= 0 || !profileRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }
        var entries = roundEntryRepository.findEntriesForProfile(id);
        long games = entries.size();
        long wins = entries.stream().filter(entry -> entry.getResult() == CommunityMatchResult.WIN).count();
        long draws = entries.stream().filter(entry -> entry.getResult() == CommunityMatchResult.DRAW).count();
        long losses = entries.stream().filter(entry -> entry.getResult() == CommunityMatchResult.LOSS).count();
        long goals = entries.stream().mapToLong(entry -> entry.getGoals()).sum();
        long assists = entries.stream().mapToLong(entry -> entry.getAssists()).sum();
        long mvps = entries.stream().filter(entry -> entry.isMvp()).count();
        long cleanSheets = entries.stream().filter(entry -> entry.isCleanSheet()).count();
        double winRate = games == 0 ? 0.0 : Math.round(10000.0 * wins / games) / 100.0;
        return new PlayerStatsResponse(id, games, wins, draws, losses,
                goals, assists, mvps, cleanSheets, winRate);
    }

    public record PlayerStatsResponse(
            Long playerId, long games, long wins, long draws, long losses,
            long goals, long assists, long mvps, long cleanSheets, double winRate) {}

    public record PlayerSummary(
            Long id, String username, String fullName, String nickname,
            String role, Boolean enabled, Boolean notLocked, java.time.Instant moderationSuspendedAt) {}
    public record PlayerSearchResponse(List<PlayerSummary> players, int page, boolean hasMore) {}
}
