package com.example.desporto24.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VenueMapLinkServiceTest {
    private final VenueMapLinkService service = new VenueMapLinkService();

    @Test
    void usesExplicitPlacePointInsteadOfMapViewportCoordinates() {
        var result = service.resolve(
                "https://www.google.com/maps/place/Campo/@38.72,-9.14,16z/data=!3d38.745!4d-9.174");
        assertEquals(38.745, result.latitude(), 0.0000001);
        assertEquals(-9.174, result.longitude(), 0.0000001);
    }

    @Test
    void acceptsDirectGoogleMapsCoordinatesWithoutNetworkOrRedirects() {
        var result = service.resolve("https://www.google.com/maps?q=38.7223,-9.1393");
        assertEquals(38.7223, result.latitude(), 0.0000001);
        assertEquals(-9.1393, result.longitude(), 0.0000001);
    }

    @Test
    void rejectsExternalHostsBeforeResolvingAnyUrl() {
        assertThrows(IllegalArgumentException.class,
                () -> service.resolve("https://example.com/maps?q=38.7,-9.1"));
        assertThrows(IllegalArgumentException.class,
                () -> service.resolve("http://www.google.com/maps?q=38.7,-9.1"));
    }

    @Test
    void rejectsInvalidPointInsteadOfSavingMisleadingCoordinates() {
        assertThrows(IllegalArgumentException.class,
                () -> service.resolve("https://www.google.com/maps?q=91.0,-9.1"));
    }
}
