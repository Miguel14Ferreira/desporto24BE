package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;

public record TransferOwnershipRequest(@NotBlank String username) {
}
