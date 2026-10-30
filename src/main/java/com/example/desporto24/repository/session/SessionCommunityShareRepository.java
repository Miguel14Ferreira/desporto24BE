package com.example.desporto24.repository.session;

import com.example.desporto24.model.Session;
import com.example.desporto24.model.community.CommunityMember;
import com.example.desporto24.model.session.SessionCommunityShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SessionCommunityShareRepository extends JpaRepository<SessionCommunityShare, Long> {
    /** Community associations for a session, also when it is shared with several communities. */
    List<SessionCommunityShare> findBySession_IdOrderByIdAsc(Long sessionId);

    boolean existsBySession_Id(Long sessionId);

    @Query("""
        select distinct share.session from SessionCommunityShare share
        where share.community.id = :communityId
        order by share.session.gameDate desc
        """)
    List<Session> sessionsSharedWithCommunity(@Param("communityId") Long communityId);

    /** A user may join a shared session if they belong to at least one target community. */
    @Query("""
        select case when count(member) > 0 then true else false end
        from SessionCommunityShare share, CommunityMember member
        where share.session.id = :sessionId
          and member.community.id = share.community.id
          and member.profile.id = :profileId
        """)
    boolean isMemberOfSharedCommunity(@Param("sessionId") Long sessionId,
                                      @Param("profileId") Long profileId);
}
