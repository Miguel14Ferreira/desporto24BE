package com.example.desporto24.update;

import com.example.desporto24.exception.domain.EmailExistException;
import com.example.desporto24.exception.domain.NotAImageFileException;
import com.example.desporto24.exception.domain.PhoneExistException;
import com.example.desporto24.exception.domain.UsernameExistException;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@AllArgsConstructor
@Service
public class UserUpdateService {
    private final ProjectServiceImpl pService;

    public Perfil update(UserUpdateRequest request) throws EmailExistException, PhoneExistException, MessagingException, UsernameExistException, IOException, NotAImageFileException {
        Perfil perfil = pService.updateUser(
                new Perfil(
                        request.getUsername(),
                        request.getFullName(),
                        request.getDateOfBirth(),
                        request.getAddress(),
                        request.getCountry(),
                        request.getLocation(),
                        request.getPhone(),
                        request.getDesportosFavoritos(),
                        request.getGender(),
                        request.getEmail(),
                        request.getFoto()));
        return perfil;
    }
}