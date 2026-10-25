package com.example.desporto24.service;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.enumeration.Role;
import com.example.desporto24.exception.AccountDisabledException;
import com.example.desporto24.exception.EmailExistException;
import com.example.desporto24.exception.PhoneExistException;
import com.example.desporto24.exception.UsernameExistException;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.ProfilePrincipal;
import com.example.desporto24.model.Status;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.auth.GoogleAuthResponse;
import com.example.desporto24.model.dto.auth.GoogleRegistrationRequest;
import com.example.desporto24.register.MFA.MFAVerification;
import com.example.desporto24.register.MFA.MFAVerificationService;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.GoogleIdentityService;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.utility.JWTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.example.desporto24.constant.UserImplConstant.*;

@Service
@RequiredArgsConstructor
public class GoogleAuthenticationService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String USER_ID_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int USER_ID_LENGTH = 10;
    private static final String TERMS_VERSION = "2026-10-01";
    private static final String PRIVACY_VERSION = "2026-10-01";

    private final GoogleIdentityService googleIdentityService;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final PasswordHashService passwordHashService;
    private final JWTokenProvider jwtTokenProvider;
    private final MFAVerificationService mfaVerificationService;
    private final NotificationsRepository notificationsRepository;
    private final PilotEventService pilotEventService;

    @Transactional
    public GoogleAuthResponse authenticate(String credential) {
        GoogleIdentityService.GoogleIdentity identity = googleIdentityService.verify(credential);

        Profile profile = profileRepository.findByGoogleSubject(identity.subject())
                .orElseGet(() -> profileRepository.findUserByEmail(identity.email()).orElse(null));

        if (profile == null) {
            return new GoogleAuthResponse(
                    true,
                    identity.email(),
                    identity.name(),
                    identity.picture(),
                    false,
                    null,
                    null,
                    null,
                    null
            );
        }

        if (profile.getGoogleSubject() != null
                && !profile.getGoogleSubject().isBlank()
                && !Objects.equals(profile.getGoogleSubject(), identity.subject())) {
            throw new BadCredentialsException("Esta conta já está associada a outra identidade Google");
        }

        if (profile.getModerationSuspendedAt() != null || Boolean.FALSE.equals(profile.getNotLocked())) {
            throw new AccountDisabledException(ACCOUNT_DISABLED);
        }

        profile.setGoogleSubject(identity.subject());
        if (Boolean.FALSE.equals(profile.getEnabled())) {
            profile.setEnabled(true);
        }

        // A verified Google identity does not authorize silently removing
        // an already enabled second factor.
        if (Boolean.TRUE.equals(profile.getMFA())
                && (profile.getMfaSecret() == null || profile.getMfaSecret().isBlank())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "A configuração MFA desta conta necessita de recuperação.");
        }

        profileRepository.save(profile);

        if (Boolean.TRUE.equals(profile.getMFA())) {
            MFAVerification challenge = createMfaChallenge(profile);
            return new GoogleAuthResponse(
                    false,
                    identity.email(),
                    identity.name(),
                    identity.picture(),
                    true,
                    challenge.getChallengeId(),
                    challenge.getExpiredAt().toInstant(ZoneOffset.UTC),
                    null,
                    null
            );
        }

        return completedLogin(profile, identity);
    }

    @Transactional
    public GoogleAuthResponse completeRegistration(GoogleRegistrationRequest request) {
        return completeRegistration(request, null);
    }

    @Transactional
    public GoogleAuthResponse completeRegistration(GoogleRegistrationRequest request, String cohort) {
        GoogleIdentityService.GoogleIdentity identity = googleIdentityService.verify(request.credential());

        if (profileRepository.findByGoogleSubject(identity.subject()).isPresent()
                || profileRepository.findUserByEmail(identity.email()).isPresent()) {
            throw new EmailExistException(EMAIL_ALREADY_EXIST);
        }

        if (profileRepository.findUserByUsernameIgnoreCase(request.username().trim()).isPresent()) {
            throw new UsernameExistException(USERNAME_ALREADY_EXIST);
        }

        String phone = trimToNull(request.phone());
        validatePhoneAvailability(phone);

        Profile profile = new Profile();
        profile.setUserId(generateUserId());
        profile.setUsername(request.username().trim());
        profile.setPassword(passwordHashService.encode(UUID.randomUUID().toString() + UUID.randomUUID()));
        profile.setFullName(identity.name() == null || identity.name().isBlank()
                ? null
                : identity.name().trim());
        profile.setNickname(trimToNull(request.nickname()));
        profile.setDateOfBirth(request.dateOfBirth());
        profile.setCountry(request.country().trim());
        profile.setIndicativePhone(phone == null ? null : trimToNull(request.indicativePhone()));
        profile.setPhone(phone == null ? null : AES.encryptString(phone, profile.getUserId()));
        profile.setGender(trimToNull(request.gender()));
        profile.setEmail(identity.email().trim());
        profile.setProfilePicUrl(identity.picture());
        profile.setNotLocked(true);
        profile.setEnabled(true);
        profile.setMFA(false);
        profile.setGoogleSubject(identity.subject());
        profile.setRole(Role.ROLE_USER.name());
        profile.setAuthorities(Role.ROLE_USER.getAuthorities());
        profile.setStatusId(Status.OFFLINE);
        profile.setLoginAttempts(0);
        profile.setIsFirstTimeLogin(true);
        profile.setTokenVersion(0L);
        profile.setTermsAcceptedAt(Instant.now());
        profile.setTermsVersion(TERMS_VERSION);
        profile.setPrivacyVersion(PRIVACY_VERSION);
        profile.setJoinDate(new Date());
        profile.setJoinDateDisplay(new java.text.SimpleDateFormat("yyyy MMM dd HH:mm").format(new Date()));

        Profile saved = profileRepository.save(profile);
        createWelcomeNotification(saved);
        pilotEventService.recordRegistrationCompleted(saved, cohort);

        return completedLogin(saved, identity);
    }

    private GoogleAuthResponse completedLogin(
            Profile profile,
            GoogleIdentityService.GoogleIdentity identity) {
        profile.setLoginAttempts(0);
        profile.setStatusId(Status.ONLINE);
        profile.setLastLoginDate(new Date());
        profile.setLastLoginDateDisplay(
                new java.text.SimpleDateFormat("yyyy MMM dd HH:mm").format(new Date())
        );

        Profile saved = profileRepository.save(profile);
        String token = jwtTokenProvider.generateJwtToken(new ProfilePrincipal(saved));
        ProfileDTO dto = profileMapper.toDto(saved);

        return new GoogleAuthResponse(
                false,
                identity.email(),
                identity.name(),
                identity.picture(),
                false,
                null,
                null,
                token,
                dto
        );
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

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void validatePhoneAvailability(String phone) {
        if (phone == null || phone.isBlank()) {
            return;
        }

        for (Profile existing : profileRepository.findAll()) {
            if (existing.getPhone() == null || existing.getUserId() == null) {
                continue;
            }

            try {
                String existingPhone = AES.decryptString(existing.getPhone(), existing.getUserId());
                if (Objects.equals(existingPhone, phone)) {
                    throw new PhoneExistException(PHONE_ALREADY_REGISTRED);
                }
            } catch (PhoneExistException exception) {
                throw exception;
            } catch (RuntimeException ignored) {
                // Legacy/corrupted values must not block a new registration.
            }
        }
    }

    private String generateUserId() {
        StringBuilder userId = new StringBuilder(USER_ID_LENGTH);
        for (int i = 0; i < USER_ID_LENGTH; i++) {
            userId.append(USER_ID_ALPHABET.charAt(RANDOM.nextInt(USER_ID_ALPHABET.length())));
        }
        return userId.toString();
    }

    private void createWelcomeNotification(Profile profile) {
        Notifications notification = new Notifications(
                "Bem-vindo ao JYNLO, " + profile.getUsername() + "!",
                "A tua conta foi criada através da Google. Completa o teu perfil e começa a explorar a comunidade.",
                "Bons jogos,",
                "JYNLO MANAGEMENT TEAM",
                java.time.Instant.now().toString(),
                false,
                false,
                false,
                true,
                null,
                profile.getUsername()
        );
        notificationsRepository.save(notification);
    }
}
