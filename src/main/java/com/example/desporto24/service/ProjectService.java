package com.example.desporto24.service;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.model.Sessao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ProjectService {

    String signUpPerfil(Perfil perfil) throws EmailExistException, PhoneExistException, UsernameExistException, MessagingException, IOException;

    List<Perfil> getPerfis();

    Perfil findUserByUsername(String username);

    Perfil findUserByEmail(String email);

    Perfil findUserByPhone(String phone);

    Sessao findSessaoByMorada(String morada);

    Sessao findSessaoByDatadejogo(Date dataDeJogo);

    Sessao findSessaoByUsername(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM Sessao a WHERE a.username = ?1")
    int deleteSessao(String email);

    Perfil updateUser(Perfil perfil) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException;

    void deleteUser(Long id);

    String changeUsernameAndPassword(Perfil perfil) throws MessagingException, EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException;
}