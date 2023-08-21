package com.example.desporto24.service;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.Ideias;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.model.Sessao;
import com.example.desporto24.service.impl.NotAnImageFileException;
import jakarta.mail.MessagingException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ProjectService {

    Perfil signUpPerfil(Perfil perfil,MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException, NotAnImageFileException, jakarta.mail.MessagingException;

    Perfil signUpPerfil2(Perfil perfil);

    List<Perfil> getPerfis();

    Perfil login(Perfil perfil) throws EmailNotVerifiedException, AccountDisabledException;

    //UserDetails loadUserByUsername(String username);

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

    Perfil updatePerfilEmergency(String username);

    Perfil updateUser(String username,Perfil perfil, MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException, NotAnImageFileException;

    Perfil updateUserFoto(String perfil, MultipartFile foto) throws UsernameExistException, EmailExistException, PhoneExistException, IOException, NotAnImageFileException;

    void deleteUser(Long id);

    void sendVerificationCode(Perfil perfil);

    Ideias newIdea(Ideias i) throws MessagingException;

    Perfil changeUsernameAndPassword(Perfil perfil) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException;

    String confirmCode(String code);

    Perfil resetPassword1(Perfil perfil) throws MessagingException, EmailNotVerifiedException;

    Perfil resetPassword2(Perfil perfil);
}