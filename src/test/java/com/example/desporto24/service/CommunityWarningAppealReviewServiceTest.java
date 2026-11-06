package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityDisciplinaryWarning;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityWarningAppeal;
import com.example.desporto24.model.community.CommunityWarningAppealDecision;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
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

class CommunityWarningAppealReviewServiceTest {
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final CommunityRepository communities = mock(CommunityRepository.class);
    private final CommunityMemberRepository members = mock(CommunityMemberRepository.class);
    private final CommunityDisciplinaryWarningRepository warnings = mock(CommunityDisciplinaryWarningRepository.class);
    private final CommunityWarningAppealRepository appeals = mock(CommunityWarningAppealRepository.class);
    private final CommunityWarningAppealDecisionRepository decisions = mock(CommunityWarningAppealDecisionRepository.class);
    private final CommunityWarningAppealReviewService service =
            new CommunityWarningAppealReviewService(profiles, communities, members, warnings, appeals, decisions);
    private final Profile reviewer = new Profile();
    private static final CommunityWarningAppealDecision.Outcome OVERTURNED =
            CommunityWarningAppealDecision.Outcome.OVERTURNED;

    @BeforeEach
    void setup() {
        reviewer.setId(30L);
        reviewer.setUsername("independent");
        when(profiles.findUserByUsername("independent")).thenReturn(Optional.of(reviewer));
        when(communities.existsById(7L)).thenReturn(true);
        membership(7L, 30L, CommunityRole.ADMIN);
        when(appeals.findById(43L)).thenReturn(Optional.of(CommunityWarningAppeal.builder()
                .id(43L).warningId(42L).communityId(7L).authorProfileId(11L)
                .statement("A minha versão").createdAt(Instant.now()).build()));
        when(warnings.findById(42L)).thenReturn(Optional.of(CommunityDisciplinaryWarning.builder()
                .id(42L).communityId(7L).targetProfileId(11L).issuedByProfileId(10L)
                .reason("Incidente").createdAt(Instant.now()).build()));
    }

    private void membership(Long community, Long profile, CommunityRole role) {
        CommunityMember membership = new CommunityMember();
        membership.setRole(role);
        when(members.findByCommunityIdAndProfileId(community, profile))
                .thenReturn(Optional.of(membership));
    }

    @Test
    void independentAdminCanDecideOnceWithReasonAndAudit() {
        when(decisions.saveAndFlush(any())).thenAnswer(invocation -> {
            CommunityWarningAppealDecision decision = invocation.getArgument(0);
            decision.setId(99L);
            return decision;
        });
        var result = service.decide(7L, 43L, "independent", OVERTURNED, "  Sem provas suficientes  ");
        assertEquals(99L, result.id());
        assertEquals(43L, result.appealId());
        assertEquals(7L, result.communityId());
        assertEquals(30L, result.reviewedByProfileId());
        assertEquals(OVERTURNED, result.outcome());
        assertEquals("Sem provas suficientes", result.reason());
        assertNotNull(result.decidedAt());
        verify(decisions).saveAndFlush(any());
    }

    @Test
    void issuerAndSubjectCannotReviewTheirOwnCase() {
        reviewer.setId(10L);
        membership(7L, 10L, CommunityRole.OWNER);
        status(HttpStatus.FORBIDDEN, () -> service.decide(7L, 43L, "independent", OVERTURNED, "Motivo"));
        reviewer.setId(11L);
        membership(7L, 11L, CommunityRole.ADMIN);
        status(HttpStatus.FORBIDDEN, () -> service.decide(7L, 43L, "independent", OVERTURNED, "Motivo"));
        verifyNoInteractions(decisions);
    }

    @Test
    void ordinaryMemberAndOutsiderCannotReview() {
        membership(7L, 30L, CommunityRole.MEMBER);
        status(HttpStatus.FORBIDDEN, () -> service.decide(7L, 43L, "independent", OVERTURNED, "Motivo"));
        status(HttpStatus.NOT_FOUND, () -> service.decide(99L, 43L, "independent", OVERTURNED, "Motivo"));
        verifyNoInteractions(decisions);
    }

    @Test
    void appealFromAnotherCommunityCannotBeReviewed() {
        membership(8L, 30L, CommunityRole.OWNER);
        when(communities.existsById(8L)).thenReturn(true);
        status(HttpStatus.NOT_FOUND, () -> service.decide(8L, 43L, "independent", OVERTURNED, "Motivo"));
        verifyNoInteractions(decisions);
    }

    @Test
    void decisionNeedsOutcomeAndBoundedReason() {
        status(HttpStatus.BAD_REQUEST, () -> service.decide(7L, 43L, "independent", null, "Motivo"));
        status(HttpStatus.BAD_REQUEST, () -> service.decide(7L, 43L, "independent", OVERTURNED, " "));
        status(HttpStatus.BAD_REQUEST, () -> service.decide(7L, 43L, "independent", OVERTURNED, "x".repeat(1001)));
        verifyNoInteractions(decisions);
    }

    @Test
    void duplicateAndConcurrentDecisionsAreRejected() {
        when(decisions.existsByAppealId(43L)).thenReturn(true);
        status(HttpStatus.CONFLICT, () -> service.decide(7L, 43L, "independent", OVERTURNED, "Motivo"));
        reset(decisions);
        when(decisions.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("duplicate"));
        status(HttpStatus.CONFLICT, () -> service.decide(7L, 43L, "independent", OVERTURNED, "Motivo"));
    }

    @Test
    void playerReadsOnlyDecisionsOnTheirOwnAppeals() {
        reviewer.setId(11L);
        membership(7L, 11L, CommunityRole.MEMBER);
        when(appeals.findTop100ByCommunityIdAndAuthorProfileIdOrderByCreatedAtDescIdDesc(7L, 11L))
                .thenReturn(List.of(CommunityWarningAppeal.builder().id(43L).build()));
        when(decisions.findTop100ByCommunityIdAndAppealIdInOrderByDecidedAtDescIdDesc(7L, List.of(43L)))
                .thenReturn(List.of());
        assertEquals(List.of(), service.mine(7L, "independent"));
        verify(decisions).findTop100ByCommunityIdAndAppealIdInOrderByDecidedAtDescIdDesc(7L, List.of(43L));
        status(HttpStatus.FORBIDDEN, () -> service.forManagers(7L, "independent"));
    }

    private static void status(HttpStatus expected, org.junit.jupiter.api.function.Executable action) {
        assertEquals(expected, assertThrows(ResponseStatusException.class, action).getStatusCode());
    }
}
