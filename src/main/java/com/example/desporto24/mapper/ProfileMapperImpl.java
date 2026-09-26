package com.example.desporto24.mapper;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.enumeration.Role;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Status;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.security.PasswordHashService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Component
@RequiredArgsConstructor
public class ProfileMapperImpl implements ProfileMapper {

    private final ProfileRepository profileRepository;
    private final PasswordHashService passwordHashService;

    @Override
    public Profile toEntity(ProfileDTO profileDTO) {
        Profile profile;
        if (profileDTO.getUserId() == null
                || profileDTO.getUserId().isEmpty()
                || profileRepository.findUserByUserId(profileDTO.getUserId()).isEmpty()) {
            profile = createNewProfile(profileDTO);
        } else {
            profile = updateExistingProfile(profileDTO);
        }
        return profile;
    }

    private Profile createNewProfile(ProfileDTO profileDTO) {
        Profile profile = new Profile();
        profile.setUserId(profileDTO.getUserId() == null || profileDTO.getUserId().isBlank()
                ? generateUserId()
                : profileDTO.getUserId());
        profile.setJoinDate(new Date());
        profile.setJoinDateDisplay(formatJoinDate(new Date()));
        profile.setEnabled(false);
        profile.setNotLocked(true);
        profile.setRole("ROLE_USER");
        profile.setAuthorities(Role.ROLE_USER.getAuthorities());
        profile.setMFA(false);
        profile.setLoginAttempts(0);
        profile.setStatusId(Status.OFFLINE);
        profile.setOldEmail(profileDTO.getEmail());
        profile.setUsername(profileDTO.getUsername());
        profile.setPassword(passwordHashService.encode(profileDTO.getPassword()));
        profile.setIsFirstTimeLogin(true);
        mapCommonFields(profile, profileDTO);
        return profile;
    }

    private Profile updateExistingProfile(ProfileDTO profileDTO) {
        Profile existingProfile = profileRepository.findUserByUsername(profileDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME));
        mapCommonFields(existingProfile, profileDTO);
        return existingProfile;
    }

    private void mapCommonFields(Profile profile, ProfileDTO profileDTO) {
        profile.setPhone(AES.encryptString(profileDTO.getPhone(), profile.getUserId()));
        profile.setAddress(AES.encryptString(profileDTO.getAddress(), profile.getUserId()));
        profile.setEmail(profileDTO.getEmail());
        profile.setCountry(profileDTO.getCountry());
        profile.setDateOfBirth(profileDTO.getDateOfBirth());
        profile.setFavoriteSports(profileDTO.getFavoriteSports());
        profile.setFullName(profileDTO.getFullName());
        profile.setGender(profileDTO.getGender());
        profile.setIndicativePhone(profileDTO.getIndicativePhone());
        profile.setLocation(profileDTO.getLocation());
        profile.setPostalCode(profileDTO.getPostalCode());
        profile.setProfilePicUrl(profileDTO.getProfilePicUrl());
        profile.setLoginAttempts(profileDTO.getLoginAttempts());
    }

    @Override
    public ProfileDTO toDto(Profile profile) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUserId(profile.getUserId());
        profileDTO.setUsername(profile.getUsername());
        profileDTO.setPassword(null);
        profileDTO.setPhone(AES.decryptString(profile.getPhone(), profile.getUserId()));
        profileDTO.setAddress(AES.decryptString(profile.getAddress(), profile.getUserId()));
        profileDTO.setEmail(profile.getEmail());
        profileDTO.setCountry(profile.getCountry());
        profileDTO.setDateOfBirth(profile.getDateOfBirth());
        profileDTO.setFavoriteSports(profile.getFavoriteSports());
        profileDTO.setFullName(profile.getFullName());
        profileDTO.setGender(profile.getGender());
        profileDTO.setIndicativePhone(profile.getIndicativePhone());
        profileDTO.setLocation(profile.getLocation());
        profileDTO.setPostalCode(profile.getPostalCode());
        profileDTO.setMFA(profile.getMFA());
        profileDTO.setNotLocked(profile.getNotLocked());
        profileDTO.setOldEmail(profile.getOldEmail());
        profileDTO.setStatusId(profile.getStatusId());
        profileDTO.setEnabled(profile.getEnabled());
        profileDTO.setProfilePicUrl(profile.getProfilePicUrl());
        profileDTO.setRole(profile.getRole());
        profileDTO.setAuthorities(profile.getAuthorities());
        profileDTO.setIsFirstTimeLogin(profile.getIsFirstTimeLogin());
        return profileDTO;
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    @Override
    public String formatJoinDate(Date date) {
        return new java.text.SimpleDateFormat("yyyy MMM dd hh:mm").format(date);
    }
}
