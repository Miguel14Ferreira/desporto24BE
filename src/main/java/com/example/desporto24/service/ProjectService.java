package com.example.desporto24.service;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.Ideias;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.model.Sessao;
import com.example.desporto24.service.impl.NotAnImageFileException;
import jakarta.mail.MessagingException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ProjectService{

    List<Perfil> getFriends(String username);

    Perfil sendFriendRequest(String usernamep1,String usernamep2) throws RequestFriendException;

    Perfil disablePerfil(String email) throws EmailNotFoundException;

    Perfil enablePerfil(String email) throws EmailNotFoundException;

    Perfil signUpPerfil(Perfil perfil,MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException, NotAnImageFileException, jakarta.mail.MessagingException;

    Perfil signUpPerfil2(Perfil email);

    List<Perfil> getPerfis();

    Perfil findUserByUsername(String username);

    Perfil findUserByEmail(String email);

    Perfil findUserByPhone(String phone);

    Sessao findSessaoByMorada(String morada);

    Sessao findSessaoByDatadejogo(Date dataDeJogo);

    Sessao findSessaoByUsername(String username);

    List<Sessao> getSessoes();

    @Transactional
    @Modifying
    @Query("DELETE FROM Sessao a WHERE a.username = ?1")
    int deleteSessao(String email);

    String confirmEmergencyToken(String token) throws EmailNotFoundException;

    Perfil updateUser(String username, Perfil perfil, MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException, NotAnImageFileException;

    void deleteUser(Long id);

    String sendVerificationCode(Perfil perfil);

    Ideias newIdea(Ideias i) throws MessagingException;

    Perfil changeUsernameAndPassword(Perfil perfil) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException;

    String confirmCode(String code);

    Perfil resetPassword1(Perfil perfil) throws MessagingException, EmailNotVerifiedException;

    Perfil resetPassword2(Perfil perfil, String token);

    String confirmToken(String token) throws EmailNotFoundException;
}