<template>
  <div class="space-y-6">
    <h3 class="text-lg font-bold">
      Reviews ({{ reviews.length }})
    </h3>

    <!-- Add a Review -->
    <form @submit.prevent="submitReview" class="space-y-3">
      <div class="flex items-center gap-2">
        <div class="flex text-amber-400">
          <button 
            v-for="i in 5" 
            :key="i"
            type="button"
            @click="selectedRating = i"
            class="focus:outline-none transition-transform hover:scale-110"
          >
            <svg 
              class="w-6 h-6" 
              :fill="i <= selectedRating ? 'currentColor' : 'none'" 
              stroke="currentColor" 
              viewBox="0 0 24 24"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.175 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.382-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z" />
            </svg>
          </button>
        </div>
        <span class="text-sm font-medium opacity-70">({{ selectedRating }}/5)</span>
      </div>
      
      <textarea
        v-model="reviewText"
        placeholder="Share your thoughts..."
        rows="2"
        class="glass-input resize-none"
        required
      ></textarea>
      
      <button
        type="submit"
        :disabled="!reviewText || submitting"
        class="glass-button-primary w-full py-2"
      >
        <span v-if="submitting">Posting...</span>
        <span v-else>Post Review</span>
      </button>
    </form>

    <!-- Display Reviews -->
    <div class="space-y-4">
      <div
        v-for="review in reviews"
        :key="review.id"
        class="flex gap-3 p-3 rounded-2xl liquid-glass"
      >
        <div class="flex-shrink-0">
          <div class="w-10 h-10 rounded-full bg-white/20 flex items-center justify-center font-bold border border-white/30">
            {{ review.username?.charAt(0).toUpperCase() }}
          </div>
        </div>

        <div class="flex-1 min-w-0">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2">
              <span class="font-bold text-sm">{{ review.username }}</span>
              <div class="flex text-amber-400 scale-75 origin-left">
                <svg 
                  v-for="i in 5" 
                  :key="i"
                  class="w-4 h-4" 
                  :fill="i <= review.rating ? 'currentColor' : 'none'" 
                  stroke="currentColor" 
                  viewBox="0 0 24 24"
                >
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.175 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.382-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z" />
                </svg>
              </div>
            </div>
            <button
              v-if="review.username === currentUser"
              @click="openDeleteModal(review.id)"
              class="p-2 rounded-full text-red-400 hover:text-red-600 hover:bg-red-500/10 transition-colors"
            >
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
            </button>
          </div>
          <p class="text-sm mt-1 break-words opacity-90">
            {{ review.reviewText }}
          </p>
        </div>
      </div>
      
      <div v-if="reviews.length === 0" class="text-center py-4 opacity-50 text-sm italic">
        No reviews yet. Be the first!
      </div>
    </div>

    <!-- Confirmation Modal -->
    <ConfirmationModal
      v-model="showDeleteModal"
      title="Delete Review"
      message="Are you sure you want to delete your review? This action cannot be undone."
      confirmText="Delete"
      type="danger"
      @confirm="confirmDelete"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import apiClient from "../api/axios";
import { useUserStore } from "../store/user";
import ConfirmationModal from "./ConfirmationModal.vue";

const props = defineProps({
  contentId: [String, Number],
  contentType: String,
});

const emit = defineEmits(["review-updated"]);

const userStore = useUserStore();
const reviews = ref([]);
const reviewText = ref("");
const selectedRating = ref(5);
const submitting = ref(false);

// Delete Modal State
const showDeleteModal = ref(false);
const reviewToDeleteId = ref(null);

const currentUser = computed(() => userStore.user?.username);

const fetchReviews = async () => {
  try {
    const response = await apiClient.get(`/reviews/${props.contentType}/${props.contentId}`);
    reviews.value = response.data;
  } catch (error) {
    console.error("Failed to fetch reviews");
  }
};

const submitReview = async () => {
  submitting.value = true;
  try {
    await apiClient.post("/reviews", {
      contentId: props.contentId,
      contentType: props.contentType,
      rating: selectedRating.value,
      reviewText: reviewText.value,
    });
    reviewText.value = "";
    selectedRating.value = 5;
    await fetchReviews();
    emit("review-updated");
  } catch (error) {
    console.error("Failed to submit review");
  } finally {
    submitting.value = false;
  }
};

const openDeleteModal = (reviewId) => {
  reviewToDeleteId.value = reviewId;
  showDeleteModal.value = true;
};

const confirmDelete = async () => {
  if (!reviewToDeleteId.value) return;
  try {
    await apiClient.delete(`/reviews/${reviewToDeleteId.value}`);
    await fetchReviews();
    emit("review-updated");
  } catch (error) {
    console.error("Failed to delete review");
  } finally {
    reviewToDeleteId.value = null;
  }
};

onMounted(fetchReviews);
</script>
