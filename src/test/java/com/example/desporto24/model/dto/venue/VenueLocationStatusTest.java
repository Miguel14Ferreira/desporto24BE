package com.example.desporto24.model.dto.venue;

import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.service.VenueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VenueLocationStatusTest {
    @Test
    void gpsPlaceholderIsNotPostalAddress() {
        assertEquals("GPS_ONLY_ADDRESS_UNKNOWN", VenueLocationStatus.from(
                "Localização no mapa: 40.4, -7.7 (morada por confirmar)", 40.4, -7.7));
        assertEquals("GPS_ONLY_ADDRESS_UNKNOWN", VenueLocationStatus.from(null, 40.4, -7.7));
        assertEquals("LOCATION_UNKNOWN", VenueLocationStatus.from("morada por confirmar", null, null));
    }

    @Test
    void realLookingAddressDoesNotMeanVerified() {
        assertEquals("ADDRESS_PROVIDED_UNVERIFIED",
                VenueLocationStatus.from("Rua do Campo 10, Coimbra", 40.2, -8.4));
    }

    @Test
    void invalidCoordinatesDoNotQualifyAsGps() {
        assertEquals("LOCATION_UNKNOWN", VenueLocationStatus.from("", Double.NaN, -8.3));
        assertEquals("LOCATION_UNKNOWN", VenueLocationStatus.from("", 40.3, null));
        assertEquals("LOCATION_UNKNOWN", VenueLocationStatus.from("", 99.0, -8.3));
    }

    @Test
    void jsonHasLocationStatusWithoutChangingExistingResponseFields() throws Exception {
        var response = new VenueService(null, null, null).toResponse(Venue.builder()
                .id(11L).name("Campo de Teste").city("Coimbra")
                .address("Localização no mapa: 40.2, -8.4 (morada por confirmar)")
                .latitude(40.2).longitude(-8.4).build());
        var json = new ObjectMapper().valueToTree(response);
        assertEquals("GPS_ONLY_ADDRESS_UNKNOWN", json.path("locationStatus").asText());
        assertEquals("Coimbra", json.path("city").asText());
        assertEquals(40.2, json.path("latitude").asDouble());
    }
}
