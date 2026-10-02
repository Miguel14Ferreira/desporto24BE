package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.service.CommunityCompetitionService;
import com.example.desporto24.service.CommunityImportService;
import com.example.desporto24.service.CommunityManagementService;
import com.example.desporto24.service.CommunitySessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityManagementService service;
    private final CommunityCompetitionService competitionService;
    private final CommunityImportService communityImportService;
    private final CommunitySessionService communitySessionService;

    @PostMapping
    public ResponseEntity<CommunityResponse> create(
            @RequestBody @Valid CreateCommunityRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.create(authentication.getName(), request));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<CommunityResponse>> mine(Authentication authentication) {
        return ResponseEntity.ok(service.mine(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityResponse> get(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(service.get(id, authentication.getName()));
    }

    @PutMapping(value = "/{id}/photo", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityResponse> updatePhoto(
            @PathVariable Long id,
            @RequestPart("photo") MultipartFile photo,
            Authentication authentication) {
        return ResponseEntity.ok(service.updatePhoto(id, authentication.getName(), photo));
    }

    @DeleteMapping("/{id}/photo")
    public ResponseEntity<CommunityResponse> removePhoto(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(service.removePhoto(id, authentication.getName()));
    }

    @PostMapping("/{id}/invites")
    public ResponseEntity<CommunityInviteResponse> invite(
            @PathVariable Long id,
            @RequestBody @Valid InviteCommunityMemberRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.invite(id, authentication.getName(), request));
    }

    @GetMapping("/invites/me")
    public ResponseEntity<List<CommunityInviteResponse>> myInvites(Authentication authentication) {
        return ResponseEntity.ok(service.myInvites(authentication.getName()));
    }

    @PostMapping("/invites/{inviteId}/accept")
    public ResponseEntity<CommunityResponse> acceptInvite(
            @PathVariable Long inviteId,
            Authentication authentication) {
        return ResponseEntity.ok(service.acceptInvite(inviteId, authentication.getName()));
    }

    @PostMapping("/invites/{inviteId}/reject")
    public ResponseEntity<Void> rejectInvite(
            @PathVariable Long inviteId,
            Authentication authentication) {
        service.rejectInvite(inviteId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/sessions")
    public ResponseEntity<List<SessionSummaryResponse>> sessions(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(
                communitySessionService.sessions(id, authentication.getName())
        );
    }

    @PostMapping("/{id}/sessions/{sessionId}/open-public")
    public ResponseEntity<SessionSummaryResponse> openSessionToPublic(
            @PathVariable Long id,
            @PathVariable Long sessionId,
            Authentication authentication) {
        return ResponseEntity.ok(
                communitySessionService.openToPublic(id, sessionId, authentication.getName())
        );
    }


    @GetMapping("/{id}/competition")
    public ResponseEntity<CommunityCompetitionOverviewResponse> competitionOverview(
            @PathVariable Long id,
            @RequestParam(required = false) Long seasonId,
            Authentication authentication) {
        return ResponseEntity.ok(
                competitionService.overview(id, authentication.getName(), seasonId)
        );
    }

    @PostMapping("/{id}/seasons")
    public ResponseEntity<CommunitySeasonResponse> createSeason(
            @PathVariable Long id,
            @RequestBody @Valid CreateCommunitySeasonRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                competitionService.createSeason(id, authentication.getName(), request)
        );
    }

    @PostMapping("/{id}/seasons/{seasonId}/close")
    public ResponseEntity<CommunitySeasonResponse> closeSeason(
            @PathVariable Long id,
            @PathVariable Long seasonId,
            Authentication authentication) {
        return ResponseEntity.ok(
                competitionService.closeSeason(id, seasonId, authentication.getName())
        );
    }

    @PutMapping("/{id}/competition/settings")
    public ResponseEntity<CommunityCompetitionSettingsResponse> updateCompetitionSettings(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCommunityCompetitionSettingsRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                competitionService.updateSettings(id, authentication.getName(), request)
        );
    }

    @PostMapping("/{id}/competition/import/context/validate")
    public ResponseEntity<CommunityImportContextValidationResponse> validateCompetitionImportContext(
            @PathVariable Long id,
            @RequestBody @Valid CommunityImportContextRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.validateContext(id, authentication.getName(), request)
        );
    }

    @PostMapping(value = "/{id}/competition/import/preview", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportPreviewResponse> previewCompetitionImport(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.preview(id, authentication.getName(), file)
        );
    }

    @PostMapping(value = "/{id}/competition/import/validate", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportMappingValidationResponse> validateCompetitionImport(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.validateMapping(id, authentication.getName(), file, mapping)
        );
    }

    @PostMapping(value = "/{id}/competition/import/normalized-preview", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportNormalizedPreviewResponse> normalizedCompetitionImportPreview(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.normalizedPreview(id, authentication.getName(), file, mapping)
        );
    }

    @PostMapping(value = "/{id}/competition/import/plan", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportPlanResponse> planCompetitionImport(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.importPlan(id, authentication.getName(), file, mapping)
        );
    }

    @PostMapping(value = "/{id}/competition/import/rounds/plan", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportRoundsPlanResponse> planCompetitionImportRounds(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.roundsPlan(id, authentication.getName(), file, mapping)
        );
    }

    @PostMapping(value = "/{id}/competition/import/rounds/final-plan", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportRoundsPlanResponse> finalCompetitionImportRoundsPlan(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            @RequestPart("context") @Valid CommunityImportContextRequest context,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.finalRoundsPlan(id, authentication.getName(), file, mapping, context)
        );
    }

    @PostMapping(value = "/{id}/competition/import/rounds/confirm", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportRoundsResultResponse> confirmCompetitionImportRounds(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            @RequestPart("context") @Valid CommunityImportContextRequest context,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.confirmRounds(id, authentication.getName(), file, mapping, context)
        );
    }

    @PostMapping(value = "/{id}/competition/import/players/confirm", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityImportPlayersResultResponse> confirmCompetitionImportPlayers(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(
                communityImportService.confirmPlayers(id, authentication.getName(), file, mapping)
        );
    }

    @PostMapping("/{id}/rounds")
    public ResponseEntity<CommunityRoundResponse> createRound(
            @PathVariable Long id,
            @RequestBody @Valid CreateCommunityRoundRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                competitionService.createRound(id, authentication.getName(), request)
        );
    }

    @DeleteMapping("/{id}/rounds/{roundId}")
    public ResponseEntity<Void> deleteRound(
            @PathVariable Long id,
            @PathVariable Long roundId,
            Authentication authentication) {
        competitionService.deleteRound(id, roundId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/members/{username}/role")
    public ResponseEntity<CommunityResponse> updateRole(
            @PathVariable Long id,
            @PathVariable String username,
            @RequestBody @Valid UpdateCommunityRoleRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.updateRole(
                id,
                authentication.getName(),
                username,
                request
        ));
    }

    @DeleteMapping("/{id}/members/{username}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long id,
            @PathVariable String username,
            Authentication authentication) {
        service.removeMember(id, authentication.getName(), username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<Void> leave(
            @PathVariable Long id,
            Authentication authentication) {
        service.leave(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/owner")
    public ResponseEntity<CommunityResponse> transferOwnership(
            @PathVariable Long id,
            @RequestBody @Valid TransferOwnershipRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.transferOwnership(
                id,
                authentication.getName(),
                request
        ));
    }
}
