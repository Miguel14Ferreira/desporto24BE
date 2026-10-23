package com.example.desporto24.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityFieldDisciplinesTest {
    @Test void jumpsRequireOneOfTheJumpDisciplines() {
        assertEquals("LONG_JUMP", CommunityFieldDisciplines.validate(
                "Atletismo", "JUMP", "LONG_JUMP"));
        assertEquals("HIGH_JUMP", CommunityFieldDisciplines.validate(
                "Atletismo", "JUMP", "HIGH_JUMP"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Atletismo", "JUMP", null));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Atletismo", "JUMP", "JAVELIN"));
    }

    @Test void throwsRequireOneOfTheThrowDisciplines() {
        for (String code : new String[]{"SHOT_PUT", "DISCUS", "JAVELIN", "HAMMER"}) {
            assertEquals(code, CommunityFieldDisciplines.validate("Atletismo", "THROW", code));
        }
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Atletismo", "THROW", "POLE_VAULT"));
    }

    @Test void racesAndOtherSportsRejectUnexpectedDiscipline() {
        assertNull(CommunityFieldDisciplines.validate("Atletismo", "TRACK", null));
        assertNull(CommunityFieldDisciplines.validate("BMX", "FREESTYLE", null));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Atletismo", "TRACK", "LONG_JUMP"));
        assertThrows(IllegalArgumentException.class, () ->
                CommunityFieldDisciplines.validate("Futebol", null, "LONG_JUMP"));
    }
}
