package org.example.crewit.service.like;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.entity.comment.PostComment;
import org.example.crewit.model.entity.like.PostLike;
import org.example.crewit.model.entity.notification.Notification;
import org.example.crewit.model.entity.post.Post;
import org.example.crewit.model.entity.user.User;
import org.example.crewit.repository.like.PostLikeRepository;
import org.example.crewit.repository.post.PostRepository;
import org.example.crewit.service.notification.NotificationService;
import org.example.crewit.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * packageName : org.example.crewit.service.service
 * fileName : PostLikeService
 * author : PC
 * date : 2024-08-09
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-09         PC          최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final NotificationService notificationService;


    // todo 좋아요 존재 확인함수
    boolean existsPostLike(Post post, User user){
        return postLikeRepository.existsByUserAndPost(user, post);
    }



    // todo 좋아요 함수
    @Transactional
    public long likePost(String token, Post post) {
        User userLiked = userService.getUserFromToken(token);
        boolean alreadyLiked = existsPostLike(post,userLiked);
        log.debug("서비스3");

        if (alreadyLiked) {
            log.debug("게시물 좋아요 존재");
            throw new RuntimeException("이미 이 게시물을 좋아요했습니다.");
        }

        // PostLike 저장
        PostLike postLike = new PostLike();
        postLike.setUser(userLiked);
        postLike.setPost(post);
        long savedLikeId = postLikeRepository.save(postLike).getLikeId();

        post.addLike();
        postRepository.save(post);

        // 알림생성
        Notification notification = new Notification();
        notification.setUser(post.getUser());
        notification.setUrl("post-detail/" + post.getPostId());
        notification.setContent(userLiked.getNickname()+"님이 회원님의 게시물을 좋아합니다. 게시물: " + post.getTitle());
        notification.setIsRead("N");
        notification.setNotificationType(Notification.NotificationType.LIKE);

        notificationService.saveNotification(notification);

        return savedLikeId;
    }

    // todo 좋아요 취소
    @Transactional
    public void unLikePost(Long likeId) {
        // 사용자가 이미 이 게시물을 좋아요 했는지 확인
        Optional<PostLike> postLike = postLikeRepository.findById(likeId);

        if (postLike.isEmpty()) {
            throw new RuntimeException("게시물을 좋아요하지 않았습니다");
        }

        postLikeRepository.deleteById(likeId);

        // cascade 로 연결 되어있어서 좋아요 있다면 글은 무조건 있음
        Optional<Post> optionalPost = postRepository.findById(postLike.get().getPost().getPostId());
        optionalPost.get().removeLike();

        postRepository.save(optionalPost.get());
    }

    // todo 좋아요 조회 함수
    public PostLike findPostLikeByUserAndPost(Long postId, String userEmail) {

        Optional<PostLike> optionalPostLike = postLikeRepository.findByUserEmailAndPostId(postId, userEmail);

        return optionalPostLike.orElse(null);
    }
}
