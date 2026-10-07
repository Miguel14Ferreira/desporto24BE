package com.example.desporto24.service;

import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.moderation.UserBlockStatusResponse;
import com.example.desporto24.model.moderation.UserBlock;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.moderation.UserBlockRepository;
import com.example.desporto24.repository.social.ProfileFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
public class UserBlockService {

    private final UserBlockRepository userBlockRepository;
    private final ProfileRepository profileRepository;
    private final FriendRepository friendRepository;
    private final ProfileFollowRepository followRepository;

    @Transactional
    public UserBlockStatusResponse blockUser(String actorUsername, String targetUsername) {
        Profile actor = profile(actorUsername);
        Profile target = profile(targetUsername);

        if (actor.getId().equals(target.getId())) {
            throw new IllegalArgumentException("Não podes bloquear o teu próprio perfil");
        }

        if (!userBlockRepository.existsByBlockerIdAndBlockedId(actor.getId(), target.getId())) {
            userBlockRepository.save(UserBlock.builder()
                    .blocker(actor)
                    .blocked(target)
                    .createdAt(Instant.now())
                    .build());
        }

        removeSocialConnection(actor, target);
        return new UserBlockStatusResponse(true);
    }

    @Transactional
    public UserBlockStatusResponse unblockUser(String actorUsername, String targetUsername) {
        Profile actor = profile(actorUsername);
        Profile target = profile(targetUsername);

        if (actor.getId().equals(target.getId())) {
            throw new IllegalArgumentException("Não podes desbloquear o teu próprio perfil");
        }

        userBlockRepository.deleteByBlockerIdAndBlockedId(actor.getId(), target.getId());
        return new UserBlockStatusResponse(false);
    }

    @Transactional(readOnly = true)
    public UserBlockStatusResponse status(String actorUsername, String targetUsername) {
        Profile actor = profile(actorUsername);
        Profile target = profile(targetUsername);

        if (actor.getId().equals(target.getId())) {
            return new UserBlockStatusResponse(false);
        }

        return new UserBlockStatusResponse(
                userBlockRepository.existsByBlockerIdAndBlockedId(actor.getId(), target.getId())
        );
    }

    @Transactional(readOnly = true)
    public Set<Long> blockedProfileIds(Profile profile) {
        return userBlockRepository.findBlockedProfileIds(profile.getId());
    }

    @Transactional(readOnly = true)
    public boolean isBlockedBetween(Profile first, Profile second) {
        if (first.getId().equals(second.getId())) {
            return false;
        }
        return userBlockRepository.existsBetween(first.getId(), second.getId());
    }

    @Transactional(readOnly = true)
    public void ensureInteractionAllowed(Profile first, Profile second) {
        if (isBlockedBetween(first, second)) {
            throw new AccessDeniedException("Interação indisponível porque existe um bloqueio entre os utilizadores");
        }
    }

    @Transactional(readOnly = true)
    public void ensureInteractionAllowed(String firstUsername, String secondUsername) {
        ensureInteractionAllowed(profile(firstUsername), profile(secondUsername));
    }

    private void removeSocialConnection(Profile actor, Profile target) {
        followRepository.deleteByFollowerIdAndFollowingId(actor.getId(), target.getId());
        followRepository.deleteByFollowerIdAndFollowingId(target.getId(), actor.getId());

        friendRepository.findByProfile1OrProfile2(actor, actor).stream()
                .filter(friend -> connects(friend, actor, target))
                .findFirst()
                .ifPresent(friendRepository::delete);
    }

    private boolean connects(Friend friend, Profile first, Profile second) {
        return (friend.getProfile1().getId().equals(first.getId())
                && friend.getProfile2().getId().equals(second.getId()))
                || (friend.getProfile1().getId().equals(second.getId())
                && friend.getProfile2().getId().equals(first.getId()));
    }

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
    }
}
