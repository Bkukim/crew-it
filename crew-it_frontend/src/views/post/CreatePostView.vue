<template>
  <div class="container mt-5 mb-5">
    <h3 class="mb-5">게시글 작성</h3>
    <!-- 제목 -->
    <div class="col-10 mb-3">
      <input
        type="text"
        class="title-input"
        placeholder="제목을 적어주세요"
        v-model="title"
      />
    </div>
    <div class="col-12 mt-3 mb-3">
      <div class="row"></div>
    </div>

    <!-- 본문 -->
    <div ref="editor"></div>

    <!-- 버튼 -->
    <div class="row mt-5 justify-content-end">
      <div class="col-auto">
        <button @click="cancelFreeBoard" class="btn" id="button-cancel-Writing">
          취소
        </button>
      </div>
      <div class="col-auto">
        <button @click="createPost" class="btn" id="button-register-Writing">
          등록하기
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import PostService from "@/services/post/PostService";
import Editor from "@toast-ui/editor";
import "@toast-ui/editor/dist/toastui-editor.css";

export default {
  data() {
    return {
      editor: null,
      title: "",
      post: {},
      likeCount: 0 // 좋아요 수를 저장할 변수 추가
    };
  },
  methods: {
    async createPost() {
    if (!this.editor) {
      console.error("에디터가 초기화되지 않았습니다.");
      return;
    }
    // 제목이 빈칸일 때 경고 메시지를 띄움
    if (this.title.trim() === "") {
      alert("제목을 입력해주세요.");
      return;
    }
    
    try {
      // 에디터의 HTML 내용 가져오기
      const content = this.editor.getHTML();
      const postId = this.$route.query.id;
      if (postId == null) {
        const post = {
          userEmail: this.$store.state.user.userEmail,
          title: this.title,
          content: content,
        };
        let response = await PostService.createPost(post);
        console.log(response);
        alert("게시글이 저장 되었습니다.");
        this.$router.push("/");
      } else {
        this.post.content = content;
        this.post.title = this.title;
        let response = await PostService.updatePost(this.post);
        console.log(response);
        alert("게시글이 수정 되었습니다.");
        this.$router.push("/");
      }
    } catch (e) {
      console.log(e);
    }
  },
    async loadPost() {
      try {
        const postId = this.$route.query.id;
        if (postId == null) {
          return;
        } else {

          let response = await PostService.getPost(postId);
          this.post = response.data;
          this.title = this.post.title; // 제목을 Vue 인스턴스에 저장
          this.likeCount = response.data.likeCount || 0;
        }
      } catch (error) {
        console.error("게시물 로드 중 오류 발생", error);
      }
    },
    // toastUi 에디터 생성
    createPostEditor(content) {
      this.editor = new Editor({
        el: this.$refs.editor, // ref로 지정한 DOM 요소 사용
        initialEditType: "wysiwyg",
        initialValue: content || "", // 에디터 초기 값 설정
        minHeight: "400px", // 에디터 최소 높이 설정
        height: 'auto', // 에디터 높이 설정
      });
    },
    cancelFreeBoard() {
      this.$router.push("/"); 
    }
  },
  async mounted() {
    if (this.$store.state.user == null) {
      alert("로그인이 필요한 페이지 입니다");
      this.$router.push("/"); 
    } 
    await this.loadPost();
    this.createPostEditor(this.post.content);
    window.scrollTo(0, 0);
  },
};
</script>

<style>
.title-input {
  width: 100%;
  border: none;
  border-bottom: 2px solid lightgray;
  outline: none;
  font-size: 18px;
  font-weight: bold; /* 굵은 글씨체 */
  padding: 12px 0; /* 입력란의 높이를 증가 */
  background-color: transparent;
}

.title-input:focus {
  border-bottom: 3px solid #45a1cb;
}

#button-cancel-Writing,
#button-register-Writing {
  background-color: #c1f4e5;
  color: #45a1cb;
  border: none;
  width: 200px;
}
</style>
