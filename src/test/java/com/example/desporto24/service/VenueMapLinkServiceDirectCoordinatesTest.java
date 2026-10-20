package com.example.desporto24.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VenueMapLinkServiceDirectCoordinatesTest {
    private final VenueMapLinkService service = new VenueMapLinkService();

    @Test
    void resolvesFullGoogleMapsLinkWithoutAnyOutboundRequest() {
        // A test-only fake Google Maps URL: an outbound request would fail.
        var point = service.resolve(
                "https://www.google.com/maps/place/Local+Test/@38.745,-9.185,17z/data=!3d38.747!4d-9.183");
        assertEquals(38.747, point.latitude(), 0.000001);
        assertEquals(-9.183, point.longitude(), 0.000001);
    }

    @Test
    void resolvesDirectCoordinatesFromGoogleMapsQuery() {
        var point = service.resolve("https://www.google.com/maps?q=41.14961,-8.61099");
        assertEquals(41.14961, point.latitude(), 0.000001);
        assertEquals(-8.61099, point.longitude(), 0.000001);
    }

    @Test
    void rejectsSpoofedGoogleDomainBeforeReadingCoordinates() {
        assertThrows(IllegalArgumentException.class, () ->
                service.resolve("https://google.com.example.org/maps?q=38.7,-9.1"));
    }

    @Test
    void rejectsOutOfRangeCoordinates() {
        assertThrows(IllegalArgumentException.class, () ->
                service.resolve("https://www.google.com/maps?q=99,-200"));
    }
}
