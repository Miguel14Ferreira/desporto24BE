package com.example.desporto24.model.dto.community;

import com.example.desporto24.enumeration.CommunityRole;

public record CommunityMemberResponse(
        Long profileId,
        String username,
        String fullName,
        String profilePicUrl,
        CommunityRole role
) {
}
