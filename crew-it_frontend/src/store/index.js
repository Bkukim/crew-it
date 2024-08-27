import { createStore } from "vuex";

// TODO: 로그인 정보 + 로그인상태 를 정의
const user = JSON.parse(localStorage.getItem("user"));

export default createStore({
  state: {
    loggedIn: user ? true : false, // 로그인 여부
    user: user ? user : null, //  로그인 정보 (웹토큰 속성 있음)
    
    searchTitle: "",

    backendIp:"localhost:8000",
    frontendIp:"localhost:8080",
  },

  getters: {},

  mutations: {
    // todo 로그인 성공함수
    loginSuccess(state, user) {
      state.loggedIn = true;
      state.user = user;
    },

    // todo 검색어 setter
    setSearchTitle(state, title) {
      state.searchTitle = title;
    },

    //todo 검색어 초기화
    clearSearchTitle(state) {
      state.searchTitle = "";
    },

    // todo 로그인 실패함수
    loginFailure(state) {
      state.loggedIn = false;
      state.user = null;
    },

    // todo 로그아웃 함수
    logout(state) {
      state.loggedIn = false;
      state.user = null;
    },

    // todo 회원가입 성공함수
    registerSuccess(state) {
      state.loggedIn = false;
    },

    // todo 회원가입 실패함수
    registerFailure(state) {
      state.loggedIn = false;
    },

  
  },

  // 비동기 함수들을 정의하는 곳
  actions: {
  },

  // 공유 저장소를 여러개 사용할 경우 모듈로 정의해서 분리가능하다. 모듈 정의하는 곳
  modules: {},
});