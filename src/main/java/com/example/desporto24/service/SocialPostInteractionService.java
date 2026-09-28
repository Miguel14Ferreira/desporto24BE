package com.example.desporto24.service;

import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.dto.social.CreateSocialCommentRequest;
import com.example.desporto24.model.dto.social.PostLikeResponse;
import com.example.desporto24.model.dto.social.SocialCommentResponse;
import com.example.desporto24.model.social.SocialPost;
import com.example.desporto24.model.social.SocialPostComment;
import com.example.desporto24.model.social.SocialPostLike;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.social.SocialPostCommentRepository;
import com.example.desporto24.repository.social.SocialPostLikeRepository;
import com.example.desporto24.repository.social.SocialPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
@Transactional
public class SocialPostInteractionService {

    private final SocialPostRepository postRepository;
    private final SocialPostLikeRepository likeRepository;
    private final SocialPostCommentRepository commentRepository;
    private final ProfileRepository profileRepository;

    public PostLikeResponse like(Long postId, String username) {
        SocialPost post = post(postId);
        Profile actor = profile(username);

        if (!likeRepository.existsByPostIdAndProfileId(postId, actor.getId())) {
            likeRepository.save(SocialPostLike.builder()
                    .post(post)
                    .profile(actor)
                    .createdAt(Instant.now())
                    .build());
        }

        return likeStatus(postId, actor.getId());
    }

    public PostLikeResponse unlike(Long postId, String username) {
        Profile actor = profile(username);
        post(postId);

        likeRepository.deleteByPostIdAndProfileId(postId, actor.getId());
        return likeStatus(postId, actor.getId());
    }

    @Transactional(readOnly = true)
    public List<SocialCommentResponse> comments(Long postId) {
        post(postId);
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SocialCommentResponse comment(
            Long postId,
            String username,
            CreateSocialCommentRequest request) {
        SocialPost post = post(postId);
        Profile actor = profile(username);

        SocialPostComment comment = commentRepository.save(
                SocialPostComment.builder()
                        .post(post)
                        .author(actor)
                        .content(request.content().trim())
                        .createdAt(Instant.now())
                        .build()
        );

        return toResponse(comment);
    }

    public void deleteComment(
            Long postId,
            Long commentId,
            String username) {
        Profile actor = profile(username);
        SocialPostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comentário não encontrado"));

        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("O comentário não pertence a esta publicação");
        }

        if (!comment.getAuthor().getId().equals(actor.getId())) {
            throw new AccessDeniedException("Só podes apagar os teus comentários");
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public long likeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Transactional(readOnly = true)
    public boolean likedBy(Long postId, Long profileId) {
        return likeRepository.existsByPostIdAndProfileId(postId, profileId);
    }

    @Transactional(readOnly = true)
    public long commentCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    private PostLikeResponse likeStatus(Long postId, Long profileId) {
        return new PostLikeResponse(
                likeRepository.existsByPostIdAndProfileId(postId, profileId),
                likeRepository.countByPostId(postId)
        );
    }

    private SocialCommentResponse toResponse(SocialPostComment comment) {
        Profile author = comment.getAuthor();
        return new SocialCommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                author.getUsername(),
                displayName(author),
                author.getProfilePicUrl(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }

    private String displayName(Profile profile) {
        if (profile.getNickname() != null && !profile.getNickname().isBlank()) {
            return profile.getNickname();
        }
        if (profile.getFullName() != null && !profile.getFullName().isBlank()) {
            return profile.getFullName();
        }
        return profile.getUsername();
    }

    private SocialPost post(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publicação não encontrada"));
    }

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
    }
}
