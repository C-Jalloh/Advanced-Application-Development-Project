<template>
  <div class="relative w-full" ref="containerRef">
    <!-- Selected Items Tags -->
    <div v-if="modelValue.length > 0" class="flex flex-wrap gap-2 mb-3">
      <div 
        v-for="val in modelValue" 
        :key="val"
        class="flex items-center gap-2 px-3 py-1 rounded-full bg-white/10 border border-white/20 text-xs font-medium animate-fade-in"
      >
        <span>{{ getOptionLabel(val) }}</span>
        <button 
          @click="removeSelected(val)"
          class="hover:text-red-400 transition-colors"
        >
          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
    </div>

    <!-- Search Input -->
    <div class="relative">
      <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
        <svg class="w-4 h-4 opacity-50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
      </div>
      <input
        ref="searchInput"
        v-model="searchQuery"
        type="text"
        :placeholder="placeholder"
        class="glass-input !pl-10 w-full"
        @focus="isOpen = true"
      />
      <button 
        v-if="searchQuery"
        @click="searchQuery = ''"
        class="absolute inset-y-0 right-0 pr-4 flex items-center hover:opacity-100 opacity-50"
      >
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>

    <!-- Results Dropdown -->
    <Transition
      enter-active-class="transition duration-300 ease-out"
      enter-from-class="transform scale-95 opacity-0 -translate-y-2"
      enter-to-class="transform scale-100 opacity-100 translate-y-0"
      leave-active-class="transition duration-200 ease-in"
      leave-from-class="transform scale-100 opacity-100 translate-y-0"
      leave-to-class="transform scale-95 opacity-0 -translate-y-2"
    >
      <div 
        v-if="isOpen && filteredOptions.length > 0"
        class="absolute z-[110] w-full mt-2 liquid-glass backdrop-blur-3xl max-h-64 overflow-y-auto custom-scrollbar border-2 shadow-2xl"
        style="background: color-mix(in srgb, var(--bg-main), transparent 5%)"
      >
        <div class="p-1">
          <button
            v-for="option in filteredOptions"
            :key="option.value"
            type="button"
            @click="toggleOption(option)"
            class="w-full px-4 py-3 text-left rounded-xl transition-all duration-200 flex items-center justify-between group hover:bg-white/10"
            :class="{ 'bg-white/20 font-bold': isSelected(option.value) }"
          >
            <div class="flex flex-col">
              <span class="text-sm">{{ option.label }}</span>
            </div>
            
            <div 
              class="w-5 h-5 rounded-md border-2 transition-all flex items-center justify-center"
              :class="isSelected(option.value) ? 'bg-accent-color border-accent-color' : 'border-white/20'"
            >
              <svg v-if="isSelected(option.value)" class="w-3 h-3 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
              </svg>
            </div>
          </button>
        </div>
      </div>
    </Transition>
    
    <div v-if="isOpen && searchQuery && filteredOptions.length === 0" 
         class="absolute z-[110] w-full mt-2 liquid-glass p-6 text-center text-sm opacity-50 italic">
      No media found matching "{{ searchQuery }}"
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => []
  },
  options: {
    type: Array,
    default: () => []
  },
  placeholder: {
    type: String,
    default: 'Search and select...'
  }
});

const emit = defineEmits(['update:modelValue']);

const searchQuery = ref('');
const isOpen = ref(false);
const containerRef = ref(null);
const searchInput = ref(null);

const filteredOptions = computed(() => {
  if (!searchQuery.value) return props.options.slice(0, 50); // Show top 50 when empty
  const query = searchQuery.value.toLowerCase();
  return props.options.filter(opt => 
    opt.label.toLowerCase().includes(query)
  );
});

const isSelected = (value) => {
  return props.modelValue.includes(value);
};

const toggleOption = (option) => {
  const newValue = [...props.modelValue];
  const index = newValue.indexOf(option.value);
  
  if (index === -1) {
    newValue.push(option.value);
  } else {
    newValue.splice(index, 1);
  }
  
  emit('update:modelValue', newValue);
};

const removeSelected = (value) => {
  const newValue = props.modelValue.filter(v => v !== value);
  emit('update:modelValue', newValue);
};

const getOptionLabel = (value) => {
  const option = props.options.find(opt => opt.value === value);
  return option ? option.label : value;
};

const handleClickOutside = (event) => {
  if (containerRef.value && !containerRef.value.contains(event.target)) {
    isOpen.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
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
</style>
