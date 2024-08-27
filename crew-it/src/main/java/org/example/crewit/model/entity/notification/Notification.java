package org.example.crewit.model.entity.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.example.crewit.model.common.BaseTimeEntity;
import org.example.crewit.model.entity.user.User;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * packageName : org.example.boardbackend.model.entity.notify
 * fileName : Notify
 * author : PC
 * date : 2024-05-29
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-29         PC          최초 생성
 */
@Entity
@Table(name = "TB_NOTIFICATION")
@SequenceGenerator(
        name = "SQ_NOTIFICATION_GENERATOR"
        , sequenceName = "SQ_NOTIFICATION"
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
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            , generator = "SQ_NOTIFICATION_GENERATOR"
    )
    private long notificationId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_EMAIL", referencedColumnName = "USER_EMAIL")
    private User user;


    private String url;

    private String content;

    private String isRead;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    public enum NotificationType {
        COMMENT, LIKE, DELETE
    }
}

