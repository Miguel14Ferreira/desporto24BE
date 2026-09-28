package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.social.CreateSocialPostRequest;
import com.example.desporto24.model.dto.social.FeedItemResponse;
import com.example.desporto24.service.SocialFeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class SocialFeedController {

    private final SocialFeedService feedService;

    @GetMapping
    public List<FeedItemResponse> feed(Authentication authentication) {
        return feedService.feed(authentication.getName());
    }

    @PostMapping("/posts")
    public FeedItemResponse createPost(
            @RequestBody @Valid CreateSocialPostRequest request,
            Authentication authentication) {
        return feedService.createPost(authentication.getName(), request);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            Authentication authentication) {
        feedService.deletePost(postId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
