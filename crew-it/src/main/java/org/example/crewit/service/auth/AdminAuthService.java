package org.example.crewit.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.dto.auth.admin.AdminReq;
import org.example.crewit.model.dto.auth.admin.AdminRes;
import org.example.crewit.model.dto.auth.user.UserRes;
import org.example.crewit.security.jwt.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * packageName : org.example.crewit.service.auth
 * fileName : AdminAuthService
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
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminAuthService {

    private final JwtUtils jwtUtils;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AdminRes adminLogin(AdminReq adminReq) {

        Authentication authentication
                = authenticationManagerBuilder.getObject().authenticate(
                new UsernamePasswordAuthenticationToken(adminReq.getAdminName(), adminReq.getPassword())
        );

        String role = new ArrayList(authentication.getAuthorities()).get(0).toString();
        if (!role.equals("ROLE_ADMIN")) {
            throw new RuntimeException("잘못된 접근입니다");
        }

//  인증 정보를 현재 보안 컨텍스트에 저장 - 이후 요청에서 인증 정보사용
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String jwt = jwtUtils.generateJwtToken(authentication);


        AdminRes adminRes = new AdminRes(
                jwt,                // 웹토큰
                adminReq.getAdminName(), // 이메일
                role);
        return adminRes;
    }
}
