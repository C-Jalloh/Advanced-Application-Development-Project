package com.example.Backend.Entities.Playlists;




import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public ResponseEntity<List<PlaylistModel>> getUserPlaylists(Principal principal) {
        return ResponseEntity.ok(playlistService.getUserPlaylists(principal.getName()));
    }

    @GetMapping("/public")
    public ResponseEntity<List<PlaylistModel>> getPublicPlaylists() {
        return ResponseEntity.ok(playlistService.getPublicPlaylists());
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistModel> getPlaylistById(Principal principal, @PathVariable Long playlistId) {
        String username = (principal != null) ? principal.getName() : null;
        return ResponseEntity.ok(playlistService.getPlaylistById(username, playlistId));
    }

    @GetMapping("/{playlistId}/music")
    public ResponseEntity<List<com.example.Backend.Entities.Music.MusicModel>> getPlaylistMusic(Principal principal, @PathVariable Long playlistId) {
        String username = (principal != null) ? principal.getName() : null;
        return ResponseEntity.ok(playlistService.getPlaylistMusic(username, playlistId));
    }

    @GetMapping("/{playlistId}/movies")
    public ResponseEntity<List<com.example.Backend.Entities.Movies.MovieModel>> getPlaylistMovies(Principal principal, @PathVariable Long playlistId) {
        String username = (principal != null) ? principal.getName() : null;
        return ResponseEntity.ok(playlistService.getPlaylistMovies(username, playlistId));
    }

    @PostMapping
    public ResponseEntity<PlaylistModel> createPlaylist(Principal principal, @RequestParam String name, @RequestParam(defaultValue = "false") boolean isPublic) {
        return ResponseEntity.ok(playlistService.createPlaylist(principal.getName(), name, isPublic));
    }

    @PatchMapping("/{playlistId}/toggle-public")
    public ResponseEntity<PlaylistModel> togglePublicStatus(Principal principal, @PathVariable Long playlistId) {
        return ResponseEntity.ok(playlistService.togglePublicStatus(principal.getName(), playlistId));
    }

    @PostMapping("/{playlistId}/add-music/{musicId}")
    public ResponseEntity<PlaylistModel> addMusicToPlaylist(Principal principal, @PathVariable Long playlistId, @PathVariable Long musicId) {
        return ResponseEntity.ok(playlistService.addMusicToPlaylist(principal.getName(), playlistId, musicId));
    }

    @PostMapping("/{playlistId}/add-movie/{movieId}")
    public ResponseEntity<PlaylistModel> addMovieToPlaylist(Principal principal, @PathVariable Long playlistId, @PathVariable Long movieId) {
        return ResponseEntity.ok(playlistService.addMovieToPlaylist(principal.getName(), playlistId, movieId));
    }

    @DeleteMapping("/{playlistId}/remove-music/{musicId}")
    public ResponseEntity<String> removeMusicFromPlaylist(Principal principal, @PathVariable Long playlistId, @PathVariable Long musicId) {
        playlistService.removeMusicFromPlaylist(principal.getName(), playlistId, musicId);
        return ResponseEntity.ok("Music removed from playlist");
    }

    @DeleteMapping("/{playlistId}/remove-movie/{movieId}")
    public ResponseEntity<String> removeMovieFromPlaylist(Principal principal, @PathVariable Long playlistId, @PathVariable Long movieId) {
        playlistService.removeMovieFromPlaylist(principal.getName(), playlistId, movieId);
        return ResponseEntity.ok("Movie removed from playlist");
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<String> deletePlaylist(Principal principal, @PathVariable Long playlistId) {
        playlistService.deletePlaylist(principal.getName(), playlistId);
        return ResponseEntity.ok("Playlist deleted successfully");
    }

    @PutMapping("/{playlistId}")
    public ResponseEntity<PlaylistModel> updatePlaylist(Principal principal, @PathVariable Long playlistId, @RequestParam String name, @RequestParam boolean isPublic) {
        return ResponseEntity.ok(playlistService.updatePlaylist(principal.getName(), playlistId, name, isPublic));
    }
}

