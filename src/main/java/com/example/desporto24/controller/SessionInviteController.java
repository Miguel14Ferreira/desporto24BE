package com.example.desporto24.controller;

import com.example.desporto24.service.SessionInviteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sessions/{sessionId}/invites")
@RequiredArgsConstructor
public class SessionInviteController {
    private final SessionInviteService inviteService;

    public record InviteRequest(@NotBlank String username) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Long> invite(@PathVariable Long sessionId,
                                    @Valid @RequestBody InviteRequest request,
                                    Authentication authentication) {
        return Map.of("id", inviteService.invite(sessionId, authentication.getName(), request.username()));
    }
}
