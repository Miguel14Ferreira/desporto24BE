package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.websocket.CommunityChatMessageCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.mockito.ArgumentCaptor;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityTimelineEntry;
import com.example.desporto24.model.community.CommunityTimelineEntry.Kind;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTimelineEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommunityTimelineServiceTest {
    @Mock CommunityRepository communities;
    @Mock CommunityMemberRepository members;
    @Mock CommunityTimelineEntryRepository timelines;
    @Mock ProfileRepository profiles;
    @Mock ApplicationEventPublisher eventPublisher;
    @InjectMocks CommunityTimelineService service;

    private final Profile owner = profile(1L, "owner");
    private final Profile member = profile(2L, "member");
    private final Profile outsider = profile(3L, "outsider");
    private final Community group = Community.builder().id(7L).owner(owner).name("Cacém").build();

    static Profile profile(Long id, String username) {
        Profile p = new Profile();
        p.setId(id);
        p.setUsername(username);
        return p;
    }

    @BeforeEach
    void communityLookup() {
        // No globally accessible community timeline, even for PUBLIC communities.
    }

    private void setup(Profile profile, boolean isMember) {
        when(profiles.findUserByUsernameIgnoreCase(profile.getUsername())).thenReturn(Optional.of(profile));
        when(communities.findById(7L)).thenReturn(Optional.of(group));
        if (profile != owner)
            when(members.existsByCommunityIdAndProfileId(7L, profile.getId())).thenReturn(isMember);
    }

    @Test
    void outsiderCannotReadOrPostInPrivateCommunity() {
        setup(outsider, false);
        assertThrows(AccessDeniedException.class, () -> service.list(7L, Kind.POST, "outsider"));
        assertThrows(AccessDeniedException.class, () -> service.post(7L, Kind.CHAT, "Olá", "outsider"));
        verifyNoInteractions(timelines);
    }

    @Test
    void membersCanPublishTrimmedFeedPosts() {
        setup(member, true);
        when(timelines.save(any())).thenAnswer(inv -> {
            CommunityTimelineEntry e = inv.getArgument(0);
            e.setId(11L);
            return e;
        });
        var response = service.post(7L, Kind.POST, "  Bom jogo!  ", "member");
        assertEquals("Bom jogo!", response.body());
        assertEquals("member", response.username());
        assertEquals("POST", response.kind());
        assertEquals(7L, response.communityId());
        verify(timelines).save(argThat(p -> p.getCreatedAt() != null
                && p.getCommunity() == group && p.getAuthor() == member));
    }

    @Test
    void chatMessagesAreBroadcastOnlyToCommunityMembersAfterPersistence() {
        setup(member, true);
        when(timelines.save(any())).thenAnswer(inv -> {
            CommunityTimelineEntry entry = inv.getArgument(0);
            entry.setId(44L);
            return entry;
        });
        when(members.findByCommunityIdOrderByJoinedAtAsc(7L)).thenReturn(List.of(
                CommunityMember.builder().profile(member).build()));
        var result = service.post(7L, Kind.CHAT, "  Bora ao jogo  ", "member");
        assertEquals("Bora ao jogo", result.body());
        var captor = ArgumentCaptor.forClass(CommunityChatMessageCreatedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertEquals(44L, captor.getValue().message().id());
        assertEquals(7L, captor.getValue().message().communityId());
        assertEquals("CHAT", captor.getValue().message().kind());
        assertEquals(java.util.Set.of("owner", "member"), captor.getValue().recipients());
        verify(timelines).save(any());
    }

    @Test
    void feedPostsNeverBroadcastAsChatMessages() {
        setup(owner, true);
        when(timelines.save(any())).thenAnswer(inv -> {
            CommunityTimelineEntry entry = inv.getArgument(0);
            entry.setId(11L);
            return entry;
        });
        service.post(7L, Kind.POST, "Notícias", "owner");
        verifyNoInteractions(eventPublisher);
        verify(members, never()).findByCommunityIdOrderByJoinedAtAsc(anyLong());
    }

    @Test
    void emptyOrOversizedContentCannotBeSaved() {
        setup(owner, true);
        assertThrows(IllegalArgumentException.class, () -> service.post(7L, Kind.POST, "  ", "owner"));
        assertThrows(IllegalArgumentException.class,
                () -> service.post(7L, Kind.POST, "x".repeat(1001), "owner"));
        assertThrows(IllegalArgumentException.class,
                () -> service.post(7L, Kind.CHAT, "x".repeat(2001), "owner"));
        verifyNoInteractions(timelines);
    }

    @Test
    void readChatOldestFirstFromBoundedLatestWindow() {
        setup(member, true);
        var earlier = entry(10L, Kind.CHAT, member);
        var later = entry(11L, Kind.CHAT, owner);
        when(timelines.findTop60ByCommunityIdAndKindOrderByCreatedAtDescIdDesc(7L, Kind.CHAT))
                .thenReturn(List.of(later, earlier));
        assertEquals(List.of(10L, 11L), service.list(7L, Kind.CHAT, "member").stream().map(x -> x.id()).toList());
    }

    @Test
    void nonModeratorCannotDeleteSomeoneElsesPost() {
        setup(member, true);
        when(timelines.findById(22L)).thenReturn(Optional.of(entry(22L, Kind.POST, owner)));
        when(members.findByCommunityIdAndProfileId(7L, 2L)).thenReturn(Optional.of(
                CommunityMember.builder().role(CommunityRole.MEMBER).build()));
        assertThrows(AccessDeniedException.class, () -> service.delete(7L, Kind.POST, 22L, "member"));
        verify(timelines, never()).delete(any());
    }

    @Test
    void adminCanModerateAndOwnerCanRemoveChat() {
        setup(member, true);
        when(timelines.findById(22L)).thenReturn(Optional.of(entry(22L, Kind.POST, outsider)));
        when(members.findByCommunityIdAndProfileId(7L, 2L)).thenReturn(Optional.of(
                CommunityMember.builder().role(CommunityRole.ADMIN).build()));
        service.delete(7L, Kind.POST, 22L, "member");
        verify(timelines).delete(any());
    }

    @Test
    void cannotDeleteAnEntryFromAnotherCommunityOrAnotherTab() {
        setup(owner, true);
        var post = entry(22L, Kind.POST, member);
        when(timelines.findById(22L)).thenReturn(Optional.of(post));
        assertThrows(ResponseStatusException.class, () -> service.delete(7L, Kind.CHAT, 22L, "owner"));
        post.setCommunity(Community.builder().id(9L).owner(owner).build());
        assertThrows(ResponseStatusException.class, () -> service.delete(7L, Kind.POST, 22L, "owner"));
        verify(timelines, never()).delete(any());
    }

    private CommunityTimelineEntry entry(Long id, Kind kind, Profile actor) {
        return CommunityTimelineEntry.builder()
                .id(id).community(group).kind(kind).author(actor)
                .body("Mensagem").createdAt(Instant.parse("2026-10-20T10:00:00Z")).build();
    }
}
