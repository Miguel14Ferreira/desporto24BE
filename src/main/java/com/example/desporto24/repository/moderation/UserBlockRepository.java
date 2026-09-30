package com.example.desporto24.repository.moderation;

import com.example.desporto24.model.moderation.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {

    boolean existsByBlockerIdAndBlockedId(Long blockerId, Long blockedId);

    long deleteByBlockerIdAndBlockedId(Long blockerId, Long blockedId);

    @Query("""
            select case when count(block) > 0 then true else false end
            from UserBlock block
            where (block.blocker.id = :firstId and block.blocked.id = :secondId)
               or (block.blocker.id = :secondId and block.blocked.id = :firstId)
            """)
    boolean existsBetween(Long firstId, Long secondId);
}
