package com.example.desporto24.repository;

import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicFeedVisibilityQueryTest {

    @Test
    void publicSessionFeedExcludesCommunityOnlyAndDisabledSessions() throws Exception {
        assertPublicVisibility(SessionRepository.class.getMethod(
                "findPublicFeedSessions", Collection.class, Date.class, Pageable.class));
        assertPublicVisibility(SessionRepository.class.getMethod(
                "findRecentPublicFeedSessions", Date.class, Pageable.class));
    }

    @Test
    void recruitmentFeedOnlyContainsUpcomingActiveSessions() throws Exception {
        assertOnlyActiveUpcomingSessions(SessionRepository.class.getMethod(
                "findPublicFeedSessions", Collection.class, Date.class, Pageable.class));
        assertOnlyActiveUpcomingSessions(SessionRepository.class.getMethod(
                "findRecentPublicFeedSessions", Date.class, Pageable.class));
    }

    private void assertOnlyActiveUpcomingSessions(Method method) {
        String jpql = method.getAnnotation(Query.class).value();
        assertTrue(jpql.contains("s.gameDate > :now"), method.getName());
        assertTrue(jpql.contains("SessionStatus.OPEN"), method.getName());
        assertTrue(jpql.contains("SessionStatus.FULL"), method.getName());
        assertTrue(jpql.contains("s.status is null"), method.getName());
    }

    @Test
    void attendanceFeedUsesTheSamePublicVisibilityRules() throws Exception {
        Method method = SessionParticipantRepository.class.getMethod(
                "findPublicAttendanceFeed", Collection.class, Pageable.class);
        assertPublicVisibility(method);
        assertTrue(method.getAnnotation(Query.class).value().contains("p.feedHidden = false"));
    }

    private void assertPublicVisibility(Method method) {
        String jpql = method.getAnnotation(Query.class).value();
        assertTrue(jpql.contains("s.privateGame = false"), method.getName());
        assertTrue(jpql.contains("s.audience = com.example.desporto24.enumeration.SessionAudience.PUBLIC"),
                method.getName());
        assertTrue(jpql.contains("s.audience is null and s.community is null"), method.getName());
        assertTrue(jpql.contains("s.enabled = true or s.enabled is null"), method.getName());
    }
}
