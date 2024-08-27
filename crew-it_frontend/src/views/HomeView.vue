<template>
  <div class="container my-5">
    <div class="category-selection my-3 d-flex justify-content-between">
      <!-- 카테고리 링크 -->
      <div>
        <a href="#" :class="{ 'category-active': currentCategory === 'recent' }" @click.prevent="setCategory('recent')" class="category-btn">
          최근순
        </a>
        <a href="#" :class="{ 'category-active': currentCategory === 'popular' }" @click.prevent="setCategory('popular')" class="category-btn">
          인기순
        </a>
      </div>
      <!-- 글쓰기 및 팀원 모집 버튼 -->
      <div class="ms-auto">
        <button class="btn write-btn" @click="goToPost">글쓰기</button>
      </div>
    </div>

    <div class="row" style="margin-top: 40px">
      <div class="col-lg-3 col-md-4 col-sm-6 col-12" v-for="(data, index) in posts" :key="index">
        <div class="card" @click="navigateToDetail(data.postId)">
          <div class="card-body">
            <h5 class="card-title">{{ data.title }}</h5>
            <p class="card-text">{{ shortenText(data.content, 100) }}</p>
            <div class="card-footer">
              <span>
                <small class="text-muted">{{ formatDate(data.createTime) }}</small>
                <small class="text-muted"> | by {{ data.user.nickname }}</small>
              </span>
              <span class="">💚 {{ data.likes }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row justify-content-center mt-5">
      <div class="col-auto" style="margin-top: 50px">
        <b-pagination
          class="custom-pagination col-12 mb-3"
          v-model="page"
          :total-rows="count"
          :per-page="pageSize"
          @click="loadPosts"
        ></b-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import PostService from "@/services/post/PostService";
import { formatDistance } from 'date-fns';
import { ko } from 'date-fns/locale';

export default {
  data() {
    return {
      posts: [],
      currentCategory: "recent",
      page: 1,
      count: 0,
      pageSize: 5,
    };
  },
  methods: {
    setCategory(category) {
      this.currentCategory = category;
      if (category == "recent") {
        this.loadLatestPosts();
      } if (category == "popular") {
        this.loadMostLikedPosts();
      }
     },
    navigateToDetail(postId) {
      this.$router.push({ path: '/post-detail/'+ postId});
    },
    goToPost() {
      if (this.$store.state.user == null) {
        alert("로그인이 필요합니다")
      } else {
        this.$router.push("/post");
      }
      
    },
    async loadLatestPosts() {
      try {
        const pageable = { page: this.page - 1, size: this.pageSize };
        let response = await PostService.getLatestPosts(pageable);
        const { posts, totalItems } = response.data;
        this.posts = posts;
        this.count = totalItems;
      } catch (e) {
        console.log(e);
      }
    },
    async loadMostLikedPosts() {
      try {
        console.log("진입")
        const pageable = { page: this.page - 1, size: this.pageSize };
        let response = await PostService.getMostLikedPosts(pageable);
        const { posts, totalItems } = response.data;
        this.posts = posts;
        this.count = totalItems;
      } catch (e) {
        console.log(e);
      }
    },
    formatDate(dateString) {
      const now = new Date();
      const date = new Date(dateString);
      return formatDistance(date, now, { addSuffix: true, locale: ko });
    },
    shortenText(text, length) {
      if (!text) return '';
      text = this.removeHtmlTags(text);
      return text.length > length ? text.substring(0, length) + '...' : text;
    },
    removeHtmlTags(text) {
      const doc = new DOMParser().parseFromString(text, "text/html");
      doc.querySelectorAll('img').forEach(e => e.parentNode.removeChild(e));
      doc.querySelectorAll('script').forEach(e => e.parentNode.removeChild(e));
      return doc.body.textContent.trim();
    }
  },
  mounted() {
    this.loadLatestPosts();
  },
};
</script>

<style>
.card {
  margin-bottom: 20px;
  transition: transform 0.3s;
  border-radius: 10px;
  border: 2px solid #ddd;
  min-height: 300px;
  display: flex;
  flex-direction: column;
}
.card:hover {
  transform: scale(1.05);
  cursor: pointer;
}
.card-body {
  padding: 15px;
  flex: 1; /* 본문은 가능한 공간을 채움 */
  display: flex;
  flex-direction: column;
}
.card-title {
  font-weight: bold; /* 제목을 굵은 글씨로 설정 */
  height: 40px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.card-text {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 4; /* 최대 4줄까지 보여줌 */
  -webkit-box-orient: vertical;
}
.card-footer {
  height: 40px;
  padding: 5px 15px;
  border-top: 1px solid #ddd; /* 경계선 위치 조정 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  text-align: center;
  margin-top: 10px;
}
.category-selection {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px !important;
}
.category-btn {
  padding: 8px 15px;
  margin: 10px 0px;
  cursor: pointer;
  color: #aaa;
  text-decoration: none;
  font-size: 1.5rem;
  font-weight: bold;
}
.category-active {
  color: black;
}
.write-btn,
.recruit-btn {
  background-color: #007bff;
  color: white;
  margin-left: 10px;
  font-size: 1.2rem;
  padding: 8px 15px;
}
.recruit-btn {
  background-color: #28a745;
}

@media (max-width: 992px) {
  .card {
    min-height: 220px;
  }
  .card-footer {
    flex-direction: column; /* 작은 화면에서는 두 줄로 배치 */
    height: auto; /* 높이 자동 조절 */
  }
  .category-btn {
    font-size: 1.3rem;
    padding: 7px 13px;
  }
  .write-btn,
  .recruit-btn {
    font-size: 1.1rem;
    padding: 7px 13px;
  }
}

@media (max-width: 768px) {
  .card {
    min-height: 180px;
  }
  .card-footer {
    flex-direction: column;
    height: auto;
    padding: 5px;
  }
  .category-btn {
    font-size: 1.1rem;
    padding: 6px 11px;
  }
  .write-btn,
  .recruit-btn {
    font-size: 1rem;
    padding: 6px 11px;
  }
}

@media (max-width: 576px) {
  .card {
    min-height: 150px;
  }
  .card-footer {
    flex-direction: column;
    height: auto;
    padding: 5px;
  }
  .category-btn {
    font-size: 0.9rem;
    padding: 5px 10px;
  }
  .write-btn,
  .recruit-btn {
    font-size: 0.9rem;
    padding: 5px 10px;
  }
}
</style>
