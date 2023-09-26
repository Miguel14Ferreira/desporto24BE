package com.example.desporto24.login;

import com.example.desporto24.changePassword.UserChangePasswordRequest;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoginService {
    private final ProjectServiceImpl perfilService;

    /* Obtenção de dados para a alteração de username e password ou apenas de username/password */

    public Perfil login(LoginRequest loginRequest) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException, EmailNotVerifiedException {
        Perfil p = perfilService.login(new Perfil(
                loginRequest.getUsername(),
                loginRequest.getPassword()));
        return p;
    }
}
