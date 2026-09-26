package com.example.desporto24.service.impl;

import com.example.desporto24.exception.RequestFriendException;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.SendFriendRequest;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.friend.FriendSummaryResponse;
import com.example.desporto24.register.FriendRequest.FriendRequest;
import com.example.desporto24.register.FriendRequest.FriendRequestService;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.interfaces.FriendService;
import com.example.desporto24.service.interfaces.NotificationService;
import com.example.desporto24.service.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.example.desporto24.service.EmailService;

import static com.example.desporto24.constant.UserImplConstant.ALREADY_FRIENDS;
import static com.example.desporto24.constant.UserImplConstant.FRIEND_REQUEST_ALREADY_PENDING;
import static com.example.desporto24.constant.UserImplConstant.FRIEND_REQUEST_SELF;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final ProfileService profileService;
    private final FriendRequestService friendRequestService;
    private final EmailService emailService;
    private final FriendRepository friendRepository;
    private final NotificationService notificationService;
    private final ProfileRepository profileRepository;

    @Override
    public void sendFriendRequest(SendFriendRequest friendRequest) {
        String senderUsername = friendRequest.getUsername1();
        String recipientUsername = friendRequest.getUsername2();

        if (senderUsername != null && senderUsername.equals(recipientUsername)) {
            throw new RequestFriendException(FRIEND_REQUEST_SELF);
        }

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

        emailService.send(profile2.getEmail(), buildNewFriendRequestEmail(profile1.getUsername()));
        saveNotification(profile2.getUsername(), formattedDate, profile1.getUsername(), token);
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
        String signature = "Cumprimentos, DESPORTO24";

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
        return friendRepository.findByProfile1OrProfile2(profile, profile).stream()
                .map(friend -> otherProfile(friend, profile))
                .toList();
    }

    @Override
    public List<FriendSummaryResponse> getFriendSummaries(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));

        return friendRepository.findByProfile1OrProfile2(profile, profile).stream()
                .map(friend -> {
                    Profile other = otherProfile(friend, profile);
                    return new FriendSummaryResponse(
                            other.getUsername(),
                            other.getFullName(),
                            other.getLocation(),
                            other.getFavoriteSports(),
                            other.getProfilePicUrl(),
                            friend.isChatBlocked(),
                            username.equals(friend.getWhoBlocked())
                    );
                })
                .toList();
    }

    private Profile otherProfile(Friend friend, Profile profile) {
        return friend.getProfile1().getId().equals(profile.getId())
                ? friend.getProfile2()
                : friend.getProfile1();
    }

    private Friend findFriendConnection(Profile first, Profile second) {
        return friendRepository.findByProfile1OrProfile2(first, first).stream()
                .filter(friend ->
                        (friend.getProfile1().getId().equals(first.getId())
                                && friend.getProfile2().getId().equals(second.getId()))
                        || (friend.getProfile1().getId().equals(second.getId())
                                && friend.getProfile2().getId().equals(first.getId())))
                .findFirst()
                .orElse(null);
    }

    private String buildNewFriendRequestEmail(String name) {
        return
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                        "\n" +
                        "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "    <tbody><tr>\n" +
                        "        \n" +
                        "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "          <tbody><tr>\n" +
                        "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td style=\"padding-left:10px\">\n" +
                        "                  \n" +
                        "                    </td>\n" +
                        "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Recebeste um novo pedido de amizade</span>\n" +
                        "                    </td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "              </a>\n" +
                        "            </td>\n" +
                        "          </tr>\n" +
                        "        </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                        "      <td>\n" +
                        "        \n" +
                        "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                        "        \n" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Foi te enviado um novo pedido de amizade por " + name + ".</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Para aceitar basta simplesmente dirigires-te à app. <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cumprimentos,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">DESPORTO24APP</p>" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                        "\n" +
                        "</div></div>";
    }
}
