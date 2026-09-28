package com.example.desporto24.model.dto.social;

public record FollowProfileResponse(
        String username,
        String fullName,
        String nickname,
        String profilePicUrl
) {
}
