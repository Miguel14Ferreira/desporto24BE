package com.example.desporto24.login;

import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.registo.UserRegistoRequest;
import com.example.desporto24.registo.token.ConfirmationTokenService;
import com.example.desporto24.repository.PerfilRepository;
import com.example.desporto24.service.impl.NotAnImageFileException;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@AllArgsConstructor
@Service
public class LoginService {
    private final ProjectServiceImpl pService;

    // Obtenção de dados do utilizador para ser efetuado o login
    public Perfil login(LoginRequest request) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, NotAnImageFileException, jakarta.mail.MessagingException, EmailNotVerifiedException, AccountDisabledException {
        Perfil perfil = pService.login(
                new Perfil(
                        request.getUsername(),
                        request.getPassword()));
        return perfil;
    }
}
