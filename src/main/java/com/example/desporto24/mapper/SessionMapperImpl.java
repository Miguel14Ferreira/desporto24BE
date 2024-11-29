package com.example.desporto24.mapper;

import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.SessionDTO;
import com.example.desporto24.service.interfaces.ProfileService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SessionMapperImpl implements SessionMapper{
    private BCryptPasswordEncoder passwordEncoder;
    private ProfileService profileService;

    @Override
    public Session toEntity(SessionDTO sessionDTO) {
        Session session = new Session();
        if (sessionDTO.getPrivateGame()) {
            session.setUserId(generateUserId());
            session.setUsername(sessionDTO.getUsername());
            String encodedPassword = passwordEncoder.encode(sessionDTO.getPassword());
            session.setPassword(encodedPassword);
            session.setAddress(sessionDTO.getAddress());
            session.setCity(sessionDTO.getCity());
            session.setCreated_at(new Date());
            session.setEnabled(false);
            session.setPlayers(sessionDTO.getPlayers());
            session.setSport(sessionDTO.getSport());
            session.setGameDate(sessionDTO.getGameDate());
            session.setPrice(sessionDTO.getPrice());
        } else {
            session.setUserId(generateUserId());
            session.setUsername(sessionDTO.getUsername());
            session.setAddress(sessionDTO.getAddress());
            session.setCity(sessionDTO.getCity());
            session.setCreated_at(new Date());
            session.setEnabled(false);
            session.setPlayers(sessionDTO.getPlayers());
            session.setSport(sessionDTO.getSport());
            session.setGameDate(sessionDTO.getGameDate());
        }
        return session;
    }
    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }
}
