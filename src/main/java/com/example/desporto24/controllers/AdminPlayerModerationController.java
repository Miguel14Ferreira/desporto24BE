package com.example.desporto24.controllers;

import com.example.desporto24.exception.AccountDeletionBlockedException;
import com.example.desporto24.service.AdminPlayerModerationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/players")
public class AdminPlayerModerationController {
    private final AdminPlayerModerationService service;

    public AdminPlayerModerationController(AdminPlayerModerationService service) {
        this.service = service;
    }

    @PostMapping("/{id}/moderation")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public AdminPlayerModerationService.ModerationResult moderate(
            @PathVariable Long id,
            @RequestBody AdminPlayerModerationService.ModerationRequest request,
            Principal principal) {
        return service.moderate(principal.getName(), id, request);
    }

    @ExceptionHandler(AccountDeletionBlockedException.class)
    public ResponseEntity<Map<String, String>> deletionBlocked(AccountDeletionBlockedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("code", "COMMUNITY_OWNERSHIP_BLOCKS_DELETION"));
    }
}
