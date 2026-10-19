package com.example.desporto24.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SportsCatalogAdditionalSportsTest {

    @Test
    void acceptsSportsIdentifiedInCascaisVenueSourceData() {
        for (String sport : new String[] {
                "Bowling", "Corrida de obstáculos", "Hipismo", "Teqball", "Ténis de praia"
        }) {
            assertTrue(SportsCatalog.ALLOWED.contains(sport), sport);
            assertTrue(SportsCatalog.isValidSelection(sport), sport);
        }

        assertTrue(SportsCatalog.isValidSelection("Teqball,Hipismo"));
        assertFalse(SportsCatalog.isValidSelection("Modalidade inventada"));
    }
}
