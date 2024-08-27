package org.example.crewit.model.dto.post;

import lombok.*;

/**
 * packageName : org.example.crewit.model.dto
 * fileName : PostReq
 * author : PC
 * date : 2024-08-06
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-06         PC          최초 생성
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostReq {
    private String userEmail;
    private String title;
    private String content;
}
