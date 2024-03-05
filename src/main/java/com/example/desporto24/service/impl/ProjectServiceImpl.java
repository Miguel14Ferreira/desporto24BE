package com.example.desporto24.service.impl;
import com.example.desporto24.Chat.ChatRepository;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.*;
import com.example.desporto24.registo.FriendRequest.FriendRequest;
import com.example.desporto24.registo.FriendRequest.FriendRequestService;
import com.example.desporto24.registo.MFA.MFAVerification;
import com.example.desporto24.registo.MFA.MFAVerificationRepository;
import com.example.desporto24.registo.MFA.MFAVerificationService;
import com.example.desporto24.registo.Notifications.Notifications;
import com.example.desporto24.registo.Notifications.NotificationsRepository;
import com.example.desporto24.registo.token.ConfirmationToken;
import com.example.desporto24.registo.token.ConfirmationTokenRepository;
import com.example.desporto24.registo.token.ConfirmationTokenService;
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
import static com.example.desporto24.enumeration.Role.ROLE_USER;
import static com.example.desporto24.model.Status.OFFLINE;
import static com.example.desporto24.model.Status.ONLINE;
import static com.example.desporto24.utility.SmsUtils.sendSMS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
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
    private PerfilRepository perfilRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private SessaoRepository sessaoRepository;
    private final MFAVerificationService MFAverificationService;
    private final MFAVerificationRepository mfaVerificationRepository;
    private final IdeiasRepository ideiasRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestService friendRequestService;
    private final NotificationsRepository notificationsRepository;
    private final ChatRepository chatRepository;
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;

    @Autowired
    public ProjectServiceImpl(PerfilRepository perfilRepository, BCryptPasswordEncoder passwordEncoder, EmailService emailService, ConfirmationTokenService confirmationTokenService, ConfirmationTokenRepository confirmationTokenRepository1, SessaoRepository sessaoRepository, MFAVerificationService mfaVerificationService, MFAVerificationRepository mfaVerificationRepository, IdeiasRepository ideiasRepository, LoginAttemptService loginAttemptService, ConfirmationTokenRepository confirmationTokenRepository, FriendRepository friendRepository, FriendRequestService friendRequestService, NotificationsRepository notificationsRepository, ChatRepository chatRepository) {
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
        this.confirmationTokenRepository = confirmationTokenRepository1;
        this.sessaoRepository = sessaoRepository;
        this.MFAverificationService = mfaVerificationService;
        this.mfaVerificationRepository = mfaVerificationRepository;
        this.ideiasRepository = ideiasRepository;
        this.friendRepository = friendRepository;
        this.friendRequestService = friendRequestService;
        this.notificationsRepository = notificationsRepository;
        this.chatRepository = chatRepository;
    }



    /*
    public String createSessao(Sessao sessao) throws SessionExistException, jakarta.mail.MessagingException {
        Perfil perfil = perfilService.findSessaoByUsername(sessao.getUsername());
        validateNewSessao(sessao.getUsername(), sessao.getMorada(), sessao.getDataDeJogo());
        String encodedPassword = passwordEncoder.encode(sessao.getPassword());
        sessao.setPassword(encodedPassword);
        sessao.setFoto(getTemporaryProfileImageURL2(sessao.getUsername()));
        sessaoRepository.save(sessao);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), perfil);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = "http://localhost:8080/cancelNewSession/cancelRegistrationToken?token=" + token;
        emailService.send(perfil.getEmail(), buildSessionCancelationEmail(sessao.getUsername(), link, sessao.getDesporto(), sessao.getDataDeJogo(), sessao.getJogadores(), sessao.getLocalidade(), sessao.getMorada(), sessao.getPreco(), sessao.getPassword()));
        return token;
    }
     */

    // Validação de criação de uma nova sessão, verifica que se a sessão nova pode ser criada
    private Sessao validateNewSessao(String currentUsername, String morada, Date dataDeJogo) throws SessionExistException {
        Sessao findSessaoByNewMorada = findSessaoByMorada(morada);
        Sessao findSessaoByNewDataDeJogo = findSessaoByDatadejogo(dataDeJogo);
        Sessao currentSession = findSessaoByUsername(currentUsername);
        if (isNotBlank(currentUsername)) {
            if (currentSession == null) {
                throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + currentUsername);
            }
            if (findSessaoByNewMorada != null && !currentSession.getId().equals(findSessaoByNewMorada.getId()) && !currentSession.getId().equals(findSessaoByNewDataDeJogo.getId())) {
                throw new SessionExistException(SESSION_ALREADY_EXIST);
            }
        }
        return currentSession;
    }

    // Encontra uma sessão/sessões pelo utilizador
    @Override
    public Sessao findSessaoByUsername(String username) {
        return sessaoRepository.findSessaoByUsername(username);
    }

    // Encontra todas as sessões disponíveis
    @Override
    public List<Sessao> getSessoes() {
        return sessaoRepository.findAll();
    }

    @Override
    public int deleteSessao(String email) {
        return 0;
    }

    public void evictUserFromLoginAttemptCache(Perfil perfil) {
        perfil.setLogginAttempts(0);
    }

    public void addUserToLoginAttemptCache(Perfil perfil) {
        Perfil p = findUserByUsername(perfil.getUsername());
        int attempts = perfil.getLogginAttempts();
        do {
            attempts = ATTEMPT_INCREMENT + perfil.getLogginAttempts();
        }
        while (perfil.getPassword() != p.getPassword());
    }

    public boolean hasExceededMaxAttempts(Perfil perfil) {
        addUserToLoginAttemptCache(perfil);
        try {
            return perfil.getLogginAttempts() >= MAXIMUM_NUMBER_OF_ATTEMPTS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Verifica se a conta do utilizador está ativa
    private Perfil validateLoginAttempt(Perfil perfil) throws EmailNotVerifiedException {
        try {
            if (perfil.getEnabled().equals(false)) {
                throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
            } else {
                return perfil;
            }
        } catch (EmailNotVerifiedException e) {
            throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
        }
    }

        // Verificação para que um novo utilizador não tenha o mesmo nome, telemóvel ou email de outro utilizador
    public Perfil validateNewUsernameEmailAndPhone(String currentUsername, String newUsername, String email, String phone) throws UsernameExistException, EmailExistException, PhoneExistException, EmailNotFoundException, UserNotFoundException {
        try {
            Perfil userByNewUsername = findUserByUsername(newUsername);
            Perfil userByNewEmail = findUserByEmail(email);
            Perfil userByNewPhone = findUserByPhone(phone);
            if (isNotBlank(currentUsername)) {
                Perfil currentUser = findUserByUsername(currentUsername);
                if (currentUser == null) {
                    throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + currentUsername);
                }
                if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                    throw new UsernameExistException(USERNAME_ALREADY_EXIST);
                }
                if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                    throw new EmailExistException(EMAIL_ALREADY_EXIST);
                }
                if (userByNewPhone != null && !currentUser.getId().equals(userByNewPhone.getId())) {
                    throw new PhoneExistException(PHONE_ALREADY_REGISTRED);
                }
                return currentUser;
            } else {
                if (userByNewUsername != null) {
                    throw new UsernameExistException(USERNAME_ALREADY_EXIST);
                }
                if (userByNewEmail != null) {
                    throw new EmailExistException(EMAIL_ALREADY_EXIST);
                }
                if (userByNewPhone != null) {
                    throw new PhoneExistException(PHONE_ALREADY_REGISTRED);
                }
                return null;
            }
        } catch (UserNotFoundException e){
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME);
        } catch (UsernameExistException e){
        throw new UsernameExistException(USERNAME_ALREADY_EXIST);
    }catch (EmailExistException e){
            throw new EmailExistException(EMAIL_ALREADY_EXIST);
        }catch (PhoneExistException e){
            throw new PhoneExistException(PHONE_ALREADY_REGISTRED);
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
    public Perfil signUpPerfil(Perfil perfil, MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, NotAnImageFileException, jakarta.mail.MessagingException, EmailNotFoundException, UserNotFoundException {
        validateNewUsernameEmailAndPhone(EMPTY, perfil.getUsername(), perfil.getEmail(), perfil.getPhone());
        String encodedPassword = passwordEncoder.encode(perfil.getPassword());
        perfil.setOldEmail(perfil.getEmail());
        perfil.setPassword(encodedPassword);
        perfil.setUserId(generateUserId());
        perfil.setJoinDate(new Date());
        Date date = new Date();
        String data = substring(String.valueOf(date),3,10);
        String data2 = substring(String.valueOf(date),24,29);
        String data3 = data2+data;
        perfil.setJoinDateDisplay(data3);
        perfil.setEnabled(false);
        perfil.setNotLocked(true);
        perfil.setRole(ROLE_USER.name());
        perfil.setAuthorities(ROLE_USER.getAuthorities());
        perfil.setMFA(false);
        perfil.setLogginAttempts(0);
        perfil.setStatus(OFFLINE);
        saveProfileImage(perfil, foto);
        perfilRepository.save(perfil);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), perfil.getUsername());
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = fromCurrentContextPath().path("/login/registerNewUser/confirmTokenRegistration/"+token).toUriString();
        emailService.send(perfil.getEmail(), buildRegistrationEmail(perfil.getUsername(),link));
        String assuntoNotificaçãoBoasVindas = " Sê bem-vindo ao nosso site, "+perfil.getUsername()+"!";
        String notificacaoBoasVindas = "Aqui poderás consultar as sessões a acontecer de momento, se quiseres criar uma sessão ou alterar algo no teu perfil, basta passares o rato na tua fotografia no canto superior direito e um menu aparecerá para selecionares o que prentendes e também como é te já apercebeste, também podes ler as tuas notificações no canto superior esquerdo.";
        String cumprimentoNotificacaoBoasVindas = "Bons jogos,";
        String assinatura = "DESPORTO24";
        Notifications notification = new Notifications(assuntoNotificaçãoBoasVindas,notificacaoBoasVindas,cumprimentoNotificacaoBoasVindas,assinatura,data3,false,false,false,true,token,perfil.getUsername());
        notificationsRepository.save(notification);
        return perfil;
    }

    @Override
    public Perfil findUserByUsername(String username) {
        return perfilRepository.findUserByUsername(username);
    }

    @Override
    public Perfil findUserByEmail(String email) throws EmailNotFoundException {
            Perfil p =  perfilRepository.findUserByEmail(email);
                return p;
    }

    @Override
    public Perfil findUserByPhone(String phone) {
        return perfilRepository.findUserByPhone(phone);
    }

    @Override
    public Sessao findSessaoByMorada(String morada) {
        return sessaoRepository.findSessaoByMorada(morada);
    }

    @Override
    public Sessao findSessaoByDatadejogo(Date dataDeJogo) {
        return sessaoRepository.findSessaoByDataDeJogo(dataDeJogo);
    }

    public List<Perfil> procurarPerfil(String username){
        return perfilRepository.pesquisaPerfil(username);
    }

    @Override
    public Chat EnviarMensagem(Chat chat) throws Exception {
        Date date = new Date();
        String data = substring(String.valueOf(date),3,10);
        String data2 = substring(String.valueOf(date),24,29);
        String data3 = data2+data;
        chat = new Chat(chat.getUsername1(),chat.getUsername2(),chat.getTexto(),data3,date);
        chatRepository.save(chat);
        return chat;
    }

    @Override
    public Perfil terminarSessao(Perfil perfil) throws EmailNotFoundException {
        Perfil p = findUserByEmail(perfil.getEmail());
        p.setStatus(OFFLINE);
        return p;
    }

    @Override
    public Perfil findUserByUserId(String userId) {
        return perfilRepository.findUserByUserId(userId);
    }

    public List<Chat> findChatMessages(String sender,String recipient){
        List<Chat> chat1 = chatRepository.findByUsername1AndUsername2(sender,recipient);
        List<Chat> chat2 = chatRepository.findByUsername1AndUsername2(recipient,sender);
        List<Chat> chats = new ArrayList<>();
        chats.addAll(chat1);
        chats.addAll(chat2);
        Collections.sort(chats,Comparator.comparing(Chat::getDate));
        if (chats == null){
            return null;
        } else {
            return chats;
        }
    }

    // Alteração de dados pelo utilizador
    @Override
    public Perfil updateUser(String username, Perfil perfil, MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException, jakarta.mail.MessagingException, NotAnImageFileException, UserNotFoundException, EmailNotFoundException {
        Perfil p = findUserByUsername(username);
        if (p.getEmail().equals(perfil.getEmail())) {
            validateNewUsernameEmailAndPhone(username, null, null, perfil.getPhone());
        } if (p.getPhone().equals(perfil.getPhone())){
            validateNewUsernameEmailAndPhone(username,null,perfil.getEmail(),null);
        } else {
            validateNewUsernameEmailAndPhone(username,null,perfil.getEmail(),perfil.getPhone());
        }
        p.setOldEmail(p.getEmail());
        p.setFullName(perfil.getFullName());
        p.setCountry(perfil.getCountry());
        p.setLocation(perfil.getLocation());
        p.setPhone(perfil.getPhone());
        p.setAddress(perfil.getAddress());
        p.setGender(perfil.getGender());
        p.setEmail(perfil.getEmail());
        p.setDateOfBirth(perfil.getDateOfBirth());
        p.setPostalCode(perfil.getPostalCode());
        p.setMFA(perfil.getMFA());
        if (foto != null) {
            saveProfileImage(p, foto);
        }
        perfilRepository.save(p);
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
        perfilRepository.deleteById(id);
    }


    // envio do SMS para o utilizador
    @Override
    public String sendVerificationCode(Perfil perfil) {
        String code = randomAlphabetic(8).toUpperCase();
        MFAVerification mfaVerification = new MFAVerification(code, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1), perfil.getUsername());
        MFAverificationService.saveConfirmationMFA(mfaVerification);
        sendSMS(perfil.getIndicativePhone(), perfil.getPhone(), "Desporto24APP \nCodigo de Verificação:\n" + code);
        return code;
    }

    // Guardar fotografia no perfil
    private void saveProfileImage(Perfil perfil, MultipartFile profileImage) throws IOException, NotAnImageFileException {
        if (profileImage != null) {
            if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + perfil.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + perfil.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(perfil.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            perfil.setFoto(setProfileImageUrl(perfil.getUsername()));
            perfilRepository.save(perfil);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        } else {
            if (perfil.getFoto() == null) {
                perfil.setFoto(getTemporaryProfileImageURL(perfil.getUsername()));
            }
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
    public Perfil changeUsernameAndPassword(Perfil perfil) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException, EmailNotFoundException, UserNotFoundException {
        Perfil p = validateNewUsernameEmailAndPhone(perfil.getUsername(), perfil.getNewUsername(), null, null);
        if (p == null) {
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + perfil.getUsername());
        }
        if (perfil.getPassword().equals(p.getPassword()) && perfil.getNewUsername().equals(p.getUsername())) {
            throw new EqualUsernameAndPasswordException(EQUAL_USERNAME_PASSWORD);
        } else {
            String encodedPassword = passwordEncoder.encode(perfil.getPassword());
            p.setUsername(perfil.getNewUsername());
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

    public Perfil enablePerfil(String email) throws EmailNotFoundException {
        Perfil p = findUserByEmail(email);
        if (p == null) {
            LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + email);
            throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
        } else {
            p.setEnabled(true);
            perfilRepository.save(p);
        }
        return p;
    }

    public Perfil enablePerfil2(String email) throws EmailNotFoundException {
        Perfil p = findUserByEmail(email);
        if (p == null) {
            LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + email);
            throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
        } else {
            p.setNotLocked(true);
            perfilRepository.save(p);
        }
        return p;
    }
    public Perfil disablePerfil(String email) throws EmailNotFoundException {
        try {
            Perfil p = findUserByEmail(email);
            if (p == null) {
                LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + email);
                throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
            } else {
                p.setNotLocked(false);
                perfilRepository.save(p);
            }
            return p;
        } catch (EmailNotFoundException e){
            throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
        }
    }

    public Perfil disablePerfilByBlock(String email) throws MessagingException, EmailNotFoundException {
        try {
            Perfil p = findUserByEmail(email);
            if (p == null) {
                throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
            } else {
                p.setNotLocked(false);
                perfilRepository.save(p);
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
                Perfil p = findUserByUsername(confirmToken.getPerfil());
                confirmationTokenRepository.deleteByPerfil(p.getUsername());
                notificationsRepository.deleteByPerfil(p.getUsername());
                perfilRepository.deleteById(p.getId());
                throw new TokenExpiredException(TOKEN_EXPIRED);
            } else {
                confirmationTokenService.setConfirmedAt(token);
                Perfil p = findUserByUsername(confirmToken.getPerfil());
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
        }
    }

    // Desativa o perfil e envia um mail para o utilizador para a definição da nova palavra-passe para a conta
    public String confirmEmergencyToken(String token,String username) throws EmailNotFoundException, MessagingException, NotFoundException, AlreadyConfirmedTokenException, TokenExpiredException {
        try {
        Perfil p = findUserByUsername(username);
        p.setStatus(OFFLINE);
        perfilRepository.save(p);
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
        Perfil p = perfilRepository.findUserByOldEmail(email);
        if (p == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + email);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + email);
        } else {
            String encodedPassword = passwordEncoder.encode(password);
            p.setPassword(encodedPassword);
            enablePerfil2(p.getOldEmail());
            perfilRepository.save(p);
            return token;
        }
    }

    // Envia um email para o utilizador conseguir efetuar o reset da password
    public Perfil resetPassword1(Perfil perfil) throws EmailNotFoundException, EmailSendingException {
        try {
            Perfil p = findUserByEmail(perfil.getEmail());
            if (p == null){
                throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL);
            }
            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), p.getUsername());
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            String link = fromCurrentContextPath().path("/login/resetPassword/" + token + "/" + p.getUsername()).toUriString();
            emailService.send(p.getEmail(), buildResetPasswordEmail(p.getUsername(), link));
            return perfil;
        } catch (EmailNotFoundException e) {
            throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL);
        } catch (MessagingException e) {
            throw new EmailSendingException(EMAIL_ERROR);
        }
    }

        // Guarda a nova password no perfil, através do email
    public Perfil resetPassword2(Perfil perfil, String token) throws UserNotFoundException, NotFoundException, AlreadyConfirmedTokenException, TokenExpiredException, EmailNotFoundException {
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
            Perfil p = perfilRepository.findUserByEmail(perfil.getEmail());
            if (p == null) {
                throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL);
            } else {
                String encodedPassword = passwordEncoder.encode(perfil.getPassword());
                p.setPassword(encodedPassword);
                perfilRepository.save(p);
                return p;
            }
        } catch (NotFoundException e) {
            throw new NotFoundException(TOKEN_NOT_FOUND);
        } catch (AlreadyConfirmedTokenException e) {
            throw new AlreadyConfirmedTokenException(TOKEN_ALREADY_CONFIRMED);
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException(TOKEN_EXPIRED);
        } catch (EmailNotFoundException e) {
            throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL);
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
    public void acceptFriendRequest(Long id,String token){
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
        Perfil perfil1 = findUserByUsername(confirmToken.getPerfil1());
        Perfil perfil2 = findUserByUsername(confirmToken.getPerfil2());
        Friend friend = new Friend();
        Perfil user1 = perfil1;
        Perfil user2 = perfil2;
        if (perfil1.getId() > perfil2.getId()){
            perfil1 = user2;
            perfil2 = user1;
        }
        if (!(friendRepository.existsByPerfil1AndPerfil2(perfil1,perfil2))){
            friend.setCreatedAt(new Date());
            friend.setPerfil1(perfil1);
            friend.setPerfil2(perfil2);
            friendRepository.save(friend);
        }
    }

    // Listar amigos
    public List<Perfil> getFriends(String username){
        Perfil p = findUserByUsername(username);
        List<Friend> friendsByFirstPerfil = friendRepository.findByPerfil1(p);
        List<Friend> friendsBySecondPerfil = friendRepository.findByPerfil2(p);
        List<Perfil> friendsPerfis = new ArrayList<>();

        for (Friend friend : friendsByFirstPerfil){
            friendsPerfis.add(findUserByUsername(friend.getPerfil2().getUsername()));
        }
        for (Friend friend : friendsBySecondPerfil){
            friendsPerfis.add(findUserByUsername(friend.getPerfil1().getUsername()));
        }
        return friendsPerfis;
    }

    @Override
    public SendFriendRequest sendFriendRequest(SendFriendRequest friendRequest) throws RequestFriendException, MessagingException {
        Perfil p1 = findUserByUsername(friendRequest.getUsername1());
        Perfil p2 = findUserByUsername(friendRequest.getUsername2());
        if(!(friendRepository.existsByPerfil1AndPerfil2(p1,p2))){
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
    public Ideias newIdea(Ideias i) throws MessagingException {
        LOGGER.info(String.valueOf(i));
        emailService.send(i.getEmail(), buildNewIdeaEmail(i.getName()));
        emailService.send("desporto24app@gmail.com",buildNewIdeaEmail(i.getEmail(),i.getSubject(),i.getProblem()));
        ideiasRepository.save(i);
        return i;
    }

    // Nova sessão a ser criada
    public Sessao createSessao(Sessao sessao, MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, NotAnImageFileException, jakarta.mail.MessagingException, SessionExistException {
        validateNewSessao(sessao.getUsername(), sessao.getMorada(), sessao.getDataDeJogo());
        sessao.setCreated_at(new Date());
        saveSessaoImage(sessao, foto);
        sessaoRepository.save(sessao);
        return sessao;
    }

    // Cria uma nova pasta que guarda a fotografia da sessão desportiva e que também guarda a fotografia na sessão
    private void saveSessaoImage(Sessao sessao, MultipartFile profileImage) throws NotAnImageFileException, IOException {
        if (profileImage != null) {
            if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
                throw new NotAnImageFileException(profileImage.getOriginalFilename() + NOT_AN_IMAGE_FILE);
            }
            Path userFolder = Paths.get(USER_FOLDER + sessao.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + sessao.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(sessao.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            sessao.setFoto(setProfileImageUrl(sessao.getUsername()));
            sessaoRepository.save(sessao);
            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        } else {
            if (sessao.getFoto() == null) {
                sessao.setFoto(getTemporaryProfileImageURL(sessao.getUsername()));
            }
        }
    }

    // Login do utilizador
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Perfil p = findUserByUsername(username);
            validateLoginAttempt(p);
            p.setLastLoginDate(new Date());
            Date date = new Date();
            String data = substring(String.valueOf(date), 3, 10);
            String data2 = substring(String.valueOf(date), 24, 29);
            String data3 = data2 + data;
            p.setLastLoginDateDisplay(data3);
            p.setStatus(ONLINE);
            perfilRepository.save(p);
            LOGGER.info(RETURNING_FOUND_USER_BY_USERNAME + " " + p.getUsername());
            PerfilPrincipal perfilPrincipal = new PerfilPrincipal(p);
            return perfilPrincipal;
        } catch (EmailNotVerifiedException e){
            throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
        }
    }
    private Perfil validateLoginAttempt3(Perfil perfil) {
        if (perfil.getMFA().equals(true)) {
            sendVerificationCode(perfil);
        } else {
        }
        return perfil;
    }
    private Perfil validateLoginAttempt2(Perfil perfil) throws EmailNotVerifiedException {
        if (perfil.getEnabled().equals(false)) {
            throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
        } else {
            return perfil;
        }
    }

    public Perfil login(@NotNull Perfil perfil) throws EmailNotVerifiedException {
        Perfil p = findUserByUsername(perfil.getUsername());
        if (perfil.getUsername() == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + perfil.getUsername());
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + perfil.getUsername());
        } else {

            validateLoginAttempt3(p);
            p.setLastLoginDate(new Date());
            p.setStatus(ONLINE);
            perfilRepository.save(p);
            LOGGER.info(RETURNING_FOUND_USER_BY_USERNAME + " " + p.getUsername());
            return p;
        }
    }
}
