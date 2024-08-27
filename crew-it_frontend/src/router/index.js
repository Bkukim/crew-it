import { createRouter, createWebHistory  } from 'vue-router'

const routes = [
  {
    path: "/",
    meta: { showHeader: true }, // 기본적으로 헤더를 보이게 설정, FALSE이면 숨김
    component: () => import("../views/HomeView.vue"),
  },
  {
    path: "/post",
    meta: { showHeader: true },
    component: () => import("../views/post/CreatePostView.vue"),
  },

  {
    path: "/post-detail/:postId",
    meta: { showHeader: true },
    component: () => import("../views/post/PostDetailView.vue"),
  },
  {
    path: "/auth-redirect",
    meta: { showHeader: false, showFooter: false },
    component: () => import("../views/auth/KakaoRedirectView.vue"),
  },
  {
    path: "/search",
    meta: { showHeader: true },
    component: () => import("../views/search/SearchView.vue"),
  },
  {
    path: "/notifications",
    component: () => import("../views/notification/NotificationView.vue"),
  },
  {
    path: "/admin-login",
    meta: { showHeader: false, showFooter: false },
    component: () => import("../views/admin/AdminLoginView.vue"),
  },
  {
    path: "/user-info",
    meta: { showHeader: true, showFooter: false },
    component: () => import("../views/user/UserInfoView.vue"),
  },
  {
    path: "/my-post",
    meta: { showHeader: true },
    component: () => import("../views/user/MyPostView.vue"),
  },
  {
    path: "/all-post",
    meta: { showHeader: false, showFooter: false },
    component: () => import("../views/admin/AdminPostCheck.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), // 히스토리 모드 설정
  routes
});


export default router
