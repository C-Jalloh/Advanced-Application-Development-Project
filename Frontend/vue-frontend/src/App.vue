<template>
  <div class="min-h-screen bg-transparent">
    <!-- Show MainLayout for authenticated users -->
    <template v-if="userStore.isAuthenticated">
      <MainLayout />
    </template>
    
    <!-- Show AuthView for non-authenticated users -->
    <template v-else>
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </template>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useUserStore } from "./store/user";
import { useThemeStore } from "./store/theme";
import MainLayout from "./layouts/MainLayout.vue";

const userStore = useUserStore();
const themeStore = useThemeStore();

onMounted(() => {
  themeStore.applyTheme();
});
</script>

<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>