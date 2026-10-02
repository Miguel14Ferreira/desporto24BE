package com.example.desporto24.constant;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConstantTest {

    @Test
    void doesNotPermitEveryEndpointAnonymously() {
        assertFalse(Arrays.asList(SecurityConstant.PUBLIC_URLS).contains("/**"));
    }

    @Test
    void doesNotExposeLegacyImagePathAnonymously() {
        assertFalse(Arrays.asList(SecurityConstant.PUBLIC_URLS).contains("/image/**"));
    }

    @Test
    void exposesOnlyTheDiscoveryCollectionAsPublicSessionGet() {
        assertTrue(Arrays.asList(SecurityConstant.PUBLIC_GET_URLS).contains("/api/sessions"));
        assertFalse(Arrays.asList(SecurityConstant.PUBLIC_GET_URLS).contains("/api/sessions/**"));
    }
}
