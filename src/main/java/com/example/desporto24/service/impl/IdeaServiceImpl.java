package com.example.desporto24.service.impl;

import com.example.desporto24.enumeration.IdeaStatus;
import com.example.desporto24.model.Ideas;
import com.example.desporto24.model.dto.idea.AdminIdeaResponse;
import com.example.desporto24.model.dto.idea.IdeaSubmissionRequest;
import com.example.desporto24.repository.IdeasRepository;
import com.example.desporto24.service.EmailService;
import com.example.desporto24.service.interfaces.IdeaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdeaServiceImpl implements IdeaService {

    private static final String MANAGEMENT_EMAIL = "desporto24app@gmail.com";

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

        Ideas saved = ideasRepository.save(idea);

        try {
            emailService.send(request.email().trim(), buildConfirmationEmail());
        } catch (RuntimeException exception) {
            log.warn("Idea {} was saved but confirmation email could not be sent", saved.getId(), exception);
        }

        try {
            emailService.send(MANAGEMENT_EMAIL, buildManagementAlertEmail(saved));
        } catch (RuntimeException exception) {
            log.warn("Idea {} was saved but management notification email could not be sent", saved.getId(), exception);
        }
    }

    @Override
    public List<AdminIdeaResponse> getAllIdeas() {
        return ideasRepository.findAll().stream()
                .sorted(Comparator.comparing(
                        Ideas::getCreatedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .map(this::toAdminResponse)
                .toList();
    }

    @Override
    public AdminIdeaResponse updateStatus(Long id, IdeaStatus status) {
        Ideas idea = ideasRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ideia não encontrada"));

        idea.setStatus(status);
        return toAdminResponse(ideasRepository.save(idea));
    }

    private AdminIdeaResponse toAdminResponse(Ideas idea) {
        return new AdminIdeaResponse(
                idea.getId(),
                idea.getName(),
                idea.getCity(),
                idea.getEmail(),
                idea.getGender(),
                idea.getSubject(),
                idea.getProblem(),
                idea.getStatus() == null ? IdeaStatus.NEW : idea.getStatus(),
                idea.getCreatedAt()
        );
    }

    private String buildConfirmationEmail() {
        return "<h2>Obrigado pela tua mensagem</h2>"
                + "<p>Recebemos a tua sugestão no Desporto24 com sucesso.</p>"
                + "<p>A nossa equipa irá analisá-la no painel de gestão.</p>"
                + "<p>Se precisares de falar connosco diretamente, responde para "
                + "<strong>" + MANAGEMENT_EMAIL + "</strong>.</p>"
                + "<p>Cumprimentos,<br>DESPORTO24 MANAGEMENT TEAM</p>";
    }

    private String buildManagementAlertEmail(Ideas idea) {
        return "<h2>Nova ideia recebida no DESPORTO24</h2>"
                + "<p><strong>ID:</strong> " + idea.getId() + "</p>"
                + "<p><strong>De:</strong> " + html(idea.getName()) + " &lt;" + html(idea.getEmail()) + "&gt;</p>"
                + "<p><strong>Localidade:</strong> " + html(idea.getCity()) + "</p>"
                + "<p><strong>Assunto:</strong> " + html(idea.getSubject()) + "</p>"
                + "<p><strong>Mensagem:</strong></p>"
                + "<p>" + html(idea.getProblem()).replace("\n", "<br>") + "</p>"
                + "<p>A sugestão ficou guardada com o estado <strong>Nova</strong> no painel de administração.</p>";
    }

    private String html(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
