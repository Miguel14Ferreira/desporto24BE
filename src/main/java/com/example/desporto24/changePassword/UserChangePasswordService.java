package com.example.desporto24.changePassword;

import com.example.desporto24.exception.domain.EmailExistException;
import com.example.desporto24.exception.domain.EqualUsernameAndPasswordException;
import com.example.desporto24.exception.domain.PhoneExistException;
import com.example.desporto24.exception.domain.UsernameExistException;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@AllArgsConstructor
public class UserChangePasswordService {

    private final ProjectServiceImpl perfilService;

    public String alterarPassword(UserChangePasswordRequest changePassword) throws EqualUsernameAndPasswordException, MessagingException, EmailExistException, PhoneExistException, UsernameExistException {
        String p = perfilService.changeUsernameAndPassword(new Perfil(
                changePassword.getUsername(),
                changePassword.getNewUsername(),
                changePassword.getPassword()));
        return p;
    }
}
