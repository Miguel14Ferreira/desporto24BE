package com.example.desporto24.service.impl;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.email.TransactionalEmailTemplate;
import com.example.desporto24.exception.*;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.profile.UpdateProfileRequest;
import com.example.desporto24.model.dto.profile.ChangePasswordRequest;
import com.example.desporto24.model.dto.profile.PublicProfileResponse;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.UserBlockService;
import com.example.desporto24.service.interfaces.ProfileService;
import com.example.desporto24.security.PasswordHashService;
import com.example.desporto24.storage.MediaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static com.example.desporto24.constant.FileConstant.*;
import static com.example.desporto24.constant.FileConstant.FILE_SAVED_IN_FILE_SYSTEM;
import static com.example.desporto24.constant.UserImplConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.springframework.http.MediaType.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final EmailService emailService;
    private final NotificationsRepository notificationsRepository;
    private final PasswordHashService passwordHashService;
    private final UserBlockService userBlockService;

    @Autowired(required = false)
    private MediaStorage mediaStorage;

    @Value("${ui.app.url}")
    private String uiAppUrl;

    @Override
    public ProfileDTO findUserByUsername(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        return profileMapper.toDto(profile);
    }

    @Override
    public PublicProfileResponse findPublicProfile(String username) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        return toPublicProfile(profile);
    }

    @Override
    public PublicProfileResponse findPublicProfile(String actorUsername, String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(actorUsername)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        Profile target = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        userBlockService.ensureInteractionAllowed(actor, target);
        return toPublicProfile(target);
    }

    @Override
    public ProfileDTO findUserByEmail(String email) {
        Profile profile = profileRepository.findUserByEmail(email).orElseThrow(() -> new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL));
        return profileMapper.toDto(profile);
    }

    @Override
    public ProfileDTO findUserByUserId(String userId) {
        Profile profile = profileRepository.findUserByUserId(userId).orElseThrow(() -> new UserIdNotFoundException(NO_USERID_FOUND_BY_ID));
        return profileMapper.toDto(profile);
    }

    @Override
    public ProfileDTO findUserByPhone(String phone) {
        Profile profile = profileRepository.findUserByPhone(phone).orElseThrow(() -> new PhoneNotFoundException(NO_USER_FOUND_BY_PHONE));
        return profileMapper.toDto(profile);
    }

    private void saveProfileImage(ProfileDTO profileDTO, MultipartFile profileImage) {
        if (profileImage == null || profileImage.isEmpty()) {
            return;
        }
        if (mediaStorage == null) {
            throw new IllegalStateException("Media storage is not configured");
        }

        Profile profile = profileRepository.findUserByUsername(profileDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        var uri = mediaStorage.store(
                "profiles/" + profile.getId() + "/" + UUID.randomUUID(),
                profileImage
        );
        profile.setProfilePicUrl(uri.toString());
        profileRepository.save(profile);
    }


    public void validateNewUsernameEmail(String currentUsername, String newUsername, String email) throws UsernameExistException, EmailExistException, UserNotFoundException {
        try {
            ProfileDTO userByNewUsername = findUserByUsername(newUsername);
            ProfileDTO userByNewEmail = findUserByEmail(email);
            if (isNotBlank(currentUsername)) {
                ProfileDTO currentUser = findUserByUsername(currentUsername);
                if (currentUser == null) {
                    throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + currentUsername);
                }
                if (userByNewUsername != null && !currentUser.getUserId().equals(userByNewUsername.getUserId())) {
                    throw new UsernameExistException(USERNAME_ALREADY_EXIST);
                }
                if (userByNewEmail != null && !currentUser.getUserId().equals(userByNewEmail.getUserId())) {
                    throw new EmailExistException(EMAIL_ALREADY_EXIST);
                }
            } else {
                if (userByNewUsername != null) {
                    throw new UsernameExistException(USERNAME_ALREADY_EXIST);
                }
                if (userByNewEmail != null) {
                    throw new EmailExistException(EMAIL_ALREADY_EXIST);
                }
            }
        } catch (UserNotFoundException e){
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME);
        } catch (UsernameExistException e){
            throw new UsernameExistException(USERNAME_ALREADY_EXIST);
        }catch (EmailExistException e){
            throw new EmailExistException(EMAIL_ALREADY_EXIST);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProfileDTO updateMyProfile(String username, UpdateProfileRequest request) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (request.email() != null && !Objects.equals(profile.getEmail(), request.email())) {
            profileRepository.findUserByEmail(request.email())
                    .filter(existing -> !existing.getId().equals(profile.getId()))
                    .ifPresent(existing -> {
                        throw new EmailExistException(EMAIL_ALREADY_EXIST);
                    });
        }

        String currentPhone = AES.decryptString(profile.getPhone(), profile.getUserId());
        if (request.phone() != null && !Objects.equals(currentPhone, request.phone())) {
            for (Profile existing : profileRepository.findAll()) {
                if (existing.getId().equals(profile.getId()) || existing.getPhone() == null) {
                    continue;
                }

                try {
                    String existingPhone = AES.decryptString(existing.getPhone(), existing.getUserId());
                    if (Objects.equals(existingPhone, request.phone())) {
                        throw new PhoneExistException(PHONE_ALREADY_REGISTRED);
                    }
                } catch (RuntimeException ignored) {
                    // Ignore legacy/corrupted phone values here; they must not block the update.
                }
            }
        }

        String previousEmail = profile.getEmail();
        // Ordinary profile changes are acknowledged in the editing screen,
        // not in the inbox. Only an actual email change is security-sensitive.
        boolean emailChanged = !Objects.equals(previousEmail, request.email());

        profile.setFullName(request.fullName());
        profile.setNickname(request.nickname());
        profile.setDateOfBirth(request.dateOfBirth());
        profile.setAddress(AES.encryptString(request.address(), profile.getUserId()));
        profile.setCountry(request.country());
        profile.setLocation(request.location());
        profile.setPostalCode(request.postalCode());
        profile.setIndicativePhone(request.indicativePhone());
        profile.setPhone(AES.encryptString(request.phone(), profile.getUserId()));
        profile.setGender(request.gender());
        profile.setEmail(request.email());
        profile.setFavoriteSports(request.favoriteSports());
        profile.setBio(request.bio());
        profile.setSkillLevel(request.skillLevel());
        profile.setAvailability(request.availability());

        Profile saved = profileRepository.save(profile);
        ProfileDTO updated = profileMapper.toDto(saved);
        updated.setOldEmail(previousEmail);

        if (emailChanged) {
            sendEmailChangedEmail(previousEmail, updated.getEmail(), updated.getUsername());
            createSecurityNotification(
                    updated.getUsername(),
                    "Alteração de email",
                    "O endereço de email associado à tua conta foi alterado. "
                            + "Se não foste tu, redefine imediatamente a tua palavra-passe."
            );
        }

        return updated;
    }

    @Override
    public ProfileDTO updateMyProfilePhoto(String username, MultipartFile photo) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (mediaStorage == null) {
            throw new IllegalStateException("Media storage is not configured");
        }

        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("A fotografia é obrigatória");
        }

        var uri = mediaStorage.store(
                "profiles/" + profile.getId() + "/" + UUID.randomUUID(),
                photo
        );

        profile.setProfilePicUrl(uri.toString());
        Profile saved = profileRepository.save(profile);
        return profileMapper.toDto(saved);
    }

    @Override
    public void changeMyPassword(String username, ChangePasswordRequest request) {
        Profile profile = profileRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        if (!passwordHashService.matchesAndUpgrade(profile, request.currentPassword())) {
            throw new BadCredentialsException(INCORRECT_CREDENTIALS);
        }

        if (request.currentPassword().equals(request.newPassword())) {
            throw new IllegalArgumentException("A nova palavra-passe tem de ser diferente da atual");
        }

        profile.setPassword(passwordHashService.encode(request.newPassword()));
        profile.setTokenVersion(profile.getTokenVersion() + 1);
        profileRepository.save(profile);

        sendPasswordChangedEmail(profile);
        createSecurityNotification(
                profile.getUsername(),
                "Palavra-passe alterada",
                "A tua palavra-passe foi alterada. Se não foste tu, redefine-a imediatamente."
        );
    }

    @Override
    public void updateUser(String username, ProfileDTO profileDTO, MultipartFile photo) {
        ProfileDTO profile = findUserByUsername(username);
        if (profile.getEmail().equals(profileDTO.getEmail())) {
            validateNewUsernameEmail(username, null, null);
        } if (profile.getPhone().equals(profileDTO.getPhone())){
            validateNewUsernameEmail(username,null, profileDTO.getEmail());
        } else {
            validateNewUsernameEmail(username,null, profileDTO.getEmail());
        }

        saveProfileImage(profile, photo);
    }

    /** Only actual account-security events belong in the notification inbox. */
    private void createSecurityNotification(String username, String subject, String message) {
        String greeting = "Cumprimentos,";
        String signature = "JYNLO MANAGEMENT TEAM";
        String displayDate = java.time.Instant.now().toString();

        Notifications notification = new Notifications(
                subject,
                message,
                greeting,
                signature,
                displayDate,
                false,
                true,
                false,
                false,
                null,
                username
        );
        // Account security alerts must not be hidden by optional notification
        // preferences. Recovery emails are also sent independently.
        notificationsRepository.save(notification);
    }

    private void sendEmailChangedEmail(String oldEmail, String currentEmail, String username) {
        String resetLink = uiAppUrl.replaceAll("/+$", "") + "/login/resetPassword";
        String emailBody = buildEmailChangedEmail(username, resetLink);
        String subject = "Alteração de email | Email address changed — Jynlo";

        if (oldEmail != null && !oldEmail.isBlank()) {
            emailService.send(oldEmail, subject, emailBody);
        }

        if (currentEmail != null
                && !currentEmail.isBlank()
                && !Objects.equals(oldEmail, currentEmail)) {
            emailService.send(currentEmail, subject, emailBody);
        }
    }

    private String decryptSafely(String value, String userId) {
        if (value == null) {
            return null;
        }
        try {
            return AES.decryptString(value, userId);
        } catch (RuntimeException exception) {
            return value;
        }
    }

    @Override
    public void changeUsernameAndPassword(ProfileDTO profileDTO) {
        Profile profile = profileRepository.findUserByUsername(profileDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        profile.setUsername(profileDTO.getNewUsername());
        profile.setPassword(passwordHashService.encode(profileDTO.getPassword()));
        profile.setTokenVersion(profile.getTokenVersion() + 1);
        profileRepository.save(profile);
    }

    @Override
    public List<ProfileDTO> searchProfileByUsername(String username) {
        List<Profile> profiles = profileRepository.searchProfile(username);
        List<ProfileDTO> profileDTOs = new ArrayList<>();

        for (Profile profile : profiles) {
            profileDTOs.add(profileMapper.toDto(profile));
        }
        return profileDTOs;
    }

    @Override
    public List<PublicProfileResponse> searchPublicProfiles(String username) {
        return profileRepository.searchProfile(username).stream()
                .map(this::toPublicProfile)
                .toList();
    }

    @Override
    public List<PublicProfileResponse> searchPublicProfiles(
            String actorUsername,
            String name,
            String sport,
            String location,
            Integer minAge,
            Integer maxAge,
            String skillLevel) {
        if (minAge != null && maxAge != null && minAge > maxAge) {
            throw new IllegalArgumentException("A idade mínima não pode ser superior à idade máxima");
        }

        String normalizedName = normalizeSearchFilter(name);
        String normalizedSport = normalizeSearchFilter(sport);
        String normalizedLocation = normalizeSearchFilter(location);
        String normalizedSkillLevel = normalizeSearchFilter(skillLevel);
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(actorUsername)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        Set<Long> blockedProfileIds = userBlockService.blockedProfileIds(actor);

        return profileRepository.searchPublicCandidates(
                        actorUsername,
                        normalizedName,
                        normalizedSport,
                        normalizedLocation,
                        normalizedSkillLevel,
                        PageRequest.of(0, 100)
                )
                .stream()
                .filter(profile -> !blockedProfileIds.contains(profile.getId()))
                .filter(profile -> matchesAge(profile, minAge, maxAge))
                .limit(50)
                .map(this::toPublicProfile)
                .toList();
    }

    // These two accounts are retained for end-to-end QA; they must not
    // appear in automated public player discovery. Explicit profile links
    // and login remain available so test workflows are not disrupted.
    private static final Set<String> NON_DISCOVERABLE_QA_USERNAMES =
            Set.of("jynloteste01", "jynloteste02");

    private boolean isQaDiscoveryAccount(Profile candidate) {
        return candidate.getUsername() != null
                && NON_DISCOVERABLE_QA_USERNAMES.contains(
                        candidate.getUsername().trim().toLowerCase(Locale.ROOT));
    }

    @Override
    public List<PublicProfileResponse> discoverPublicProfiles(String username) {
        Profile actor = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        Set<Long> blockedProfileIds = userBlockService.blockedProfileIds(actor);

        return profileRepository.discoverProfiles(username, PageRequest.of(0, 100))
                .stream()
                .filter(profile -> !blockedProfileIds.contains(profile.getId()))
                .filter(profile -> !isQaDiscoveryAccount(profile))
                .sorted((left, right) -> {
                    int scoreCompare = Integer.compare(
                            compatibilityScore(actor, right),
                            compatibilityScore(actor, left)
                    );
                    if (scoreCompare != 0) {
                        return scoreCompare;
                    }
                    return Long.compare(right.getId(), left.getId());
                })
                .limit(12)
                .map(this::toPublicProfile)
                .toList();
    }

    private PublicProfileResponse toPublicProfile(Profile profile) {
        ProfileDTO dto = profileMapper.toDto(profile);
        return new PublicProfileResponse(
                dto.getUsername(),
                dto.getFullName(),
                dto.getNickname(),
                dto.getCountry(),
                dto.getLocation(),
                dto.getFavoriteSports(),
                dto.getBio(),
                dto.getSkillLevel(),
                dto.getAvailability(),
                ageOf(profile.getDateOfBirth()),
                dto.getProfilePicUrl(),
                publicJoinDate(profile)
        );
    }

    private int compatibilityScore(Profile actor, Profile candidate) {
        int score = 0;

        Set<String> actorSports = normalizedSports(actor.getFavoriteSports());
        Set<String> candidateSports = normalizedSports(candidate.getFavoriteSports());
        long sharedSports = actorSports.stream()
                .filter(candidateSports::contains)
                .count();
        score += (int) sharedSports * 5;

        if (sameNormalized(actor.getLocation(), candidate.getLocation())) {
            score += 4;
        }

        if (sameNormalized(actor.getSkillLevel(), candidate.getSkillLevel())) {
            score += 2;
        }

        Integer actorAge = ageOf(actor.getDateOfBirth());
        Integer candidateAge = ageOf(candidate.getDateOfBirth());
        if (actorAge != null && candidateAge != null) {
            int difference = Math.abs(actorAge - candidateAge);
            if (difference <= 5) {
                score += 2;
            } else if (difference <= 10) {
                score += 1;
            }
        }

        if (sameNormalized(actor.getAvailability(), candidate.getAvailability())) {
            score += 1;
        }

        return score;
    }

    private boolean matchesAge(Profile profile, Integer minAge, Integer maxAge) {
        if (minAge == null && maxAge == null) {
            return true;
        }

        Integer age = ageOf(profile.getDateOfBirth());
        if (age == null) {
            return false;
        }

        return (minAge == null || age >= minAge)
                && (maxAge == null || age <= maxAge);
    }

    private Integer ageOf(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isBlank()) {
            return null;
        }

        try {
            LocalDate birthDate = LocalDate.parse(dateOfBirth.trim());
            LocalDate today = LocalDate.now(java.time.ZoneId.of("Europe/Lisbon"));
            if (birthDate.isAfter(today)) {
                return null;
            }
            return Period.between(birthDate, today).getYears();
        } catch (RuntimeException exception) {
            return null;
        }
    }

    private Set<String> normalizedSports(String sports) {
        if (sports == null || sports.isBlank()) {
            return Set.of();
        }

        Set<String> values = new HashSet<>();
        for (String sport : sports.split(",")) {
            String normalized = sport.trim().toLowerCase(Locale.ROOT);
            if (!normalized.isBlank()) {
                values.add(normalized);
            }
        }
        return values;
    }

    private boolean sameNormalized(String left, String right) {
        return left != null
                && right != null
                && !left.isBlank()
                && left.trim().equalsIgnoreCase(right.trim());
    }

    private String normalizeSearchFilter(String value) {
        return value == null
                ? ""
                : value.trim().toLowerCase(Locale.ROOT);
    }

    private String publicJoinDate(Profile profile) {
        if (profile.getJoinDate() == null) {
            return null;
        }

        java.text.SimpleDateFormat formatter =
                new java.text.SimpleDateFormat("dd/MM/yyyy, HH:mm", java.util.Locale.forLanguageTag("pt-PT"));
        formatter.setTimeZone(java.util.TimeZone.getTimeZone("Europe/Lisbon"));
        return formatter.format(profile.getJoinDate());
    }

    private void sendPasswordChangedEmail(Profile profile) {
        if (profile.getEmail() == null || profile.getEmail().isBlank()) {
            return;
        }
        String link = uiAppUrl.replaceAll("/+$", "") + "/login/resetPassword";
        emailService.send(profile.getEmail(), "Palavra-passe alterada | Password changed — Jynlo",
                buildPasswordChangedEmail(profile.getUsername(), link));
    }

    private String buildPasswordChangedEmail(String name, String link) {
        return TransactionalEmailTemplate.render(
                "Palavra-passe alterada",
                "Olá " + name + ", a palavra-passe da tua conta Jynlo foi alterada.",
                "Password changed",
                "Hi " + name + ", the password for your Jynlo account has been changed.",
                "Redefinir palavra-passe / Reset password",
                link,
                "Se foste tu, não precisas de fazer nada. Se não reconheces esta alteração, redefine imediatamente a tua palavra-passe.",
                "If you made this change, no action is needed. If you do not recognise this change, reset your password immediately."
        );
    }

    private String buildEmailChangedEmail(String name, String link) {
        return TransactionalEmailTemplate.render(
                "Alteração de email",
                "Olá " + name + ", o endereço de email associado à tua conta Jynlo foi alterado.",
                "Email address changed",
                "Hi " + name + ", the email address associated with your Jynlo account has been changed.",
                "Redefinir palavra-passe / Reset password",
                link,
                "Se foste tu, não precisas de fazer nada. Se não reconheces esta alteração, redefine imediatamente a tua palavra-passe.",
                "If you made this change, no action is needed. If you do not recognise this change, reset your password immediately."
        );
    }

    @Override
    public void save(ProfileDTO profileDTO) {
        Profile profile = profileMapper.toEntity(profileDTO);
        profileRepository.save(profile);
    }
}