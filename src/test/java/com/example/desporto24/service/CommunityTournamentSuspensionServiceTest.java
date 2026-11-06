package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityTournamentSuspension;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTournamentSuspensionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommunityTournamentSuspensionServiceTest {
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final CommunityRepository communities = mock(CommunityRepository.class);
    private final CommunityMemberRepository members = mock(CommunityMemberRepository.class);
    private final CommunityTournamentSuspensionRepository suspensions =
            mock(CommunityTournamentSuspensionRepository.class);
    private final CommunityTournamentSuspensionService service =
            new CommunityTournamentSuspensionService(profiles, communities, members, suspensions);
    private final Profile manager = new Profile();

    @BeforeEach
    void init() {
        manager.setId(10L);
        manager.setUsername("manager");
        when(profiles.findUserByUsername("manager")).thenReturn(Optional.of(manager));
        when(communities.existsById(7L)).thenReturn(true);
        member(10L, CommunityRole.OWNER);
        member(11L, CommunityRole.MEMBER);
    }

    private void member(long id, CommunityRole role) {
        CommunityMember entry = new CommunityMember();
        entry.setRole(role);
        when(members.findByCommunityIdAndProfileId(7L, id)).thenReturn(Optional.of(entry));
    }

    @Test
    void ownerCanSuspendOrdinaryMemberForBoundedTime() {
        when(suspensions.save(any())).thenAnswer(call -> {
            CommunityTournamentSuspension entity = call.getArgument(0);
            entity.setId(41L);
            return entity;
        });
        var result = service.issue(7L, "manager", 11L, 3, "  Comportamento grave  ");
        assertEquals(41L, result.id());
        assertEquals(7L, result.communityId());
        assertEquals(11L, result.targetProfileId());
        assertEquals("Comportamento grave", result.reason());
        assertEquals(3, java.time.Duration.between(result.startsAt(), result.endsAt()).toDays());
        assertNull(result.revokedAt());
    }

    @Test
    void managersCannotSuspendOtherManagersOrThemselves() {
        member(12L, CommunityRole.ADMIN);
        member(13L, CommunityRole.OWNER);
        status(HttpStatus.FORBIDDEN, () -> service.issue(7L, "manager", 12L, 1, "Incidente"));
        status(HttpStatus.FORBIDDEN, () -> service.issue(7L, "manager", 13L, 1, "Incidente"));
        status(HttpStatus.BAD_REQUEST, () -> service.issue(7L, "manager", 10L, 1, "Incidente"));
        verify(suspensions, never()).save(any());
    }

    @Test
    void durationAndReasonAreStrictlyValidated() {
        status(HttpStatus.BAD_REQUEST, () -> service.issue(7L, "manager", 11L, 0, "Motivo"));
        status(HttpStatus.BAD_REQUEST, () -> service.issue(7L, "manager", 11L, 31, "Motivo"));
        status(HttpStatus.BAD_REQUEST, () -> service.issue(7L, "manager", 11L, 1, " "));
        status(HttpStatus.BAD_REQUEST, () -> service.issue(7L, "manager", 11L, 1, "x".repeat(1001)));
        verify(suspensions, never()).save(any());
    }

    @Test
    void cannotExtendActiveSuspensionsByStackingNewOnes() {
        when(suspensions.countActive(eq(7L), eq(11L), any(Instant.class))).thenReturn(1L);
        status(HttpStatus.CONFLICT, () -> service.issue(7L, "manager", 11L, 2, "Motivo"));
        verify(suspensions, never()).save(any());
    }

    @Test
    void eligibilityGateBlocksOnlyActiveScopedSuspension() {
        when(suspensions.countActive(eq(7L), eq(11L), any(Instant.class))).thenReturn(1L);
        status(HttpStatus.FORBIDDEN, () -> service.requireEligible(7L, 11L));
        assertDoesNotThrow(() -> service.requireEligible(8L, 11L));
        assertDoesNotThrow(() -> service.requireEligible(7L, 99L));
    }

    @Test
    void ownerCanLiftSuspensionWithRecordedReason() {
        var active = CommunityTournamentSuspension.builder()
                .id(41L).communityId(7L).targetProfileId(11L).issuedByProfileId(10L)
                .reason("Motivo").startsAt(Instant.now()).endsAt(Instant.now().plusSeconds(86400))
                .build();
        when(suspensions.findById(41L)).thenReturn(Optional.of(active));
        when(suspensions.save(any())).thenAnswer(call -> call.getArgument(0));
        var result = service.revoke(7L, 41L, "manager", "Revisão do incidente");
        assertNotNull(result.revokedAt());
        assertEquals(10L, result.revokedByProfileId());
        assertEquals("Revisão do incidente", result.revocationReason());
        status(HttpStatus.CONFLICT, () -> service.revoke(7L, 41L, "manager", "Outra decisão"));
    }

    @Test
    void memberCannotIssueOrSeePrivateCommunityHistory() {
        member(10L, CommunityRole.MEMBER);
        status(HttpStatus.FORBIDDEN, () -> service.issue(7L, "manager", 11L, 1, "Motivo"));
        status(HttpStatus.FORBIDDEN, () -> service.forManagers(7L, "manager"));
        assertEquals(List.of(), service.mine(7L, "manager"));
        verify(suspensions).findTop100ByCommunityIdAndTargetProfileIdOrderByStartsAtDescIdDesc(7L, 10L);
        verify(suspensions, never()).findTop100ByCommunityIdOrderByStartsAtDescIdDesc(any());
    }

    private static void status(HttpStatus expected, org.junit.jupiter.api.function.Executable action) {
        assertEquals(expected, assertThrows(ResponseStatusException.class, action).getStatusCode());
    }
}
