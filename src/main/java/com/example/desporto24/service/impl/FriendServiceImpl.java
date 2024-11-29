package com.example.desporto24.service.impl;

import com.example.desporto24.exception.RequestFriendException;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.SendFriendRequest;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.register.FriendRequest.FriendRequest;
import com.example.desporto24.register.FriendRequest.FriendRequestService;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.service.interfaces.FriendService;
import com.example.desporto24.service.interfaces.NotificationService;
import com.example.desporto24.service.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.example.desporto24.service.EmailService;

import static com.example.desporto24.constant.UserImplConstant.ALREADY_FRIENDS;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final ProfileService profileService;
    private final FriendRequestService friendRequestService;
    private final EmailService emailService;
    private final FriendRepository friendRepository;
    private final NotificationService notificationService;

    @Override
    public void sendFriendRequest(SendFriendRequest friendRequest) {
        ProfileDTO profile1 = profileService.findUserByUsername(friendRequest.getUsername1());
        ProfileDTO profile2 = profileService.findUserByUsername(friendRequest.getUsername2());

        if (areAlreadyFriends(profile1, profile2)) {
            throw new RequestFriendException(ALREADY_FRIENDS);
        }

        String token = UUID.randomUUID().toString();
        String formattedDate = formatDate(new Date());

        FriendRequest newFriendRequest = new FriendRequest(token, formattedDate, profile1.getUsername(), profile2.getUsername());
        friendRequestService.saveFriendRequest(newFriendRequest);

        emailService.send(profile2.getEmail(), buildNewFriendRequestEmail(profile1.getUsername()));
        saveNotification(profile2.getUsername(), formattedDate, profile1.getUsername());
    }

    private boolean areAlreadyFriends(ProfileDTO profile1, ProfileDTO profile2) {
        return friendRepository.findByProfile1AndProfile2(profile1, profile2) != null;
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy HH:mm");
        return formatter.format(date);
    }

    private void saveNotification(String username, String date, String senderUsername) {
        String subject = "You have a new friend request!";
        String message = "You received a friend request from " + senderUsername + ". You can accept or reject this request.";
        String signature = "Best Regards, DESPORTO24";

        Notifications notification = new Notifications(subject, message, "Regards,", signature, date, true, false, false, false, UUID.randomUUID().toString(), username);
        notificationService.save(notification);
    }

    @Override
    public void blockFriend(String firstProfile, String secondProfile) {
        toggleFriendChatBlocked(firstProfile, secondProfile);
    }

    private void toggleFriendChatBlocked(String firstProfile, String secondProfile) {
        ProfileDTO profile1 = profileService.findUserByUsername(firstProfile);
        ProfileDTO profile2 = profileService.findUserByUsername(secondProfile);
        Friend friendConnection = friendRepository.findByProfile1AndProfile2(profile1, profile2);

        if (friendConnection != null) {
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
        ProfileDTO profile1 = profileService.findUserByUsername(firstProfile);
        ProfileDTO profile2 = profileService.findUserByUsername(secondProfile);
        Friend friendConnection = friendRepository.findByProfile1AndProfile2(profile1, profile2);

        if (friendConnection != null) {
            friendConnection.setChatBlocked(false);
            friendConnection.setWhoBlocked(null);
            friendRepository.save(friendConnection);
        }
    }

    @Override
    public void removeFriend(String firstProfile, String secondProfile) {
        ProfileDTO profile1 = profileService.findUserByUsername(firstProfile);
        ProfileDTO profile2 = profileService.findUserByUsername(secondProfile);
        Friend friendConnection = friendRepository.findByProfile1AndProfile2(profile1, profile2);

        if (friendConnection != null) {
            friendRepository.delete(friendConnection);
        }
    }

    @Override
    public List<ProfileDTO> getFriends(String username) {
        return List.of();
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
