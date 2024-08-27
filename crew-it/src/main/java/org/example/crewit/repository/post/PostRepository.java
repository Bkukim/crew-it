package org.example.crewit.repository.post;

import org.example.crewit.model.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * packageName : org.example.crewit.repository.post
 * fileName : PostRepository
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
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.title LIKE CONCAT('%', :title, '%') ORDER BY p.createTime desc ")
    Page<Post> findPostsOrderByCreateTime(Pageable pageable, @Param("title") String title);


    @Query("SELECT p FROM Post p WHERE p.title LIKE CONCAT('%', :title, '%') ORDER BY p.likes desc , p.createTime desc ")
    Page<Post> findPostsOrderByLikes(Pageable pageable, @Param("title") String title);

    @Query("SELECT p FROM Post p WHERE p.user.userEmail = :userEmail ORDER BY p.createTime desc ")
    Page<Post> findPostsByUserEmailOrderByCreateTime(Pageable pageable, @Param("userEmail") String userEmail);



}
