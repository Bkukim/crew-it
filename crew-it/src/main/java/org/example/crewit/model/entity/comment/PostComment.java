package org.example.crewit.model.entity.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.example.crewit.model.common.BaseTimeEntity;
import org.example.crewit.model.entity.post.Post;
import org.example.crewit.model.entity.user.User;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName : org.example.crewit.model.entity.comment
 * fileName : PostComment
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
@Entity
@Table(name = "TB_POST_COMMENT")
@SequenceGenerator(
        name = "SQ_TB_POST_COMMENT_GENERATOR"
        , sequenceName = "SQ_TB_POST_COMMENT"
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
public class PostComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            , generator = "SQ_TB_POST_COMMENT_GENERATOR"
    )
    private Long commentId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", referencedColumnName = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_EMAIL", referencedColumnName = "USER_EMAIL")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARENT_ID",nullable = true)
    @JsonBackReference
    private PostComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PostComment> childrenComments = new ArrayList<>();

    public void addChildrenComment(PostComment childComment) {
        childrenComments.add(childComment);
        childComment.setParentComment(this);
    }
    private String content;
}
