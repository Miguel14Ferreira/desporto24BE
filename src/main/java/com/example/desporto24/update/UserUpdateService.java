package com.example.desporto24.update;

import com.example.desporto24.exception.domain.EmailExistException;
import com.example.desporto24.exception.domain.NotAImageFileException;
import com.example.desporto24.exception.domain.PhoneExistException;
import com.example.desporto24.exception.domain.UsernameExistException;
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

    public Perfil update(UserUpdateRequest request,MultipartFile file) throws EmailExistException, PhoneExistException, MessagingException, UsernameExistException, IOException, NotAImageFileException, NotAnImageFileException {
        Perfil perfil = pService.updateUser(
                new Perfil(
                        request.getUsername(),
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
                        request.getDesportosFavoritos()),
                        file);
        return perfil;
    }
    public Perfil updateEmergency(UserUpdateRequest request) throws EmailExistException, PhoneExistException, MessagingException, UsernameExistException, IOException, NotAImageFileException {
        Perfil perfil = pService.updateUser2(
                new Perfil(
                        request.getUsername(),
                        request.getFullName(),
                        request.getDateOfBirth(),
                        request.getAddress(),
                        request.getCountry(),
                        request.getLocation(),
                        request.getIndicativePhone(),
                        request.getPostalCode(),
                        request.getPhone(),
                        request.getGender(),
                        request.getEmail(),
                        request.getDesportosFavoritos()));
        return perfil;
    }
}