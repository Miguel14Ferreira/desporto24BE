package com.example.desporto24.service;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.*;
import com.example.desporto24.register.Notifications.Notifications;
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

    List<Profile> getFriends(String username) throws Exception;

    List<Notifications> getNotificationsFromPerfil(String username);

    SendFriendRequest sendFriendRequest(SendFriendRequest sendFriendRequest) throws Exception;

    void acceptFriendRequest(Long id,String token) throws Exception;

    Profile disablePerfil(String email) throws EmailNotFoundException;

    Profile enablePerfil(String email) throws EmailNotFoundException;

    Profile signUpPerfil(Profile profile, MultipartFile foto) throws Exception, NotAnImageFileException;

    List<Profile> procurarPerfil(String username);

    Chat EnviarMensagem(Chat chat) throws Exception;

    Profile terminarSessao(Profile profile) throws EmailNotFoundException;

    Profile findUserByUserId(String userId);

    List<Chat> findChatMessages(String senderId,String recipientId);

    Profile findUserByUsername(String username) throws Exception;

    Profile findUserByEmail(String email) throws EmailNotFoundException;

    Profile findUserByPhone(String phone);

    Session findSessaoByMorada(String morada);

    Session findSessaoByDatadejogo(Date dataDeJogo);

    Session findSessaoByUsername(String username);
    
    List<Session> getSessoes();

    @Transactional
    @Modifying
    @Query("DELETE FROM Sessao a WHERE a.username = ?1")
    int deleteSessao(String email);

    String confirmEmergencyToken(String token,String username) throws EmailNotFoundException, MessagingException, NotFoundException, AlreadyConfirmedTokenException, TokenExpiredException;

    String EmergencyResetPassword(String token, String username, String password) throws EmailNotFoundException, MessagingException;

    Profile updateUser(String username, Profile profile, MultipartFile foto) throws Exception, NotAnImageFileException;

    void deleteUser(Long id);

    Profile disablePerfilByBlock(String username) throws MessagingException, EmailNotFoundException;

    void deleteNotification(Long id);

    String sendVerificationCode(Profile profile) throws Exception;

    Ideas newIdea(Ideas i) throws MessagingException;

    Profile changeUsernameAndPassword(Profile profile) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException, UserNotFoundException, EmailNotFoundException;

    String confirmCode(String code) throws NotFoundException, SMSNotFoundException, SMSAlreadyConfirmedException, SMSExpiredException;

    Profile resetPassword1(Profile profile) throws Exception;

    Profile resetPassword2(Profile profile, String token) throws UserNotFoundException, NotFoundException, AlreadyConfirmedTokenException, TokenExpiredException, EmailNotFoundException;

    String confirmToken(String token) throws EmailNotFoundException, NotFoundException, AlreadyConfirmedTokenException, TokenExpiredException;

    String confirmSession(String token) throws EmailNotFoundException, NotFoundException, AlreadyConfirmedTokenException, TokenExpiredException;
}