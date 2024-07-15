package com.example.desporto24.changePassword;

import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class UserChangePasswordService {

    private final ProjectServiceImpl perfilService;

    /* Obtenção de dados para a alteração de username e password ou apenas de username/password */

    public Profile alterarPassword(UserChangePasswordRequest changePassword) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException, UserNotFoundException, EmailNotFoundException {
        return perfilService.changeUsernameAndPassword(new Profile(
                changePassword.getUsername(),
                changePassword.getNewUsername(),
                changePassword.getPassword()));
    }

    /* Obtenção de dados para a alteração de password por email, é pedido ao utilizador para colocar o email */

    public Profile alterarPasswordPorTokenStep1(UserChangePasswordRequest changePassword) throws EmailNotFoundException, EmailSendingException {
        return perfilService.resetPassword1(new Profile(
                changePassword.getEmail()));
    }

    /* Obtenção de dados para a definição da nova password para o Utilizador */

    public Profile alterarPasswordPorTokenStep2(UserChangePasswordRequest changePassword, String token) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException, UserNotFoundException, EmailNotFoundException, TokenExpiredException, AlreadyConfirmedTokenException, NotFoundException, AccountDisabledException {
        return perfilService.resetPassword2(new Profile(
                changePassword.getUsername(),
                changePassword.getPassword()),token);
    }
}
