<template>
  <div class="post-detail">
    <div class="post-header">
      <h1>{{ post?.title }}</h1>
      <div class="post-meta">
        <span
          class="edit-delete"
          v-if="
            this.$store.state.user != nul &&
            this.$store.state.user.userEmail == post?.user.userEmail
          "
        >
          <router-link :to="'/post?id=' + post?.postId" class="edit"
            >수정</router-link
          >
          <div @click="deletePost()" class="delete">삭제</div>
        </span>
        <span class="author-date">
          <span>{{ post?.user ? post.user.nickname : "알 수 없음" }}</span>
          <span class="meta-separator">|</span>
          <span>{{ formatDate(post?.createTime) }}</span>
        </span>
      </div>
    </div>
    <div class="post-body">
      <p v-html="post?.content"></p>
    </div>
    <div class="comments-section">
      <h3>{{ comments.length }}개의 댓글</h3>
      <div class="new-comment">
        <textarea
          v-model="newCommentText"
          placeholder="댓글을 작성하세요"
        ></textarea>
        <button @click="addComment">댓글 작성</button>
      </div>
      <ul class="comment-list">
        <li class="comment" v-for="comment in comments" :key="comment.id">
          <div class="comment-header">
            <span class="comment-author">{{ comment.user.nickname }}</span>
            <span class="comment-separator">|</span>
            <span class="comment-date">{{
              formatDate(comment.createTime)
            }}</span>
          </div>
          <div class="comment-body">{{ comment.content }}</div>
          <div class="comment-controls">
            <button @click="toggleReply(comment)">답글</button>
            <div v-if="comment.replying">
              <textarea
                v-model="comment.newReplyText"
                placeholder="답글을 작성하세요"
              ></textarea>
              <button @click="addReply(comment)">답글 작성</button>
            </div>
          </div>
          <div class="reply-toggle" v-if="comment.childrenComments.length > 0">
            <button @click="comment.showReplies = !comment.showReplies">
              {{
                comment.showReplies
                  ? "답글 숨기기"
                  : "답글 보기 (" + comment.childrenComments.length + ")"
              }}
            </button>
          </div>
          <ul class="replies" v-if="comment.showReplies">
            <li v-for="reply in comment.childrenComments" :key="reply.id">
              <span>{{ reply.user.nickname }}</span>
              <span>{{ formatDate(reply.createTime) }}</span>
              <p>{{ reply.content }}</p>
            </li>
          </ul>
        </li>
      </ul>
    </div>
    <div class="row justify-content-center mt-5">
      <div class="col-auto" style="margin-top: 50px">
        <b-pagination
          class="custom-pagination col-12 mb-3"
          v-model="page"
          :total-rows="count"
          :per-page="pageSize"
          @click="loadPostComments()"
        ></b-pagination>
      </div>
    </div>
    <!-- 좋아요 버튼 -->
    <div class="like-container">
      <button @click="toggleLike" class="like-button">
        {{ liked ? "💚" : "🤍" }}
      </button>
      <span class="like-count">{{ post?.likes }}</span>
    </div>
  </div>
</template>

<script>
import CommentService from "@/services/comment/CommentService";
import PostService from "@/services/post/PostService";

export default {
  data() {
    return {
      post: null,
      likeId: 0,
      comments: [],
      newCommentText: "",
      commentCount:0,
      liked: false, 
      page: 1,
      count: 0,
      pageSize: 10,
    };
  },
  methods: {
    async loadPostLike() {
      try {
        if (this.$store.state.user != null) {
          let response = await PostService.getPostLike(
            this.post.postId,
            this.$store.state.user.userEmail
          );

          if (response.status == 200) {
            this.liked = true;
            this.likeId = response.data;
          } else {
            this.liked = false;
          }
        } else {
          return;
        }
      } catch (error) {
        console.log("좋아요한 회원이 아님");
      }
    },
    async loadPost() {
      try {
        let response = await PostService.getPost(this.$route.params.postId);

        this.post = response.data;
        this.$nextTick(() => {
          this.resizeImages(); // 이미지 리사이징 함수 호출
        });
      } catch (error) {
        alert("유효하지 않은 게시물입니다");
        console.error("게시물 로드 중 오류 발생", error);
        this.$router.push("/");
      }
    },
    async loadPostComments() {
      try {
        const pageable = { page: this.page - 1, size: this.pageSize };
        let response = await CommentService.getPostComments(
          pageable,
          this.$route.params.postId
        );
        // this.loadPostCommentsCount(this.$route.params.postId);
        const { postComments, totalItems } = response.data;
        this.comments = postComments;
        this.count = totalItems;
      } catch (error) {
        console.error("댓글 로드 중 오류 발생", error);
      }
    },
    // async loadPostCommentsCount(postId){
    //   try {
    //     const response = await CommentService.getPostCommentsCount(postId);
    //     this.commentCount = response.data;

    //   } catch (error) {
    //     console.log("댓글 갯수 로드 중 오류 발생");
    //   }
    // },
    async deletePost() {
      try {
        let result = confirm("정말로 삭제 하시겠습니까?");
        if (result) {
          await PostService.deletePost(this.post.postId);

          alert("게시물이 성공적으로 삭제되었습니다.");
          this.$router.push("/");
        }
      } catch (error) {
        console.error("Error:", error);
        alert("게시물 삭제에 실패하였습니다.");
      }
    },
    async addComment() {
      try {
        if (this.$store.state.user == null) {
          alert("로그인이 필요합니다");
          return;
        }
        if (this.newCommentText.trim()) {
          const postCommentReq = {
            postId: this.post.postId,
            writer: this.$store.state.user.userEmail,
            content: this.newCommentText,
          };
          await CommentService.createComment(postCommentReq);
          this.loadPostComments();
          this.newCommentText = "";
        } else {
          alert("댓글을 입력해주세요");
        }
      } catch (error) {
        console.log("댓글 입력 오류", error);
      }
    },
    async addReply(comment) {
      try {
        if (this.$store.state.user == null) {
          alert("로그인이 필요합니다");
        }
        if (comment.newReplyText.trim()) {
          const commentReplyReq = {
            postId: this.post.postId,
            parentId: comment.commentId,
            content: comment.newReplyText,
          };
          await CommentService.createReply(commentReplyReq);
          // comment.replying = false;
          this.loadPostComments();
          comment.newReplyText = ""; 
        }
      } catch (error) {
        console.log("답글 생성 중 오류 발생", error);
      }
    },
    toggleReply(comment) {
      comment.replying = !comment.replying;
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString();
    },
    async toggleLike() {
      try {
        if (this.$store.state.user == null) {
          alert("로그인이 필요합니다");
          return;
        }

        this.liked = !this.liked;
        if (this.liked) {
          let response = await PostService.likePost(this.post);
          this.likeId = response.data;
          this.post.likes += 1;
        } else {
          await PostService.unLikePost(this.likeId);
          this.likeId = 0;
          if (this.post.likes > 0) {
            this.post.likes -= 1;
          } else {
            this.post.likes = 0;
          }
        }
      } catch (error) {
        this.liked = !this.liked;
        console.log("좋아요 오류 발생", error);
      }
    },
    resizeImages() {
      const images = this.$el.querySelectorAll(".post-body img");
      images.forEach((img) => {
        img.style.maxWidth = "100%"; // 부모 요소의 너비로 제한
        img.style.height = "auto"; // 비율에 맞게 자동 조정
        img.style.objectFit = "contain"; // 이미지가 컨테이너에 맞추어 축소되도록 함
      });
    },
  },
  async created() {
    await this.loadPost();
    this.loadPostLike();
    this.loadPostComments();
  },
};
</script>

<style scoped>
.post-detail {
  max-width: 800px;
  margin: auto;
  padding: 20px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.post-header {
  margin-bottom: 30px;
  text-align: center;
}

.post-header h1 {
  font-size: 2.5em;
  font-weight: bold;
  color: #333;
  margin-bottom: 1.5em;
  line-height: 1.2;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 1.2em;
  margin-bottom: 3em;
}

.edit-delete {
  display: flex;
  align-items: center;
  color: gray;
}

.edit,
.delete {
  cursor: pointer;
  color: gray;
  text-decoration: none;
  margin-right: 10px;
}

.author-date {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.author-date span {
  margin-left: 10px;
}

.meta-separator,
.comment-separator {
  margin: 0 10px;
  color: #888;
}

.post-body {
  min-height: 300px;
  max-width: 800px; 
  font-size: 1.2em;
  line-height: 1.5;
  color: #333;
  margin-bottom: 100px;
  white-space: normal;
}

/* 이미지 크기 조정 */
.post-body img {
  max-width: 100%; /* 부모 요소의 크기에 맞춤 */
  height: auto; /* 원본 비율 유지 */
  object-fit: contain; /* 이미지가 컨테이너 안에 맞추어 축소되도록 설정 */
}

.comments-section {
  border-top: 2px solid #ccc;
  padding-top: 20px;
  margin-top: 0px;
}

.new-comment {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.new-comment textarea,
.comment textarea {
  min-height: 80px; 
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 2px solid #ddd;
  border-radius: 4px;
}

.new-comment button {
  padding: 10px 20px;
  background-color: #c1f4e5;
  color: #45a1cb;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.comment-list {
  list-style: none;
  padding: 0;
}

.comment {
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-top: 20px;
  background-color: #f8f8f8;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  margin-top: 15px;
}

.comment-date {
  font-size: 1.1em;
}

.comment-author {
  font-weight: bold;
  font-size: 1.1em;
}

.comment-body {
  min-height: 25px;
  font-size: 1.1em;
  line-height: 1.5;
  color: #333;
}

.comment-controls {
  text-align: right;
  padding-top: 10px;
}

.comment-controls button {
  padding: 6px 12px;
  border: none;
  border-radius: 5px;
  background-color: #c1f4e5;
  color: #45a1cb;
  cursor: pointer;
  transition: background-color 0.3s;
}

.comment-controls button:hover {
  background-color: #b2e4d3;
}

.comment-controls button:active {
  background-color: #9edac1;
}

.reply-toggle {
  text-align: right;
  padding-top: 10px;
}

.reply-toggle button {
  background: none;
  border: none;
  color: #45a1cb;
  cursor: pointer;
  font-size: 1em;
}

.replies {
  list-style: none;
  padding-left: 20px;
}

.like-container {
  position: fixed;
  right: 10vw; 
  top: 50%;
  transform: translateY(-50%);
  background-color: #f0f0f0;
  border-radius: 50px;
  padding: 10px 20px; 
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.like-button {
  background: none;
  border: none;
  font-size: 2em;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s, font-size 0.3s;
}

.like-count {
  margin-left: 10px;
  font-size: 1.5em; 
  color: #333;
}

.like-container:hover {
  transform: translateY(-50%) scale(1.1);
}

@media (max-width: 768px) {
  .like-button {
    font-size: 1.5em; 
  }

  .like-count {
    font-size: 1.2em;
  }
}

@media (max-width: 576px) {
  .like-button {
    font-size: 1.2em;
  }

  .like-count {
    font-size: 1em;
  }
}

@media (max-width: 768px) {
  .post-detail {
    padding: 15px;
  }

  .post-header h1 {
    font-size: 2em;
  }

  .comment-author,
  .comment-date {
    font-size: 0.9em;
  }

  .comment button {
    padding: 4px 8px;
  }
}

@media (max-width: 576px) {
  .post-detail {
    padding: 10px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  }

  .post-header h1 {
    font-size: 1.8em;
  }

  .new-comment textarea,
  .comment textarea {
    font-size: 14px;
  }

  .comment {
    font-size: 14px;
  }
}

@media (max-width: 400px) {
  .post-detail {
    padding: 5px;
  }

  .post-header h1 {
    font-size: 1.5em;
  }

  .new-comment textarea,
  .comment textarea {
    font-size: 12px;
  }

  .comment {
    font-size: 12px;
  }
}
</style>
