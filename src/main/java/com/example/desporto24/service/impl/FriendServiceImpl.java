package com.example.desporto24.service.impl;

import com.example.desporto24.exception.RequestFriendException;
import com.example.desporto24.email.TransactionalEmailTemplate;
import org.springframework.beans.factory.annotation.Value;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.SendFriendRequest;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.friend.FriendSummaryResponse;
import com.example.desporto24.model.dto.friend.FriendshipStatusResponse;
import com.example.desporto24.register.FriendRequest.FriendRequest;
import com.example.desporto24.register.FriendRequest.FriendRequestService;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.interfaces.FriendService;
import com.example.desporto24.service.interfaces.NotificationService;
import com.example.desporto24.service.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.UserBlockService;

import static com.example.desporto24.constant.UserImplConstant.ALREADY_FRIENDS;
import static com.example.desporto24.constant.UserImplConstant.FRIEND_REQUEST_ALREADY_PENDING;
import static com.example.desporto24.constant.UserImplConstant.FRIEND_REQUEST_SELF;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService {

    private final ProfileService profileService;
    private final FriendRequestService friendRequestService;
    private final EmailService emailService;
    private final FriendRepository friendRepository;
    private final NotificationService notificationService;
    private final ProfileRepository profileRepository;
    private final UserBlockService userBlockService;

    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private com.example.desporto24.service.NotificationPreferencesService preferenceService;

    @Value("${ui.app.url}")
    private String uiAppUrl;

    @Override
    public void sendFriendRequest(SendFriendRequest friendRequest) {
        String senderUsername = friendRequest.getUsername1();
        String recipientUsername = friendRequest.getUsername2();

        if (senderUsername != null && senderUsername.equals(recipientUsername)) {
            throw new RequestFriendException(FRIEND_REQUEST_SELF);
        }

        userBlockService.ensureInteractionAllowed(senderUsername, recipientUsername);

        ProfileDTO profile1 = profileService.findUserByUsername(senderUsername);
        ProfileDTO profile2 = profileService.findUserByUsername(recipientUsername);

        if (profile1.getUsername().equals(profile2.getUsername())) {
            throw new RequestFriendException(FRIEND_REQUEST_SELF);
        }

        if (areAlreadyFriends(profile1, profile2)) {
            throw new RequestFriendException(ALREADY_FRIENDS);
        }

        if (friendRequestService.hasPendingRequestBetween(
                profile1.getUsername(),
                profile2.getUsername())) {
            throw new RequestFriendException(FRIEND_REQUEST_ALREADY_PENDING);
        }

        String token = UUID.randomUUID().toString();
        String formattedDate = formatDate(new Date());

        FriendRequest newFriendRequest = new FriendRequest(token, formattedDate, profile1.getUsername(), profile2.getUsername());
        friendRequestService.saveFriendRequest(newFriendRequest);

        // The request and its in-app action must not depend on optional email delivery.
        saveNotification(profile2.getUsername(), formattedDate, profile1.getUsername(), token);

        if (preferenceService == null || preferenceService.allowsOptionalEmailCategory(
                profile2.getUsername(), com.example.desporto24.service.NotificationPreferencesService.FRIEND_REQUEST)) {
            try {
                emailService.send(profile2.getEmail(), "Pedido de amizade | Friend request — Jynlo",
                        buildNewFriendRequestEmail(profile1.getUsername()));
            } catch (RuntimeException emailFailure) {
                // The persisted request remains actionable even if Resend is unavailable.
                log.warn("Optional friend request email failed; the friendship request remains pending",
                        emailFailure);
            }
        }
    }

    private boolean areAlreadyFriends(ProfileDTO profile1, ProfileDTO profile2) {
        Profile first = profileRepository.findUserByUsername(profile1.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        Profile second = profileRepository.findUserByUsername(profile2.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        return findFriendConnection(first, second) != null;
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy HH:mm");
        return formatter.format(date);
    }

    private void saveNotification(
            String username,
            String date,
            String senderUsername,
            String friendRequestToken) {
        String subject = "Tu tens um novo pedido de amizade!";
        String message = "Olá, tu recebeste um novo pedido de amizade vindo de " + senderUsername + ". Tu podes aceitar ou rejeitar este pedido.";
        String signature = "Cumprimentos, Jynlo";

        Notifications notification = new Notifications(
                subject,
                message,
                "Regards,",
                signature,
                date,
                true,
                false,
                false,
                false,
                friendRequestToken,
                username
        );
        notificationService.save(notification);
    }

    @Override
    public void blockFriend(String firstProfile, String secondProfile) {
        toggleFriendChatBlocked(firstProfile, secondProfile);
    }

    private void toggleFriendChatBlocked(String firstProfile, String secondProfile) {
        Profile profile1 = profileRepository.findUserByUsername(firstProfile)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        Profile profile2 = profileRepository.findUserByUsername(secondProfile)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        Friend friendConnection = findFriendConnection(profile1, profile2);

        if (friendConnection != null) {
            if (friendConnection.isChatBlocked()) {
                if (!profile1.getUsername().equals(friendConnection.getWhoBlocked())) {
                    throw new AccessDeniedException(
                            "O chat foi bloqueado pelo outro utilizador"
                    );
                }
                return;
            }

            friendConnection.setChatBlocked(true);
            friendConnection.setWhoBlocked(profile1.getUsername());
            friendRepository.save(friendConnection);
        }
    }

    @Override
    public void unblockFriend(String firstProfile, String secondProfile) {
        toggleFriendChatUnblocked(firstProfile, secondProfile);
    }

    private void toggleFriendChatUnblocked(String firstProfile, String secondProfile) {
        Profile profile1 = profileRepository.findUserByUsername(firstProfile)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        Profile profile2 = profileRepository.findUserByUsername(secondProfile)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        Friend friendConnection = findFriendConnection(profile1, profile2);

        if (friendConnection != null) {
            if (!friendConnection.isChatBlocked()) {
                return;
            }

            if (!profile1.getUsername().equals(friendConnection.getWhoBlocked())) {
                throw new AccessDeniedException(
                        "Apenas quem bloqueou o chat pode desbloqueá-lo"
                );
            }

            friendConnection.setChatBlocked(false);
            friendConnection.setWhoBlocked(null);
            friendRepository.save(friendConnection);
        }
    }

    @Override
    public void removeFriend(String firstProfile, String secondProfile) {
        Profile profile1 = profileRepository.findUserByUsername(firstProfile)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        Profile profile2 = profileRepository.findUserByUsername(secondProfile)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        Friend friendConnection = findFriendConnection(profile1, profile2);

        if (friendConnection != null) {
            friendRepository.delete(friendConnection);
        }
    }

    @Override
    public List<Profile> getFriends(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        return friendRepository.findConnectionsForProfile(profile.getId()).stream()
                .map(friend -> otherProfile(friend, profile))
                .toList();
    }

    @Override
    public List<FriendSummaryResponse> getFriendSummaries(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));

        return friendRepository.findConnectionsForProfile(profile.getId()).stream()
                .map(friend -> {
                    Profile other = otherProfile(friend, profile);
                    return new FriendSummaryResponse(
                            other.getUsername(),
                            other.getFullName(),
                            other.getNickname(),
                            other.getProfilePicUrl(),
                            friend.isChatBlocked(),
                            username.equals(friend.getWhoBlocked())
                    );
                })
                .toList();
    }

    @Override
    public FriendshipStatusResponse getFriendshipStatus(String requester, String recipient) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(requester)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        Profile target = profileRepository.findUserByUsernameIgnoreCase(recipient)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));
        if (actor.getId().equals(target.getId())) {
            return new FriendshipStatusResponse(false, false);
        }
        boolean friends = findFriendConnection(actor, target) != null;
        boolean pending = !friends && friendRequestService.hasPendingRequestBetween(
                actor.getUsername(), target.getUsername());
        return new FriendshipStatusResponse(friends, pending);
    }

    private Profile otherProfile(Friend friend, Profile profile) {
        return friend.getProfile1().getId().equals(profile.getId())
                ? friend.getProfile2()
                : friend.getProfile1();
    }

    private Friend findFriendConnection(Profile first, Profile second) {
        return friendRepository.findConnection(first.getId(), second.getId())
                .orElse(null);
    }

    private String buildNewFriendRequestEmail(String name) {
        String link = uiAppUrl.replaceAll("/+$", "") + "/menu/notifications";
        return TransactionalEmailTemplate.render(
                "Recebeste um pedido de amizade",
                "@" + name + " enviou-te um pedido de amizade. Entra na tua conta para aceitar ou rejeitar o pedido.",
                "Friend request",
                "@" + name + " sent you a friend request. Sign in to accept or reject the request.",
                "Ver pedido / View request",
                link,
                "O pedido também está disponível nas Notificações da app.",
                "You can also find the request in Notifications in the app."
        );
    }
}
