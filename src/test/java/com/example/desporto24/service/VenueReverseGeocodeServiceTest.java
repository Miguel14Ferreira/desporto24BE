package com.example.desporto24.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class VenueReverseGeocodeServiceTest {
    private final VenueReverseGeocodeService service = new VenueReverseGeocodeService();

    @Test void suggestsRealNearbyCityAndStreetWithoutGuessingHouseNumber() throws Exception {
        var loc = service.parse("""
            {"status":"ok","result":{"distance_m":24,"precision":"house",
             "components":{"road":"Rua de São João","house_number":"12",
               "city":"Agualva-Cacém","postcode":"2735-000","country":"Portugal"}}}
            """, 79d);
        assertEquals("Agualva-Cacém",loc.city());
        assertEquals("Rua de São João",loc.address());
        assertEquals(24,loc.distanceMeters());
    }

    @Test void onlyIncludesHouseNumberWhenAccuracyAndDistanceAreVeryHigh() throws Exception {
        var loc = service.parse("""
            {"result":{"distance_m":7,"components":{"city":"Lisboa","road":"Rua do Campo",
              "house_number":"10"}}}
            """, 12d);
        assertEquals("Rua do Campo, 10",loc.address());
        assertEquals("Lisboa",loc.city());
    }

    @Test void doesNotPresentDistantRoadAsExactAddress() throws Exception {
        var loc = service.parse("""
            {"result":{"distance_m":475,"components":{"city":"Sintra",
              "road":"Rua Distante","house_number":"99"}}}
            """, 35d);
        assertEquals("Sintra",loc.city());
        assertNull(loc.address());
    }

    @Test void safelyHandlesProviderMissingResultOrMissingStreet() throws Exception {
        assertNull(service.parse("{\"status\":\"ok\",\"result\":null}", 100d).city());
        var loc=service.parse("""
            {"result":{"distance_m":10,"components":{"village":"Magoito"}}}
            """, 100d);
        assertEquals("Magoito",loc.city());
        assertNull(loc.address());
    }

    @Test void rejectsInvalidCoordinatesBeforeSendingToProvider() {
        assertThrows(ResponseStatusException.class, ()->service.reverse(91d,0d,10d));
        assertThrows(ResponseStatusException.class, ()->service.reverse(0d,181d,10d));
        assertThrows(ResponseStatusException.class, ()->service.reverse(Double.NaN,0d,10d));
        assertThrows(ResponseStatusException.class, ()->service.reverse(10d,10d,-1d));
    }
}
