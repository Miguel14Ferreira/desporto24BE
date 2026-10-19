package com.example.desporto24.service;

import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.enumeration.ParticipantState;
import com.example.desporto24.enumeration.SessionStatus;
import com.example.desporto24.exception.AccountDeletionBlockedException;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.model.social.SocialPost;
import com.example.desporto24.repository.ConfirmationTokenRepository;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.FriendRequestRepository;
import com.example.desporto24.repository.MFAVerificationRepository;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityInviteRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunityRepository;
import com.example.desporto24.repository.moderation.UserBlockRepository;
import com.example.desporto24.repository.session.SessionMessageRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.repository.session.SessionWaitlistRepository;
import com.example.desporto24.repository.social.ProfileFollowRepository;
import com.example.desporto24.repository.social.SocialPostCommentRepository;
import com.example.desporto24.repository.social.SocialPostLikeRepository;
import com.example.desporto24.repository.social.SocialPostRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.storage.MediaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDeletionService {

    private final ProfileRepository profileRepository;
    private final CommunityRepository communityRepository;
    private final CommunityInviteRepository communityInviteRepository;
    private final CommunityMemberRepository communityMemberRepository;
    private final ProfileFollowRepository profileFollowRepository;
    private final SocialPostRepository socialPostRepository;
    private final SocialPostCommentRepository socialPostCommentRepository;
    private final SocialPostLikeRepository socialPostLikeRepository;
    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository sessionParticipantRepository;
    private final SessionWaitlistRepository sessionWaitlistRepository;
    private final SessionMessageRepository sessionMessageRepository;
    private final WaitlistPromotionService waitlistPromotionService;
    private final SessionNotificationService sessionNotificationService;
    private final UserBlockRepository userBlockRepository;
    private final ChatRepository chatRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final NotificationsRepository notificationsRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MFAVerificationRepository mfaVerificationRepository;
    private final PasswordHashService passwordHashService;

    @Autowired(required = false)
    private MediaStorage mediaStorage;

    @Transactional
    public void deleteMyAccount(String username) {
        Profile profile = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        Long profileId = profile.getId();
        if (communityRepository.existsByOwnerId(profileId)) {
            throw new AccountDeletionBlockedException(
                    "Transfere primeiro a ownership das comunidades que criaste antes de eliminar a conta."
            );
        }

        String originalUsername = profile.getUsername();
        Instant now = Instant.now();
        String anonymousSuffix =
                profileId + "-" + UUID.randomUUID().toString().substring(0, 12);
        String anonymousUsername = "deleted-" + anonymousSuffix;
        String anonymousUserId = "deleted-" + UUID.randomUUID();

        Set<String> mediaKeys = new LinkedHashSet<>();
        addProfileMediaKey(profile.getProfilePicUrl(), mediaKeys);

        List<SocialPost> ownPosts = socialPostRepository.findByAuthorId(profileId);
        ownPosts.stream()
                .map(SocialPost::getImageStorageKey)
                .filter(key -> key != null && !key.isBlank())
                .forEach(mediaKeys::add);
        ownPosts.stream()
                .map(SocialPost::getVideoStorageKey)
                .filter(key -> key != null && !key.isBlank())
                .forEach(mediaKeys::add);

        cancelOwnedSessions(profile, anonymousUsername, anonymousUserId);
        leaveFutureSessions(profile, now);

        sessionWaitlistRepository.deleteByProfileId(profileId);
        sessionMessageRepository.deleteBySenderProfileId(profileId);

        communityInviteRepository.deleteByInvitedProfileIdOrInvitedById(
                profileId, profileId);
        communityMemberRepository.deleteByProfileId(profileId);

        profileFollowRepository.deleteByFollowerIdOrFollowingId(
                profileId, profileId);
        userBlockRepository.deleteByBlockerIdOrBlockedId(
                profileId, profileId);
        friendRepository.deleteByProfile1IdOrProfile2Id(
                profileId, profileId);

        chatRepository.deleteByUsername1OrUsername2(
                originalUsername, originalUsername);
        friendRequestRepository.deleteByPerfil1OrPerfil2(
                originalUsername, originalUsername);
        notificationsRepository.deleteByUsernameOrRelatedUsername(
                originalUsername, originalUsername);
        confirmationTokenRepository.deleteByProfile(originalUsername);
        mfaVerificationRepository.deleteByPerfil(originalUsername);

        for (SocialPost post : ownPosts) {
            socialPostLikeRepository.deleteByPostId(post.getId());
            socialPostCommentRepository.deleteByPostId(post.getId());
        }
        socialPostCommentRepository.deleteByAuthorId(profileId);
        socialPostLikeRepository.deleteByProfileId(profileId);
        socialPostRepository.deleteAll(ownPosts);

        anonymize(profile, anonymousUsername, anonymousUserId);
        profileRepository.saveAndFlush(profile);

        deleteMediaAfterCommit(mediaKeys);
    }

    private void cancelOwnedSessions(
            Profile profile,
            String anonymousUsername,
            String anonymousUserId) {

        for (Session session :
                sessionRepository.findByProfileIdOrderByGameDateAsc(profile.getId())) {
            if (session.getStatus() == SessionStatus.OPEN
                    || session.getStatus() == SessionStatus.FULL) {
                session.setStatus(SessionStatus.CANCELLED);
                session.setUpdatedAt(Instant.now());
                sessionNotificationService.notifySessionCancelled(session);
            }

            session.setUsername(anonymousUsername);
            session.setUserId(anonymousUserId);
            sessionRepository.save(session);
        }
    }

    private void leaveFutureSessions(Profile profile, Instant now) {
        List<SessionParticipant> activeMemberships =
                sessionParticipantRepository
                        .findByProfileIdAndStateOrderByJoinedAtAsc(
                                profile.getId(),
                                ParticipantState.ACTIVE
                        );

        for (SessionParticipant membership : activeMemberships) {
            Session session = membership.getSession();
            if (session.getGameDate() == null
                    || !session.getGameDate().toInstant().isAfter(now)) {
                continue;
            }

            membership.setState(ParticipantState.LEFT);
            membership.setExitedAt(now);
            sessionParticipantRepository.save(membership);

            boolean ownedByDeletedProfile = session.getProfile() != null
                    && session.getProfile().getId().equals(profile.getId());

            if (!ownedByDeletedProfile
                    && (session.getStatus() == SessionStatus.OPEN
                    || session.getStatus() == SessionStatus.FULL)) {
                waitlistPromotionService.promoteNext(session, now);
            }
        }
    }

    private void anonymize(
            Profile profile,
            String anonymousUsername,
            String anonymousUserId) {

        profile.setUsername(anonymousUsername);
        profile.setNewUsername(null);
        profile.setUserId(anonymousUserId);
        profile.setPassword(
                passwordHashService.encode(UUID.randomUUID().toString()));

        profile.setFullName(null);
        profile.setNickname(null);
        profile.setDateOfBirth(null);
        profile.setAddress(null);
        profile.setCountry(null);
        profile.setLocation(null);
        profile.setPostalCode(null);
        profile.setIndicativePhone(null);
        profile.setPhone(null);
        profile.setGender(null);
        profile.setEmail(anonymousUsername + "@deleted.invalid");
        profile.setFavoriteSports(null);
        profile.setBio(null);
        profile.setSkillLevel(null);
        profile.setAvailability(null);
        profile.setProfilePicUrl(null);

        profile.setLoginAttempts(0);
        profile.setNotLocked(false);
        profile.setModerationSuspendedAt(null);
        profile.setEnabled(false);
        profile.setMFA(false);
        profile.setMfaSecret(null);
        profile.setGoogleSubject(null);
        profile.setRole(null);
        profile.setAuthorities(new String[0]);

        profile.setLastLoginDateDisplay(null);
        profile.setJoinDateDisplay(null);
        profile.setLastLoginDate(null);
        profile.setJoinDate(null);
        profile.setOldEmail(null);

        profile.setTermsAcceptedAt(null);
        profile.setTermsVersion(null);
        profile.setPrivacyVersion(null);

        profile.setTokenVersion(profile.getTokenVersion() + 1);
    }

    private void addProfileMediaKey(String profilePicUrl, Set<String> keys) {
        if (profilePicUrl == null || profilePicUrl.isBlank()) {
            return;
        }

        int index = profilePicUrl.indexOf("/profiles/");
        if (index >= 0) {
            keys.add(profilePicUrl.substring(index + 1));
            return;
        }

        index = profilePicUrl.indexOf("profiles/");
        if (index >= 0) {
            keys.add(profilePicUrl.substring(index));
        }
    }

    private void deleteMediaAfterCommit(Set<String> mediaKeys) {
        if (mediaStorage == null || mediaKeys.isEmpty()) {
            return;
        }

        Runnable deletion = () -> mediaKeys.forEach(key -> {
            try {
                mediaStorage.delete(key);
            } catch (RuntimeException exception) {
                log.warn("Could not delete account media {}", key, exception);
            }
        });

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            deletion.run();
                        }
                    }
            );
        } else {
            deletion.run();
        }
    }
}
