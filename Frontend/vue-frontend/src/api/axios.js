import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://127.0.0.1:8080/api", // Using IP instead of localhost to avoid resolution issues
  headers: {
    "Content-Type": "application/json",
  },
});

// Automatically add token to requests if available
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default apiClient;
