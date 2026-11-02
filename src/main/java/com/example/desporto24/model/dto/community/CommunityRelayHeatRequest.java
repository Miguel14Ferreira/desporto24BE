package com.example.desporto24.model.dto.community;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record CommunityRelayHeatRequest(
        @NotNull @DecimalMin("0.001") @Digits(integer=8,fraction=3) BigDecimal elapsedSeconds,
        @NotNull @Size(min=4,max=4) List<@NotNull Long> runnerProfileIds
) {}
