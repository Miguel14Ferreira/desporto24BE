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
import com.example.desporto24.service.ReferenceCatalogService;
import com.example.desporto24.email.TransactionalEmailTemplate;
import com.example.desporto24.service.LoginAttemptService;
import com.example.desporto24.service.PilotEventService;
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
import java.time.Instant;
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
    private static final String TERMS_VERSION = "2026-10-01";
    private static final String PRIVACY_VERSION = "2026-10-01";

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
    private final PilotEventService pilotEventService;
    private final ReferenceCatalogService referenceCatalogService;

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
        signUpProfile(request, photo, null);
    }

    @Override
    @Transactional
    public void signUpProfile(RegistrationRequest request, MultipartFile photo, String cohort) {
        if (!referenceCatalogService.isAllowedOrBlank(
                ReferenceCatalogService.GENDER,
                request.gender())) {
            throw new IllegalArgumentException("Género inválido");
        }

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUserId(generateUserId());
        profileDTO.setUsername(request.username().trim());
        profileDTO.setPassword(request.password());
        profileDTO.setNickname(trimToNull(request.nickname()));
        profileDTO.setDateOfBirth(request.dateOfBirth());
        profileDTO.setCountry(request.country().trim());
        profileDTO.setIndicativePhone(trimToNull(request.indicativePhone()));
        profileDTO.setPhone(trimToNull(request.phone()));
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
        validatePhoneAvailability(profileDTO.getPhone());
        profileDTO.setProfilePicUrl(null);

        Profile profile = profileMapper.toEntity(profileDTO);
        profile.setTermsAcceptedAt(Instant.now());
        profile.setTermsVersion(TERMS_VERSION);
        profile.setPrivacyVersion(PRIVACY_VERSION);

        Profile savedProfile = profileRepository.saveAndFlush(profile);
        saveRegistrationProfileImage(savedProfile, profileDTO, photo);
        if (previewMode) {
            createWelcomeNotification(profileDTO, null);
            pilotEventService.recordRegistrationCompleted(savedProfile, cohort);
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
        emailService.send(profileDTO.getEmail(), "Confirma o teu email | Confirm your email — Desporto24",
                buildRegistrationEmail(profileDTO.getUsername(), link));

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

    private void validatePhoneAvailability(String phone) {
        if (phone == null || phone.isBlank()) {
            return;
        }

        String phoneHash = com.example.desporto24.encryption.AES.hashLookupValue(phone);
        if (profileRepository.findByPhoneHash(phoneHash).isPresent()) {
            throw new PhoneExistException(PHONE_ALREADY_REGISTRED);
        }
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

        Profile profile = profileRepository.findUserByUsername(confirmationToken.getProfile())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_EMAIL));
        updatePassword(profile, newPassword);
        profile.setNotLocked(true);
        profile.setLoginAttempts(0);
        profile.setStatusId(OFFLINE);
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
        Profile saved = profileRepository.save(profile);
        pilotEventService.recordRegistrationCompleted(saved);
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
        return TransactionalEmailTemplate.render(
                "Recupera a tua palavra-passe",
                "Olá " + name + ", recebemos um pedido para definir uma nova palavra-passe para a tua conta Desporto24.",
                "Reset your password",
                "Hi " + name + ", we received a request to set a new password for your Desporto24 account.",
                "Definir palavra-passe / Reset password",
                link,
                "O link expira em 15 minutos. Se não fizeste este pedido, podes ignorar este email. A tua palavra-passe só será alterada quando definires uma nova.",
                "This link expires in 15 minutes. If you did not make this request, you can ignore this email. Your password will only change when you set a new one."
        );
    }

    private String buildRegistrationEmail(String name, String link) {
        return TransactionalEmailTemplate.render(
                "Confirma o teu email",
                "Olá " + name + ", obrigado por te registares no Desporto24. Confirma o teu email para ativar a conta.",
                "Confirm your email",
                "Hi " + name + ", thank you for registering with Desporto24. Confirm your email to activate your account.",
                "Confirmar email / Confirm email",
                link,
                "O link expira em 15 minutos. Se não criaste esta conta, podes ignorar este email.",
                "This link expires in 15 minutes. If you did not create this account, you can ignore this email."
        );
    }
}
