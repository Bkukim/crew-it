// 웹토큰을 헤더에 넣어 spring 으로 보내기 위한 헤더를 정의해주는 곳

export default function AuthHeader(){ 
    let user = JSON.parse(localStorage.getItem('user'));
    if (user && user.accessToken) {
      return { Authorization: 'Bearer ' + user.accessToken }; 
    } else {
      return {};
    }
}


