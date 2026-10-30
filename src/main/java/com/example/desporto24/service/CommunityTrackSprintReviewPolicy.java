package com.example.desporto24.service;

import com.example.desporto24.model.community.CommunityTrackSprintReview;
import java.util.*;

/** Pure policy shared by write guards and award projection. */
final class CommunityTrackSprintReviewPolicy {
    private CommunityTrackSprintReviewPolicy() {}

    static String latest(List<CommunityTrackSprintReview> events) {
        return events.isEmpty() ? null : events.get(events.size()-1).getAction();
    }

    static boolean blocks(String action) {
        return "REQUESTED".equals(action) || "OVERTURNED".equals(action);
    }

    static boolean affects(String affectedSlot,String targetSlot) {
        if(affectedSlot.equals(targetSlot)) return true;
        Set<String> visited=new HashSet<>();
        Deque<String> queue=new ArrayDeque<>(List.of(affectedSlot));
        while(!queue.isEmpty()) {
            String source=queue.removeFirst();
            if(!visited.add(source))continue;
            for(String child:CommunityTrackSprintOlympic24.nextSlots(source)) {
                if(child.equals(targetSlot))return true;
                queue.addLast(child);
            }
        }
        return false;
    }
}
