package com.example.desporto24.model.dto.venue;

import java.util.Locale;

/**
 * Location data quality. Does not attest that a venue is open or accessible;
 * supplied postal address text is not necessarily independently verified.
 */
public final class VenueLocationStatus {
    public static final String GPS_ONLY = "GPS_ONLY_ADDRESS_UNKNOWN";
    public static final String ADDRESS_PROVIDED = "ADDRESS_PROVIDED_UNVERIFIED";
    public static final String UNKNOWN = "LOCATION_UNKNOWN";

    private VenueLocationStatus() {}

    public static String from(String address, Double latitude, Double longitude) {
        boolean hasGps = latitude != null && longitude != null
                && Double.isFinite(latitude) && Double.isFinite(longitude)
                && Math.abs(latitude) <= 90 && Math.abs(longitude) <= 180;

        String value = address == null ? "" : address.strip().toLowerCase(Locale.ROOT);
        boolean placeholder = value.isEmpty()
                || value.startsWith("localização no mapa:")
                || value.startsWith("localizacao no mapa:")
                || value.startsWith("coordenadas (morada por confirmar)")
                || value.contains("morada por confirmar")
                || value.contains("address to be confirmed")
                || value.startsWith("map location:");

        if (placeholder) return hasGps ? GPS_ONLY : UNKNOWN;
        return ADDRESS_PROVIDED;
    }
}
