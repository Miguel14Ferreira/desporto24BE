package com.example.desporto24.service.impl;

import com.example.desporto24.model.Ideas;
import com.example.desporto24.model.dto.idea.IdeaSubmissionRequest;
import com.example.desporto24.repository.IdeasRepository;
import com.example.desporto24.service.interfaces.IdeaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdeaServiceImpl implements IdeaService {

    private final IdeasRepository ideasRepository;
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
    }
}
