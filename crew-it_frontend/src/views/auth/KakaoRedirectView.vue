<template>
  <div></div>
</template>

<script>
import AuthService from "@/services/auth/AuthService";
export default {
  data() {
    return {
      user: {},
    };
  },
  methods: {
    async kakaoLogin(code) {
      try {
        let response = await AuthService.socialLogin(code);
       
          const user = response.data;
          localStorage.setItem("user", JSON.stringify(user));
          this.$store.commit("loginSuccess", user);
          this.$router.push("/");
          setTimeout(() => window.location.reload(), 500);

      } catch (e) {
        console.log(e);
      }
    },
    
  },
  mounted() {
    let code = this.$route.query.code;
    this.kakaoLogin(code);
  },

};
</script>
