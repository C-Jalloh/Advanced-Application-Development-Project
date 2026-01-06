<template>
  <div class="h-screen flex flex-col overflow-hidden bg-transparent">
    <!-- Top Navbar - Floating -->
    <div class="p-3 pb-0 relative z-50">
      <header class="h-16 px-4 md:px-6 flex items-center justify-between gap-4 liquid-glass border-2 flex-shrink-0 !rounded-[20px]">
        <!-- Left: Logo -->
        <div class="flex items-center gap-4 flex-1">
          <button 
            @click="drawer = !drawer" 
            class="md:hidden p-2 rounded-full hover:bg-white/20 transition-colors flex-shrink-0"
          >
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
            </svg>
          </button>

          <h1 class="text-2xl font-bold flex items-center gap-2" style="color: var(--text-main)">
            <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 2L2 7v10c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V7l-10-5z"/>
            </svg>
            <span class="hidden sm:inline">Prism</span>
          </h1>
        </div>
        
        <!-- Center: Search Bar -->
        <div class="hidden md:flex items-center justify-center flex-1">
          <div class="liquid-glass flex items-center px-4 py-1 w-full max-w-md h-10">
            <svg class="w-4 h-4 flex-shrink-0" style="color: var(--text-muted)" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
            </svg>
            <input 
              v-model="globalSearchQuery"
              @input="handleGlobalSearch"
              type="text" 
              placeholder="Search movies and music..." 
              class="bg-transparent border-none outline-none ml-2 w-full text-sm"
              style="color: var(--text-main); --placeholder-color: var(--text-muted)"
            >
            <button
              v-if="globalSearchQuery"
              @click="clearGlobalSearch"
              class="ml-2 p-1 hover:bg-white/10 rounded-full transition-colors"
            >
              <svg class="w-4 h-4" style="color: var(--text-muted)" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>

        <!-- Right: Actions -->
        <div class="flex items-center justify-end gap-2 md:gap-4 flex-1">
          <!-- Theme Selector -->
          <div class="relative">
            <button 
              @click="showThemeMenu = !showThemeMenu"
              class="glass-button px-3 md:px-4 h-10"
            >
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01" />
              </svg>
              <span class="hidden lg:inline text-sm font-semibold">Theme</span>
            </button>
            
            <div 
              v-if="showThemeMenu"
              class="absolute right-0 mt-2 w-48 liquid-glass backdrop-blur-3xl rounded-xl overflow-hidden z-50 border-2 shadow-[0_20px_50px_rgba(0,0,0,0.3)]"
              style="background: color-mix(in srgb, var(--bg-main), transparent 15%)"
            >
              <div class="p-2 space-y-1">
                <button 
                  v-for="(theme, key) in themeStore.themes" 
                  :key="key"
                  @click="themeStore.setTheme(key); showThemeMenu = false"
                  class="w-full px-4 py-2 text-left rounded-full transition-colors flex items-center justify-between group hover:bg-white/10"
                  :class="{ 'bg-white/20 font-bold': themeStore.currentTheme === key }"
                  style="color: var(--text-main)"
                >
                  <span class="text-sm font-medium">{{ theme.label || theme.name }}</span>
                  <div :class="['w-3 h-3 rounded-full bg-gradient-to-r', theme.colors]"></div>
                </button>
              </div>
            </div>
          </div>

          <!-- User Menu -->
          <div class="relative">
            <button 
              @click="showUserMenu = !showUserMenu"
              class="glass-button w-10 h-10 !rounded-full !p-0 overflow-hidden flex items-center justify-center"
            >
              <img 
                v-if="userStore.user?.profilePicture" 
                :src="`http://127.0.0.1:8080/images/${userStore.user.profilePicture}`" 
                class="w-full h-full object-cover block"
              />
              <div v-else class="w-full h-full flex items-center justify-center" style="background-color: var(--accent-color)">
                <span class="text-white font-bold text-xs">{{ userInitial }}</span>
              </div>
            </button>
            
            <div 
              v-if="showUserMenu"
              class="absolute right-0 mt-2 w-48 liquid-glass backdrop-blur-3xl rounded-xl overflow-hidden border-2 shadow-[0_20px_50px_rgba(0,0,0,0.3)]"
              style="background: color-mix(in srgb, var(--bg-main), transparent 15%)"
            >
              <div class="px-4 py-3 border-b border-white/10">
                <p class="font-semibold truncate" style="color: var(--text-main)">{{ userStore.user?.username || 'User' }}</p>
                <p class="text-xs" style="color: var(--text-muted)">User</p>
              </div>
              <router-link 
                to="/profile"
                class="w-full px-4 py-3 text-left hover:bg-white/10 transition-colors flex items-center gap-2"
                style="color: var(--text-main)"
                @click="showUserMenu = false"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
                Profile Settings
              </router-link>
              <button 
                @click="logout"
                class="w-full px-4 py-3 text-left rounded-b-xl hover:bg-white/10 transition-colors flex items-center gap-2"
                style="color: var(--text-main)"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"/>
                </svg>
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>
    </div>

    <div class="flex flex-1 overflow-hidden gap-3 pt-2">
      <!-- Glass Sidebar -->
      <aside 
        :class="[
          'fixed md:relative top-0 left-0 h-full w-64 liquid-glass !rounded-none !rounded-tr-[30px] border-r-2 p-6 z-40 transition-transform duration-300',
          drawer ? 'translate-x-0' : '-translate-x-full md:translate-x-0'
        ]"
      >
        <nav class="space-y-2">
          <router-link to="/" class="nav-item">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"/>
            </svg>
            <span>Home</span>
          </router-link>

          <router-link to="/movies" class="nav-item">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 4v16M17 4v16M3 8h4m10 0h4M3 12h18M3 16h4m10 0h4M4 20h16a1 1 0 001-1V5a1 1 0 00-1-1H4a1 1 0 00-1 1v14a1 1 0 001 1z"/>
            </svg>
            <span>Movies</span>
          </router-link>

          <router-link to="/music" class="nav-item">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3"/>
            </svg>
            <span>Music</span>
          </router-link>

          <router-link to="/playlists" class="nav-item">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
            </svg>
            <span>My Playlists</span>
          </router-link>

          <router-link to="/profile" class="nav-item">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
            <span>Profile</span>
          </router-link>
        </nav>
      </aside>

      <!-- Overlay for mobile -->
      <div 
        v-if="drawer" 
        @click="drawer = false"
        class="fixed inset-0 bg-black/20 z-30 md:hidden"
      ></div>

      <!-- Main Content Area -->
      <main class="flex-1 overflow-y-auto no-scrollbar liquid-glass !rounded-none !rounded-tl-[30px] border-l-2 p-4 md:p-6 min-w-0">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, provide } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '../store/user';
import { useThemeStore } from '../store/theme';

const drawer = ref(false);
const showUserMenu = ref(false);
const showThemeMenu = ref(false);
const globalSearchQuery = ref('');
let searchTimeout = null;

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const themeStore = useThemeStore();

// Provide global search to child components
provide('globalSearchQuery', globalSearchQuery);

onMounted(() => {
  themeStore.applyTheme();
});

const userInitial = computed(() => {
  if (userStore.user?.username) {
    return userStore.user.username.substring(0, 2).toUpperCase();
  }
  return 'U';
});

const handleGlobalSearch = () => {
  clearTimeout(searchTimeout);
  searchTimeout = setTimeout(() => {
    // The search query is reactive and will be picked up by child components
    console.log('Global search:', globalSearchQuery.value);
  }, 300);
};

const clearGlobalSearch = () => {
  globalSearchQuery.value = '';
  handleGlobalSearch();
};

const logout = () => {
  userStore.logout();
  router.push('/login');
  showUserMenu.value = false;
};
</script>

<style scoped>
.nav-item {
  @apply flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 font-medium;
  color: var(--text-main);
}

.nav-item:hover {
  background: var(--glass-bg);
  box-shadow: var(--glass-inner-glow);
}

.nav-item.router-link-active {
  background: var(--glass-bg);
  border: 1px solid var(--glass-border);
  box-shadow: var(--glass-shadow), var(--glass-inner-glow);
  @apply font-bold;
}
</style>
