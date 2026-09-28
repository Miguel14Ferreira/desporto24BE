package com.example.desporto24.model.dto.profile;

import java.util.List;

public record ProfileActivityResponse(
        int activitiesCompleted,
        int peoplePlayedWith,
        int sportsPractised,
        long eventsCreated,
        int participantsMobilized,
        long communitiesJoined,
        List<String> achievements
) {
}
