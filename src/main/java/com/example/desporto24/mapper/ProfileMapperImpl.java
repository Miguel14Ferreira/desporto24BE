package com.example.desporto24.mapper;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;
import static com.example.desporto24.enumeration.Role.ROLE_USER;
import static com.example.desporto24.model.Status.OFFLINE;
import static org.apache.commons.lang3.StringUtils.substring;

@Component
@RequiredArgsConstructor
public class ProfileMapperImpl implements ProfileMapper{

    private final BCryptPasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    @Override
    public Profile toEntity(ProfileDTO profileDTO) {
        Profile profile = profileRepository.findUserByUsername(profileDTO.getUsername()).orElseThrow(() -> new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME));
        if (profile == null){
            profile = new Profile();
            profile.setUserId(generateUserId());
            profile.setUsername(profileDTO.getUsername());
            String encodedPassword = passwordEncoder.encode(profileDTO.getPassword());
            profile.setPassword(encodedPassword);
            String encodedPhone = AES.encryptString(profileDTO.getPhone(), profile.getUserId());
            profile.setPhone(encodedPhone);
            String encodedAddress = AES.encryptString(profileDTO.getAddress(), profile.getUserId());
            profile.setAddress(encodedAddress);
            profile.setEmail(profileDTO.getEmail());
            profile.setCountry(profileDTO.getCountry());
            profile.setDateOfBirth(profileDTO.getDateOfBirth());
            profile.setFavoriteSports(profileDTO.getFavoriteSports());
            profile.setFullName(profileDTO.getFullName());
            profile.setGender(profileDTO.getGender());
            profile.setIndicativePhone(profileDTO.getIndicativePhone());
            profile.setLocation(profileDTO.getLocation());
            profile.setOldEmail(profileDTO.getEmail());
            profile.setPostalCode(profileDTO.getPostalCode());
            profile.setJoinDate(new Date());
            Date date = new Date();
            String data = substring(String.valueOf(date), 3, 10);
            String data2 = substring(String.valueOf(date), 24, 29);
            String data3 = data2 + data;
            profile.setJoinDateDisplay(data3);
            profile.setEnabled(false);
            profile.setNotLocked(true);
            profile.setRole(ROLE_USER.name());
            profile.setAuthorities(ROLE_USER.getAuthorities());
            profile.setMFA(false);
            profile.setLogginAttempts(0);
            profile.setStatusId(OFFLINE);
        } else {
            String encodedPhone = AES.encryptString(profileDTO.getPhone(), profile.getUserId());
            profile.setPhone(encodedPhone);
            String encodedAddress = AES.encryptString(profileDTO.getAddress(), profile.getUserId());
            profile.setAddress(encodedAddress);
            profile.setEmail(profileDTO.getEmail());
            profile.setCountry(profileDTO.getCountry());
            profile.setDateOfBirth(profileDTO.getDateOfBirth());
            profile.setFavoriteSports(profileDTO.getFavoriteSports());
            profile.setFullName(profileDTO.getFullName());
            profile.setGender(profileDTO.getGender());
            profile.setIndicativePhone(profileDTO.getIndicativePhone());
            profile.setLocation(profileDTO.getLocation());
            profile.setPostalCode(profileDTO.getPostalCode());
            profile.setMFA(profile.getMFA());
            profile.setLogginAttempts(profile.getLogginAttempts());
            profile.setProfilePic(profile.getProfilePic());
            profile.setNotLocked(profile.getNotLocked());
        }
        return profile;
    }

    @Override
    public ProfileDTO toDto(Profile profile) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUserId(profile.getUserId());
        profileDTO.setUsername(profile.getUsername());
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
        profileDTO.setProfilePic(profile.getProfilePic());
        profileDTO.setStatusId(profile.getStatusId());

        return profileDTO;
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }
}
