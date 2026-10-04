package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.AdminRoleAudit;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.AdminRoleAuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminRoleServiceTest {
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final AdminRoleAuditRepository audits = mock(AdminRoleAuditRepository.class);
    private final AdminRoleService service = new AdminRoleService(profiles, audits);
    private Profile actor;
    private Profile target;

    @BeforeEach void prepare() {
        actor = profile(1L, "owner", "ROLE_SUPER_ADMIN");
        target = profile(2L, "ana", "ROLE_USER");
        target.setTokenVersion(7);
        when(profiles.findUserByUsername("owner")).thenReturn(Optional.of(actor));
        when(profiles.findForRoleChange(1L)).thenReturn(Optional.of(actor));
        when(profiles.findForRoleChange(2L)).thenReturn(Optional.of(target));
    }

    @Test void promotionUpdatesPermissionsInvalidatesTokensAndRecordsActor() {
        var result = service.changeRole("owner", 2L, "ROLE_ADMIN", "ROLE_USER");
        assertEquals("ROLE_ADMIN", result.role());
        assertEquals("ROLE_ADMIN", target.getRole());
        assertArrayEquals(com.example.desporto24.enumeration.Role.ROLE_ADMIN.getAuthorities(), target.getAuthorities());
        assertEquals(8, target.getTokenVersion());
        var captured = org.mockito.ArgumentCaptor.forClass(AdminRoleAudit.class);
        verify(audits).save(captured.capture());
        assertEquals(1L, captured.getValue().getActorId());
        assertEquals(2L, captured.getValue().getTargetId());
        assertEquals("ROLE_USER", captured.getValue().getPreviousRole());
        assertEquals("ROLE_ADMIN", captured.getValue().getNewRole());
        assertNotNull(captured.getValue().getOccurredAt());
    }

    @Test void ordinaryAdminCannotChangeRoles() {
        actor.setRole("ROLE_ADMIN");
        assertEquals(403, assertThrows(ResponseStatusException.class,
                () -> service.changeRole("owner", 2L, "ROLE_ADMIN", "ROLE_USER")).getStatusCode().value());
        assertEquals("ROLE_USER", target.getRole());
        verifyNoInteractions(audits);
    }

    @Test void superAdministratorCannotBeDemoted() {
        target.setRole("ROLE_SUPER_ADMIN");
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.changeRole("owner", 2L, "ROLE_USER", "ROLE_SUPER_ADMIN")).getStatusCode().value());
        verifyNoInteractions(audits);
    }

    @Test void staleRoleRequiresRefreshInsteadOfOverwritingAnotherChange() {
        target.setRole("ROLE_ADMIN");
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.changeRole("owner", 2L, "ROLE_ADMIN", "ROLE_USER")).getStatusCode().value());
        verifyNoInteractions(audits);
    }

    @Test void demotionInvalidatesExistingAdminToken() {
        target.setRole("ROLE_ADMIN");
        service.changeRole("owner", 2L, "ROLE_USER", "ROLE_ADMIN");
        assertEquals("ROLE_USER", target.getRole());
        assertEquals(8, target.getTokenVersion());
    }

    @Test void deletedAndInactiveAccountsCannotBePromoted() {
        target.setEmail("deleted-2@deleted.invalid");
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.changeRole("owner", 2L, "ROLE_ADMIN", "ROLE_USER")).getStatusCode().value());
        target.setEmail("ana@example.com");
        target.setEnabled(false);
        assertEquals(409, assertThrows(ResponseStatusException.class,
                () -> service.changeRole("owner", 2L, "ROLE_ADMIN", "ROLE_USER")).getStatusCode().value());
        verifyNoInteractions(audits);
    }

    private Profile profile(Long id, String username, String role) {
        Profile p = new Profile();
        p.setId(id); p.setUsername(username); p.setRole(role);
        p.setEnabled(true); p.setNotLocked(true);
        return p;
    }
}
