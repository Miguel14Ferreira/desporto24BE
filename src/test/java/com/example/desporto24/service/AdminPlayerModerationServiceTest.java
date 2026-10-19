package com.example.desporto24.service;

import com.example.desporto24.model.AdminPlayerModerationAudit;
import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.AdminPlayerModerationAuditRepository;
import com.example.desporto24.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminPlayerModerationServiceTest {
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final AdminPlayerModerationAuditRepository audits = mock(AdminPlayerModerationAuditRepository.class);
    private final AccountDeletionService deletion = mock(AccountDeletionService.class);
    private final AdminPlayerModerationService service = new AdminPlayerModerationService(profiles, audits, deletion);
    private Profile actor, target;

    @BeforeEach void setup() {
        actor = profile(1L, "admin", "ROLE_ADMIN");
        target = profile(2L, "ana", "ROLE_USER");
        target.setTokenVersion(4);
        when(profiles.findUserByUsername("admin")).thenReturn(Optional.of(actor));
        when(profiles.findForRoleChange(1L)).thenReturn(Optional.of(actor));
        when(profiles.findForRoleChange(2L)).thenReturn(Optional.of(target));
    }

    @Test void suspendsRevokesTokensAndAuditsReason() {
        var result = service.moderate("admin", 2L, request("SUSPEND", false));
        assertFalse(result.notLocked());
        assertNotNull(target.getModerationSuspendedAt());
        assertFalse(target.getNotLocked());
        assertEquals(5, target.getTokenVersion());
        var record = org.mockito.ArgumentCaptor.forClass(AdminPlayerModerationAudit.class);
        verify(audits).save(record.capture());
        assertEquals("SUSPEND", record.getValue().getAction());
        assertEquals(1L, record.getValue().getActorId());
        assertEquals("Inappropriate messages", record.getValue().getReason());
    }

    @Test void reinstatementRequiresRecordedSuspension() {
        failWith(409, () -> service.moderate("admin", 2L, request("REINSTATE", false)));
        target.setNotLocked(false);
        target.setModerationSuspendedAt(Instant.now());
        var result = service.moderate("admin", 2L, request("REINSTATE", true));
        assertTrue(result.notLocked());
        assertNull(target.getModerationSuspendedAt());
        assertEquals(5, target.getTokenVersion());
    }

    @Test void onlySuperAdminCanDeleteUsingExistingDeletionService() {
        failWith(403, () -> service.moderate("admin", 2L, request("DELETE", false)));
        actor.setRole("ROLE_SUPER_ADMIN");
        service.moderate("admin", 2L, request("DELETE", false));
        verify(deletion).deleteMyAccount("ana");
        verify(audits).save(any(AdminPlayerModerationAudit.class));
    }

    @Test void protectsOtherAdminsAndSelf() {
        target.setRole("ROLE_ADMIN");
        failWith(409, () -> service.moderate("admin", 2L, request("SUSPEND", false)));
        failWith(403, () -> service.moderate("admin", 1L,
                new AdminPlayerModerationService.ModerationRequest("SUSPEND", "admin", false, "Inappropriate messages")));
    }

    @Test void rejectsStaleSearchAndMissingReason() {
        failWith(409, () -> service.moderate("admin", 2L, request("SUSPEND", true)));
        failWith(400, () -> service.moderate("admin", 2L,
                new AdminPlayerModerationService.ModerationRequest("SUSPEND", "ana", false, "short")));
        verifyNoInteractions(audits);
    }

    private AdminPlayerModerationService.ModerationRequest request(String action, boolean suspended) {
        return new AdminPlayerModerationService.ModerationRequest(action, "ana", suspended, "Inappropriate messages");
    }
    private Profile profile(Long id, String username, String role) {
        Profile p = new Profile();
        p.setId(id); p.setUsername(username); p.setEmail(username + "@example.com");
        p.setRole(role); p.setEnabled(true); p.setNotLocked(true);
        return p;
    }
    private void failWith(int status, Runnable callable) {
        assertEquals(status, assertThrows(ResponseStatusException.class, callable::run).getStatusCode().value());
    }
}
