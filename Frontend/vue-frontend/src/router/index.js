import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "../store/user";
import HomeView from "../views/HomeView.vue";
import AuthView from "../views/AuthView.vue";
import MoviesView from "../views/MoviesView.vue";
import MovieDetailView from "../views/MovieDetailView.vue";
import MusicView from "../views/MusicView.vue";
import MusicDetailView from "../views/MusicDetailView.vue";
import PlaylistsView from "../views/PlaylistsView.vue";
import PlaylistDetailView from "../views/PlaylistDetailView.vue";
import ProfileView from "../views/ProfileView.vue";


const routes = [
  { path: "/", component: HomeView },
  { path: "/login", component: AuthView, meta: { guest: true } },
  { path: "/register", component: AuthView, meta: { guest: true } },
  { path: "/movies", component: MoviesView, meta: { requiresAuth: true } },
  { path: "/movies/:id", component: MovieDetailView, meta: { requiresAuth: true } },
  { path: "/music", component: MusicView, meta: { requiresAuth: true } },
  { path: "/music/:id", component: MusicDetailView, meta: { requiresAuth: true } },
  { path: "/playlists", component: PlaylistsView, meta: { requiresAuth: true } },
  { path: "/playlists/:id", component: PlaylistDetailView, meta: { requiresAuth: true } },
  { path: "/profile", component: ProfileView, meta: { requiresAuth: true } },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore();
  
  if (userStore.token && !userStore.user) {
    await userStore.fetchUser();
  }

  if (to.meta.requiresAuth && !userStore.isAuthenticated) {
    next("/login");
  } else if (to.meta.guest && userStore.isAuthenticated) {
    next("/movies");
  } else if (to.meta.requiresAdmin && !userStore.isAdmin) {
    next("/movies");
  } else {
    next();
  }
});

export default router;