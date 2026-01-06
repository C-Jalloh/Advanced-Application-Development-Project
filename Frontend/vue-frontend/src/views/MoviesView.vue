<template>
  <div class="p-4 md:p-6">
    <!-- Header Section -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between mb-8 gap-4">
      <div class="flex items-center gap-3 md:gap-4">
        <div class="w-12 h-12 md:w-16 md:h-16 rounded-xl md:rounded-2xl liquid-glass flex items-center justify-center shadow-inner flex-shrink-0" style="color: var(--text-main)">
          <svg class="w-6 h-6 md:w-8 md:h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 4v16M17 4v16M3 8h4m10 0h4M3 12h18M3 16h4m10 0h4M4 20h16a1 1 0 001-1V5a1 1 0 00-1-1H4a1 1 0 00-1 1v14a1 1 0 001 1z" />
          </svg>
        </div>
        <div class="min-w-0">
          <h1 class="text-2xl md:text-3xl font-bold truncate">Movies</h1>
          <p class="text-sm opacity-70 truncate">Explore and manage your collection</p>
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
          Add Movie
        </button>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <div v-for="n in 8" :key="n" class="h-[450px] rounded-3xl bg-white/20 animate-pulse border border-white/30"></div>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="p-4 rounded-2xl liquid-glass border-red-500/50 text-red-600 flex items-center gap-3">
      <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
      {{ error }}
    </div>

    <!-- Movie Grid -->
    <div v-else>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <div 
          v-for="movie in movies" 
          :key="movie.id"
          class="group relative flex flex-col liquid-glass overflow-hidden transition-all duration-300 hover:-translate-y-2"
        >
          <!-- Image Container -->
          <div class="relative h-[300px] overflow-hidden">
            <img 
              :src="movie.imageUrl ? (movie.imageUrl.startsWith('http') ? movie.imageUrl : `http://127.0.0.1:8080/images/${movie.imageUrl}`) : 'https://placehold.co/400x600?text=No+Poster'" 
              class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
              @error="(e) => e.target.src = 'https://placehold.co/400x600?text=No+Poster'"
            />
            <div class="absolute inset-0 bg-black/40 backdrop-blur-[2px] opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-center justify-center p-4">
              <div class="flex flex-col gap-2 w-full max-w-[150px]">
                <router-link 
                  :to="`/movies/${movie.id}`"
                  class="glass-button text-center py-2 text-sm !text-white"
                >
                  View Details
                </router-link>
                <button 
                  @click="openAddToPlaylist(movie)"
                  class="glass-button py-2 text-sm !text-white flex items-center justify-center gap-2"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                  </svg>
                  Add to Playlist
                </button>
                <div class="flex gap-2">
                  <button 
                    @click="openEditDrawer(movie)"
                    class="glass-button flex-1 py-2 text-sm !text-white"
                  >
                    Edit
                  </button>
                  <button 
                    @click="deleteMovie(movie)"
                    class="glass-button p-2 !text-white hover:!text-red-500"
                  >
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
            <div class="absolute top-3 left-3 flex flex-wrap gap-1">
              <span 
                v-if="movie.genres && movie.genres.length"
                v-for="g in movie.genres.slice(0, 2)"
                :key="g.id"
                class="px-3 py-1 rounded-full backdrop-blur-md text-white text-xs font-bold border border-white/20" 
                style="background-color: var(--accent-color)"
              >
                {{ g.name }}
              </span>
              <span 
                v-else
                class="px-3 py-1 rounded-full backdrop-blur-md text-white text-xs font-bold border border-white/20" 
                style="background-color: var(--accent-color)"
              >
                {{ movie.genre }}
              </span>
            </div>
            <div v-if="movie.voteAverage" class="absolute top-3 right-3 px-3 py-1 rounded-full backdrop-blur-md !text-white text-xs font-bold border border-white/20 flex items-center gap-1" style="background-color: var(--accent-color)">
              <svg class="w-3 h-3 fill-current !text-white" viewBox="0 0 20 20">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
              <span class="!text-white">{{ movie.voteAverage.toFixed(1) }}</span>
            </div>
          </div>

          <!-- Content -->
          <div class="p-5 flex-1 flex flex-col">
            <div class="flex justify-between items-start mb-2">
              <h3 class="text-xl font-bold truncate">{{ movie.title }}</h3>
              <span class="text-sm font-semibold opacity-80">{{ movie.releaseYear }}</span>
            </div>
            <p class="text-sm line-clamp-2 mb-4 flex-1 opacity-70">{{ movie.description }}</p>
            
            <div class="flex items-center justify-between">
              <div class="flex items-center gap-2 text-xs opacity-60">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
                {{ movie.directorEntity ? movie.directorEntity.name : movie.director }}
              </div>
              <div v-if="movie.duration" class="text-xs opacity-60 flex items-center gap-1">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                {{ movie.duration }} min
              </div>
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
      :title="isEdit ? 'Edit Movie' : 'Add New Movie'"
    >
      <MovieForm
        v-if="drawerOpen"
        :initialData="selectedMovie"
        :isEdit="isEdit"
        @success="onFormSuccess"
      />
    </GlassFormDrawer>

    <!-- Confirmation Modal -->
    <ConfirmationModal
      v-model="showDeleteModal"
      title="Delete Movie"
      :message="`Are you sure you want to delete '${movieToDelete?.title}'? This action cannot be undone.`"
      confirmText="Delete"
      type="danger"
      @confirm="confirmDelete"
    />

    <!-- Add to Playlist Modal -->
    <AddToPlaylistModal
      v-model="showPlaylistModal"
      :item="selectedMovieForPlaylist"
      itemType="movie"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch, inject } from 'vue';
import apiClient from "../api/axios";
import Reviews from "../components/Reviews.vue";
import GlassFormDrawer from "../components/GlassFormDrawer.vue";
import MovieForm from "../components/MovieForm.vue";
import ConfirmationModal from "../components/ConfirmationModal.vue";
import FilterPills from "../components/FilterPills.vue";
import AddToPlaylistModal from "../components/AddToPlaylistModal.vue";

const movies = ref([]);
const loading = ref(true);
const error = ref("");
const drawerOpen = ref(false);
const isEdit = ref(false);
const selectedMovie = ref(null);

// Pagination State
const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = ref(12);

// Delete Modal State
const showDeleteModal = ref(false);
const movieToDelete = ref(null);

// Playlist Modal State
const showPlaylistModal = ref(false);
const selectedMovieForPlaylist = ref(null);

// Filter State (using object for FilterPills component)
const filters = ref({});
const genres = ref([]);
const years = ref([]);

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
      key: 'year',
      label: 'Year',
      options: [
        { label: 'All Years', value: null },
        ...years.value.map(y => ({ label: y.toString(), value: y }))
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

const fetchMovies = async () => {
  loading.value = true;
  error.value = "";
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value
    };
    
    // Add search from global search bar
    if (globalSearchQuery.value) {
      params.title = globalSearchQuery.value;
    }
    
    // Add filter parameters
    if (filters.value.genre) params.genre = filters.value.genre;
    if (filters.value.year) params.year = filters.value.year;

    const response = await apiClient.get("/movies", { params });
    console.log("Movies API Response:", response.data);
    if (response.data && response.data.content) {
      movies.value = response.data.content.map(m => ({ ...m, showReviews: false }));
      totalPages.value = response.data.totalPages;
    } else {
      throw new Error("Invalid response format from server");
    }
  } catch (err) {
    console.error("Movies Fetch Error:", err);
    if (err.response) {
      error.value = `Failed to fetch movies: ${err.response.data.message || err.response.statusText}`;
    } else {
      error.value = "Failed to fetch movies: Network error or server is down";
    }
  } finally {
    loading.value = false;
  }
};

const fetchMetadata = async () => {
  try {
    const [genresResponse, yearsResponse] = await Promise.all([
      apiClient.get("/movies/genres"),
      apiClient.get("/movies/years")
    ]);
    genres.value = genresResponse.data;
    years.value = yearsResponse.data;
  } catch (err) {
    console.error("Failed to fetch metadata:", err);
  }
};

// Watch global search query
watch(globalSearchQuery, () => {
  clearTimeout(searchTimeout);
  searchTimeout = setTimeout(() => {
    currentPage.value = 0;
    fetchMovies();
  }, 300);
});

const applyFilters = () => {
  currentPage.value = 0;
  fetchMovies();
};

const changePage = (page) => {
  currentPage.value = page;
  fetchMovies();
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const openCreateDrawer = () => {
  isEdit.value = false;
  selectedMovie.value = {
    title: "",
    genre: "",
    director: "",
    releaseYear: null,
    duration: null,
    description: "",
    imageUrl: ""
  };
  drawerOpen.value = true;
};

const openEditDrawer = (movie) => {
  isEdit.value = true;
  selectedMovie.value = { ...movie };
  drawerOpen.value = true;
};

const openAddToPlaylist = (movie) => {
  selectedMovieForPlaylist.value = movie;
  showPlaylistModal.value = true;
};

const onFormSuccess = () => {
  drawerOpen.value = false;
  fetchMovies();
  fetchMetadata(); // Refresh metadata in case new genres/years were added
};

const deleteMovie = (movie) => {
  movieToDelete.value = movie;
  showDeleteModal.value = true;
};

const confirmDelete = async () => {
  if (!movieToDelete.value) return;
  try {
    await apiClient.delete(`/movies/${movieToDelete.value.id}`);
    fetchMovies();
    fetchMetadata(); // Refresh metadata in case deleted movie affected genres/years
  } catch (err) {
    console.error("Delete failed", err);
  } finally {
    movieToDelete.value = null;
  }
};

onMounted(() => {
  fetchMetadata();
  fetchMovies();
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
