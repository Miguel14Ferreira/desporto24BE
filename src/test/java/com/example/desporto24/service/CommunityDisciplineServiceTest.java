package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityDisciplinaryWarning;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityDisciplinaryWarningRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommunityDisciplineServiceTest {
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final CommunityRepository communities = mock(CommunityRepository.class);
    private final CommunityMemberRepository members = mock(CommunityMemberRepository.class);
    private final CommunityDisciplinaryWarningRepository warnings =
            mock(CommunityDisciplinaryWarningRepository.class);
    private final CommunityDisciplineService service =
            new CommunityDisciplineService(profiles, communities, members, warnings);

    private final Profile actor = new Profile();

    @BeforeEach
    void setup() {
        actor.setId(10L);
        actor.setUsername("manager");
        when(profiles.findUserByUsername("manager")).thenReturn(Optional.of(actor));
        when(communities.existsById(7L)).thenReturn(true);
        membership(10L, CommunityRole.OWNER);
        membership(11L, CommunityRole.MEMBER);
    }

    private void membership(long id, CommunityRole role) {
        CommunityMember member = new CommunityMember();
        member.setRole(role);
        when(members.findByCommunityIdAndProfileId(7L, id)).thenReturn(Optional.of(member));
    }

    @Test
    void ownerCanIssueAuditableCommunityOnlyWarning() {
        when(warnings.save(any())).thenAnswer(invocation -> {
            CommunityDisciplinaryWarning warning = invocation.getArgument(0);
            warning.setId(55L);
            return warning;
        });
        var result = service.warn(7L, "manager", 11L, "  Desrespeito das regras  ");
        assertEquals(55L, result.id());
        assertEquals(7L, result.communityId());
        assertEquals(10L, result.issuedByProfileId());
        assertEquals(11L, result.targetProfileId());
        assertEquals("Desrespeito das regras", result.reason());
        assertNotNull(result.createdAt());
        verify(warnings).save(argThat(warning ->
                warning.getCommunityId() == 7L &&
                warning.getTargetProfileId() == 11L &&
                warning.getIssuedByProfileId() == 10L));
    }

    @Test
    void adminsCanWarnButOrdinaryMembersCannot() {
        membership(10L, CommunityRole.ADMIN);
        when(warnings.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        assertDoesNotThrow(() -> service.warn(7L, "manager", 11L, "Incidente no jogo"));
        membership(10L, CommunityRole.MEMBER);
        assertStatus(HttpStatus.FORBIDDEN, () -> service.warn(7L, "manager", 11L, "Incidente"));
        verify(warnings, times(1)).save(any());
    }

    @Test
    void managerCannotWarnOwnerAdminSelfOrOutsider() {
        membership(12L, CommunityRole.OWNER);
        membership(13L, CommunityRole.ADMIN);
        assertStatus(HttpStatus.FORBIDDEN, () -> service.warn(7L, "manager", 12L, "Problema"));
        assertStatus(HttpStatus.FORBIDDEN, () -> service.warn(7L, "manager", 13L, "Problema"));
        assertStatus(HttpStatus.BAD_REQUEST, () -> service.warn(7L, "manager", 10L, "Problema"));
        assertStatus(HttpStatus.NOT_FOUND, () -> service.warn(7L, "manager", 99L, "Problema"));
        verifyNoInteractions(warnings);
    }

    @Test
    void reasonIsMandatoryAndBounded() {
        for (String invalid : new String[] { "", "  ", "x".repeat(1001) }) {
            assertStatus(HttpStatus.BAD_REQUEST, () -> service.warn(7L, "manager", 11L, invalid));
        }
        assertStatus(HttpStatus.BAD_REQUEST, () -> service.warn(7L, "manager", 11L, null));
        verifyNoInteractions(warnings);
    }

    @Test
    void onlyManagersCanReadWholeCommunityHistory() {
        when(warnings.findTop100ByCommunityIdOrderByCreatedAtDescIdDesc(7L))
                .thenReturn(List.of());
        assertEquals(List.of(), service.listForManagers(7L, "manager"));
        membership(10L, CommunityRole.MEMBER);
        assertStatus(HttpStatus.FORBIDDEN, () -> service.listForManagers(7L, "manager"));
        verify(warnings, times(1)).findTop100ByCommunityIdOrderByCreatedAtDescIdDesc(7L);
    }

    @Test
    void ordinaryMemberCanReadOnlyTheirOwnWarnings() {
        membership(10L, CommunityRole.MEMBER);
        when(warnings.findTop100ByCommunityIdAndTargetProfileIdOrderByCreatedAtDescIdDesc(7L, 10L))
                .thenReturn(List.of());
        assertEquals(List.of(), service.mine(7L, "manager"));
        verify(warnings).findTop100ByCommunityIdAndTargetProfileIdOrderByCreatedAtDescIdDesc(7L, 10L);
        verify(warnings, never()).findTop100ByCommunityIdOrderByCreatedAtDescIdDesc(any());
    }

    @Test
    void unrelatedCommunitiesCannotBeQueried() {
        assertStatus(HttpStatus.NOT_FOUND, () -> service.mine(999L, "manager"));
        verifyNoInteractions(warnings);
    }

    private static void assertStatus(HttpStatus status, org.junit.jupiter.api.function.Executable action) {
        assertEquals(status, assertThrows(ResponseStatusException.class, action).getStatusCode());
    }
}
