<template>
  <Teleport to="body">
    <Transition name="drawer">
      <div v-if="modelValue" class="fixed inset-0 z-[9999] flex justify-end">
        <!-- Overlay -->
        <div 
          class="fixed inset-0 bg-black/60 backdrop-blur-md -z-10"
          @click="close"
        ></div>

        <!-- Drawer Panel -->
        <div 
          class="h-full w-full max-w-lg liquid-glass-drawer rounded-l-3xl shadow-2xl flex flex-col overflow-hidden"
        >
          <!-- Header -->
          <div class="p-6 flex items-center justify-between border-b border-white/10 glass-header">
            <h2 class="text-2xl font-bold">{{ title }}</h2>
            <button 
              @click="close"
              class="p-2 rounded-full hover:bg-white/10 transition-colors opacity-70 hover:opacity-100"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <!-- Content -->
          <div class="flex-1 overflow-y-auto p-6 scrollbar-theme">
            <slot></slot>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
const props = defineProps({
  modelValue: Boolean,
  title: String,
});

const emit = defineEmits(['update:modelValue']);

const close = () => {
  emit('update:modelValue', false);
};
</script>

<style scoped>
.liquid-glass-drawer {
  background: color-mix(in srgb, var(--glass-bg), transparent 10%);
  backdrop-filter: blur(40px) saturate(200%);
  -webkit-backdrop-filter: blur(40px) saturate(200%);
  border-left: 1px solid var(--glass-border);
  box-shadow: -10px 0 40px rgba(0, 0, 0, 0.2);
  color: var(--text-main);
}

.glass-header {
  background: rgba(255, 255, 255, 0.03);
}

.scrollbar-theme::-webkit-scrollbar {
  width: 6px;
}

.scrollbar-theme::-webkit-scrollbar-thumb {
  background: var(--glass-border);
  border-radius: 10px;
}

/* Drawer Transition */
.drawer-enter-active,
.drawer-leave-active {
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.drawer-enter-active .liquid-glass-drawer,
.drawer-leave-active .liquid-glass-drawer {
  transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.drawer-enter-from {
  opacity: 0;
}

.drawer-enter-from .liquid-glass-drawer {
  transform: translateX(100%);
}

.drawer-leave-to {
  opacity: 0;
}

.drawer-leave-to .liquid-glass-drawer {
  transform: translateX(100%);
}
</style>
