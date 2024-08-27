package org.example.crewit.security.services;

import lombok.RequiredArgsConstructor;
import org.example.crewit.model.entity.user.User;
import org.example.crewit.repository.user.UserRepository;
import org.example.crewit.security.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * packageName : org.example.board_login_in_webtoken.security.services
 * fileName : UserDetailServiceImpl
 * author : hayj6
 * date : 2024-05-22(022)
 * description :
 * 요약 : TODO : DB 인증을 담당하는 클래스
 * TODO 로그인할때 ID,PW 받음. DB에 들어가있는상태. 우리 DB에 있는지 확인하는 절차 = DB 인증. 있으면 우리 유저라서 인증된 유저
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-22(022)         hayj6          최초 생성
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        User user
                = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("ID 없음:" + userId));


        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());


        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);

        return new UserDto(user.getUserEmail(),
                user.getPassword(),
                authorities
        );
    }
}
