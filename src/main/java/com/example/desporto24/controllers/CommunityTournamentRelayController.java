package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.service.CommunityTournamentRelayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/tournaments/{tournamentId}/relays")
@PreAuthorize("isAuthenticated()")
public class CommunityTournamentRelayController {
    private final CommunityTournamentRelayService relays;

    @GetMapping
    public CommunityRelayOverviewResponse overview(@PathVariable Long communityId,
            @PathVariable Long tournamentId,Authentication authentication) {
        return relays.overview(communityId,tournamentId,authentication.getName());
    }

    @PostMapping("/final/publish")
    public CommunityRelayOverviewResponse publishFinal(@PathVariable Long communityId,
            @PathVariable Long tournamentId,Authentication authentication){
        return relays.publishFinal(communityId,tournamentId,authentication.getName());
    }

    @PutMapping("/final/heats/{heatNumber}/teams/{teamId}")
    public CommunityRelayOverviewResponse recordFinal(@PathVariable Long communityId,
            @PathVariable Long tournamentId,@PathVariable int heatNumber,
            @PathVariable Long teamId,@Valid @RequestBody CommunityRelayHeatRequest request,
            Authentication authentication){
        return relays.recordFinal(communityId,tournamentId,heatNumber,teamId,request,authentication.getName());
    }

    @PutMapping("/decisions/{heatId}")
    public CommunityRelayOverviewResponse decide(@PathVariable Long communityId,
            @PathVariable Long tournamentId,@PathVariable Long heatId,
            @Valid @RequestBody CommunityRelayDecisionRequest request,
            Authentication authentication) {
        return relays.decide(communityId,tournamentId,heatId,request,authentication.getName());
    }

    @PutMapping("/heats/{heatNumber}/teams/{teamId}")
    public CommunityRelayOverviewResponse record(@PathVariable Long communityId,
            @PathVariable Long tournamentId,@PathVariable int heatNumber,
            @PathVariable Long teamId,@Valid @RequestBody CommunityRelayHeatRequest request,
            Authentication authentication) {
        return relays.record(communityId,tournamentId,heatNumber,teamId,
                request,authentication.getName());
    }
}
