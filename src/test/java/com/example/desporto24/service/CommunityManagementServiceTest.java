package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.community.InviteCommunityMemberRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityInviteRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommunityManagementServiceTest {

    @Test
    void inviteUnknownUsernameUsesUserNotFoundExceptionInsteadOfInternalError() {
        CommunityRepository communityRepository = mock(CommunityRepository.class);
        CommunityMemberRepository memberRepository = mock(CommunityMemberRepository.class);
        CommunityInviteRepository inviteRepository = mock(CommunityInviteRepository.class);
        ProfileRepository profileRepository = mock(ProfileRepository.class);

        Profile owner = new Profile();
        owner.setId(1L);
        owner.setUsername("owner");

        CommunityMember ownerMembership = CommunityMember.builder()
                .profile(owner)
                .role(CommunityRole.OWNER)
                .build();

        when(profileRepository.findUserByUsernameIgnoreCase("owner"))
                .thenReturn(Optional.of(owner));
        when(memberRepository.findByCommunityIdAndProfileId(10L, 1L))
                .thenReturn(Optional.of(ownerMembership));
        when(profileRepository.findUserByUsernameIgnoreCase("missing-user"))
                .thenReturn(Optional.empty());

        CommunityManagementService service = new CommunityManagementService(
                communityRepository,
                memberRepository,
                inviteRepository,
                profileRepository
        );

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> service.invite(
                        10L,
                        "owner",
                        new InviteCommunityMemberRequest("missing-user")
                )
        );

        assertEquals(NO_USER_FOUND_BY_USERNAME, exception.getMessage());
        verify(inviteRepository, never()).save(any());
    }
}
