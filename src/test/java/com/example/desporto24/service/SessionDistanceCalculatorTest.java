package com.example.desporto24.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SessionDistanceCalculatorTest {

    private final SessionDistanceCalculator calculator = new SessionDistanceCalculator();

    @Test
    void calculatesZeroForSamePoint() {
        assertEquals(0.0, calculator.kilometres(1.0, 1.0, 1.0, 1.0), 0.001);
    }
}
