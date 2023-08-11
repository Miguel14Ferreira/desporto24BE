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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class UserRegistoService {
    private final ProjectServiceImpl pService;
    private final ConfirmationTokenService confirmationTokenService;
    private final PerfilRepository pr;

    public Perfil register(UserRegistoRequest request) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, NotAnImageFileException, jakarta.mail.MessagingException {
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
                        request.getPhone(),
                        request.getGender(),
                        request.getEmail(),
                        request.getDesportosFavoritos(),
                        request.getFoto()));
        return perfil;
    }

    public Perfil register2(UserRegistoRequest request) throws EmailExistException, PhoneExistException, UsernameExistException, IOException, NotAnImageFileException, jakarta.mail.MessagingException {
        Perfil perfil = pService.signUpPerfil2(
                new Perfil(
                        request.getUsername(),
                        request.getPassword(),
                        request.getFullName(),
                        request.getDateOfBirth(),
                        request.getAddress(),
                        request.getCountry(),
                        request.getLocation(),
                        request.getPostalCode(),
                        request.getPhone(),
                        request.getGender(),
                        request.getEmail(),
                        request.getDesportosFavoritos(),
                        request.getFoto()));
        return perfil;
    }
    /*

    public Perfil register2(UserRegistoRequest request,MultipartFile foto) throws EmailExistException, PhoneExistException, MessagingException, UsernameExistException, IOException, NotAnImageFileException {
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
                        request.getPhone(),
                        request.getGender(),
                        request.getEmail(),
                        request.getDesportosFavoritos()),
                        foto);
        return perfil;
    }
     */

    @Transactional
    public String confirmEmergencyToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token não encontrado."));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Este link já foi clicado.");
        }
        confirmationTokenService.setConfirmedAt(token);
        pService.disablePerfil(confirmationToken.getPerfil().getEmail());
        return "A tua conta neste momento encontra-se bloqueada, podes fechar esta janela.";
    }
}