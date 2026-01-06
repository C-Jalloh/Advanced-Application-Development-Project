<template>
  <div class="relative">
    <!-- Filter Button -->
    <button
      @click="toggleCategories"
      class="glass-button-primary px-6 py-2.5 flex items-center gap-2 relative z-10"
    >
      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
      </svg>
      <span class="font-semibold">Filters</span>
      <span v-if="activeFiltersCount > 0" class="ml-1 px-2 py-0.5 rounded-full text-xs font-bold bg-white/20">
        {{ activeFiltersCount }}
      </span>
    </button>

    <!-- Category Pills Container -->
    <Transition
      enter-active-class="transition-all duration-300 ease-out"
      enter-from-class="opacity-0 scale-95 translate-y-[-10px]"
      enter-to-class="opacity-100 scale-100 translate-y-0"
      leave-active-class="transition-all duration-200 ease-in"
      leave-from-class="opacity-100 scale-100 translate-y-0"
      leave-to-class="opacity-0 scale-95 translate-y-[-10px]"
    >
      <div 
        v-if="showCategories && !selectedCategory"
        class="absolute right-full top-0 mr-3 liquid-glass backdrop-blur-3xl rounded-2xl p-4 z-20 border-2 border-white/30 shadow-[0_20px_60px_rgba(0,0,0,0.2)] min-w-[300px]"
      >
        <div class="flex flex-wrap gap-3 justify-end">
          <!-- Clear All Button -->
          <button
            v-if="activeFiltersCount > 0"
            @click="clearAllFilters"
            class="liquid-glass backdrop-blur-xl px-5 py-2.5 rounded-full font-semibold text-sm border-2 border-red-500/30 text-red-500 hover:border-red-500/50 hover:bg-red-500/10 transition-all duration-200 hover:scale-105 shadow-[0_4px_15px_rgba(0,0,0,0.1)] whitespace-nowrap flex items-center gap-2"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
            Clear All
          </button>

          <button
            v-for="category in categories"
            :key="category.key"
            @click="selectCategory(category)"
            class="liquid-glass backdrop-blur-xl px-5 py-2.5 rounded-full font-semibold text-sm border-2 border-white/30 hover:border-white/50 transition-all duration-200 hover:scale-105 shadow-[0_4px_15px_rgba(0,0,0,0.1)] whitespace-nowrap"
          >
            {{ category.label }}
          </button>
        </div>
      </div>
    </Transition>

    <!-- Selected Category Pill + Options -->
    <Transition
      enter-active-class="transition-all duration-300 ease-out"
      enter-from-class="opacity-0 scale-95 translate-x-[10px]"
      enter-to-class="opacity-100 scale-100 translate-x-0"
      leave-active-class="transition-all duration-200 ease-in"
      leave-from-class="opacity-100 scale-100 translate-x-0"
      leave-to-class="opacity-0 scale-95 translate-x-[10px]"
    >
      <div 
        v-if="selectedCategory"
        class="absolute right-full top-[-20px] mr-3 z-30 max-w-[780px]"
      >
        <!-- Category Header with Back Button -->
        <div class="flex items-center gap-3 mb-3 justify-end">
          <button 
            @click="deselectCategory"
            class="liquid-glass backdrop-blur-xl px-4 py-1.5 rounded-full text-xs font-bold border-2 border-white/20 hover:border-white/40 transition-all flex items-center gap-1"
          >
            <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
            </svg>
            Back
          </button>
          <span class="text-sm font-bold opacity-70">{{ selectedCategory.label }}</span>
        </div>

        <!-- Options Pills in 2 rows -->
        <div class="grid grid-rows-2 grid-flow-col auto-cols-max gap-3">
          <TransitionGroup
            enter-active-class="transition-all duration-300 ease-out"
            enter-from-class="opacity-0 scale-90"
            enter-to-class="opacity-100 scale-100"
            leave-active-class="transition-all duration-200 ease-in"
            leave-from-class="opacity-100 scale-100"
            leave-to-class="opacity-0 scale-90"
          >
            <button
              v-for="(option, index) in visibleOptions"
              :key="option.value || 'all'"
              @click="selectOption(option)"
              :style="{ transitionDelay: `${index * 20}ms` }"
              class="liquid-glass backdrop-blur-xl px-4 py-2 rounded-full text-sm border-2 border-white/20 hover:border-white/40 transition-all duration-200 hover:scale-105 shadow-[0_4px_15px_rgba(0,0,0,0.1)] whitespace-nowrap text-center"
              :class="{ 
                'font-bold border-white/50 bg-white/10': isOptionSelected(option),
                'hover:bg-white/5': !isOptionSelected(option)
              }"
            >
              {{ option.label }}
            </button>
          </TransitionGroup>
        </div>
        
        <!-- Show More Button -->
        <button
          v-if="hasMoreOptions"
          @click="currentPage++"
          class="mt-3 liquid-glass backdrop-blur-xl px-4 py-2 rounded-full text-sm border-2 border-white/20 hover:border-white/40 transition-all duration-200 hover:scale-105 shadow-[0_4px_15px_rgba(0,0,0,0.1)] font-semibold"
        >
          Show More
        </button>
      </div>
    </Transition>

    <!-- Backdrop for closing -->
    <Transition
      enter-active-class="transition-opacity duration-200"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition-opacity duration-150"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div
        v-if="showCategories || selectedCategory"
        @click="closeAll"
        class="fixed inset-0 z-[5]"
      ></div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';

const props = defineProps({
  categories: {
    type: Array,
    required: true,
    // Expected format: [{ key: 'genre', label: 'Genre', options: [{label: 'Action', value: 'Action'}] }]
  },
  modelValue: {
    type: Object,
    default: () => ({})
    // Expected format: { genre: 'Action', year: 2020 }
  }
});

const emit = defineEmits(['update:modelValue', 'filter']);

const showCategories = ref(false);
const selectedCategory = ref(null);
const currentPage = ref(0);
const optionsPerPage = 12;

const visibleOptions = computed(() => {
  if (!selectedCategory.value) return [];
  const options = selectedCategory.value.options || [];
  const startIndex = currentPage.value * optionsPerPage;
  const endIndex = startIndex + optionsPerPage;
  return options.slice(startIndex, endIndex);
});

const hasMoreOptions = computed(() => {
  if (!selectedCategory.value) return false;
  const options = selectedCategory.value.options || [];
  return (currentPage.value + 1) * optionsPerPage < options.length;
});

const activeFiltersCount = computed(() => {
  return Object.keys(props.modelValue).filter(key => {
    const value = props.modelValue[key];
    return value !== null && value !== '' && value !== undefined;
  }).length;
});

const toggleCategories = () => {
  if (selectedCategory.value) {
    closeAll();
  } else {
    showCategories.value = !showCategories.value;
  }
};

const selectCategory = (category) => {
  selectedCategory.value = category;
  showCategories.value = false;
};

const deselectCategory = () => {
  selectedCategory.value = null;
  showCategories.value = true;
};

const clearAllFilters = () => {
  const emptyFilters = {};
  emit('update:modelValue', emptyFilters);
  emit('filter', emptyFilters);
  closeAll();
};

const selectOption = (option) => {
  if (!selectedCategory.value) return;
  
  const newFilters = { ...props.modelValue };
  
  // If "All" option is selected, clear that filter
  if (option.value === null || option.value === '' || option.value === undefined) {
    delete newFilters[selectedCategory.value.key];
  } else {
    newFilters[selectedCategory.value.key] = option.value;
  }
  
  emit('update:modelValue', newFilters);
  emit('filter', newFilters);
  
  // Close everything after selection
  setTimeout(() => {
    closeAll();
  }, 150);
};

const isOptionSelected = (option) => {
  if (!selectedCategory.value) return false;
  const currentValue = props.modelValue[selectedCategory.value.key];
  
  // Handle "All" option
  if (option.value === null || option.value === '' || option.value === undefined) {
    return !currentValue || currentValue === '' || currentValue === null;
  }
  
  return currentValue === option.value;
};

const closeAll = () => {
  selectedCategory.value = null;
  showCategories.value = false;
  currentPage.value = 0;
};

// Close on route change
watch(() => props.modelValue, () => {
  // Keep UI open for feedback, but could close if desired
}, { deep: true });
</script>

<style scoped>
/* Ensure smooth transitions */
* {
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
