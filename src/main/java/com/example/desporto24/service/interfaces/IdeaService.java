package com.example.desporto24.service.interfaces;

import com.example.desporto24.model.dto.idea.IdeaSubmissionRequest;

public interface IdeaService {
    void submitNewIdea(IdeaSubmissionRequest request);
}