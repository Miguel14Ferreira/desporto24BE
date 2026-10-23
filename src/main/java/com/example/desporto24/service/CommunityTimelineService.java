package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.websocket.CommunityChatMessageCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import java.util.LinkedHashSet;
import java.util.Set;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunityTimelineEntry;
import com.example.desporto24.model.dto.community.CommunityTimelineEntryResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.community.CommunityTimelineEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import org.springframework.data.domain.PageRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityTimelineService {
    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityTimelineEntryRepository timelineRepository;
    private final ProfileRepository profileRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<CommunityTimelineEntryResponse> list(Long communityId, CommunityTimelineEntry.Kind kind, String username) {
        requireMember(communityId, username);
        List<CommunityTimelineEntry> items = new ArrayList<>(
                timelineRepository.findTop60ByCommunityIdAndKindOrderByCreatedAtDescIdDesc(communityId, kind));
        if (kind == CommunityTimelineEntry.Kind.CHAT) Collections.reverse(items);
        return items.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<CommunityTimelineEntryResponse> olderMessages(Long communityId, Long beforeId, String username) {
        requireMember(communityId, username);
        if (beforeId == null || beforeId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message cursor");
        }
        CommunityTimelineEntry cursor = timelineRepository.findById(beforeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));
        if (!communityId.equals(cursor.getCommunity().getId())
                || cursor.getKind() != CommunityTimelineEntry.Kind.CHAT) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
        }
        List<CommunityTimelineEntry> older = new ArrayList<>(
                timelineRepository.findByCommunityIdAndKindAndIdLessThanOrderByIdDesc(
                        communityId, CommunityTimelineEntry.Kind.CHAT, beforeId, PageRequest.of(0, 60)));
        Collections.reverse(older);
        return older.stream().map(this::toResponse).toList();
    }

    @Transactional
    public CommunityTimelineEntryResponse post(Long communityId, CommunityTimelineEntry.Kind kind,
                                                String body, String username) {
        Access access = requireMember(communityId, username);
        String normalized = body == null ? "" : body.trim();
        int max = kind == CommunityTimelineEntry.Kind.POST ? 1000 : 2000;
        if (normalized.isBlank() || normalized.length() > max) {
            throw new IllegalArgumentException("Text must contain between 1 and " + max + " characters");
        }
        CommunityTimelineEntry saved = timelineRepository.save(CommunityTimelineEntry.builder()
                .community(access.community())
                .author(access.actor())
                .kind(kind)
                .body(normalized)
                .createdAt(Instant.now())
                .build());
        CommunityTimelineEntryResponse response = toResponse(saved);
        if (kind == CommunityTimelineEntry.Kind.CHAT) {
            // Fan out to current members only; the listener delivers AFTER_COMMIT.
            Set<String> recipients = new LinkedHashSet<>();
            if (access.community().getOwner() != null) {
                recipients.add(access.community().getOwner().getUsername());
            }
            memberRepository.findByCommunityIdOrderByJoinedAtAsc(communityId)
                    .forEach(member -> recipients.add(member.getProfile().getUsername()));
            eventPublisher.publishEvent(new CommunityChatMessageCreatedEvent(response, recipients));
        }
        return response;
    }

    @Transactional
    public void delete(Long communityId, CommunityTimelineEntry.Kind kind, Long entryId, String username) {
        Access access = requireMember(communityId, username);
        CommunityTimelineEntry entry = timelineRepository.findById(entryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found"));
        if (!communityId.equals(entry.getCommunity().getId()) || entry.getKind() != kind) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found");
        }
        boolean author = access.actor().getId().equals(entry.getAuthor().getId());
        boolean manager = access.community().getOwner().getId().equals(access.actor().getId())
                || memberRepository.findByCommunityIdAndProfileId(communityId, access.actor().getId())
                   .map(member -> member.getRole() == CommunityRole.ADMIN || member.getRole() == CommunityRole.OWNER)
                   .orElse(false);
        if (!author && !manager) throw new AccessDeniedException("Only the author or a community manager can delete");
        timelineRepository.delete(entry);
    }

    private Access requireMember(Long communityId, String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new AccessDeniedException("Not authenticated as a community member"));
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Community not found"));
        boolean owner = community.getOwner().getId().equals(actor.getId());
        if (!owner && !memberRepository.existsByCommunityIdAndProfileId(communityId, actor.getId())) {
            throw new AccessDeniedException("Only community members can access this content");
        }
        return new Access(community, actor);
    }

    private CommunityTimelineEntryResponse toResponse(CommunityTimelineEntry entry) {
        return new CommunityTimelineEntryResponse(
                entry.getId(), entry.getCommunity().getId(), entry.getKind().name(),
                entry.getAuthor().getUsername(), entry.getBody(), entry.getCreatedAt());
    }

    private record Access(Community community, Profile actor) {}
}
