package org.example.crewit.controller.user.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.crewit.model.entity.notification.Notification;
import org.example.crewit.service.notification.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName : org.example.crewit.controller.user.notification
 * fileName : UserNotificationController
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
@RestController
@RequestMapping("/api/user/notifications")
@RequiredArgsConstructor
public class UserNotificationController {

    private final NotificationService notificationService;

    // todo 읽지 않은 알림 조회
    @GetMapping("/unread")
    public ResponseEntity<Object> getUnreadNotifications(@RequestParam String userEmail) {
            List<Notification> unreadNotifications = notificationService.getUnreadNotifications(userEmail);
            if (unreadNotifications.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            } else {
                return ResponseEntity.ok(unreadNotifications);
            }
    }

    // todo 모든 알림 조회
    @GetMapping
    public ResponseEntity<Object> getAllNotifications(@RequestParam String userEmail) {
        List<Notification> notifications = notificationService.getAllNotifications(userEmail);

        if (notifications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        }

    }

    // todo 알림 읽음으로 표시
    @PutMapping("/{notificationId}")
    public ResponseEntity<Object> updateAsRead(@RequestHeader("Authorization") String token, @PathVariable Long notificationId, @RequestBody Notification notification){
        try {
            notificationService.markAsRead(token,notification);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.debug("에러" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // todo 읽지 않은 알림 갯수 표시
    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadNotificationCount(@RequestParam String userEmail) {
        long count = notificationService.getUnreadNotificationCount(userEmail);
        return ResponseEntity.ok(count);
    }

}
