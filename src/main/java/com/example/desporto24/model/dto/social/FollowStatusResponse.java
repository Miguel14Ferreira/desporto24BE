package com.example.desporto24.model.dto.social;

public record FollowStatusResponse(
        boolean followedByMe,
        boolean followsMe,
        long followers,
        long following
) {
}
