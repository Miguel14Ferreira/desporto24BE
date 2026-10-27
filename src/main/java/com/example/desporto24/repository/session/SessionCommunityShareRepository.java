package com.example.desporto24.repository.session;

import com.example.desporto24.model.Session;
import com.example.desporto24.model.session.SessionCommunityShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SessionCommunityShareRepository extends JpaRepository<SessionCommunityShare, Long> {
    @Query("""
        select distinct share.session from SessionCommunityShare share
        where share.community.id = :communityId
        order by share.session.gameDate desc
        """)
    List<Session> sessionsSharedWithCommunity(@Param("communityId") Long communityId);
}
