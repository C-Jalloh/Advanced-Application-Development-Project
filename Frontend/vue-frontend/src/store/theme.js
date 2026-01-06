import { defineStore } from 'pinia';
import { ref, watch } from 'vue';

export const useThemeStore = defineStore('theme', () => {
  const currentTheme = ref(localStorage.getItem('prism-theme') || 'light-glass');

  const themes = {
    'light-glass': {
      name: 'Light Glass',
      class: 'theme-light-glass',
      colors: 'from-indigo-600 to-purple-600'
    },
    'dark-glass': {
      name: 'Dark Glass',
      class: 'theme-dark-glass',
      colors: 'from-blue-400 to-emerald-400'
    },
    'deep-sea': {
      name: 'Deep Sea',
      class: 'theme-deep-sea',
      colors: 'from-cyan-500 to-blue-700'
    },
    'sunset': {
      name: 'Sunset',
      class: 'theme-sunset',
      colors: 'from-orange-500 to-rose-600'
    }
  };

  const setTheme = (themeKey) => {
    if (themes[themeKey]) {
      currentTheme.value = themeKey;
      localStorage.setItem('prism-theme', themeKey);
      applyTheme();
    }
  };

  const applyTheme = () => {
    const root = document.documentElement;
    // Remove all theme classes
    Object.values(themes).forEach(t => root.classList.remove(t.class));
    // Add current theme class
    root.classList.add(themes[currentTheme.value].class);
  };

  return {
    currentTheme,
    themes,
    setTheme,
    applyTheme
  };
});
