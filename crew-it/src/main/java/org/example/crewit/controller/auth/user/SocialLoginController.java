package org.example.crewit.controller.auth.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.dto.auth.user.KakaoReq;
import org.example.crewit.model.dto.auth.user.UserRes;
import org.example.crewit.service.auth.SocialLoginService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * packageName : org.example.crewit.controller.auth
 * fileName : SocialLoginController
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
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SocialLoginController {
    @Qualifier("kakaoLoginService")
    private final SocialLoginService kakaoLoginService;

    // todo 카카오 로그인 함수
    @PostMapping("/kakao-login")
    public ResponseEntity<Object> kakaoLogin(@RequestBody KakaoReq kakaoReq){
        try {
            String accessToken = kakaoLoginService.getAccessToken(kakaoReq.getCode()   );
            UserRes userRes = kakaoLoginService.getUserInfo(accessToken);
            return new ResponseEntity<>(userRes, HttpStatus.OK);
        }catch (Exception e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
