package com.example.desporto24.service;

import com.example.desporto24.exception.UserNotFoundException;
import com.example.desporto24.model.Profile;
import com.example.desporto24.model.Session;
import com.example.desporto24.model.dto.social.CreateSocialPostRequest;
import com.example.desporto24.model.dto.social.FeedItemResponse;
import com.example.desporto24.model.social.SocialPost;
import com.example.desporto24.model.session.SessionParticipant;
import com.example.desporto24.repository.ProfileRepository;
import com.example.desporto24.repository.SessionRepository;
import com.example.desporto24.repository.social.ProfileFollowRepository;
import com.example.desporto24.repository.social.SocialPostRepository;
import com.example.desporto24.repository.social.SocialPostLikeRepository;
import com.example.desporto24.repository.social.SocialPostCommentRepository;
import com.example.desporto24.repository.session.SessionParticipantRepository;
import com.example.desporto24.storage.MediaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.UUID;

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
    private final SessionParticipantRepository participantRepository;

    @Autowired(required = false)
    private MediaStorage mediaStorage;

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

    public FeedItemResponse createPostWithImage(
            String username,
            String content,
            MultipartFile image) {
        String normalizedContent = content == null ? "" : content.trim();

        if (normalizedContent.isBlank()) {
            throw new IllegalArgumentException("A publicação não pode estar vazia");
        }
        if (normalizedContent.length() > 1000) {
            throw new IllegalArgumentException("A publicação não pode ter mais de 1000 caracteres");
        }
        if (image == null || image.isEmpty()) {
            return createPost(
                    username,
                    new CreateSocialPostRequest(normalizedContent)
            );
        }
        if (mediaStorage == null) {
            throw new IllegalStateException("Media storage is not configured");
        }

        Profile author = profile(username);
        String baseKey = "posts/" + author.getId() + "/" + UUID.randomUUID();
        URI uri = mediaStorage.store(baseKey, image);
        String storageKey = storageKey(uri);

        SocialPost post = postRepository.save(SocialPost.builder()
                .author(author)
                .content(normalizedContent)
                .imageUrl(uri.toString())
                .imageStorageKey(storageKey)
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

        likeRepository.deleteByPostId(postId);
        commentRepository.deleteByPostId(postId);
        postRepository.delete(post);
        postRepository.flush();

        if (
                mediaStorage != null
                && post.getImageStorageKey() != null
                && !post.getImageStorageKey().isBlank()
        ) {
            mediaStorage.delete(post.getImageStorageKey());
        }
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

        List<SessionParticipant> attendedSessions =
                participantRepository.findPublicAttendanceFeed(
                        authorIds,
                        PageRequest.of(0, FEED_LIMIT)
                );

        Set<Long> postIds = posts.stream()
                .map(SocialPost::getId)
                .collect(Collectors.toSet());
        Map<Long, Long> likeCounts = countsByPostId(
                postIds.isEmpty() ? List.of() : likeRepository.countByPostIds(postIds));
        Map<Long, Long> commentCounts = countsByPostId(
                postIds.isEmpty() ? List.of() : commentRepository.countByPostIds(postIds));
        Set<Long> likedPostIds = postIds.isEmpty()
                ? Set.of()
                : likeRepository.findLikedPostIds(postIds, actor.getId());

        List<FeedItemResponse> items = new ArrayList<>();
        posts.stream()
                .map(post -> postItem(post, likeCounts, commentCounts, likedPostIds))
                .forEach(items::add);
        sessions.stream().map(this::sessionItem).forEach(items::add);
        attendedSessions.stream().map(this::attendanceItem).forEach(items::add);

        return items.stream()
                .sorted((left, right) -> right.createdAt().compareTo(left.createdAt()))
                .limit(FEED_LIMIT)
                .toList();
    }

    private FeedItemResponse postItem(SocialPost post, Profile viewer) {
        return postItem(post, Map.of(), Map.of(), Set.of());
    }

    private FeedItemResponse postItem(
            SocialPost post,
            Map<Long, Long> likeCounts,
            Map<Long, Long> commentCounts,
            Set<Long> likedPostIds) {
        Profile author = post.getAuthor();

        return new FeedItemResponse(
                "POST",
                post.getId(),
                author.getUsername(),
                displayName(author),
                author.getProfilePicUrl(),
                post.getContent(),
                post.getImageUrl(),
                post.getCreatedAt(),
                null,
                null,
                null,
                null,
                likeCounts.getOrDefault(post.getId(), 0L),
                likedPostIds.contains(post.getId()),
                commentCounts.getOrDefault(post.getId(), 0L)
        );
    }

    private Map<Long, Long> countsByPostId(List<Object[]> rows) {
        return rows.stream().collect(Collectors.toMap(
                row -> (Long) row[0],
                row -> (Long) row[1]
        ));
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

    private FeedItemResponse attendanceItem(
            SessionParticipant participant) {
        Profile author = participant.getProfile();
        Session session = participant.getSession();

        return new FeedItemResponse(
                "SESSION_ATTENDED",
                null,
                author.getUsername(),
                displayName(author),
                author.getProfilePicUrl(),
                null,
                null,
                participant.getAttendanceRecordedAt(),
                session.getId(),
                session.getSport(),
                session.getCity(),
                session.getGameDate() == null
                        ? null
                        : session.getGameDate().toInstant(),
                0,
                false,
                0
        );
    }

    private String storageKey(URI uri) {
        String path = uri.getPath();
        if (path == null || path.isBlank()) {
            return null;
        }

        int index = path.indexOf("/posts/");
        if (index >= 0) {
            return path.substring(index + 1);
        }

        index = path.indexOf("posts/");
        if (index >= 0) {
            return path.substring(index);
        }

        return path.startsWith("/") ? path.substring(1) : path;
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
