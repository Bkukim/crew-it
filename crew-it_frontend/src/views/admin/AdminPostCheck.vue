<template>
  <div class="container mt-5">
    <!-- 상단 네비게이션 바 -->
    <div class="d-flex justify-content-between align-items-center mb-4">
      <!-- 검색 폼 -->
      <form class="search-form d-flex" @submit.prevent="goToSearch">
        <input
          class="form-control me-2"
          type="search"
          placeholder="제목을 입력하세요"
          aria-label="Search"
          v-model="searchTitle"
        />
        <button class="btn btn-outline-success" type="submit">
          검색
        </button>
      </form>

      <!-- 로그아웃 버튼 -->
      <button class="btn btn-outline-danger" @click="handleLogout">
        로그아웃
      </button>
    </div>

    <!-- 게시물 테이블 -->
     <div class="posts">
    <table class="table table-striped table-hover text-center">
      <thead class="thead-dark">
        <tr>
          <th scope="col">제목</th>
          <th scope="col">등록일</th>
          <th scope="col">삭제</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(data, index) in posts" :key="index">
          <td>
            <router-link :to="'/post-detail/' + data.postId" class="text-decoration-none text-dark">
              {{ data.title }}
            </router-link>
          </td>
          <td>{{ formattedDateTime(data.createTime) }}</td>
          <td>
            <button class="btn btn-danger" @click="deletePost(data.postId)">
              삭제
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

    <!-- 페이지네이션 -->
    <div class="d-flex justify-content-center mt-4">
      <b-pagination
        v-model="page"
        :total-rows="count"
        :per-page="pageSize"
        aria-label="Page navigation"
      ></b-pagination>
    </div>
  </div>
</template>

<script>
import AuthService from "@/services/auth/AuthService";
import PostService from "@/services/post/PostService";

export default {
  data() {
    return {
      posts: [],
      page: 1,
      count: 0,
      pageSize: 10,
      searchTitle: "",
    };
  },
  methods: {
    async loadLatestPosts() {
      try {
        const pageable = { page: this.page - 1, size: this.pageSize };
        let response = await PostService.getLatestPosts(pageable, this.searchTitle);
        const { posts, totalItems } = response.data;
        this.posts = posts;
        this.count = totalItems;
      } catch (error) {
        console.log("게시물 로드 중 오류 발생", error);
      }
    },
    async deletePost(postId) {
      try {
        let result = confirm("정말로 삭제 하시겠습니까?");
        if (result) {
          await PostService.deletePostAsAdmin(postId);
          alert("게시물이 성공적으로 삭제되었습니다.");
          this.loadLatestPosts(); // 게시물 삭제 후 목록 새로고침
        }
      } catch (error) {
        console.log("게시물 삭제 중 오류 발생", error);
        alert("게시물 삭제에 실패하였습니다.");
      }
    },
    formattedDateTime(dateString) {
      const date = new Date(dateString);
      return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
    },
    goToSearch() {
      this.loadLatestPosts();
    },
    handleLogout() {
      let result = confirm("정말로 로그아웃 하시겠습니까?");
      if (result) {
        AuthService.logout(); 
        this.$store.commit("logout"); 
        this.$router.push("/admin-login");
      } else {
        return;
      }
    },
  },
  mounted() {
     // 새로고침이 이미 수행된 상태인지 확인
     if (!localStorage.getItem('reloaded')) {
      localStorage.setItem('reloaded', 'true');
      window.location.reload();
    } else {
      localStorage.removeItem('reloaded');
    }
    if (this.$store.state.user == null || this.$store.state.user.role != "ROLE_ADMIN" ) {
      alert("로그인이 필요한 페이지 입니다");
      this.$router.push("/admin-login"); 
    } 
    this.loadLatestPosts();
  },
};
</script>

<style scoped>
.posts{
  min-height: 30vw;
}
.table {
  margin-top: 20px;
}

.search-form {
  max-width: 500px;
  width: 100%;
}

.btn-danger {
  padding: 0.375rem 0.75rem;
  font-size: 0.875rem;
}

.btn-outline-danger {
  padding: 0.375rem 0.75rem;
  font-size: 0.875rem;
}

.text-dark {
  color: #333 !important;
}

.text-decoration-none {
  text-decoration: none !important;
}
</style>
