package com.example.desporto24.service;

import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.social.FollowProfileResponse;
import com.example.desporto24.model.dto.social.FollowStatusResponse;
import com.example.desporto24.model.social.ProfileFollow;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.social.ProfileFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileFollowService {

    private final ProfileFollowRepository followRepository;
    private final ProfileRepository profileRepository;
    private final UserBlockService userBlockService;

    public FollowStatusResponse follow(String actorUsername, String targetUsername) {
        Profile actor = profile(actorUsername);
        Profile target = profile(targetUsername);

        if (actor.getId().equals(target.getId())) {
            throw new IllegalArgumentException("Não podes seguir o teu próprio perfil");
        }

        userBlockService.ensureInteractionAllowed(actor, target);

        if (!followRepository.existsByFollowerIdAndFollowingId(
                actor.getId(), target.getId())) {
            followRepository.save(ProfileFollow.builder()
                    .follower(actor)
                    .following(target)
                    .createdAt(Instant.now())
                    .build());
        }

        return status(actor, target);
    }

    public FollowStatusResponse unfollow(String actorUsername, String targetUsername) {
        Profile actor = profile(actorUsername);
        Profile target = profile(targetUsername);

        followRepository.deleteByFollowerIdAndFollowingId(
                actor.getId(), target.getId());

        return status(actor, target);
    }

    @Transactional(readOnly = true)
    public FollowStatusResponse status(String actorUsername, String targetUsername) {
        Profile actor = profile(actorUsername);
        Profile target = profile(targetUsername);
        userBlockService.ensureInteractionAllowed(actor, target);
        return status(actor, target);
    }

    @Transactional(readOnly = true)
    public List<FollowProfileResponse> following(String username) {
        Profile profile = profile(username);

        return followRepository.findByFollowerIdOrderByCreatedAtDesc(
                        profile.getId(),
                        PageRequest.of(0, 200)
                )
                .stream()
                .map(ProfileFollow::getFollowing)
                .map(this::toProfile)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FollowProfileResponse> followers(String username) {
        Profile profile = profile(username);

        return followRepository.findByFollowingIdOrderByCreatedAtDesc(
                        profile.getId(),
                        PageRequest.of(0, 200)
                )
                .stream()
                .map(ProfileFollow::getFollower)
                .map(this::toProfile)
                .toList();
    }

    private FollowStatusResponse status(Profile actor, Profile target) {
        return new FollowStatusResponse(
                !actor.getId().equals(target.getId())
                        && followRepository.existsByFollowerIdAndFollowingId(
                        actor.getId(), target.getId()),
                !actor.getId().equals(target.getId())
                        && followRepository.existsByFollowerIdAndFollowingId(
                        target.getId(), actor.getId()),
                followRepository.countByFollowingId(target.getId()),
                followRepository.countByFollowerId(target.getId())
        );
    }

    private FollowProfileResponse toProfile(Profile profile) {
        return new FollowProfileResponse(
                profile.getUsername(),
                profile.getFullName(),
                profile.getNickname(),
                profile.getProfilePicUrl()
        );
    }

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
    }
}
