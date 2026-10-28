package com.example.desporto24.controllers;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.session.SessionPreset;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.session.SessionPresetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SessionPresetControllerTest {
    private SessionPresetController controller;
    private SessionPresetRepository presets;
    private ProfileRepository profiles;
    private Authentication actor;
    private Profile owner;

    private SessionPresetController.Settings settings() {
        return new SessionPresetController.Settings(
                "Futsal", "INDIVIDUAL", "Lisboa", "Rua da Bola", 10,
                "START", 1, true, BigDecimal.valueOf(2.50), true,
                List.of(), null, false, 17L, "venue",
                null, null, null, "NONE", 4, 1, "SCHEDULED");
    }

    @BeforeEach
    void setup() {
        presets = mock(SessionPresetRepository.class);
        profiles = mock(ProfileRepository.class);
        actor = mock(Authentication.class);
        owner = new Profile();
        owner.setId(42L);
        owner.setUsername("player-one");
        when(actor.isAuthenticated()).thenReturn(true);
        when(actor.getName()).thenReturn("player-one");
        when(profiles.findUserByUsernameIgnoreCase("player-one")).thenReturn(Optional.of(owner));
        controller = new SessionPresetController(presets, profiles, new ObjectMapper());
    }

    @Test
    void savesOnlyAllowlistedReusableSettingsAndDoesNotCreateASession() {
        when(presets.saveAndFlush(any())).thenAnswer(inv -> {
            SessionPreset p = inv.getArgument(0);
            p.setId(55L);
            p.setCreatedAt(Instant.now());
            p.setUpdatedAt(p.getCreatedAt());
            return p;
        });
        var result = controller.create(new SessionPresetController.SaveRequest("Futsal semanal", settings()), actor);
        assertEquals(55L, result.id());
        assertEquals("Futsal semanal", result.name());
        assertEquals(17L, result.settings().selectedVenueId());
        assertEquals("SCHEDULED", result.settings().registrationOpeningMode());
        var captured = org.mockito.ArgumentCaptor.forClass(SessionPreset.class);
        verify(presets).saveAndFlush(captured.capture());
        assertEquals(42L, captured.getValue().getOwner().getId());
        assertFalse(captured.getValue().getSettingsJson().contains("password"));
        assertFalse(captured.getValue().getSettingsJson().contains("gameDate"));
        assertFalse(captured.getValue().getSettingsJson().contains("registrationOpensAt"));
    }

    @Test
    void deniesOtherUsersPresetIdEvenWhenTheyKnowIt() {
        when(presets.findByIdAndOwner_Id(99L, 42L)).thenReturn(Optional.empty());
        var save = new SessionPresetController.SaveRequest("Outro nome", settings());
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> controller.update(99L, save, actor)).getStatusCode().value());
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> controller.delete(99L, actor)).getStatusCode().value());
        verify(presets, never()).saveAndFlush(any());
        verify(presets, never()).delete(any());
    }

    @Test
    void refusesInvalidSizesAndLimitsNumberOfPresets() {
        var invalid = new SessionPresetController.Settings(
                "Futsal", "INDIVIDUAL", "Lisboa", "Rua da Bola", 101,
                "START", 1, false, BigDecimal.ZERO, false,
                List.of(), null, false, null, "venue",
                null, null, null, "NONE", 4, 1, "IMMEDIATE");
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> controller.create(new SessionPresetController.SaveRequest("Teste", invalid), actor))
                .getStatusCode().value());
        when(presets.countByOwner_Id(42L)).thenReturn(20L);
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> controller.create(new SessionPresetController.SaveRequest("Teste", settings()), actor))
                .getStatusCode().value());
    }

    @Test
    void anonymousCallCannotReadOrModifyPrivatePresets() {
        assertEquals(401, assertThrows(ResponseStatusException.class,
                () -> controller.mine(null)).getStatusCode().value());
        verifyNoInteractions(presets);
    }
}
