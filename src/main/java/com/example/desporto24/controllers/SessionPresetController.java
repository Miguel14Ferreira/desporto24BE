package com.example.desporto24.controllers;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.session.SessionPreset;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.session.SessionPresetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * User-owned reusable session configurations. A preset is NOT a session:
 * no notifications, calendar slots or player invitations are created here.
 * Neither passwords nor dates/times are part of the persisted settings contract.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions/presets")
public class SessionPresetController {
    private static final int MAX_PRESETS = 20;
    private final SessionPresetRepository presets;
    private final ProfileRepository profiles;
    // Spring Boot 4 configures Jackson 3 by default; avoid requesting an absent Jackson 2 bean.
    private final ObjectMapper mapper = new ObjectMapper();

    /** Explicit allowlist; Jackson ignores any unexpected client fields (including passwords and dates). */
    public record Settings(
            String sport, String playFormat, String city, String address,
            Integer maxParticipants, String creationMode, Integer confirmedPlayers,
            Boolean hasPrice, BigDecimal priceAmount, Boolean privateGame,
            List<Long> communityShareIds, Long communityId, Boolean seasonGame,
            Long selectedVenueId, String locationMethod, Double latitude, Double longitude,
            String venueMapLink, String recurrence, Integer recurrenceOccurrences,
            Integer weeklySessionsPerWeek, String registrationOpeningMode
    ) {}

    public record SaveRequest(@NotBlank @Size(max = 70) String name,
                              @NotNull @Valid Settings settings) {}
    public record PresetResponse(Long id, String name, Settings settings,
                                 Instant createdAt, Instant updatedAt) {}

    private Profile requireActor(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()
                || auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return profiles.findUserByUsernameIgnoreCase(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    private void validate(Settings s) {
        if (s == null || s.sport() == null || s.sport().isBlank() || s.sport().length() > 100
                || s.city() != null && s.city().length() > 120
                || s.address() != null && s.address().length() > 350
                || s.maxParticipants() == null || s.maxParticipants() < 2 || s.maxParticipants() > 100
                || s.confirmedPlayers() != null && (s.confirmedPlayers() < 1 || s.confirmedPlayers() > s.maxParticipants())
                || s.priceAmount() != null && (s.priceAmount().signum() < 0 || s.priceAmount().compareTo(new BigDecimal("100000")) > 0)
                || s.communityShareIds() != null && (s.communityShareIds().size() > 5
                  || s.communityShareIds().stream().anyMatch(id -> id == null || id <= 0))
                || s.communityId() != null && s.communityId() <= 0
                || s.selectedVenueId() != null && s.selectedVenueId() <= 0
                || s.latitude() != null && (!Double.isFinite(s.latitude()) || Math.abs(s.latitude()) > 90)
                || s.longitude() != null && (!Double.isFinite(s.longitude()) || Math.abs(s.longitude()) > 180)
                || (s.latitude() == null) != (s.longitude() == null)
                || s.venueMapLink() != null && s.venueMapLink().length() > 1200
                || s.recurrenceOccurrences() != null && (s.recurrenceOccurrences() < 2 || s.recurrenceOccurrences() > 52)
                || s.weeklySessionsPerWeek() != null && (s.weeklySessionsPerWeek() < 1 || s.weeklySessionsPerWeek() > 7)
                || !oneOf(s.creationMode(), "START", "COMPLETE")
                || !oneOf(s.playFormat(), "INDIVIDUAL", "PAIRS")
                || !oneOf(s.locationMethod(), "venue", "maps", "gps")
                || !oneOf(s.recurrence(), "NONE", "WEEKLY", "BIWEEKLY")
                || !oneOf(s.registrationOpeningMode(), "IMMEDIATE", "SCHEDULED", "MANUAL")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Configuração de sessão inválida");
        }
    }

    private static boolean oneOf(String value, String... values) {
        if (value == null) return true;
        return Set.of(values).contains(value);
    }

    private String serialize(Settings settings) {
        try { return mapper.writeValueAsString(settings); }
        catch (JsonProcessingException e) { throw new IllegalStateException("Preset serialization", e); }
    }

    private PresetResponse response(SessionPreset p) {
        try {
            return new PresetResponse(p.getId(), p.getName(),
                    mapper.readValue(p.getSettingsJson(), Settings.class),
                    p.getCreatedAt(), p.getUpdatedAt());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid saved session preset", e);
        }
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<PresetResponse> mine(Authentication authentication) {
        Long ownerId = requireActor(authentication).getId();
        return presets.findAllByOwner_IdOrderByUpdatedAtDesc(ownerId).stream().map(this::response).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public PresetResponse create(@RequestBody @Valid SaveRequest request, Authentication authentication) {
        Profile owner = requireActor(authentication);
        validate(request.settings());
        if (presets.countByOwner_Id(owner.getId()) >= MAX_PRESETS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Limite de 20 perfis de sessões");
        }
        SessionPreset preset = new SessionPreset();
        preset.setOwner(owner);
        preset.setName(request.name().trim());
        preset.setSettingsJson(serialize(request.settings()));
        return response(presets.saveAndFlush(preset));
    }

    @PutMapping("/{id}")
    @Transactional
    public PresetResponse update(@PathVariable Long id, @RequestBody @Valid SaveRequest request,
                                 Authentication authentication) {
        SessionPreset preset = presets.findByIdAndOwner_Id(id, requireActor(authentication).getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        validate(request.settings());
        preset.setName(request.name().trim());
        preset.setSettingsJson(serialize(request.settings()));
        return response(presets.saveAndFlush(preset));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable Long id, Authentication authentication) {
        SessionPreset preset = presets.findByIdAndOwner_Id(id, requireActor(authentication).getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        presets.delete(preset);
    }
}
