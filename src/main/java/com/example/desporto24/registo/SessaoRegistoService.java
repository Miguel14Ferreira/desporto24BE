package com.example.desporto24.registo;

import com.example.desporto24.exception.domain.SessionExistException;
import com.example.desporto24.model.Sessao;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;

import javax.mail.MessagingException;

@AllArgsConstructor
public class SessaoRegistoService {

    private final ProjectServiceImpl sessaoService;

    public String createSessao(SessaoRegistoRequest request) throws SessionExistException, MessagingException {
        String sessao = sessaoService.createSessao(
                new Sessao(
                        request.getUsername(),
                        request.getDesporto(),
                        request.getJogadores(),
                        request.getDatadejogo(),
                        request.getLocalidade(),
                        request.getMorada(),
                        request.getPreco(),
                        request.getPassword()));
        return sessao;
    }
}
