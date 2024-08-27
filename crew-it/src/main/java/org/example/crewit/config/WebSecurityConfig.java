package org.example.crewit.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.example.crewit.repository.user.UserRepository;
import org.example.crewit.security.jwt.AuthTokenFilter;
import org.example.crewit.security.jwt.JwtUtils;
import org.example.crewit.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * packageName : org.example.boardbackend.config
 * fileName : WebSecurityConfig
 * author : hayj6
 * date : 2024-05-21(021)
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-21(021)         hayj6          최초 생성
 */
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(new JwtUtils(), new UserDetailsServiceImpl(userRepository)); //  웹토큰 인증필터 생성자 함수
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/img/**",
                "/css/**",
                "/js/**"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults()); // cors 사용
        http.csrf((csrf) -> csrf.disable());   // csrf 해킹 보안 비활성화(쿠키/세션 사용안함)

        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(req -> req.disable()); // form 태그 action 을 이용한 로그인 사용안함 -> axios 통신함

        http.authorizeHttpRequests(req -> req // todo 여기서 부터 controller의 url을 제한함으로 db와의 접근을 제한한다.
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()

                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/user/**").hasRole("USER")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest()
                .authenticated());


        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
