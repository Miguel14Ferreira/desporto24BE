package com.example.desporto24.model.dto.community;

/**
 * Optional custom points for one sport+format inside a single season.
 * No rule means the existing sport-only ranking remains unchanged.
 */
public record CommunityRacketRuleResponse(
        String sport,
        String matchFormat,
        int winPoints,
        int lossPoints,
        String tieBreak
) {}
