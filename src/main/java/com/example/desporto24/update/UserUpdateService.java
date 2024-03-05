package com.example.desporto24.update;

import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.Perfil;
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

    public Perfil update(String username, UserUpdateRequest request, MultipartFile file) throws EmailExistException, PhoneExistException, MessagingException, UsernameExistException, IOException, NotAImageFileException, NotAnImageFileException, UserNotFoundException, EmailNotFoundException {
        Perfil perfil = pService.updateUser(username,
                new Perfil(
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
        return perfil;
    }
}