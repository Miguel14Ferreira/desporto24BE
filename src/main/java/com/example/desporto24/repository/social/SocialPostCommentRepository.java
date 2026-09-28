package com.example.desporto24.repository.social;

import com.example.desporto24.model.social.SocialPostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocialPostCommentRepository extends JpaRepository<SocialPostComment, Long> {

    List<SocialPostComment> findByPostIdOrderByCreatedAtAsc(Long postId);

    long countByPostId(Long postId);
}
