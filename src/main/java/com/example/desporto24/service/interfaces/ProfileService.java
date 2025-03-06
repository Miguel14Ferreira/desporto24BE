package com.example.desporto24.service.interfaces;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProfileService {
    ProfileDTO findUserByUsername(String username);
    ProfileDTO findUserByEmail(String email);
    ProfileDTO findUserByUserId(String userId);
    ProfileDTO findUserByPhone(String phone);
    void updateUser(String username, ProfileDTO profileDTO, MultipartFile photo);
    void changeUsernameAndPassword(ProfileDTO profileDTO);
    List<ProfileDTO> searchProfileByUsername(String username);
    void save(ProfileDTO profileDTO);
}