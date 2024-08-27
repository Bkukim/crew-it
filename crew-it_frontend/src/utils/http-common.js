// 1)
import axios from "axios";

export default axios.create({
  // baseURL: "http://13.209.24.76:8000/api",  // AWS ip 주소
  baseURL: "http://localhost:8000/api",
  headers: {
    "Content-Type": "application/json"
  }
});
