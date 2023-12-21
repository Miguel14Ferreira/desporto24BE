package com.example.desporto24.service;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.*;
import com.example.desporto24.registo.Notifications.Notifications;
import com.example.desporto24.service.impl.NotAnImageFileException;
import jakarta.mail.MessagingException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProjectService{

    List<Perfil> getFriends(String username);

    List<Notifications> getNotificationsFromPerfil(String username);

    SendFriendRequest sendFriendRequest(SendFriendRequest sendFriendRequest) throws RequestFriendException, MessagingException;

    void acceptFriendRequest(Long id,String token);

    Perfil disablePerfil(String email) throws EmailNotFoundException;

    Perfil enablePerfil(String email) throws EmailNotFoundException;

    Perfil signUpPerfil(Perfil perfil,MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException, NotAnImageFileException, jakarta.mail.MessagingException;

    Perfil signUpPerfil2(Perfil email);

    List<Perfil> procurarPerfil(String username);

    Chat EnviarMensagem(Chat chat) throws Exception;

    Perfil terminarSessao(Perfil perfil);

    List<Chat> findChatMessages(String senderId,String recipientId);

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

    void deleteNotification(Long id);

    String sendVerificationCode(Perfil perfil);

    Ideias newIdea(Ideias i) throws MessagingException;

    Perfil changeUsernameAndPassword(Perfil perfil) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException;

    String confirmCode(String code);

    Perfil resetPassword1(Perfil perfil) throws MessagingException, EmailNotVerifiedException;

    Perfil resetPassword2(Perfil perfil, String token);

    String confirmToken(String token) throws EmailNotFoundException;
}