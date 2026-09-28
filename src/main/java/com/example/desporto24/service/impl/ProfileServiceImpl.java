package com.example.desporto24.service.impl;

import com.example.desporto24.encryption.AES;
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
        String previousAddress = decryptSafely(profile.getAddress(), profile.getUserId());
        String previousPhone = decryptSafely(profile.getPhone(), profile.getUserId());
        List<String> changedFields = detectProfileChanges(profile, request, previousAddress, previousPhone);
        boolean emailChanged = !Objects.equals(previousEmail, request.email());

        profile.setFullName(request.fullName());
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

        Profile saved = profileRepository.save(profile);
        ProfileDTO updated = profileMapper.toDto(saved);
        updated.setOldEmail(previousEmail);

        if (!changedFields.isEmpty()) {
            if (emailChanged) {
                sendProfileSecurityEmail(
                        previousEmail,
                        updated.getEmail(),
                        updated.getUsername(),
                        "Alteração de email",
                        "O endereço de email associado à tua conta DESPORTO24 foi alterado."
                );
                createSecurityNotification(
                        updated.getUsername(),
                        "Alteração de email",
                        "O endereço de email associado à tua conta foi alterado. "
                                + "Se não foste tu, redefine imediatamente a tua palavra-passe."
                );
            } else {
                createProfileUpdateNotification(updated.getUsername(), changedFields);
            }
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

        sendProfileSecurityEmail(
                profile.getEmail(),
                profile.getEmail(),
                profile.getUsername(),
                "Palavra-passe alterada",
                "A palavra-passe da tua conta DESPORTO24 foi alterada."
        );
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
        createProfileUpdateNotification(profile.getUsername(), List.of("Fotografia de perfil"));
    }
    private void createProfileUpdateNotification(String username, List<String> changedFields) {
        String fields = String.join(", ", changedFields);
        String subject = "Perfil atualizado";
        String message = "Foram atualizados os seguintes dados do teu perfil: " + fields + ".";
        createNotification(username, subject, message, false);
    }

    private void createSecurityNotification(String username, String subject, String message) {
        createNotification(username, subject, message, true);
    }

    private void createNotification(String username, String subject, String message, boolean security) {
        String greeting = "Cumprimentos,";
        String signature = "DESPORTO24 MANAGEMENT TEAM";
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
        notificationsRepository.save(notification);
    }

    private void sendProfileSecurityEmail(
            String oldEmail,
            String currentEmail,
            String username,
            String title,
            String description) {
        String resetLink = uiAppUrl.replaceAll("/+$", "") + "/login/resetPassword";
        String emailBody = buildSecurityEmail(username, title, description, resetLink);

        if (oldEmail != null && !oldEmail.isBlank()) {
            emailService.send(oldEmail, emailBody);
        }

        if (currentEmail != null
                && !currentEmail.isBlank()
                && !Objects.equals(oldEmail, currentEmail)) {
            emailService.send(currentEmail, emailBody);
        }
    }

    private List<String> detectProfileChanges(
            Profile profile,
            UpdateProfileRequest request,
            String previousAddress,
            String previousPhone) {
        List<String> changed = new ArrayList<>();

        addChanged(changed, "Nome", profile.getFullName(), request.fullName());
        addChanged(changed, "Data de nascimento", profile.getDateOfBirth(), request.dateOfBirth());
        addChanged(changed, "Morada", previousAddress, request.address());
        addChanged(changed, "País", profile.getCountry(), request.country());
        addChanged(changed, "Localização", profile.getLocation(), request.location());
        addChanged(changed, "Código postal", profile.getPostalCode(), request.postalCode());
        addChanged(changed, "Indicativo telefónico", profile.getIndicativePhone(), request.indicativePhone());
        addChanged(changed, "Telefone", previousPhone, request.phone());
        addChanged(changed, "Género", profile.getGender(), request.gender());
        addChanged(changed, "Email", profile.getEmail(), request.email());
        addChanged(changed, "Desportos favoritos", profile.getFavoriteSports(), request.favoriteSports());

        return changed;
    }

    private void addChanged(List<String> changed, String label, Object before, Object after) {
        if (!Objects.equals(before, after)) {
            changed.add(label);
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
    public List<PublicProfileResponse> discoverPublicProfiles(String username) {
        return profileRepository.discoverProfiles(username, PageRequest.of(0, 12)).stream()
                .map(this::toPublicProfile)
                .toList();
    }

    private PublicProfileResponse toPublicProfile(Profile profile) {
        ProfileDTO dto = profileMapper.toDto(profile);
        return new PublicProfileResponse(
                dto.getUsername(),
                dto.getFullName(),
                dto.getCountry(),
                dto.getLocation(),
                dto.getFavoriteSports(),
                dto.getProfilePicUrl(),
                publicJoinDate(profile)
        );
    }

    private String publicJoinDate(Profile profile) {
        if (profile.getJoinDateDisplay() != null && !profile.getJoinDateDisplay().isBlank()) {
            return profile.getJoinDateDisplay();
        }
        if (profile.getJoinDate() == null) {
            return null;
        }
        return new java.text.SimpleDateFormat("MM/yyyy").format(profile.getJoinDate());
    }

    private String buildSecurityEmail(String name, String title, String description, String link) {
        return "<h2>" + title + "</h2>"
                + "<p>Olá " + name + ",</p>"
                + "<p>" + description + "</p>"
                + "<p>Se foste tu, não precisas de fazer nada.</p>"
                + "<p>Se não reconheces esta alteração, redefine imediatamente a tua palavra-passe:</p>"
                + "<p><a href=\"" + link + "\">Redefinir palavra-passe</a></p>"
                + "<p>Cumprimentos,<br>DESPORTO24 MANAGEMENT TEAM</p>";
    }

    @Override
    public void save(ProfileDTO profileDTO) {
        Profile profile = profileMapper.toEntity(profileDTO);
        profileRepository.save(profile);
    }
}