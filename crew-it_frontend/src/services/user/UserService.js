import http from "@/utils/http-common"; // spring 통신 정의 파일
import AuthHeader from "@/services/auth/AuthHeader";

class UserService {

  // TODO 회원 정보 수정
  updateUser(user) {
    return http.put(`/user/${user.userEmail}`, user, {
      headers: AuthHeader(),
    });
  }

  // TODO 회원 정보 가져오기
  getUser(userEmail) {
    return http.get(`/user/${userEmail}`, {
      headers: AuthHeader(),
    });
  }
  // TODO 회원 탈퇴
  deleteUser(userEmail) {
    return http.delete(`/user/${userEmail}`, {
      headers: AuthHeader(),
    });
  }
}

export default new UserService();
