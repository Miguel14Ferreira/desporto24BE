package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.service.CommunityCompetitionService;
import com.example.desporto24.service.CommunityImportService;
import com.example.desporto24.service.CommunityStandingsArchiveService;
import com.example.desporto24.service.CommunityHistoricalMatchPreviewService;
import com.example.desporto24.service.CommunityHistoricalMatchArchiveService;
import com.example.desporto24.service.CommunityManagementService;
import com.example.desporto24.service.CommunityHistoricalPlayerLinkService;
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
    private final CommunityHistoricalPlayerLinkService historicalPlayerLinkService;
    private final CommunityImportService communityImportService;
    private final CommunityStandingsArchiveService historicalStandingsService;
    private final CommunityHistoricalMatchPreviewService historicalMatchPreviewService;
    private final CommunityHistoricalMatchArchiveService historicalMatchArchiveService;
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

    @PostMapping("/{id}/join")
    public ResponseEntity<CommunityResponse> joinPublic(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(service.joinPublic(id, authentication.getName()));
    }

    @PostMapping("/{id}/join-requests")
    public ResponseEntity<CommunityJoinRequestResponse> requestJoin(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(service.requestJoin(id, authentication.getName()));
    }

    @GetMapping("/{id}/join-requests")
    public ResponseEntity<List<CommunityJoinRequestResponse>> pendingJoinRequests(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(service.pendingJoinRequests(id, authentication.getName()));
    }

    @GetMapping("/{id}/join-requests/me")
    public ResponseEntity<CommunityJoinRequestResponse> myPendingJoinRequest(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.of(service.myPendingJoinRequest(id, authentication.getName()));
    }

    @PostMapping("/{id}/join-requests/{requestId}/accept")
    public ResponseEntity<CommunityResponse> acceptJoinRequest(
            @PathVariable Long id,
            @PathVariable Long requestId,
            Authentication authentication) {
        return ResponseEntity.ok(service.acceptJoinRequest(id, requestId, authentication.getName()));
    }

    @PostMapping("/{id}/join-requests/{requestId}/reject")
    public ResponseEntity<Void> rejectJoinRequest(
            @PathVariable Long id,
            @PathVariable Long requestId,
            Authentication authentication) {
        service.rejectJoinRequest(id, requestId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/features")
    public ResponseEntity<CommunityResponse> updateFeatures(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCommunityFeaturesRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.updateFeatures(id, authentication.getName(), request));
    }

    @PutMapping("/{id}/sports")
    public ResponseEntity<CommunityResponse> updateSports(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCommunitySportsRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.updateSports(id, authentication.getName(), request));
    }

    @PutMapping("/{id}/visibility")
    public ResponseEntity<CommunityResponse> updateVisibility(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCommunityVisibilityRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.updateVisibility(id, authentication.getName(), request));
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

    @GetMapping("/{id}/competition/historical-players")
    public ResponseEntity<List<HistoricalPlayerCandidateResponse>> historicalPlayerCandidates(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(historicalPlayerLinkService.candidates(id, authentication.getName()));
    }

    @PostMapping("/{id}/competition/historical-player-links")
    public ResponseEntity<HistoricalPlayerLinkResponse> proposeHistoricalPlayerLink(
            @PathVariable Long id, @RequestBody @Valid ProposeHistoricalPlayerLinkRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(historicalPlayerLinkService.propose(id, authentication.getName(), request));
    }

    @GetMapping("/{id}/competition/historical-player-links/me")
    public ResponseEntity<List<HistoricalPlayerLinkResponse>> myHistoricalPlayerLinks(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(historicalPlayerLinkService.pendingForMe(id, authentication.getName()));
    }

    @PostMapping("/{id}/competition/historical-player-links/{linkId}/accept")
    public ResponseEntity<HistoricalPlayerLinkResponse> acceptHistoricalPlayerLink(
            @PathVariable Long id, @PathVariable Long linkId, Authentication authentication) {
        return ResponseEntity.ok(historicalPlayerLinkService.decide(id, linkId, authentication.getName(), true));
    }

    @PostMapping("/{id}/competition/historical-player-links/{linkId}/reject")
    public ResponseEntity<HistoricalPlayerLinkResponse> rejectHistoricalPlayerLink(
            @PathVariable Long id, @PathVariable Long linkId, Authentication authentication) {
        return ResponseEntity.ok(historicalPlayerLinkService.decide(id, linkId, authentication.getName(), false));
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

    /**
     * Safe read-only first step for importing real historical tournament matches.
     * No match, participant, team or tournament is created by this endpoint.
     */
    @PostMapping(value = "/{id}/competition/import/matches/preview",
            consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityHistoricalMatchPreviewResponse> previewHistoricalMatches(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(historicalMatchPreviewService.preview(id, authentication.getName(), file, mapping));
    }

    /** Preserve real old fixtures in a separate archive, never a live tournament. */
    @PostMapping(value = "/{id}/competition/import/matches/confirm",
            consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityHistoricalMatchArchiveResponse> confirmHistoricalMatches(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            @RequestPart("context") @Valid CommunityHistoricalMatchArchiveConfirmRequest context,
            Authentication authentication) {
        return ResponseEntity.ok(historicalMatchArchiveService.confirm(
                id, authentication.getName(), file, mapping, context));
    }

    @GetMapping("/{id}/competition/match-archives")
    public ResponseEntity<List<CommunityHistoricalMatchArchiveSummaryResponse>> historicalMatchArchives(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(historicalMatchArchiveService.list(id, authentication.getName()));
    }

    @GetMapping("/{id}/competition/match-archives/{archiveId}")
    public ResponseEntity<CommunityHistoricalMatchArchiveResponse> historicalMatchArchive(
            @PathVariable Long id, @PathVariable Long archiveId, Authentication authentication) {
        return ResponseEntity.ok(historicalMatchArchiveService.detail(
                id, archiveId, authentication.getName()));
    }

    /**
     * Archived rankings do not invent matches or alter the live standings.
     * The same workbook and mapping must be sent to preview and confirmation.
     */
    @PostMapping(value = "/{id}/competition/import/standings/preview",
            consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityStandingsArchivePreviewResponse> previewHistoricalStandings(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            Authentication authentication) {
        return ResponseEntity.ok(historicalStandingsService.preview(id, authentication.getName(), file, mapping));
    }

    @PostMapping(value = "/{id}/competition/import/standings/confirm",
            consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommunityStandingsArchiveResponse> confirmHistoricalStandings(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("mapping") @Valid CommunityImportMappingRequest mapping,
            @RequestPart("context") @Valid CommunityStandingsArchiveConfirmRequest context,
            Authentication authentication) {
        return ResponseEntity.ok(historicalStandingsService.confirm(id, authentication.getName(), file, mapping, context));
    }

    @GetMapping("/{id}/competition/standings-archives")
    public ResponseEntity<List<CommunityStandingsArchiveSummaryResponse>> historicalStandingsArchives(
            @PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(historicalStandingsService.list(id, authentication.getName()));
    }

    @GetMapping("/{id}/competition/standings-archives/{archiveId}")
    public ResponseEntity<CommunityStandingsArchiveResponse> historicalStandingsArchive(
            @PathVariable Long id, @PathVariable Long archiveId, Authentication authentication) {
        return ResponseEntity.ok(historicalStandingsService.detail(id, archiveId, authentication.getName()));
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
