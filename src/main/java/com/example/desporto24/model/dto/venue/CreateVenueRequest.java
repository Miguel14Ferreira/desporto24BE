package com.example.desporto24.model.dto.venue;

import com.example.desporto24.model.venue.Venue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateVenueRequest(
        @NotBlank @Size(max = 140) String name,
        @NotBlank @Size(max = 120) String city,
        @NotBlank @Size(max = 255) String address,
        @Size(max = 255) String sports,
        @Size(max = 80) String venueType,
        Venue.OwnershipType ownershipType,
        @Size(max = 80) String surfaceType,
        boolean indoor,
        boolean changingRooms,
        Boolean toilets,
        Boolean lockers,
        Boolean accessible,
        Boolean firstAid,
        Boolean aed,
        Boolean cafe,
        @Size(max = 500) String services,
        @Size(max = 500) String transport,
        @Size(max = 1000) String openingHours,
        @Size(max = 255) String priceInfo,
        boolean parking,
        boolean lighting,
        @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
        @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude
) {
}
