package com.example.desporto24.service;

import com.example.desporto24.enumeration.AttendanceStatus;
import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.profile.ProfileActivityResponse;
import com.example.desporto24.model.session.SessionParticipant;
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

        List<SessionParticipant> attended = participantRepository
                .findByProfileIdAndAttendanceStatusOrderByAttendanceRecordedAtDesc(
                        profile.getId(),
                        AttendanceStatus.ATTENDED
                );

        Set<String> sports = new HashSet<>();
        Set<Long> people = new HashSet<>();
        Set<Long> attendedSessionIds = new HashSet<>();
        Set<String> activeWeeks = new HashSet<>();

        for (SessionParticipant ownAttendance : attended) {
            Session session = ownAttendance.getSession();
            if (session == null) {
                continue;
            }

            attendedSessionIds.add(session.getId());

            if (session.getSport() != null && !session.getSport().isBlank()) {
                sports.add(session.getSport().trim().toLowerCase(Locale.ROOT));
            }

            if (session.getGameDate() != null) {
                var localDate = session.getGameDate().toInstant()
                        .atZone(java.time.ZoneId.of("Europe/Lisbon"))
                        .toLocalDate();
                var weekFields = WeekFields.ISO;
                activeWeeks.add(
                        localDate.get(weekFields.weekBasedYear())
                                + "-"
                                + localDate.get(weekFields.weekOfWeekBasedYear())
                );
            }

        }

        if (!attendedSessionIds.isEmpty()) {
            people.addAll(
                    participantRepository
                            .findDistinctProfileIdsBySessionIdsAndAttendanceStatusExcludingProfile(
                                    attendedSessionIds,
                                    AttendanceStatus.ATTENDED,
                                    profile.getId()
                            )
            );
        }

        List<Session> createdSessions =
                sessionRepository.findByProfileIdOrderByGameDateAsc(profile.getId());

        int mobilized = 0;
        if (!createdSessions.isEmpty()) {
            Set<Long> createdSessionIds = createdSessions.stream()
                    .map(Session::getId)
                    .collect(java.util.stream.Collectors.toSet());

            mobilized = Math.toIntExact(
                    participantRepository
                            .countBySessionIdsAndAttendanceStatusExcludingProfile(
                                    createdSessionIds,
                                    AttendanceStatus.ATTENDED,
                                    profile.getId()
                            )
            );
        }

        long eventsCreated = sessionRepository.countByProfileId(profile.getId());
        long communitiesJoined = communityMemberRepository.countByProfileId(profile.getId());

        LinkedHashSet<String> achievements = new LinkedHashSet<>();
        if (!attended.isEmpty()) achievements.add("FIRST_ACTIVITY");
        if (attended.size() >= 10) achievements.add("TEN_ACTIVITIES");
        if (sports.size() >= 5) achievements.add("EXPLORER");
        if (eventsCreated > 0) achievements.add("ORGANIZER");
        if (mobilized >= 10) achievements.add("COMMUNITY_BUILDER");
        if (activeWeeks.size() >= 3) achievements.add("REGULAR");

        return new ProfileActivityResponse(
                attended.size(),
                people.size(),
                sports.size(),
                eventsCreated,
                mobilized,
                communitiesJoined,
                List.copyOf(achievements)
        );
    }
}
