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

    @Test void resolvesExactCacemStreetWhereMunicipalityProviderOnlyReturnsSintra() throws Exception {
        // Both fixtures are live provider responses for the same public street
        // near Rua do Olival, Cacém, and deliberately not a user's GPS.
        var coarse = service.parse("""
            {"status":"ok","result":{"distance_m":848,"precision":"city",
              "components":{"suburb":"Paiões","city":"Sintra","country":"Portugal"}}}
            """, 78d);
        assertNull(coarse.address());
        var detailed = service.parsePhoton("""
            {"type":"FeatureCollection","features":[{"type":"Feature",
              "properties":{"type":"street","name":"Rua do Olival","district":"Cacém",
              "city":"Cacém e São Marcos","postcode":"2735-303"},
              "geometry":{"type":"Point","coordinates":[-9.3104716,38.769178]}}]}
            """, 38.7692039, -9.3103216);
        assertEquals("Cacém", detailed.city());
        assertEquals("Rua do Olival", detailed.address());
        assertTrue(detailed.distanceMeters() < 25);
        assertEquals("OpenStreetMap / Photon", detailed.provider());
    }

    @Test void labelsFartherStreetsApproximateInsteadOfInventingTheirNumber() throws Exception {
        var detailed = service.parsePhoton("""
            {"features":[{"properties":{"type":"street","name":"Rua do Campo",
              "district":"Cacém"},"geometry":{"coordinates":[-9.313,38.7692]}}]}
            """, 38.7692, -9.3103);
        assertEquals("Cacém", detailed.city());
        assertEquals("Perto de Rua do Campo", detailed.address());
    }

    @Test void unknownPhotonStreetDoesNotInventAnAddress() throws Exception {
        var noStreet = service.parsePhoton("""
            {"features":[{"properties":{"city":"Cacém","type":"city"},
              "geometry":{"coordinates":[-9.31,38.77]}}]}
            """, 38.7692,-9.3103);
        assertEquals("Cacém", noStreet.city());
        assertNull(noStreet.address());
    }

    @Test void rejectsInvalidCoordinatesBeforeSendingToProvider() {
        assertThrows(ResponseStatusException.class, ()->service.reverse(91d,0d,10d));
        assertThrows(ResponseStatusException.class, ()->service.reverse(0d,181d,10d));
        assertThrows(ResponseStatusException.class, ()->service.reverse(Double.NaN,0d,10d));
        assertThrows(ResponseStatusException.class, ()->service.reverse(10d,10d,-1d));
    }
}
