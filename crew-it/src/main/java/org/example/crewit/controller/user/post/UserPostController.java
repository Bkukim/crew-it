package org.example.crewit.controller.user.post;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.dto.post.CommentReplyReq;
import org.example.crewit.model.dto.post.PostCommentReq;
import org.example.crewit.model.dto.post.PostReq;
import org.example.crewit.model.entity.comment.PostComment;
import org.example.crewit.model.entity.post.Post;
import org.example.crewit.model.entity.user.User;
import org.example.crewit.service.comment.CommentService;
import org.example.crewit.service.post.PostService;
import org.example.crewit.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * packageName : org.example.crewit.controller.post
 * fileName : PostController
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
@RestController
@RequestMapping("/api/user/posts")
@RequiredArgsConstructor
public class UserPostController {

    private final PostService postService;
    private final CommentService commentService;

    //    todo: 저장함수
    @PostMapping
    public ResponseEntity<Object> createPost(@RequestHeader("Authorization") String token, @RequestBody PostReq postReq) {
        try {
            Post savedPost = postService.savePost(token, postReq);
            return ResponseEntity.ok(savedPost);
        }catch (Exception e){
            log.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 저장 실패");
        }

    }

    //    todo: 수정함수
    @PutMapping("/{postId}")
    public ResponseEntity<Object> updatePost(@RequestHeader("Authorization") String token, @PathVariable Long postId, @RequestBody Post post) {
        try {

            Post updatedPost = postService.updatePost(token, post);
            return ResponseEntity.ok(updatedPost);
        }catch (Exception e){
            log.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 수정 실패");
        }

    }

    // todo 삭제함수
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@RequestHeader("Authorization") String token, @PathVariable Long postId) {
        try {
            postService.deletePost(token,postId);
            return ResponseEntity.ok("게시물이 성공적으로 삭제되었습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 삭제 실패");
        }
    }
    // todo 댓글 저장함수
    @PostMapping("/comments")
    public ResponseEntity<Object> CreatePostComment(@RequestHeader("Authorization") String token, @RequestBody PostCommentReq postCommentReq) {
        try {
            PostComment savedComment = commentService.savePostComment(token, postCommentReq);
            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            log.debug("댓글 저장 오류" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 저장에 실패하였습니다.");
        }
    }

    // todo 댓글 저장함수
    @PostMapping("/comments/replies")
    public ResponseEntity<Object> CreateCommentReply(@RequestHeader("Authorization") String token, @RequestBody CommentReplyReq commentReplyReq) {
        try {
            PostComment savedReply = commentService.saveCommentReply(token, commentReplyReq);
            return ResponseEntity.ok(savedReply);
        } catch (Exception e) {
            log.debug("댓글 저장 오류" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 저장에 실패하였습니다.");
        }
    }

    // todo 회원 게시물 전체 조회
    @GetMapping
    public ResponseEntity<Object> getLatestPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String userEmail
    ) {
        try {
            Map<String, Object> usersPosts = postService.findPostsByUserEmailOrderByCreateTime(page, size, userEmail);
            log.debug("가져온 객체들"+usersPosts.get("posts").toString());
            return ResponseEntity.ok(usersPosts);
        } catch (Exception e) {
            log.error("회원 게시글을 가져오는 데 실패", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 조회 실패");
        }
    }
}
