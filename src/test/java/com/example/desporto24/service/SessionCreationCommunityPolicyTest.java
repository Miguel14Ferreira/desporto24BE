package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionCreationCommunityPolicyTest {
    @Test
    void existingCommunitiesAllowMembersButRestrictedCommunitiesRequireAdminOrOwner() {
        var memberRepository = mock(CommunityMemberRepository.class);
        var service = mock(SessionServiceImpl.class, CALLS_REAL_METHODS);
        ReflectionTestUtils.setField(service, "communityMemberRepository", memberRepository);
        var group = Community.builder().id(9L).build();
        var creator = new Profile(); creator.setId(7L);
        var membership = CommunityMember.builder().community(group).profile(creator)
                .role(CommunityRole.MEMBER).build();
        when(memberRepository.findByCommunityIdAndProfileId(9L, 7L))
                .thenReturn(Optional.of(membership));

        assertTrue(group.isMembersCanCreateSessions());
        assertDoesNotThrow(() -> require(service, group, creator));
        group.setMembersCanCreateSessions(false);
        assertThrows(AccessDeniedException.class, () -> require(service, group, creator));

        membership.setRole(CommunityRole.ADMIN);
        assertDoesNotThrow(() -> require(service, group, creator));
        membership.setRole(CommunityRole.OWNER);
        assertDoesNotThrow(() -> require(service, group, creator));
        when(memberRepository.findByCommunityIdAndProfileId(9L, 7L)).thenReturn(Optional.empty());
        assertThrows(AccessDeniedException.class, () -> require(service, group, creator));
    }

    private void require(SessionServiceImpl service, Community group, Profile actor) {
        ReflectionTestUtils.invokeMethod(service, "requireCanCreateCommunitySession", group, actor);
    }
}
