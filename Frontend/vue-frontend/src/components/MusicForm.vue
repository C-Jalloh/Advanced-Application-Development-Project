<template>
  <form @submit.prevent="submit" class="space-y-4">
    <div class="space-y-1">
      <label class="glass-label">Song Title</label>
      <input 
        v-model="formData.title"
        type="text"
        placeholder="Song Title"
        class="glass-input"
        required
      />
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div class="space-y-1">
        <label class="glass-label">Artist</label>
        <input 
          v-model="formData.artist"
          type="text"
          placeholder="Artist Name"
          class="glass-input"
        />
      </div>
      <div class="space-y-1">
        <label class="glass-label">Album</label>
        <input 
          v-model="formData.album"
          type="text"
          placeholder="Album Name"
          class="glass-input"
        />
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div class="space-y-1">
        <label class="glass-label">Genre</label>
        <input 
          v-model="formData.genre"
          type="text"
          placeholder="e.g. Pop"
          class="glass-input"
        />
      </div>
      <div class="space-y-1">
        <label class="glass-label">Release Year</label>
        <input 
          v-model="formData.releaseYear"
          type="number"
          placeholder="2024"
          class="glass-input"
        />
      </div>
    </div>

    <div class="space-y-1">
      <label class="glass-label">Album Banner</label>
      <div class="relative">
        <input 
          type="file"
          @change="onFileChange"
          accept="image/*"
          class="glass-input file:mr-4 file:py-1 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-white/20 file:text-current hover:file:bg-white/30"
        />
        <div v-if="uploading" class="absolute right-3 top-2">
          <svg class="animate-spin h-5 w-5 text-current" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
        </div>
      </div>
      <div v-if="previewUrl || formData.imageUrl" class="mt-2 rounded-xl overflow-hidden border border-white/50 shadow-sm">
        <img :src="previewUrl || `http://127.0.0.1:8080/images/${formData.imageUrl}`" class="w-full h-40 object-cover" />
      </div>
    </div>

    <div class="pt-4 flex justify-end">
      <button 
        type="submit" 
        :disabled="loading || uploading"
        class="glass-button-primary w-full py-3"
      >
        <span v-if="loading">Processing...</span>
        <span v-else>{{ isEdit ? 'Update Music' : 'Add Music' }}</span>
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
const uploading = ref(false);
const previewUrl = ref(null);
const formData = reactive({
  title: "",
  artist: "",
  album: "",
  genre: "",
  releaseYear: null,
  imageUrl: "",
  ...props.initialData
});

const onFileChange = async (e) => {
  const file = e.target.files[0];
  if (!file) {
    previewUrl.value = null;
    return;
  }

  const reader = new FileReader();
  reader.onload = (e) => {
    previewUrl.value = e.target.result;
  };
  reader.readAsDataURL(file);

  uploading.value = true;
  const data = new FormData();
  data.append("file", file);

  try {
    const response = await apiClient.post("/media/upload", data, {
      headers: { "Content-Type": "multipart/form-data" }
    });
    // Store only the filename in imageUrl to match the view's expectation
    formData.imageUrl = response.data.fileName;
  } catch (error) {
    console.error("Upload failed", error);
  } finally {
    uploading.value = false;
  }
};

const submit = async () => {
  loading.value = true;
  try {
    // Clean data before sending
    const payload = { ...formData };
    
    // Convert empty strings to null for numeric fields
    if (payload.releaseYear === "" || payload.releaseYear === undefined) payload.releaseYear = null;
    if (payload.duration === "" || payload.duration === undefined) payload.duration = null;
    
    // Remove fields that shouldn't be sent to the backend
    delete payload.showReviews;
    delete payload.rating;
    delete payload.createdAt;

    if (props.isEdit) {
      await apiClient.put(`/music/${formData.id}`, payload);
    } else {
      // Ensure ID is not sent on create
      delete payload.id;
      await apiClient.post("/music", payload);
    }
    emit("success");
  } catch (error) {
    console.error("Submission failed", error);
  } finally {
    loading.value = false;
  }
};
</script>
