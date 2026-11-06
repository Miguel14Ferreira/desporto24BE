package com.example.desporto24.controllers;

import com.example.desporto24.service.CommunityDisciplineService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/discipline")
@PreAuthorize("isAuthenticated()")
public class CommunityDisciplineController {
    private final CommunityDisciplineService service;

    public record IssueWarningRequest(@NotNull Long targetProfileId,
                                      @NotBlank @Size(max = 1000) String reason) {}

    @PostMapping("/warnings")
    @ResponseStatus(HttpStatus.CREATED)
    public CommunityDisciplineService.WarningResponse issueWarning(
            @PathVariable Long communityId,
            @RequestBody @Valid IssueWarningRequest request,
            Authentication authentication) {
        return service.warn(communityId, authentication.getName(),
                request.targetProfileId(), request.reason());
    }

    @GetMapping("/warnings")
    public List<CommunityDisciplineService.WarningResponse> warnings(
            @PathVariable Long communityId, Authentication authentication) {
        return service.listForManagers(communityId, authentication.getName());
    }

    @GetMapping("/warnings/me")
    public List<CommunityDisciplineService.WarningResponse> mine(
            @PathVariable Long communityId, Authentication authentication) {
        return service.mine(communityId, authentication.getName());
    }
}
