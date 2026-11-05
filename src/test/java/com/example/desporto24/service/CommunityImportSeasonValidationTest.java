package com.example.desporto24.service;

import com.example.desporto24.enumeration.CommunityRole;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.community.Community;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.community.CommunitySeason;
import com.example.desporto24.model.dto.community.CommunityImportContextRequest;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.community.CommunityMemberRepository;
import com.example.desporto24.repository.community.CommunitySeasonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommunityImportSeasonValidationTest {
    @Mock private ProfileRepository profileRepository;
    @Mock private CommunityMemberRepository memberRepository;
    @Mock private CommunitySeasonRepository communitySeasonRepository;
    @InjectMocks private CommunityImportService service;

    private CommunitySeason prepare(String seasonSport) {
        Profile owner = new Profile();
        owner.setId(1L);
        owner.setUsername("owner");
        Community community = Community.builder().id(7L)
                .sports(List.of("Futebol", "Karting")).build();
        CommunitySeason season = CommunitySeason.builder().id(44L)
                .community(community).sport(seasonSport).active(false)
                .name("2025/26").build();
        when(profileRepository.findUserByUsernameIgnoreCase("owner"))
                .thenReturn(Optional.of(owner));
        when(memberRepository.findByCommunityIdAndProfileId(7L, 1L))
                .thenReturn(Optional.of(CommunityMember.builder()
                        .community(community).profile(owner).role(CommunityRole.OWNER).build()));
        when(communitySeasonRepository.findById(44L)).thenReturn(Optional.of(season));
        return season;
    }

    @Test
    void rejectsMismatchedSportBeforeImportPlanOrWrites() {
        prepare("Futebol");
        var context = new CommunityImportContextRequest(44L, 2025, null, null, "Karting");
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.validateContext(7L, "owner", context));
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
    }

    @Test
    void acceptsMatchingHistoricalSeasonAndSharedArchive() {
        prepare("Karting");
        var result = service.validateContext(7L, "owner",
                new CommunityImportContextRequest(44L, 2025, null, null, "karting"));
        assertEquals(44L, result.seasonId());

        // A legacy shared season has no sport tag and accepts any declared sport.
        var shared = CommunitySeason.builder().id(44L)
                .community(Community.builder().id(7L).sports(List.of("Futebol", "Karting")).build())
                .sport(null).active(false).name("Histórico").build();
        when(communitySeasonRepository.findById(44L)).thenReturn(Optional.of(shared));
        assertEquals(44L, service.validateContext(7L, "owner",
                new CommunityImportContextRequest(44L, 2025, null, null, "Futebol")).seasonId());
    }
}
