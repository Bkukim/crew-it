<template>
  <div class="notification-container">
    <div class="notification-title">알림</div>
    <div class="btn-group mb-4" role="group" aria-label="Notification filter">
      <button
        type="button"
        class="btn"
        :class="currentTab === 'all' ? 'btn-custom' : 'btn-light'"
        @click="setTab('all')"
      >
        전체
      </button>
      <button
        type="button"
        class="btn"
        :class="currentTab == 'unread' ? 'btn-custom' : 'btn-light'"
        @click="setTab('unread')"
      >
        읽지 않음
      </button>
    </div>

    <!-- 알림이 없을 때 -->
    <div class="content-space">
      <div v-if="notifications.length == 0 && currentTab == 'all'" class="no-notification-container">
        <div class="no-notification-text">새로운 알림이 없습니다.</div>
      </div>
      <div v-else-if="notifications.length == 0 && currentTab == 'unread'" class="no-notification-container">
        <div class="no-notification-text">읽지 않은 알림이 없습니다.</div>
      </div>

      <!-- 알림이 있을 때 -->
      <div v-else class="table-container">
        <table class="table table-bordered">
          <thead>
            <tr></tr>
          </thead>
          <tbody>
            <tr v-for="notification in notifications" :key="notification.id">
              <td
                class="position-relative clickable"
                @click="onNotificationClick(notification)"
              >
                {{ notification.content }}
                <span
                  v-if="notification.isRead == 'N'"
                  class="unread-dot"
                ></span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import NotificationService from "@/services/notification/NotificationService";

export default {
  data() {
    return {
      currentTab: "all",
      notifications: [],
    };
  },
  methods: {
    async loadUnreadNotifications() {
      try {
        let response = await NotificationService.getUnreadNotifications(
          this.$store.state.user.userEmail
        );
        this.notifications = response.data;
      } catch (error) {
        console.log("알림을 불러오던 중 오류가 발생하였습니다.", error);
      }
    },
    async loadAllNotifications() {
      try {
        let response = await NotificationService.getAllNotifications(
          this.$store.state.user.userEmail
        );
        this.notifications = response.data;
      } catch (error) {
        console.log("알림을 불러오던 중 오류가 발생하였습니다.", error);
      }
    },
    async markNotificationAsRead(notification) {
      try {
        notification.isRead = "Y";
        await NotificationService.updateAsRead(notification);
      } catch (error) {
        console.log("유효하지 않은 알림", error);
      }
    },
    setTab(tab) {
      this.currentTab = tab;
      if (this.currentTab == "unread") {
        this.loadUnreadNotifications();
      } else {
        this.loadAllNotifications();
      }
    },
    onNotificationClick(notification) {
      this.markNotificationAsRead(notification);

      if (notification.url != null) {
        this.$router.push(notification.url);
      } else {
        alert("해당 알람은 페이지 이동이 없는 알람입니다");
      }
    },
  },
  mounted() {
    if (this.$store.state.user == null) {
      alert("로그인이 필요한 페이지 입니다");
      this.$router.push("/"); 
    } 
    this.loadAllNotifications();
  },
};
</script>

<style scoped>
body {
  background-color: #f8f9fa;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0;
}

.notification-container {
  text-align: center;
  width: 80%; /* 원하는 대로 조정 */
  margin: 0 auto;
}

.notification-title {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 20px;
}

.btn-custom {
  background-color: #28a745;
  color: white;
}

.btn-custom:hover {
  background-color: #218838;
}

.no-notification-container {
  min-height: 500px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.no-notification-text {
  font-size: 1.5rem;
  color: #6c757d;
}

.table-container {
  display: flex;
  justify-content: center;
}

.table {
  margin-top: 20px;
  width: 70%; /* 테이블 너비를 반으로 줄임 */
}

.unread-dot {
  position: absolute;
  top: 10%;
  right: 5%;
  width: 10px;
  height: 10px;
  background-color: red;
  border-radius: 50%;
}
.content-space {
  min-height: 500px;
}
.clickable {
  cursor: pointer;
}
</style>
