package com.example.desporto24.service.impl;

import com.example.desporto24.exception.*;
import com.example.desporto24.enumeration.Role;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Status;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.LoginResponse;
import com.example.desporto24.model.dto.auth.MfaConfirmRequest;
import com.example.desporto24.model.dto.auth.RegistrationRequest;
import com.example.desporto24.register.MFA.MFAVerification;
import com.example.desporto24.register.MFA.MFAVerificationService;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.register.token.ConfirmationToken;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.security.TotpService;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.LoginAttemptService;
import com.example.desporto24.service.interfaces.AuthenticationService;
import com.example.desporto24.service.interfaces.ProfileService;
import com.example.desporto24.utility.JWTokenProvider;
import com.example.desporto24.storage.MediaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import java.security.SecureRandom;

import static com.example.desporto24.constant.FileConstant.*;
import static com.example.desporto24.constant.UserImplConstant.*;
import static com.example.desporto24.encryption.AES.decryptString;
import static com.example.desporto24.model.Status.OFFLINE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final SecureRandom MFA_CODE_RANDOM = new SecureRandom();
    private static final int MFA_CODE_BOUND = 1_000_000;
    private static final String USER_ID_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int USER_ID_LENGTH = 10;

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
    private final TotpService totpService;

    @Value("${ui.app.url}")
    private String uiAppUrl;

    @Value("${desporto.preview-mode:false}")
    private boolean previewMode;

    @Autowired(required = false)
    private MediaStorage mediaStorage;

    @Override
    public LoginResponse login(ProfileDTO credentials) {
        String suppliedUsername = credentials.getUsername() == null
                ? ""
                : credentials.getUsername().trim();
        Profile profile = profileRepository.findUserByUsernameIgnoreCase(suppliedUsername)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        validateProfileStatus(profile);
        loginAttemptService.handleLoginAttempt(profile.getUsername(), credentials.getPassword(), profile);

        if (Boolean.TRUE.equals(profile.getMFA())
                && (profile.getMfaSecret() == null || profile.getMfaSecret().isBlank())) {
            profile.setMFA(false);
            profileRepository.save(profile);
        }

        if (Boolean.TRUE.equals(profile.getMFA())
                && profile.getMfaSecret() != null
                && !profile.getMfaSecret().isBlank()) {
            MFAVerification challenge = createMfaChallenge(profile);
            return new LoginResponse(
                    true,
                    challenge.getChallengeId(),
                    challenge.getExpiredAt().toInstant(ZoneOffset.UTC),
                    null,
                    null
            );
        }

        return completedLogin(profile);
    }

    @Override
    public LoginResponse refresh(String refreshToken) {
        String username = jwtTokenProvider.getSubject(refreshToken);
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        validateProfileStatus(profile);

        if (profile.getTokenVersion() != jwtTokenProvider.getTokenVersion(refreshToken)
                || !jwtTokenProvider.isRefreshTokenValid(username, refreshToken)) {
            throw new BadCredentialsException(INCORRECT_CREDENTIALS);
        }

        String token = jwtTokenProvider.generateJwtToken(
                new com.example.desporto24.model.ProfilePrincipal(profile)
        );
        return new LoginResponse(false, null, null, token, profileMapper.toDto(profile));
    }

    @Override
    public String createRefreshToken(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        validateProfileStatus(profile);

        return jwtTokenProvider.generateRefreshToken(
                new com.example.desporto24.model.ProfilePrincipal(profile)
        );
    }

    @Override
    public LoginResponse resendMfa(String challengeId) {
        MFAVerification previousChallenge = mfaVerificationService.getByChallengeId(challengeId)
                .orElseThrow(() -> new SMSNotFoundException(SMS_FAIL));

        Profile profile = profileRepository.findUserByUsername(previousChallenge.getPerfil())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        validateProfileStatus(profile);

        MFAVerification challenge = createMfaChallenge(profile);
        return new LoginResponse(
                true,
                challenge.getChallengeId(),
                challenge.getExpiredAt().toInstant(ZoneOffset.UTC),
                null,
                null
        );
    }

    @Override
    public LoginResponse confirmMfa(MfaConfirmRequest request) {
        MFAVerification challenge = mfaVerificationService.getByChallengeId(request.challengeId())
                .orElseThrow(() -> new SMSNotFoundException(SMS_FAIL));

        validateMfaCodeExpiryAndConfirmation(challenge);

        Profile profile = profileRepository.findUserByUsername(challenge.getPerfil())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        validateProfileStatus(profile);

        if (profile.getMfaSecret() == null || profile.getMfaSecret().isBlank()) {
            throw new BadCredentialsException(SMS_FAIL);
        }

        String secret = decryptString(profile.getMfaSecret(), profile.getUserId());
        String normalizedCode = request.code().trim();
        if (!totpService.verify(secret, normalizedCode)) {
            throw new BadCredentialsException(SMS_FAIL);
        }

        LocalDateTime confirmedAt = LocalDateTime.now(ZoneOffset.UTC);
        if (!mfaVerificationService.confirmIfPending(challenge.getChallengeId(), confirmedAt)) {
            throw new SMSAlreadyConfirmedException(SMS_ALREADY_CONFIRMED);
        }

        return completedLogin(profile);
    }

    private LoginResponse completedLogin(Profile profile) {
        updateProfileLoginDetails(profile);
        Profile saved = profileRepository.save(profile);
        String token = jwtTokenProvider.generateJwtToken(new com.example.desporto24.model.ProfilePrincipal(saved));
        return new LoginResponse(false, null, null, token, profileMapper.toDto(saved));
    }

    private MFAVerification createMfaChallenge(Profile profile) {
        String challengeId = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now(ZoneOffset.UTC);

        MFAVerification challenge = new MFAVerification(
                challengeId,
                null,
                createdAt,
                createdAt.plusMinutes(5),
                profile.getUsername()
        );
        mfaVerificationService.saveConfirmationMFA(challenge);
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
    public void terminateSession(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        profile.setStatusId(OFFLINE);
        profile.setIsFirstTimeLogin(false);
        invalidateExistingTokens(profile);
        profileRepository.save(profile);
    }

    @Override
    @Transactional
    public void signUpProfile(RegistrationRequest request, MultipartFile photo) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUserId(generateUserId());
        profileDTO.setUsername(request.username().trim());
        profileDTO.setPassword(request.password());
        profileDTO.setNickname(trimToNull(request.nickname()));
        profileDTO.setDateOfBirth(request.dateOfBirth());
        profileDTO.setCountry(request.country().trim());
        profileDTO.setIndicativePhone(request.indicativePhone().trim());
        profileDTO.setPhone(request.phone().trim());
        profileDTO.setGender(trimToNull(request.gender()));
        profileDTO.setEmail(request.email().trim());
        profileDTO.setNotLocked(true);
        profileDTO.setEnabled(previewMode);
        profileDTO.setMFA(false);
        profileDTO.setRole(Role.ROLE_USER.name());
        profileDTO.setAuthorities(Role.ROLE_USER.getAuthorities());
        profileDTO.setStatusId(Status.OFFLINE);
        profileDTO.setLoginAttempts(0);
        profileDTO.setIsFirstTimeLogin(true);

        validateNewUsernameEmail(profileDTO);
        profileDTO.setProfilePicUrl(null);
        Profile savedProfile = profileRepository.saveAndFlush(profileMapper.toEntity(profileDTO));
        saveRegistrationProfileImage(savedProfile, profileDTO, photo);
        if (previewMode) {
            createWelcomeNotification(profileDTO, null);
        } else {
            sendSignUpEmail(profileDTO);
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String resolveMfaPhone(Profile profile) {
        try {
            return decryptString(profile.getPhone(), profile.getUserId());
        } catch (RuntimeException exception) {
            return profile.getPhone();
        }
    }

    private void saveRegistrationProfileImage(
            Profile profile,
            ProfileDTO profileDTO,
            MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            return;
        }
        if (mediaStorage == null) {
            throw new IllegalStateException("Media storage is not configured");
        }

        var uri = mediaStorage.store(
                "profiles/" + profile.getId() + "/" + UUID.randomUUID(),
                photo
        );

        profile.setProfilePicUrl(uri.toString());
        profileRepository.save(profile);
        profileDTO.setProfilePicUrl(uri.toString());
    }

    private void sendSignUpEmail(ProfileDTO profileDTO)  {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), profileDTO.getUsername()
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = uiAppUrl.replaceAll("/+$", "")
                + "/?confirmRegistration=" + token;
        log.info("Registration confirmation link created for {}", profileDTO.getUsername());
        emailService.send(profileDTO.getEmail(), buildRegistrationEmail(profileDTO.getUsername(), link));

        createWelcomeNotification(profileDTO, token);
    }

    @Override
    public void resendRegistrationConfirmation(String username) {
        String suppliedUsername = username == null ? "" : username.trim();
        Profile profile = profileRepository.findUserByUsernameIgnoreCase(suppliedUsername)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (Boolean.TRUE.equals(profile.getEnabled())) {
            return;
        }

        confirmationTokenService.deleteByProfile(profile.getUsername());

        ProfileDTO profileDTO = profileMapper.toDto(profile);
        sendSignUpEmail(profileDTO);
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
            Optional<Profile> userByNewUsername =
                    profileRepository.findUserByUsernameIgnoreCase(profileDTO.getUsername());
            Optional<Profile> userByNewEmail = profileRepository.findUserByEmail(profileDTO.getEmail());

            if (userByNewUsername.isPresent()) {
                throw new UsernameExistException(USERNAME_ALREADY_EXIST);
            }

            if (userByNewEmail.isPresent()) {
                throw new EmailExistException(EMAIL_ALREADY_EXIST);
            }
        }
    }

    @Override
    public void resetPasswordStepOne(String email) {
        ProfileDTO profileDTO = profileService.findUserByEmail(email);
        String token = createAndSaveConfirmationToken(profileDTO.getUsername());

        String link = uiAppUrl.replaceAll("/+$", "")
                + "/login/resetPassword/" + token;
        log.info("Password reset link created for {}", profileDTO.getUsername());
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
        invalidateExistingTokens(profile);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        profileRepository.save(profile);
    }

    private void updatePassword(Profile profile, String newPassword) {
        profile.setPassword(passwordHashService.encode(newPassword));
    }

    private void invalidateExistingTokens(Profile profile) {
        profile.setTokenVersion(profile.getTokenVersion() + 1);
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
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = getValidConfirmationToken(token);
        confirmationTokenService.setConfirmedAt(token);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        Profile profile = profileRepository.findUserByUsername(confirmationToken.getProfile()).orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        profile.setEnabled(true);
        profileRepository.save(profile);
    }

    private String generateVerificationCode() {
        return String.format(Locale.ROOT, "%06d", MFA_CODE_RANDOM.nextInt(MFA_CODE_BOUND));
    }

    private String generateUserId() {
        StringBuilder userId = new StringBuilder(USER_ID_LENGTH);
        for (int i = 0; i < USER_ID_LENGTH; i++) {
            userId.append(USER_ID_ALPHABET.charAt(MFA_CODE_RANDOM.nextInt(USER_ID_ALPHABET.length())));
        }
        return userId.toString();
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
