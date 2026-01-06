<template>
  <div class="space-y-6">
    <!-- Playlist Header / Name Edit -->
    <div v-if="isOwner" class="p-4 rounded-2xl bg-black/5 border border-white/10 space-y-4">
      <div class="flex items-center justify-between px-1">
        <p class="text-xs font-bold uppercase tracking-widest opacity-60">Playlist Name</p>
        <button 
          @click="toggleNameEdit"
          class="text-xs font-bold text-accent-color hover:underline"
        >
          {{ isEditingName ? 'Save' : 'Edit' }}
        </button>
      </div>
      
      <div v-if="isEditingName" class="relative">
        <input 
          v-model="editedName"
          @keyup.enter="saveName"
          class="w-full bg-black/20 border border-white/10 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:border-accent-color transition-colors"
          placeholder="Enter playlist name..."
        />
      </div>
      <h3 v-else class="text-xl font-bold px-1">{{ playlist.name }}</h3>
    </div>

    <!-- Quick Actions -->
    <div v-if="isOwner" class="flex items-center justify-between p-4 rounded-2xl bg-black/5 border border-white/10">
      <div class="flex items-center gap-3">
        <div class="p-2 rounded-lg bg-black/10">
          <svg class="w-5 h-5 opacity-70" :class="playlist.isPublic ? 'text-green-400' : 'text-gray-400'" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path v-if="playlist.isPublic" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            <path v-if="playlist.isPublic" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
            <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l18 18" />
          </svg>
        </div>
        <div>
          <p class="text-xs opacity-60 uppercase tracking-widest">Status</p>
          <p class="font-bold flex items-center gap-2">
            {{ playlist.isPublic ? 'Public' : 'Private' }}
            <span v-if="isSideLoading" class="inline-block w-3 h-3 border-2 border-white/20 border-t-white rounded-full animate-spin"></span>
          </p>
        </div>
      </div>
      <div class="flex items-center gap-3">
        <button 
          @click="togglePublic"
          :disabled="isSideLoading"
          class="relative inline-flex items-center group focus:outline-none"
          :title="playlist.isPublic ? 'Make Private' : 'Make Public'"
        >
          <div 
            class="w-12 h-6 rounded-full transition-colors duration-200 border border-white/10"
            :class="playlist.isPublic ? 'bg-accent-color/40' : 'bg-white/10'"
          ></div>
          <div 
            class="absolute left-1 top-1 w-4 h-4 bg-white rounded-full transition-transform duration-200 shadow-md"
            :class="playlist.isPublic ? 'translate-x-6' : 'translate-x-0'"
          ></div>
        </button>

        <div class="w-px h-8 bg-white/10 mx-1"></div>

        <button 
          @click="showDeleteModal = true"
          class="p-2.5 rounded-xl text-red-400 hover:text-white hover:bg-red-500 transition-all border border-red-500/20 hover:border-red-500 shadow-lg group"
          title="Delete Playlist"
        >
          <svg class="w-5 h-5 group-hover:scale-110 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
          </svg>
        </button>
      </div>
    </div>

    <!-- Add Item Section -->
    <div v-if="isOwner" class="space-y-3 p-4 rounded-2xl bg-black/5 border border-white/10">
      <p class="text-xs font-bold uppercase tracking-widest opacity-60">Add Content</p>
      <MediaMultiSelect
        v-model="selectedItems"
        :options="combinedOptions"
        placeholder="Search movies or music..."
      />
      <button 
        @click="addItems"
        :disabled="selectedItems.length === 0"
        class="glass-button w-full py-3 flex items-center justify-center gap-2"
      >
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
        </svg>
        Add to Playlist {{ selectedItems.length > 0 ? `(${selectedItems.length})` : '' }}
      </button>
    </div>

    <!-- Content List -->
    <div class="space-y-4">
      <div class="flex items-center justify-between px-1">
        <h4 class="text-sm font-bold uppercase tracking-widest opacity-60">Current Items</h4>
        <span class="text-xs opacity-60">{{ playlistSongs.length + playlistMovies.length }} items</span>
      </div>
      
      <div class="space-y-3 max-h-[50vh] overflow-y-auto pr-2 custom-scrollbar">
        <!-- Movies -->
        <div v-for="movie in playlistMovies" :key="'m'+movie.id" class="flex items-center justify-between p-3 rounded-2xl bg-white/5 border border-white/10 group hover:bg-white/10 hover:border-white/20 transition-all">
          <div class="flex items-center gap-4 min-w-0">
            <div class="w-12 h-16 rounded-xl bg-black/20 overflow-hidden flex-shrink-0 shadow-lg group-hover:scale-105 transition-transform">
              <img 
                :src="movie.imageUrl ? (movie.imageUrl.startsWith('http') ? movie.imageUrl : `http://127.0.0.1:8080/images/${movie.imageUrl}`) : 'https://placehold.co/40x60?text=🎬'" 
                class="w-full h-full object-cover" 
                @error="(e) => e.target.src = 'https://placehold.co/40x60?text=🎬'" 
              />
            </div>
            <div class="min-w-0">
              <p class="text-sm font-bold truncate group-hover:text-accent-color transition-colors">{{ movie.title }}</p>
              <div class="flex items-center gap-2 mt-0.5">
                <span class="text-[10px] px-1.5 py-0.5 rounded-md bg-white/10 opacity-70">MOVIE</span>
                <span class="text-xs opacity-50">{{ movie.releaseYear }}</span>
              </div>
            </div>
          </div>
          <button v-if="isOwner" @click="removeMovie(movie.id)" class="p-2.5 rounded-xl text-red-400 hover:text-white hover:bg-red-500 transition-all opacity-0 group-hover:opacity-100 shadow-lg">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
          </button>
        </div>

        <!-- Music -->
        <div v-for="song in playlistSongs" :key="'s'+song.id" class="flex items-center justify-between p-3 rounded-2xl bg-white/5 border border-white/10 group hover:bg-white/10 hover:border-white/20 transition-all">
          <div class="flex items-center gap-4 min-w-0">
            <div class="w-12 h-12 rounded-xl bg-indigo-500/20 flex items-center justify-center flex-shrink-0 shadow-lg group-hover:rotate-6 transition-transform overflow-hidden">
              <img 
                v-if="song.imageUrl"
                :src="song.imageUrl.startsWith('http') ? song.imageUrl : `http://127.0.0.1:8080/images/${song.imageUrl}`"
                class="w-full h-full object-cover"
                @error="(e) => e.target.src = ''"
              />
              <span v-else class="text-2xl">🎵</span>
            </div>
            <div class="min-w-0">
              <p class="text-sm font-bold truncate group-hover:text-accent-color transition-colors">{{ song.title }}</p>
              <div class="flex items-center gap-2 mt-0.5">
                <span class="text-[10px] px-1.5 py-0.5 rounded-md bg-indigo-500/20 text-indigo-300 opacity-70">MUSIC</span>
                <span class="text-xs opacity-50 truncate">{{ song.artist }}</span>
              </div>
            </div>
          </div>
          <button v-if="isOwner" @click="removeSong(song.id)" class="p-2.5 rounded-xl text-red-400 hover:text-white hover:bg-red-500 transition-all opacity-0 group-hover:opacity-100 shadow-lg">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
          </button>
        </div>

        <div v-if="playlistSongs.length === 0 && playlistMovies.length === 0" class="text-center py-12 opacity-60 italic text-sm">
          No items in this playlist
        </div>
      </div>
    </div>

    <!-- Confirmation Modal -->
    <ConfirmationModal
      v-model="showDeleteModal"
      title="Delete Playlist"
      :message="`Are you sure you want to delete '${playlist.name}'? This action cannot be undone.`"
      confirmText="Delete"
      type="danger"
      @confirm="confirmDelete"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import apiClient from "../api/axios";
import ConfirmationModal from "./ConfirmationModal.vue";
import MediaMultiSelect from "./MediaMultiSelect.vue";
import { useUserStore } from "../store/user";

const userStore = useUserStore();
const currentUser = computed(() => userStore.user?.username);

const props = defineProps({
  playlist: Object,
  allSongs: {
    type: Array,
    default: () => []
  },
  allMovies: {
    type: Array,
    default: () => []
  }
});

const isOwner = computed(() => props.playlist?.user?.username === currentUser.value);

const songOptions = computed(() => {
  if (!Array.isArray(props.allSongs)) return [];
  return props.allSongs.map(song => ({
    value: `song:${song.id}`,
    label: `🎵 ${song.title} - ${song.artist}`
  }));
});

const movieOptions = computed(() => {
  if (!Array.isArray(props.allMovies)) return [];
  return props.allMovies.map(movie => ({
    value: `movie:${movie.id}`,
    label: `🎬 ${movie.title} (${movie.releaseYear})`
  }));
});

const combinedOptions = computed(() => [
  ...songOptions.value,
  ...movieOptions.value
]);

const emit = defineEmits(['playlist-updated']);

const playlistSongs = ref([]);
const playlistMovies = ref([]);
const selectedItems = ref([]);
const showDeleteModal = ref(false);
const isSideLoading = ref(false);
const isEditingName = ref(false);
const editedName = ref('');

const toggleNameEdit = () => {
  if (isEditingName.value) {
    saveName();
  } else {
    editedName.value = props.playlist.name;
    isEditingName.value = true;
  }
};

const saveName = async () => {
  if (editedName.value === props.playlist.name) {
    isEditingName.value = false;
    return;
  }
  
  isSideLoading.value = true;
  try {
    await apiClient.put(`/playlists/${props.playlist.id}`, null, {
      params: { 
        name: editedName.value,
        isPublic: props.playlist.isPublic
      }
    });
    emit('playlist-updated', 'updated');
    isEditingName.value = false;
  } catch (error) {
    console.error("Failed to update playlist name:", error);
  } finally {
    isSideLoading.value = false;
  }
};

const fetchPlaylistContent = async () => {
  try {
    const [musicRes, moviesRes] = await Promise.all([
      apiClient.get(`/playlists/${props.playlist.id}/music`),
      apiClient.get(`/playlists/${props.playlist.id}/movies`)
    ]);
    playlistSongs.value = musicRes.data;
    playlistMovies.value = moviesRes.data;
  } catch (error) {
    console.error("Failed to fetch playlist content");
  }
};

const addItems = async () => {
  if (selectedItems.value.length === 0) return;
  isSideLoading.value = true;
  try {
    const promises = selectedItems.value.map(val => {
      const [type, id] = val.split(':');
      const endpoint = type === 'movie' ? 'add-movie' : 'add-music';
      return apiClient.post(`/playlists/${props.playlist.id}/${endpoint}/${id}`);
    });

    await Promise.all(promises);
    selectedItems.value = [];
    await fetchPlaylistContent();
    emit('playlist-updated', 'updated');
  } catch (error) {
    console.error("Failed to add items to playlist:", error);
  } finally {
    isSideLoading.value = false;
  }
};

const removeSong = async (songId) => {
  isSideLoading.value = true;
  try {
    await apiClient.delete(`/playlists/${props.playlist.id}/remove-music/${songId}`);
    await fetchPlaylistContent();
    emit('playlist-updated', 'updated');
  } catch (error) {
    console.error("Failed to remove song");
  } finally {
    isSideLoading.value = false;
  }
};

const removeMovie = async (movieId) => {
  isSideLoading.value = true;
  try {
    await apiClient.delete(`/playlists/${props.playlist.id}/remove-movie/${movieId}`);
    await fetchPlaylistContent();
    emit('playlist-updated', 'updated');
  } catch (error) {
    console.error("Failed to remove movie");
  } finally {
    isSideLoading.value = false;
  }
};

const togglePublic = async () => {
  isSideLoading.value = true;
  try {
    await apiClient.patch(`/playlists/${props.playlist.id}/toggle-public`);
    emit('playlist-updated', 'updated');
  } catch (error) {
    console.error("Failed to toggle public status");
  } finally {
    isSideLoading.value = false;
  }
};

const confirmDelete = async () => {
  try {
    await apiClient.delete(`/playlists/${props.playlist.id}`);
    emit('playlist-updated', 'deleted');
  } catch (error) {
    console.error("Failed to delete playlist");
  }
};

onMounted(fetchPlaylistContent);

// Watch for playlist prop changes to refresh content
watch(() => props.playlist.id, fetchPlaylistContent);
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.2);
}
</style>
