package org.example.crewit.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * packageName : org.example.boardbackend.security.dto
 * fileName : UserDto
 * author : hayj6
 * date : 2024-05-21(021)
 * description : TODO: 검증된 유저 클래스 정의
 * 요약 :
 *      TODO
 *          1) 스프링에서 제공한 유저 클래스 : User, UserDetails 2중에 1개 상속
 *          2) 검증된 유저 객체에는 권한이 있음 : 권한 넣기
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-21(021)         hayj6          최초 생성
 */
@Setter
@Getter
@ToString
public class UserDto extends User {

    private String userId;


    public UserDto(String userId, String password, Collection<? extends GrantedAuthority> authorities) {
        super(userId, password, authorities);
        this.userId = userId;
    }
}