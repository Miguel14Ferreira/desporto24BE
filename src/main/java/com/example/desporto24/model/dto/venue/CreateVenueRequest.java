package com.example.desporto24.model.dto.venue;

import com.example.desporto24.model.venue.Venue;
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
        boolean parking,
        boolean lighting,
        Double latitude,
        Double longitude
) {
}
