package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.community.CreateCommunityRequest;
import com.example.desporto24.model.dto.community.UpdateCommunitySportsRequest;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CommunityMultipleSportsServiceTest {
    private final CommunityRepository communities = mock(CommunityRepository.class);
    private final CommunityMemberRepository members = mock(CommunityMemberRepository.class);
    private final ProfileRepository profiles = mock(ProfileRepository.class);
    private final CommunityManagementService service = new CommunityManagementService(
            communities, members, mock(CommunityInviteRepository.class),
            mock(CommunityJoinRequestRepository.class), profiles,
            mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));
    private final Profile owner = new Profile();

    @BeforeEach
    void setup() {
        org.springframework.test.util.ReflectionTestUtils.setField(service, "creationQuota",
                mock(CommunityCreationQuotaService.class));
        owner.setId(4L);
        owner.setUsername("owner");
        when(profiles.findUserByUsernameIgnoreCase("owner")).thenReturn(Optional.of(owner));
        when(communities.save(any(Community.class))).thenAnswer(i -> i.getArgument(0));
        when(members.save(any(CommunityMember.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void createsCommunityWithSeveralSportsAndLegacyPrimarySport() {
        var result = service.create("owner", new CreateCommunityRequest(
                " Grupo ", null, "Futebol", CommunityVisibility.PRIVATE,
                List.of(" Padel ", "Basquetebol", "padel", "Natação")));
        assertEquals("Padel", result.sport());
        assertEquals(List.of("Padel", "Basquetebol", "Natação"), result.sports());
        var captor = org.mockito.ArgumentCaptor.forClass(Community.class);
        verify(communities).save(captor.capture());
        assertEquals(List.of("Padel", "Basquetebol", "Natação"),
                captor.getValue().getSports());
    }

    @Test
    void remainsCompatibleWithSingleSportAndWithoutSport() {
        var legacy = service.create("owner", new CreateCommunityRequest(
                "Futebol", null, "Futebol", CommunityVisibility.PUBLIC));
        assertEquals("Futebol", legacy.sport());
        assertEquals(List.of("Futebol"), legacy.sports());

        var sportless = service.create("owner", new CreateCommunityRequest(
                "Aberta", null, null, CommunityVisibility.PUBLIC));
        assertNull(sportless.sport());
        assertEquals(List.of(), sportless.sports());
    }

    @Test
    void adminCanReplaceSportsAndThePrimaryLegacySportFollowsFirstSelection() {
        var group = Community.builder().id(7L).owner(owner)
                .name("Comunidade").sport("Futebol").sports(new java.util.ArrayList<>(List.of("Futebol")))
                .visibility(CommunityVisibility.PUBLIC).build();
        var adminMembership = CommunityMember.builder().community(group)
                .profile(owner).role(CommunityRole.ADMIN).build();
        when(members.findByCommunityIdAndProfileId(7L, 4L))
                .thenReturn(Optional.of(adminMembership));
        when(members.findByCommunityIdOrderByJoinedAtAsc(7L))
                .thenReturn(List.of(adminMembership));

        var response = service.updateSports(7L, "owner",
                new UpdateCommunitySportsRequest(List.of(" Ténis ", "Futebol", "ténis")));
        assertEquals("Ténis", response.sport());
        assertEquals(List.of("Ténis", "Futebol"), response.sports());
        assertEquals("Ténis", group.getSport());
        verify(communities).save(group);
    }

    @Test
    void ordinaryMemberCannotChangeTheSports() {
        var group = Community.builder().id(7L).owner(owner).name("Comunidade").build();
        when(members.findByCommunityIdAndProfileId(7L, 4L))
                .thenReturn(Optional.of(CommunityMember.builder().community(group)
                        .profile(owner).role(CommunityRole.MEMBER).build()));

        assertThrows(SecurityException.class, () -> service.updateSports(
                7L, "owner", new UpdateCommunitySportsRequest(List.of("Padel"))));
        verify(communities, never()).save(any());
    }

    @Test
    void rejectsBlankOrOversizedSportWithoutWriting() {
        assertThrows(IllegalArgumentException.class, () -> service.create(
                "owner", new CreateCommunityRequest("Comunidade", null, null,
                        CommunityVisibility.PUBLIC, List.of("Padel", " "))));

        assertThrows(IllegalArgumentException.class, () -> service.create(
                "owner", new CreateCommunityRequest("Comunidade", null, null,
                        CommunityVisibility.PUBLIC, List.of("X".repeat(101)))));
        verify(communities, never()).save(any());
    }
}
