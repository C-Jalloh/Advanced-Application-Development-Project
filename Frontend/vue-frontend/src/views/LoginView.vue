<template>
  <div class="min-h-screen flex items-center justify-center p-6">
    <router-link to="/" class="absolute top-6 left-6 z-50 glass-button px-4 py-2 flex items-center gap-2">
      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
      </svg>
      Back to Home
    </router-link>

    <div class="max-w-md mx-auto p-12 liquid-glass rounded-[3rem] shadow-xl transition-all duration-500 hover:shadow-2xl hover:-translate-y-2 border border-white/20 animate-fade-in">
      <h2 class="text-3xl font-bold text-center mb-8" style="color: var(--text-main)">Login</h2>
    
    <form @submit.prevent="login" class="space-y-6">
      <div class="space-y-2">
        <label class="text-sm font-medium ml-1 opacity-70">Username</label>
        <input 
          v-model="username" 
          placeholder="Enter your username" 
          class="glass-input" 
          required 
        />
      </div>
      
      <div class="space-y-2">
        <label class="text-sm font-medium ml-1 opacity-70">Password</label>
        <input 
          v-model="password" 
          type="password" 
          placeholder="Enter your password" 
          class="glass-input" 
          required 
        />
      </div>

      <button 
        type="submit" 
        class="glass-button-primary w-full py-3 text-lg mt-4"
      >
        Login
      </button>
    </form>
    
    <p v-if="errorMessage" class="text-red-400 text-center mt-4 text-sm font-medium">{{ errorMessage }}</p>
    
    <div class="mt-8 pt-6 border-t border-white/10 text-center">
      <p class="text-sm opacity-70">
        Don't have an account? 
        <router-link to="/register" class="font-bold hover:underline" style="color: var(--accent-color)">
          Register here
        </router-link>
      </p>
    </div>
  </div>
</template>

<script>
import { useUserStore } from "../store/user";

export default {
  data() {
    return { username: "", password: "", errorMessage: "" };
  },
  methods: {
    async login() {
      const userStore = useUserStore();
      try {
        await userStore.login(this.username, this.password);
        this.$router.push("/movies");
      } catch (error) {
        this.errorMessage = "Invalid credentials!";
      }
    },
  },
};
</script>

<style scoped>
.animate-fade-in {
  animation: fadeIn 0.6s ease-out forwards;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
  