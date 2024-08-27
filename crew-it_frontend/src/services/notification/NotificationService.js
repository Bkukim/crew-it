import http from "@/utils/http-common"; // spring 통신 정의 파일
import AuthHeader from "@/services/auth/AuthHeader";

class NotificationService {

  // TODO: 읽지 않은 알림 조회
  getUnreadNotifications(userEmail) {
    return http.get(`/user/notifications/unread`, {
      params: {
        userEmail: userEmail
      },
      headers: AuthHeader(),
    });
  }
  // TODO: 알림 전체 조회
  getAllNotifications(userEmail) {
    return http.get(`/user/notifications`, {
      params: {
        userEmail: userEmail
      },
      headers: AuthHeader(),
    });
  }
  // TODO: 알림 읽음 표시
  updateAsRead(notification) {
    return http.put(`/user/notifications/${notification.notificationId}`,notification, {
      
      headers: AuthHeader(),
    });
  }
  
  // TODO: 읽지 않은 알림 갯수 표시
  getUnreadNotificationCount(userEmail) {
    return http.get(`/user/notifications/unread/count`, {
      params: {
        userEmail: userEmail
      },
      headers: AuthHeader(),
    });
  }
}

export default new NotificationService();
