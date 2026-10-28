package com.example.desporto24.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Production uses spring.main.lazy-initialization=true. A listener on the lazy
 * calling service alone is not guaranteed to execute until the first call.
 * Keep this tiny observer eager, and instantiate the service only AFTER_READY,
 * so the server can expose its HTTP health endpoint first.
 */
@Component
@Lazy(false)
@RequiredArgsConstructor
public class DirectCallProviderStartupProbe {
    private static final Logger log = LoggerFactory.getLogger(DirectCallProviderStartupProbe.class);
    private final ObjectProvider<DirectCallService> calling;

    @EventListener(ApplicationReadyEvent.class)
    public void verifyAtReady() {
        // Do not print the URL, key, secret, JWT, room or user information.
        log.info("Jynlo LiveKit provider status: {}", calling.getObject().providerStatus());
    }
}
