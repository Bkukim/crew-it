package org.example.crewit.model.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.example.crewit.model.common.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * packageName : org.example.boardbackend.model.entity.auth
 * fileName : User
 * author : hayj6
 * date : 2024-05-21(021)
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-21(021)         hayj6          최초 생성
 */
@Entity
@Table(name="TB_USER")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "USER_EMAIL",unique=true)
    private String userEmail;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 20)
    private String nickname;

    @Column(length = 20, nullable = false)
    private String role;

}
