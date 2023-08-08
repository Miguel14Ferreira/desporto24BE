package com.example.desporto24.changePassword;

import com.example.desporto24.exception.domain.EmailExistException;
import com.example.desporto24.exception.domain.EqualUsernameAndPasswordException;
import com.example.desporto24.exception.domain.PhoneExistException;
import com.example.desporto24.exception.domain.UsernameExistException;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserChangePasswordService {

    private ProjectServiceImpl perfilService;

    @Autowired
    public UserChangePasswordService(ProjectServiceImpl perfilService) {
        this.perfilService = perfilService;
    }

    public String alterarPassword(UserChangePasswordRequest changePassword) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException {
        String p = perfilService.changeUsernameAndPassword(new Perfil(
                changePassword.getUsername(),
                changePassword.getNewUsername(),
                changePassword.getPassword()));
        return p;
    }
}
