package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.social.CreateSocialPostRequest;
import com.example.desporto24.model.dto.moderation.CreateUserReportRequest;
import com.example.desporto24.model.dto.social.CreateSocialCommentRequest;
import com.example.desporto24.model.dto.social.PostLikeResponse;
import com.example.desporto24.model.dto.social.SocialCommentResponse;
import com.example.desporto24.model.dto.social.FeedItemResponse;
import com.example.desporto24.service.SocialFeedService;
import com.example.desporto24.service.SocialPostInteractionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class SocialFeedController {

    private final SocialFeedService feedService;
    private final SocialPostInteractionService interactionService;

    @GetMapping
    public List<FeedItemResponse> feed(Authentication authentication) {
        return feedService.feed(authentication.getName());
    }

    @PostMapping(
            value = "/posts",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public FeedItemResponse createPost(
            @RequestBody @Valid CreateSocialPostRequest request,
            Authentication authentication) {
        return feedService.createPost(authentication.getName(), request);
    }

    @PostMapping(
            value = "/posts",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public FeedItemResponse createPostWithImage(
            @RequestPart("content") String content,
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication authentication) {
        return feedService.createPostWithImage(
                authentication.getName(),
                content,
                image
        );
    }

    @PostMapping("/posts/{postId}/likes")
    public PostLikeResponse likePost(
            @PathVariable Long postId,
            Authentication authentication) {
        return interactionService.like(postId, authentication.getName());
    }

    @DeleteMapping("/posts/{postId}/likes")
    public PostLikeResponse unlikePost(
            @PathVariable Long postId,
            Authentication authentication) {
        return interactionService.unlike(postId, authentication.getName());
    }

    @GetMapping("/posts/{postId}/comments")
    public List<SocialCommentResponse> comments(@PathVariable Long postId) {
        return interactionService.comments(postId);
    }

    @PostMapping("/posts/{postId}/comments")
    public SocialCommentResponse comment(
            @PathVariable Long postId,
            @RequestBody @Valid CreateSocialCommentRequest request,
            Authentication authentication) {
        return interactionService.comment(
                postId,
                authentication.getName(),
                request
        );
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Authentication authentication) {
        interactionService.deleteComment(
                postId,
                commentId,
                authentication.getName()
        );
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{postId}/report")
    public ResponseEntity<Void> reportPost(
            @PathVariable Long postId,
            @RequestBody @Valid CreateUserReportRequest request,
            Authentication authentication) {
        interactionService.reportPost(
                postId,
                authentication.getName(),
                request
        );
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            Authentication authentication) {
        feedService.deletePost(postId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
