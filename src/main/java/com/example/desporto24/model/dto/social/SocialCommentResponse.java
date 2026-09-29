package com.example.desporto24.model.dto.social;

import java.time.Instant;

public record SocialCommentResponse(
        Long id,
        Long postId,
        String authorUsername,
        String authorDisplayName,
        String authorProfilePicUrl,
        String content,
        Instant createdAt
) {
}
