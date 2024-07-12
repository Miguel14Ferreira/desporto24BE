package com.example.desporto24.service.impl;
import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.encryption.AES;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.*;
import com.example.desporto24.register.FriendRequest.FriendRequest;
import com.example.desporto24.register.FriendRequest.FriendRequestService;
import com.example.desporto24.register.MFA.MFAVerification;
import com.example.desporto24.register.MFA.MFAVerificationRepository;
import com.example.desporto24.register.MFA.MFAVerificationService;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.register.Notifications.NotificationsRepository;
import com.example.desporto24.register.token.ConfirmationToken;
import com.example.desporto24.register.token.ConfirmationTokenRepository;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.repository.*;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.LoginAttemptService;
import com.example.desporto24.service.ProjectService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.desporto24.constant.FileConstant.*;
import static com.example.desporto24.constant.SessionImplConstant.SESSION_ALREADY_EXIST;
import static com.example.desporto24.constant.UserImplConstant.*;
import static com.example.desporto24.encryption.AES.decryptString;
import static com.example.desporto24.enumeration.Role.ROLE_USER;
import static com.example.desporto24.model.Status.OFFLINE;
import static com.example.desporto24.model.Status.ONLINE;
import static com.example.desporto24.utility.SmsUtils.sendSMS;
import static java.lang.Boolean.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Qualifier("userDetailsService")
public class ProjectServiceImpl implements ProjectService,UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(UserDetailsService.class);
    private ProfileRepository profileRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private SessionRepository sessionRepository;
    private final MFAVerificationService MFAverificationService;
    private final MFAVerificationRepository mfaVerificationRepository;
    private final IdeasRepository ideasRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestService friendRequestService;
    private final NotificationsRepository notificationsRepository;
    private final ChatRepository chatRepository;
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;

    @Autowired
    public ProjectServiceImpl(ProfileRepository profileRepository, BCryptPasswordEncoder passwordEncoder, EmailService emailService, ConfirmationTokenService confirmationTokenService, ConfirmationTokenRepository confirmationTokenRepository1, SessionRepository sessionRepository, MFAVerificationService mfaVerificationService, MFAVerificationRepository mfaVerificationRepository, IdeasRepository ideasRepository, LoginAttemptService loginAttemptService, ConfirmationTokenRepository confirmationTokenRepository, FriendRepository friendRepository, FriendRequestService friendRequestService, NotificationsRepository notificationsRepository, ChatRepository chatRepository) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
        this.confirmationTokenRepository = confirmationTokenRepository1;
        this.sessionRepository = sessionRepository;
        this.MFAverificationService = mfaVerificationService;
        this.mfaVerificationRepository = mfaVerificationRepository;
        this.ideasRepository = ideasRepository;
        this.friendRepository = friendRepository;
        this.friendRequestService = friendRequestService;
        this.notificationsRepository = notificationsRepository;
        this.chatRepository = chatRepository;
    }


    // Validação de criação de uma nova sessão, verifica que se a sessão nova pode ser criada
    private Session validateNewSessao(String currentUsername, String morada, Date dataDeJogo) throws SessionExistException {
        Session findSessionByNewMorada = findSessaoByMorada(morada);
        Session findSessionByNewDataDeJogo = findSessaoByDatadejogo(dataDeJogo);
        Session currentSession = findSessaoByUsername(currentUsername);
        if (isNotBlank(currentUsername)) {
            if (currentSession == null) {
                throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + currentUsername);
            }
            if (findSessionByNewMorada != null && !currentSession.getId().equals(findSessionByNewMorada.getId()) && !currentSession.getId().equals(findSessionByNewDataDeJogo.getId())) {
                throw new SessionExistException(SESSION_ALREADY_EXIST);
            }
        }
        return currentSession;
    }

    // Encontra uma sessão/sessões pelo utilizador
    @Override
    public Session findSessaoByUsername(String username) {
        return sessionRepository.findSessionByUsername(username);
    }

    // Encontra todas as sessões disponíveis
    @Override
    public List<Session> getSessoes() {
        return sessionRepository.findAll();
    }

    @Override
    public int deleteSessao(String email) {
        return 0;
    }

    public void evictUserFromLoginAttemptCache(Profile profile) {
        profile.setLogginAttempts(0);
    }

    public void addUserToLoginAttemptCache(Profile profile) throws Exception {
        Profile p = findUserByUsername(profile.getUsername());
        int attempts = profile.getLogginAttempts();
        do {
            attempts = ATTEMPT_INCREMENT + profile.getLogginAttempts();
        }
        while (profile.getPassword() != p.getPassword());
    }

    public boolean hasExceededMaxAttempts(Profile profile) throws Exception {
        addUserToLoginAttemptCache(profile);
        try {
            return profile.getLogginAttempts() >= MAXIMUM_NUMBER_OF_ATTEMPTS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Verifica se a conta do utilizador está ativa
    private Profile validateLoginAttempt(Profile profile) throws EmailNotVerifiedException {
        try {
            if (profile.getEnabled().equals(false)) {
                throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
            } else {
                return profile;
            }
        } catch (EmailNotVerifiedException e) {
            throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
        }
    }

    // Verificação para que um novo utilizador não tenha o mesmo nome ou email de outro utilizador
    public Profile validateNewUsernameEmail(String currentUsername, String newUsername, String email) throws UsernameExistException, EmailExistException, UserNotFoundException {
        try {
            Profile userByNewUsername = findUserByUsername(newUsername);
            Profile userByNewEmail = findUserByEmail(email);
            if (isNotBlank(currentUsername)) {
                Profile currentUser = findUserByUsername(currentUsername);
                if (currentUser == null) {
                    throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + currentUsername);
                }
                if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                    throw new UsernameExistException(USERNAME_ALREADY_EXIST);
                }
                if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                    throw new EmailExistException(EMAIL_ALREADY_EXIST);
                }
                return currentUser;
            } else {
                if (userByNewUsername != null) {
                    throw new UsernameExistException(USERNAME_ALREADY_EXIST);
                }
                if (userByNewEmail != null) {
                    throw new EmailExistException(EMAIL_ALREADY_EXIST);
                }
                return null;
            }
        } catch (UserNotFoundException e){
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME);
        } catch (UsernameExistException e){
            throw new UsernameExistException(USERNAME_ALREADY_EXIST);
        }catch (EmailExistException e){
            throw new EmailExistException(EMAIL_ALREADY_EXIST);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
        private String buildEmergencyResetPasswordMail(String name, String link) {
        return
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                        "\n" +
                        "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "    <tbody><tr>\n" +
                        "        \n" +
                        "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "          <tbody><tr>\n" +
                        "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td style=\"padding-left:10px\">\n" +
                        "                  \n" +
                        "                    </td>\n" +
                        "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Desporto24</span>\n" +
                        "                    </td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "              </a>\n" +
                        "            </td>\n" +
                        "          </tr>\n" +
                        "        </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                        "      <td>\n" +
                        "        \n" +
                        "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                        "        \n" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + " (se não reconheces este username, é porque provavelmente foi alterado),</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Pedimos-te agora para definires uma nova palavra-passe através deste link: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Clica aqui para alterares a tua palavra-passe</a> </p> <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Recomendamos-te que depois de alterares a tua palavra-passe que entres na tua conta e verifica se outros dados foram alterados para que tu possas alterá-los.</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Atenção, este link vai expirar dentro de 15 minutos.</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cumprimentos,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">DESPORTO24APP</p>" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                        "\n" +
                        "</div></div>";
    }

    // envio do email para ser efetuado o reset à password
    private String buildResetPasswordEmail(String name, String link) {
        return
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                        "\n" +
                        "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "    <tbody><tr>\n" +
                        "        \n" +
                        "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "          <tbody><tr>\n" +
                        "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td style=\"padding-left:10px\">\n" +
                        "                  \n" +
                        "                    </td>\n" +
                        "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Desporto24</span>\n" +
                        "                    </td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "              </a>\n" +
                        "            </td>\n" +
                        "          </tr>\n" +
                        "        </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                        "      <td>\n" +
                        "        \n" +
                        "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                        "        \n" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Face ao teu pedido para definir uma palavra-passe, aqui tens o link para defini-la </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Clica aqui para alterares a tua palavra-passe</a><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Atenção, este link vai expirar dentro de 15 minutos.</p> </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cumprimentos,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">DESPORTO24APP</p>" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                        "\n" +
                        "</div></div>";
    }

    // gerador de UserID
    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    // novo registo pelo utilizador e definição de alguns parametros para a classe perfil
    public Profile signUpPerfil(Profile profile, MultipartFile foto) throws NotAnImageFileException, Exception {
        validateNewUsernameEmail(EMPTY, profile.getUsername(), profile.getEmail());
        String encodedPassword = passwordEncoder.encode(profile.getPassword());
        profile.setPassword(encodedPassword);

        profile.setUserId(generateUserId());

        String encodedPhone = AES.encryptString(profile.getPhone(), profile.getUserId());
        profile.setPhone(encodedPhone);

        String encodedAddress  = AES.encryptString(profile.getAddress(), profile.getUserId());
        profile.setAddress(encodedAddress);

        profile.setOldEmail(profile.getEmail());


        profile.setJoinDate(new Date());
        Date date = new Date();
        String data = substring(String.valueOf(date),3,10);
        String data2 = substring(String.valueOf(date),24,29);
        String data3 = data2+data;
        profile.setJoinDateDisplay(data3);
        profile.setEnabled(false);
        profile.setNotLocked(true);
        profile.setRole(ROLE_USER.name());
        profile.setAuthorities(ROLE_USER.getAuthorities());
        profile.setMFA(false);
        profile.setLogginAttempts(0);
        profile.setStatus(OFFLINE);
        saveProfileImage(profile, foto);
        profileRepository.save(profile);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), profile.getUsername());
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = fromCurrentContextPath().path("/login/registerNewUser/confirmTokenRegistration/"+token).toUriString();
        emailService.send(profile.getEmail(), buildRegistrationEmail(profile.getUsername(),link));
        String assuntoNotificaçãoBoasVindas = " Sê bem-vindo ao nosso site, "+ profile.getUsername()+"!";
        String notificacaoBoasVindas = "Aqui poderás consultar as sessões a acontecer de momento, se quiseres criar uma sessão ou alterar algo no teu perfil, basta passares o rato na tua fotografia no canto superior direito e um menu aparecerá para selecionares o que prentendes e também como é te já apercebeste, também podes ler as tuas notificações no canto superior esquerdo.";
        String cumprimentoNotificacaoBoasVindas = "Bons jogos,";
        String assinatura = "DESPORTO24";
        Notifications notification = new Notifications(assuntoNotificaçãoBoasVindas,notificacaoBoasVindas,cumprimentoNotificacaoBoasVindas,assinatura,data3,false,false,false,true,token, profile.getUsername());
        notificationsRepository.save(notification);
        return profile;
    }

    @Override
    public Profile findUserByUsername(String username) throws Exception {
        Profile p = profileRepository.findUserByUsername(username);


//        if (p != null && p.getEnabled()){
//            System.out.println(p.getPhone());
//            p.setPhone(decryptString(p.getPhone(), p.getUserId()));
//            System.out.println(p.getPhone());
//            p.setAddress(decryptString(p.getAddress(), p.getUserId()));
//        }

        return p;
    }

    @Override
    public Profile findUserByEmail(String email) throws EmailNotFoundException {
            Profile p =  profileRepository.findUserByEmail(email);
                return p;
    }

    @Override
    public Profile findUserByPhone(String phone) {
        return profileRepository.findUserByPhone(phone);
    }

    @Override
    public Session findSessaoByMorada(String morada) {
        return sessionRepository.findSessionByAddress(morada);
    }

    @Override
    public Session findSessaoByDatadejogo(Date dataDeJogo) {
        return sessionRepository.findSessionByGameDate(dataDeJogo);
    }

    public List<Profile> procurarPerfil(String username){
        return profileRepository.pesquisaProfile(username);
    }

    @Override
    public Chat EnviarMensagem(Chat chat) throws Exception {
        Profile profile1 = profileRepository.findUserByUsername(chat.getUsername1());
        Profile profile2 = profileRepository.findUserByUsername(chat.getUsername2());
        Friend friend = friendRepository.findByProfile1AndProfile2(profile1,profile2);
        if (friend.isChatBlocked()){
            throw new ChatBlockedException(CHAT_BLOCKED);
        }
        Date date = new Date();
        String data = substring(String.valueOf(date),3,10);
        String data2 = substring(String.valueOf(date),24,29);
        String data3 = data2+data;
        chat = new Chat(chat.getUsername1(),chat.getUsername2(),chat.getText(),data3,date);
        chatRepository.save(chat);
        return chat;
    }

    @Override
    public Profile terminarSessao(Profile profile) throws EmailNotFoundException {
        Profile p = findUserByEmail(profile.getEmail());
        p.setStatus(OFFLINE);
        return p;
    }

    @Override
    public Profile findUserByUserId(String userId) {
        return profileRepository.findUserByUserId(userId);
    }

    public List<Chat> findChatMessages(String sender,String recipient){
        List<Chat> chat1 = chatRepository.findByUsername1AndUsername2(sender,recipient);
        List<Chat> chat2 = chatRepository.findByUsername1AndUsername2(recipient,sender);
        List<Chat> chats = new ArrayList<>();
        chats.addAll(chat1);
        chats.addAll(chat2);
        chats.sort(Comparator.comparing(Chat::getDate));
        return chats;
    }

    // Alteração de dados pelo utilizador
    @Override
    public Profile updateUser(String username, Profile profile, MultipartFile foto) throws Exception, NotAnImageFileException {
        Profile p = findUserByUsername(username);
        if (p.getEmail().equals(profile.getEmail())) {
            validateNewUsernameEmail(username, null, null);
        } if (p.getPhone().equals(profile.getPhone())){
            validateNewUsernameEmail(username,null, profile.getEmail());
        } else {
            validateNewUsernameEmail(username,null, profile.getEmail());
        }
        p.setOldEmail(p.getEmail());
        p.setFullName(profile.getFullName());
        p.setCountry(profile.getCountry());
        p.setLocation(profile.getLocation());
        String encodedPhone = AES.encryptString(profile.getPhone(), p.getUserId());
        p.setPhone(encodedPhone);
        String encodedAddress  = AES.encryptString(profile.getAddress(), p.getUserId());
        p.setAddress(encodedAddress);
        p.setGender(profile.getGender());
        p.setEmail(profile.getEmail());
        p.setDateOfBirth(profile.getDateOfBirth());
        p.setPostalCode(profile.getPostalCode());
        p.setMFA(profile.getMFA());
        if (foto != null) {
            saveProfileImage(p, foto);
        }
        profileRepository.save(p);
        String token = UUID.randomUUID().toString();
        ConfirmationToken emergencyToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), p.getUsername());
        confirmationTokenService.saveConfirmationToken(emergencyToken);
        String link = fromCurrentContextPath().path("/confirmEmergencyToken/"+token+"/"+p.getUsername()).toUriString();
        emailService.send(p.getOldEmail(), buildChangePerfilEmail(p.getUsername(), link));
        String assuntoNotificacaoAlteracaoDados = " Alteração de Dados de perfil";
        String notificacaoAlteracaoDados = "Foram feitas alterações dos dados pessoais do teu perfil, se não foste tu clica para bloquearmos temporareamente a tua conta.";
        String cumprimentosNotificacao = "Cumprimentos,";
        String assinatura = "DESPORTO24";
        Date date = new Date();
        String data = substring(String.valueOf(date),3,10);
        String data2 = substring(String.valueOf(date),24,29);
        String data3 = data2+data;
        Notifications notification = new Notifications(assuntoNotificacaoAlteracaoDados,notificacaoAlteracaoDados,cumprimentosNotificacao,assinatura,data3,false,true,false,false,link,username);
        notificationsRepository.save(notification);
        return p;
    }


    @Override
    public void deleteUser(Long id) {
        profileRepository.deleteById(id);
    }


    // envio do SMS para o utilizador
    @Override
    public String sendVerificationCode(Profile profile) throws Exception {
        String code = randomAlphabetic(8).toUpperCase();
        MFAVerification mfaVerification = new MFAVerification(code, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), profile.getUsername());
        MFAverificationService.saveConfirmationMFA(mfaVerification);
        sendSMS(profile.getIndicativePhone(), decryptString(profile.getPhone(), profile.getUserId()), "Desporto24APP \nCodigo de Verificação:\n" + code);
        return code;
    }

    // Guardar fotografia no perfil
    private void saveProfileImage(Profile profile, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + profile.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + profile.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(profile.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            profile.setProfilePic(setProfileImageUrl(profile.getUsername()));
            profileRepository.save(profile);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        } else {
            if (profile.getProfilePic() == null) {
                profile.setProfilePic(getTemporaryProfileImageURL(profile.getUsername()));
            }
        }
    }

    private void saveSessionImage(Session session, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + session.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + session.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(session.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            session.setGamePhoto(setProfileImageUrl(session.getUsername()));
            sessionRepository.save(session);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    // Criação de URL para a fotografia escolhida pelo utilizador no registo ou na alteração de dados
    private String setProfileImageUrl(String username) {
        return fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
    }

    // Criação de URL quando o utilizador não escolhe qualquer fotografia pelo utilizador no registo ou na alteração de dados
    private String getTemporaryProfileImageURL(String username) {
        return fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    // Alteração do nome e da password
    @Override
    public Profile changeUsernameAndPassword(Profile profile) throws EqualUsernameAndPasswordException, EmailExistException, UsernameExistException, jakarta.mail.MessagingException, UserNotFoundException {
        Profile p = validateNewUsernameEmail(profile.getUsername(), profile.getNewUsername(), null);
        if (p == null) {
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + profile.getUsername());
        }
        if (profile.getPassword().equals(p.getPassword()) && profile.getNewUsername().equals(p.getUsername())) {
            throw new EqualUsernameAndPasswordException(EQUAL_USERNAME_PASSWORD);
        } else {
            String encodedPassword = passwordEncoder.encode(profile.getPassword());
            p.setUsername(profile.getNewUsername());
            p.setPassword(encodedPassword);
            String token = UUID.randomUUID().toString();
            ConfirmationToken emergencyToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), p.getUsername());
            confirmationTokenService.saveConfirmationToken(emergencyToken);
            String link = fromCurrentContextPath().path("/confirmEmergencyToken/"+token+"/"+p.getUsername()).toUriString();
            emailService.send(p.getEmail(), buildChangePerfilEmail(p.getUsername(), link));
            String assuntoNotificacaoAlteracaoDados = " Alteração de Dados de perfil";
            String notificacaoAlteracaoDados = "Foram feitas alterações dos dados pessoais do teu perfil, se não foste tu clica para bloquearmos temporareamente a tua conta.";
            String cumprimentosNotificacao = "Cumprimentos,";
            String assinatura = "DESPORTO24";
            Date date = new Date();
            String data = substring(String.valueOf(date),3,10);
            String data2 = substring(String.valueOf(date),24,29);
            String data3 = data2+data;
            Notifications notification = new Notifications(assuntoNotificacaoAlteracaoDados,notificacaoAlteracaoDados,cumprimentosNotificacao,assinatura,data3,false,true,false,false,link,p.getUsername());
            notificationsRepository.save(notification);
            return p;
        }
    }

    public Profile enablePerfil(String email) throws EmailNotFoundException {
        Profile p = findUserByEmail(email);
        if (p == null) {
            LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + email);
            throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
        } else {
            p.setEnabled(true);
            profileRepository.save(p);
        }
        return p;
    }

    public Profile enablePerfil2(String email) throws EmailNotFoundException {
        Profile p = findUserByEmail(email);
        if (p == null) {
            LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + email);
            throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
        } else {
            p.setNotLocked(true);
            profileRepository.save(p);
        }
        return p;
    }
    public Profile disablePerfil(String email) throws EmailNotFoundException {
        try {
            Profile p = findUserByEmail(email);
            if (p == null) {
                LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + email);
                throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
            } else {
                p.setNotLocked(false);
                profileRepository.save(p);
            }
            return p;
        } catch (EmailNotFoundException e){
            throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
        }
    }

    public Profile disablePerfilByBlock(String email) throws MessagingException, EmailNotFoundException {
        try {
            Profile p = findUserByEmail(email);
            if (p == null) {
                throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
            } else {
                p.setNotLocked(false);
                profileRepository.save(p);
                String token2 = UUID.randomUUID().toString();
                ConfirmationToken confirmationToken = new ConfirmationToken(token2, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), p.getUsername());
                confirmationTokenService.saveConfirmationToken(confirmationToken);
                String link = fromCurrentContextPath().path("/confirmEmergencyToken/resetPassword/" + token2 + "/" + p.getOldEmail()).toUriString();
                emailService.send(p.getOldEmail(), buildEmergencyResetPasswordMail(p.getUsername(), link));
            }
            return p;
        } catch (EmailNotFoundException e) {
            throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
        }
    }


    // Activa o perfil
    public String confirmToken(String token) throws EmailNotFoundException, NotFoundException, AlreadyConfirmedTokenException, TokenExpiredException {
        try {
            ConfirmationToken confirmToken = confirmationTokenService
                    .getToken(token)
                    .orElseThrow(() ->
                            new NotFoundException(TOKEN_NOT_FOUND));
            if (confirmToken.getConfirmedAt() != null) {
                throw new AlreadyConfirmedTokenException(TOKEN_ALREADY_CONFIRMED);
            }
            if (confirmToken.getExpiredAt().isBefore(LocalDateTime.now())) {
                Profile p = findUserByUsername(confirmToken.getPerfil());
                confirmationTokenRepository.deleteByPerfil(p.getUsername());
                notificationsRepository.deleteByPerfil(p.getUsername());
                profileRepository.deleteById(p.getId());
                throw new TokenExpiredException(TOKEN_EXPIRED);
            } else {
                confirmationTokenService.setConfirmedAt(token);
                Profile p = findUserByUsername(confirmToken.getPerfil());
                enablePerfil(p.getEmail());
                confirmationTokenService.saveConfirmationToken(confirmToken);
                return token;
            }
        } catch (NotFoundException e){
            throw new NotFoundException(TOKEN_NOT_FOUND);
        } catch (AlreadyConfirmedTokenException e){
            throw new AlreadyConfirmedTokenException(TOKEN_ALREADY_CONFIRMED);
        } catch (TokenExpiredException e){
            throw new TokenExpiredException(TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String confirmSession(String token) throws EmailNotFoundException, NotFoundException, AlreadyConfirmedTokenException, TokenExpiredException {
        try {
            ConfirmationToken confirmToken = confirmationTokenService
                    .getToken(token)
                    .orElseThrow(() ->
                            new NotFoundException(TOKEN_NOT_FOUND));
            if (confirmToken.getConfirmedAt() != null) {
                throw new AlreadyConfirmedTokenException(TOKEN_ALREADY_CONFIRMED);
            }
            if (confirmToken.getExpiredAt().isBefore(LocalDateTime.now())) {
                Profile p = findUserByUsername(confirmToken.getPerfil());
                sessionRepository.deleteById(p.getId());
                throw new TokenExpiredException(TOKEN_EXPIRED);
            } else {
                confirmationTokenService.setConfirmedAt(token);
                Profile p = findUserByUsername(confirmToken.getPerfil());
                enablePerfil(p.getEmail());
                confirmationTokenService.saveConfirmationToken(confirmToken);
                return token;
            }
        } catch (NotFoundException e){
            throw new NotFoundException(TOKEN_NOT_FOUND);
        } catch (AlreadyConfirmedTokenException e){
            throw new AlreadyConfirmedTokenException(TOKEN_ALREADY_CONFIRMED);
        } catch (TokenExpiredException e){
            throw new TokenExpiredException(TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Desativa o perfil e envia um mail para o utilizador para a definição da nova palavra-passe para a conta
    public String confirmEmergencyToken(String token,String username) throws EmailNotFoundException, MessagingException, NotFoundException, AlreadyConfirmedTokenException, TokenExpiredException {
        try {
        Profile p = findUserByUsername(username);
        p.setStatus(OFFLINE);
        profileRepository.save(p);
        ConfirmationToken confirmToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new NotFoundException(TOKEN_NOT_FOUND));
        if (confirmToken.getConfirmedAt() != null) {
            throw new AlreadyConfirmedTokenException(TOKEN_ALREADY_CONFIRMED);
        }
        if (confirmToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(TOKEN_EXPIRED);
        }
        confirmationTokenService.setConfirmedAt(token);
        disablePerfil(p.getEmail());
        confirmationTokenService.saveConfirmationToken(confirmToken);
        String token2 = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token2, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), confirmToken.getPerfil());
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = fromCurrentContextPath().path("/confirmEmergencyToken/resetPassword/"+token2+"/"+p.getOldEmail()).toUriString();
        emailService.send(p.getOldEmail(), buildEmergencyResetPasswordMail(p.getUsername(),link));
        return token;
        } catch (NotFoundException e){
            throw new NotFoundException(TOKEN_NOT_FOUND);
        } catch (AlreadyConfirmedTokenException e){
            throw new AlreadyConfirmedTokenException(TOKEN_ALREADY_CONFIRMED);
        } catch (TokenExpiredException e){
            throw new TokenExpiredException(TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Confirmação do código enviado por SMS (MFA autenticação)
    public String confirmCode(String code) throws SMSNotFoundException, SMSAlreadyConfirmedException, SMSExpiredException {
        try {
            MFAVerification mfaVerification = MFAverificationService
                    .getCode(code)
                    .orElseThrow(() ->
                            new SMSNotFoundException(SMS_FAIL));
            if (mfaVerification.getConfirmedAt() != null) {
                throw new SMSAlreadyConfirmedException(SMS_ALREADY_CONFIRMED);
            }
            if (mfaVerification.getExpiredAt().isBefore(LocalDateTime.now())) {
                throw new SMSExpiredException(SMS_EXPIRED);
            }
            MFAverificationService.setConfirmedAt(code);
            return code;
        } catch (SMSNotFoundException e) {
            throw new SMSNotFoundException(SMS_FAIL);
        } catch (SMSAlreadyConfirmedException e) {
            throw new SMSAlreadyConfirmedException(SMS_ALREADY_CONFIRMED);
        } catch (SMSExpiredException e) {
            throw new SMSExpiredException(SMS_EXPIRED);
        }
    }
    // envio do email de registo para o utilizador conseguir ativar a sua conta
    private String buildRegistrationEmail(String name, String link) {
        return
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                        "\n" +
                        "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "    <tbody><tr>\n" +
                        "        \n" +
                        "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "          <tbody><tr>\n" +
                        "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td style=\"padding-left:10px\">\n" +
                        "                  \n" +
                        "                    </td>\n" +
                        "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Registo no Desporto24!</span>\n" +
                        "                    </td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "              </a>\n" +
                        "            </td>\n" +
                        "          </tr>\n" +
                        "        </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                        "      <td>\n" +
                        "        \n" +
                        "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                        "        \n" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Obrigado por te registares na nossa aplicação. Clica neste link para ativar a tua conta: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Clica aqui para finalizar o teu registo</a> </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cumprimentos,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">DESPORTO24APP</p>" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                        "\n" +
                        "</div></div>";
    }

    // Email enviado ao utilizador ao alterar os seus dados pessoais
    private String buildChangePerfilEmail(String name, String link) {
        return
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                        "\n" +
                        "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "    <tbody><tr>\n" +
                        "        \n" +
                        "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "          <tbody><tr>\n" +
                        "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td style=\"padding-left:10px\">\n" +
                        "                  \n" +
                        "                    </td>\n" +
                        "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Alteração de dados pessoais</span>\n" +
                        "                    </td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "              </a>\n" +
                        "            </td>\n" +
                        "          </tr>\n" +
                        "        </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                        "      <td>\n" +
                        "        \n" +
                        "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                        "        \n" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Dados pessoais foram alterados no teu perfil. </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Se não foste tu quem fez esta alteração, \n por favor clica neste link para bloquearmos temporareamente a tua conta: \n <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Clica aqui para bloquearmos a tua conta</a> </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Após bloqueares a tua conta irás receber um novo mail para definires a tua nova palavra-passe e proceder ao desbloqueio da conta.</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Este link vai expirar numa hora. </p> \n<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cumprimentos,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">DESPORTO24APP</p>" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                        "\n" +
                        "</div></div>";
    }

    public String EmergencyResetPassword(String token, String email,String password) throws EmailNotFoundException {
        ConfirmationToken confirmToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token não encontrado."));
        if (confirmToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token já foi confirmado.");
        }
        if (confirmToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expirado");
        }
        confirmationTokenService.setConfirmedAt(token);
        Profile p = profileRepository.findUserByOldEmail(email);
        if (p == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + email);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + email);
        } else {
            String encodedPassword = passwordEncoder.encode(password);
            p.setPassword(encodedPassword);
            enablePerfil2(p.getOldEmail());
            profileRepository.save(p);
            return token;
        }
    }

    // Envia um email para o utilizador conseguir efetuar o reset da password
    public Profile resetPassword1(Profile profile) throws EmailNotFoundException, EmailSendingException {
        try {
            Profile p = findUserByEmail(profile.getEmail());
            if (p == null){
                throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL);
            }
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), p.getUsername());
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            String link = fromCurrentContextPath().path("/login/resetPassword/" + token + "/" + p.getUsername()).toUriString();
            emailService.send(p.getEmail(), buildResetPasswordEmail(p.getUsername(), link));
            return profile;
        } catch (EmailNotFoundException e) {
            throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL);
        } catch (MessagingException e) {
            throw new EmailSendingException(EMAIL_ERROR);
        }
    }

        // Guarda a nova password no perfil, através do email
    public Profile resetPassword2(Profile profile, String token) throws UserNotFoundException, NotFoundException, AlreadyConfirmedTokenException, TokenExpiredException, EmailNotFoundException {
        try {
            ConfirmationToken confirmToken = confirmationTokenService
                    .getToken(token)
                    .orElseThrow(() ->
                            new NotFoundException(TOKEN_NOT_FOUND));
            if (confirmToken.getConfirmedAt() != null) {
                throw new AlreadyConfirmedTokenException(TOKEN_ALREADY_CONFIRMED);
            }
            if (confirmToken.getExpiredAt().isBefore(LocalDateTime.now())) {
                throw new TokenExpiredException(TOKEN_EXPIRED);
            }
            confirmationTokenService.setConfirmedAt(token);
            Profile p = profileRepository.findUserByUsername(profile.getUsername());
            if (p == null) {
                throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME);
            } else if (!p.getNotLocked()) {
                throw new AccountDisabledException(ACCOUNT_DISABLED);
            } else {
                String encodedPassword = passwordEncoder.encode(profile.getPassword());
                p.setPassword(encodedPassword);
                profileRepository.save(p);
                return p;
            }
        } catch (NotFoundException e) {
            throw new NotFoundException(TOKEN_NOT_FOUND);
        } catch (AlreadyConfirmedTokenException e) {
            throw new AlreadyConfirmedTokenException(TOKEN_ALREADY_CONFIRMED);
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException(TOKEN_EXPIRED);
        } catch (AccountDisabledException e) {
            throw new RuntimeException(e);
        }
    }


        // Email enviado quando um utilizador coloca uma nova ideia para a aplicação
    private String buildNewIdeaEmail(String name) {
        return
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                        "\n" +
                        "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "    <tbody><tr>\n" +
                        "        \n" +
                        "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "          <tbody><tr>\n" +
                        "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td style=\"padding-left:10px\">\n" +
                        "                  \n" +
                        "                    </td>\n" +
                        "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Nova sugestão</span>\n" +
                        "                    </td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "              </a>\n" +
                        "            </td>\n" +
                        "          </tr>\n" +
                        "        </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                        "      <td>\n" +
                        "        \n" +
                        "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                        "        \n" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Queremos agradecer-te pela tua sugestão e responderemos o mais rápidamente possível. </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Poderemos demorar entre uma semana a duas a responder \n por isso não fiques surpreso se demorarmos algum tempo a responder de volta \n <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Cumprimentos,</p> <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">DESPORTO24APP</p>" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                        "\n" +
                        "</div></div>";
    }
    private String buildNewIdeaEmail(String name, String assunto, String mensagem) {
        return
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                        "\n" +
                        "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "    <tbody><tr>\n" +
                        "        \n" +
                        "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "          <tbody><tr>\n" +
                        "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td style=\"padding-left:10px\">\n" +
                        "                  \n" +
                        "                    </td>\n" +
                        "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Nova sugestão</span>\n" +
                        "                    </td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "              </a>\n" +
                        "            </td>\n" +
                        "          </tr>\n" +
                        "        </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                        "      <td>\n" +
                        "        \n" +
                        "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                        "        \n" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Foi enviada uma nova sugestão por: " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Assunto: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n "+ assunto + " <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Mensagem: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n"+ mensagem +"</p>" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                        "\n" +
                        "</div></div>";
    }

    private String buildNewFriendRequestEmail(String name) {
        return
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                        "\n" +
                        "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "    <tbody><tr>\n" +
                        "        \n" +
                        "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "          <tbody><tr>\n" +
                        "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td style=\"padding-left:10px\">\n" +
                        "                  \n" +
                        "                    </td>\n" +
                        "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Recebeste um novo pedido de amizade</span>\n" +
                        "                    </td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "              </a>\n" +
                        "            </td>\n" +
                        "          </tr>\n" +
                        "        </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                        "      <td>\n" +
                        "        \n" +
                        "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                        "        \n" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Foi te enviado um novo pedido de amizade por " + name + ".</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Para aceitar basta simplesmente dirigires-te à app. <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cumprimentos,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">DESPORTO24APP</p>" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                        "\n" +
                        "</div></div>";
    }
    public void acceptFriendRequest(Long id,String token) throws Exception {
        FriendRequest confirmToken = friendRequestService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token não encontrado."));
        if (confirmToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token já foi confirmado.");
        }
        notificationsRepository.deleteById(id);
        friendRequestService.setConfirmedAt(token);
        friendRequestService.saveFriendRequest(confirmToken);
        Profile profile1 = findUserByUsername(confirmToken.getPerfil1());
        Profile profile2 = findUserByUsername(confirmToken.getPerfil2());
        Profile user1 = profile1;
        Profile user2 = profile2;
        if (profile1.getId() > profile2.getId()){
            profile1 = user2;
            profile2 = user1;
        }
            Friend friend = new Friend();
            friend.setCreatedAt(new Date());
            friend.setProfile1(profile1);
            friend.setProfile2(profile2);
            friend.setChatBlocked(false);
            friendRepository.save(friend);
    }

    @Override
    public void blockFriend(String firstProfile, String secondPerfil) {
        Profile profile1 = profileRepository.findUserByUsername(firstProfile);
        Profile profile2 = profileRepository.findUserByUsername(secondPerfil);
        Friend friend = friendRepository.findByProfile1AndProfile2(profile1,profile2);
        if(friend != null){
            friend.setChatBlocked(true);
            friend.setWhoBlocked(profile1.getUsername());
            friendRepository.save(friend);
        }
    }

    // Listar amigos
    public List<Profile> getFriends(String username) throws Exception {
        Profile p = findUserByUsername(username);
        List<Friend> friendsByFirstPerfil = friendRepository.findByProfile1(p);
        List<Friend> friendsBySecondPerfil = friendRepository.findByProfile2(p);
        List<Profile> friendsPerfis = new ArrayList<>();

        for (Friend friend : friendsByFirstPerfil){
            friendsPerfis.add(findUserByUsername(friend.getProfile2().getUsername()));
        }
        for (Friend friend : friendsBySecondPerfil){
            friendsPerfis.add(findUserByUsername(friend.getProfile1().getUsername()));
        }
        return friendsPerfis;
    }

    @Override
    public SendFriendRequest sendFriendRequest(SendFriendRequest friendRequest) throws Exception {
        Profile p1 = findUserByUsername(friendRequest.getUsername1());
        Profile p2 = findUserByUsername(friendRequest.getUsername2());
        Friend friend = friendRepository.findByProfile1AndProfile2(p1,p2);
        if(friend == null) {
            String token = UUID.randomUUID().toString();
            Date date = new Date();
            String data = substring(String.valueOf(date),3,10);
            String data2 = substring(String.valueOf(date),24,29);
            String data3 = data2+data;
            FriendRequest newFriendRequest = new FriendRequest(token, data3, p1.getUsername(), p2.getUsername());
            friendRequestService.saveFriendRequest(newFriendRequest);
            emailService.send(p2.getEmail(), buildNewFriendRequestEmail(p1.getUsername()));
            String assuntoFriendRequestNotification = " Recebeste um novo pedido de amizade!";
            String friendRequestNotification = "Recebeste um novo pedido de amizade vindo de " + p1.getUsername() + ", podes aceitar ou rejeitar este pedido.";
            String cumprimentosFriendRequestNotification = "Cumprimentos,";
            String assinatura = "DESPORTO24";
            Notifications n = new Notifications(assuntoFriendRequestNotification,friendRequestNotification,cumprimentosFriendRequestNotification,assinatura,data3,true,false,false,false,token,p2.getUsername());
            notificationsRepository.save(n);
        } else {
            throw new RequestFriendException("Vocês já são amigos!");
        }
        return friendRequest;
    }
    @Override
    public List<Notifications> getNotificationsFromPerfil(String username) {
        List<Notifications> notificationsFromPerfil = notificationsRepository.findByPerfil(username);
        return notificationsFromPerfil;
    }

    public void deleteNotification(Long id){
        notificationsRepository.deleteById(id);
    }


    // Nova ideia/sugestão recebida
    public Ideas newIdea(Ideas i) throws MessagingException {
        LOGGER.info(String.valueOf(i));
        emailService.send(i.getEmail(), buildNewIdeaEmail(i.getName()));
        emailService.send("desporto24app@gmail.com",buildNewIdeaEmail(i.getEmail(),i.getSubject(),i.getProblem()));
        ideasRepository.save(i);
        return i;
    }

    // Nova sessão a ser criada
    public Session createSessao(Session sessao, MultipartFile foto) throws Exception, NotAnImageFileException {
        Profile perfil = findUserByUsername(sessao.getUsername());
        validateNewSessao(sessao.getUsername(), sessao.getAddress(), sessao.getGameDate());
        String encodedPassword = passwordEncoder.encode(sessao.getPassword());
        sessao.setPassword(encodedPassword);
        sessao.setCreated_at(new Date());
        sessao.setEnabled(FALSE);
        sessionRepository.save(sessao);
        saveSessionImage(sessao,foto);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), perfil.getUsername());
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = "http://localhost:8080/createNewSession?token=" + token;
        emailService.send(perfil.getEmail(), buildCreateSessionEmail(sessao.getUsername(), link, sessao.getSport(), sessao.getGameDate(), sessao.getPlayers(), sessao.getCity(), sessao.getAddress(), sessao.getPrice(), sessao.getPassword()));
        return sessao;
    }

    private String buildCreateSessionEmail(String username, String link, String sport, Date gameDate, String players, String city, String address, String price, String password) {
        return
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                        "\n" +
                        "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "    <tbody><tr>\n" +
                        "        \n" +
                        "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "          <tbody><tr>\n" +
                        "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td style=\"padding-left:10px\">\n" +
                        "                  \n" +
                        "                    </td>\n" +
                        "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Registo no Desporto24!</span>\n" +
                        "                    </td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "              </a>\n" +
                        "            </td>\n" +
                        "          </tr>\n" +
                        "        </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                        "      <td>\n" +
                        "        \n" +
                        "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                        "        \n" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + username + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Antes de procederes com a criação da tua sessão, iremos pedir-te para confirmares os dados que introduziste e ver se está tudo correto: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " +
                        "      Tipo de jogo:" + sport + "</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Data de jogo:" + gameDate + "</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Nº de jogadores: "+ players +"</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " +
                        "       Localidade:" + city + "</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Morada do campo: "+ address + "</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Preço: " + price + "</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">"    +
                        "       Depois de confirmares os dados, clica no link abaixo para ativar a tua sessão: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Clica aqui</a> </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Nota, se verificares que algo não corresponde ao que desejavas, iremos pedir para não pressionares no link e que procedas à criação de uma nova sessão<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cumprimentos,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">DESPORTO24APP</p>" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                        "\n" +
                        "</div></div>";
    }

    // Login do utilizador
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Profile p = findUserByUsername(username);
            validateLoginAttempt(p);
            p.setLastLoginDate(new Date());
            Date date = new Date();
            String data = substring(String.valueOf(date), 3, 10);
            String data2 = substring(String.valueOf(date), 24, 29);
            String data3 = data2 + data;
            p.setLastLoginDateDisplay(data3);
            p.setStatus(ONLINE);
            profileRepository.save(p);
            LOGGER.info(RETURNING_FOUND_USER_BY_USERNAME + " " + p.getUsername());
            ProfilePrincipal profilePrincipal = new ProfilePrincipal(p);
            return profilePrincipal;
        } catch (EmailNotVerifiedException e){
            throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
        }
    }
    private Profile validateLoginAttempt3(Profile profile) throws Exception {
        if (profile.getMFA().equals(true)) {
            sendVerificationCode(profile);
        } else {
        }
        return profile;
    }
    private Profile validateLoginAttempt2(Profile profile) throws EmailNotVerifiedException {
        if (profile.getEnabled().equals(false)) {
            throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
        } else {
            return profile;
        }
    }

    public Profile login(@NotNull Profile profile) throws Exception {
        Profile p = findUserByUsername(profile.getUsername());
        if (profile.getUsername() == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + profile.getUsername());
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + profile.getUsername());
        } else {

            validateLoginAttempt3(p);
            p.setLastLoginDate(new Date());
            p.setStatus(ONLINE);
            profileRepository.save(p);
            LOGGER.info(RETURNING_FOUND_USER_BY_USERNAME + " " + p.getUsername());
            return p;
        }
    }
}
