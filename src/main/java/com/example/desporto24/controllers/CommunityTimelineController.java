package com.example.desporto24.controllers;

import com.example.desporto24.model.community.CommunityTimelineEntry.Kind;
import com.example.desporto24.model.dto.community.CommunityTimelineEntryRequest;
import com.example.desporto24.model.dto.community.CommunityTimelineEntryResponse;
import com.example.desporto24.service.CommunityTimelineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Separate from the public social feed and from short-lived session chat. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities/{communityId}")
public class CommunityTimelineController {
    private final CommunityTimelineService service;

    @GetMapping("/posts")
    public List<CommunityTimelineEntryResponse> posts(
            @PathVariable Long communityId, Authentication authentication) {
        return service.list(communityId, Kind.POST, authentication.getName());
    }

    @PostMapping("/posts")
    public CommunityTimelineEntryResponse publish(
            @PathVariable Long communityId, @RequestBody @Valid CommunityTimelineEntryRequest request,
            Authentication authentication) {
        return service.post(communityId, Kind.POST, request.body(), authentication.getName());
    }

    @DeleteMapping("/posts/{entryId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long communityId, @PathVariable Long entryId, Authentication authentication) {
        service.delete(communityId, Kind.POST, entryId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/messages")
    public List<CommunityTimelineEntryResponse> messages(
            @PathVariable Long communityId, Authentication authentication) {
        return service.list(communityId, Kind.CHAT, authentication.getName());
    }

    @GetMapping("/messages/older")
    public List<CommunityTimelineEntryResponse> olderMessages(
            @PathVariable Long communityId, @RequestParam Long before,
            Authentication authentication) {
        return service.olderMessages(communityId, before, authentication.getName());
    }

    @PostMapping("/messages")
    public CommunityTimelineEntryResponse message(
            @PathVariable Long communityId, @RequestBody @Valid CommunityTimelineEntryRequest request,
            Authentication authentication) {
        return service.post(communityId, Kind.CHAT, request.body(), authentication.getName());
    }

    @DeleteMapping("/messages/{entryId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long communityId, @PathVariable Long entryId, Authentication authentication) {
        service.delete(communityId, Kind.CHAT, entryId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
