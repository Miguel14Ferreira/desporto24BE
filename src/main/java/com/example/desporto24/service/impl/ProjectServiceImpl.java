package com.example.desporto24.service.impl;

import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.*;
import com.example.desporto24.registo.FriendRequest.FriendRequest;
import com.example.desporto24.registo.FriendRequest.FriendRequestService;
import com.example.desporto24.registo.MFA.MFAVerification;
import com.example.desporto24.registo.MFA.MFAVerificationService;
import com.example.desporto24.registo.UserRegistoService;
import com.example.desporto24.registo.token.ConfirmationToken;
import com.example.desporto24.registo.token.ConfirmationTokenRepository;
import com.example.desporto24.registo.token.ConfirmationTokenService;
import com.example.desporto24.repository.FriendRepository;
import com.example.desporto24.repository.IdeiasRepository;
import com.example.desporto24.repository.PerfilRepository;
import com.example.desporto24.repository.SessaoRepository;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.LoginAttemptService;
import com.example.desporto24.service.ProjectService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
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
import static com.example.desporto24.utility.SmsUtils.sendSMS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Qualifier("userDetailsService")
public class ProjectServiceImpl implements ProjectService,UserDetailsService {


    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private Logger LOGGER = LoggerFactory.getLogger(UserDetailsService.class);
    private PerfilRepository perfilRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private ProjectService perfilService;
    private EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;
    private UserRegistoService registoService;
    private SessaoRepository sessaoRepository;
    private final MFAVerificationService MFAverificationService;
    private final ExceptionHandling exceptionHandling;
    private final IdeiasRepository ideiasRepository;
    private final LoginAttemptService loginAttemptService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestService friendRequestService;
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;

    @Autowired
    public ProjectServiceImpl(PerfilRepository perfilRepository, BCryptPasswordEncoder passwordEncoder, EmailService emailService, ConfirmationTokenService confirmationTokenService, SessaoRepository sessaoRepository, MFAVerificationService mfaVerificationService, ExceptionHandling exceptionHandling, IdeiasRepository ideiasRepository, LoginAttemptService loginAttemptService, ConfirmationTokenRepository confirmationTokenRepository, FriendRepository friendRepository, FriendRequestService friendRequestService) {
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
        this.sessaoRepository = sessaoRepository;
        this.MFAverificationService = mfaVerificationService;
        this.exceptionHandling = exceptionHandling;
        this.ideiasRepository = ideiasRepository;
        this.loginAttemptService = loginAttemptService;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.friendRepository = friendRepository;
        this.friendRequestService = friendRequestService;
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
        if (perfil.getEnabled().equals(false)) {
            throw new EmailNotVerifiedException(TOKEN_NOT_VERIFIED);
        } else {
            return perfil;
        }
    }

    // Verificação para que um novo utilizador não tenha o mesmo nome, telemóvel ou email de outro utilizador
    public Perfil validateNewUsernameEmailAndPhone(String currentUsername, String newUsername, String email, String phone) throws UsernameExistException, EmailExistException, PhoneExistException {
        Perfil userByNewUsername = findUserByUsername(newUsername);
        Perfil userByNewEmail = findUserByEmail(email);
        Perfil userByNewPhone = findUserByPhone(phone);
        if (isNotBlank(currentUsername)) {
            Perfil currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + currentUsername);
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
                throw new UsernameNotFoundException(USERNAME_ALREADY_EXIST);
            }
            if (userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXIST);
            }
            if (userByNewPhone != null) {
                throw new PhoneExistException(PHONE_ALREADY_REGISTRED);
            }
            return null;
        }
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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Pediste da nossa parte para efetuares um reset à tua password. Clica neste link para fazemos isso: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " + link + " </p><p>Cumprimentos,</p><p>DESPORTO24APP</p>" +
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
    public Perfil signUpPerfil(Perfil perfil, MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, NotAnImageFileException, jakarta.mail.MessagingException {
        validateNewUsernameEmailAndPhone(EMPTY, perfil.getUsername(), perfil.getEmail(), perfil.getPhone());
        String encodedPassword = passwordEncoder.encode(perfil.getPassword());
        perfil.setPassword(encodedPassword);
        perfil.setUserId(generateUserId());
        perfil.setJoinDate(new Date());
        perfil.setEnabled(false);
        perfil.setNotLocked(true);
        perfil.setRole(ROLE_USER.name());
        perfil.setAuthorities(ROLE_USER.getAuthorities());
        perfil.setMFA(false);
        perfil.setLogginAttempts(0);
        saveProfileImage(perfil, foto);
        perfilRepository.save(perfil);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), perfil);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = fromCurrentContextPath().path("/login/registerNewUser/confirmTokenRegistration/"+token).toUriString();
        emailService.send(perfil.getEmail(), buildRegistrationEmail(perfil.getUsername(),link));
        return perfil;
    }

    // ativação da conta do utilizador acabado de efetuar o seu registo
    public Perfil signUpPerfil2(Perfil email) {
        Perfil p = perfilRepository.findUserByEmail(email.getEmail());
        if (p == null) {
            LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + email.getEmail());
            throw new UsernameNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email.getEmail());
        } else {
            String token = "registrationNewAccountToken" + email.getEmail();
            ConfirmationToken confirmationToken = confirmationTokenService
                    .getToken(token)
                    .orElseThrow(() ->
                            new IllegalStateException("token não encontrado."));
            if (confirmationToken.getConfirmedAt() != null) {
                throw new IllegalStateException("Este link já foi clicado.");
            }
            if (confirmationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("Código expirado");
            }
            confirmationTokenService.setConfirmedAt(token);
            perfilRepository.save(p);
            return p;
        }
    }

    @Override
    public List<Perfil> getPerfis() {
        return perfilRepository.findAll();
    }

    @Override
    public Perfil findUserByUsername(String username) {
        return perfilRepository.findUserByUsername(username);
    }

    @Override
    public Perfil findUserByEmail(String email) {
        return perfilRepository.findUserByEmail(email);
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

    // Alteração de dados pelo utilizador
    @Override
    public Perfil updateUser(String username, Perfil perfil, MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException, jakarta.mail.MessagingException, NotAnImageFileException {
        Perfil p = findUserByUsername(username);
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
        String token = randomNumeric(20).toString();
        ConfirmationToken emergencyToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), p);
        confirmationTokenService.saveConfirmationToken(emergencyToken);
        String link = fromCurrentContextPath().path("/confirmEmergencyToken/"+token).toUriString();
        emailService.send(p.getEmail(), buildChangePerfilEmail(p.getUsername(), link));
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
        MFAVerification mfaVerification = new MFAVerification(code, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), perfil);
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
    public Perfil changeUsernameAndPassword(Perfil perfil) throws EqualUsernameAndPasswordException, EmailExistException, PhoneExistException, UsernameExistException, jakarta.mail.MessagingException {
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
            String token = randomNumeric(20).toString();
            ConfirmationToken emergencyToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), p);
            confirmationTokenService.saveConfirmationToken(emergencyToken);
            String link = fromCurrentContextPath().path("/confirmEmergencyToken/"+token).toUriString();
            emailService.send(p.getEmail(), buildChangePerfilEmail(p.getUsername(), link));
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
    public Perfil disablePerfil(String email) throws EmailNotFoundException {
        Perfil p = findUserByEmail(email);
        if (p == null) {
            LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + email);
            throw new EmailNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + email);
        } else {
            p.setNotLocked(false);
            perfilRepository.save(p);
        }
        return p;
    }

    // Activa o perfil
    public String confirmToken(String token) throws EmailNotFoundException {
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
        enablePerfil(confirmToken.getPerfil().getEmail());
        confirmationTokenService.saveConfirmationToken(confirmToken);
        return token;
    }

    // Desativa o perfil
    public String confirmEmergencyToken(String token) throws EmailNotFoundException {
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
        disablePerfil(confirmToken.getPerfil().getEmail());
        confirmationTokenService.saveConfirmationToken(confirmToken);
        return token;
    }

    // Confirmação do código enviado por SMS (MFA autenticação)
    public String confirmCode(String code) {
        MFAVerification mfaVerification = MFAverificationService
                .getCode(code)
                .orElseThrow(() ->
                        new IllegalStateException("codigo não encontrado."));
        if (mfaVerification.getConfirmedAt() != null) {
            throw new IllegalStateException("Código já foi confirmado.");
        }
        if (mfaVerification.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Código expirado");
        }
        MFAverificationService.setConfirmedAt(code);
        return code;
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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Obrigado por te registares na nossa aplicação. Clica neste link para ativar a tua conta: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " + link + " </p><p>Cumprimentos,</p><p>DESPORTO24APP</p>" +
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
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#000000;text-decoration:none;vertical-align:top;display:inline-block\">Alteração de dados</span>\n" +
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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Dados foram alterados na tua conta. </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Se não foste tu quem fez esta alteração, \n por favor clica neste link para bloquearmos temporareamente a tua conta: \n <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " + link + " </p>\n Este link vai expirar numa hora. \n <p>Cumprimentos,</p><p>DESPORTO24APP</p>" +
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

    // Envia um email para o utilizador conseguir efetuar o reset da password
    public Perfil resetPassword1(Perfil perfil) throws MessagingException, EmailNotVerifiedException {
        perfil = perfilRepository.findUserByEmail(perfil.getEmail());
        if (perfil == null) {
            LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + perfil.getEmail());
            throw new UsernameNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + perfil.getEmail());
        } else {
            String token = randomNumeric(20).toString();
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), perfil);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            String link = fromCurrentContextPath().path("/login/resetPassword/"+token+"/"+perfil.getUsername()).toUriString();
            emailService.send(perfil.getEmail(), buildResetPasswordEmail(perfil.getUsername(), link));
            return perfil;
        }
    }

    // Guarda a nova password no perfil, através do email
    public Perfil resetPassword2(Perfil perfil, String token) {
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
        Perfil p = perfilRepository.findUserByEmail(perfil.getEmail());
        if (p == null) {
            LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + perfil.getEmail());
            throw new UsernameNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + perfil.getEmail());
        } else {
            String encodedPassword = passwordEncoder.encode(perfil.getPassword());
            p.setPassword(encodedPassword);
            perfilRepository.save(p);
            return p;
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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Queremos agradecer-te pela tua sugestão e responderemos o mais rápidamente possível. </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Poderemos demorar entre uma semana a duas a responder \n por isso não fiques surpreso se demorarmos algum tempo a responder de volta \n <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <p>Cumprimentos,</p><p>DESPORTO24APP</p>" +
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

    private String buildNewFriendRequestEmail(String name, String assunto, String mensagem) {
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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Foi te enviado um novo pedido de amizade por: " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Assunto: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Para aceitar basta clicares no link abaixo ou podes simplesmente dirigires-te à app. <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Mensagem: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n"+ mensagem +"</p>" +
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
    public void acceptFriendRequest(String token){
        FriendRequest confirmToken = friendRequestService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token não encontrado."));
        if (confirmToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token já foi confirmado.");
        }
        friendRequestService.setConfirmedAt(token);
        friendRequestService.saveFriendRequest(confirmToken);
        Perfil perfil1 = findUserByUsername(confirmToken.getPerfil1().getUsername());
        Perfil perfil2 = findUserByUsername(confirmToken.getPerfil2().getUsername());
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
    public Perfil sendFriendRequest(String usernamep1, String usernamep2) throws RequestFriendException {
        Perfil p1 = findUserByUsername(usernamep1);
        Perfil p2 = findUserByUsername(usernamep2);
        if(!(friendRepository.existsByPerfil1AndPerfil2(p1,p2))){
            String token = UUID.randomUUID().toString();
            FriendRequest confirmationToken = new FriendRequest(token, LocalDateTime.now(), p1, p2);
            friendRequestService.saveFriendRequest(confirmationToken);
            String link = fromCurrentContextPath().path("/login/confirmNewFriend/"+token).toUriString();
            emailService.send(p2.getEmail(), buildRegistrationEmail(p2.getUsername(),link));
        } else {
            throw new RequestFriendException("Vocês já são amigos!");
        }
        return p1;
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
        Perfil p = findUserByUsername(username);
        if (username == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(p);
            p.setLastLoginDateDisplay(p.getLastLoginDate());
            p.setLastLoginDate(new Date());
            perfilRepository.save(p);
            LOGGER.info(RETURNING_FOUND_USER_BY_USERNAME + " " + p.getUsername());
            PerfilPrincipal perfilPrincipal = new PerfilPrincipal(p);
            return perfilPrincipal;
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

    public Perfil login(Perfil perfil) throws EmailNotVerifiedException {
        Perfil p = findUserByUsername(perfil.getUsername());
        if (perfil.getUsername() == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + perfil.getUsername());
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + perfil.getUsername());
        } else {
            validateLoginAttempt(p);
            validateLoginAttempt2(p);
            validateLoginAttempt3(p);
            p.setLastLoginDateDisplay(p.getLastLoginDate());
            p.setLastLoginDate(new Date());
            perfilRepository.save(p);
            LOGGER.info(RETURNING_FOUND_USER_BY_USERNAME + " " + p.getUsername());
            return p;
        }
    }
}
