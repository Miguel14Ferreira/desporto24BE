package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityInviteStatus;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.exception.CommunityInvitePendingException;
import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.enumeration.CommunityVisibility;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityInvite;
import com.example.desporto24.model.community.CommunityJoinRequest;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.dto.community.*;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.enumeration.NotificationType;
import com.example.desporto24.repository.community.CommunityInviteRepository;
import com.example.desporto24.repository.community.CommunityJoinRequestRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import com.example.desporto24.storage.MediaStorage;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityManagementService {

    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityInviteRepository inviteRepository;
    private final CommunityJoinRequestRepository joinRequestRepository;
    private final ProfileRepository profileRepository;
    private final NotificationsRepository notificationsRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired(required = false)
    private MediaStorage mediaStorage;

    public CommunityResponse create(String username, CreateCommunityRequest request) {
        Profile owner = profile(username);

        Community community = Community.builder()
                .name(request.name().trim())
                .description(trimToNull(request.description()))
                .sport(trimToNull(request.sport()))
                .visibility(request.visibility())
                .owner(owner)
                .rankingsEnabled(false)
                .winPoints(3)
                .drawPoints(1)
                .lossPoints(0)
                .createdAt(Instant.now())
                .build();

        Community saved = communityRepository.save(community);

        memberRepository.save(CommunityMember.builder()
                .community(saved)
                .profile(owner)
                .role(CommunityRole.OWNER)
                .joinedAt(Instant.now())
                .build());

        return toResponse(saved, owner);
    }

    @Transactional(readOnly = true)
    public List<CommunityResponse> mine(String username) {
        Profile current = profile(username);
        return memberRepository.findByProfileIdOrderByJoinedAtDesc(current.getId()).stream()
                .map(member -> toResponse(member.getCommunity(), current))
                .toList();
    }

    @Transactional(readOnly = true)
    public CommunityResponse get(Long communityId, String username) {
        Profile current = profile(username);
        Community community = community(communityId);

        CommunityMember membership = memberRepository
                .findByCommunityIdAndProfileId(communityId, current.getId())
                .orElse(null);

        if (membership == null) {
            throw new SecurityException("Não tens acesso a esta comunidade");
        }

        return toResponse(community, current);
    }

    public CommunityResponse joinPublic(Long communityId, String username) {
        Profile current = profile(username);
        Community community = community(communityId);

        if (community.getVisibility() != CommunityVisibility.PUBLIC) {
            throw new SecurityException("Esta comunidade requer aprovação para aderir");
        }

        if (!memberRepository.existsByCommunityIdAndProfileId(communityId, current.getId())) {
            memberRepository.save(CommunityMember.builder()
                    .community(community)
                    .profile(current)
                    .role(CommunityRole.MEMBER)
                    .joinedAt(Instant.now())
                    .build());
        }

        joinRequestRepository.findByCommunityIdAndRequesterIdAndStatus(
                        communityId, current.getId(), CommunityInviteStatus.PENDING)
                .ifPresent(pending -> {
                    pending.setStatus(CommunityInviteStatus.ACCEPTED);
                    joinRequestRepository.save(pending);
                });

        inviteRepository.findByCommunityIdAndInvitedProfileIdAndStatus(
                        communityId, current.getId(), CommunityInviteStatus.PENDING)
                .ifPresent(pending -> {
                    pending.setStatus(CommunityInviteStatus.ACCEPTED);
                    inviteRepository.save(pending);
                });

        return toResponse(community, current);
    }

    public CommunityJoinRequestResponse requestJoin(Long communityId, String username) {
        Profile current = profile(username);
        Community community = community(communityId);

        if (community.getVisibility() != CommunityVisibility.PRIVATE) {
            throw new IllegalArgumentException("Comunidades públicas não requerem pedido de adesão");
        }
        if (memberRepository.existsByCommunityIdAndProfileId(communityId, current.getId())) {
            throw new IllegalArgumentException("Já pertences a esta comunidade");
        }

        CommunityJoinRequest request = joinRequestRepository
                .findByCommunityIdAndRequesterIdAndStatus(
                        communityId, current.getId(), CommunityInviteStatus.PENDING)
                .orElseGet(() -> joinRequestRepository.save(CommunityJoinRequest.builder()
                        .community(community)
                        .requester(current)
                        .status(CommunityInviteStatus.PENDING)
                        .createdAt(Instant.now())
                        .build()));

        return toJoinRequestResponse(request);
    }

    @Transactional(readOnly = true)
    public List<CommunityJoinRequestResponse> pendingJoinRequests(Long communityId, String username) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);

        return joinRequestRepository
                .findByCommunityIdAndStatusOrderByCreatedAtAsc(
                        communityId, CommunityInviteStatus.PENDING)
                .stream()
                .map(this::toJoinRequestResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<CommunityJoinRequestResponse> myPendingJoinRequest(Long communityId, String username) {
        Profile current = profile(username);
        community(communityId);

        return joinRequestRepository
                .findByCommunityIdAndRequesterIdAndStatus(
                        communityId, current.getId(), CommunityInviteStatus.PENDING)
                .map(this::toJoinRequestResponse);
    }

    public CommunityResponse acceptJoinRequest(Long communityId, Long requestId, String username) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);

        CommunityJoinRequest request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido de adesão não encontrado"));
        requireJoinRequestCommunity(request, communityId);
        requireJoinRequestPending(request);

        Profile requester = request.getRequester();
        if (!memberRepository.existsByCommunityIdAndProfileId(communityId, requester.getId())) {
            memberRepository.save(CommunityMember.builder()
                    .community(request.getCommunity())
                    .profile(requester)
                    .role(CommunityRole.MEMBER)
                    .joinedAt(Instant.now())
                    .build());
        }

        inviteRepository.findByCommunityIdAndInvitedProfileIdAndStatus(
                        communityId, requester.getId(), CommunityInviteStatus.PENDING)
                .ifPresent(pending -> {
                    pending.setStatus(CommunityInviteStatus.ACCEPTED);
                    inviteRepository.save(pending);
                });

        request.setStatus(CommunityInviteStatus.ACCEPTED);
        joinRequestRepository.save(request);
        return toResponse(request.getCommunity(), actor);
    }

    public void rejectJoinRequest(Long communityId, Long requestId, String username) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);

        CommunityJoinRequest request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido de adesão não encontrado"));
        requireJoinRequestCommunity(request, communityId);
        requireJoinRequestPending(request);

        request.setStatus(CommunityInviteStatus.REJECTED);
        joinRequestRepository.save(request);
    }

    public CommunityResponse updateVisibility(
            Long communityId, String username, UpdateCommunityVisibilityRequest request) {
        Profile actor = profile(username);
        CommunityMember membership = requireOwner(communityId, actor);
        if (request.visibility() == null) {
            throw new IllegalArgumentException("A visibilidade é obrigatória");
        }

        Community community = membership.getCommunity();
        community.setVisibility(request.visibility());
        Community saved = communityRepository.save(community);
        return toResponse(saved, actor);
    }

    public CommunityResponse updatePhoto(Long communityId, String username, MultipartFile photo) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);

        if (mediaStorage == null) {
            throw new IllegalStateException("Media storage is not configured");
        }
        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("A fotografia é obrigatória");
        }

        Community community = community(communityId);
        var uri = mediaStorage.store(
                "communities/" + community.getId() + "/" + UUID.randomUUID(),
                photo
        );

        community.setPhotoUrl(uri.toString());
        Community saved = communityRepository.save(community);
        return toResponse(saved, actor);
    }

    public CommunityResponse removePhoto(Long communityId, String username) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);

        Community community = community(communityId);
        String photoUrl = community.getPhotoUrl();
        community.setPhotoUrl(null);
        Community saved = communityRepository.save(community);
        deleteCommunityPhoto(photoUrl, communityId);
        return toResponse(saved, actor);
    }

    private void deleteCommunityPhoto(String photoUrl, Long communityId) {
        if (mediaStorage == null || photoUrl == null || photoUrl.isBlank()) {
            return;
        }

        String prefix = "communities/" + communityId + "/";
        int keyStart = photoUrl.indexOf(prefix);
        if (keyStart >= 0) {
            String objectKey = photoUrl.substring(keyStart);
            try {
                mediaStorage.delete(objectKey);
            } catch (RuntimeException exception) {
                log.warn(
                        "Community {} photo was detached but storage cleanup failed for key {}",
                        communityId,
                        objectKey,
                        exception
                );
            }
        }
    }

    public CommunityInviteResponse invite(Long communityId, String username, InviteCommunityMemberRequest request) {
        Profile actor = profile(username);
        requireAdmin(communityId, actor);

        Profile invited = profile(request.username().trim());
        if (memberRepository.existsByCommunityIdAndProfileId(communityId, invited.getId())) {
            throw new IllegalArgumentException("Este utilizador já pertence à comunidade");
        }

        inviteRepository.findByCommunityIdAndInvitedProfileIdAndStatus(
                communityId, invited.getId(), CommunityInviteStatus.PENDING
        ).ifPresent(existing -> {
            throw new CommunityInvitePendingException();
        });

        Community community = community(communityId);
        CommunityInvite invite = inviteRepository.save(CommunityInvite.builder()
                .community(community)
                .invitedProfile(invited)
                .invitedBy(actor)
                .status(CommunityInviteStatus.PENDING)
                .createdAt(Instant.now())
                .build());

        notifyCommunityInvite(invited, actor, community);
        eventPublisher.publishEvent(new CommunityInviteCreatedEvent(
                invite.getId(), invited.getEmail(), actor.getUsername(), community.getName()));
        return toInviteResponse(invite);
    }

    @Transactional(readOnly = true)
    public List<CommunityInviteResponse> myInvites(String username) {
        Profile current = profile(username);
        return inviteRepository.findByInvitedProfileIdAndStatusOrderByCreatedAtDesc(
                        current.getId(), CommunityInviteStatus.PENDING)
                .stream()
                .map(this::toInviteResponse)
                .toList();
    }

    public CommunityResponse acceptInvite(Long inviteId, String username) {
        Profile current = profile(username);
        CommunityInvite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new IllegalArgumentException("Convite não encontrado"));

        requireInviteTarget(invite, current);
        requirePending(invite);

        Community community = invite.getCommunity();
        if (!memberRepository.existsByCommunityIdAndProfileId(community.getId(), current.getId())) {
            memberRepository.save(CommunityMember.builder()
                    .community(community)
                    .profile(current)
                    .role(CommunityRole.MEMBER)
                    .joinedAt(Instant.now())
                    .build());
        }

        joinRequestRepository.findByCommunityIdAndRequesterIdAndStatus(
                        community.getId(), current.getId(), CommunityInviteStatus.PENDING)
                .ifPresent(pending -> {
                    pending.setStatus(CommunityInviteStatus.ACCEPTED);
                    joinRequestRepository.save(pending);
                });

        invite.setStatus(CommunityInviteStatus.ACCEPTED);
        inviteRepository.save(invite);
        return toResponse(community, current);
    }

    public void rejectInvite(Long inviteId, String username) {
        Profile current = profile(username);
        CommunityInvite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new IllegalArgumentException("Convite não encontrado"));

        requireInviteTarget(invite, current);
        requirePending(invite);
        invite.setStatus(CommunityInviteStatus.REJECTED);
        inviteRepository.save(invite);
    }

    public CommunityResponse updateRole(
            Long communityId,
            String actorUsername,
            String targetUsername,
            UpdateCommunityRoleRequest request) {
        Profile actor = profile(actorUsername);
        CommunityMember actorMembership = requireOwner(communityId, actor);

        Profile target = profile(targetUsername);
        CommunityMember targetMembership = memberRepository
                .findByCommunityIdAndProfileId(communityId, target.getId())
                .orElseThrow(() -> new IllegalArgumentException("O utilizador não pertence à comunidade"));

        if (targetMembership.getRole() == CommunityRole.OWNER) {
            throw new IllegalArgumentException("Usa a transferência de ownership para alterar o Owner");
        }
        if (request.role() == CommunityRole.OWNER) {
            throw new IllegalArgumentException("Usa a transferência de ownership para definir um novo Owner");
        }

        targetMembership.setRole(request.role());
        memberRepository.save(targetMembership);

        return toResponse(actorMembership.getCommunity(), actor);
    }

    public void removeMember(Long communityId, String actorUsername, String targetUsername) {
        Profile actor = profile(actorUsername);
        CommunityMember actorMembership = requireAdmin(communityId, actor);

        Profile target = profile(targetUsername);
        CommunityMember targetMembership = memberRepository
                .findByCommunityIdAndProfileId(communityId, target.getId())
                .orElseThrow(() -> new IllegalArgumentException("O utilizador não pertence à comunidade"));

        if (targetMembership.getRole() == CommunityRole.OWNER) {
            throw new IllegalArgumentException("O Owner não pode ser removido");
        }
        if (targetMembership.getRole() == CommunityRole.ADMIN
                && actorMembership.getRole() != CommunityRole.OWNER) {
            throw new SecurityException("Apenas o Owner pode remover administradores");
        }

        memberRepository.delete(targetMembership);
        eventPublisher.publishEvent(new CommunityMembershipEndedEvent(communityId, target.getId()));
    }

    public void leave(Long communityId, String username) {
        Profile current = profile(username);
        CommunityMember membership = memberRepository
                .findByCommunityIdAndProfileId(communityId, current.getId())
                .orElseThrow(() -> new IllegalArgumentException("Não pertences a esta comunidade"));

        if (membership.getRole() == CommunityRole.OWNER) {
            throw new IllegalArgumentException("Transfere primeiro a ownership antes de sair");
        }

        memberRepository.delete(membership);
        eventPublisher.publishEvent(new CommunityMembershipEndedEvent(communityId, current.getId()));
    }

    public CommunityResponse transferOwnership(
            Long communityId,
            String username,
            TransferOwnershipRequest request) {
        Profile current = profile(username);
        CommunityMember currentMembership = requireOwner(communityId, current);

        Profile newOwner = profile(request.username().trim());
        CommunityMember newOwnerMembership = memberRepository
                .findByCommunityIdAndProfileId(communityId, newOwner.getId())
                .orElseThrow(() -> new IllegalArgumentException("O novo Owner tem de ser membro da comunidade"));

        currentMembership.setRole(CommunityRole.ADMIN);
        newOwnerMembership.setRole(CommunityRole.OWNER);
        memberRepository.save(currentMembership);
        memberRepository.save(newOwnerMembership);

        Community community = currentMembership.getCommunity();
        community.setOwner(newOwner);
        communityRepository.save(community);

        return toResponse(community, current);
    }

    private CommunityResponse toResponse(Community community, Profile current) {
        List<CommunityMember> memberships = memberRepository.findByCommunityIdOrderByJoinedAtAsc(community.getId());
        CommunityRole myRole = memberships.stream()
                .filter(m -> m.getProfile().getId().equals(current.getId()))
                .map(CommunityMember::getRole)
                .findFirst()
                .orElse(null);

        List<CommunityMemberResponse> members = memberships.stream()
                .sorted(Comparator.comparing(m -> roleOrder(m.getRole())))
                .map(m -> new CommunityMemberResponse(
                        m.getProfile().getId(),
                        m.getProfile().getUsername(),
                        m.getProfile().getFullName(),
                        m.getProfile().getProfilePicUrl(),
                        m.getRole()
                ))
                .toList();

        return new CommunityResponse(
                community.getId(),
                community.getName(),
                community.getDescription(),
                community.getSport(),
                community.getPhotoUrl(),
                community.getVisibility(),
                community.isRankingsEnabled(),
                community.getOwner().getUsername(),
                myRole,
                members.size(),
                community.getCreatedAt(),
                members
        );
    }

    private int roleOrder(CommunityRole role) {
        return switch (role) {
            case OWNER -> 0;
            case ADMIN -> 1;
            case MEMBER -> 2;
        };
    }

    private void notifyCommunityInvite(Profile invited, Profile actor, Community community) {
        Notifications notification = new Notifications();
        notification.setSubject("Convite para a comunidade " + community.getName());
        notification.setMessage("@" + actor.getUsername() + " convidou-te para a comunidade "
                + community.getName() + ". Abre Comunidades para aceitar ou rejeitar o convite.");
        notification.setGreeting("");
        notification.setSignature("");
        notification.setCreatedAt(Instant.now().toString());
        notification.setFriendRequest(false);
        notification.setUpdateProfile(false);
        notification.setSessionInvite(false);
        notification.setNormalMessage(false);
        notification.setToken(null);
        notification.setUsername(invited.getUsername());
        notification.setRelatedUsername(actor.getUsername());
        notification.setType(NotificationType.COMMUNITY_INVITE);
        notification.setRead(false);
        notificationsRepository.save(notification);
    }

    private CommunityInviteResponse toInviteResponse(CommunityInvite invite) {
        return new CommunityInviteResponse(
                invite.getId(),
                invite.getCommunity().getId(),
                invite.getCommunity().getName(),
                invite.getInvitedBy().getUsername(),
                invite.getCreatedAt()
        );
    }

    private CommunityJoinRequestResponse toJoinRequestResponse(CommunityJoinRequest request) {
        return new CommunityJoinRequestResponse(
                request.getId(),
                request.getCommunity().getId(),
                request.getCommunity().getName(),
                request.getRequester().getUsername(),
                request.getStatus(),
                request.getCreatedAt()
        );
    }

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(
                        com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME
                ));
    }

    private Community community(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comunidade não encontrada"));
    }

    private CommunityMember requireAdmin(Long communityId, Profile actor) {
        CommunityMember membership = memberRepository
                .findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new SecurityException("Não pertences a esta comunidade"));

        if (membership.getRole() != CommunityRole.OWNER && membership.getRole() != CommunityRole.ADMIN) {
            throw new SecurityException("Apenas Owners e Admins podem executar esta ação");
        }
        return membership;
    }

    private CommunityMember requireOwner(Long communityId, Profile actor) {
        CommunityMember membership = memberRepository
                .findByCommunityIdAndProfileId(communityId, actor.getId())
                .orElseThrow(() -> new SecurityException("Não pertences a esta comunidade"));

        if (membership.getRole() != CommunityRole.OWNER) {
            throw new SecurityException("Apenas o Owner pode executar esta ação");
        }
        return membership;
    }

    private void requireInviteTarget(CommunityInvite invite, Profile profile) {
        if (!invite.getInvitedProfile().getId().equals(profile.getId())) {
            throw new SecurityException("Este convite não te pertence");
        }
    }

    private void requirePending(CommunityInvite invite) {
        if (invite.getStatus() != CommunityInviteStatus.PENDING) {
            throw new IllegalArgumentException("Este convite já não está pendente");
        }
    }

    private void requireJoinRequestCommunity(CommunityJoinRequest request, Long communityId) {
        if (!request.getCommunity().getId().equals(communityId)) {
            throw new IllegalArgumentException("Este pedido não pertence à comunidade");
        }
    }

    private void requireJoinRequestPending(CommunityJoinRequest request) {
        if (request.getStatus() != CommunityInviteStatus.PENDING) {
            throw new IllegalArgumentException("Este pedido já não está pendente");
        }
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
