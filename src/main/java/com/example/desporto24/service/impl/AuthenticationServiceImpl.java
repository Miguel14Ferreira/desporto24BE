package com.example.desporto24.service.impl;

import com.example.desporto24.exception.*;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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
import com.example.desporto24.security.DistributedAuthRateLimiter;
import java.time.Duration;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.service.EmailService;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
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
import java.util.concurrent.TimeUnit;
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
    // This is per-instance protection. Edge/distributed throttling remains a separate task.
    private final Cache<String, Integer> mfaAttempts = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();
    // A new MFA challenge must not reset the account's guess budget.
    // This prevents repeated /mfa/resend or fresh login challenges from
    // multiplying TOTP attempts. A shared edge limiter is still needed for
    // deployments with more than one API instance.
    private final Cache<String, Integer> mfaAccountAttempts = CacheBuilder.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();
    private final Cache<String, Boolean> mfaResendCooldown = CacheBuilder.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .maximumSize(10_000)
            .build();

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

    @Value("${ui.app.url}")
    private String uiAppUrl;

    @Value("${desporto.preview-mode:false}")
    private boolean previewMode;

    @Autowired(required = false)
    private MediaStorage mediaStorage;

    @Autowired(required = false)
    private DistributedAuthRateLimiter distributedAttempts;

    @Override
    public LoginResponse login(ProfileDTO credentials) {
        String suppliedUsername = credentials.getUsername() == null
                ? ""
                : credentials.getUsername().trim();
        // Throttle before looking up a user: unknown usernames must not bypass
        // failed-login limits or reveal whether an account exists.
        loginAttemptService.requireAttemptAllowed(suppliedUsername);
        Profile profile = resolvePasswordLoginIdentity(suppliedUsername)
                .orElseThrow(() -> {
                    loginAttemptService.recordMissingAccountAttempt(suppliedUsername);
                    return new BadCredentialsException(INCORRECT_CREDENTIALS);
                });

        validateProfileStatus(profile);
        loginAttemptService.handleLoginAttempt(profile.getUsername(), credentials.getPassword(), profile);

        // An inconsistent MFA configuration must not silently downgrade the
        // account to password-only login. Recovery must be an explicit,
        // separately authorized operation.
        if (Boolean.TRUE.equals(profile.getMFA())
                && (profile.getMfaSecret() == null || profile.getMfaSecret().isBlank())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "A configuração MFA desta conta necessita de recuperação.");
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

    /**
     * Resolve only an unambiguous account. The existing login body continues
     * using "username" for backwards compatibility; it can now contain a
     * username, email or phone. Password/MFA/JWT checks use the canonical
     * Profile.username exactly as before.
     *
     * Phone numbers are encrypted with a per-profile AES key and cannot be
     * queried by plaintext. Use paginated reads only for phone-shaped inputs,
     * and fail closed on shared national numbers or identifier collisions.
     * A keyed, indexed lookup is preferable if this account base grows large.
     */
    private Optional<Profile> resolvePasswordLoginIdentity(String identifier) {
        Optional<Profile> byUsername = profileRepository.findUserByUsernameIgnoreCase(identifier);

        if (identifier.indexOf('@') >= 0) {
            return uniqueIdentity(byUsername,
                    profileRepository.findUserByEmailIgnoreCase(identifier));
        }

        String phone = canonicalInputPhone(identifier);
        if (phone == null) return byUsername;

        Profile match = byUsername.orElse(null);
        boolean hasPhoneMatch = false;
        boolean ambiguous = false;
        int pageNumber = 0;
        org.springframework.data.domain.Page<Profile> page;
        do {
            page = profileRepository.findAll(
                    org.springframework.data.domain.PageRequest.of(pageNumber++, 200));
            for (Profile candidate : page.getContent()) {
                if (!matchesStoredPhone(candidate, phone)) continue;
                if (hasPhoneMatch && !sameIdentity(match, candidate)) {
                    ambiguous = true;
                    break;
                }
                if (match != null && !sameIdentity(match, candidate)) {
                    ambiguous = true;
                    break;
                }
                match = candidate;
                hasPhoneMatch = true;
            }
            if (ambiguous) break;
        } while (page.hasNext());

        // A numeric username is still supported. But an identifier pointing
        // at a different phone owner must not silently select either account.
        if (ambiguous) throw new BadCredentialsException(INCORRECT_CREDENTIALS);
        return Optional.ofNullable(match);
    }

    private Optional<Profile> uniqueIdentity(Optional<Profile> first, Optional<Profile> second) {
        if (first.isPresent() && second.isPresent()
                && !sameIdentity(first.get(), second.get())) {
            throw new BadCredentialsException(INCORRECT_CREDENTIALS);
        }
        return first.isPresent() ? first : second;
    }

    private boolean sameIdentity(Profile one, Profile two) {
        return one == two
                || (one != null && two != null && one.getId() != null
                        && one.getId().equals(two.getId()));
    }

    /** Canonical phone: national digits or +country-code-and-digits. */
    private String canonicalInputPhone(String input) {
        if (input == null || input.length() > 40
                || !input.matches("[+0-9() .-]+")) {
            return null;
        }
        String compact = input.replaceAll("[() .-]", "");
        if (compact.startsWith("00")) compact = "+" + compact.substring(2);
        if (!compact.matches("\\+?[0-9]{7,15}")) return null;
        return compact;
    }

    private boolean matchesStoredPhone(Profile profile, String canonicalInput) {
        if (profile.getPhone() == null || profile.getPhone().isBlank()
                || profile.getUserId() == null || profile.getUserId().isBlank()) return false;
        final String stored;
        try {
            stored = decryptString(profile.getPhone(), profile.getUserId());
        } catch (RuntimeException invalidLegacyValue) {
            // Historical or damaged ciphertext is never interpreted as an ID.
            return false;
        }
        if (stored == null) return false;
        String local = stored.replaceAll("[() +.-]", "");
        if (!local.matches("[0-9]{4,15}")) return false;

        String prefix = profile.getIndicativePhone() == null
                ? "" : profile.getIndicativePhone().replaceAll("[() +.-]", "");
        if (prefix.startsWith("00")) prefix = prefix.substring(2);
        if (!prefix.isEmpty() && !prefix.matches("[0-9]{1,4}")) return false;

        String international = prefix.isBlank() ? local : prefix + local;
        return canonicalInput.startsWith("+")
                ? !prefix.isBlank() && canonicalInput.substring(1).equals(international)
                : canonicalInput.equals(local) || canonicalInput.equals(international);
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
    @Transactional
    public LoginResponse resendMfa(String challengeId) {
        MFAVerification previousChallenge = mfaVerificationService.getByChallengeId(challengeId)
                .orElseThrow(() -> new SMSNotFoundException(SMS_FAIL));
        validateMfaCodeExpiryAndConfirmation(previousChallenge);

        Profile profile = profileRepository.findUserByUsername(previousChallenge.getPerfil())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        validateProfileStatus(profile);
        if (!Boolean.TRUE.equals(profile.getMFA())
                || profile.getMfaSecret() == null || profile.getMfaSecret().isBlank()) {
            throw new BadCredentialsException(SMS_FAIL);
        }

        String account = profile.getUsername().trim().toLowerCase(Locale.ROOT);
        if (distributedAttempts != null) {
            distributedAttempts.reserveAttempt("mfa-resend", account, 1, Duration.ofSeconds(60));
        }
        // Reserving per account prevents chaining fresh challenge IDs to
        // bypass the old per-challenge endpoint cooldown.
        if (mfaResendCooldown.asMap().putIfAbsent(account, Boolean.TRUE) != null) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Tenta novamente dentro de 60 segundos.");
        }
        // Single-use rotation: both concurrent resend requests cannot mint
        // a usable challenge from the same predecessor.
        if (!mfaVerificationService.confirmIfPending(
                previousChallenge.getChallengeId(), LocalDateTime.now(ZoneOffset.UTC))) {
            throw new SMSAlreadyConfirmedException(SMS_ALREADY_CONFIRMED);
        }

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

        // Reserve a try atomically BEFORE checking the TOTP. Concurrent requests
        // cannot bypass the five-attempt limit for the same MFA challenge.
        int attemptNumber = mfaAttempts.asMap().merge(
                challenge.getChallengeId(), 1, (previous, increment) -> Math.min(6, previous + increment));
        if (attemptNumber > 5) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Demasiadas tentativas de confirmação. Inicia sessão novamente.");
        }
        String account = profile.getUsername().trim().toLowerCase(Locale.ROOT);
        if (distributedAttempts != null) {
            distributedAttempts.reserveAttempt("mfa-confirm", account, 5, Duration.ofMinutes(15));
        }
        int accountAttempt = mfaAccountAttempts.asMap().merge(
                account, 1, (previous, increment) -> Math.min(6, previous + increment));
        if (accountAttempt > 5) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Demasiadas tentativas MFA. Tenta novamente mais tarde.");
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
        mfaAttempts.invalidate(challenge.getChallengeId());
        mfaAccountAttempts.invalidate(account);
        mfaResendCooldown.invalidate(account);
        if (distributedAttempts != null) {
            distributedAttempts.clearAfterSuccess("mfa-confirm", account);
            distributedAttempts.clearAfterSuccess("mfa-resend", account);
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
        if (profile.getModerationSuspendedAt() != null || Boolean.FALSE.equals(profile.getNotLocked())) {
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
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUserId(generateUserId());
        profileDTO.setUsername(request.username().trim());
        profileDTO.setPassword(passwordHashService.encode(request.password()));
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
            sendSignUpEmail(profileDTO, request.returnSessionId(), request.returnCommunityId());
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

    private void sendSignUpEmail(ProfileDTO profileDTO) {
        sendSignUpEmail(profileDTO, null, null);
    }

    private void sendSignUpEmail(ProfileDTO profileDTO, Long returnSessionId, Long returnCommunityId) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), profileDTO.getUsername()
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = uiAppUrl.replaceAll("/+$", "")
                + "/?confirmRegistration=" + token
                + (returnSessionId != null && returnSessionId > 0
                        ? "&returnSessionId=" + returnSessionId
                        : returnCommunityId != null && returnCommunityId > 0
                            ? "&returnCommunityId=" + returnCommunityId
                            : "");
        log.info("Registration confirmation link created for {}", profileDTO.getUsername());
        emailService.send(profileDTO.getEmail(), "Confirma o teu email | Confirm your email — Jynlo",
                buildRegistrationEmail(profileDTO.getUsername(), link));

        createWelcomeNotification(profileDTO, token);
    }

    @Override
    public void resendRegistrationConfirmation(String username) {
        resendRegistrationConfirmation(username, null, null);
    }

    @Override
    public void resendRegistrationConfirmation(String username, Long returnSessionId) {
        resendRegistrationConfirmation(username, returnSessionId, null);
    }

    @Override
    public void resendRegistrationConfirmation(
            String username, Long returnSessionId, Long returnCommunityId) {
        String suppliedUsername = username == null ? "" : username.trim();
        Profile profile = profileRepository.findUserByUsernameIgnoreCase(suppliedUsername)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (Boolean.TRUE.equals(profile.getEnabled())) {
            return;
        }

        confirmationTokenService.deleteByProfile(profile.getUsername());

        ProfileDTO profileDTO = profileMapper.toDto(profile);
        sendSignUpEmail(profileDTO, returnSessionId, returnCommunityId);
    }

    private void createWelcomeNotification(ProfileDTO profileDTO, String token) {
        Date date = new Date();
        String formattedDate = formatDate2(date);

        String subject = "Bem-vindo ao nosso site, " + profileDTO.getUsername() + "!";
        String message = "Aqui podes explorar todas as sessões desportivas que estão a decorrer neste momento. Para mais detalhes, basta clicar em qualquer uma delas. Além disso, tens a opção de atualizar o teu perfil ou criar a tua própria sessão desportiva.";
        String greeting = "Bons jogos,";
        String signature = "JYNLO";

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
        emailService.send(profileDTO.getEmail(), "Recupera a tua palavra-passe | Reset your password — Jynlo",
                buildResetPasswordEmail(profileDTO.getUsername(), link));
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
        // Password reset cannot remove a moderation suspension.
        profile.setNotLocked(profile.getModerationSuspendedAt() == null);
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
                "Olá " + name + ", recebemos um pedido para definir uma nova palavra-passe para a tua conta Jynlo.",
                "Reset your password",
                "Hi " + name + ", we received a request to set a new password for your Jynlo account.",
                "Definir palavra-passe / Reset password",
                link,
                "O link expira em 15 minutos. Se não fizeste este pedido, podes ignorar este email. A tua palavra-passe só será alterada quando definires uma nova.",
                "This link expires in 15 minutes. If you did not make this request, you can ignore this email. Your password will only change when you set a new one."
        );
    }

    private String buildRegistrationEmail(String name, String link) {
        return TransactionalEmailTemplate.render(
                "Confirma o teu email",
                "Olá " + name + ", obrigado por te registares no Jynlo. Confirma o teu email para ativar a conta.",
                "Confirm your email",
                "Hi " + name + ", thank you for registering with Jynlo. Confirm your email to activate your account.",
                "Confirmar email / Confirm email",
                link,
                "O link expira em 15 minutos. Se não criaste esta conta, podes ignorar este email.",
                "This link expires in 15 minutes. If you did not create this account, you can ignore this email."
        );
    }
}
