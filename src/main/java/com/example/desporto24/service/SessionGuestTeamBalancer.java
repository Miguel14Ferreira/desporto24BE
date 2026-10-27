package com.example.desporto24.service;

import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.CommunityPlayer;
import com.example.desporto24.model.community.CommunityStandingsArchiveRow;
import com.example.desporto24.model.dto.session.SessionTeamPlayerResponse;
import com.example.desporto24.model.dto.session.SessionTeamSuggestionResponse;
import com.example.desporto24.model.session.SessionGuestRequest;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.community.CommunityPlayerRepository;
import com.example.desporto24.repository.community.CommunityStandingsArchiveRepository;
import com.example.desporto24.repository.community.CommunityStandingsArchiveRowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.*;

/** Exact Excel ranking balance for real accounts and organizer-approved guests. */
@Service
@RequiredArgsConstructor
public class SessionGuestTeamBalancer {
    private final CommunityStandingsArchiveRepository archives;
    private final CommunityStandingsArchiveRowRepository rows;
    private final CommunityPlayerRepository communityPlayers;

    public SessionTeamSuggestionResponse suggest(Session session,List<SessionParticipant> registered,
                                                 List<SessionGuestRequest> guests) {
        Long communityId=session.getCommunity().getId();
        var archive=archives.findFirstByCommunityIdAndEntryTypeOrderByCreatedAtDescIdDesc(
                communityId,"INDIVIDUAL").orElseThrow(()->new ResponseStatusException(
                HttpStatus.CONFLICT,"Importa primeiro uma classificação individual Excel"));
        Map<String,CommunityStandingsArchiveRow> standings=new HashMap<>();
        for(var row:rows.findByArchiveIdOrderByPositionAscIdAsc(archive.getId())){
            standings.put(row.getNormalizedName(),row);
        }
        Map<Long,Set<String>> aliases=new HashMap<>();
        for(CommunityPlayer p:communityPlayers.findByCommunityIdOrderByDisplayNameAsc(communityId)){
            if(p.getProfile()!=null){
                aliases.computeIfAbsent(p.getProfile().getId(),unused->new HashSet<>())
                        .add(p.getNormalizedName());
            }
        }
        List<Candidate> candidates=new ArrayList<>();
        for(SessionParticipant member:registered){
            Profile p=member.getProfile();
            Set<String> names=new HashSet<>(aliases.getOrDefault(p.getId(),Set.of()));
            for(String name:List.of(p.getUsername()==null?"":p.getUsername(),
                    p.getFullName()==null?"":p.getFullName(),
                    p.getNickname()==null?"":p.getNickname())){
                if(!name.isBlank())names.add(normalized(name));
            }
            Set<CommunityStandingsArchiveRow> matches=new HashSet<>();
            for(String name:names){var row=standings.get(name);if(row!=null)matches.add(row);}
            var row=matches.size()==1?matches.iterator().next():null;
            candidates.add(new Candidate(p.getUsername(),p.getUsername(),false,row));
        }
        for(SessionGuestRequest guest:guests){
            String name=guest.getCommunityPlayer()==null?guest.getNormalizedName()
                    :guest.getCommunityPlayer().getNormalizedName();
            candidates.add(new Candidate("guest:"+guest.getId(),guest.getDisplayName(),true,
                    standings.get(name)));
        }
        Map<Long,Integer> collisions=new HashMap<>();
        for(Candidate c:candidates){
            if(c.row()!=null)collisions.merge(c.row().getId(),1,Integer::sum);
        }
        int known=0;double knownTotal=0.0;
        for(Candidate c:candidates){
            if(c.row()!=null&&collisions.get(c.row().getId())==1){
                known++;knownTotal+=c.row().getPoints();
            }
        }
        if(known==0)throw new ResponseStatusException(HttpStatus.CONFLICT,
                "A classificação Excel não contém os participantes desta sessão");
        double mean=knownTotal/known;
        List<Seed> seeds=new ArrayList<>();
        for(Candidate c:candidates){
            var row=c.row()!=null&&collisions.get(c.row().getId())==1?c.row():null;
            seeds.add(new Seed(c.key(),c.name(),c.guest(),row,
                    row==null?mean:row.getPoints()));
        }
        seeds.sort(Comparator.comparing(Seed::key,String.CASE_INSENSITIVE_ORDER));
        boolean[] assigned=CommunityExcelTeamBalancer.balanceScores(
                seeds.stream().map(Seed::score).toList());
        List<SessionTeamPlayerResponse> a=new ArrayList<>(),b=new ArrayList<>();
        double sumA=0,sumB=0;
        for(int i=0;i<seeds.size();i++){
            Seed s=seeds.get(i);var rank=s.row();
            var player=new SessionTeamPlayerResponse(s.key(),round(s.score()),rank!=null,
                    rank==null||rank.getPlayed()==null?0:rank.getPlayed(),
                    rank==null||rank.getWins()==null?0:rank.getWins(),
                    rank==null||rank.getGoalsFor()==null?0:rank.getGoalsFor(),0,
                    s.name(),s.guest());
            if(assigned[i]){a.add(player);sumA+=s.score();}
            else{b.add(player);sumB+=s.score();}
        }
        return new SessionTeamSuggestionResponse(List.copyOf(a),List.copyOf(b),
                round(sumA),round(sumB),known,seeds.size());
    }
    private static double round(double value){return Math.round(value*100.0)/100.0;}
    private static String normalized(String name){
        return Normalizer.normalize(name.strip(),Normalizer.Form.NFD)
                .replaceAll("\\p{M}","").toLowerCase(Locale.ROOT).replaceAll("\\s+"," ");
    }
    private record Candidate(String key,String name,boolean guest,CommunityStandingsArchiveRow row){}
    private record Seed(String key,String name,boolean guest,CommunityStandingsArchiveRow row,double score){}
}
