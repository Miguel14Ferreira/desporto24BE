package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommunityCreationQuotaServiceTest {
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final CommunityRepository communities = mock(CommunityRepository.class);
    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
    private final CommunityCreationQuotaService quotas =
            new CommunityCreationQuotaService(profiles, communities, jdbc);
    private final Profile actor = new Profile();

    @BeforeEach
    void setup() {
        actor.setId(7L);
        actor.setUsername("fixture");
        when(profiles.lockForCommunityQuota(7L)).thenReturn(Optional.of(actor));
    }

    private void owned(long current) {
        when(communities.countByOwnerId(7L)).thenReturn(current);
    }

    @Test
    void creationAllowedAsLongAsFewerThanFiveAreOwned() {
        owned(4);
        var quota = quotas.quota(actor);
        assertEquals(5, quota.limit());
        assertEquals(4, quota.owned());
        assertTrue(quota.canCreate());
        assertDoesNotThrow(() -> quotas.reserveCreate(actor));
        verify(profiles).lockForCommunityQuota(7L);
        // Creation counts no longer have a daily threshold or insert events.
        verifyNoInteractions(jdbc);
    }

    @Test
    void fourthAndFifthCreationOnSameDayAreNotThrottled() {
        owned(3);
        quotas.reserveCreate(actor);
        owned(4);
        quotas.reserveCreate(actor);
        verify(profiles, times(2)).lockForCommunityQuota(7L);
        verifyNoInteractions(jdbc);
    }

    @Test
    void fullOwnerQuotaRejectsWithoutPersistingOrRecordingCreation() {
        owned(5);
        var error = assertThrows(ResponseStatusException.class,
                () -> quotas.reserveCreate(actor));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        assertFalse(quotas.quota(actor).canCreate());
        verifyNoInteractions(jdbc);
    }

    @Test
    void legacyOwnersAboveFiveKeepTheirGroupsButCannotCreateMore() {
        owned(8);
        assertEquals(8, quotas.quota(actor).owned());
        assertFalse(quotas.quota(actor).canCreate());
        assertThrows(ResponseStatusException.class, () -> quotas.reserveCreate(actor));
        verify(communities, never()).delete(any());
    }

    @Test
    void adminOverrideCanRaiseOwnerQuotaAndAllowsImmediateCreation() {
        actor.setCommunityCreationLimit(10);
        owned(5);
        assertEquals(10, quotas.quota(actor).limit());
        assertDoesNotThrow(() -> quotas.reserveCreate(actor));
        verifyNoInteractions(jdbc);
    }

    @Test
    void transferChecksOnlyTargetOwnerCapacity() {
        owned(5);
        assertEquals(HttpStatus.CONFLICT,
                assertThrows(ResponseStatusException.class,
                        () -> quotas.requireTransferCapacity(actor)).getStatusCode());
        reset(communities);
        owned(4);
        assertDoesNotThrow(() -> quotas.requireTransferCapacity(actor));
        verifyNoInteractions(jdbc);
    }

    @Test
    void superAdminOverrideIsPersistedAndAuditedWithReason() {
        owned(5);
        when(profiles.save(actor)).thenReturn(actor);
        var quota = quotas.updateOverride(7L, 19L, 8, "Clube com várias equipas");
        assertEquals(8, quota.limit());
        assertEquals(8, actor.getCommunityCreationLimit());
        verify(jdbc).update(contains("community_creation_limit_audit"),
                eq(19L), eq(7L), isNull(), eq(8),
                eq("Clube com várias equipas"), any(OffsetDateTime.class));
    }

    @Test
    void resetToDefaultRequiresReasonAndWritesAudit() {
        owned(5);
        actor.setCommunityCreationLimit(8);
        quotas.updateOverride(7L, 19L, null, "Reposição do limite padrão");
        assertNull(actor.getCommunityCreationLimit());
        verify(jdbc).update(contains("community_creation_limit_audit"),
                eq(19L), eq(7L), eq(8), isNull(),
                eq("Reposição do limite padrão"), any(OffsetDateTime.class));
    }

    @Test
    void invalidOverrideAndMissingReasonCannotMutateTarget() {
        assertEquals(HttpStatus.BAD_REQUEST,
                assertThrows(ResponseStatusException.class,
                        () -> quotas.updateOverride(7L, 19L, 101, "reason")).getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST,
                assertThrows(ResponseStatusException.class,
                        () -> quotas.updateOverride(7L, 19L, 8, " ")).getStatusCode());
        verify(profiles, never()).save(any());
        verifyNoInteractions(jdbc);
    }
}
