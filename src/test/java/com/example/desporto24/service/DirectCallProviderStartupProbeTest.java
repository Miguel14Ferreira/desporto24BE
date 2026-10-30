package com.example.desporto24.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Lazy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectCallProviderStartupProbeTest {
    @Test
    void startupProbeIsEagerEvenWhenTheRestOfTheAppIsLazy() {
        Lazy lazy = DirectCallProviderStartupProbe.class.getAnnotation(Lazy.class);
        assertNotNull(lazy);
        assertFalse(lazy.value());
        @SuppressWarnings("unchecked")
        ObjectProvider<DirectCallService> provider = mock(ObjectProvider.class);
        DirectCallService calls = mock(DirectCallService.class);
        when(provider.getObject()).thenReturn(calls);
        when(calls.providerStatus()).thenReturn("ready");
        new DirectCallProviderStartupProbe(provider).verifyAtReady();
        verify(calls).providerStatus();
    }
}
