<template>
  <div v-if="loading" class="flex items-center justify-center min-h-screen">
    <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-white"></div>
  </div>
  
  <div v-else-if="error" class="p-8 text-center">
    <div class="liquid-glass p-8 rounded-3xl border-red-500/50 text-red-500 inline-block">
      <h2 class="text-2xl font-bold mb-2">Error</h2>
      <p>{{ error }}</p>
      <router-link to="/playlists" class="glass-button mt-4 inline-block px-6 py-2">Back to Playlists</router-link>
    </div>
  </div>

  <div v-else-if="playlist" class="p-4 md:p-8 max-w-7xl mx-auto">
    <!-- Back Button -->
    <router-link to="/playlists" class="glass-button px-4 py-2 mb-8 inline-flex items-center gap-2">
      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
      </svg>
      Back to Playlists
    </router-link>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
      <!-- Left Column: Playlist Info -->
      <div class="lg:col-span-1">
        <div class="liquid-glass rounded-[3rem] overflow-hidden shadow-2xl border border-white/20 sticky top-8">
          <div class="aspect-square bg-gradient-to-br from-indigo-500/20 to-purple-500/20 flex items-center justify-center relative">
            <svg class="w-32 h-32 opacity-20" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
            </svg>
            <div class="absolute inset-0 flex flex-col items-center justify-center p-6 text-center">
              <h1 class="text-3xl font-black mb-2">{{ playlist.name }}</h1>
              <div class="flex items-center gap-2 opacity-70">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
                <span class="font-medium">{{ playlist.user?.username }}</span>
              </div>
            </div>
          </div>
          
          <div class="p-8 space-y-6">
            <div class="flex items-center justify-between">
              <div class="text-center flex-1">
                <p class="text-sm opacity-60 uppercase tracking-widest mb-1">Rating</p>
                <div class="flex items-center justify-center gap-2 text-2xl font-bold !text-white">
                  <svg class="w-6 h-6 fill-current text-amber-400" viewBox="0 0 20 20">
                    <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                  </svg>
                  {{ playlist.rating ? playlist.rating.toFixed(1) : '0.0' }}
                </div>
              </div>
              <div class="w-px h-12 bg-white/10"></div>
              <div class="text-center flex-1">
                <p class="text-sm opacity-60 uppercase tracking-widest mb-1">Status</p>
                <span v-if="playlist.isPublic" class="text-sm px-3 py-1 rounded-full bg-green-500/20 text-green-400 border border-green-500/30 font-bold uppercase tracking-wider">Public</span>
                <span v-else class="text-sm px-3 py-1 rounded-full bg-white/10 text-white/50 border border-white/10 font-bold uppercase tracking-wider">Private</span>
              </div>
            </div>

            <div class="space-y-4 pt-6 border-t border-white/10">
              <div class="flex items-center justify-between text-sm">
                <span class="opacity-60">Movies</span>
                <span class="font-bold">{{ playlist.movies?.length || 0 }}</span>
              </div>
              <div class="flex items-center justify-between text-sm">
                <span class="opacity-60">Music</span>
                <span class="font-bold">{{ playlist.songs?.length || 0 }}</span>
              </div>
              <div class="flex items-center justify-between text-sm">
                <span class="opacity-60">Created</span>
                <span class="font-bold">{{ formatDate(playlist.createdAt) }}</span>
              </div>
            </div>

            <div v-if="isOwner" class="pt-6">
              <button 
                @click="showEditDrawer = true"
                class="glass-button w-full py-3 flex items-center justify-center gap-2"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                </svg>
                Manage Playlist
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Column: Content & Reviews -->
      <div class="lg:col-span-2 space-y-8">
        <!-- Playlist Content -->
        <div class="liquid-glass p-8 md:p-10 rounded-[3rem] border border-white/20 shadow-xl overflow-hidden relative">
          <div class="absolute top-0 right-0 w-64 h-64 bg-accent-color/5 blur-[100px] -z-10"></div>
          
          <h2 class="text-3xl font-bold mb-8 flex items-center gap-3">
            <span class="p-3 rounded-2xl bg-white/10 shadow-inner">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
              </svg>
            </span>
            Playlist Content
          </h2>
          
          <div class="space-y-12">
            <!-- Movies Section -->
            <div v-if="playlist.movies && playlist.movies.length > 0">
              <div class="flex items-center justify-between mb-6">
                <h3 class="text-xl font-bold flex items-center gap-3">
                  <span class="text-2xl">🎬</span>
                  Movies
                </h3>
                <span class="px-3 py-1 rounded-full bg-white/5 border border-white/10 text-xs font-medium opacity-60">
                  {{ playlist.movies.length }} {{ playlist.movies.length === 1 ? 'film' : 'films' }}
                </span>
              </div>
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-5">
                <router-link 
                  v-for="movie in playlist.movies" 
                  :key="movie.id"
                  :to="`/movies/${movie.id}`"
                  class="group relative flex items-center gap-5 p-4 rounded-[2rem] bg-white/5 border border-white/10 hover:bg-white/10 hover:border-white/20 hover:translate-y-[-4px] transition-all duration-300 shadow-lg hover:shadow-accent-color/5"
                >
                  <div class="w-16 h-24 rounded-2xl overflow-hidden shadow-2xl flex-shrink-0 group-hover:scale-105 transition-transform duration-500">
                    <img 
                      :src="movie.imageUrl ? (movie.imageUrl.startsWith('http') ? movie.imageUrl : `http://127.0.0.1:8080/images/${movie.imageUrl}`) : 'https://placehold.co/40x60?text=🎬'" 
                      class="w-full h-full object-cover" 
                      @error="(e) => e.target.src = 'https://placehold.co/40x60?text=🎬'" 
                    />
                  </div>
                  <div class="min-w-0 flex-1">
                    <p class="text-lg font-bold truncate group-hover:text-accent-color transition-colors">{{ movie.title }}</p>
                    <div class="flex items-center gap-2 mt-1 opacity-60 text-sm">
                      <span>{{ movie.releaseYear }}</span>
                      <span class="w-1 h-1 rounded-full bg-white/40"></span>
                      <span class="truncate">{{ movie.genre }}</span>
                    </div>
                  </div>
                  <div class="opacity-0 group-hover:opacity-100 transition-opacity pr-2">
                    <svg class="w-5 h-5 text-accent-color" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                    </svg>
                  </div>
                </router-link>
              </div>
            </div>

            <!-- Music Section -->
            <div v-if="playlist.songs && playlist.songs.length > 0">
              <div class="flex items-center justify-between mb-6">
                <h3 class="text-xl font-bold flex items-center gap-3">
                  <span class="text-2xl">🎵</span>
                  Music
                </h3>
                <span class="px-3 py-1 rounded-full bg-white/5 border border-white/10 text-xs font-medium opacity-60">
                  {{ playlist.songs.length }} {{ playlist.songs.length === 1 ? 'track' : 'tracks' }}
                </span>
              </div>
              <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <router-link 
                  v-for="song in playlist.songs" 
                  :key="song.id"
                  :to="`/music/${song.id}`"
                  class="group flex items-center gap-4 p-4 rounded-[1.5rem] bg-white/5 border border-white/10 hover:bg-white/10 hover:border-white/20 hover:scale-[1.02] transition-all duration-300"
                >
                  <div class="w-12 h-12 rounded-2xl bg-gradient-to-br from-indigo-500/30 to-purple-500/30 flex items-center justify-center flex-shrink-0 group-hover:rotate-12 transition-transform shadow-lg overflow-hidden">
                    <img 
                      v-if="song.imageUrl"
                      :src="song.imageUrl.startsWith('http') ? song.imageUrl : `http://127.0.0.1:8080/images/${song.imageUrl}`"
                      class="w-full h-full object-cover"
                      @error="(e) => e.target.src = ''"
                    />
                    <span v-else class="text-2xl">🎵</span>
                  </div>
                  <div class="min-w-0 flex-1">
                    <p class="font-bold truncate group-hover:text-accent-color transition-colors">{{ song.title }}</p>
                    <p class="text-sm opacity-60 truncate">{{ song.artist }}</p>
                  </div>
                  <svg class="w-5 h-5 opacity-0 group-hover:opacity-60 transition-opacity" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
                  </svg>
                </router-link>
              </div>
            </div>

            <div v-if="(!playlist.movies || playlist.movies.length === 0) && (!playlist.songs || playlist.songs.length === 0)" class="text-center py-20 bg-white/5 rounded-[3rem] border border-dashed border-white/10">
              <div class="mb-4 text-4xl opacity-20">📁</div>
              <p class="text-lg opacity-40 font-medium italic">Your playlist canvas is blank</p>
              <p class="text-sm opacity-30 mt-1">Start adding movies and music to bring it to life</p>
            </div>
          </div>
        </div>

        <!-- Reviews Section -->
        <div class="liquid-glass p-8 md:p-12 rounded-[3rem] border border-white/20 shadow-xl">
          <h2 class="text-3xl font-bold mb-8">Community Reviews</h2>
          <Reviews :contentId="playlist.id" contentType="PLAYLIST" @review-updated="fetchPlaylist" />
        </div>
      </div>
    </div>

    <!-- Manage Drawer (Reuse PlaylistDetail component logic) -->
    <GlassFormDrawer
      v-model="showEditDrawer"
      title="Manage Playlist"
    >
      <div class="p-4">
        <PlaylistDetail
          v-if="playlist"
          :playlist="playlist"
          :allSongs="allSongs"
          :allMovies="allMovies"
          @playlist-updated="onPlaylistUpdated"
        />
      </div>
    </GlassFormDrawer>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import apiClient from "../api/axios";
import Reviews from "../components/Reviews.vue";
import PlaylistDetail from "../components/PlaylistDetail.vue";
import GlassFormDrawer from "../components/GlassFormDrawer.vue";
import { useUserStore } from "../store/user";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const playlist = ref(null);
const allSongs = ref([]);
const allMovies = ref([]);
const loading = ref(true);
const error = ref("");
const showEditDrawer = ref(false);

const isOwner = computed(() => {
  return playlist.value?.user?.username === userStore.user?.username;
});

const fetchPlaylist = async () => {
  try {
    const response = await apiClient.get(`/playlists/${route.params.id}`);
    playlist.value = response.data;
  } catch (err) {
    console.error("Failed to fetch playlist:", err);
    error.value = err.response?.data?.message || "Failed to load playlist";
  }
};

const fetchAllContent = async () => {
  try {
    const [songsRes, moviesRes] = await Promise.all([
      apiClient.get('/music?size=1000'),
      apiClient.get('/movies?size=1000')
    ]);
    allSongs.value = songsRes.data.content || [];
    allMovies.value = moviesRes.data.content || [];
  } catch (err) {
    console.error("Failed to fetch content for management:", err);
  }
};

const init = async () => {
  loading.value = true;
  await Promise.all([fetchPlaylist(), fetchAllContent()]);
  loading.value = false;
};

const onPlaylistUpdated = (action) => {
  if (action === 'deleted') {
    router.push('/playlists');
  } else {
    fetchPlaylist();
  }
};

const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

onMounted(init);
</script>
