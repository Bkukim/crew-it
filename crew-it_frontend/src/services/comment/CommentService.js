import http from "@/utils/http-common"; 
import AuthHeader from "@/services/auth/AuthHeader";

class PostService {
  // TODO : 댓글 저장
  createComment(postCommentReq) {
    return http.post(`/user/posts/comments`, postCommentReq, {
      headers: AuthHeader(),
    });
  }

  // TODO : 대댓글 저장
  createReply(commentReplyReq) {
    return http.post(`/user/posts/comments/replies`, commentReplyReq, {
      headers: AuthHeader(),
    });
  }
  // TODO: 게시물 댓글 조회
  getPostComments(pageable, postId) {
    return http.get(`/public/posts/comments`, {
      params: {
        page: pageable.page,
        size: pageable.size,
        postId: postId, 
      },
      headers: AuthHeader(),
    });
  }
  
  // TODO: 게시물 댓글 조회
  getPostCommentsCount(postId) {
    return http.get(`/public/posts/comments/count`, {
      params: {
        postId: postId, 
      },
      headers: AuthHeader(),
    });
  }


}

export default new PostService();
