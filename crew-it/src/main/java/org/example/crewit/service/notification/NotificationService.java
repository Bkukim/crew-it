package org.example.crewit.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.entity.notification.Notification;
import org.example.crewit.model.entity.user.User;
import org.example.crewit.repository.notification.NotificationRepository;
import org.example.crewit.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName : org.example.crewit.service.notification
 * fileName : NotificationService
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
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    // todo 알림 저장
    @Transactional
    public void saveNotification (Notification notification){
        notificationRepository.save(notification);
    }

    // todo 읽지 않은 알림 조회
    public List<Notification> getUnreadNotifications(String userEmail) {
        return notificationRepository.findByUserUserEmailAndIsRead(userEmail);
    }

    // todo 전체 알림 조회
    public List<Notification> getAllNotifications(String userEmail) {
        return notificationRepository.findByUserUserEmailOrderByCreateTimeDesc(userEmail);
    }

    // todo 읽지 않은 알림 갯수
    public long getUnreadNotificationCount(String userEmail) {
        return notificationRepository.countUnreadNotificationsByUserEmail(userEmail);
    }

    // todo 알림 읽음 표시
    @Transactional
    public void markAsRead(String token,Notification notification) {
        if (notification == null) {
            throw new RuntimeException("알림이 없습니다.");
        }
        if (notification.getIsRead().equals("N")) {
            notification.setIsRead("Y");
        }
        User user = userService.getUserFromToken(token);
        notification.setUser(user);
        saveNotification(notification);
    }
}
