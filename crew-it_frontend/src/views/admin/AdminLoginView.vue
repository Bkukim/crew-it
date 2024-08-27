<template>
  <div class="login-container">
    <h2>관리자 로그인</h2>
    <form @submit.prevent="login">
      <div class="input-group">
        <label for="username">ID</label>
        <input type="text" id="username" v-model="adminName" required />
      </div>
      <div class="input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="password" required />
      </div>
      <button @click="adminLogin()">Login</button>
    </form>
  </div>
</template>

<script>
import AuthService from "@/services/auth/AuthService";

export default {
  data() {
    return {
      adminName: "",
      password: "",
    };
  },
  methods: {
    async adminLogin() {
      try {
        const adminReq = {
          adminName: this.adminName,
          password: this.password,
        };
        let response = await AuthService.adminLogin(adminReq);
        localStorage.setItem("user", JSON.stringify(response.data)); // 로칼스토리지는 객체를 저장할 수 없기에 이걸 문자열로 바꿔서 진행해야한다.
        this.$store.commit("loginSuccess", response.data);
        this.$router.replace("/all-post");
      } catch (error) {
        console.log("관리자 로그인 실패", error);
        alert("회원정보가 일치하지 않습니다");
      }
    },
  },
};
</script>
<style>
body {
  font-family: Arial, sans-serif;
  background-color: #f2f2f2;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0;
}

.login-container {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  width: 300px;
  text-align: center;
}

h2 {
  margin-bottom: 20px;
}

.input-group {
  margin-bottom: 15px;
  text-align: left;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
  border-radius: 4px;
  border: 1px solid #ccc;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

button:hover {
  background-color: #45a049;
}
</style>
