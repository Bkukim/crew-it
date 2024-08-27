package org.example.crewit.model.entity.like;

import jakarta.persistence.*;
import lombok.*;
import org.example.crewit.model.entity.post.Post;
import org.example.crewit.model.entity.user.User;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


/**
 * packageName : org.example.crewit.model.entity.post
 * fileName : PostLike
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
@Entity
@Table(name = "TB_POST_LIKE")
@SequenceGenerator(
        name = "SQ_TB_POST_LIKE_GENERATOR"
        , sequenceName = "SQ_TB_POST_LIKE"
        , initialValue = 1
        , allocationSize = 1
)
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TB_POST_LIKE_GENERATOR")
    private long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", referencedColumnName = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_EMAIL", referencedColumnName = "USER_EMAIL")
    private User user;



}

