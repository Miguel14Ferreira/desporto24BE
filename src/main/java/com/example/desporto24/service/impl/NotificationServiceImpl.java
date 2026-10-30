package com.example.desporto24.service.impl;

import com.example.desporto24.exception.NotificationNotFoundException;
import com.example.desporto24.exception.TokenExpiredException;
import com.example.desporto24.exception.TokenNotFoundException;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Friend;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Status;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.register.FriendRequest.FriendRequest;
import com.example.desporto24.register.FriendRequest.FriendRequestService;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.register.token.ConfirmationToken;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.UserBlockService;
import com.example.desporto24.service.PostGameReminderService;
import com.example.desporto24.service.interfaces.NotificationService;
import com.example.desporto24.service.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.desporto24.constant.UserImplConstant.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationsRepository notificationsRepository;
    private final EmailService emailService;
    private final ProfileService profileService;
    private final ConfirmationTokenService confirmationTokenService;
    private final FriendRequestService friendRequestService;
    private final FriendRepository friendRepository;
    private final ProfileRepository profileRepository;
    private final UserBlockService userBlockService;
    private final PostGameReminderService postGameReminderService;

    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private com.example.desporto24.service.NotificationPreferencesService preferenceService;

    @Value("${ui.app.url}")
    private String uiAppUrl;

    @Override
    public List<Notifications> getNotificationsFromProfile(String username) {
        postGameReminderService.createDueReminders(username);
        List<Notifications> records = notificationsRepository.findByUsername(username);
        if (preferenceService == null) return records;
        var prefs = preferenceService.get(username);
        if (prefs.inAppEnabled() && prefs.disabledInAppTypes().isEmpty()) return records;
        return records.stream().filter(n ->
                com.example.desporto24.service.NotificationPreferencesService.isAccountSecurityAlert(n)
                || (prefs.inAppEnabled() && !prefs.disabledInAppTypes().contains(
                        com.example.desporto24.service.NotificationPreferencesService.category(n)))
        ).toList();
    }

    @Override
    public long countUnreadNotifications(String username) {
        postGameReminderService.createDueReminders(username);
        if (preferenceService == null) return notificationsRepository.countByUsernameAndReadFalse(username);
        var prefs = preferenceService.get(username);
        if (prefs.inAppEnabled() && prefs.disabledInAppTypes().isEmpty()) {
            return notificationsRepository.countByUsernameAndReadFalse(username);
        }
        return notificationsRepository.findByUsername(username).stream()
                .filter(n -> !n.isRead() && (
                        com.example.desporto24.service.NotificationPreferencesService.isAccountSecurityAlert(n)
                        || (prefs.inAppEnabled() && !prefs.disabledInAppTypes().contains(
                                com.example.desporto24.service.NotificationPreferencesService.category(n)))
                )).count();
    }

    @Override
    public void disableProfileAndSendEmail(String email, String authenticatedUsername) {
        ProfileDTO profileDTO = getUserByEmailOrThrow(email);
        if (!authenticatedUsername.equals(profileDTO.getUsername())) {
            throw new AccessDeniedException("Cannot disable another user's account");
        }
        blockOwnAccount(authenticatedUsername);
    }

    @Override
    public void blockOwnAccount(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (Boolean.FALSE.equals(profile.getNotLocked())) {
            return;
        }

        profile.setNotLocked(false);
        profile.setStatusId(Status.OFFLINE);
        profile.setTokenVersion(profile.getTokenVersion() + 1);
        profileRepository.save(profile);

        String token = createConfirmationToken(profile.getUsername());
        String link = uiAppUrl.replaceAll("/+$", "")
                + "/login/resetPassword/" + token;
        emailService.send(
                profile.getEmail(),
                buildAccountLockedRecoveryEmail(profile.getUsername(), link)
        );
    }

    private String createConfirmationToken(String username) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                username
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    private ProfileDTO getUserByEmailOrThrow(String email) {
        return profileService.findUserByEmail(email);
    }

    @Override
    public void acceptFriendRequest(Long id, String username) {
        Notifications notification = getOwnedNotification(id, username);
        FriendRequest friendRequest = getFriendRequestFromNotification(notification, username);

        friendRequestService.setConfirmedAt(friendRequest.getToken());
        createFriendRelationship(friendRequest.getPerfil1(), friendRequest.getPerfil2());
        notificationsRepository.delete(notification);
    }

    @Override
    public void rejectFriendRequest(Long id, String username) {
        Notifications notification = getOwnedNotification(id, username);
        FriendRequest friendRequest = getFriendRequestFromNotification(notification, username);

        friendRequestService.deleteFriendRequest(friendRequest);
        notificationsRepository.delete(notification);
    }

    @Override
    public void acceptFriendRequestByToken(Long id, String token) {
        FriendRequest friendRequest = getValidFriendRequestToken(token);
        notificationsRepository.deleteById(id);
        friendRequestService.setConfirmedAt(token);

        createFriendRelationship(friendRequest.getPerfil1(), friendRequest.getPerfil2());
    }

    private FriendRequest getFriendRequestFromNotification(
            Notifications notification,
            String username) {
        if (!notification.isFriendRequest()
                || notification.getToken() == null
                || notification.getToken().isBlank()) {
            throw new AccessDeniedException("Notification is not an actionable friend request");
        }

        FriendRequest friendRequest = getValidFriendRequestToken(notification.getToken());
        if (!username.equals(friendRequest.getPerfil2())) {
            throw new AccessDeniedException("Friend request does not belong to authenticated user");
        }

        return friendRequest;
    }

    @Override
    public void save(Notifications notifications) {
        if (preferenceService == null
                || preferenceService.allowsInApp(notifications.getUsername(), notifications)) {
            notificationsRepository.save(notifications);
        }
    }

    @Override
    public void deleteNotification(Notifications notifications) {
        Notifications notifications1 = notificationsRepository.findByToken(notifications.getToken()).orElseThrow(() -> new NotificationNotFoundException(NO_NOTIFICATION_FOUND));
        notificationsRepository.delete(notifications1);
    }

    @Override
    public void deleteNotification(Long id, String username) {
        Notifications notification = getOwnedNotification(id, username);
        notificationsRepository.delete(notification);
    }

    @Override
    public void deleteAllNotifications(String username) {
        notificationsRepository.deleteByUsername(username);
    }

    @Override
    public void markRead(Long id, String username) {
        Notifications notification = getOwnedNotification(id, username);
        if (!notification.isRead()) {
            notification.setRead(true);
            notificationsRepository.save(notification);
        }
    }

    private Notifications getOwnedNotification(Long id, String username) {
        Notifications notification = notificationsRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(NO_NOTIFICATION_FOUND));

        if (notification.getUsername() == null
                || !notification.getUsername().equals(username)) {
            throw new AccessDeniedException("Notification does not belong to authenticated user");
        }

        return notification;
    }

    private void createFriendRelationship(String username1, String username2) {
        Profile profile1 = profileRepository.findUserByUsername(username1).orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        Profile profile2 = profileRepository.findUserByUsername(username2).orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        userBlockService.ensureInteractionAllowed(profile1, profile2);

        Friend friend = new Friend();
        friend.setCreatedAt(new Date());
        friend.setProfile1(profile1);
        friend.setProfile2(profile2);
        friend.setChatBlocked(false);
        friendRepository.save(friend);
    }

    private FriendRequest getValidFriendRequestToken(String token) {
        FriendRequest friendRequest = friendRequestService
                .getToken(token)
                .orElseThrow(() -> new TokenNotFoundException(TOKEN_NOT_FOUND));
        if (friendRequest.getConfirmedAt() != null) {
            throw new TokenExpiredException(TOKEN_EXPIRED);
        }
        return friendRequest;
    }

    private String buildAccountLockedRecoveryEmail(String name, String link) {
        return "<h2>Acessos à conta bloqueados</h2>"
                + "<p>Olá " + name + ",</p>"
                + "<p>Os acessos à tua conta JYNLO foram bloqueados por motivos de segurança.</p>"
                + "<p>Todos os tokens de sessão anteriores deixaram de ser válidos.</p>"
                + "<p>Para recuperares o acesso, define uma nova palavra-passe através deste link:</p>"
                + "<p><a href=\"" + link + "\">Recuperar e desbloquear conta</a></p>"
                + "<p>O link expira em 15 minutos.</p>"
                + "<p>Cumprimentos,<br>JYNLO MANAGEMENT TEAM</p>";
    }

    private String buildEmergencyResetPasswordMail(String name, String link) {
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
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Jynlo</span>\n" +
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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + " (se não reconheces este username, é porque provavelmente foi alterado),</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Pedimos-te agora para definires uma nova palavra-passe através deste link: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Clica aqui para alterares a tua palavra-passe</a> </p> <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Recomendamos-te que depois de alterares a tua palavra-passe que entres na tua conta e verifica se outros dados foram alterados para que tu possas alterá-los.</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Atenção, este link vai expirar dentro de 15 minutos.</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cumprimentos,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">JYNLOAPP</p>" +
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