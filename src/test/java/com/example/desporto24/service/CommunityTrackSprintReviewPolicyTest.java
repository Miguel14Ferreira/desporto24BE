package com.example.desporto24.service;

import com.example.desporto24.model.community.CommunityTrackSprintReview;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTrackSprintReviewPolicyTest {
    @Test void anOfficialRequestOrOverturnBlocksAllDependentRounds() {
        assertTrue(CommunityTrackSprintReviewPolicy.blocks("REQUESTED"));
        assertTrue(CommunityTrackSprintReviewPolicy.blocks("OVERTURNED"));
        assertFalse(CommunityTrackSprintReviewPolicy.blocks("UPHELD"));
        assertTrue(CommunityTrackSprintReviewPolicy.affects("Q1","S1"));
        assertTrue(CommunityTrackSprintReviewPolicy.affects("Q1","G"));
        assertTrue(CommunityTrackSprintReviewPolicy.affects("Q1","F58"));
        assertTrue(CommunityTrackSprintReviewPolicy.affects("S1","B"));
        assertTrue(CommunityTrackSprintReviewPolicy.affects("G","G"));
        assertFalse(CommunityTrackSprintReviewPolicy.affects("Q1","Q2"));
        assertFalse(CommunityTrackSprintReviewPolicy.affects("S1","S2"));
    }
    @Test void originalVerdictRetainedUntilFormalOverturn() {
        assertNull(CommunityTrackSprintReviewPolicy.latest(List.of()));
        var requested=CommunityTrackSprintReview.builder().action("REQUESTED").build();
        var upheld=CommunityTrackSprintReview.builder().action("UPHELD").build();
        var overruled=CommunityTrackSprintReview.builder().action("OVERTURNED").build();
        assertEquals("REQUESTED",CommunityTrackSprintReviewPolicy.latest(List.of(requested)));
        assertEquals("UPHELD",CommunityTrackSprintReviewPolicy.latest(List.of(requested,upheld)));
        assertEquals("OVERTURNED",CommunityTrackSprintReviewPolicy.latest(List.of(requested,overruled)));
    }
}
