package org.example.crewit.model.dto.auth.admin;

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
public class AdminRes {
    private String accessToken;
    private String userEmail;

    public AdminRes(String accessToken, String userEmail, String role) {
        this.accessToken = accessToken;
        this.userEmail = userEmail;
        this.role = role;
    }

    private String tokenType = "Bearer";
    private String role;
}
