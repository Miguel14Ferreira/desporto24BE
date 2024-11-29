package com.example.desporto24.service.impl;

import com.example.desporto24.exception.*;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.register.MFA.MFAVerification;
import com.example.desporto24.register.MFA.MFAVerificationService;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.register.token.ConfirmationToken;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.LoginAttemptService;
import com.example.desporto24.service.interfaces.AuthenticationService;
import com.example.desporto24.service.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static com.example.desporto24.constant.FileConstant.*;
import static com.example.desporto24.constant.UserImplConstant.*;
import static com.example.desporto24.encryption.AES.decryptString;
import static com.example.desporto24.model.Status.OFFLINE;
import static com.example.desporto24.utility.ProfileUtils.getLoggedInUser;
import static com.example.desporto24.utility.SmsUtils.sendSMS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final LoginAttemptService loginAttemptService;
    private final ProfileService profileService;
    private final AuthenticationManager authenticationManager;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final NotificationsRepository notificationsRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MFAVerificationService mfaVerificationService;



    @Override
    public Profile authenticate(ProfileDTO profileDTO) {
        Profile profile = (Profile) profileService.loadUserByUsername(profileDTO.getUsername());

        Authentication authentication = Boolean.FALSE.equals(profile.getMFA())
                ? handleRegularLogin(profile.getUsername(), profile.getPassword(), profile)
                : handleMFALogin(profileDTO.getUsername(), profile.getPassword());

        return getLoggedInUser(authentication);
    }
    private Authentication handleRegularLogin(String username, String password, Profile profile) {
        loginAttemptService.hasExceededMaxAttempts(profile);
        loginAttemptService.addUserToLoginAttemptCache(profile);
        Authentication authentication = authenticationManager.authenticate(unauthenticated(username, password));
        loginAttemptService.evictUserFromLoginAttemptCache(profile);
        return authentication;
    }

    private Authentication handleMFALogin(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(unauthenticated(username, password));
        sendVerificationCode(username);
        return authentication;
    }

    @Override
    public void terminateSession(String email) {
        ProfileDTO profileDTO = profileService.findUserByEmail(email);
        profileDTO.setStatusId(OFFLINE);
        profileService.save(profileDTO);
    }

    @Override
    public void signUpProfile(ProfileDTO profileDTO, MultipartFile photo) {
        validateNewUsernameEmail(EMPTY, profileDTO.getUsername(), profileDTO.getEmail());
        Profile profile = profileMapper.toEntity(profileDTO);
        saveProfileImage(profile, photo);
        profileRepository.save(profile);
        sendSignUpEmail(profile);
    }

    private void saveProfileImage(Profile profile, MultipartFile profileImage) {
        if (profileImage != null) {
            validateImageFileType(profileImage.getContentType());
            Path userFolder = createUserFolder(profile.getUsername());
            deleteExistingProfileImage(userFolder, profile.getUsername());
            saveNewProfileImage(userFolder, profile.getUsername(), profileImage);
            profile.setProfilePic(setProfileImageUrl(profile.getUsername()));
            profileRepository.save(profile);
        } else {
            setDefaultProfileImageIfNeeded(profile);
        }
    }

    private void setDefaultProfileImageIfNeeded(Profile profile) {
        if (profile.getProfilePic() == null) {
            profile.setProfilePic(getTemporaryProfileImageURL(profile.getUsername()));
        }
    }

    private String getTemporaryProfileImageURL(String username) {
        return fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private String setProfileImageUrl(String username) {
        return fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
    }

    private void saveNewProfileImage(Path userFolder, String username, MultipartFile profileImage) {
        try {
            Files.copy(profileImage.getInputStream(), userFolder.resolve(username + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            log.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteExistingProfileImage(Path userFolder, String username) {
        try {
            Files.deleteIfExists(Paths.get(userFolder + username + DOT + JPG_EXTENSION));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path createUserFolder(String username) {
        try {
            Path userFolder = Paths.get(USER_FOLDER + username).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                log.info(DIRECTORY_CREATED + userFolder);
            }
            return userFolder;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendSignUpEmail(Profile profile)  {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), profile.getUsername()
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = fromCurrentContextPath().path("/login/registerNewUser/confirmTokenRegistration/" + token).toUriString();
        emailService.send(profile.getEmail(), buildRegistrationEmail(profile.getUsername(), link));

        createWelcomeNotification(profile, token);
    }

    private void createWelcomeNotification(Profile profile, String token) {
        Date date = new Date();
        String formattedDate = formatDate2(date);

        String subject = "Welcome to our site, " + profile.getUsername() + "!";
        String message = "Here you can check the sessions happening right now. To create a session or update your profile, hover over your photo in the top right corner.";
        String greeting = "Best regards,";
        String signature = "DESPORTO24";

        Notifications notification = new Notifications(
                subject, message, greeting, signature, formattedDate, false, false, false, true, token, profile.getUsername()
        );

        notificationsRepository.save(notification);
    }

    private String formatDate2(Date date) {
        String dayMonth = substring(String.valueOf(date), 3, 10);
        String year = substring(String.valueOf(date), 24, 29);
        return year + dayMonth;
    }

    @SneakyThrows
    public void validateNewUsernameEmail(String currentUsername, String newUsername, String email) {
        ProfileDTO userByNewUsername = profileService.findUserByUsername(newUsername);
        ProfileDTO userByNewEmail = profileService.findUserByEmail(email);

        if (isNotBlank(currentUsername)) {
            ProfileDTO currentUser = profileService.findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME);
            }

            if (userByNewUsername != null && !currentUser.getUserId().equals(userByNewUsername.getUserId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXIST);
            }

            if (userByNewEmail != null && !currentUser.getUserId().equals(userByNewEmail.getUserId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXIST);
            }

            return;
        }

        if (userByNewUsername != null) {
            throw new UsernameExistException(USERNAME_ALREADY_EXIST);
        }

        if (userByNewEmail != null) {
            throw new EmailExistException(EMAIL_ALREADY_EXIST);
        }

    }

    private void validateImageFileType(String contentType) {
        if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(contentType)) {
            throw new NotAnImageFileException(NOT_AN_IMAGE_FILE);
        }
    }

    @Override
    public void resetPasswordStepOne(String email) {
        ProfileDTO profileDTO = profileService.findUserByEmail(email);
        String token = createAndSaveConfirmationToken(profileDTO.getUsername());

        String link = fromCurrentContextPath().path("/login/resetPassword/" + token + "/" + profileDTO.getUsername()).toUriString();
        emailService.send(profileDTO.getEmail(), buildResetPasswordEmail(profileDTO.getUsername(), link));
    }

    private String createAndSaveConfirmationToken(String username) {
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

    @Override
    public void resetPasswordStepTwo(String username, String newPassword, String token) {

        getValidConfirmationToken(token);
        confirmationTokenService.setConfirmedAt(token);

        ProfileDTO profile = validateProfileForPasswordReset(username);
        updatePassword(profile, newPassword);
    }

    private void updatePassword(ProfileDTO profileDTO, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        profileDTO.setPassword(encodedPassword);
        profileService.save(profileDTO);
    }

    private ProfileDTO validateProfileForPasswordReset(String username) throws UserNotFoundException, AccountDisabledException {
        ProfileDTO profile = profileService.findUserByUsername(username);
        if (Boolean.FALSE.equals(profile.getNotLocked())) {
            throw new AccountDisabledException(ACCOUNT_DISABLED);
        }
        return profile;
    }

    private void getValidConfirmationToken(String token) {
        ConfirmationToken confirmToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found."));
        if (confirmToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token already confirmed.");
        }
        if (confirmToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired.");
        }
    }

    @Override
    public void confirmMfaCode(String code) {
        MFAVerification mfaVerification = getMFAVerificationOrThrow(code);
        validateMfaCodeExpiryAndConfirmation(mfaVerification);

        mfaVerificationService.setConfirmedAt(code);
    }

    private void validateMfaCodeExpiryAndConfirmation(MFAVerification mfaVerification) throws SMSAlreadyConfirmedException, SMSExpiredException {
        if (mfaVerification.getConfirmedAt() != null) {
            throw new SMSAlreadyConfirmedException(SMS_FAIL);
        }
        if (mfaVerification.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new SMSExpiredException(SMS_ALREADY_CONFIRMED);
        }
    }

    private MFAVerification getMFAVerificationOrThrow(String code) {
        return mfaVerificationService.getCode(code.toUpperCase()).orElseThrow(() -> new SMSNotFoundException(SMS_FAIL));
    }

    @Override
    public void sendVerificationCode(String username) {
        ProfileDTO profileDTO = profileService.findUserByUsername(username);
        String code = generateVerificationCode();
        saveMFAVerification(profileDTO.getUsername(), code);
        sendSMS(profileDTO.getIndicativePhone(), decryptString(profileDTO.getPhone(), profileDTO.getUserId()), "Desporto24APP \nCodigo de Verificação:\n" + code);
    }

    private String generateVerificationCode() {
        return randomAlphabetic(8).toUpperCase();
    }
    private void saveMFAVerification(String username, String code) {
        MFAVerification mfaVerification = new MFAVerification(
                code,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                username
        );
        mfaVerificationService.saveConfirmationMFA(mfaVerification);
    }


    private String buildResetPasswordEmail(String name, String link) {
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
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Desporto24</span>\n" +
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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Face ao teu pedido para definir uma palavra-passe, aqui tens o link para defini-la </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Clica aqui para alterares a tua palavra-passe</a><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Atenção, este link vai expirar dentro de 15 minutos.</p> </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cumprimentos,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">DESPORTO24APP</p>" +
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

    private String buildRegistrationEmail(String name, String link) {
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
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Registo no Desporto24!</span>\n" +
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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Obrigado por te registares na nossa aplicação. Clica neste link para ativar a tua conta: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Clica aqui para finalizar o teu registo</a> </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cumprimentos,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">DESPORTO24APP</p>" +
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
