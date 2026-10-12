package com.example.desporto24.repository;

import com.example.desporto24.repository.session.SessionParticipantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.lang.reflect.Method;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicFeedVisibilityQueryTest {

    @Test
    void publicSessionFeedExcludesCommunityOnlyAndDisabledSessions() throws Exception {
        assertPublicVisibility(SessionRepository.class.getMethod(
                "findPublicFeedSessions", Collection.class, Pageable.class));
        assertPublicVisibility(SessionRepository.class.getMethod(
                "findRecentPublicFeedSessions", Pageable.class));
    }

    @Test
    void attendanceFeedUsesTheSamePublicVisibilityRules() throws Exception {
        assertPublicVisibility(SessionParticipantRepository.class.getMethod(
                "findPublicAttendanceFeed", Collection.class, Pageable.class));
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
