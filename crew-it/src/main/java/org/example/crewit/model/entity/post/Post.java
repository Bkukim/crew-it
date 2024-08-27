package org.example.crewit.model.entity.post;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.common.BaseTimeEntity;
import org.example.crewit.model.entity.comment.PostComment;
import org.example.crewit.model.entity.like.PostLike;
import org.example.crewit.model.entity.user.User;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName : org.example.crewit.model.entity.post
 * fileName : Post
 * author : PC
 * date : 2024-08-05
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-05         PC          최초 생성
 */
@Entity
@Table(name = "TB_POST")
@SequenceGenerator(
        name = "SQ_TB_POST_GENERATOR"
        , sequenceName = "SQ_TB_POST"
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
@Slf4j
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            , generator = "SQ_TB_POST_GENERATOR"
    )
    @Column(name = "POST_ID")
    private long postId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_EMAIL", referencedColumnName = "USER_EMAIL")
    private User user;

    @Column(name = "LIKES")
    private long likes;

    @Lob
    private String content;
    private String title;


    public void addLike(){
        this.likes +=1;
    }

    public void removeLike(){
        if (likes <= 0 ) {
            this.likes = 0;
        }else {
            this.likes -=1;
        }
    }

}
