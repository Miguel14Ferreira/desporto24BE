package com.example.desporto24.service.impl;

import com.example.desporto24.model.Ideas;
import com.example.desporto24.repository.IdeasRepository;
import com.example.desporto24.service.interfaces.IdeaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdeaServiceImpl implements IdeaService {

    private final IdeasRepository ideasRepository;
    @Override
    public void submitNewIdea(Ideas idea) {
        ideasRepository.save(idea);
    }
}
