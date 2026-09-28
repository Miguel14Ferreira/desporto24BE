package com.example.desporto24.service.interfaces;

import com.example.desporto24.enumeration.IdeaStatus;
import com.example.desporto24.model.dto.idea.AdminIdeaResponse;
import com.example.desporto24.model.dto.idea.IdeaSubmissionRequest;

import java.util.List;

public interface IdeaService {
    void submitNewIdea(IdeaSubmissionRequest request);
    List<AdminIdeaResponse> getAllIdeas();
    AdminIdeaResponse updateStatus(Long id, IdeaStatus status);
}
