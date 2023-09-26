package com.example.desporto24.newIdea;

import com.example.desporto24.model.Ideias;
import com.example.desporto24.service.impl.ProjectServiceImpl;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NewIdeaService {

    private final ProjectServiceImpl projectService;

    // Obtenção de dados para a nova sugestão/ideia do utilizador
    public Ideias registerNewIdea(NewIdeaRequest newIdeaRequest) throws MessagingException {
        Ideias ideia = projectService.newIdea( new Ideias(
                newIdeaRequest.getName(),
                newIdeaRequest.getEmail(),
                newIdeaRequest.getCity(),
                newIdeaRequest.getGender(),
                newIdeaRequest.getSubject(),
                newIdeaRequest.getProblem()
        ));
                return ideia;
    }
}
