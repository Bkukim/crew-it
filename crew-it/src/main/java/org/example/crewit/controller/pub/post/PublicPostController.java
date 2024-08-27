package org.example.crewit.controller.pub.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.entity.like.PostLike;
import org.example.crewit.model.entity.post.Post;
import org.example.crewit.service.comment.CommentService;
import org.example.crewit.service.like.PostLikeService;
import org.example.crewit.service.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/public/posts")
@RequiredArgsConstructor
public class PublicPostController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;

    // todo 최신순 검색 전체 조회
    @GetMapping("/latest")
    public ResponseEntity<Object> getLatestPostsForSearching(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(defaultValue = "") String title
    ) {
        try {
            Map<String, Object> latestPosts = postService.findByTitleContainingOrderByCreateTime(page, size, title);
            return ResponseEntity.ok(latestPosts);
        } catch (Exception e) {
            log.error("최신순 게시물 조회 에러", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 조회 에러");
        }
    }

    // todo 좋아요순 검색 전체 조회
    @GetMapping("/most-liked")
    public ResponseEntity<Object> getMostLikedPostsForSearching(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String title
    ) {
        try {
            Map<String, Object> latestPosts = postService.findByTitleContainingOrderByLikes(page, size, title);
            return ResponseEntity.ok(latestPosts);
        } catch (Exception e) {
            log.error("좋아요순 게시물 조회 에러", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 조회 에러");
        }
    }


    // todo 게시물 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        try {
            Post post = postService.getPostWithPostComments(postId);
            if (post != null) {
                log.debug("가져온 게시물" + post);
                return ResponseEntity.ok(post);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error("상세조회 에러", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // todo 회원이 게시물 좋아요 했는지 확인하는 함수
    @GetMapping("/like")
    public ResponseEntity<Object> checkLikeStatus(
            @RequestParam(defaultValue = "") String userEmail,
            @RequestParam(defaultValue = "") Long postId) {

        PostLike postLikeOpt = postLikeService.findPostLikeByUserAndPost(postId, userEmail);

        return ResponseEntity.ok(postLikeOpt);
    }

    // todo 게시물 댓글 조회
    @GetMapping("/comments")
    public ResponseEntity<Object> getPostComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(defaultValue = "") long postId
    ) {
        try {
            Map<String, Object> comments = commentService.getPostCommentsByPostId(page, size, postId);
            if (comments.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(comments);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 조회 에러");
        }
    }

    //todo 댓글 갯수 조회
    @GetMapping("/comments/count")
    public ResponseEntity<Long> getPostCommentsCount(@RequestParam long postId) {
        long count = commentService.getPostCommentsCount(postId);
        return ResponseEntity.ok(count);
    }
}
