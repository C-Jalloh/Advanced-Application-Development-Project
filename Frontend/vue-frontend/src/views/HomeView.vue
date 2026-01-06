<template>
  <div :class="['min-h-screen flex flex-col overflow-x-hidden', { 'pt-0': isAuthenticated, 'pt-20': !isAuthenticated }]">
    <LandingNavbar v-if="!isAuthenticated" />
    
    <!-- Hero Section -->
    <section :class="['flex items-center justify-center relative overflow-hidden', isAuthenticated ? 'py-12 min-h-[60vh]' : 'py-32 min-h-screen']">
      <!-- Decorative Blobs -->
      <div class="absolute top-0 left-1/4 w-[500px] h-[500px] bg-indigo-400/20 rounded-full blur-[120px] animate-blob"></div>
      <div class="absolute bottom-0 right-1/4 w-[500px] h-[500px] bg-purple-400/20 rounded-full blur-[120px] animate-blob animation-delay-2000"></div>
      <div class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[800px] h-[800px] bg-blue-400/10 rounded-full blur-[150px] animate-blob animation-delay-4000"></div>

      <div class="relative z-10 text-center max-w-5xl mx-auto">
        <div class="mb-12 inline-block p-6 rounded-[2.5rem] bg-white/10 backdrop-blur-2xl border border-white/20 shadow-2xl animate-bounce-slow">
          <svg class="w-24 h-24" style="color: var(--text-main)" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        
        <h1 class="text-6xl sm:text-7xl md:text-9xl font-black mb-8 tracking-tighter leading-none">
          <span :class="['bg-clip-text text-transparent bg-gradient-to-r', themeColors]">PRISM</span>
        </h1>
        
        <p class="text-xl md:text-3xl mb-12 font-medium leading-relaxed opacity-80 max-w-3xl mx-auto px-4">
          Your Media Universe. Discover, organize, and enjoy your favorite movies and music with a stunning liquid glass interface.
        </p>

        <div class="flex flex-col sm:flex-row items-center justify-center gap-6">
          <router-link 
            to="/movies" 
            class="w-full sm:w-auto glass-button-primary px-10 py-4 text-xl group"
          >
            Start Exploring
            <svg class="w-6 h-6 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
            </svg>
          </router-link>
          
          <router-link 
            v-if="!isAuthenticated"
            to="/login" 
            class="w-full sm:w-auto glass-button px-10 py-4 text-xl"
          >
            Sign In
          </router-link>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section id="features" class="py-32 px-6 relative">
      <div class="max-w-7xl mx-auto">
        <div class="text-center mb-20">
          <h2 class="text-4xl md:text-6xl font-bold mb-6">Everything you need</h2>
          <p class="text-xl opacity-70 max-w-2xl mx-auto">One platform to manage all your entertainment. Beautifully designed, effortlessly functional.</p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-10">
          <!-- Movies Card -->
          <router-link to="/movies" class="group liquid-glass p-10 transition-all duration-500 hover:-translate-y-4 hover:shadow-2xl border-2">
            <div class="w-20 h-20 rounded-[2rem] bg-white/10 flex items-center justify-center mb-8 group-hover:scale-110 transition-transform group-hover:rotate-3">
              <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 4v16M17 4v16M3 8h4m10 0h4M3 12h18M3 16h4m10 0h4M4 20h16a1 1 0 001-1V5a1 1 0 00-1-1H4a1 1 0 00-1 1v14a1 1 0 001 1z" />
              </svg>
            </div>
            <h3 class="text-3xl font-bold mb-4">Cinema Hub</h3>
            <p class="text-lg opacity-70 leading-relaxed">
              Browse thousands of movies, view detailed metadata, and keep track of what you want to watch next.
            </p>
          </router-link>

          <!-- Music Card -->
          <router-link to="/music" class="group liquid-glass p-10 transition-all duration-500 hover:-translate-y-4 hover:shadow-2xl border-2">
            <div class="w-20 h-20 rounded-[2rem] bg-white/10 flex items-center justify-center mb-8 group-hover:scale-110 transition-transform group-hover:-rotate-3">
              <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
              </svg>
            </div>
            <h3 class="text-3xl font-bold mb-4">Sonic Library</h3>
            <p class="text-lg opacity-70 leading-relaxed">
              Your personal music collection, organized and ready to play. High-quality audio meets high-quality design.
            </p>
          </router-link>

          <!-- Playlists Card -->
          <router-link to="/playlists" class="group liquid-glass p-10 transition-all duration-500 hover:-translate-y-4 hover:shadow-2xl border-2">
            <div class="w-20 h-20 rounded-[2rem] bg-white/10 flex items-center justify-center mb-8 group-hover:scale-110 transition-transform group-hover:rotate-3">
              <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
              </svg>
            </div>
            <h3 class="text-3xl font-bold mb-4">Smart Playlists</h3>
            <p class="text-lg opacity-70 leading-relaxed">
              Create the perfect mix for any occasion. Our smart organization makes playlist management a breeze.
            </p>
          </router-link>
        </div>
      </div>
    </section>

    <!-- Stats Section -->
    <section v-if="!isAuthenticated" class="py-20 px-6">
      <div class="max-w-7xl mx-auto grid grid-cols-2 md:grid-cols-4 gap-8">
        <div class="text-center">
          <div class="text-4xl md:text-6xl font-black mb-2" style="color: var(--accent-color)">50k+</div>
          <div class="text-sm md:text-lg font-bold opacity-60 uppercase tracking-widest">Movies</div>
        </div>
        <div class="text-center">
          <div class="text-4xl md:text-6xl font-black mb-2" style="color: var(--accent-color)">1M+</div>
          <div class="text-sm md:text-lg font-bold opacity-60 uppercase tracking-widest">Tracks</div>
        </div>
        <div class="text-center">
          <div class="text-4xl md:text-6xl font-black mb-2" style="color: var(--accent-color)">10k+</div>
          <div class="text-sm md:text-lg font-bold opacity-60 uppercase tracking-widest">Users</div>
        </div>
        <div class="text-center">
          <div class="text-4xl md:text-6xl font-black mb-2" style="color: var(--accent-color)">4.9/5</div>
          <div class="text-sm md:text-lg font-bold opacity-60 uppercase tracking-widest">Rating</div>
        </div>
      </div>
    </section>

    <!-- How it Works Section -->
    <section id="how-it-works" class="py-32 px-6 bg-white/5 backdrop-blur-sm">
      <div class="max-w-7xl mx-auto">
        <div class="flex flex-col md:flex-row items-center gap-20">
          <div class="flex-1">
            <h2 class="text-4xl md:text-6xl font-bold mb-8 leading-tight">Designed for the modern collector</h2>
            <div class="space-y-8">
              <div class="flex gap-6">
                <div class="w-12 h-12 rounded-full bg-indigo-500 flex-shrink-0 flex items-center justify-center text-white font-bold text-xl shadow-lg shadow-indigo-500/30">1</div>
                <div>
                  <h4 class="text-2xl font-bold mb-2">Create your account</h4>
                  <p class="opacity-70 text-lg">Join Prism in seconds and start building your personal media universe.</p>
                </div>
              </div>
              <div class="flex gap-6">
                <div class="w-12 h-12 rounded-full bg-purple-500 flex-shrink-0 flex items-center justify-center text-white font-bold text-xl shadow-lg shadow-purple-500/30">2</div>
                <div>
                  <h4 class="text-2xl font-bold mb-2">Import your media</h4>
                  <p class="opacity-70 text-lg">Easily add movies and music to your collection with our automated scraping tools.</p>
                </div>
              </div>
              <div class="flex gap-6">
                <div class="w-12 h-12 rounded-full bg-pink-500 flex-shrink-0 flex items-center justify-center text-white font-bold text-xl shadow-lg shadow-pink-500/30">3</div>
                <div>
                  <h4 class="text-2xl font-bold mb-2">Enjoy anywhere</h4>
                  <p class="opacity-70 text-lg">Access your library from any device with our responsive, glass-inspired interface.</p>
                </div>
              </div>
            </div>
          </div>
          <div class="flex-1 relative">
            <div class="liquid-glass p-4 rounded-[3rem] shadow-2xl rotate-3 hover:rotate-0 transition-transform duration-700">
              <div class="bg-black/20 rounded-[2rem] aspect-video flex items-center justify-center overflow-hidden">
                <img src="https://images.unsplash.com/photo-1536440136628-849c177e76a1?auto=format&fit=crop&w=800&q=80" alt="Cinema" class="w-full h-full object-cover opacity-80" />
              </div>
            </div>
            <div class="absolute -bottom-10 -left-10 liquid-glass p-4 rounded-[2rem] shadow-2xl -rotate-6 hover:rotate-0 transition-transform duration-700 hidden lg:block">
              <div class="bg-black/20 rounded-[1.5rem] w-64 h-40 flex items-center justify-center overflow-hidden">
                <img src="https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=400&q=80" alt="Music" class="w-full h-full object-cover opacity-80" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Testimonials Section -->
    <section v-if="!isAuthenticated" class="py-32 px-6 relative overflow-hidden">
      <div class="max-w-7xl mx-auto">
        <div class="text-center mb-20">
          <h2 class="text-4xl md:text-6xl font-bold mb-6">Loved by media fans</h2>
          <p class="text-xl opacity-70 max-w-2xl mx-auto">See what our community has to say about their Prism experience.</p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div class="liquid-glass p-8 border-2">
            <div class="flex gap-1 mb-4 text-yellow-400">
              <svg v-for="i in 5" :key="i" class="w-5 h-5 fill-current" viewBox="0 0 20 20"><path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/></svg>
            </div>
            <p class="text-lg italic mb-6 opacity-80">"The interface is absolutely stunning. I've never seen a media manager that looks this good while being so fast."</p>
            <div class="flex items-center gap-4">
              <div class="w-12 h-12 rounded-full bg-gradient-to-br from-indigo-500 to-purple-500"></div>
              <div>
                <div class="font-bold">Sarah Jenkins</div>
                <div class="text-sm opacity-60">Movie Enthusiast</div>
              </div>
            </div>
          </div>

          <div class="liquid-glass p-8 border-2 md:translate-y-8">
            <div class="flex gap-1 mb-4 text-yellow-400">
              <svg v-for="i in 5" :key="i" class="w-5 h-5 fill-current" viewBox="0 0 20 20"><path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/></svg>
            </div>
            <p class="text-lg italic mb-6 opacity-80">"Prism has completely changed how I organize my music. The smart playlists are a game changer for my workout routines."</p>
            <div class="flex items-center gap-4">
              <div class="w-12 h-12 rounded-full bg-gradient-to-br from-blue-500 to-cyan-500"></div>
              <div>
                <div class="font-bold">Marcus Chen</div>
                <div class="text-sm opacity-60">Music Producer</div>
              </div>
            </div>
          </div>

          <div class="liquid-glass p-8 border-2">
            <div class="flex gap-1 mb-4 text-yellow-400">
              <svg v-for="i in 5" :key="i" class="w-5 h-5 fill-current" viewBox="0 0 20 20"><path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"/></svg>
            </div>
            <p class="text-lg italic mb-6 opacity-80">"Finally, a media app that respects my privacy and looks beautiful at the same time. The liquid glass theme is pure eye candy."</p>
            <div class="flex items-center gap-4">
              <div class="w-12 h-12 rounded-full bg-gradient-to-br from-pink-500 to-rose-500"></div>
              <div>
                <div class="font-bold">Elena Rodriguez</div>
                <div class="text-sm opacity-60">Digital Artist</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Developers Section -->
    <section v-if="!isAuthenticated" class="py-32 px-6 bg-white/5 backdrop-blur-sm">
      <div class="max-w-7xl mx-auto">
        <div class="text-center mb-20">
          <h2 class="text-4xl md:text-6xl font-bold mb-6">Meet the Developers</h2>
          <p class="text-xl opacity-70 max-w-2xl mx-auto">The creative minds behind the Prism media universe.</p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-12 max-w-4xl mx-auto">
          <!-- Ebrima S Jallow -->
          <div class="group liquid-glass p-10 transition-all duration-500 hover:-translate-y-4 hover:shadow-2xl border-2 text-center">
            <div class="w-32 h-32 rounded-full bg-gradient-to-br from-indigo-500 to-purple-600 mx-auto mb-8 flex items-center justify-center text-white text-4xl font-bold shadow-xl group-hover:scale-110 transition-transform">
              EJ
            </div>
            <h3 class="text-3xl font-bold mb-2">Ebrima S Jallow</h3>
            <p class="text-indigo-400 font-semibold mb-4 uppercase tracking-widest text-sm">Backend Architect</p>
            <p class="opacity-70 leading-relaxed">
              Specializing in Spring Boot and system architecture, Ebrima ensures Prism's core is robust, secure, and lightning-fast.
            </p>
          </div>

          <!-- Nyanya Zainab -->
          <div class="group liquid-glass p-10 transition-all duration-500 hover:-translate-y-4 hover:shadow-2xl border-2 text-center">
            <div class="w-32 h-32 rounded-full bg-gradient-to-br from-pink-500 to-rose-600 mx-auto mb-8 flex items-center justify-center text-white text-4xl font-bold shadow-xl group-hover:scale-110 transition-transform">
              NZ
            </div>
            <h3 class="text-3xl font-bold mb-2">Nyanya Zainab</h3>
            <p class="text-pink-400 font-semibold mb-4 uppercase tracking-widest text-sm">Frontend Visionary</p>
            <p class="opacity-70 leading-relaxed">
              With a passion for UI/UX and Vue.js, Nyanya crafted the stunning liquid glass interface that makes Prism a joy to use.
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="py-32 px-6 text-center relative overflow-hidden">
      <div class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full h-full bg-gradient-to-b from-transparent via-indigo-500/5 to-transparent"></div>
      <div class="relative z-10 max-w-4xl mx-auto">
        <h2 class="text-5xl md:text-7xl font-bold mb-8">Ready to step into the light?</h2>
        <p class="text-2xl opacity-70 mb-12">Join thousands of users who have already transformed their media experience.</p>
        <router-link 
          v-if="!isAuthenticated"
          to="/register" 
          class="glass-button-primary px-12 py-5 text-2xl shadow-2xl shadow-indigo-500/20"
        >
          Get Started for Free
        </router-link>
        <router-link 
          v-else
          to="/movies" 
          class="glass-button-primary px-12 py-5 text-2xl shadow-2xl shadow-indigo-500/20"
        >
          Go to Dashboard
        </router-link>
      </div>
    </section>

    <Footer v-if="!isAuthenticated" />
  </div>
</template>

<script setup>
import { useUserStore } from "../store/user";
import { useThemeStore } from "../store/theme";
import { computed } from "vue";
import LandingNavbar from "../components/LandingNavbar.vue";
import Footer from "../components/Footer.vue";

const userStore = useUserStore();
const themeStore = useThemeStore();
const isAuthenticated = computed(() => userStore.isAuthenticated);
const themeColors = computed(() => themeStore.themes[themeStore.currentTheme].colors);
</script>

<style scoped>
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

.animate-bounce-slow {
  animation: bounce 3s infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateY(-5%); animation-timing-function: cubic-bezier(0.8, 0, 1, 1); }
  50% { transform: translateY(0); animation-timing-function: cubic-bezier(0, 0, 0.2, 1); }
}
</style>
