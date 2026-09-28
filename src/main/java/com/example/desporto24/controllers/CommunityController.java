package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.service.CommunityManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityManagementService service;

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
