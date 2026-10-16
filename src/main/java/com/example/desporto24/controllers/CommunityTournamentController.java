package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityTournamentPageResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentParticipantResponse;
import com.example.desporto24.model.dto.community.CommunityTournamentParticipantsPageResponse;
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

    @PutMapping("/{tournamentId}/participants/me")
    public ResponseEntity<CommunityTournamentParticipantResponse> join(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            Authentication authentication) {
        return ResponseEntity.ok(tournamentService.join(
                communityId, tournamentId, authentication.getName()));
    }

    @PutMapping("/{tournamentId}/participants/{profileId}/confirm")
    public ResponseEntity<CommunityTournamentParticipantResponse> confirmParticipant(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            @PathVariable Long profileId,
            Authentication authentication) {
        return ResponseEntity.ok(tournamentService.confirmParticipant(
                communityId, tournamentId, profileId, authentication.getName()));
    }

    @DeleteMapping("/{tournamentId}/participants/me")
    public ResponseEntity<Void> leave(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            Authentication authentication) {
        tournamentService.leave(communityId, tournamentId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{tournamentId}/participants/{profileId}")
    public ResponseEntity<Void> removeParticipant(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            @PathVariable Long profileId,
            Authentication authentication) {
        tournamentService.removeParticipant(communityId, tournamentId, profileId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{tournamentId}/participants")
    public ResponseEntity<CommunityTournamentParticipantsPageResponse> participants(
            @PathVariable Long communityId,
            @PathVariable Long tournamentId,
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        return ResponseEntity.ok(tournamentService.participants(
                communityId, tournamentId, authentication.getName(), page));
    }

    @GetMapping
    public ResponseEntity<CommunityTournamentPageResponse> list(
            @PathVariable Long communityId,
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        return ResponseEntity.ok(tournamentService.list(communityId, authentication.getName(), page));
    }
}
