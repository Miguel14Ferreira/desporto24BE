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
import org.springframework.mock.web.MockMultipartFile;
import com.example.desporto24.storage.MediaStorage;
import org.springframework.test.util.ReflectionTestUtils;
import java.net.URI;
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
    void olderMessagesRequireMembershipAndReturnOrderedBoundedHistory() {
        setup(member, true);
        var cursor = entry(91L, Kind.CHAT, member);
        when(timelines.findById(91L)).thenReturn(Optional.of(cursor));
        when(timelines.findByCommunityIdAndKindAndIdLessThanOrderByIdDesc(
                eq(7L), eq(Kind.CHAT), eq(91L), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(List.of(entry(90L, Kind.CHAT, owner), entry(89L, Kind.CHAT, member)));
        var result = service.olderMessages(7L, 91L, "member");
        assertEquals(List.of(89L, 90L), result.stream().map(x -> x.id()).toList());
        verify(timelines).findByCommunityIdAndKindAndIdLessThanOrderByIdDesc(
                eq(7L), eq(Kind.CHAT), eq(91L),
                argThat(page -> page.getPageSize() == 60 && page.getPageNumber() == 0));
    }

    @Test
    void olderMessagesCannotBeQueriedByOutsidersOrWithForeignCursor() {
        setup(outsider, false);
        assertThrows(AccessDeniedException.class,
                () -> service.olderMessages(7L, 91L, "outsider"));
        verifyNoInteractions(timelines);
    }

    @Test
    void olderMessagesRejectPostCursorAndInvalidIdentifiers() {
        setup(owner, true);
        when(timelines.findById(91L)).thenReturn(Optional.of(entry(91L, Kind.POST, member)));
        assertThrows(ResponseStatusException.class,
                () -> service.olderMessages(7L, 91L, "owner"));
        assertThrows(ResponseStatusException.class,
                () -> service.olderMessages(7L, 0L, "owner"));
        verify(timelines, never()).findByCommunityIdAndKindAndIdLessThanOrderByIdDesc(
                anyLong(), any(), anyLong(), any());
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

    @Test
    void communityReplyPersistsVerifiedChatSnapshotAndCannotQuoteOtherCommunitiesOrPosts() {
        setup(member, true);
        var original = entry(19L, Kind.CHAT, owner);
        original.setBody("Há treino?");
        when(timelines.findById(19L)).thenReturn(Optional.of(original));
        when(timelines.save(any())).thenAnswer(inv -> {
            CommunityTimelineEntry saved = inv.getArgument(0);
            saved.setId(20L);
            return saved;
        });
        var sent = service.post(7L, Kind.CHAT, "Sim!", "member", 19L);
        assertEquals(19L, sent.replyToMessageId());
        assertEquals("owner", sent.replyToSender());
        assertEquals("Há treino?", sent.replyToText());
        verify(timelines, times(1)).save(any());

        original.setCommunity(Community.builder().id(9L).owner(owner).build());
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.post(7L, Kind.CHAT, "Não pode", "member", 19L)).getStatusCode().value());
        original.setCommunity(group);
        original.setKind(Kind.POST);
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.post(7L, Kind.CHAT, "Não é chat", "member", 19L)).getStatusCode().value());
        verify(timelines, times(1)).save(any());
    }

    @Test
    void communityFeedCannotQuoteChatAndMissingParentNeverPersists() {
        setup(owner, true);
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.post(7L, Kind.POST, "Post", "owner", 18L)).getStatusCode().value());
        when(timelines.findById(77L)).thenReturn(Optional.empty());
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.post(7L, Kind.CHAT, "Olá", "owner", 77L)).getStatusCode().value());
        verify(timelines, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void membersUploadPhotosAndVideosWithoutChangingCommunityChat() {
        setup(member, true);
        MediaStorage media = mock(MediaStorage.class);
        ReflectionTestUtils.setField(service, "mediaStorage", media);
        MockMultipartFile image = new MockMultipartFile("image", "photo.png", "image/png", new byte[] {1, 2});
        MockMultipartFile video = new MockMultipartFile("video", "clip.mp4", "video/mp4", new byte[] {3, 4});
        when(media.store(startsWith("community-posts/7/"), same(image)))
                .thenReturn(URI.create("/media/community-posts/7/photo.png"));
        when(media.storeVideo(startsWith("community-posts/7/"), same(video)))
                .thenReturn(URI.create("/media/community-posts/7/clip.mp4"));
        when(timelines.save(any())).thenAnswer(inv -> {
            CommunityTimelineEntry entry = inv.getArgument(0);
            entry.setId(entry.getImageUrl() == null ? 15L : 14L);
            return entry;
        });

        var photo = service.postWithMedia(7L, "  Foi um bom jogo! ", image, null, "member");
        assertEquals("Foi um bom jogo!", photo.body());
        assertEquals("/media/community-posts/7/photo.png", photo.imageUrl());
        assertNull(photo.videoUrl());

        var movie = service.postWithMedia(7L, "", null, video, "member");
        assertEquals("", movie.body());
        assertEquals("/media/community-posts/7/clip.mp4", movie.videoUrl());
        assertNull(movie.imageUrl());
        verify(timelines, times(2)).save(argThat(entry ->
                entry.getKind() == Kind.POST && entry.getCommunity() == group));
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void outsidersCannotUploadEvenBeforeMediaValidationOrStorage() {
        setup(outsider, false);
        MediaStorage media = mock(MediaStorage.class);
        ReflectionTestUtils.setField(service, "mediaStorage", media);
        MockMultipartFile image = new MockMultipartFile("image", "photo.png", "image/png", new byte[] {1});
        assertThrows(AccessDeniedException.class,
                () -> service.postWithMedia(7L, "a", image, null, "outsider"));
        verifyNoInteractions(media, timelines);
    }

    @Test
    void combinedPhotoAndVideoAreRejectedBeforeWritingObjects() {
        setup(owner, true);
        MediaStorage media = mock(MediaStorage.class);
        ReflectionTestUtils.setField(service, "mediaStorage", media);
        MockMultipartFile image = new MockMultipartFile("image", "p.png", "image/png", new byte[] {1});
        MockMultipartFile video = new MockMultipartFile("video", "v.mp4", "video/mp4", new byte[] {2});
        assertThrows(IllegalArgumentException.class,
                () -> service.postWithMedia(7L, "hello", image, video, "owner"));
        verifyNoInteractions(media, timelines);
    }

    @Test
    void deletingCommunityPostAlsoDeletesItsUploadedMedia() {
        setup(owner, true);
        MediaStorage media = mock(MediaStorage.class);
        ReflectionTestUtils.setField(service, "mediaStorage", media);
        var post = entry(50L, Kind.POST, member);
        post.setImageStorageKey("community-posts/7/previous.png");
        when(timelines.findById(50L)).thenReturn(Optional.of(post));

        service.delete(7L, Kind.POST, 50L, "owner");

        var order = inOrder(timelines, media);
        order.verify(timelines).delete(post);
        order.verify(timelines).flush();
        order.verify(media).delete("community-posts/7/previous.png");
    }

    private com.example.desporto24.storage.PrivateChatFileStorage stubPrivateChatStorage() {
        var storage = mock(com.example.desporto24.storage.PrivateChatFileStorage.class);
        ReflectionTestUtils.setField(service, "privateChatStorage", storage);
        return storage;
    }

    @Test
    void communityPrivateAttachmentsAreMemberOnlyAndFailClosedIfBucketIsUnavailable() {
        setup(outsider, false);
        var storage = stubPrivateChatStorage();
        assertThrows(AccessDeniedException.class,
                () -> service.chatAttachmentsAvailable(7L, "outsider"));
        assertThrows(AccessDeniedException.class,
                () -> service.postChatAttachment(7L,
                        new MockMultipartFile("file", "x.pdf", "application/pdf", new byte[] {1}),
                        "", "outsider", null));
        assertThrows(AccessDeniedException.class,
                () -> service.downloadChatAttachment(7L, 123L, "outsider"));
        verifyNoInteractions(storage, timelines);
    }

    @Test
    void communityMemberOnlySeesPrivateAvailabilityAfterHeadBucketSucceeds() {
        setup(member, true);
        var storage = stubPrivateChatStorage();
        when(storage.isConfigured()).thenReturn(false, true);
        assertFalse(service.chatAttachmentsAvailable(7L, "member"));
        assertTrue(service.chatAttachmentsAvailable(7L, "member"));
        verify(storage, times(2)).isConfigured();
        verifyNoInteractions(timelines);
    }

    @Test
    void memberCanSendPrivateChatFileWithVerifiedReplyAndAfterCommitAudience() {
        setup(member, true);
        var storage = stubPrivateChatStorage();
        MockMultipartFile pdf = new MockMultipartFile("file", "treino.pdf",
                "application/pdf", new byte[] {1, 2, 3});
        String key = "private-chat/12345678-1111-2222-3333-999999999999.pdf";
        var parent = entry(41L, Kind.CHAT, owner);
        when(timelines.findById(41L)).thenReturn(Optional.of(parent));
        when(storage.store(pdf)).thenReturn(
                new com.example.desporto24.storage.PrivateChatFileStorage.StoredFile(
                        key, "treino.pdf", "application/pdf", 3L));
        when(timelines.save(any())).thenAnswer(inv -> {
            CommunityTimelineEntry sent = inv.getArgument(0);
            sent.setId(42L);
            return sent;
        });
        when(members.findByCommunityIdOrderByJoinedAtAsc(7L)).thenReturn(List.of(
                CommunityMember.builder().profile(member).build()));
        var response = service.postChatAttachment(7L, pdf, " ", "member", 41L);
        assertEquals(42L, response.id());
        assertEquals(41L, response.replyToMessageId());
        assertEquals("Mensagem", response.replyToText());
        assertEquals("📎 treino.pdf", response.body());
        assertEquals("treino.pdf", response.attachmentName());
        assertEquals("application/pdf", response.attachmentMimeType());
        assertEquals(3L, response.attachmentSize());
        assertNull(response.imageUrl());
        assertNull(response.videoUrl());
        assertFalse(response.toString().contains(key));
        verify(timelines).save(argThat(e -> key.equals(e.getAttachmentKey())
                && e.getKind() == Kind.CHAT && e.getImageUrl() == null && e.getVideoUrl() == null));
        verify(timelines).flush();
        var events = org.mockito.ArgumentCaptor.forClass(CommunityChatMessageCreatedEvent.class);
        verify(eventPublisher).publishEvent(events.capture());
        assertEquals(java.util.Set.of("member", "owner"), events.getValue().recipients());
        assertEquals(42L, events.getValue().message().id());
        verify(storage, never()).delete(anyString());
    }

    @Test
    void communityPrivateChatRejectsForeignRepliesBeforeStoringAnything() {
        setup(member, true);
        var storage = stubPrivateChatStorage();
        var parent = entry(55L, Kind.CHAT, owner);
        parent.setCommunity(Community.builder().id(9L).owner(owner).build());
        when(timelines.findById(55L)).thenReturn(Optional.of(parent));
        MockMultipartFile pdf = new MockMultipartFile("file", "treino.pdf",
                "application/pdf", new byte[] {1});
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.postChatAttachment(7L, pdf, "", "member", 55L))
                .getStatusCode().value());
        parent.setCommunity(group);
        parent.setKind(Kind.POST);
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> service.postChatAttachment(7L, pdf, "", "member", 55L))
                .getStatusCode().value());
        verifyNoInteractions(storage);
        verify(timelines, never()).save(any());
    }

    @Test
    void communityChatFilePersistenceFailureDeletesUploadedPrivateObject() {
        setup(owner, true);
        var storage = stubPrivateChatStorage();
        MockMultipartFile pdf = new MockMultipartFile("file", "doc.pdf",
                "application/pdf", new byte[] {1});
        String key = "private-chat/12345678-1111-2222-3333-888888888888.pdf";
        when(storage.store(pdf)).thenReturn(
                new com.example.desporto24.storage.PrivateChatFileStorage.StoredFile(
                        key, "doc.pdf", "application/pdf", 1L));
        when(timelines.save(any())).thenThrow(new IllegalStateException("DB unavailable"));
        assertThrows(IllegalStateException.class,
                () -> service.postChatAttachment(7L, pdf, "", "owner", null));
        verify(storage).delete(key);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void chatFileDownloadsRequireCurrentMembershipMatchingCommunityAndChatKind() {
        setup(member, true);
        var storage = stubPrivateChatStorage();
        String key = "private-chat/12345678-1111-2222-3333-777777777777.pdf";
        var chat = entry(73L, Kind.CHAT, owner);
        chat.setAttachmentKey(key);
        chat.setAttachmentName("jogo.pdf");
        chat.setAttachmentMimeType("application/pdf");
        when(timelines.findById(73L)).thenReturn(Optional.of(chat));
        when(storage.read(key)).thenReturn(new byte[] {1, 3, 5});
        var file = service.downloadChatAttachment(7L, 73L, "member");
        assertArrayEquals(new byte[] {1, 3, 5}, file.content());
        assertEquals("jogo.pdf", file.filename());
        verify(storage).read(key);

        chat.setCommunity(Community.builder().id(9L).owner(owner).build());
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.downloadChatAttachment(7L, 73L, "member")).getStatusCode().value());
        chat.setCommunity(group);
        chat.setKind(Kind.POST);
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.downloadChatAttachment(7L, 73L, "member")).getStatusCode().value());
        chat.setKind(Kind.CHAT);
        chat.setAttachmentKey(null);
        assertEquals(404, assertThrows(ResponseStatusException.class,
                () -> service.downloadChatAttachment(7L, 73L, "member")).getStatusCode().value());
        verify(storage, times(1)).read(anyString());
    }

    @Test
    void moderatorDeletingChatAlsoDeletesItsPrivateObject() {
        setup(owner, true);
        var storage = stubPrivateChatStorage();
        var chat = entry(75L, Kind.CHAT, member);
        chat.setAttachmentKey("private-chat/12345678-1111-2222-3333-666666666666.pdf");
        when(timelines.findById(75L)).thenReturn(Optional.of(chat));
        service.delete(7L, Kind.CHAT, 75L, "owner");
        var order = inOrder(timelines, storage);
        order.verify(timelines).delete(chat);
        order.verify(timelines).flush();
        order.verify(storage).delete(chat.getAttachmentKey());
    }

    private CommunityTimelineEntry entry(Long id, Kind kind, Profile actor) {
        return CommunityTimelineEntry.builder()
                .id(id).community(group).kind(kind).author(actor)
                .body("Mensagem").createdAt(Instant.parse("2026-10-20T10:00:00Z")).build();
    }
}
