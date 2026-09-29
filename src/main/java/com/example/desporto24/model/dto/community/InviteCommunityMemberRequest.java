package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;

public record InviteCommunityMemberRequest(@NotBlank String username) {
}
