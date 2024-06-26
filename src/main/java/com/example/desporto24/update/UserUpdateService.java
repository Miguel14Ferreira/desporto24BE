package com.example.desporto24.update;

import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.service.impl.NotAnImageFileException;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@Service
public class UserUpdateService {
    private final ProjectServiceImpl pService;

    public Profile update(String username, UserUpdateRequest request, MultipartFile file) throws Exception, NotAnImageFileException {
        Profile profile = pService.updateUser(username,
                new Profile(
                        request.getFullName(),
                        request.getDateOfBirth(),
                        request.getAddress(),
                        request.getCountry(),
                        request.getLocation(),
                        request.getPostalCode(),
                        request.getIndicativePhone(),
                        request.getPhone(),
                        request.getGender(),
                        request.getEmail(),
                        request.getDesportosFavoritos(),
                        request.getMfa()),
                        file);
        return profile;
    }
}