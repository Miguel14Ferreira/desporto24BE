package com.example.desporto24.service.impl;

import com.example.desporto24.exception.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Status;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.model.dto.auth.MfaConfirmRequest;
import com.example.desporto24.register.MFA.MFAVerification;
import com.example.desporto24.register.MFA.MFAVerificationService;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.register.token.ConfirmationToken;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.LoginAttemptService;
import com.example.desporto24.service.interfaces.AuthenticationService;
import com.example.desporto24.service.interfaces.ProfileService;
import com.example.desporto24.utility.JWTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.Locale;

import static com.example.desporto24.constant.FileConstant.*;
import static com.example.desporto24.constant.UserImplConstant.*;
import static com.example.desporto24.encryption.AES.decryptString;
import static com.example.desporto24.model.Status.OFFLINE;
import static com.example.desporto24.utility.SmsUtils.sendSMS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final LoginAttemptService loginAttemptService;
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final NotificationsRepository notificationsRepository;
    private final MFAVerificationService mfaVerificationService;
    private final PasswordHashService passwordHashService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTokenProvider jwtTokenProvider;
    private final ProfileMapper profileMapper;

    @Override
    public LoginResponse login(ProfileDTO credentials) {
        Profile profile = profileRepository.findUserByUsername(credentials.getUsername())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        validateProfileStatus(profile);
        loginAttemptService.handleLoginAttempt(credentials.getUsername(), credentials.getPassword(), profile);

        if (Boolean.TRUE.equals(profile.getMFA())) {
            MFAVerification challenge = createAndSendMfaChallenge(profile);
            return new LoginResponse(
                    true,
                    challenge.getChallengeId(),
                    challenge.getExpiredAt().toInstant(ZoneOffset.UTC),
                    null,
                    profileMapper.toDto(profile)
            );
        }

        return completedLogin(profile);
    }

    @Override
    public LoginResponse confirmMfa(MfaConfirmRequest request) {
        MFAVerification challenge = mfaVerificationService.getByChallengeId(request.challengeId())
                .orElseThrow(() -> new SMSNotFoundException(SMS_FAIL));

        validateMfaCodeExpiryAndConfirmation(challenge);

        String normalizedCode = request.code().trim().toUpperCase(Locale.ROOT);
        if (!passwordEncoder.matches(normalizedCode, challenge.getCodeHash())) {
            throw new BadCredentialsException(SMS_FAIL);
        }

        mfaVerificationService.setConfirmedAt(
                challenge.getChallengeId(),
                LocalDateTime.now(ZoneOffset.UTC)
        );

        Profile profile = profileRepository.findUserByUsername(challenge.getPerfil())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        return completedLogin(profile);
    }

    private LoginResponse completedLogin(Profile profile) {
        updateProfileLoginDetails(profile);
        Profile saved = profileRepository.save(profile);
        String token = jwtTokenProvider.generateJwtToken(new com.example.desporto24.model.ProfilePrincipal(saved));
        return new LoginResponse(false, null, null, token, profileMapper.toDto(saved));
    }

    private MFAVerification createAndSendMfaChallenge(Profile profile) {
        String challengeId = UUID.randomUUID().toString();
        String rawCode = generateVerificationCode();
        LocalDateTime createdAt = LocalDateTime.now(ZoneOffset.UTC);

        MFAVerification challenge = new MFAVerification(
                challengeId,
                passwordEncoder.encode(rawCode),
                createdAt,
                createdAt.plusMinutes(5),
                profile.getUsername()
        );
        mfaVerificationService.saveConfirmationMFA(challenge);

        sendSMS(
                profile.getIndicativePhone(),
                decryptString(profile.getPhone(), profile.getUserId()),
                "Desporto24APP \nCodigo de Verificação:\n" + rawCode
        );
        return challenge;
    }

    private void validateProfileStatus(Profile profile) {
        if (Boolean.FALSE.equals(profile.getNotLocked())) {
            throw new AccountDisabledException(ACCOUNT_DISABLED);
        }
        if (Boolean.FALSE.equals(profile.getEnabled())) {
            throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
        }
    }

    private void updateProfileLoginDetails(Profile profile) {
        profile.setLoginAttempts(0);
        profile.setStatusId(Status.ONLINE);
        profile.setLastLoginDate(new Date());
        profile.setLastLoginDateDisplay(formatJoinDate(new Date()));
    }

    private String formatJoinDate(Date date) {
        return new java.text.SimpleDateFormat("yyyy MMM dd hh:mm").format(date);
    }
    @Override
    public void terminateSession(ProfileDTO profileDTO) {
        Profile profile = profileRepository.findUserByUsername(profileDTO.getUsername()).orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        profile.setStatusId(OFFLINE);
        profile.setIsFirstTimeLogin(false);
        profileRepository.save(profile);
    }

    @Override
    public void signUpProfile(ProfileDTO profileDTO, MultipartFile photo) {
        validateNewUsernameEmail(profileDTO);
        sendSignUpEmail(profileDTO);
        saveProfileImage(profileDTO, photo);
        profileService.save(profileDTO);
    }

    private void saveProfileImage(ProfileDTO profileDTO, MultipartFile profileImage) {
        if (profileImage != null) {
            validateImageFileType(profileImage.getContentType());
            Path userFolder = createUserFolder(profileDTO.getUsername());
            deleteExistingProfileImage(userFolder, profileDTO.getUsername());
            saveNewProfileImage(userFolder, profileDTO.getUsername(), profileImage);
            profileDTO.setProfilePicUrl(setProfileImageUrl(profileDTO.getUsername()));
        } else {
            setDefaultProfileImageIfNeeded(profileDTO);
        }
    }

    private void setDefaultProfileImageIfNeeded(ProfileDTO profileDTO) {
            profileDTO.setProfilePicUrl(TEMP_PROFILE_IMAGE_BASE_URL);
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

    private void sendSignUpEmail(ProfileDTO profileDTO)  {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), profileDTO.getUsername()
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = fromCurrentContextPath().path("/api/auth/register/confirm").queryParam("token",token).toUriString();
        log.info(link);
        emailService.send(profileDTO.getEmail(), buildRegistrationEmail(profileDTO.getUsername(), link));

        createWelcomeNotification(profileDTO, token);
    }

    private void createWelcomeNotification(ProfileDTO profileDTO, String token) {
        Date date = new Date();
        String formattedDate = formatDate2(date);

        String subject = "Bem-vindo ao nosso site, " + profileDTO.getUsername() + "!";
        String message = "Aqui podes explorar todas as sessões desportivas que estão a decorrer neste momento. Para mais detalhes, basta clicar em qualquer uma delas. Além disso, tens a opção de atualizar o teu perfil ou criar a tua própria sessão desportiva.";
        String greeting = "Bons jogos,";
        String signature = "DESPORTO24";

        Notifications notification = new Notifications(
                subject, message, greeting, signature, formattedDate, false, false, false, true, token, profileDTO.getUsername()
        );

        notificationsRepository.save(notification);
    }

    private String formatDate2(Date date) {
        String dayMonth = substring(String.valueOf(date), 3, 10);
        String year = substring(String.valueOf(date), 24, 29);
        return year + dayMonth;
    }

    public void validateNewUsernameEmail(ProfileDTO profileDTO) {
        if (isNotBlank(profileDTO.getUsername())) {
            Optional<Profile> userByNewUsername = profileRepository.findUserByUsername(profileDTO.getUsername());
            Optional<Profile> userByNewEmail = profileRepository.findUserByEmail(profileDTO.getEmail());

            if (userByNewUsername.isPresent()) {
                throw new UsernameExistException(USERNAME_ALREADY_EXIST);
            }

            if (userByNewEmail.isPresent()) {
                throw new EmailExistException(EMAIL_ALREADY_EXIST);
            }
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

        String link = fromCurrentContextPath().path("api/auth/reset-password/confirm").queryParam("token",token).toUriString();
        log.info(link);
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
    public void resetPasswordStepTwo(String newPassword, String token) {

        ConfirmationToken confirmationToken = getValidConfirmationToken(token);
        confirmationTokenService.setConfirmedAt(token);

        Profile profile = validateProfileForPasswordReset(confirmationToken.getProfile());
        updatePassword(profile, newPassword);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        profileRepository.save(profile);
    }

    private void updatePassword(Profile profile, String newPassword) {
        profile.setPassword(passwordHashService.encode(newPassword));
    }

    private Profile validateProfileForPasswordReset(String username) throws UserNotFoundException, AccountDisabledException {
        Profile profile = profileRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_EMAIL));
        if (Boolean.FALSE.equals(profile.getNotLocked())) {
            throw new AccountDisabledException(ACCOUNT_DISABLED);
        }
        return profile;
    }

    private ConfirmationToken getValidConfirmationToken(String token) {
        ConfirmationToken confirmToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException(TOKEN_NOT_FOUND));
        if (confirmToken.getConfirmedAt() != null) {
            throw new IllegalStateException(TOKEN_ALREADY_CONFIRMED);
        }
        if (confirmToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException(TOKEN_EXPIRED);
        }
        return confirmToken;
    }

    private void validateMfaCodeExpiryAndConfirmation(MFAVerification mfaVerification) throws SMSAlreadyConfirmedException, SMSExpiredException {
        if (mfaVerification.getConfirmedAt() != null) {
            throw new SMSAlreadyConfirmedException(SMS_FAIL);
        }
        if (mfaVerification.getExpiredAt().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            throw new SMSExpiredException(SMS_ALREADY_CONFIRMED);
        }
    }

    @Override
    public void sendVerificationCode(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        createAndSendMfaChallenge(profile);
    }

    @Override
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = getValidConfirmationToken(token);
        confirmationTokenService.setConfirmedAt(token);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        Profile profile = profileRepository.findUserByUsername(confirmationToken.getProfile()).orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        profile.setEnabled(true);
        profileRepository.save(profile);
    }

    private String generateVerificationCode() {
        return randomAlphabetic(8).toUpperCase();
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
