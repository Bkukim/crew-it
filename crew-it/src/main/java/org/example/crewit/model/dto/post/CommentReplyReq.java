package org.example.crewit.model.dto.post;

import lombok.*;
import org.example.crewit.model.entity.comment.PostComment;
import org.example.crewit.model.entity.post.Post;

/**
 * packageName : org.example.crewit.model.dto.post
 * fileName : CommentReplyReq
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
public class CommentReplyReq {
    private long postId;
    private long parentId;
    private String content;
}
