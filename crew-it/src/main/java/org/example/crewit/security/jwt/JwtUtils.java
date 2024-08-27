package org.example.crewit.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.security.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;


/**
 * packageName : org.example.boardbackend.security.jwt
 * fileName : JwtUtils
 * author : hayj6
 * date : 2024-05-21(021)
 * description :
 * 요약 : TODO 쓸만한 함수들: UTILITY 함수들. JWT는 토큰이다. 토큰에 해당하는 쓸만한 함수들이 있는 클래스. 토큰 생성, 검증함수
 * 토큰을 발급해주려면 토큰을 생성해야함. 그걸 생성하는 함수가 여기있음
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-21(021)         hayj6          최초 생성
 */
@Slf4j
@Component
public class JwtUtils {
    private final String jwtSecret = "bd1889daea6ad98b6e099034b0544974a17bab50c2872e3916c6c7d7762ce80fe9bc0a72ea5d62e65eb7e2396d6dcd1fb90937c7ead79adce142c8383de20776";

    private final int jwtExpirationMs = 6000000;

    // TODO JWT(웹토큰) 생성 함수
    // 사용자 인증 정보를 기반으로 JWT 토큰을 생성
    public String generateJwtToken(Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();

        // 비밀키를 Base64로 디코딩하여 SecretKeySpec 객체 생성
        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);
        Key secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());


        return Jwts.builder()
                // 1) 헤더 생략
                // 2) 주제 = 이메일 넣기
                .setSubject(userDto.getUserId())
                // 3) 토큰발급시간 : 현재시간
                .setIssuedAt(new Date())
                // 4) 만료일자 적용 : 현재시간(new Date()).getTime()) + 10분
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                // 5) 디지털서명 : 암호화 적용(HS512 사용 , 비밀키 넣기)
                .signWith(secretKey, SignatureAlgorithm.HS512) // SecretKeySpec와 알고리즘 사용
                .compact(); // 토큰 생성
    }

    // TODO: JWT(웹토큰) 에서 유저ID(이메일) 꺼내기 함수
    public String getUserNameFromJwtToken(String token) {
        // 비밀키를 Base64로 디코딩하여 SecretKeySpec 객체 생성
        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);
        Key secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        // 웹토큰의 비밀키 + 토큰명을 적용해 body(내용) 안의 subject(유저ID(이메일))에 접근해서 꺼냄
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // TODO: 3) JWT(웹토큰) 이 유효한지 검증하는 함수 : 웹토큰 자체에 대한 유효성. 토큰:헤더 본문 서명(인증)으로 이루어져 있음
    // 이 세 가지가 유효한지 검증. 인증=디지털서명 : 암호화가 해커에 의해 훼손되었을 때
    public boolean validateJwtToken(String authToken) {
        try {

            byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);
            Key secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("디지털서명이 훼손되었습니다.: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("웹토큰 유효하지 않습니다.: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("웹토큰 만료되었습니다. : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("웹토큰을 지원하지 않습니다.: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("웹토큰 내용이 비었습니다. : {}", e.getMessage());
        }

        return false;
    }

    // 토큰에서 실제 JWT 부분만 추출
    public String getJwtFromToken(String token) {
        return token.substring(7);
    }



}
