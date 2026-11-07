package com.example.desporto24.constant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SportsCatalogAirsoftTest {
    @Test void airsoftIsSelectableAloneAndWithOtherSports() {
        assertTrue(SportsCatalog.ALLOWED.contains("Airsoft"));
        assertTrue(SportsCatalog.isValidSelection("Airsoft"));
        assertTrue(SportsCatalog.isValidSelection("Airsoft,Padel"));
    }

    @Test void unknownSportsRemainRejected() {
        assertFalse(SportsCatalog.isValidSelection("Airsoft,ModalidadeInventada"));
    }
}
