// 웹토큰을 헤더에 넣어 spring 으로 보내기 위한 헤더를 정의해주는 곳
// + 파일 업로드 헤더도 제공

export default function AuthHeaderUpload(){
    let user = JSON.parse(localStorage.getItem('user')); 
    if (user && user.accessToken) {
      return { 
        "Content-Type": "multipart/form-data",
        Authorization: 'Bearer ' + user.accessToken }; 
    } else {
      return {}; 
    }
}


