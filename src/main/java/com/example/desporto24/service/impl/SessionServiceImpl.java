package com.example.desporto24.service.impl;

import com.example.desporto24.exception.NotAnImageFileException;
import com.example.desporto24.exception.SessionExistException;
import com.example.desporto24.exception.SessionNotFound;
import com.example.desporto24.mapper.SessionMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.model.dto.SessionDTO;
import com.example.desporto24.register.token.ConfirmationToken;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.interfaces.ProfileService;
import com.example.desporto24.service.interfaces.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.desporto24.constant.FileConstant.*;
import static com.example.desporto24.constant.FileConstant.FILE_SAVED_IN_FILE_SYSTEM;
import static com.example.desporto24.constant.SessionImplConstant.NO_SESSION_FOUND_BY_USERNAME;
import static com.example.desporto24.constant.SessionImplConstant.SESSION_ALREADY_EXIST;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final ProfileService profileService;
    private final SessionMapper sessionMapper;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public Session findSessionByUsername(String username) {
        return sessionRepository.findSessionByUsername(username);
    }

    @Override
    public List<Session> getSessions() {
        return sessionRepository.findAll();
    }

    private void validateNewSession(String currentUsername, String address, Date gameDate) {
        Session newAddressSession = findSessionByAddress(address);
        Session newGameDateSession = findSessionByGameDate(gameDate);
        Session currentSession = findSessionByUsername(currentUsername);

        if (isNotBlank(currentUsername) && currentSession == null) {
            throw new SessionNotFound(NO_SESSION_FOUND_BY_USERNAME);
        }

        if (newAddressSession != null && !currentSession.getId().equals(newAddressSession.getId())
                && !currentSession.getId().equals(newGameDateSession.getId())) {
            throw new SessionExistException(SESSION_ALREADY_EXIST);
        }

    }

    private void saveSessionImage(Session session,MultipartFile profileImage) {
        validateImageFileType(profileImage.getContentType());
        Path userFolder = createSessionFolder(session.getUsername());
        deleteExistingProfileImage(userFolder, session.getUsername());
        saveNewProfileImage(userFolder, session.getUsername(), profileImage);
        session.setGamePhoto(setSessionImageUrl(session.getUsername()));
        sessionRepository.save(session);
    }


// Criação de URL para a fotografia escolhida pelo utilizador no registo ou na alteração de dados
private String setSessionImageUrl(String username) {
    return fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
}

// Helper methods for image operations
private void validateImageFileType(String contentType) {
    if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(contentType)) {
        throw new NotAnImageFileException(NOT_AN_IMAGE_FILE);
    }
}

private Path createSessionFolder(String username) {
    try {
        Path userFolder = Paths.get(SESSION_FOLDER + username).toAbsolutePath().normalize();
        if (!Files.exists(userFolder)) {
            Files.createDirectories(userFolder);
            log.info(DIRECTORY_CREATED + userFolder);
        }
        return userFolder;
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

private void deleteExistingProfileImage(Path userFolder, String username) {
    try {
        Files.deleteIfExists(Paths.get(userFolder + username + DOT + JPG_EXTENSION));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

    private void saveNewProfileImage(Path userFolder, String username, MultipartFile profileImage) {
    try {
        Files.copy(profileImage.getInputStream(), userFolder.resolve(username + DOT + JPG_EXTENSION), REPLACE_EXISTING);
        log.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
    } catch (IOException e) {
        throw new RuntimeException(e);
        }
    }

    @Override
    public void createSession(SessionDTO sessionDTO, MultipartFile photo) {
        ProfileDTO profileDTO = profileService.findUserByUsername(sessionDTO.getUsername());
        validateNewSession(sessionDTO.getUsername(), sessionDTO.getAddress(), sessionDTO.getGameDate());

        Session session = sessionMapper.toEntity(sessionDTO);
        sessionRepository.save(session);
        saveSessionImage(session,photo);

        String token = createAndSaveConfirmationToken(profileDTO.getUsername());
        String link = "http://localhost:8080/createNewSession?token=" + token;

        emailService.send(profileDTO.getEmail(), buildCreateSessionEmail(
                session.getUsername(),
                link,
                session.getSport(),
                session.getGameDate(),
                session.getPlayers(),
                session.getCity(),
                session.getAddress(),
                session.getPrice()
        ));
    }

    private String createAndSaveConfirmationToken(String username) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                username
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    @Override
    public Session findSessionByAddress(String address) {
        return sessionRepository.findSessionByAddress(address);
    }

    @Override
    public Session findSessionByGameDate(Date gameDate) {return sessionRepository.findSessionByGameDate(gameDate);}


    private String buildCreateSessionEmail(String username, String link, String sport, Date gameDate, String players, String city, String address, String price) {
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
}
