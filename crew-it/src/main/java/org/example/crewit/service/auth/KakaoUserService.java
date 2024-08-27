package org.example.crewit.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.dto.auth.user.UserRes;
import org.example.crewit.model.entity.user.User;
import org.example.crewit.security.jwt.JwtUtils;
import org.example.crewit.service.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * packageName : org.example.routtoproject.service.member
 * fileName : KakaoUserService
 * author : PC
 * date : 2024-05-24
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-24         PC          최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Qualifier("kakaoLoginService")
public class KakaoUserService implements SocialLoginService {

    // 속성에 입력한 카카오톡
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client-secret}")
    private String clientSecret;


    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    //    todo 카카오 로그인
    @Override
    @Transactional
    public String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate(); // 이걸로 카카오에 요청 전송

        // 카카오에서 지정한 인가 코드를 이용한 액세스 토근 요철 url
        String url = "https://kauth.kakao.com/oauth/token";

        // 요청 바디생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); // 여러 값을 가질 수 있으므로 multi 사용
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", clientSecret);
        params.add("scope", "account_email profile_nickname");

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 생성 : 바디와 헤더를 넣어 요청으로 보냄
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        // 받아온 값에서 토큰을 추출
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("access_token")) {
            throw new RuntimeException("토큰을 받아오지 못했습니다");
        }
        String accessToken = (String) responseBody.get("access_token");
        return accessToken;
    }

    @Override
    @Transactional
    public UserRes getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate(); // 요청에 사용

        // 카카오에서 user 정보 받아올때 지정한 url
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, Map.class);


        Map<String, Object> userInfo = userInfoResponse.getBody();
        if (userInfo == null) {
            throw new RuntimeException("유저 정보를 불러오지 못했습니다");
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> properties = (Map<String, Object>) userInfo.get("properties");

        String userEmail = (String) kakaoAccount.get("email");
        String nickname = (String) properties.get("nickname");


        if (!userService.existsById(userEmail)) { // 존재 하지 않을시 회원가입

            User user = new User(
                    userEmail,
                    passwordEncoder.encode("asdfasdgfsadgqawrg23463457"),
                    nickname,
                    "ROLE_USER"
                    );
            userService.save(user);
            Authentication authentication
                    = authenticationManagerBuilder.getObject().authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, "asdfasdgfsadgqawrg23463457")

            );
            //  인증 정보를 현재 보안 컨텍스트에 저장 - 이후 요청에서 인증 정보사용
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);
            UserRes userRes = new UserRes(
                    jwt,                // 웹토큰
                    userEmail,
                    "ROLE_USER",
                    nickname);
            return userRes;
        } else {
            Authentication authentication
                    = authenticationManagerBuilder.getObject().authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, "asdfasdgfsadgqawrg23463457")
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);
            UserRes userRes = new UserRes(
                    jwt,                // 웹토큰
                    userEmail,
                    "ROLE_USER",
                    nickname);
            return userRes;
        }
    }


}