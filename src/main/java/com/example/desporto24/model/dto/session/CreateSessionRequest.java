package com.example.desporto24.model.dto.session;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateSessionRequest(
        @NotBlank String sport,
        @NotNull Instant gameDate,
        @NotBlank String city,
        @NotBlank String address,
        @NotNull @Min(2) @Max(100) Integer maxParticipants,
        @DecimalMin("0.00") BigDecimal priceAmount,
        boolean privateGame,
        String password,
        Double latitude,
        Double longitude,
        Long communityId,
        Long venueId
) {
    public CreateSessionRequest(
            String sport,
            Instant gameDate,
            String city,
            String address,
            Integer maxParticipants,
            BigDecimal priceAmount,
            boolean privateGame,
            String password,
            Double latitude,
            Double longitude) {
        this(
                sport,
                gameDate,
                city,
                address,
                maxParticipants,
                priceAmount,
                privateGame,
                password,
                latitude,
                longitude,
                null,
                null
        );
    }

    public CreateSessionRequest(
            String sport,
            Instant gameDate,
            String city,
            String address,
            Integer maxParticipants,
            BigDecimal priceAmount,
            boolean privateGame,
            String password,
            Double latitude,
            Double longitude,
            Long communityId) {
        this(
                sport,
                gameDate,
                city,
                address,
                maxParticipants,
                priceAmount,
                privateGame,
                password,
                latitude,
                longitude,
                communityId,
                null
        );
    }
}
