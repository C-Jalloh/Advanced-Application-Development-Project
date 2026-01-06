<template>
  <div v-if="modelValue" class="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-fade-in">
    <div class="w-full max-w-md liquid-glass p-6 space-y-6 animate-scale-in">
      <div class="flex items-center justify-between">
        <h3 class="text-xl font-bold">Add to Playlist</h3>
        <button @click="$emit('update:modelValue', false)" class="p-2 hover:bg-white/10 rounded-full transition-colors">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <div class="space-y-4">
        <div class="flex items-center gap-4 p-3 rounded-2xl bg-white/5 border border-white/10">
          <div class="w-12 h-16 rounded-lg bg-black/20 overflow-hidden flex-shrink-0">
            <img v-if="item.imageUrl" :src="item.imageUrl.startsWith('http') ? item.imageUrl : `http://127.0.0.1:8080/images/${item.imageUrl}`" class="w-full h-full object-cover" />
            <div v-else class="w-full h-full flex items-center justify-center text-2xl">
              {{ itemType === 'movie' ? '🎬' : '🎵' }}
            </div>
          </div>
          <div class="min-w-0">
            <p class="font-bold truncate">{{ item.title }}</p>
            <p class="text-sm opacity-60 truncate">{{ itemType === 'movie' ? item.releaseYear : item.artist }}</p>
          </div>
        </div>

        <div v-if="loading" class="py-8 flex justify-center">
          <div class="w-8 h-8 border-4 border-white/20 border-t-white rounded-full animate-spin"></div>
        </div>

        <div v-else-if="playlists.length === 0" class="text-center py-8 space-y-4">
          <p class="text-sm opacity-60 italic">You don't have any playlists yet.</p>
          <router-link to="/playlists" class="glass-button inline-block px-6 py-2">Create Playlist</router-link>
        </div>

        <div v-else class="space-y-2 max-h-60 overflow-y-auto pr-2 custom-scrollbar">
          <button 
            v-for="playlist in playlists" 
            :key="playlist.id"
            @click="addToPlaylist(playlist)"
            :disabled="addingTo === playlist.id"
            class="w-full flex items-center justify-between p-3 rounded-xl bg-white/5 border border-white/10 hover:bg-white/10 hover:border-white/20 transition-all group"
          >
            <div class="flex items-center gap-3">
              <div class="w-10 h-10 rounded-lg bg-white/10 flex items-center justify-center group-hover:scale-110 transition-transform">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3" />
                </svg>
              </div>
              <span class="font-medium">{{ playlist.name }}</span>
            </div>
            <div v-if="addingTo === playlist.id" class="w-4 h-4 border-2 border-white/20 border-t-white rounded-full animate-spin"></div>
            <svg v-else class="w-5 h-5 opacity-0 group-hover:opacity-100 transition-opacity" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
          </button>
        </div>
      </div>

      <div v-if="message" :class="`p-3 rounded-xl text-sm text-center animate-fade-in ${message.type === 'success' ? 'bg-green-500/20 text-green-400 border border-green-500/30' : 'bg-red-500/20 text-red-400 border border-red-500/30'}`">
        {{ message.text }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import apiClient from '../api/axios';

const props = defineProps({
  modelValue: Boolean,
  item: Object,
  itemType: {
    type: String,
    required: true,
    validator: (value) => ['movie', 'music'].includes(value)
  }
});

const emit = defineEmits(['update:modelValue']);

const playlists = ref([]);
const loading = ref(true);
const addingTo = ref(null);
const message = ref(null);

const fetchPlaylists = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/playlists/my-playlists');
    playlists.value = response.data;
  } catch (err) {
    console.error('Failed to fetch playlists:', err);
  } finally {
    loading.value = false;
  }
};

const addToPlaylist = async (playlist) => {
  addingTo.value = playlist.id;
  message.value = null;
  try {
    const action = props.itemType === 'movie' ? 'add-movie' : 'add-music';
    const endpoint = `/playlists/${playlist.id}/${action}/${props.item.id}`;
    
    await apiClient.post(endpoint);
    
    message.value = { type: 'success', text: `Added to ${playlist.name}!` };
    setTimeout(() => {
      emit('update:modelValue', false);
      message.value = null;
    }, 1500);
  } catch (err) {
    console.error('Failed to add to playlist:', err);
    message.value = { 
      type: 'error', 
      text: err.response?.data?.message || 'Failed to add to playlist' 
    };
  } finally {
    addingTo.value = null;
  }
};

watch(() => props.modelValue, (newVal) => {
  if (newVal) {
    fetchPlaylists();
    message.value = null;
  }
});
</script>
