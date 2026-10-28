package com.example.desporto24.controllers;

import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.session.SessionCommunityShareRepository;
import com.example.desporto24.service.interfaces.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/** Only exposes associations after the ordinary authenticated session-access check. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class SessionContextController {
    private final SessionService sessionService;
    private final SessionRepository sessions;
    private final SessionCommunityShareRepository shares;

    public record CommunityLabel(Long id, String name) {}
    public record ContextResponse(List<CommunityLabel> communities, boolean seasonGame,
                                  String seasonName) {}

    @GetMapping("/{sessionId}/context")
    @Transactional(readOnly = true)
    public ContextResponse context(@PathVariable Long sessionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // Same authorization as GET /api/sessions/{id}: no leaking member-only sessions.
        sessionService.getById(sessionId, authentication.getName());
        Session session = sessions.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LinkedHashMap<Long, CommunityLabel> linked = new LinkedHashMap<>();
        Community direct = session.getCommunity();
        if (direct != null) linked.put(direct.getId(), new CommunityLabel(direct.getId(), direct.getName()));
        shares.findBySession_IdOrderByIdAsc(sessionId).forEach(share -> {
            Community community = share.getCommunity();
            if (community != null) linked.putIfAbsent(community.getId(),
                    new CommunityLabel(community.getId(), community.getName()));
        });
        var season = session.getSeasonCompetition();
        return new ContextResponse(new ArrayList<>(linked.values()),
                season != null, season == null ? null : season.getName());
    }
}
