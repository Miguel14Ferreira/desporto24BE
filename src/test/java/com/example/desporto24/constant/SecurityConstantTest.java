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
    void exposesVenueReadsWithoutOpeningVenueWrites() {
        var publicGets = Arrays.asList(SecurityConstant.PUBLIC_GET_URLS);
        assertTrue(publicGets.contains("/api/venues"));
        assertTrue(publicGets.contains("/api/venues/*"));
        assertFalse(Arrays.asList(SecurityConstant.PUBLIC_URLS).contains("/api/venues/**"));
    }

    @Test
    void exposesOnlyTheDiscoveryCollectionAsPublicSessionGet() {
        assertTrue(Arrays.asList(SecurityConstant.PUBLIC_GET_URLS).contains("/api/sessions"));
        assertFalse(Arrays.asList(SecurityConstant.PUBLIC_GET_URLS).contains("/api/sessions/**"));
    }
}
