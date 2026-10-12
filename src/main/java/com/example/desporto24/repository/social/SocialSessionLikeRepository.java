package com.example.desporto24.repository.social;

import com.example.desporto24.model.social.SocialSessionLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface SocialSessionLikeRepository extends JpaRepository<SocialSessionLike, Long> {

    boolean existsBySessionIdAndProfileId(Long sessionId, Long profileId);

    long countBySessionId(Long sessionId);

    long deleteBySessionIdAndProfileId(Long sessionId, Long profileId);

    @Query("select l.session.id, count(l.id) from SocialSessionLike l where l.session.id in :sessionIds group by l.session.id")
    List<Object[]> countBySessionIds(@Param("sessionIds") Set<Long> sessionIds);

    @Query("select l.session.id from SocialSessionLike l where l.session.id in :sessionIds and l.profile.id = :profileId")
    Set<Long> findLikedSessionIds(@Param("sessionIds") Set<Long> sessionIds, @Param("profileId") Long profileId);
}
