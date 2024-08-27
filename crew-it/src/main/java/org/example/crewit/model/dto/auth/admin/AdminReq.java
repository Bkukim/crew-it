package org.example.crewit.model.dto.auth.admin;

import lombok.*;

/**
 * packageName : org.example.crewit.model.dto.auth.admin
 * fileName : UserReq
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
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdminReq {
    private String adminName;    // ID
    private String password;  // 암호
}
