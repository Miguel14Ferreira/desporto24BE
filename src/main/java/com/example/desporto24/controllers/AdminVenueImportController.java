package com.example.desporto24.controllers;

import com.example.desporto24.configuration.SnidVenueImportData;
import com.example.desporto24.model.dto.venue.OpenStreetMapVenueImportRequest;
import com.example.desporto24.model.dto.venue.VenueResponse;
import com.example.desporto24.model.venue.Venue;
import com.example.desporto24.repository.venue.VenueRepository;
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
    private final VenueRepository venueRepository;
    private final SnidVenueImportData snidVenueImportData;
    private final TaskExecutor taskExecutor;
    private final AtomicBoolean snidImportRunning = new AtomicBoolean(false);
    private volatile String snidImportStatus = "NOT_STARTED";
    private volatile Instant snidImportStartedAt;
    private volatile Instant snidImportFinishedAt;
    private volatile SnidVenueImportData.ImportResult snidImportResult;

    @GetMapping("/imports/snid/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Map<String, Object> snidImportStatus() {
        Map<String, Object> result = new java.util.LinkedHashMap<>();
        result.put("status", snidImportStatus);
        result.put("running", snidImportRunning.get());
        result.put("startedAt", snidImportStartedAt);
        result.put("finishedAt", snidImportFinishedAt);
        result.put("processed", snidImportResult == null ? null : snidImportResult.processed());
        result.put("inserted", snidImportResult == null ? null : snidImportResult.inserted());
        result.put("skipped", snidImportResult == null ? null : snidImportResult.skipped());
        return result;
    }

    @GetMapping("/imports/snid/validate")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Map<String, Object> validateSnidSource() {
        return snidVenueImportData.validateSource();
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
        snidImportResult = null;
        try {
            taskExecutor.execute(() -> {
                try {
                    snidImportResult = snidVenueImportData.importVenues();
                    snidImportStatus = "FINISHED";
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

    @PostMapping("/imports/openstreetmap/preview")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Map<String, Object>> previewOpenStreetMapVenues(
            @RequestBody @Valid List<OpenStreetMapVenueImportRequest> requests) {
        if (requests.size() > 25) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum 25 venues per preview");
        }
        return requests.stream().map(request -> {
            Venue exactSource = venueRepository.findByInformationSourceAndSourceReference(
                    Venue.InformationSource.OPENSTREETMAP, request.sourceReference()).orElse(null);
            List<Venue> candidates = venueRepository
                    .findByCityContainingIgnoreCaseOrderByNameAsc(request.city()).stream()
                    .filter(venue -> {
                        boolean sameName = venue.getName().trim()
                                .equalsIgnoreCase(request.name().trim());
                        boolean near = request.latitude() != null && request.longitude() != null
                                && venue.getLatitude() != null && venue.getLongitude() != null
                                && Math.abs(venue.getLatitude() - request.latitude()) <= 0.0025
                                && Math.abs(venue.getLongitude() - request.longitude()) <= 0.0025;
                        return sameName || near;
                    })
                    .limit(10)
                    .toList();
            Map<String, Object> result = new java.util.LinkedHashMap<>();
            result.put("sourceReference", request.sourceReference());
            result.put("name", request.name());
            result.put("status", exactSource != null ? "EXISTING_SOURCE"
                    : candidates.isEmpty() ? "NO_CANDIDATE" : "REVIEW_CANDIDATES");
            result.put("candidates", candidates.stream().map(venue -> Map.of(
                    "id", venue.getId(),
                    "name", venue.getName(),
                    "city", venue.getCity()
            )).toList());
            return result;
        }).toList();
    }

    @PostMapping("/imports/openstreetmap")
    public List<VenueResponse> importOpenStreetMapVenues(
            @RequestBody @Valid List<OpenStreetMapVenueImportRequest> requests) {
        return venueService.importOpenStreetMapVenues(requests);
    }
}
