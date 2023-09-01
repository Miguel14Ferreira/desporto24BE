package com.example.desporto24.changePassword;

import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.registo.token.ConfirmationToken;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class UserChangePasswordService {

    private final ProjectServiceImpl perfilService;

    /* Obtenção de dados para a alteração de username e password ou apenas de username/password */

    public Perfil alterarPassword(UserChangePasswordRequest changePassword) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException {
        Perfil p = perfilService.changeUsernameAndPassword(new Perfil(
                changePassword.getUsername(),
                changePassword.getNewUsername(),
                changePassword.getPassword()));
        return p;
    }

    /* Obtenção de dados para a alteração de password por email, é pedido ao utilizador para colocar o email */

    public Perfil alterarPasswordPorTokenStep1(UserChangePasswordRequest changePassword) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException, EmailNotVerifiedException {
        Perfil p = perfilService.resetPassword1(new Perfil(
                changePassword.getEmail()));
        return p;
    }

    /* Obtenção de dados para a definição da nova password para o Utilizador */

    public Perfil alterarPasswordPorTokenStep2(UserChangePasswordRequest changePassword) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException {
        Perfil p = perfilService.resetPassword2(new Perfil(
                changePassword.getUsername(),
                changePassword.getPassword()));
        return p;
    }
}
