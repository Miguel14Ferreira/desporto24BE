package com.example.desporto24.model.dto.venue;

import java.util.List;

/**
 * Existing catalog coverage only: the absence of records for a district
 * does not establish that there are no real-world sports venues there.
 */
public record VenueCoverageReport(
        long totalVenues,
        long withoutDistrict,
        long withoutCoordinates,
        long withoutSports,
        List<DistrictCoverage> districts
) {
    public record DistrictCoverage(
            String district,
            long venues,
            long missingCoordinates,
            long missingSports,
            long openStreetMapVenues
    ) {
    }
}
