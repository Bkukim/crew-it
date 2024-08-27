package org.example.crewit.repository.notification;

import org.example.crewit.model.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName : org.example.crewit.repository.notification
 * fileName : NotificationRepository
 * author : PC
 * date : 2024-08-15
 * description :
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-15         PC          최초 생성
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query("SELECT n FROM Notification n WHERE n.user.userEmail = :userEmail AND n.isRead = 'N' ORDER BY n.createTime DESC ")
    List<Notification> findByUserUserEmailAndIsRead(@Param("userEmail") String userEmail);

    List<Notification> findByUserUserEmailOrderByCreateTimeDesc(String userEmail);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.userEmail = :userEmail AND n.isRead = 'N'")
    long countUnreadNotificationsByUserEmail(@Param("userEmail") String userEmail);
}
