package org.example.crewit.repository.like;

import org.example.crewit.model.entity.like.PostLike;
import org.example.crewit.model.entity.post.Post;
import org.example.crewit.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName : org.example.crewit.repository.like
 * fileName : PostLikeRepository
 * author : PC
 * date : 2024-08-09
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-09         PC          최초 생성
 */
@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {


    @Query("SELECT pl FROM PostLike pl WHERE pl.user.userEmail = :userEmail AND pl.post.postId = :postId")
    Optional<PostLike> findByUserEmailAndPostId( @Param("postId") Long postId,@Param("userEmail") String userEmail);

    boolean existsByUserAndPost(User user, Post post);


}
