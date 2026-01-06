<template>
  <div v-if="loading" class="flex items-center justify-center min-h-screen">
    <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-white"></div>
  </div>
  
  <div v-else-if="error" class="p-8 text-center">
    <div class="liquid-glass p-8 rounded-3xl border-red-500/50 text-red-500 inline-block">
      <h2 class="text-2xl font-bold mb-2">Error</h2>
      <p>{{ error }}</p>
      <router-link to="/movies" class="glass-button mt-4 inline-block px-6 py-2">Back to Movies</router-link>
    </div>
  </div>

  <div v-else-if="movie" class="p-4 md:p-8 max-w-7xl mx-auto">
    <!-- Back Button -->
    <button @click="router.back()" class="glass-button px-4 py-2 mb-8 inline-flex items-center gap-2">
      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
      </svg>
      Go Back
    </button>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
      <!-- Left Column: Poster & Quick Info -->
      <div class="lg:col-span-1">
        <div class="liquid-glass rounded-[3rem] overflow-hidden shadow-2xl border border-white/20 sticky top-8">
          <img 
            :src="movie.imageUrl ? (movie.imageUrl.startsWith('http') ? movie.imageUrl : `http://127.0.0.1:8080/images/${movie.imageUrl}`) : 'https://placehold.co/400x600?text=No+Poster'" 
            class="w-full aspect-[2/3] object-cover"
            @error="(e) => e.target.src = 'https://placehold.co/400x600?text=No+Poster'"
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
                <div class="flex items-center justify-center gap-2 text-2xl font-bold !text-white">
                  <svg class="w-6 h-6 fill-current !text-white" viewBox="0 0 20 20">
                    <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                  </svg>
                  {{ movie.rating ? movie.rating.toFixed(1) : 'N/A' }}
                </div>
              </div>
              <div class="w-px h-12 bg-white/10"></div>
              <div class="text-center flex-1">
                <p class="text-sm opacity-60 uppercase tracking-widest mb-1">Year</p>
                <p class="text-2xl font-bold">{{ movie.releaseYear || 'N/A' }}</p>
              </div>
            </div>

            <div class="space-y-4 pt-6 border-t border-white/10">
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-xl liquid-glass flex items-center justify-center">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                </div>
                <div>
                  <p class="text-xs opacity-50">Duration</p>
                  <p class="font-semibold">{{ movie.duration }} minutes</p>
                </div>
              </div>
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-xl liquid-glass flex items-center justify-center">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                </div>
                <div>
                  <p class="text-xs opacity-50">Director</p>
                  <p class="font-semibold">{{ movie.directorEntity ? movie.directorEntity.name : movie.director }}</p>
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
              v-for="genre in movie.genres" 
              :key="genre.id"
              class="px-4 py-1.5 rounded-full bg-white/10 text-sm font-bold border border-white/20"
            >
              {{ genre.name }}
            </span>
          </div>
          
          <h1 class="text-4xl md:text-6xl font-black mb-6 leading-tight">{{ movie.title }}</h1>
          
          <div class="prose prose-invert max-w-none">
            <h2 class="text-xl font-bold mb-4 opacity-60 uppercase tracking-widest">Overview</h2>
            <p class="text-lg leading-relaxed opacity-80">{{ movie.description }}</p>
          </div>

          <div v-if="movie.actors && movie.actors.length" class="mt-12">
            <h2 class="text-xl font-bold mb-6 opacity-60 uppercase tracking-widest">Cast</h2>
            <div class="flex flex-wrap gap-4">
              <div v-for="actor in movie.actors" :key="actor.id" class="flex items-center gap-3 liquid-glass px-4 py-2 rounded-2xl border border-white/10">
                <div class="w-8 h-8 rounded-full bg-white/20 flex items-center justify-center font-bold text-xs">
                  {{ actor.name.charAt(0) }}
                </div>
                <span class="font-medium">{{ actor.name }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Reviews Section -->
        <div class="liquid-glass p-8 md:p-12 rounded-[3rem] border border-white/20 shadow-xl">
          <h2 class="text-3xl font-bold mb-8">Community Reviews</h2>
          <Reviews :contentId="movie.id" contentType="MOVIE" @review-updated="fetchMovie" />
        </div>
      </div>
    </div>

    <!-- Add to Playlist Modal -->
    <AddToPlaylistModal
      v-model="showPlaylistModal"
      :item="movie"
      itemType="movie"
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
const movie = ref(null);
const loading = ref(true);
const error = ref("");
const showPlaylistModal = ref(false);

const fetchMovie = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get(`/movies/${route.params.id}`);
    movie.value = response.data;
  } catch (err) {
    console.error("Error fetching movie details:", err);
    error.value = "Could not load movie details.";
  } finally {
    loading.value = false;
  }
};

onMounted(fetchMovie);
</script>
