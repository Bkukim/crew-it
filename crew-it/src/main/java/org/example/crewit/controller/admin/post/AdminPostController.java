package org.example.crewit.controller.admin.post;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.service.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * packageName : org.example.crewit.controller.admin.post
 * fileName : AdminPostController
 * author : PC
 * date : 2024-08-14
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-14         PC          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostService postService;


    // todo 삭제함수
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost( @PathVariable Long postId) {
        try {

            postService.deletePostAsAdmin(postId);
            return ResponseEntity.ok("게시물이 성공적으로 삭제되었습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 삭제 실패");
        }
    }
}
