package com.example.desporto24.service.impl;

import com.example.desporto24.model.Ideas;
import com.example.desporto24.model.dto.idea.IdeaSubmissionRequest;
import com.example.desporto24.repository.IdeasRepository;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.interfaces.IdeaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdeaServiceImpl implements IdeaService {

    private final IdeasRepository ideasRepository;
    private final EmailService emailService;

    @Override
    public void submitNewIdea(IdeaSubmissionRequest request) {
        Ideas idea = new Ideas(
                request.name().trim(),
                request.email().trim(),
                request.city().trim(),
                request.gender(),
                request.subject().trim(),
                request.problem().trim()
        );

        ideasRepository.save(idea);
        emailService.send(request.email().trim(), buildConfirmationEmail());
    }

    private String buildConfirmationEmail() {
        return "<h2>Obrigado pela tua mensagem</h2>"
                + "<p>Recebemos a tua sugestão no Desporto24 com sucesso.</p>"
                + "<p>Obrigado por nos ajudares a melhorar a plataforma.</p>"
                + "<p>Cumprimentos,<br>DESPORTO24</p>";
    }
}
