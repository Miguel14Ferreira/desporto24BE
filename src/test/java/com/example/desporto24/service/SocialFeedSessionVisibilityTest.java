package com.example.desporto24.service;

import com.example.desporto24.enumeration.SessionAudience;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.Community;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void imageOfPublicSessionIsAvailableToFeed() {
        Session session = new Session();
        session.setAudience(SessionAudience.PUBLIC);
        session.setPrivateGame(false);
        session.setEnabled(true);
        session.setGamePhoto("https://example.test/photos/game.jpg");

        assertEquals("https://example.test/photos/game.jpg",
                SocialFeedService.publicSessionImage(session));
    }

    @Test
    void privateAndCommunitySessionImagesAreNeverExposedToFeed() {
        Session session = new Session();
        session.setGamePhoto("https://example.test/photos/private.jpg");
        session.setAudience(SessionAudience.PUBLIC);
        session.setPrivateGame(true);
        assertNull(SocialFeedService.publicSessionImage(session));

        session.setPrivateGame(false);
        session.setAudience(SessionAudience.COMMUNITY_ONLY);
        assertNull(SocialFeedService.publicSessionImage(session));

        session.setAudience(SessionAudience.PUBLIC);
        session.setEnabled(false);
        assertNull(SocialFeedService.publicSessionImage(session));
    }

    @Test
    void missingOrBlankPhotoDoesNotProduceAnImage() {
        Session session = new Session();
        session.setAudience(SessionAudience.PUBLIC);
        session.setEnabled(true);
        assertNull(SocialFeedService.publicSessionImage(session));
        session.setGamePhoto("   ");
        assertNull(SocialFeedService.publicSessionImage(session));
        assertNull(SocialFeedService.publicSessionImage(null));
    }

    @Test
    void missingSessionCannotBeShared() {
        assertFalse(SocialFeedService.isPubliclyShareableSession(null));
    }
}
