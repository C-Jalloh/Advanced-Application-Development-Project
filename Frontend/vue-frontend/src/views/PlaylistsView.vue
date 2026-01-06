<template>
  <div class="p-4 md:p-6">
    <!-- Header Section -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between mb-8 gap-4">
      <div class="flex items-center gap-3 md:gap-4">
        <div class="w-12 h-12 md:w-16 md:h-16 rounded-xl md:rounded-2xl liquid-glass flex items-center justify-center shadow-inner flex-shrink-0" style="color: var(--text-main)">
          <svg class="w-6 h-6 md:w-8 md:h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
          </svg>
        </div>
        <div class="min-w-0">
          <h1 class="text-2xl md:text-3xl font-bold truncate">Playlists</h1>
          <p class="text-sm opacity-70 truncate">Discover and curate your collections</p>
        </div>
      </div>
      <button 
        @click="openCreateDrawer"
        class="glass-button-primary px-6 md:px-8 py-2 md:py-2.5 w-full sm:w-auto"
      >
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
        </svg>
        New Playlist
      </button>
    </div>

    <!-- Tabs -->
    <div class="flex gap-4 mb-8 border-b border-white/10">
      <button 
        v-for="tab in ['All', 'My Playlists', 'Public']" 
        :key="tab"
        @click="activeTab = tab"
        class="pb-4 px-2 text-sm font-bold transition-all relative"
        :class="activeTab === tab ? 'text-white' : 'opacity-50 hover:opacity-100'"
      >
        {{ tab }}
        <div v-if="activeTab === tab" class="absolute bottom-0 left-0 right-0 h-0.5 bg-white animate-fade-in"></div>
      </button>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <div v-for="n in 8" :key="n" class="h-[250px] rounded-3xl bg-white/20 animate-pulse border border-white/30"></div>
    </div>

    <!-- Playlist Grid -->
    <div v-else>
      <div v-if="filteredPlaylists.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <router-link 
          v-for="playlist in filteredPlaylists" 
          :key="playlist.id"
          :to="`/playlists/${playlist.id}`"
          class="group relative flex flex-col liquid-glass overflow-hidden transition-all duration-300 hover:-translate-y-2"
        >
          <!-- Card Header/Image Placeholder -->
          <div class="relative h-40 bg-gradient-to-br from-indigo-500/20 to-purple-500/20 flex items-center justify-center overflow-hidden">
            <svg class="w-16 h-16 opacity-20 group-hover:scale-110 transition-transform duration-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
            </svg>
            
            <!-- Badges -->
            <div class="absolute top-3 left-3 flex gap-2">
              <span v-if="playlist.isPublic" class="px-2 py-0.5 rounded-full bg-green-500/20 text-green-400 border border-green-500/30 text-[10px] font-bold uppercase tracking-wider">Public</span>
              <span v-else class="px-2 py-0.5 rounded-full bg-white/10 text-white/50 border border-white/10 text-[10px] font-bold uppercase tracking-wider">Private</span>
            </div>

            <div v-if="playlist.rating" class="absolute top-3 right-3 px-2 py-0.5 rounded-full bg-amber-500/20 text-amber-400 border border-amber-500/30 text-[10px] font-bold flex items-center gap-1">
              <svg class="w-3 h-3 fill-current" viewBox="0 0 20 20">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
              {{ playlist.rating.toFixed(1) }}
            </div>
          </div>

          <!-- Content -->
          <div class="p-5 flex-1 flex flex-col">
            <h3 class="text-xl font-bold truncate mb-1 group-hover:text-accent transition-colors">{{ playlist.name }}</h3>
            <div class="flex items-center gap-2 text-sm opacity-60 mb-4">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
              {{ playlist.user?.username }}
            </div>
            
            <div class="mt-auto flex items-center justify-between pt-4 border-t border-white/5">
              <div class="flex gap-3 text-xs opacity-50">
                <span class="flex items-center gap-1">
                  🎬 {{ playlist.movies?.length || 0 }}
                </span>
                <span class="flex items-center gap-1">
                  🎵 {{ playlist.songs?.length || 0 }}
                </span>
              </div>
              <span class="text-[10px] opacity-30 uppercase font-bold tracking-widest">View Details →</span>
            </div>
          </div>
        </router-link>
      </div>
      
      <div v-else class="flex flex-col items-center justify-center py-20 text-center">
        <div class="w-20 h-20 rounded-full liquid-glass flex items-center justify-center mb-4" style="color: var(--text-muted)">
          <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 13h6m-3-3v6m-9 1V7a2 2 0 012-2h6l2 2h6a2 2 0 012 2v8a2 2 0 01-2 2H5a2 2 0 01-2-2z" />
          </svg>
        </div>
        <h3 class="text-xl font-bold mb-2">No playlists found</h3>
        <p class="opacity-70 mb-6">Try changing your filter or create a new one</p>
      </div>
    </div>

    <!-- Form Drawer -->
    <GlassFormDrawer
      v-model="drawerOpen"
      title="Create New Playlist"
    >
      <PlaylistForm
        v-if="drawerOpen"
        @success="onFormSuccess"
      />
    </GlassFormDrawer>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import apiClient from "../api/axios";
import PlaylistForm from "../components/PlaylistForm.vue";
import GlassFormDrawer from "../components/GlassFormDrawer.vue";
import { useUserStore } from "../store/user";

const userStore = useUserStore();
const myPlaylists = ref([]);
const publicPlaylists = ref([]);
const loading = ref(true);
const drawerOpen = ref(false);
const activeTab = ref('All');

const fetchPlaylists = async () => {
  loading.value = true;
  try {
    const [myRes, publicRes] = await Promise.all([
      apiClient.get("/playlists"),
      apiClient.get("/playlists/public")
    ]);
    myPlaylists.value = myRes.data;
    publicPlaylists.value = publicRes.data;
  } catch (error) {
    console.error("Failed to fetch playlists");
  } finally {
    loading.value = false;
  }
};

const filteredPlaylists = computed(() => {
  // Combine and remove duplicates (if my playlist is public)
  const combined = [...myPlaylists.value];
  publicPlaylists.value.forEach(p => {
    if (!combined.find(cp => cp.id === p.id)) {
      combined.push(p);
    }
  });

  if (activeTab.value === 'My Playlists') {
    return myPlaylists.value;
  } else if (activeTab.value === 'Public') {
    return publicPlaylists.value;
  }
  return combined;
});

const openCreateDrawer = () => {
  drawerOpen.value = true;
};

const onFormSuccess = () => {
  drawerOpen.value = false;
  fetchPlaylists();
};

onMounted(fetchPlaylists);
</script>
