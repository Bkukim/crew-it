package org.example.crewit.service.post;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.dto.post.PostReq;
import org.example.crewit.model.entity.notification.Notification;
import org.example.crewit.model.entity.user.User;
import org.example.crewit.model.entity.post.Post;
import org.example.crewit.repository.post.PostRepository;
import org.example.crewit.security.jwt.JwtUtils;
import org.example.crewit.service.notification.NotificationService;
import org.example.crewit.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * packageName : org.example.crewit.service.post
 * fileName : PostService
 * author : PC
 * date : 2024-08-06
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-06         PC          최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final NotificationService notificationService;




    // todo 게시물 최신순 전체 조회
    public Map<String, Object> findByTitleContainingOrderByCreateTime(int page, int size, String title) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Post> allPostSummaries
                = postRepository
                .findPostsOrderByCreateTime(pageable,title);

        Map<String, Object> LatestPosts = new HashMap<>();
        LatestPosts.put("posts", allPostSummaries.getContent());       // faq 배열
        LatestPosts.put("currentPage", allPostSummaries.getNumber());       // 현재페이지번호
        LatestPosts.put("totalItems", allPostSummaries.getTotalElements()); // 총건수(개수)
        LatestPosts.put("totalPages", allPostSummaries.getTotalPages());    // 총페이지수

        return LatestPosts;
    }

    // todo 게시물 최신순 전체 조회
    public Map<String, Object> findByTitleContainingOrderByLikes(int page, int size, String title) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Post> allPostSummaries
                = postRepository
                .findPostsOrderByLikes(pageable,title);

        Map<String, Object> LatestPosts = new HashMap<>();
        LatestPosts.put("posts", allPostSummaries.getContent());       // faq 배열
        LatestPosts.put("currentPage", allPostSummaries.getNumber());       // 현재페이지번호
        LatestPosts.put("totalItems", allPostSummaries.getTotalElements()); // 총건수(개수)
        LatestPosts.put("totalPages", allPostSummaries.getTotalPages());    // 총페이지수

        return LatestPosts;
    }

    // todo 자식 엔티티를 포함하여 Parent 엔티티를 가져오는 메소드
    @Transactional(readOnly = true)
    public Post getPostWithPostComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없습니다 " + postId));
        log.debug("가져온 게시물" + post.toString());
        return post;
    }

    // todo Post를 저장하는 메소드
    @Transactional
    public Post savePost(String token,PostReq postReq) {
        User user = userService.getUserFromToken(token);
        if (!user.getUserEmail().equals(postReq.getUserEmail())) {
            throw new RuntimeException("권한이 없습니다");
        }
        Post post = new Post();
        post.setUser(user);
        post.setTitle(postReq.getTitle());
        post.setContent(postReq.getContent());
//        post.setPostComments(post.getPostComments());
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(String token, Post post) {
        String jwt = jwtUtils.getJwtFromToken(token);
        String userEmailFromToken = jwtUtils.getUserNameFromJwtToken(jwt);
        if (!userEmailFromToken.equals(post.getUser().getUserEmail())) {
            throw new RuntimeException("권한이 없습니다");
        }
//        post.setPostComments(post.getPostComments());
        return postRepository.save(post);
    }

    // todo 특정 Post 엔티티를 삭제하는 메소드
    @Transactional
    public void deletePost(String token, Long postId) {

        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new EntityNotFoundException("해당 정보로 게시물을 찾을 수 없습니다. postId = " + postId);
        }
        log.debug("실행되나");
        String jwt = jwtUtils.getJwtFromToken(token);
        String userEmailFromToken = jwtUtils.getUserNameFromJwtToken(jwt);
        if (!post.get().getUser().getUserEmail().equals(userEmailFromToken) ) {
            throw new RuntimeException("권한이 없습니다");
        }

        postRepository.deleteById(postId);
    }

    // todo 특정 Post 엔티티를 삭제하는 메소드
    @Transactional
    public void deletePostAsAdmin(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시물을 찾을수 없음"));
        // 알림 생성
        Notification notification = new Notification();
        notification.setUser(post.getUser());
        notification.setContent("회원님의 게시물이 관리자의 의해 삭제되었습니다. 게시물: " + post.getTitle());
        notification.setIsRead("N");
        notification.setNotificationType(Notification.NotificationType.DELETE);

        notificationService.saveNotification(notification);
        postRepository.deleteById(postId);
    }

    // todo
    public Map<String, Object> findPostsByUserEmailOrderByCreateTime(int page, int size, String userEmail) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Post> usersPostSummaries
                = postRepository
                .findPostsByUserEmailOrderByCreateTime(pageable,userEmail);

        Map<String, Object> usersPosts = new HashMap<>();
        usersPosts.put("posts", usersPostSummaries.getContent());       // faq 배열
        usersPosts.put("currentPage", usersPostSummaries.getNumber());       // 현재페이지번호
        usersPosts.put("totalItems", usersPostSummaries.getTotalElements()); // 총건수(개수)
        usersPosts.put("totalPages", usersPostSummaries.getTotalPages());    // 총페이지수

        return usersPosts;
    }
}
