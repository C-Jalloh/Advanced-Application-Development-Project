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
          <h1 class="text-2xl md:text-3xl font-bold truncate">Music</h1>
          <p class="text-sm opacity-70 truncate">Your personal soundtrack collection</p>
        </div>
      </div>
      <div class="flex gap-3">
        <FilterPills
          v-model="filters"
          :categories="filterCategories"
          @filter="applyFilters"
        />
        <button 
          @click="openCreateDrawer"
          class="glass-button-primary px-6 md:px-8 py-2 md:py-2.5"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          Add Music
        </button>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <div v-for="n in 8" :key="n" class="h-[400px] rounded-3xl bg-white/20 animate-pulse border border-white/30"></div>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="p-4 rounded-2xl liquid-glass border-red-500/50 text-red-600 flex items-center gap-3">
      <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
      {{ error }}
    </div>

    <!-- Music Grid -->
    <div v-else>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <div 
          v-for="song in music" 
          :key="song.id"
          class="group relative flex flex-col liquid-glass overflow-hidden transition-all duration-300 hover:-translate-y-2"
        >
          <!-- Image Container -->
          <div class="relative h-[200px] overflow-hidden">
            <img 
              :src="song.imageUrl ? (song.imageUrl.startsWith('http') ? song.imageUrl : `http://127.0.0.1:8080/images/${song.imageUrl}`) : 'https://placehold.co/400x400?text=No+Album+Art'" 
              class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
              @error="(e) => e.target.src = 'https://placehold.co/400x400?text=No+Album+Art'"
            />
            <div class="absolute inset-0 bg-black/40 backdrop-blur-[2px] opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-center justify-center p-4">
              <div class="flex flex-col gap-2 w-full max-w-[150px]">
                <router-link 
                  :to="`/music/${song.id}`"
                  class="glass-button text-center py-2 text-sm !text-white"
                >
                  View Details
                </router-link>
                <button 
                  @click="openAddToPlaylist(song)"
                  class="glass-button py-2 text-sm !text-white flex items-center justify-center gap-2"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                  </svg>
                  Add to Playlist
                </button>
                <div class="flex gap-2">
                  <button 
                    @click="openEditDrawer(song)"
                    class="glass-button flex-1 py-2 text-sm !text-white"
                  >
                    Edit
                  </button>
                  <button 
                    @click="deleteMusic(song)"
                    class="glass-button p-2 !text-white hover:!text-red-500"
                  >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Content -->
          <div class="p-5 flex-1 flex flex-col">
            <h3 class="text-xl font-bold truncate mb-1">{{ song.title }}</h3>
            <div class="flex items-center gap-2 font-medium text-sm mb-3 opacity-90">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
              {{ song.artist }}
            </div>
            
            <div class="flex flex-wrap gap-2 mb-4">
              <template v-if="song.genres && song.genres.length">
                <span 
                  v-for="g in song.genres" 
                  :key="g.id"
                  class="px-2 py-1 rounded-lg bg-white/10 text-xs font-bold border border-white/20"
                >
                  {{ g.name }}
                </span>
              </template>
              <span v-else class="px-2 py-1 rounded-lg bg-white/10 text-xs font-bold border border-white/20">
                {{ song.genre }}
              </span>
              
              <span class="px-2 py-1 rounded-lg bg-white/10 text-xs font-bold border border-white/20">
                {{ song.albumEntity ? song.albumEntity.title : song.album }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- Pagination Controls -->
      <div class="mt-12 flex items-center justify-center gap-4">
        <button 
          @click="changePage(currentPage - 1)"
          :disabled="currentPage === 0"
          class="glass-button px-6 py-2 disabled:opacity-30 disabled:cursor-not-allowed"
        >
          Previous
        </button>
        <div class="flex items-center gap-2">
          <span class="text-sm opacity-60">Page</span>
          <span class="font-bold">{{ currentPage + 1 }}</span>
          <span class="text-sm opacity-60">of</span>
          <span class="font-bold">{{ totalPages }}</span>
        </div>
        <button 
          @click="changePage(currentPage + 1)"
          :disabled="currentPage >= totalPages - 1"
          class="glass-button px-6 py-2 disabled:opacity-30 disabled:cursor-not-allowed"
        >
          Next
        </button>
      </div>
    </div>

    <!-- Form Drawer -->
    <GlassFormDrawer
      v-model="drawerOpen"
      :title="isEdit ? 'Edit Music' : 'Add New Music'"
    >
      <MusicForm
        v-if="drawerOpen"
        :initialData="selectedMusic"
        :isEdit="isEdit"
        @success="onFormSuccess"
      />
    </GlassFormDrawer>

    <!-- Confirmation Modal -->
    <ConfirmationModal
      v-model="showDeleteModal"
      title="Delete Music"
      :message="`Are you sure you want to delete '${musicToDelete?.title}'? This action cannot be undone.`"
      confirmText="Delete"
      type="danger"
      @confirm="confirmDelete"
    />

    <!-- Add to Playlist Modal -->
    <AddToPlaylistModal
      v-model="showPlaylistModal"
      :item="selectedMusicForPlaylist"
      itemType="music"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch, inject } from 'vue';
import apiClient from "../api/axios";
import Reviews from "../components/Reviews.vue";
import GlassFormDrawer from "../components/GlassFormDrawer.vue";
import MusicForm from "../components/MusicForm.vue";
import ConfirmationModal from "../components/ConfirmationModal.vue";
import FilterPills from "../components/FilterPills.vue";
import AddToPlaylistModal from "../components/AddToPlaylistModal.vue";

const music = ref([]);
const loading = ref(true);
const error = ref("");
const drawerOpen = ref(false);
const isEdit = ref(false);
const selectedMusic = ref(null);

// Pagination State
const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = ref(12);

// Delete Modal State
const showDeleteModal = ref(false);
const musicToDelete = ref(null);

// Playlist Modal State
const showPlaylistModal = ref(false);
const selectedMusicForPlaylist = ref(null);

// Filter State (using object for FilterPills component)
const filters = ref({});
const genres = ref([]);
const artists = ref([]);

// Inject global search from MainLayout
const globalSearchQuery = inject('globalSearchQuery', ref(''));
let searchTimeout = null;

// Computed filter categories for FilterPills
const filterCategories = computed(() => {
  return [
    {
      key: 'genre',
      label: 'Genre',
      options: [
        { label: 'All Genres', value: '' },
        ...genres.value.map(g => ({ label: g, value: g }))
      ]
    },
    {
      key: 'artist',
      label: 'Artist',
      options: [
        { label: 'All Artists', value: '' },
        ...artists.value.map(a => ({ label: a, value: a }))
      ]
    }
  ];
});

const hasActiveFilters = computed(() => {
  return Object.keys(filters.value).some(key => {
    const value = filters.value[key];
    return value !== null && value !== '' && value !== undefined;
  });
});

const fetchMusic = async () => {
  loading.value = true;
  error.value = "";
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value
    };
    
    // Add search from global search bar
    if (globalSearchQuery.value) {
      params.query = globalSearchQuery.value;
    }
    
    // Add filter parameters
    if (filters.value.genre) params.genre = filters.value.genre;
    if (filters.value.artist) params.artist = filters.value.artist;

    const response = await apiClient.get("/music", { params });
    console.log("Music API Response:", response.data);
    // Spring Data Page object returns content in 'content' field
    music.value = response.data.content.map(s => ({ ...s, showReviews: false }));
    totalPages.value = response.data.totalPages;
  } catch (err) {
    console.error("Music Fetch Error:", err);
    if (err.response) {
      console.error("Error Data:", err.response.data);
      error.value = `Failed to fetch music: ${err.response.data.message || err.response.statusText}`;
    } else {
      error.value = "Failed to fetch music: Network error or server is down";
    }
  } finally {
    loading.value = false;
  }
};

const fetchMetadata = async () => {
  try {
    const [genresResponse, artistsResponse] = await Promise.all([
      apiClient.get("/music/genres"),
      apiClient.get("/music/artists")
    ]);
    genres.value = genresResponse.data;
    artists.value = artistsResponse.data;
  } catch (err) {
    console.error("Failed to fetch metadata:", err);
  }
};

// Watch global search query
watch(globalSearchQuery, () => {
  clearTimeout(searchTimeout);
  searchTimeout = setTimeout(() => {
    currentPage.value = 0;
    fetchMusic();
  }, 300);
});

const applyFilters = () => {
  currentPage.value = 0;
  fetchMusic();
};

const changePage = (page) => {
  if (page >= 0 && page < totalPages.value) {
    currentPage.value = page;
    fetchMusic();
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
};

const openCreateDrawer = () => {
  isEdit.value = false;
  selectedMusic.value = {
    title: "",
    artist: "",
    album: "",
    genre: "",
    releaseYear: null,
    imageUrl: ""
  };
  drawerOpen.value = true;
};

const openEditDrawer = (song) => {
  isEdit.value = true;
  selectedMusic.value = { ...song };
  drawerOpen.value = true;
};

const openAddToPlaylist = (song) => {
  selectedMusicForPlaylist.value = song;
  showPlaylistModal.value = true;
};

const onFormSuccess = () => {
  drawerOpen.value = false;
  fetchMusic();
  fetchMetadata(); // Refresh metadata in case new genres/artists were added
};

const deleteMusic = (song) => {
  musicToDelete.value = song;
  showDeleteModal.value = true;
};

const confirmDelete = async () => {
  if (!musicToDelete.value) return;
  try {
    await apiClient.delete(`/music/${musicToDelete.value.id}`);
    fetchMusic();
    fetchMetadata(); // Refresh metadata in case deleted music affected genres/artists
  } catch (err) {
    console.error("Delete failed", err);
  } finally {
    musicToDelete.value = null;
  }
};

onMounted(() => {
  fetchMetadata();
  fetchMusic();
});
</script>

<style scoped>
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.animate-fade-in {
  animation: fadeIn 0.3s ease-out forwards;
}
</style>
