package com.example.desporto24.controllers;

import com.example.desporto24.service.CommunityInlineSpeedProgressionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/tournaments/{tournamentId}/inline-speed")
@PreAuthorize("isAuthenticated()")
public class CommunityInlineSpeedProgressionController {
    private final CommunityInlineSpeedProgressionService service;

    public record ApproveRequest(
            @NotEmpty List<@NotNull @Positive Long> playerIds,
            @Size(max = 500) String reason) {}

    public record TieBreakApproveRequest(
            @NotEmpty List<@NotNull @Positive Long> playerIds,
            @Size(max = 500) String reason,
            String evidenceType,
            @Size(max = 500) String evidenceReference,
            List<@NotNull @Positive Long> trialIds,
            @Size(max = 500) String trialOrderExceptionReason) {}

    @GetMapping
    public CommunityInlineSpeedProgressionService.Overview overview(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            Authentication auth) {
        return service.overview(communityId, tournamentId, auth.getName());
    }

    @PutMapping("/heats/{roundId}")
    public CommunityInlineSpeedProgressionService.Overview link(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable Long roundId, @RequestParam(defaultValue = "1") int heatNumber,
            Authentication auth) {
        return service.link(communityId, tournamentId, roundId, heatNumber, auth.getName());
    }

    @DeleteMapping("/heats/{roundId}")
    public CommunityInlineSpeedProgressionService.Overview unlink(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable Long roundId, Authentication auth) {
        return service.unlink(communityId, tournamentId, roundId, auth.getName());
    }

    /**
     * Record the jury's tie-break ruling independently of the original heat
     * results. A later confirmation still must ratify qualified skaters.
     */
    @PutMapping("/tiebreaks/{sourceStage}")
    public CommunityInlineSpeedProgressionService.Overview resolveTieBreak(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable String sourceStage, @RequestBody @Valid TieBreakApproveRequest request,
            Authentication auth) {
        return service.resolveTieBreak(communityId, tournamentId, sourceStage,
                new CommunityInlineSpeedProgressionService.TieBreakRequest(
                        request.playerIds(), request.reason(),
                        request.evidenceType(), request.evidenceReference(),
                        request.trialIds(), request.trialOrderExceptionReason()), auth.getName());
    }

    /** Record the actual results of a trial only after it has happened. */
    @PostMapping("/tiebreaks/{sourceStage}/trials")
    public CommunityInlineSpeedProgressionService.Overview recordTrial(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable String sourceStage,
            @RequestBody @Valid CommunityInlineSpeedProgressionService.TrialCreateRequest request,
            Authentication auth) {
        return service.recordTieTrial(communityId, tournamentId, sourceStage,
                request, auth.getName());
    }

    @PutMapping("/qualifications/{sourceStage}")
    public CommunityInlineSpeedProgressionService.Overview confirm(
            @PathVariable Long communityId, @PathVariable Long tournamentId,
            @PathVariable String sourceStage, @RequestBody @Valid ApproveRequest request,
            Authentication auth) {
        return service.confirm(communityId, tournamentId, sourceStage,
                new CommunityInlineSpeedProgressionService.ConfirmRequest(
                        request.playerIds(), request.reason()), auth.getName());
    }
}
