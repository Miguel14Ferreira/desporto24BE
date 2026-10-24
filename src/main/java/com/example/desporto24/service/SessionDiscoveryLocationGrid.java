package com.example.desporto24.service;

/**
 * Fixed public-discovery location cells for sports sessions.
 *
 * The browser may submit arbitrary origins and radii repeatedly. Returning
 * distances from the real stored coordinates would permit multilateration,
 * including for password-protected games whose address is intentionally hidden.
 * Both the returned distance AND the radius filter must use the same cell center.
 *
 * Cell size is roughly 5-7 km along each axis in mainland Portugal. This is
 * deliberate discovery-level precision, not a venue/meeting-place coordinate.
 * It is stable across users, sessions, requests and application instances.
 */
public final class SessionDiscoveryLocationGrid {

    private static final double LATITUDE_CELL_DEGREES = 0.05;
    private static final double LONGITUDE_CELL_DEGREES = 0.075;

    private SessionDiscoveryLocationGrid() {
    }

    public static ApproximatePoint approximate(double latitude, double longitude) {
        if (!Double.isFinite(latitude) || !Double.isFinite(longitude)
                || Math.abs(latitude) > 90.0 || Math.abs(longitude) > 180.0) {
            throw new IllegalArgumentException("Invalid session coordinates");
        }
        return new ApproximatePoint(
                cellCenter(latitude, LATITUDE_CELL_DEGREES),
                cellCenter(longitude, LONGITUDE_CELL_DEGREES));
    }

    private static double cellCenter(double value, double cellSize) {
        return (Math.floor(value / cellSize) + 0.5) * cellSize;
    }

    public record ApproximatePoint(double latitude, double longitude) {
    }
}
