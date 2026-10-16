package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityTournamentPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentResponse;
import com.example.desporto24.model.dto.community.CreateCommunityTournamentRequest;
import com.example.desporto24.service.CommunityTournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/tournaments")
@PreAuthorize("isAuthenticated()")
public class CommunityTournamentController {
    private final CommunityTournamentService tournamentService;

    @PostMapping
    public ResponseEntity<CommunityTournamentResponse> create(
            @PathVariable Long communityId,
            @RequestBody @Valid CreateCommunityTournamentRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tournamentService.create(communityId, authentication.getName(), request));
    }

    @GetMapping
    public ResponseEntity<CommunityTournamentPageResponse> list(
            @PathVariable Long communityId,
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        return ResponseEntity.ok(tournamentService.list(communityId, authentication.getName(), page));
    }
}
