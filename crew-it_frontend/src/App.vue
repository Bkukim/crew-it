<template>
  <div>
    <!-- 머리말 -->
    <!-- 헤더를 조건부로 렌더링 -->
    <HeaderCom v-if="showHeader" />

    <!-- 본문 -->
    <div class="qhsans">
      <router-view />
    </div>

    <!-- 푸터 -->
    <FoterView v-if="showFooter" />
  </div>
</template>
<script>
import HeaderCom from "@/components/common/HeaderCom.vue";
import FoterView from '@/components/common/FoterView.vue';

export default {
  components: {
    HeaderCom,
    FoterView,
  },
  computed: {
    showHeader() {
      // 현재 라우트의 메타데이터를 확인하여 헤더를 보여줄지 결정
      return this.$route.meta.showHeader !== false;
    },
     showFooter() {
      // 현재 라우트의 메타데이터를 확인하여 헤더를 보여줄지 결정
      return this.$route.meta.showFooter !== false;
    },
  },
};
</script>
<style >
/* .qhsans{
  height: 50vw;
} */
</style>

