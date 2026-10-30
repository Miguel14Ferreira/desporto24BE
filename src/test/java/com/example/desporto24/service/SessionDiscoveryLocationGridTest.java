package com.example.desporto24.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionDiscoveryLocationGridTest {

    @Test
    void twoDifferentMeetingPlacesInOneCellHaveIdenticalPublicCoordinates() {
        // Roughly 4 km apart, in the same coarse public-search cell.
        var one = SessionDiscoveryLocationGrid.approximate(38.721, -9.280);
        var other = SessionDiscoveryLocationGrid.approximate(38.749, -9.230);

        assertEquals(one, other);
        SessionDistanceCalculator distance = new SessionDistanceCalculator();
        assertTrue(distance.kilometres(38.721, -9.280, 38.749, -9.230) > 3);

        for (double[] probe : new double[][] {
                {38.700, -9.220}, {38.800, -9.360},
                {38.740, -9.250}, {38.900, -9.100}}) {
            assertEquals(
                    distance.kilometres(probe[0], probe[1], one.latitude(), one.longitude()),
                    distance.kilometres(probe[0], probe[1], other.latitude(), other.longitude()));
        }
    }

    @Test
    void gridRevealsOnlyAnApproximateSearchAreaNotStoredExactCoordinates() {
        var coarse = SessionDiscoveryLocationGrid.approximate(38.73654, -9.25672);
        SessionDistanceCalculator distance = new SessionDistanceCalculator();
        double offset = distance.kilometres(38.73654, -9.25672,
                coarse.latitude(), coarse.longitude());

        assertTrue(offset < 5, "The meeting place remains within an approximate 5 km cell radius");
        assertNotEquals(38.73654, coarse.latitude());
        assertNotEquals(-9.25672, coarse.longitude());
    }

    @Test
    void invalidStoredLocationsAreRejectedInsteadOfPubliclyTriangulated() {
        assertThrows(IllegalArgumentException.class,
                () -> SessionDiscoveryLocationGrid.approximate(Double.NaN, -9));
        assertThrows(IllegalArgumentException.class,
                () -> SessionDiscoveryLocationGrid.approximate(91, -9));
        assertThrows(IllegalArgumentException.class,
                () -> SessionDiscoveryLocationGrid.approximate(38.7, Double.POSITIVE_INFINITY));
    }
}
