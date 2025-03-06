package com.example.desporto24.service.impl;

import com.example.desporto24.encryption.AES;
import com.example.desporto24.exception.*;
import com.example.desporto24.mapper.ProfileMapper;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.ProfileDTO;
import com.example.desporto24.register.Notifications.Notifications;
import com.example.desporto24.repository.NotificationsRepository;
import com.example.desporto24.register.token.ConfirmationToken;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
import static com.example.desporto24.constant.FileConstant.FILE_SAVED_IN_FILE_SYSTEM;
import static com.example.desporto24.constant.UserImplConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final NotificationsRepository notificationsRepository;

    @Override
    public ProfileDTO findUserByUsername(String username) {
        Profile profile = profileRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
        return profileMapper.toDto(profile);
    }

    @Override
    public ProfileDTO findUserByEmail(String email) {
        Profile profile = profileRepository.findUserByEmail(email).orElseThrow(() -> new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL));
        return profileMapper.toDto(profile);
    }

    @Override
    public ProfileDTO findUserByUserId(String userId) {
        Profile profile = profileRepository.findUserByUserId(userId).orElseThrow(() -> new UserIdNotFoundException(NO_USERID_FOUND_BY_ID));
        return profileMapper.toDto(profile);
    }

    @Override
    public ProfileDTO findUserByPhone(String phone) {
        Profile profile = profileRepository.findUserByPhone(phone).orElseThrow(() -> new PhoneNotFoundException(NO_USER_FOUND_BY_PHONE));
        return profileMapper.toDto(profile);
    }

    private void saveProfileImage(ProfileDTO profileDTO, MultipartFile profileImage) {
            Profile profile = profileRepository.findUserByUsername(profileDTO.getUsername()).orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
            validateImageFileType(profileImage.getContentType());
            Path userFolder = createProfileFolder(profileDTO.getUsername());
            deleteExistingProfileImage(userFolder, profileDTO.getUsername());
            saveNewProfileImage(userFolder, profileDTO.getUsername(), profileImage);
            profile.setProfilePicUrl(setProfileImageUrl(profileDTO.getUsername()));
            save(profileDTO);
        }


// Criação de URL para a fotografia escolhida pelo utilizador no registo ou na alteração de dados
        private String setProfileImageUrl(String username) {
            return fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
        }

// Helper methods for image operations
        private void validateImageFileType(String contentType) {
            if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(contentType)) {
                throw new NotAnImageFileException(NOT_AN_IMAGE_FILE);
            }
        }

        private Path createProfileFolder(String username) {
            try {
                Path userFolder = Paths.get(USER_FOLDER + username).toAbsolutePath().normalize();
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


    public void validateNewUsernameEmail(String currentUsername, String newUsername, String email) throws UsernameExistException, EmailExistException, UserNotFoundException {
        try {
            ProfileDTO userByNewUsername = findUserByUsername(newUsername);
            ProfileDTO userByNewEmail = findUserByEmail(email);
            if (isNotBlank(currentUsername)) {
                ProfileDTO currentUser = findUserByUsername(currentUsername);
                if (currentUser == null) {
                    throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + " " + currentUsername);
                }
                if (userByNewUsername != null && !currentUser.getUserId().equals(userByNewUsername.getUserId())) {
                    throw new UsernameExistException(USERNAME_ALREADY_EXIST);
                }
                if (userByNewEmail != null && !currentUser.getUserId().equals(userByNewEmail.getUserId())) {
                    throw new EmailExistException(EMAIL_ALREADY_EXIST);
                }
            } else {
                if (userByNewUsername != null) {
                    throw new UsernameExistException(USERNAME_ALREADY_EXIST);
                }
                if (userByNewEmail != null) {
                    throw new EmailExistException(EMAIL_ALREADY_EXIST);
                }
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

    @Override
    public void updateUser(String username, ProfileDTO profileDTO, MultipartFile photo) {
        ProfileDTO profile = findUserByUsername(username);
        if (profile.getEmail().equals(profileDTO.getEmail())) {
            validateNewUsernameEmail(username, null, null);
        } if (profile.getPhone().equals(profileDTO.getPhone())){
            validateNewUsernameEmail(username,null, profileDTO.getEmail());
        } else {
            validateNewUsernameEmail(username,null, profileDTO.getEmail());
        }

        saveProfileImage(profile, photo);
        sendUpdateProfileEmail(profile);
    }
    private void sendUpdateProfileEmail(ProfileDTO profileDTO) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), profileDTO.getUsername());
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = fromCurrentContextPath().path("/confirmEmergencyToken/"+token+"/"+profileDTO.getUsername()).toUriString();
        emailService.send(profileDTO.getOldEmail(), buildChangeProfileEmail(profileDTO.getUsername(), link));
        emailService.send(profileDTO.getEmail(), buildChangeProfileEmail(profileDTO.getUsername(), link));
        String assuntoNotificacaoAlteracaoDados = " Alteração de Dados de perfil";
        String notificacaoAlteracaoDados = "Foram feitas alterações dos dados pessoais do teu perfil, se não foste tu clica para bloquearmos temporareamente a tua conta.";
        String cumprimentosNotificacao = "Cumprimentos,";
        String assinatura = "DESPORTO24";
        Date date = new Date();
        String data = substring(String.valueOf(date),3,10);
        String data2 = substring(String.valueOf(date),24,29);
        String data3 = data2+data;
        Notifications notification = new Notifications(assuntoNotificacaoAlteracaoDados,notificacaoAlteracaoDados,cumprimentosNotificacao,assinatura,data3,false,true,false,false,link,profileDTO.getUsername());
        notificationsRepository.save(notification);
    }

    @Override
    public void changeUsernameAndPassword(ProfileDTO profileDTO) {
        ProfileDTO profile = findUserByUsername(profileDTO.getUsername());
        profile.setUsername(profileDTO.getNewUsername());
        profile.setPassword(AES.encryptString(profileDTO.getPassword(), profile.getUserId()));
        save(profile);
    }

    @Override
    public List<ProfileDTO> searchProfileByUsername(String username) {
        List<Profile> profiles = profileRepository.searchProfile(username);
        List<ProfileDTO> profileDTOs = new ArrayList<>();

        for (Profile profile : profiles) {
            ProfileDTO profileDTO = profileMapper.toDto(profile);
            profileDTOs.add(profileDTO);
        }
        return profileDTOs;
    }

    private String buildChangeProfileEmail(String name, String link) {
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

    @Override
    public void save(ProfileDTO profileDTO) {
        Profile profile = profileMapper.toEntity(profileDTO);
        profileRepository.save(profile);
    }
}