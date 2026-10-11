package com.example.desporto24.model.dto.session;

import java.math.BigDecimal;
import java.time.Instant;

public record SessionSearchCriteria(
        String sport,
        String city,
        Instant from,
        Instant to,
        Double latitude,
        Double longitude,
        Double radiusKm,
        String availability,
        Boolean privateGame,
        Boolean freeOnly,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String sort,
        String query
) {
    public SessionSearchCriteria(String sport, String city, Instant from, Instant to,
                                 Double latitude, Double longitude, Double radiusKm,
                                 String availability, Boolean privateGame, Boolean freeOnly,
                                 BigDecimal minPrice, BigDecimal maxPrice, String sort) {
        this(sport, city, from, to, latitude, longitude, radiusKm, availability,
                privateGame, freeOnly, minPrice, maxPrice, sort, null);
    }
}
