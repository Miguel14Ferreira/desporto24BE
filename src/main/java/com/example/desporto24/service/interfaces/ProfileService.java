package com.example.desporto24.service.interfaces;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.profile.UpdateProfileRequest;
import com.example.desporto24.model.dto.profile.ChangePasswordRequest;
import com.example.desporto24.model.dto.profile.PublicProfileResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProfileService {
    ProfileDTO findUserByUsername(String username);
    PublicProfileResponse findPublicProfile(String username);
    ProfileDTO findUserByEmail(String email);
    ProfileDTO findUserByUserId(String userId);
    ProfileDTO findUserByPhone(String phone);
    void updateUser(String username, ProfileDTO profileDTO, MultipartFile photo);
    ProfileDTO updateMyProfile(String username, UpdateProfileRequest request);
    ProfileDTO updateMyProfilePhoto(String username, MultipartFile photo);
    void changeMyPassword(String username, ChangePasswordRequest request);
    void changeUsernameAndPassword(ProfileDTO profileDTO);
    List<ProfileDTO> searchProfileByUsername(String username);
    List<PublicProfileResponse> searchPublicProfiles(String username);
    List<PublicProfileResponse> searchPublicProfiles(
            String actorUsername,
            String name,
            String sport,
            String location,
            Integer minAge,
            Integer maxAge,
            String skillLevel);
    List<PublicProfileResponse> discoverPublicProfiles(String username);
    void save(ProfileDTO profileDTO);
}