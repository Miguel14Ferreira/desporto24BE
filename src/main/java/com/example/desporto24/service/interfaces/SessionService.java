package com.example.desporto24.service.interfaces;

import com.example.desporto24.exception.NotAnImageFileException;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.SessionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface SessionService {
    Session findSessionByUsername(String username);
    List<Session> getSessions();
    void createSession(SessionDTO sessionDTO, MultipartFile photo) throws Exception, NotAnImageFileException;
    Session findSessionByAddress(String address);
    Session findSessionByGameDate(Date gameDate);
}