package org.example.crewit.model.dto.post;

import lombok.*;

/**
 * packageName : org.example.crewit.model.dto.post
 * fileName : PostComentDto
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
public class PostCommentReq {
    long postId;
    String writer;
    String content;

}
