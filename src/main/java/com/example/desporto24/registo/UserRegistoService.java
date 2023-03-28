package com.example.desporto24.registo;
import com.example.desporto24.exception.domain.EmailExistException;
import com.example.desporto24.exception.domain.PhoneExistException;
import com.example.desporto24.exception.domain.UsernameExistException;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.registo.token.ConfirmationToken;
import com.example.desporto24.registo.token.ConfirmationTokenService;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;

@AllArgsConstructor
@Service
public class UserRegistoService {
    private final ProjectServiceImpl pService;
    private ConfirmationTokenService confirmationTokenService;

    public String register(UserRegistoRequest request) throws EmailExistException, PhoneExistException, MessagingException, UsernameExistException, IOException {
        String perfil = pService.signUpPerfil(
                new Perfil(
                        request.getUsername(),
                        request.getPassword(),
                        request.getFullName(),
                        request.getDateOfBirth(),
                        request.getAddress(),
                        request.getCountry(),
                        request.getLocation(),
                        request.getPhone(),
                        request.getDesportosFavoritos(),
                        request.getGender(),
                        request.getEmail(),
                        request.getFoto()));
        return perfil;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token não encontrado."));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email já foi confirmado.");
        }
        confirmationTokenService.setConfirmedAt(token);
        pService.enablePerfil(confirmationToken.getPerfil().getEmail());
        return "A tua conta está agora ativa, podes fechar esta janela.";
    }
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