space<template>
  <!-- 자유게시판 -->
  <div class="row justify-content-center">
    <div class="col-12">
      <h1 class="board-title align-items-center">나의 게시물</h1>

      <div class="content-space">
        <div
          v-if="posts.length == 0 "
          class="no-post-container"
        >
          <div class="no-post-text">작성한 게시물이 없습니다.</div>
        </div>
        <div v-else>
          <table class="table mt-5 text-center table-custom">
            <thead class="thead-dark">
              <tr>
                <td class="text-center" scope="col">제목</td>
                <td class="text-center" scope="col">등록일</td>
                <td scope="col">수정</td>
                <td scope="col">삭제</td>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(data, index) in posts" :key="index">
                <td class="col-4 title-column">
                  <router-link
                    :to="'/post-detail/' + data.postId"
                    class="alltext router-link-exact-active custom-pagination link-style"
                    >{{ data.title }}</router-link
                  >
                </td>
                <td class="col-4 date-column">
                  {{ formatDate(data.createTime) }}
                </td>
                <td>
                  <button
                    type="button"
                    class="btn btn-success btn-custom"
                    @click="goUpdatePost(data.postId)"
                  >
                    수정
                  </button>
                </td>
                <td>
                  <button
                    type="button"
                    class="btn btn-danger btn-custom"
                    @click="deletePost(data.postId)"
                  >
                    삭제
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="row justify-content-center mt-4">
            <div class="col-auto">
              <b-pagination
                class="col-12 mb-3 custom-pagination"
                v-model="page"
                :total-rows="count"
                :per-page="pageSize"
              ></b-pagination>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PostService from "@/services/post/PostService";
import { formatDistance } from "date-fns";
import { ko } from "date-fns/locale";
export default {
  data() {
    return {
      user: {},
      posts: {},
      page: 1,
      count: 0,
      pageSize: 10,
    };
  },
  methods: {
    async loadUsersPosts() {
      try {
        const pageable = { page: this.page - 1, size: this.pageSize };
        let response = await PostService.getUsersPosts(
          pageable,
          this.$store.state.user.userEmail
        );
        const { posts, totalItems } = response.data;
        this.posts = posts;
        this.count = totalItems;
        console.log("post", this.posts);
      } catch (error) {
        console.error("게시물 로드 중 오류 발생", error);
      }
    },
    formatDate(dateString) {
      const now = new Date();
      const date = new Date(dateString);
      return formatDistance(date, now, { addSuffix: true, locale: ko });
    },
    async deletePost(postId) {
      try {
        let result = confirm("정말로 삭제 하시겠습니까?");
        if (result) {
          await PostService.deletePost(postId);

          alert("게시물이 성공적으로 삭제되었습니다.");
          this.loadUsersPosts();
        }
      } catch (error) {
        console.error("Error:", error);
        alert("서버와의 통신 중 오류가 발생했습니다.");
      }
    },
    goUpdatePost(postId) {
      this.$router.push({ path: "/post", query: { id: postId } });
    },
  },
  mounted() {
    if (this.$store.state.user == null) {
      alert("로그인이 필요한 페이지 입니다");
      this.$router.push("/");
    }
    this.loadUsersPosts();
  },
};
</script>

<style scoped>
.board-title {
  text-align: center; /* 가운데 정렬 */
  font-size: 2vw;
  letter-spacing: -1.2px;
  background-color: white;
  padding-bottom: 20px;
}

.title-column {
  text-align: center;
  font-weight: bold;
}

.date-column {
  text-align: center;
  color: #333;
}

.link-style {
  text-decoration: none;
  color: #333;
}
.content-space {
  min-height: 500px;
}
.no-post-text {
  font-size: 1.5rem;
  color: #6c757d;
}
.no-post-container {
  min-height: 500px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>
