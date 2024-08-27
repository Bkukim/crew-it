package org.example.crewit.controller.user.user;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.entity.post.Post;
import org.example.crewit.model.entity.user.User;
import org.example.crewit.security.jwt.JwtUtils;
import org.example.crewit.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * packageName : org.example.crewit.controller.user.user
 * fileName : UserController
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
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    //    todo: 회원 수정 함수
    @PutMapping("/{userEmail}")
    public ResponseEntity<Object> updateUser(@RequestHeader("Authorization") String token, @PathVariable String userEmail, @RequestBody User user) {
        try {
            String jwt = jwtUtils.getJwtFromToken(token);
            String userEmailFromToken = jwtUtils.getUserNameFromJwtToken(jwt);
            if (!userEmailFromToken.equals(user.getUserEmail())) {
                throw new RuntimeException("권한이 없습니다");
            }
            User updatedUser = userService.save(user);
            return ResponseEntity.ok(updatedUser);
        }catch (Exception e){
            log.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보 수정 실패");
        }

    }
    //    todo: 회원 정보 호출 함수
    @GetMapping("/{userEmail}")
    public ResponseEntity<Object> getUser(@RequestHeader("Authorization") String token, @PathVariable String userEmail) {
        try {
            String jwt = jwtUtils.getJwtFromToken(token);
            String userEmailFromToken = jwtUtils.getUserNameFromJwtToken(jwt);

            if (!userEmailFromToken.equals(userEmail)) {
                throw new RuntimeException("권한이 없습니다");
            }
            User user = userService.findById(userEmail);
            return ResponseEntity.ok(user);
        }catch (Exception e){
            log.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보 가져오기 실패");
        }
    }

    // todo 회원 탈퇴 함수
    @DeleteMapping("/{userEmail}")
    public ResponseEntity<Object> withdraw(@RequestHeader("Authorization") String token, @PathVariable String userEmail) {
        String jwt = jwtUtils.getJwtFromToken(token);
        String userEmailFromToken = jwtUtils.getUserNameFromJwtToken(jwt);

        if (!userEmailFromToken.equals(userEmail)) {
            throw new RuntimeException("권한이 없습니다");
        }
        userService.deleteUser(userEmailFromToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
