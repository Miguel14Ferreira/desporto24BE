package com.example.desporto24.controllers;

import com.example.desporto24.model.dto.idea.IdeaSubmissionRequest;
import com.example.desporto24.service.interfaces.IdeaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IdeaControllerTest {

    @Mock
    private IdeaService ideaService;

    @InjectMocks
    private IdeaController controller;

    @Test
    void submitUsesTypedPublicRequest() {
        IdeaSubmissionRequest request = new IdeaSubmissionRequest(
                "Miguel Ferreira",
                "Lisboa",
                "miguel@example.com",
                "masculino",
                "Nova funcionalidade",
                "Gostava de ter torneios."
        );

        var response = controller.registerNewIdea(request);

        assertEquals(200, response.getStatusCode().value());
        verify(ideaService).submitNewIdea(request);
    }
}
