package com.example.desporto24.service;

import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SocialFeedSessionVisibilityTest {

    @Test
    void publicSessionCanBeShared() {
        Session session = new Session();
        session.setAudience(SessionAudience.PUBLIC);
        session.setPrivateGame(false);
        session.setEnabled(true);

        assertTrue(SocialFeedService.isPubliclyShareableSession(session));
    }

    @Test
    void legacySessionWithoutAudienceOrCommunityIsPublic() {
        Session session = new Session();

        assertTrue(SocialFeedService.isPubliclyShareableSession(session));
    }

    @Test
    void communityOnlySessionCannotBeShared() {
        Session session = new Session();
        session.setAudience(SessionAudience.COMMUNITY_ONLY);

        assertFalse(SocialFeedService.isPubliclyShareableSession(session));
    }

    @Test
    void legacyCommunitySessionCannotBeShared() {
        Session session = new Session();
        session.setCommunity(mock(Community.class));

        assertFalse(SocialFeedService.isPubliclyShareableSession(session));
    }

    @Test
    void privateSessionCannotBeShared() {
        Session session = new Session();
        session.setAudience(SessionAudience.PUBLIC);
        session.setPrivateGame(true);

        assertFalse(SocialFeedService.isPubliclyShareableSession(session));
    }

    @Test
    void disabledSessionCannotBeShared() {
        Session session = new Session();
        session.setAudience(SessionAudience.PUBLIC);
        session.setEnabled(false);

        assertFalse(SocialFeedService.isPubliclyShareableSession(session));
    }

    @Test
    void missingSessionCannotBeShared() {
        assertFalse(SocialFeedService.isPubliclyShareableSession(null));
    }
}
