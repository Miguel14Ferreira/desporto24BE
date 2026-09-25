package com.example.desporto24.constant;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConstantTest {

    @Test
    void doesNotPermitEveryEndpointAnonymously() {
        assertFalse(Arrays.asList(SecurityConstant.PUBLIC_URLS).contains("/**"));
    }
}
