package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.profile.ProfileActivityResponse;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.WeekFields;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileActivityService {

    private final ProfileRepository profileRepository;
    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository participantRepository;
    private final CommunityMemberRepository communityMemberRepository;

    public ProfileActivityResponse activity(String username) {
        Profile profile = profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));

        long attendedCount = participantRepository.countByProfileIdAndAttendanceStatus(
                profile.getId(),
                AttendanceStatus.ATTENDED
        );

        Set<String> sports = new HashSet<>(
                participantRepository.findDistinctSportsByProfileIdAndAttendanceStatus(
                        profile.getId(),
                        AttendanceStatus.ATTENDED
                )
        );

        Set<String> activeWeeks = new HashSet<>();
        for (java.util.Date gameDate :
                participantRepository.findGameDatesByProfileIdAndAttendanceStatus(
                        profile.getId(),
                        AttendanceStatus.ATTENDED
                )) {
            var localDate = gameDate.toInstant()
                    .atZone(java.time.ZoneId.of("Europe/Lisbon"))
                    .toLocalDate();
            var weekFields = WeekFields.ISO;
            activeWeeks.add(
                    localDate.get(weekFields.weekBasedYear())
                            + "-"
                            + localDate.get(weekFields.weekOfWeekBasedYear())
            );
        }

        long peopleCount = participantRepository.countDistinctProfilesAttendedWith(
                profile.getId(),
                AttendanceStatus.ATTENDED
        );

        int mobilized = Math.toIntExact(
                participantRepository.countAttendedParticipantsForCreatedSessions(
                        profile.getId(),
                        AttendanceStatus.ATTENDED
                )
        );

        long eventsCreated = sessionRepository.countByProfileId(profile.getId());
        long communitiesJoined = communityMemberRepository.countByProfileId(profile.getId());

        LinkedHashSet<String> achievements = new LinkedHashSet<>();
        if (attendedCount > 0) achievements.add("FIRST_ACTIVITY");
        if (attendedCount >= 10) achievements.add("TEN_ACTIVITIES");
        if (sports.size() >= 5) achievements.add("EXPLORER");
        if (eventsCreated > 0) achievements.add("ORGANIZER");
        if (mobilized >= 10) achievements.add("COMMUNITY_BUILDER");
        if (activeWeeks.size() >= 3) achievements.add("REGULAR");

        return new ProfileActivityResponse(
                Math.toIntExact(attendedCount),
                Math.toIntExact(peopleCount),
                sports.size(),
                eventsCreated,
                mobilized,
                communitiesJoined,
                List.copyOf(achievements)
        );
    }
}
