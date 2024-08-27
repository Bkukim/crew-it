package org.example.crewit.model.dto.auth.user;

import lombok.*;

/**
 * packageName : org.example.crewit.model.dto.auth
 * fileName : UserRes
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
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRes {
    private String accessToken;

    //    토큰 타입
    private String tokenType = "Bearer";
    private String userEmail;
    private String role;
    private String nickname;


    public UserRes(String accessToken, String userEmail, String role, String nickname) {
        this.accessToken = accessToken;
        this.userEmail = userEmail;
        this.role = role;
        this.nickname = nickname;
    }
}
