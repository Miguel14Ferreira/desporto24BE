package com.example.desporto24.model.dto.community;
import jakarta.validation.constraints.*;
public record CommunityRelayDecisionRequest(
    @NotBlank String status,
    @NotBlank @Size(min=5,max=500) String reason
) {}
