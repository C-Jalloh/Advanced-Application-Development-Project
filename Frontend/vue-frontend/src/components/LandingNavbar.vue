<template>
  <nav class="fixed top-0 left-0 right-0 z-50 p-4">
    <div class="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between liquid-glass border-2 !rounded-full">
      <router-link to="/" class="flex items-center gap-2">
        <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 24 24">
          <path d="M12 2L2 7v10c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V7l-10-5z"/>
        </svg>
        <span class="text-2xl font-bold tracking-tight">Prism</span>
      </router-link>

      <div class="hidden md:flex items-center gap-8">
        <a href="#features" class="text-sm font-semibold hover:opacity-70 transition-opacity">Features</a>
        <a href="#how-it-works" class="text-sm font-semibold hover:opacity-70 transition-opacity">How it Works</a>
        <router-link to="/movies" class="text-sm font-semibold hover:opacity-70 transition-opacity">Explore</router-link>
      </div>

      <div class="flex items-center gap-4">
        <router-link 
          v-if="!isAuthenticated"
          to="/login" 
          class="text-sm font-bold hover:opacity-70 transition-opacity px-4"
        >
          Sign In
        </router-link>
        <router-link 
          v-if="!isAuthenticated"
          to="/register" 
          class="glass-button-primary px-6 py-2 text-sm"
        >
          Join Now
        </router-link>
        <router-link 
          v-else
          to="/movies" 
          class="glass-button-primary px-6 py-2 text-sm"
        >
          Dashboard
        </router-link>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { computed } from 'vue';
import { useUserStore } from '../store/user';

const userStore = useUserStore();
const isAuthenticated = computed(() => userStore.isAuthenticated);
</script>
