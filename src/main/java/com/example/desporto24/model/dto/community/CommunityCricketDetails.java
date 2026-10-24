package com.example.desporto24.model.dto.community;

/**
 * Legal deliveries, not balls bowled including wides/no-balls.
 * Side A bats first; side B chases. Runs remain in teamScore A/B.
 */
public record CommunityCricketDetails(
        Integer wicketsA, Integer legalBallsA,
        Integer wicketsB, Integer legalBallsB
) {}
