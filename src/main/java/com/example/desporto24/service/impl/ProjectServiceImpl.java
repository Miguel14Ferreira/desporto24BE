package com.example.desporto24.service.impl;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.Ideias;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.model.PerfilPrincipal;
import com.example.desporto24.model.Sessao;
import com.example.desporto24.registo.MFA.MFAVerification;
import com.example.desporto24.registo.MFA.MFAVerificationService;
import com.example.desporto24.registo.UserRegistoRequest;
import com.example.desporto24.registo.UserRegistoService;
import com.example.desporto24.registo.token.ConfirmationToken;
import com.example.desporto24.registo.token.ConfirmationTokenService;
import com.example.desporto24.repository.IdeiasRepository;
import com.example.desporto24.repository.PerfilRepository;
import com.example.desporto24.repository.SessaoRepository;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.ProjectService;
import com.example.desporto24.update.token.UpdateToken;
import com.example.desporto24.update.token.UpdateTokenService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.metamodel.mapping.MappingModelCreationLogger.LOGGER;
import static org.springframework.http.MediaType.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Qualifier("UserDetailsService")
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
    private final UpdateTokenService updateTokenService;
    private final IdeiasRepository ideiasRepository;

    @Autowired
    public ProjectServiceImpl(PerfilRepository perfilRepository, BCryptPasswordEncoder passwordEncoder, EmailService emailService, ConfirmationTokenService confirmationTokenService, SessaoRepository sessaoRepository, MFAVerificationService mfaVerificationService, ExceptionHandling exceptionHandling, UpdateTokenService updateTokenService, IdeiasRepository ideiasRepository) {
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
        this.sessaoRepository = sessaoRepository;
        this.MFAverificationService = mfaVerificationService;
        this.exceptionHandling = exceptionHandling;
        this.updateTokenService = updateTokenService;
        this.ideiasRepository = ideiasRepository;
    }


    @Override
    public Perfil updateUserFoto(String utilizador, MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, NotAnImageFileException {
        Perfil perfil = validateNewUsernameEmailAndPhone(utilizador, null, null, null);
        saveProfileImage(perfil, foto);
        return perfil;
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

    private String buildSessionCancelationEmail(String Username, String link, String desporto, Date dataDeJogo, String jogadores, String localidade, String morada, String preco, String password) {
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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + Username + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Acabou de ser criada uma sessão da tua parte com os seguintes atributos: </p> " +
                        "            Sessão criada por " + Username + ":<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> </p>" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Desporto: " + desporto +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Data de Jogo: " + dataDeJogo +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Número de jogadores: " + jogadores +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Localidade: " + localidade +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Morada: " + morada +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Preço: " + preco +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Password do jogo: " + password +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Se não foste que criaste esta sessão, este link irá servir para eliminar a sessão: </p><p><a href=\"" + link + "\">Apagar sessão</a> </p>\n Este link vai expirar em 15 minutos. <p>Cumprimentos,</p><p>DESPORTO24APP</p>" +
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
    */


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

    @Override
    public Sessao findSessaoByUsername(String username) {
        return sessaoRepository.findSessaoByUsername(username);
    }

    @Override
    public int deleteSessao(String email) {
        return 0;
    }

    public Perfil login(Perfil perfil) throws UsernameNotFoundException, EmailNotVerifiedException, AccountDisabledException {
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
            p.setLogginAttempts(0);
            perfilRepository.save(p);
            LOGGER.info(RETURNING_FOUND_USER_BY_USERNAME + " " + p.getUsername());
            return p;
        }
    }


    private void validateLoginAttempt(Perfil perfil) throws AccountDisabledException {
        if (perfil.isNotLocked()) {
            int attempts = 0;
            Perfil perfilPesquisa = perfilRepository.findUserByUsername(perfil.getUsername());
            do {
                attempts = perfil.getLogginAttempts() + 1;
                perfil.setLogginAttempts(attempts);
            }
            while (perfilPesquisa.getPassword() != perfil.getPassword());
            if (attempts > 6) {
                perfil.setNotLocked(false);
            } else {
                perfil.setNotLocked(true);
            }
        } else {
            exceptionHandling.lockedException();
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


    public int enablePerfil(String email) {
        return perfilRepository.enablePerfil(email);
    }

    public int disablePerfil(String email) {
        return perfilRepository.disablePerfil(email);
    }


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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Pediste da nossa parte para efetuares um reset à tua password. Clica neste link para fazemos isso: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Efetuar o reset à password</a> </p><p>Cumprimentos,</p><p>DESPORTO24APP</p>" +
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

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Obrigado por te registares na nossa aplicação. Clica neste link para ativar a tua conta: </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Ativar agora</a> </p><p>Cumprimentos,</p><p>DESPORTO24APP</p>" +
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
        saveProfileImage(perfil,foto);
        perfilRepository.save(perfil);
        String token = "registrationNewAccountToken"+perfil.getUsername();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), perfil);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = "http://localhost:4200/login/registerNewUser/confirmTokenRegistration";
        emailService.send(perfil.getEmail(), buildRegistrationEmail(perfil.getUsername(), link));
        return perfil;
    }

    public Perfil signUpPerfil2(Perfil perfil) {
        Perfil p = perfilRepository.findUserByUsername(perfil.getUsername());
        if (p == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + perfil.getUsername());
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + perfil.getUsername());
        } else {
            String token = "registrationNewAccountToken"+perfil.getUsername();
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
            enablePerfil(confirmationToken.getPerfil().getEmail());
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

    @Override
    public Perfil updateUser(String username,Perfil perfil, MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, MessagingException, jakarta.mail.MessagingException, NotAnImageFileException {
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
        if (foto != null){
            saveProfileImage(p,foto);
        }
            perfilRepository.save(p);
            String link = "http://localhost:4200/confirmEmergencyToken";
            emailService.send(p.getEmail(), buildChangePerfilEmail(p.getUsername(), link));
        return p;
    }


    @Override
    public void deleteUser(Long id) {
        perfilRepository.deleteById(id);
    }

    @Override
    public void sendVerificationCode(Perfil perfil) {
        String code = randomAlphabetic(8).toUpperCase();
        MFAVerification mfaVerification = new MFAVerification(code, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), perfil);
        MFAverificationService.saveConfirmationMFA(mfaVerification);
        sendSMS(perfil.getIndicativePhone(),perfil.getPhone(), "Desporto24APP \nCodigo de Verificação:\n" + code);
    }

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
    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
    }

    private String getTemporaryProfileImageURL(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

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
            return p;
        }
    }

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
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Olá " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Dados foram alterados na tua conta. </p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">\n Se não foste tu quem fez esta alteração, \n por favor clica neste link para bloquearmos temporareamente a tua conta: \n <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Bloquear</a> </p>\n Este link vai expirar numa hora. \n <p>Cumprimentos,</p><p>DESPORTO24APP</p>" +
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

    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Perfil perfil = perfilRepository.findUserByUsername(username);
        if (perfil == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + perfil.getUsername());
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + perfil.getUsername());
        } else {
            validateLoginAttempt(perfil);
            validateLoginAttempt2(perfil);
            perfil.setLastLoginDateDisplay(perfil.getLastLoginDate());
            perfil.setLastLoginDate(new Date());
            perfilRepository.save(perfil);
            LOGGER.info(RETURNING_FOUND_USER_BY_USERNAME + " " + perfil.getUsername());
            return new PerfilPrincipal(perfil);
        }
    }

    public Perfil resetPassword1(Perfil perfil) throws MessagingException, EmailNotVerifiedException {
        perfil = perfilRepository.findUserByEmail(perfil.getEmail());
        if (perfil == null) {
            LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + perfil.getEmail());
            throw new UsernameNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + perfil.getEmail());
        } else {
            String link = "http://localhost:4200/resetPassword/newPassword";
            emailService.send(perfil.getEmail(), buildResetPasswordEmail(perfil.getUsername(), link));
            return perfil;
        }
    }
        public Perfil resetPassword2(Perfil perfil) {
            Perfil p = perfilRepository.findUserByUsername(perfil.getUsername());
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

    public Perfil updatePerfilEmergency(String username) {
        Perfil p = findUserByUsername(username);
        if (p == null){
            LOGGER.error(NO_EMAIL_FOUND_BY_EMAIL + p.getEmail());
            throw new UsernameNotFoundException(NO_EMAIL_FOUND_BY_EMAIL + p.getEmail());
        }
        disablePerfil(p.getEmail());
        return p;
    }
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

    public void signUpPerfil3(Perfil perfil) {
    }

    public Ideias newIdea(Ideias i) throws MessagingException {
        LOGGER.info(String.valueOf(i));
        emailService.send(i.getEmail(),buildNewIdeaEmail(i.getName()));
        ideiasRepository.save(i);
        return i;
    }
}
