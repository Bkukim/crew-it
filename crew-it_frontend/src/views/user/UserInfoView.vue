<template>
  <div class="container my-5">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="text-center mb-5">
          <h1 class="mb-3">{{user?.nickname}}님 환영해요</h1>
        </div>
        <form @submit.prevent="changeNickname">
          <div class="mb-5">
            <label for="nickname" class="form-label text-center"><strong>닉네임</strong></label>
            <input type="text" class="form-control w-50 mx-auto" id="nickname" v-model="user.nickname" required>
          </div>
          <div class="text-center mb-4">
            <p class="text-danger cursor-pointer" @click="deleteAccount">회원탈퇴</p>
          </div>
          <div class="text-end">
            <button type="submit" class="btn btn-primary btn-lg">확인</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import UserService from '@/services/user/UserService';

export default {
  data() {
    return {
      user: {}
    }
  },
  methods: {
    async loadUser(){
      try {
          let response = await UserService.getUser(this.$store.state.user.userEmail);
          this.user = response.data;
      } catch (error) {
          console.log("회원 정보를 불러오는데 실패했습니다.", error);
      }
    }, 

    async changeNickname() {
      try {
        let response = await UserService.updateUser(this.user);
        this.user = response.data;
        alert("닉네임을 변경했습니다");
        this.$router.push("/")
      } catch (error) {
        console.log("닉네임 변경에 실패했습니다.", error);
      }
    },

    async deleteAccount() {
      if (confirm("정말로 회원탈퇴를 하시겠습니까?")) {
        try {
          await UserService.deleteUser(this.$store.state.user.userEmail);
          alert("회원탈퇴가 완료되었습니다.");
          this.$store.commit('logout');
          this.$router.push("/");
        } catch (error) {
          console.log("회원탈퇴에 실패했습니다.", error);
        }
      }
    }
  },
  mounted() {
    if (this.$store.state.user == null) {
      alert("로그인이 필요한 페이지 입니다");
      this.$router.push("/"); 
    } 
    this.loadUser();
  },
}
</script>

<style scoped>
h1 {
  font-size: 2.5rem; /* Larger font for the welcome message */
}

.form-control {
  height: calc(1.5em + 0.75rem + 2px); /* Standard height adjusting if needed */
  width: 50%; /* Adjusting input width to half */
}

label {
  font-size: 1.2rem; /* Larger label text */
  display: block; /* Ensures the label takes full width for proper alignment */
  text-align: center; /* Centers the text within the label */
}

.mb-5 {
  margin-bottom: 3rem; /* Increased vertical spacing */
}

button.btn-lg {
  padding: 0.5rem 1rem; /* Larger button for better interaction */
}

.text-danger {
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
}
</style>
