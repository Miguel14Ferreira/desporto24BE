package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateCommunitySportsRequest(
        @NotNull @Size(max = 128) List<@NotBlank @Size(max = 100) String> sports
) {
}
