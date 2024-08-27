package org.example.crewit.repository.comment;

import org.example.crewit.model.entity.comment.PostComment;
import org.example.crewit.model.entity.post.Post;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName : org.example.crewit.repository.comment
 * fileName : CommentRepository
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
@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    @Query("SELECT pc FROM PostComment pc LEFT JOIN FETCH pc.childrenComments WHERE pc.post.postId = :postId AND pc.parentComment IS NULL ORDER BY pc.createTime ASC ")
    Page<PostComment> findPostCommentsOrderByCreateTime(Pageable pageable, @Param("postId") long postId);

    @Query("SELECT COUNT(pc) FROM PostComment pc WHERE pc.post.postId = :postId AND pc.parentComment IS NULL")
    long countPostComments(@Param("postId") long postId);

}
