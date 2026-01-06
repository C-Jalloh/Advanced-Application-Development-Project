import { defineStore } from 'pinia';
import apiClient from '../api/axios';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || null,
  }),
  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.user?.role === 'ADMIN',
  },
  actions: {
    async login(username, password) {
      try {
        const response = await apiClient.post('/auth/login', { username, password });
        this.token = response.data.token;
        localStorage.setItem('token', this.token);
        await this.fetchUser();
        return true;
      } catch (error) {
        console.error('Login failed', error);
        throw error;
      }
    },
    async fetchUser() {
      if (!this.token) return;
      try {
        const response = await apiClient.get('/users/me');
        this.user = response.data;
        localStorage.setItem('username', this.user.username);
      } catch (error) {
        this.logout();
      }
    },
    async updateProfile(profileData) {
      try {
        const response = await apiClient.put('/users/profile', profileData);
        this.user = response.data;
        return response.data;
      } catch (error) {
        console.error('Update profile failed', error);
        throw error;
      }
    },
    async updateProfilePicture(file) {
      try {
        const formData = new FormData();
        formData.append('file', file);
        const response = await apiClient.post('/users/profile-picture', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });
        if (this.user) {
          this.user.profilePicture = response.data;
        }
        return response.data;
      } catch (error) {
        console.error('Update profile picture failed', error);
        throw error;
      }
    },
    logout() {
      this.user = null;
      this.token = null;
      localStorage.removeItem('token');
      localStorage.removeItem('username');
    },
  },
});
