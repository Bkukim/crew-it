import http from "@/utils/http-common"; // spring 통신 정의 파일
import AuthHeader from "@/services/auth/AuthHeader";

class PostService {
  // todo : 게시물 저장
  createPost(post) {
    return http.post(`/user/posts`, post, {
      headers: AuthHeader(),
    });
  }

  // todo : 게시물 수정
  updatePost(post) {
    return http.put(`/user/posts/${post.postId}`, post, {
      headers: AuthHeader(),
    });
  }

  // TODO: 메인에서 최신순 조회
  getLatestPosts(pageable, title) {
    return http.get(`/public/posts/latest`, {
      params: {
        page: pageable.page,
        size: pageable.size,
        title: title, // title을 쿼리 파라미터로 전달
      },
      headers: AuthHeader(),
    });
  }

  // TODO: 메인에서 좋아요순 조회
  getMostLikedPosts(pageable, title) {
    return http.get(`/public/posts/most-liked`, {
      params: {
        page: pageable.page,
        size: pageable.size,
        title: title, // title을 쿼리 파라미터로 전달
      },
      headers: AuthHeader(),
    });
  }

  // todo : 상세조회
  getPost(postId) {
    return http.get(`/public/posts/${postId}`, {
      headers: AuthHeader(),
    });
  }

  // todo : 삭제
  deletePost(postId) {
    return http.delete(`/user/posts/${postId}`, {
      headers: AuthHeader(),
    });
  }
  // todo 게시물 좋아요
  getPostLike(postId, userEmail) {
    return http.get(`/user/posts/like`, {
      params: {
        postId: postId,
        userEmail: userEmail,
      },
      headers: AuthHeader(),
    });
  }

  //  todo : 게시물 좋아요
  likePost(post) {
    return http.post(`/user/posts/like`, post, {
      headers: AuthHeader(),
    });
  }

  //  todo : 게시물 좋아요 취소
  unLikePost(likeId) {
    return http.delete(`/user/posts/like/${likeId}`, {
      headers: AuthHeader(),
    });
  }

  // todo : 유저 게시물 조회
  getUsersPosts(pageable, userEmail) {
    return http.get(`/user/posts`, {
      params: {
        page: pageable.page,
        size: pageable.size,
        userEmail: userEmail,
      },
      headers: AuthHeader(),
    });
  }

  // todo : 삭제
  deletePostAsAdmin(postId) {
    return http.delete(`/admin/posts/${postId}`, {
      headers: AuthHeader(),
    });
  }
}

export default new PostService();
