package com.example.desporto24.registo;
import com.example.desporto24.exception.domain.EmailExistException;
import com.example.desporto24.exception.domain.PhoneExistException;
import com.example.desporto24.exception.domain.UsernameExistException;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.registo.token.ConfirmationToken;
import com.example.desporto24.registo.token.ConfirmationTokenService;
import com.example.desporto24.repository.PerfilRepository;
import com.example.desporto24.service.impl.NotAnImageFileException;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;
import static com.example.desporto24.enumeration.Role.ROLE_USER;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hibernate.metamodel.mapping.MappingModelCreationLogger.LOGGER;

@AllArgsConstructor
@Service
public class UserRegistoService {
    private final ProjectServiceImpl pService;
    private final ConfirmationTokenService confirmationTokenService;
    private final PerfilRepository pr;
    private final BCryptPasswordEncoder passwordEncoder;

    // Obtenção de dados do novo utilizador
    public Perfil register(UserRegistoRequest request, MultipartFile foto) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, NotAnImageFileException, jakarta.mail.MessagingException {
        Perfil perfil = pService.signUpPerfil(
                new Perfil(
                        request.getUsername(),
                        request.getPassword(),
                        request.getFullName(),
                        request.getDateOfBirth(),
                        request.getAddress(),
                        request.getCountry(),
                        request.getLocation(),
                        request.getPostalCode(),
                        request.getIndicativePhone(),
                        request.getPhone(),
                        request.getGender(),
                        request.getEmail(),
                        request.getDesportosFavoritos()),
                        foto);
        return perfil;
    }
}