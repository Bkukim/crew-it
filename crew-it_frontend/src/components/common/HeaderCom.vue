<template>
  <div>
    <nav class="navbar navbar-expand navbar-light">
      <div class="container-fluid">
        <!-- 홈 로고 -->
        <router-link class="navbar-brand" to="/">
          <img src="@/assets/img/logo2.png" alt="Logo" class="navbar-logo" />
        </router-link>

        <div class="navbar-content">
          <!-- 서치바 -->
          <div class="search-bar">
            <router-link :to="{ path: '/search' }" class="search-icon-link">
              <img
                src="@/assets/img/search_icon.png"
                alt="Search Icon"
                class="search-icon"
              />
            </router-link>
          </div>

          <!-- 로그인 아웃 상태일 시 -->
          <div v-if="!this.$store.state.loggedIn">
            <a href="#" class="login-text" @click.prevent="showLoginModal"
              >로그인</a
            >
          </div>

          <!-- 로그인 상태일 시 -->
          <div v-else>
            <!-- Group Notification and My Page icons together -->
            <div class="nav-icons">
              <!-- 알림 드롭다운 -->
              <div class="nav-item dropdown" ref="notificationDropdown">
                <router-link class="nav-link" to="/notifications">
                  <img
                    src="@/assets/img/Notification_icon.png"
                    alt="Notification Icon"
                    class="notification-icon"
                  />
                  <span
                    v-if="notificationCount > 0"
                    class="badge bg-danger notification-badge"
                    >{{ notificationCount }}</span
                  >
                </router-link>
              </div>

              <!-- 마이 페이지 드롭다운 -->
              <div class="nav-item dropdown" ref="mypageDropdown">
                <a
                  class="nav-link"
                  href="#"
                  @click.prevent="toggleMypageDropdown"
                >
                  <img
                    src="@/assets/img/mypage_icon.png"
                    alt="Mypage Icon"
                    class="icon"
                  />
                </a>
                <ul v-if="isMypageDropdownVisible" class="dropdown-menu">
                  <li>
                    <router-link
                      class="dropdown-item"
                      to="/user-info"
                      @click="closeDropdown"
                      >회원 정보 수정</router-link
                    >
                  </li>
                  <li>
                    <router-link
                      class="dropdown-item"
                      to="/my-post"
                      @click="closeDropdown"
                      >내가 쓴글</router-link
                    >
                  </li>
                  <li>
                    <a
                      class="dropdown-item"
                      href="#"
                      @click.prevent="handleLogout"
                      >로그아웃</a
                    >
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </nav>

    <!-- 소셜 로그인 모달 -->
    <div
      class="modal"
      tabindex="-1"
      role="dialog"
      v-if="isLoginModalVisible"
      @click.self="hideLoginModal"
    >
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">소셜 로그인</h5>
            <button
              type="button"
              class="btn-close"
              @click="hideLoginModal"
            ></button>
          </div>
          <div class="modal-body">
            <p>소셜 계정으로 로그인하세요:</p>
            <div class="d-grid gap-3">
              <button class="btn btn-warning btn-lg" @click="loginWithKakao">
                Kakao 로그인
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AuthService from "@/services/auth/AuthService";
import NotificationService from "@/services/notification/NotificationService";

export default {
  data() {
    return {
      isLoginModalVisible: false,
      notificationCount: 0,
      isMypageDropdownVisible: false,
      isNotificationDropdownVisible: false,
      intervalId: null, 
    };
  },
  methods: {
    showLoginModal() {
      this.isLoginModalVisible = true;
    },
    hideLoginModal() {
      this.isLoginModalVisible = false;
    },
    closeDropdown() {
      this.isMypageDropdownVisible = false;
    },
    toggleMypageDropdown() {
      this.isMypageDropdownVisible = !this.isMypageDropdownVisible;
      this.isNotificationDropdownVisible = false; 
      console.log("My Page dropdown visibility:", this.isMypageDropdownVisible);
    },
    toggleNotificationDropdown() {
      this.isNotificationDropdownVisible = !this.isNotificationDropdownVisible;
      this.isMypageDropdownVisible = false;
      console.log(
        "Notification dropdown visibility:",
        this.isNotificationDropdownVisible
      );
    },
    loginWithKakao() {
      const client_id = "cbc38029207b4a67d7a74e10d84cdb00"; // Rest API 키
      const redirect_uri =
        "http://" + this.$store.state.frontendIp + "/auth-redirect"; // Redirect URI
      const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${client_id}&redirect_uri=${redirect_uri}&response_type=code`; // response_type=code는 고정
      window.location.href = kakaoAuthUrl;
    },
    async getUnreadNotifyCount() {
      try {
        const response = await NotificationService.getUnreadNotificationCount(
          this.$store.state.user.userEmail
        );
        console.log("알림 갯수 ",response.data);
        this.notificationCount = response.data;
      } catch (error) {
        console.error("알림 갯수를 가져오는데 실패했습니다", error);
      }
    },

    handleClickOutside(event) {
      // mypageDropdown이 존재하고, 해당 요소 밖에서 클릭이 발생했는지 확인
      if (
        this.isMypageDropdownVisible &&
        this.$refs.mypageDropdown &&
        !this.$refs.mypageDropdown.contains(event.target)
      ) {
        this.isMypageDropdownVisible = false;
      }

      // notificationDropdown이 존재하고, 해당 요소 밖에서 클릭이 발생했는지 확인
      if (
        this.isNotificationDropdownVisible &&
        this.$refs.notificationDropdown &&
        !this.$refs.notificationDropdown.contains(event.target)
      ) {
        this.isNotificationDropdownVisible = false;
      }
    },
    handleLogout() {
      let result = confirm("정말로 로그아웃 하시겠습니까?");
      if (result) {
        AuthService.logout(); 
        this.$store.commit("logout"); 
        this.$router.push("/");
      } else {
        return;
      }
    },
    goToSearch() {
      this.$router.push({
        path: "/search",
        query: { p: this.searchTitle },
      });
    },
  },
  mounted() {
    document.addEventListener("click", this.handleClickOutside);
    this.getUnreadNotifyCount(); // 처음 로드 시 한 번 호출
    this.intervalId = setInterval(this.getUnreadNotifyCount, 60000); // 60초마다 호출
  },
  beforeUnmount() {
    document.removeEventListener("click", this.handleClickOutside);
    // 컴포넌트가 파괴되기 전에 setInterval을 정리하여 메모리 누수를 방지
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  },
};
</script>

<style>
.navbar {
  margin: 20px 200px 0 200px;
  padding: 0.5rem 1rem;
  background-color: transparent;
}

.navbar-logo {
  width: 100px;
  height: 66px;
  margin-left: 20px;
}

.navbar-content {
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: flex-end;
  gap: 1rem;
}

.nav-icons {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-item {
  display: flex;
  align-items: center;
  position: relative; /* Ensure dropdowns align properly */
}

.search-form {
  display: flex;
  align-items: center;
  flex-grow: 1;
  justify-content: flex-end;
  margin-right: 1rem;
}

.search-icon {
  cursor: pointer;
  width: 30px; /* 같은 크기 */
  height: 30px; /* 같은 크기 */
  margin-left: 0.5rem;
}

.search-button {
  margin-left: 0.5rem;
}

.form-control-sm {
  max-width: 25%;
}

.icon {
  width: 40px;
  height: 40px;
}

.notification-icon {
  width: 30px;
  height: 30px;
}

.login-text {
  color: black;
  font-weight: bold;
  font-size: 20px;
  margin-right: 1rem;
  margin-left: 1rem;
  white-space: nowrap;
  text-decoration: none;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-dialog {
  max-width: 500px;
  width: 100%;
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.btn-close {
  border: none;
  background: none;
  font-size: 20px;
}

.d-grid {
  display: grid;
  gap: 1rem;
}

.btn-lg {
  padding: 0.75rem 1.25rem;
  font-size: 1.25rem;
}

.nav-item .dropdown-toggle::after {
  display: none;
}

@media (max-width: 1200px) {
  .navbar {
    margin: 20px 50px 0 50px;
  }
  .login-text {
    font-size: 18px;
  }
}

@media (max-width: 992px) {
  .navbar {
    margin: 20px 10px 0 10px;
  }
  .form-control-sm {
    max-width: 55%;
  }
  .login-text {
    font-size: 16px;
  }
}

@media (max-width: 768px) {
  .navbar {
    margin: 20px 5px 0 5px;
  }
  .form-control-sm {
    max-width: 70%;
  }
  .login-text {
    font-size: 10px;
    margin-right: 0.2rem;
    margin-left: 0.2rem;
  }
}

.notification-dropdown .dropdown-menu {
  width: 300px;
  max-height: 400px;
  overflow-y: auto;
}

.notification-dropdown .dropdown-toggle {
  color: #444;
  text-decoration: none;
}

.notification-badge {
  position: absolute;
  top: -5px;
  right: -10px;
}

.dropdown-menu {
  position: absolute;
  z-index: 1050;
  display: block;
  background-color: white;
  border: 1px solid #ccc;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
  min-width: 10rem;
  padding: 0.5rem 0;
  margin-top: 0.5rem; /* Ensure dropdown is below icon */
  top: 100%; /* Positions the dropdown below the icon */
  left: 0;
}
.notification-badge {
  position: absolute;
  top: -1px;
  right: -8px;
  font-size: 12px;
  padding: 2px 5px;
  border-radius: 50%;
  background-color: red;
  color: white;
}
</style>
