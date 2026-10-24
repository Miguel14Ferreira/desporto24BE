package com.example.desporto24.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SnidSourceValidationOnReady {

    private final SnidVenueImportData snidVenueImportData;
    private final TaskExecutor taskExecutor;

    // Stories scheduling creates a second TaskExecutor bean named taskScheduler.
    // Keep the existing asynchronous SNID validation on its original executor.
    public SnidSourceValidationOnReady(
            SnidVenueImportData snidVenueImportData,
            @Qualifier("applicationTaskExecutor") TaskExecutor taskExecutor) {
        this.snidVenueImportData = snidVenueImportData;
        this.taskExecutor = taskExecutor;
    }

    @Value("${desporto.venues.snid.validate-on-ready:false}")
    private boolean enabled;

    @EventListener(ApplicationReadyEvent.class)
    public void validateAfterStartup() {
        if (!enabled) return;
        taskExecutor.execute(() -> {
            try {
                log.info("SNID read-only source validation started");
                log.info("SNID read-only source validation result: {}", snidVenueImportData.validateSource());
            } catch (Exception ex) {
                log.warn("SNID read-only source validation failed: {}", ex.toString());
            }
        });
    }
}
