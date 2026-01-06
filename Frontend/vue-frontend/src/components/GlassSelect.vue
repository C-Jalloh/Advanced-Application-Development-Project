<template>
  <div class="relative w-full" ref="selectRef">
    <label v-if="label" class="glass-label">{{ label }}</label>
    
    <button
      type="button"
      @click="isOpen = !isOpen"
      class="glass-input flex items-center justify-between text-left"
      :class="{ 'border-accent-color shadow-[0_0_0_4px_rgba(var(--accent-color-rgb),0.15)]': isOpen }"
    >
      <span :class="{ 'opacity-50': !modelValue && placeholder }">
        {{ selectedLabel || placeholder }}
      </span>
      <svg 
        class="w-5 h-5 transition-transform duration-300" 
        :class="{ 'rotate-180': isOpen }"
        fill="none" stroke="currentColor" viewBox="0 0 24 24"
      >
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
      </svg>
    </button>

    <Transition
      enter-active-class="transition duration-300 ease-out"
      enter-from-class="transform scale-95 opacity-0 -translate-y-2"
      enter-to-class="transform scale-100 opacity-100 translate-y-0"
      leave-active-class="transition duration-200 ease-in"
      leave-from-class="transform scale-100 opacity-100 translate-y-0"
      leave-to-class="transform scale-95 opacity-0 -translate-y-2"
    >
      <div 
        v-if="isOpen"
        class="absolute z-[100] w-full mt-2 liquid-glass backdrop-blur-3xl max-h-60 overflow-y-auto no-scrollbar border-2 shadow-[0_20px_50px_rgba(0,0,0,0.3)]"
        style="background: color-mix(in srgb, var(--bg-main), transparent 15%)"
      >
        <div class="p-1">
          <button
            v-for="option in options"
            :key="option.value"
            type="button"
            @click="selectOption(option)"
            class="w-full px-4 py-2 text-left rounded-xl transition-all duration-200 flex items-center justify-between group hover:bg-white/10"
            :class="{ 'bg-white/20 font-bold': modelValue === option.value }"
          >
            <span class="text-sm">{{ option.label }}</span>
            <svg v-if="modelValue === option.value" class="w-4 h-4 text-accent-color" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
            </svg>
          </button>
          <div v-if="options.length === 0" class="px-4 py-3 text-sm opacity-50 italic">
            No options available
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';

const props = defineProps({
  modelValue: [String, Number],
  options: {
    type: Array,
    default: () => []
  },
  placeholder: {
    type: String,
    default: 'Select an option'
  },
  label: String
});

const emit = defineEmits(['update:modelValue', 'change']);

const isOpen = ref(false);
const selectRef = ref(null);

const selectedLabel = computed(() => {
  const option = props.options.find(opt => opt.value === props.modelValue);
  return option ? option.label : '';
});

const selectOption = (option) => {
  emit('update:modelValue', option.value);
  emit('change', option.value);
  isOpen.value = false;
};

const handleClickOutside = (event) => {
  if (selectRef.value && !selectRef.value.contains(event.target)) {
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
