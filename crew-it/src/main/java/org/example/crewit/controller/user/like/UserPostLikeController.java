package org.example.crewit.controller.user.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.entity.like.PostLike;
import org.example.crewit.model.entity.post.Post;
import org.example.crewit.service.like.PostLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * packageName : org.example.crewit.controller.user.post
 * fileName : UserPostLikeController
 * author : PC
 * date : 2024-08-10
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-10         PC          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api/user/posts/like")
@RequiredArgsConstructor
public class UserPostLikeController {

    private final PostLikeService postLikeService;

    // todo 좋아요 함수
    @PostMapping
    public ResponseEntity<Object> likePost(@RequestHeader("Authorization") String token, @RequestBody Post post){
        try {

            long savedLikeId = postLikeService.likePost(token, post);
            return ResponseEntity.ok(savedLikeId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            log.debug("좋아요 에러"+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("좋아요 실패");
        }
    }

    // todo 좋아요 취소 함수
    @DeleteMapping("/{likeId}")
    public ResponseEntity<String> unLikePost(@PathVariable Long likeId) {
        try {
            postLikeService.unLikePost(likeId);
            return ResponseEntity.ok("좋아요 취소 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("좋아요를 취소 실패");
        }
    }

    // todo 해당 회원이 게시물에 좋아요 했는지 확인함수
    @GetMapping
    public ResponseEntity<Object> checkLikeStatus(
            @RequestParam(defaultValue = "0") Long postId,
            @RequestParam(defaultValue = "") String userEmail) {
        try{
            PostLike postLikeOpt = postLikeService.findPostLikeByUserAndPost(postId, userEmail);
            // PostLike가 존재하면 200 OK와 함께 PostLike 객체를 반환하고,
            // 존재하지 않으면 204 No Content 상태 코드를 반환
            return ResponseEntity.ok(postLikeOpt.getLikeId());
        }catch (Exception e){
            log.debug("에러원인 ::: "+ e.getMessage());
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("좋아요를 불러오지 못하였습니다.");
        }

    }
}
