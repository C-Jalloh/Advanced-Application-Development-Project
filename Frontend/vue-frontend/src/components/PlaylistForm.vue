<template>
  <form @submit.prevent="submit" class="space-y-6">
    <div class="space-y-1">
      <label class="glass-label">Playlist Name</label>
      <input 
        v-model="formData.name"
        type="text"
        placeholder="My Awesome Playlist"
        class="glass-input"
        required
      />
    </div>

    <div class="flex items-center gap-3 p-4 rounded-2xl bg-white/5 border border-white/10">
      <div class="flex-1">
        <h4 class="font-bold text-sm">Public Playlist</h4>
        <p class="text-xs opacity-60">Allow others to view this playlist</p>
      </div>
      <label class="relative inline-flex items-center cursor-pointer">
        <input type="checkbox" v-model="formData.isPublic" class="sr-only peer">
        <div class="w-11 h-6 bg-white/20 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-indigo-600"></div>
      </label>
    </div>

    <div class="pt-4 flex justify-end">
      <button 
        type="submit" 
        :disabled="loading"
        class="glass-button-primary w-full py-3"
      >
        <span v-if="loading">Processing...</span>
        <span v-else>{{ isEdit ? 'Update Playlist' : 'Create Playlist' }}</span>
      </button>
    </div>
  </form>
</template>

<script setup>
import { ref, reactive } from 'vue';
import apiClient from "../api/axios";

const props = defineProps({
  initialData: {
    type: Object,
    default: () => ({})
  },
  isEdit: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['success']);

const loading = ref(false);
const formData = reactive({
  name: "",
  isPublic: false,
  ...props.initialData
});

const submit = async () => {
  loading.value = true;
  try {
    if (props.isEdit) {
      await apiClient.put(`/playlists/${formData.id}`, null, {
        params: { 
          name: formData.name,
          isPublic: formData.isPublic
        }
      });
    } else {
      await apiClient.post("/playlists", null, {
        params: { 
          name: formData.name,
          isPublic: formData.isPublic
        }
      });
    }
    emit("success");
  } catch (error) {
    console.error("Submission failed", error);
  } finally {
    loading.value = false;
  }
};
</script>
