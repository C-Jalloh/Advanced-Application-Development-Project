<template>
  <div class="h-screen w-full flex items-center justify-center overflow-hidden relative" style="background-color: var(--bg-main)">
    
    <!-- Back to Home -->
    <router-link to="/" class="absolute top-6 left-6 z-50 glass-button px-4 py-2 flex items-center gap-2">
      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
      </svg>
      Back to Home
    </router-link>

    <!-- Background Blobs for Liquid Feel -->
    <div class="absolute top-10 left-10 w-72 h-72 rounded-full mix-blend-multiply filter blur-xl opacity-60 animate-blob" style="background-color: var(--blob-1)"></div>
    <div class="absolute top-10 right-10 w-72 h-72 rounded-full mix-blend-multiply filter blur-xl opacity-60 animate-blob animation-delay-2000" style="background-color: var(--blob-2)"></div>
    <div class="absolute -bottom-8 left-20 w-72 h-72 rounded-full mix-blend-multiply filter blur-xl opacity-60 animate-blob animation-delay-4000" style="background-color: var(--blob-3)"></div>

    <!-- Main Container -->
    <div class="relative w-full max-w-[900px] h-full md:h-[600px] md:rounded-3xl shadow-2xl overflow-hidden backdrop-blur-xl border border-white/30 flex flex-col md:flex-row" 
         style="background: var(--glass-bg)"
         :class="{ 'right-panel-active': isSignUp }">
      
      <!-- LOGIN FORM CONTAINER -->
      <div class="form-container sign-in-container absolute top-0 h-full transition-all duration-700 ease-in-out w-full md:w-1/2 flex items-center justify-center p-6">
        <form @submit.prevent="handleLogin" class="liquid-glass w-full max-w-[400px] h-[98%] flex flex-col items-center justify-center px-6 md:px-12 py-12 text-center rounded-[3rem] shadow-xl transition-all duration-500 hover:shadow-2xl hover:-translate-y-2 border border-white/20">
          <h1 class="font-bold text-3xl mb-4">Sign in</h1>
          <span class="text-sm mb-6 opacity-70">Enter your credentials to continue</span>
          
          <div class="w-full mb-3">
            <input 
              v-model="loginData.username"
              type="text"
              placeholder="Username"
              class="glass-input"
              required
            />
          </div>

          <div class="w-full mb-4 relative">
            <input 
              v-model="loginData.password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="Password"
              class="glass-input pr-12"
              required
            />
            <button 
              type="button" 
              @click="showPassword = !showPassword"
              class="absolute right-4 top-1/2 -translate-y-1/2 opacity-50 hover:opacity-100 transition-opacity"
            >
              <svg v-if="!showPassword" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
              <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l18 18" />
              </svg>
            </button>
          </div>

          <span class="text-xs mb-4 cursor-pointer hover:underline opacity-70">Forgot your password?</span>
          <button type="submit" class="glass-button-primary px-12 py-3">
            Sign In
          </button>
          <p v-if="errorMessage" class="text-red-500 text-xs mt-4">{{ errorMessage }}</p>
          
          <!-- Mobile Toggle -->
          <p class="mt-8 text-sm md:hidden">
            Don't have an account? 
            <button type="button" @click="isSignUp = true" class="font-bold underline">Sign Up</button>
          </p>
        </form>
      </div>

      <!-- REGISTER FORM CONTAINER -->
      <div class="form-container sign-up-container absolute top-0 h-full transition-all duration-700 ease-in-out w-full md:w-1/2 flex items-center justify-center p-6">
        <form @submit.prevent="handleRegister" class="liquid-glass w-full max-w-[400px] h-[98%] flex flex-col items-center justify-center px-6 md:px-12 py-12 text-center rounded-[3rem] shadow-xl transition-all duration-500 hover:shadow-2xl hover:-translate-y-2 border border-white/20">
          <h1 class="font-bold text-3xl mb-4">Create Account</h1>
          <span class="text-sm mb-6 opacity-70">Enter your information to register</span>
          
          <div class="w-full mb-3">
            <input 
              v-model="regData.username"
              type="text"
              placeholder="Username"
              class="glass-input"
              required
            />
          </div>

          <div class="w-full mb-3">
            <input 
              v-model="regData.email"
              type="email"
              placeholder="Email"
              class="glass-input"
              required
            />
          </div>

          <div class="w-full mb-2 relative">
            <input 
              v-model="regData.password"
              :type="showRegPassword ? 'text' : 'password'"
              placeholder="Password"
              class="glass-input pr-12"
              required
            />
            <button 
              type="button" 
              @click="showRegPassword = !showRegPassword"
              class="absolute right-4 top-1/2 -translate-y-1/2 opacity-50 hover:opacity-100 transition-opacity"
            >
              <svg v-if="!showRegPassword" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
              <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l18 18" />
              </svg>
            </button>
          </div>

          <!-- Password Guidelines -->
          <div class="w-full mb-4 px-2 text-left">
            <div v-for="(req, index) in passwordRequirements" :key="index" 
                 class="flex items-center gap-2 text-[10px] transition-all duration-300"
                 :class="regData.password ? (req.met ? 'text-green-500' : 'text-red-400') : 'opacity-50'">
              <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path v-if="req.met" stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12" />
              </svg>
              {{ req.label }}
            </div>
          </div>

          <button type="submit" class="glass-button-primary px-12 py-3">
            Sign Up
          </button>
          <p v-if="message" class="text-green-600 text-xs mt-4">{{ message }}</p>

          <!-- Mobile Toggle -->
          <p class="mt-8 text-sm md:hidden">
            Already have an account? 
            <button type="button" @click="isSignUp = false" class="font-bold underline">Sign In</button>
          </p>
        </form>
      </div>

      <!-- OVERLAY CONTAINER (The Moving Part) -->
      <div class="overlay-container hidden md:block absolute top-0 left-1/2 w-1/2 h-full overflow-hidden transition-transform duration-700 ease-in-out rounded-l-[100px]">
        <div :class="['overlay text-white relative -left-full h-full w-[200%] transform transition-transform duration-700 ease-in-out flex flex-row bg-gradient-to-r', themeStore.themes[themeStore.currentTheme].colors]">
          
          <!-- Left Panel (Visible when showing Register Form) -->
          <div class="overlay-panel overlay-left absolute flex flex-col items-center justify-center text-center top-0 h-full w-1/2 transform translate-x-[-20%] transition-transform duration-700 ease-in-out px-10">
            <h1 class="text-3xl font-bold mb-4 text-white">Welcome Back!</h1>
            <p class="mb-8 font-light text-white">To keep connected with us please login with your personal info</p>
            <button type="button" @click="isSignUp = false" class="glass-button px-12 py-3 !text-white !border-white/30 hover:!bg-white/20">
              Sign In
            </button>
          </div>
          
          <!-- Right Panel (Visible when showing Login Form) -->
          <div class="overlay-panel overlay-right absolute right-0 flex flex-col items-center justify-center text-center top-0 h-full w-1/2 transform transition-transform duration-700 ease-in-out px-10">
            <h1 class="text-3xl font-bold mb-4 text-white">Hello, Friend!</h1>
            <p class="mb-8 font-light text-white">Enter your personal details and start your journey with Prism</p>
            <button type="button" @click="isSignUp = true" class="glass-button px-12 py-3 !text-white !border-white/30 hover:!bg-white/20">
              Sign Up
            </button>
          </div>

        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../store/user';
import { useThemeStore } from '../store/theme';
import apiClient from '../api/axios';

const isSignUp = ref(false);
const loginData = ref({ username: '', password: '' });
const regData = ref({ username: '', email: '', password: '' });
const showPassword = ref(false);
const showRegPassword = ref(false);
const router = useRouter();
const userStore = useUserStore();
const themeStore = useThemeStore();

const passwordRequirements = computed(() => {
  const p = regData.value.password;
  return [
    { label: 'At least 8 characters', met: p.length >= 8 },
    { label: 'At least one uppercase letter', met: /[A-Z]/.test(p) },
    { label: 'At least one number', met: /[0-9]/.test(p) },
    { label: 'At least one special character', met: /[^A-Za-z0-9]/.test(p) }
  ];
});

onMounted(() => {
  themeStore.applyTheme();
});

const errorMessage = ref('');
const message = ref('');

const handleLogin = async () => {
    try {
      errorMessage.value = '';
      await userStore.login(loginData.value.username, loginData.value.password);
      router.push('/movies');
    } catch (error) {
      errorMessage.value = error.response?.data?.message || 'Invalid credentials!';
    }
};

const handleRegister = async () => {
    try {
      message.value = '';
      
      // Validate password requirements
      const unmet = passwordRequirements.value.filter(r => !r.met);
      if (unmet.length > 0) {
        message.value = 'Please meet all password requirements.';
        return;
      }

      await apiClient.post('/auth/register', regData.value);
      message.value = 'Registration successful! Please login.';
      setTimeout(() => {
        isSignUp.value = false;
        regData.value = { username: '', email: '', password: '' };
      }, 2000);
    } catch (error) {
      message.value = error.response?.data?.message || 'Error: Could not register!';
    }
};
</script>

<style scoped>
/* Form containers positioning */
.sign-in-container {
  left: 0;
  z-index: 20;
}

.sign-up-container {
  left: 0;
  opacity: 0;
  z-index: 10;
}

/* Desktop specific positioning */
@media (min-width: 768px) {
  .sign-in-container {
    width: 50%;
  }
  .sign-up-container {
    width: 50%;
  }
}

/* Logic for the sliding panels */
.right-panel-active .sign-in-container {
  transform: translateX(100%);
  opacity: 0;
  z-index: 1;
}

/* Mobile logic: don't translate, just fade */
@media (max-width: 767px) {
  .right-panel-active .sign-in-container {
    transform: none;
    opacity: 0;
    z-index: 1;
  }
  
  .right-panel-active .sign-up-container {
    transform: none !important;
    opacity: 1;
    z-index: 25;
  }
}

.right-panel-active .sign-up-container {
  transform: translateX(100%);
  opacity: 1;
  z-index: 25;
  animation: show 0.6s;
}

.overlay-container {
  z-index: 100;
}

.right-panel-active .overlay-container {
  transform: translateX(-100%);
  border-radius: 0 100px 100px 0;
}

.right-panel-active .overlay {
  transform: translateX(50%);
}

.right-panel-active .overlay-left {
  transform: translateX(0);
}

.right-panel-active .overlay-right {
  transform: translateX(20%);
}

@keyframes show {
  0%, 49.99% { 
    opacity: 0; 
    z-index: 1; 
  }
  50%, 100% { 
    opacity: 1; 
    z-index: 25; 
  }
}

/* Blob Animation */
@keyframes blob {
  0% { transform: translate(0px, 0px) scale(1); }
  33% { transform: translate(30px, -50px) scale(1.1); }
  66% { transform: translate(-20px, 20px) scale(0.9); }
  100% { transform: translate(0px, 0px) scale(1); }
}

.animate-blob {
  animation: blob 7s infinite;
}

.animation-delay-2000 {
  animation-delay: 2s;
}

.animation-delay-4000 {
  animation-delay: 4s;
}

/* Responsive design */
@media (max-width: 1024px) {
  /* Removed the generic .relative override as it was too broad */
}
</style>
