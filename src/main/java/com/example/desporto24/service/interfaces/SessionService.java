package com.example.desporto24.service.interfaces;

import com.example.desporto24.model.dto.session.CreateSessionRequest;
import com.example.desporto24.model.dto.session.MySessionsResponse;
import com.example.desporto24.model.dto.session.JoinSessionRequest;
import com.example.desporto24.model.dto.session.JoinSessionResponse;
import com.example.desporto24.model.dto.session.SessionDetailResponse;
import com.example.desporto24.model.dto.session.SessionSearchCriteria;
import com.example.desporto24.model.dto.session.SessionSummaryResponse;
import com.example.desporto24.model.dto.session.SessionParticipantResponse;
import com.example.desporto24.enumeration.SessionProblemType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SessionService {
    SessionDetailResponse create(CreateSessionRequest request, String username);
    SessionDetailResponse create(CreateSessionRequest request, MultipartFile photo, String username);
    SessionDetailResponse getById(Long sessionId, String username);
    List<SessionSummaryResponse> series(Long sessionId, String username);
    List<SessionSummaryResponse> discover(SessionSearchCriteria criteria);
    List<SessionSummaryResponse> discover(SessionSearchCriteria criteria, String username);
    MySessionsResponse mine(String username);
    List<SessionParticipantResponse> participants(Long sessionId, String username);
    JoinSessionResponse join(Long sessionId, JoinSessionRequest request, String username);
    void leave(Long sessionId, String username);
    void cancel(Long sessionId, String username);
    void complete(Long sessionId, String username);
    void reportDidNotOccur(Long sessionId, String username);
    void reportProblem(Long sessionId, SessionProblemType type, String username);
    void removeParticipant(Long sessionId, Long participantId, String username);
}
