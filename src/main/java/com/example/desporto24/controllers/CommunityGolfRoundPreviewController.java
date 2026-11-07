package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.CommunityGolfRoundStandingResponse;
import com.example.desporto24.service.CommunityGolfRoundPreviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}/golf/rounds")
@PreAuthorize("isAuthenticated()")
public class CommunityGolfRoundPreviewController {
    private final CommunityGolfRoundPreviewService preview;

    @GetMapping("/{roundId}/standings")
    public List<CommunityGolfRoundStandingResponse> standings(
            @PathVariable Long communityId, @PathVariable Long roundId,
            Authentication authentication) {
        return preview.preview(communityId, roundId, authentication.getName()).stream()
                .map(s -> new CommunityGolfRoundStandingResponse(
                        s.position(), s.playerId(), s.grossStrokes())).toList();
    }
}
