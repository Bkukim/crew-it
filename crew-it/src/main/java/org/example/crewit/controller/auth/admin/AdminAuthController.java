package org.example.crewit.controller.auth.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.dto.auth.admin.AdminReq;
import org.example.crewit.model.dto.auth.admin.AdminRes;
import org.example.crewit.model.entity.user.User;
import org.example.crewit.service.auth.AdminAuthService;
import org.example.crewit.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * packageName : org.example.crewit.controller.admin
 * fileName : AdminAutnController
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
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
public class AdminAuthController {
    private final AdminAuthService adminAuthService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    //    todo 관리자 로그인 함수
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AdminReq adminReq) {
        try {
            log.debug(adminReq.toString());
            AdminRes loginedAdminRes = adminAuthService.adminLogin(adminReq);
            return ResponseEntity.ok(loginedAdminRes);

        } catch (Exception e) {
            log.debug("관리자 로그인 에러 ::" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보가 틀립니다.");
        }
    }


    @PostMapping("/register/{userEmail}/{password}")
    public ResponseEntity<Object> createUser(@PathVariable String userEmail,@PathVariable String password
    ) {
        try {
        User user = new User(
                userEmail,
                passwordEncoder.encode(password),
                "관리자",
                "ROLE_ADMIN");
            log.debug(String.valueOf(user));
            userService.save(user);

            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (Exception e) {
            log.debug("에러 : " + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
