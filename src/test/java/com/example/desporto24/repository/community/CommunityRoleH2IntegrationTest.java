package com.example.desporto24.repository.community;

import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityJoinRequest;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.community.UpdateCommunityFeaturesRequest;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.CommunityManagementService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Real Hibernate/JPA repositories and an in-memory H2 database.
 * @DataJpaTest rolls back every test and no external services are contacted.
 * This covers service + persistence authorization, not HTTP status mapping.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:community-role-integration;MODE=LEGACY;NON_KEYWORDS=SESSION;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.hikari.schema=PUBLIC"
})
class CommunityRoleH2IntegrationTest {

    @Autowired private EntityManager em;
    @Autowired private CommunityRepository communities;
    @Autowired private CommunityMemberRepository members;
    @Autowired private CommunityJoinRequestRepository requests;
    @Autowired private ProfileRepository profiles;

    private CommunityManagementService service;
    private Long groupId;
    private Long outsiderId;

    @BeforeEach
    void seedDisposableOwnerAdminMemberAndNonmember() {
        Profile owner = profile("h2-role-owner");
        Profile admin = profile("h2-role-admin");
        Profile member = profile("h2-role-member");
        Profile outsider = profile("h2-role-outsider");

        Community group = Community.builder()
                .name("Disposable role matrix")
                .owner(owner)
                .visibility(CommunityVisibility.PRIVATE)
                .rankingsEnabled(false)
                .createdAt(Instant.now())
                .build();
        em.persist(group);
        em.persist(membership(group, owner, CommunityRole.OWNER));
        em.persist(membership(group, admin, CommunityRole.ADMIN));
        em.persist(membership(group, member, CommunityRole.MEMBER));
        em.flush();
        groupId = group.getId();
        outsiderId = outsider.getId();
        em.clear();

        service = new CommunityManagementService(
                communities, members, mock(CommunityInviteRepository.class), requests, profiles,
                mock(NotificationsRepository.class), mock(ApplicationEventPublisher.class));
    }

    @Test
    void onlyPersistedMembersCanReadFullCommunityDetails() {
        assertEquals(CommunityRole.OWNER, service.get(groupId, "h2-role-owner").myRole());
        assertEquals(CommunityRole.ADMIN, service.get(groupId, "h2-role-admin").myRole());
        assertEquals(CommunityRole.MEMBER, service.get(groupId, "h2-role-member").myRole());
        assertThrows(SecurityException.class, () -> service.get(groupId, "h2-role-outsider"));
    }

    @Test
    void realPendingJoinRequestRowsAreOnlyVisibleToManagers() {
        Long requestId = seedPendingRequest();

        assertThrows(SecurityException.class,
                () -> service.pendingJoinRequests(groupId, "h2-role-member"));
        assertThrows(SecurityException.class,
                () -> service.pendingJoinRequests(groupId, "h2-role-outsider"));
        assertEquals(requestId,
                service.pendingJoinRequests(groupId, "h2-role-owner").get(0).id());
        assertEquals(requestId,
                service.pendingJoinRequests(groupId, "h2-role-admin").get(0).id());
    }

    @Test
    void rejectedApprovalsDoNotChangePendingRequestsOrAddMembers() {
        Long requestId = seedPendingRequest();
        assertThrows(SecurityException.class,
                () -> service.acceptJoinRequest(groupId, requestId, "h2-role-member"));
        assertThrows(SecurityException.class,
                () -> service.acceptJoinRequest(groupId, requestId, "h2-role-outsider"));

        em.flush();
        em.clear();
        assertEquals(CommunityInviteStatus.PENDING,
                requests.findById(requestId).orElseThrow().getStatus());
        assertFalse(members.existsByCommunityIdAndProfileId(groupId, outsiderId));
    }

    @Test
    void featuresAreWrittenByAdminsButNotByMembersOrOutsiders() {
        var enable = new UpdateCommunityFeaturesRequest(true, true, false);
        assertThrows(SecurityException.class,
                () -> service.updateFeatures(groupId, "h2-role-member", enable));
        assertThrows(SecurityException.class,
                () -> service.updateFeatures(groupId, "h2-role-outsider", enable));
        em.flush();
        em.clear();
        assertFalse(communities.findById(groupId).orElseThrow().isRankingsEnabled());

        assertEquals(CommunityRole.ADMIN,
                service.updateFeatures(groupId, "h2-role-admin", enable).myRole());
        em.flush();
        em.clear();
        assertTrue(communities.findById(groupId).orElseThrow().isRankingsEnabled());
    }

    private Long seedPendingRequest() {
        Community group = communities.findById(groupId).orElseThrow();
        Profile outsider = profiles.findUserByUsernameIgnoreCase("h2-role-outsider").orElseThrow();
        CommunityJoinRequest request = CommunityJoinRequest.builder()
                .community(group).requester(outsider)
                .status(CommunityInviteStatus.PENDING)
                .createdAt(Instant.now()).build();
        em.persist(request);
        em.flush();
        Long id = request.getId();
        em.clear();
        return id;
    }

    private Profile profile(String username) {
        Profile profile = new Profile();
        profile.setUsername(username);
        em.persist(profile);
        return profile;
    }

    private CommunityMember membership(Community group, Profile profile, CommunityRole role) {
        return CommunityMember.builder()
                .community(group).profile(profile).role(role)
                .joinedAt(Instant.now()).build();
    }
}
