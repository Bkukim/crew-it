import http from "@/utils/http-common.js";

class AuthService {
  
  // 카카오 로그인
  socialLogin(code) {
    // 요청 시 코드와 데이터를 함께 전송
    const KakaoReq = {
      code,
    };

    return http.post('/auth/kakao-login', KakaoReq, {
      headers: {
        'Content-Type': 'application/json',
        // 추가적인 보안 헤더가 필요한 경우 여기에 추가
      }
    });
  }
  logout() {
    // 로컬 스토리지의 값을 삭제
    // 사용법 : localStorage.removeIItem("키이름")
    localStorage.removeItem("user");
  }

  adminLogin(adminReq){
    return http.post('/auth/admin/login', adminReq, {
      headers: {
        'Content-Type': 'application/json',
      }
    });
    
  }
 
}

export default new AuthService();
