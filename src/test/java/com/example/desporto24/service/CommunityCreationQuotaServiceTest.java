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

    private void counts(long active, long daily) {
        when(communities.countByOwnerId(7L)).thenReturn(active);
        when(jdbc.queryForObject(anyString(), eq(Long.class), eq(7L),
                any(OffsetDateTime.class), any(OffsetDateTime.class)))
                .thenReturn(daily);
    }

    @Test
    void defaultQuotaAllowsCreationUntilFiveOwned() {
        counts(4, 0);
        var q = quotas.quota(actor);
        assertEquals(5, q.limit());
        assertEquals(4, q.owned());
        assertTrue(q.canCreate());
        quotas.reserveCreate(actor);
        verify(jdbc).update(contains("community_creation_event"),
                eq(7L), any(OffsetDateTime.class));
    }

    @Test
    void fullOwnerQuotaRejectsWithoutRecordingAnotherCreation() {
        counts(5, 0);
        var error = assertThrows(ResponseStatusException.class,
                () -> quotas.reserveCreate(actor));
        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(jdbc, never()).update(anyString(), any(), any());
    }

    @Test
    void existingOwnersAboveFiveRetainCommunitiesButCannotCreateMore() {
        counts(8, 0);
        assertEquals(8, quotas.quota(actor).owned());
        assertFalse(quotas.quota(actor).canCreate());
        assertThrows(ResponseStatusException.class, () -> quotas.reserveCreate(actor));
        verify(communities, never()).delete(any());
    }

    @Test
    void twoNewGroupsPerDayCannotBeCircumventedByDeletion() {
        counts(0, 2);
        var error = assertThrows(ResponseStatusException.class,
                () -> quotas.reserveCreate(actor));
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, error.getStatusCode());
        verify(jdbc, never()).update(anyString(), any(), any());
    }

    @Test
    void increasingLimitAllowsOwnerWithFiveToCreate() {
        actor.setCommunityCreationLimit(10);
        counts(5, 1);
        assertEquals(10, quotas.quota(actor).limit());
        quotas.reserveCreate(actor);
        verify(jdbc).update(contains("community_creation_event"),
                eq(7L), any(OffsetDateTime.class));
    }

    @Test
    void transferChecksNewOwnerCapacityButDoesNotConsumeDailyAllowance() {
        counts(5, 0);
        assertEquals(HttpStatus.CONFLICT,
                assertThrows(ResponseStatusException.class,
                        () -> quotas.requireTransferCapacity(actor)).getStatusCode());
        reset(jdbc, communities);
        counts(4, 2);
        assertDoesNotThrow(() -> quotas.requireTransferCapacity(actor));
        verify(jdbc, never()).update(anyString(), any(), any());
    }

    @Test
    void superAdminOverrideIsPersistedAndAuditedWithReason() {
        counts(5, 0);
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
        counts(5, 0);
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
    }
}
