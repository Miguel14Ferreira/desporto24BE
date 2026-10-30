package com.example.desporto24.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContinentalDistrictsTest {
    @Test
    void includesEighteenMainlandDistrictsAndBothAutonomousRegions() {
        assertEquals(20, ContinentalDistricts.NAMES.size());
        assertEquals("Açores", ContinentalDistricts.normalizeOptional("ACOReS"));
        assertEquals("Madeira", ContinentalDistricts.normalizeFilter(" MADEIRA "));
        assertTrue(ContinentalDistricts.NAMES.contains("Açores"));
        assertTrue(ContinentalDistricts.NAMES.contains("Madeira"));
        assertEquals("Évora", ContinentalDistricts.normalizeOptional("  EVORA "));
        assertEquals("Bragança", ContinentalDistricts.normalizeOptional("braganca"));
        assertEquals("Setúbal", ContinentalDistricts.normalizeFilter("SETUBAL"));
        assertNull(ContinentalDistricts.normalizeOptional(null));
        assertEquals("", ContinentalDistricts.normalizeFilter(""));
    }

    @Test
    void rejectsUnknownCityNamesWithoutBroadeningTheRegionWhitelist() {
        assertThrows(IllegalArgumentException.class, () -> ContinentalDistricts.normalizeFilter("Funchal"));
        assertThrows(IllegalArgumentException.class, () -> ContinentalDistricts.normalizeFilter("Porto Santo"));
        assertThrows(IllegalArgumentException.class, () -> ContinentalDistricts.normalizeFilter("Badajoz"));
    }
}
