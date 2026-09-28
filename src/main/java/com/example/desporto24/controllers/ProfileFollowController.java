package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.social.FollowProfileResponse;
import com.example.desporto24.model.dto.social.FollowStatusResponse;
import com.example.desporto24.service.ProfileFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class ProfileFollowController {

    private final ProfileFollowService followService;

    @PostMapping("/{username}")
    public FollowStatusResponse follow(
            @PathVariable String username,
            Authentication authentication) {
        return followService.follow(authentication.getName(), username);
    }

    @DeleteMapping("/{username}")
    public FollowStatusResponse unfollow(
            @PathVariable String username,
            Authentication authentication) {
        return followService.unfollow(authentication.getName(), username);
    }

    @GetMapping("/{username}/status")
    public FollowStatusResponse status(
            @PathVariable String username,
            Authentication authentication) {
        return followService.status(authentication.getName(), username);
    }

    @GetMapping("/{username}/followers")
    public List<FollowProfileResponse> followers(@PathVariable String username) {
        return followService.followers(username);
    }

    @GetMapping("/{username}/following")
    public List<FollowProfileResponse> following(@PathVariable String username) {
        return followService.following(username);
    }
}
