package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityDisciplinaryWarning;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityWarningAppeal;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityDisciplinaryWarningRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityWarningAppealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommunityWarningAppealServiceTest {
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final CommunityRepository communities = mock(CommunityRepository.class);
    private final CommunityMemberRepository members = mock(CommunityMemberRepository.class);
    private final CommunityDisciplinaryWarningRepository warnings = mock(CommunityDisciplinaryWarningRepository.class);
    private final CommunityWarningAppealRepository appeals = mock(CommunityWarningAppealRepository.class);
    private final CommunityWarningAppealService service =
            new CommunityWarningAppealService(profiles, communities, members, warnings, appeals);

    private final Profile player = new Profile();

    @BeforeEach
    void setup() {
        player.setId(11L);
        player.setUsername("player");
        when(profiles.findUserByUsername("player")).thenReturn(Optional.of(player));
        when(communities.existsById(7L)).thenReturn(true);
        membership(7L, 11L, CommunityRole.MEMBER);
        when(warnings.findById(42L)).thenReturn(Optional.of(CommunityDisciplinaryWarning.builder()
                .id(42L).communityId(7L).targetProfileId(11L).issuedByProfileId(10L)
                .reason("Incidente").createdAt(Instant.now()).build()));
    }

    private void membership(Long community, Long profile, CommunityRole role) {
        var member = new CommunityMember();
        member.setRole(role);
        when(members.findByCommunityIdAndProfileId(community, profile)).thenReturn(Optional.of(member));
    }

    @Test
    void playerCanSubmitOneImmutableStatementAboutOwnWarning() {
        when(appeals.saveAndFlush(any())).thenAnswer(call -> {
            CommunityWarningAppeal appeal = call.getArgument(0);
            appeal.setId(51L);
            return appeal;
        });
        var result = service.submit(7L, 42L, "player", "  A minha versão dos factos  ");
        assertEquals(51L, result.id());
        assertEquals(42L, result.warningId());
        assertEquals(11L, result.authorProfileId());
        assertEquals(7L, result.communityId());
        assertEquals("A minha versão dos factos", result.statement());
        assertNotNull(result.createdAt());
        verify(appeals).saveAndFlush(any());
    }

    @Test
    void anotherPersonOrCommunityCannotAppealWarning() {
        player.setId(15L);
        membership(7L, 15L, CommunityRole.MEMBER);
        assertStatus(HttpStatus.NOT_FOUND, () -> service.submit(7L, 42L, "player", "Falso"));
        player.setId(11L);
        membership(8L, 11L, CommunityRole.MEMBER);
        when(communities.existsById(8L)).thenReturn(true);
        assertStatus(HttpStatus.NOT_FOUND, () -> service.submit(8L, 42L, "player", "Falso"));
        verifyNoInteractions(appeals);
    }

    @Test
    void duplicateAndRaceConditionsCannotCreateSecondAppeal() {
        when(appeals.existsByWarningId(42L)).thenReturn(true);
        assertStatus(HttpStatus.CONFLICT, () -> service.submit(7L, 42L, "player", "Contestação"));
        reset(appeals);
        when(appeals.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("duplicate"));
        assertStatus(HttpStatus.CONFLICT, () -> service.submit(7L, 42L, "player", "Contestação"));
    }

    @Test
    void blankOrOversizedStatementIsNotSaved() {
        for (String statement : new String[] {"", " ", "x".repeat(2001)}) {
            assertStatus(HttpStatus.BAD_REQUEST, () -> service.submit(7L, 42L, "player", statement));
        }
        assertStatus(HttpStatus.BAD_REQUEST, () -> service.submit(7L, 42L, "player", null));
        verify(appeals, never()).saveAndFlush(any());
    }

    @Test
    void playerCanOnlyReadOwnAppealsAndManagersSeeCommunityAppeals() {
        when(appeals.findTop100ByCommunityIdAndAuthorProfileIdOrderByCreatedAtDescIdDesc(7L, 11L))
                .thenReturn(List.of());
        assertEquals(List.of(), service.mine(7L, "player"));
        assertStatus(HttpStatus.FORBIDDEN, () -> service.forManagers(7L, "player"));
        membership(7L, 11L, CommunityRole.ADMIN);
        when(appeals.findTop100ByCommunityIdOrderByCreatedAtDescIdDesc(7L)).thenReturn(List.of());
        assertEquals(List.of(), service.forManagers(7L, "player"));
    }

    @Test
    void outsiderCannotAccessAppeals() {
        assertStatus(HttpStatus.NOT_FOUND, () -> service.mine(100L, "player"));
        verifyNoInteractions(appeals);
    }

    private static void assertStatus(HttpStatus status, org.junit.jupiter.api.function.Executable action) {
        assertEquals(status, assertThrows(ResponseStatusException.class, action).getStatusCode());
    }
}
