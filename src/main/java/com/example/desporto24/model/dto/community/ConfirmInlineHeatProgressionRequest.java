package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ConfirmInlineHeatProgressionRequest(
        @NotEmpty @Size(max = 20) List<Long> sourceRoundIds,
        @NotBlank @Size(min = 10, max = 255) String reason
) {}
