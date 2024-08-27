package org.example.crewit.service.comment;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.dto.post.CommentReplyReq;
import org.example.crewit.model.dto.post.PostCommentReq;
import org.example.crewit.model.entity.comment.PostComment;
import org.example.crewit.model.entity.notification.Notification;
import org.example.crewit.model.entity.post.Post;
import org.example.crewit.model.entity.user.User;
import org.example.crewit.repository.comment.PostCommentRepository;
import org.example.crewit.repository.post.PostRepository;
import org.example.crewit.security.jwt.JwtUtils;
import org.example.crewit.service.notification.NotificationService;
import org.example.crewit.service.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * packageName : org.example.crewit.service.comment
 * fileName : CommentService
 * author : PC
 * date : 2024-08-07
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-07         PC          최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final NotificationService notificationService;


    @Transactional
    public PostComment savePostComment(String token, PostCommentReq postCommentReq) {

        // token 으로 작성자 가져오기
        User commentWriter = userService.getUserFromToken(token);
        Post post = postRepository.findById(postCommentReq.getPostId()).orElseThrow(() -> new RuntimeException("게시물을 찾을수 없음"));

        PostComment postComment = new PostComment();
        postComment.setPost(post);
        postComment.setUser(commentWriter);
        postComment.setContent(postCommentReq.getContent());

        PostComment savedPostComment = postCommentRepository.save(postComment);
        log.debug("게시물 작성자 " + post.getUser());
        // 알림생성
        Notification notification = new Notification();
        notification.setUser(post.getUser());
        notification.setUrl("post-detail/" + post.getPostId());
        notification.setContent(commentWriter.getNickname()+"님이 회원님의 게시물에 댓글을 남겼습니다.: " + postComment.getContent());
        notification.setIsRead("N");
        notification.setNotificationType(Notification.NotificationType.COMMENT);

        notificationService.saveNotification(notification);

        return savedPostComment;
    }

    @Transactional
    public PostComment saveCommentReply(String token, CommentReplyReq commentReplyReq) {

        // token 으로 작성자 가져오기
        User replyWriter = userService.getUserFromToken(token);

        Post post = postRepository.findById(commentReplyReq.getPostId()).orElseThrow(() -> new RuntimeException("게시물을 찾을수 없음"));
        PostComment parentComment = postCommentRepository.findById(commentReplyReq.getParentId()).orElseThrow(() -> new RuntimeException("부모댓글을 찾을수 없음"));

        // 저장할 답글 생성
        PostComment commentReply = new PostComment();
        commentReply.setPost(post);
        commentReply.setUser(replyWriter);
        commentReply.setContent(commentReplyReq.getContent());

        parentComment.addChildrenComment(commentReply);

        PostComment savedReply = postCommentRepository.save(commentReply);
        postCommentRepository.save(parentComment);


        // 알림 생성
        Notification notification = new Notification();
        notification.setUser(parentComment.getUser());
        notification.setUrl("post-detail/" + post.getPostId());
        notification.setContent(replyWriter.getNickname()+"님이 회원님의 댓글에 응답을 남겼습니다.: " + commentReply.getContent());
        notification.setIsRead("N");
        notification.setNotificationType(Notification.NotificationType.COMMENT);

        notificationService.saveNotification(notification);

        return savedReply;
    }

    @Transactional
    public Map<String, Object> getPostCommentsByPostId(int page, int size, long postId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostComment> postComments = postCommentRepository.findPostCommentsOrderByCreateTime(pageable, postId);


        Map<String, Object> comments = new HashMap<>();
        comments.put("postComments", postComments.getContent());       // faq 배열
        comments.put("currentPage", postComments.getNumber());       // 현재페이지번호
        comments.put("totalItems", postComments.getTotalElements()); // 총건수(개수)
        comments.put("totalPages", postComments.getTotalPages());    // 총페이지수

        return comments;
    }


    public long getPostCommentsCount(long postId) {
        return postCommentRepository.countPostComments(postId);
    }
}
