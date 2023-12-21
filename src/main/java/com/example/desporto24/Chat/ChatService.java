package com.example.desporto24.Chat;

import com.example.desporto24.changePassword.UserChangePasswordRequest;
import com.example.desporto24.exception.domain.EmailExistException;
import com.example.desporto24.exception.domain.EqualUsernameAndPasswordException;
import com.example.desporto24.exception.domain.PhoneExistException;
import com.example.desporto24.exception.domain.UsernameExistException;
import com.example.desporto24.model.Chat;
import com.example.desporto24.model.Perfil;
import com.example.desporto24.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatService {

    private ProjectService perfilService;
    public Chat sendMsg(ChatRequest chatRequest) throws Exception {
        Chat p = perfilService.EnviarMensagem(new Chat(
                chatRequest.getUsername1(),
                chatRequest.getUsername2(),
                chatRequest.getTexto()));
        return p;
    }
}
