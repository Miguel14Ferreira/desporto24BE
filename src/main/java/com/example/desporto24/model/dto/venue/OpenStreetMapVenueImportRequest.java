package com.example.desporto24.model.dto.venue;

import com.example.desporto24.model.venue.Venue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public record OpenStreetMapVenueImportRequest(
        @NotBlank @Size(max = 255) String sourceReference,
        @NotBlank @Size(max = 140) String name,
        @NotBlank @Size(max = 120) String city,
        @Size(max = 255) String address,
        @Size(max = 255) String sports,
        @Size(max = 80) String venueType,
        Venue.EnvironmentType environmentType,
        @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
        @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
        @Size(max = 120) String district
) {
    public OpenStreetMapVenueImportRequest(
            String sourceReference, String name, String city, String address,
            String sports, String venueType, Venue.EnvironmentType environmentType,
            Double latitude, Double longitude) {
        this(sourceReference, name, city, address, sports, venueType,
                environmentType, latitude, longitude, null);
    }
    @AssertTrue(message = "Morada ou coordenadas GPS válidas obrigatórias")
    public boolean isValidVenueLocation() {
        boolean validGps = latitude != null && longitude != null
                && Double.isFinite(latitude) && Double.isFinite(longitude)
                && latitude >= 36.8 && latitude <= 42.3
                && longitude >= -9.6 && longitude <= -6.0;
        return validGps && (address == null || address.length() <= 255);
    }
}
