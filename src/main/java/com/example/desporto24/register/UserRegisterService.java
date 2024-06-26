package com.example.desporto24.register;
import com.example.desporto24.exception.domain.*;
import com.example.desporto24.model.Profile;
import com.example.desporto24.register.token.ConfirmationTokenService;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.service.impl.NotAnImageFileException;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@Service
public class UserRegisterService {
    private final ProjectServiceImpl pService;
    private final ConfirmationTokenService confirmationTokenService;
    private final ProfileRepository pr;
    private final BCryptPasswordEncoder passwordEncoder;

    // Obtenção de dados do novo utilizador
    public Profile register(UserRegisterRequest request, MultipartFile foto) throws Exception, NotAnImageFileException {
        Profile profile = pService.signUpPerfil(
                new Profile(
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
        return profile;
    }
}