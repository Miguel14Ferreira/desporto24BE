package com.example.desporto24.register;

import com.example.desporto24.exception.domain.EmailExistException;
import com.example.desporto24.exception.domain.PhoneExistException;
import com.example.desporto24.exception.domain.SessionExistException;
import com.example.desporto24.exception.domain.UsernameExistException;
import com.example.desporto24.model.Session;
import com.example.desporto24.service.impl.NotAnImageFileException;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@Service
public class SessionRegisterService {

    private final ProjectServiceImpl sessaoService;

    // Obtenção dos dados para criação de nova sessão do utilizador
    public Session createSessao(SessionRegisterRequest request, MultipartFile foto) throws SessionExistException, jakarta.mail.MessagingException, EmailExistException, PhoneExistException, IOException, UsernameExistException, NotAnImageFileException {
        Session session = sessaoService.createSessao(
                new Session(
                        request.getUsername(),
                        request.getDesporto(),
                        request.getJogadores(),
                        request.getDatadejogo(),
                        request.getLocalidade(),
                        request.getPrivado(),
                        request.getMorada(),
                        request.getPreco(),
                        request.getPassword()),
                        foto);
        return session;
    }
}
