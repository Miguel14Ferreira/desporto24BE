package com.example.desporto24.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContinentalDistrictsTest {
    @Test
    void includesEighteenDistrictsAndCanonicalizesAccentsAndCase() {
        assertEquals(18, ContinentalDistricts.NAMES.size());
        assertEquals("Évora", ContinentalDistricts.normalizeOptional("  EVORA "));
        assertEquals("Bragança", ContinentalDistricts.normalizeOptional("braganca"));
        assertEquals("setúbal", ContinentalDistricts.normalizeFilter("SETUBAL"));
        assertNull(ContinentalDistricts.normalizeOptional(null));
        assertEquals("", ContinentalDistricts.normalizeFilter(""));
    }

    @Test
    void rejectsUnknownAndIslandDistricts() {
        assertThrows(IllegalArgumentException.class, () -> ContinentalDistricts.normalizeFilter("Funchal"));
        assertThrows(IllegalArgumentException.class, () -> ContinentalDistricts.normalizeFilter("Porto Santo"));
        assertThrows(IllegalArgumentException.class, () -> ContinentalDistricts.normalizeFilter("Badajoz"));
    }
}
