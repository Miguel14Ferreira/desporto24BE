package com.example.desporto24.model.dto.social;

public record PostLikeResponse(
        boolean likedByMe,
        long likeCount
) {
}
