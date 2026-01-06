<template>
  <div class="p-6 max-w-4xl mx-auto">
    <div class="mb-8">
      <h1 class="text-3xl font-bold">Profile Settings</h1>
      <p class="opacity-70">Manage your account preferences and profile information</p>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
      <!-- Left Column: Profile Picture & Basic Info -->
      <div class="md:col-span-1 space-y-6">
        <div class="liquid-glass p-6 text-center">
          <div class="relative inline-block group">
            <div class="w-32 h-32 rounded-full overflow-hidden border-4 border-white/30 shadow-xl mx-auto bg-white/10">
              <img 
                v-if="userStore.user?.profilePicture" 
                :src="`http://127.0.0.1:8080/images/${userStore.user.profilePicture}`" 
                class="w-full h-full object-cover"
              />
              <div v-else class="w-full h-full flex items-center justify-center text-4xl font-bold opacity-50">
                {{ userStore.user?.username?.charAt(0).toUpperCase() }}
              </div>
            </div>
            <label class="absolute bottom-0 right-0 p-2 glass-button-primary rounded-full cursor-pointer shadow-lg transform transition-transform hover:scale-110">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 13a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              <input type="file" class="hidden" @change="handleFileUpload" accept="image/*" />
            </label>
          </div>
          
          <h2 class="mt-4 text-xl font-bold">{{ userStore.user?.username }}</h2>
          <p class="text-sm opacity-60">{{ userStore.user?.email }}</p>
          <div class="mt-2 inline-block px-3 py-1 rounded-full text-xs font-bold bg-white/10 border border-white/20">
            {{ userStore.user?.role }}
          </div>
        </div>

        <div class="liquid-glass p-6">
          <h3 class="font-bold mb-4 flex items-center gap-2">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            Account Stats
          </h3>
          <div class="space-y-3 text-sm">
            <div class="flex justify-between opacity-70">
              <span>Member Since</span>
              <span>Dec 2025</span>
            </div>
            <div class="flex justify-between opacity-70">
              <span>Reviews Posted</span>
              <span>{{ userStore.user?.reviewsCount || 0 }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Column: Edit Forms -->
      <div class="md:col-span-2 space-y-6">
        <!-- Profile Information -->
        <div class="liquid-glass p-8">
          <h3 class="text-xl font-bold mb-6">Profile Information</h3>
          <form @submit.prevent="saveProfile" class="space-y-4">
            <div class="space-y-1">
              <label class="glass-label">Email Address</label>
              <input 
                v-model="profileForm.email"
                type="email"
                class="glass-input"
                placeholder="your@email.com"
              />
            </div>
            
            <div class="space-y-1">
              <label class="glass-label">Bio</label>
              <textarea 
                v-model="profileForm.bio"
                rows="4"
                class="glass-input resize-none"
                placeholder="Tell us about yourself..."
              ></textarea>
            </div>

            <div class="pt-4">
              <button 
                type="submit" 
                class="glass-button-primary px-8 py-2.5"
                :disabled="saving"
              >
                <span v-if="saving">Saving...</span>
                <span v-else>Save Changes</span>
              </button>
            </div>
          </form>
        </div>

        <!-- Preferences -->
        <div class="liquid-glass p-8 relative z-10">
          <h3 class="text-xl font-bold mb-6">Preferences</h3>
          <div class="space-y-6">
            <div class="flex items-center justify-between">
              <div>
                <p class="font-bold">Theme Preference</p>
                <p class="text-sm opacity-60">Choose your favorite look for Prism</p>
              </div>
              <div class="w-48">
                <GlassSelect 
                  v-model="profileForm.themePreference"
                  :options="themeOptions"
                  @change="handleThemeChange"
                />
              </div>
            </div>

            <div class="flex items-center justify-between">
              <div>
                <p class="font-bold">Email Notifications</p>
                <p class="text-sm opacity-60">Receive updates about new releases</p>
              </div>
              <button 
                @click="notificationsEnabled = !notificationsEnabled"
                class="relative inline-block w-12 h-6 rounded-full bg-white/10 border border-white/20 cursor-pointer transition-colors"
                :class="{ 'bg-green-500/30': notificationsEnabled }"
              >
                <div 
                  class="absolute left-1 top-1 w-4 h-4 rounded-full bg-white/50 transition-transform"
                  :class="{ 'translate-x-6': notificationsEnabled }"
                ></div>
              </button>
            </div>
          </div>
        </div>

        <!-- Security -->
        <div class="liquid-glass p-8 border-red-500/20">
          <h3 class="text-xl font-bold mb-6 text-red-400">Security</h3>
          <button class="glass-button border-red-500/30 text-red-400 hover:bg-red-500/10">
            Change Password
          </button>
        </div>
      </div>
    </div>

    <!-- Notification Modal -->
    <ConfirmationModal
      v-model="modal.show"
      :title="modal.title"
      :message="modal.message"
      :type="modal.type"
      :showCancel="false"
      confirmText="OK"
      @confirm="modal.show = false"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useUserStore } from '../store/user';
import { useThemeStore } from '../store/theme';
import apiClient from '../api/axios';
import ConfirmationModal from '../components/ConfirmationModal.vue';
import GlassSelect from '../components/GlassSelect.vue';

const userStore = useUserStore();
const themeStore = useThemeStore();

const saving = ref(false);
const notificationsEnabled = ref(true);
const profileForm = reactive({
  email: '',
  bio: '',
  themePreference: 'light-glass'
});

const themeOptions = computed(() => {
  return Object.entries(themeStore.themes).map(([key, theme]) => ({
    value: key,
    label: theme.name
  }));
});

const handleThemeChange = (newTheme) => {
  profileForm.themePreference = newTheme;
  themeStore.setTheme(newTheme);
  saveProfile();
};

const modal = reactive({
  show: false,
  title: '',
  message: '',
  type: 'info'
});

const showModal = (title, message, type = 'info') => {
  modal.title = title;
  modal.message = message;
  modal.type = type;
  modal.show = true;
};

onMounted(() => {
  if (userStore.user) {
    profileForm.email = userStore.user.email || '';
    profileForm.bio = userStore.user.bio || '';
    profileForm.themePreference = userStore.user.themePreference || 'ocean';
  }
});

const saveProfile = async () => {
  saving.value = true;
  try {
    await userStore.updateProfile({ ...profileForm });
    // If theme changed, apply it immediately
    if (profileForm.themePreference !== themeStore.currentTheme) {
      themeStore.setTheme(profileForm.themePreference);
    }
    showModal('Success', 'Profile updated successfully!');
  } catch (error) {
    showModal('Error', 'Failed to update profile', 'danger');
  } finally {
    saving.value = false;
  }
};

const handleFileUpload = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  // Basic validation
  if (file.size > 10 * 1024 * 1024) {
    showModal('File Too Large', 'Please select an image smaller than 10MB', 'danger');
    return;
  }

  try {
    await userStore.updateProfilePicture(file);
    showModal('Success', 'Profile picture updated!');
  } catch (error) {
    showModal('Error', 'Failed to upload picture. Please try again.', 'danger');
  }
};
</script>
