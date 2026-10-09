package com.example.desporto24.controllers;

import com.example.desporto24.configuration.SnidVenueImportData;
import com.example.desporto24.model.dto.venue.OpenStreetMapVenueImportRequest;
import com.example.desporto24.model.dto.venue.VenueResponse;
import com.example.desporto24.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/venues")
public class AdminVenueImportController {

    private final VenueService venueService;
    private final SnidVenueImportData snidVenueImportData;
    private final TaskExecutor taskExecutor;
    private final AtomicBoolean snidImportRunning = new AtomicBoolean(false);
    private volatile String snidImportStatus = "NOT_STARTED";
    private volatile Instant snidImportStartedAt;
    private volatile Instant snidImportFinishedAt;

    @GetMapping("/imports/snid/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Map<String, Object> snidImportStatus() {
        Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("status", snidImportStatus);
        result.put("running", snidImportRunning.get());
        result.put("startedAt", snidImportStartedAt);
        result.put("finishedAt", snidImportFinishedAt);
        return result;
    }

    @PostMapping("/imports/snid")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.ACCEPTED)
    public void importSnidVenues() {
        if (!snidImportRunning.compareAndSet(false, true)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "SNID import already running");
        }
        snidImportStatus = "RUNNING";
        snidImportStartedAt = Instant.now();
        snidImportFinishedAt = null;
        try {
            taskExecutor.execute(() -> {
                try {
                    snidVenueImportData.importVenues();
                    snidImportStatus = "FINISHED_CHECK_LOGS";
                } catch (Exception ex) {
                    snidImportStatus = "FAILED";
                    throw ex;
                } finally {
                    snidImportFinishedAt = Instant.now();
                    snidImportRunning.set(false);
                }
            });
        } catch (RuntimeException ex) {
            snidImportStatus = "FAILED";
            snidImportFinishedAt = Instant.now();
            snidImportRunning.set(false);
            throw ex;
        }
    }

    @PostMapping("/imports/openstreetmap")
    public List<VenueResponse> importOpenStreetMapVenues(
            @RequestBody @Valid List<OpenStreetMapVenueImportRequest> requests) {
        return venueService.importOpenStreetMapVenues(requests);
    }
}
