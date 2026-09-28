package com.example.desporto24.service;

import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.social.CreateSocialPostRequest;
import com.example.desporto24.model.dto.social.FeedItemResponse;
import com.example.desporto24.model.social.SocialPost;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.social.ProfileFollowRepository;
import com.example.desporto24.repository.social.SocialPostRepository;
import com.example.desporto24.repository.social.SocialPostLikeRepository;
import com.example.desporto24.repository.social.SocialPostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.example.desporto24.constant.UserImplConstant.NO_USER_FOUND_BY_USERNAME;

@Service
@RequiredArgsConstructor
@Transactional
public class SocialFeedService {

    private static final int FEED_LIMIT = 40;

    private final SocialPostRepository postRepository;
    private final ProfileFollowRepository followRepository;
    private final SocialPostLikeRepository likeRepository;
    private final SocialPostCommentRepository commentRepository;
    private final ProfileRepository profileRepository;
    private final SessionRepository sessionRepository;

    public FeedItemResponse createPost(
            String username,
            CreateSocialPostRequest request) {
        Profile author = profile(username);

        SocialPost post = postRepository.save(SocialPost.builder()
                .author(author)
                .content(request.content().trim())
                .createdAt(Instant.now())
                .build());

        return postItem(post, author);
    }

    public void deletePost(Long postId, String username) {
        Profile actor = profile(username);
        SocialPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Publicação não encontrada"));

        if (!post.getAuthor().getId().equals(actor.getId())) {
            throw new AccessDeniedException("Só podes apagar as tuas publicações");
        }

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public List<FeedItemResponse> feed(String username) {
        Profile actor = profile(username);

        Set<Long> authorIds = new LinkedHashSet<>();
        authorIds.add(actor.getId());
        followRepository.findByFollowerIdOrderByCreatedAtDesc(actor.getId())
                .stream()
                .map(follow -> follow.getFollowing().getId())
                .forEach(authorIds::add);

        boolean discoveryFallback = authorIds.size() == 1;

        List<SocialPost> posts = discoveryFallback
                ? postRepository.findAllByOrderByCreatedAtDesc(
                        PageRequest.of(0, FEED_LIMIT))
                : postRepository.findFeedPosts(
                        authorIds,
                        PageRequest.of(0, FEED_LIMIT));

        List<Session> sessions = discoveryFallback
                ? sessionRepository.findRecentPublicFeedSessions(
                        PageRequest.of(0, FEED_LIMIT))
                : sessionRepository.findPublicFeedSessions(
                        authorIds,
                        PageRequest.of(0, FEED_LIMIT));

        List<FeedItemResponse> items = new ArrayList<>();
        posts.stream().map(post -> postItem(post, actor)).forEach(items::add);
        sessions.stream().map(this::sessionItem).forEach(items::add);

        return items.stream()
                .sorted((left, right) -> right.createdAt().compareTo(left.createdAt()))
                .limit(FEED_LIMIT)
                .toList();
    }

    private FeedItemResponse postItem(SocialPost post, Profile viewer) {
        Profile author = post.getAuthor();

        return new FeedItemResponse(
                "POST",
                post.getId(),
                author.getUsername(),
                displayName(author),
                author.getProfilePicUrl(),
                post.getContent(),
                post.getCreatedAt(),
                null,
                null,
                null,
                null,
                likeRepository.countByPostId(post.getId()),
                likeRepository.existsByPostIdAndProfileId(
                        post.getId(),
                        viewer.getId()
                ),
                commentRepository.countByPostId(post.getId())
        );
    }

    private FeedItemResponse sessionItem(Session session) {
        Profile author = session.getProfile();
        Instant createdAt = session.getCreated_at() == null
                ? (session.getUpdatedAt() == null ? Instant.now() : session.getUpdatedAt())
                : session.getCreated_at().toInstant();

        return new FeedItemResponse(
                "SESSION_CREATED",
                null,
                author == null ? session.getUsername() : author.getUsername(),
                author == null ? session.getUsername() : displayName(author),
                author == null ? null : author.getProfilePicUrl(),
                null,
                createdAt,
                session.getId(),
                session.getSport(),
                session.getCity(),
                session.getGameDate() == null ? null : session.getGameDate().toInstant(),
                0,
                false,
                0
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

    private Profile profile(String username) {
        return profileRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException(NO_USER_FOUND_BY_USERNAME));
    }
}
