package com.example.desporto24.model.dto.friend;

public record FriendSummaryResponse(
        String username,
        String fullName,
        String nickname,
        String profilePicUrl,
        boolean chatBlocked,
        boolean blockedByMe
) {
}
