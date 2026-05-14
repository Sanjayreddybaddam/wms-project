import axios from "axios";
import { getToken } from "../utils/auth";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

api.interceptors.request.use((config) => {
  const token = getToken();

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  (res) => res,
  (err) => {
    const status = err.response?.status;

    // ❌ only redirect on REAL auth failure
    if (status === 401) {
      localStorage.clear();
      window.location.href = "/login";
    }

    return Promise.reject(err);
  }
);

export default api;