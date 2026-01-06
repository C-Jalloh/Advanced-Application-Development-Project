<template>
  <div v-if="loading" class="flex items-center justify-center min-h-screen">
    <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-white"></div>
  </div>
  
  <div v-else-if="error" class="p-8 text-center">
    <div class="liquid-glass p-8 rounded-3xl border-red-500/50 text-red-500 inline-block">
      <h2 class="text-2xl font-bold mb-2">Error</h2>
      <p>{{ error }}</p>
      <router-link to="/music" class="glass-button mt-4 inline-block px-6 py-2">Back to Music</router-link>
    </div>
  </div>

  <div v-else-if="song" class="p-4 md:p-8 max-w-7xl mx-auto">
    <!-- Back Button -->
    <button @click="router.back()" class="glass-button px-4 py-2 mb-8 inline-flex items-center gap-2">
      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
      </svg>
      Go Back
    </button>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
      <!-- Left Column: Album Art & Quick Info -->
      <div class="lg:col-span-1">
        <div class="liquid-glass rounded-[3rem] overflow-hidden shadow-2xl border border-white/20 sticky top-8">
          <img 
            :src="song.imageUrl ? (song.imageUrl.startsWith('http') ? song.imageUrl : `http://127.0.0.1:8080/images/${song.imageUrl}`) : 'https://placehold.co/400x400?text=No+Album+Art'" 
            class="w-full aspect-square object-cover"
            @error="(e) => e.target.src = 'https://placehold.co/400x400?text=No+Album+Art'"
          />
          <div class="p-8 space-y-6">
            <button 
              @click="showPlaylistModal = true"
              class="glass-button w-full py-3 flex items-center justify-center gap-2 !text-white"
              style="background-color: var(--accent-color)"
            >
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
              </svg>
              Add to Playlist
            </button>

            <div class="flex items-center justify-between">
              <div class="text-center flex-1">
                <p class="text-sm opacity-60 uppercase tracking-widest mb-1">Rating</p>
                <div class="flex items-center justify-center gap-2 text-2xl font-bold text-amber-400">
                  <svg class="w-6 h-6 fill-current" viewBox="0 0 20 20">
                    <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                  </svg>
                  {{ song.rating ? song.rating.toFixed(1) : 'N/A' }}
                </div>
              </div>
              <div class="w-px h-12 bg-white/10"></div>
              <div class="text-center flex-1">
                <p class="text-sm opacity-60 uppercase tracking-widest mb-1">Year</p>
                <p class="text-2xl font-bold">{{ song.releaseYear || 'N/A' }}</p>
              </div>
            </div>

            <div class="space-y-4 pt-6 border-t border-white/10">
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-xl liquid-glass flex items-center justify-center">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
                  </svg>
                </div>
                <div>
                  <p class="text-xs opacity-50">Artist</p>
                  <p class="font-semibold">{{ song.artist }}</p>
                </div>
              </div>
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-xl liquid-glass flex items-center justify-center">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 10h16M4 14h16M4 18h16" />
                  </svg>
                </div>
                <div>
                  <p class="text-xs opacity-50">Album</p>
                  <p class="font-semibold">{{ song.albumEntity ? song.albumEntity.title : song.album }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Column: Details & Reviews -->
      <div class="lg:col-span-2 space-y-8">
        <div class="liquid-glass p-8 md:p-12 rounded-[3rem] border border-white/20 shadow-xl">
          <div class="flex flex-wrap gap-2 mb-6">
            <span 
              v-for="genre in song.genres" 
              :key="genre.id"
              class="px-4 py-1.5 rounded-full bg-white/10 text-sm font-bold border border-white/20"
            >
              {{ genre.name }}
            </span>
            <span v-if="!song.genres || !song.genres.length" class="px-4 py-1.5 rounded-full bg-white/10 text-sm font-bold border border-white/20">
              {{ song.genre }}
            </span>
          </div>
          
          <h1 class="text-4xl md:text-6xl font-black mb-4 leading-tight">{{ song.title }}</h1>
          <p class="text-2xl opacity-70 mb-8">by {{ song.artist }}</p>
          
          <div class="grid grid-cols-1 md:grid-cols-2 gap-8 mt-12">
            <div class="liquid-glass p-6 rounded-2xl border border-white/10">
              <h3 class="text-sm opacity-50 uppercase tracking-widest mb-4">Track Info</h3>
              <div class="space-y-3">
                <div class="flex justify-between">
                  <span class="opacity-60">Duration</span>
                  <span class="font-mono">{{ Math.floor(song.duration / 60) }}:{{ (song.duration % 60).toString().padStart(2, '0') }}</span>
                </div>
                <div class="flex justify-between">
                  <span class="opacity-60">Release Year</span>
                  <span>{{ song.releaseYear }}</span>
                </div>
              </div>
            </div>
            
            <div v-if="song.albumEntity" class="liquid-glass p-6 rounded-2xl border border-white/10">
              <h3 class="text-sm opacity-50 uppercase tracking-widest mb-4">Album Details</h3>
              <div class="space-y-3">
                <div class="flex justify-between">
                  <span class="opacity-60">Title</span>
                  <span>{{ song.albumEntity.title }}</span>
                </div>
                <div class="flex justify-between">
                  <span class="opacity-60">Artist</span>
                  <span>{{ song.artist }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Reviews Section -->
        <div class="liquid-glass p-8 md:p-12 rounded-[3rem] border border-white/20 shadow-xl">
          <h2 class="text-3xl font-bold mb-8">Listener Reviews</h2>
          <Reviews :contentId="song.id" contentType="MUSIC" @review-updated="fetchSong" />
        </div>
      </div>
    </div>

    <!-- Add to Playlist Modal -->
    <AddToPlaylistModal
      v-model="showPlaylistModal"
      :item="song"
      itemType="music"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import apiClient from "../api/axios";
import Reviews from "../components/Reviews.vue";
import AddToPlaylistModal from "../components/AddToPlaylistModal.vue";

const route = useRoute();
const router = useRouter();
const song = ref(null);
const loading = ref(true);
const error = ref("");
const showPlaylistModal = ref(false);

const fetchSong = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get(`/music/${route.params.id}`);
    song.value = response.data;
  } catch (err) {
    console.error("Error fetching music details:", err);
    error.value = "Could not load music details.";
  } finally {
    loading.value = false;
  }
};

onMounted(fetchSong);
</script>
