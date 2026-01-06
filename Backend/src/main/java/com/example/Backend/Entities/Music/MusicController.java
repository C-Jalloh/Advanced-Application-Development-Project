package com.example.Backend.Entities.Music;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping
    public ResponseEntity<Page<MusicModel>> getAllMusic(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String artist) {
        
        // If any search/filter parameters are provided, use search and filter
        if ((query != null && !query.isEmpty()) || 
            (genre != null && !genre.isEmpty()) || 
            (artist != null && !artist.isEmpty())) {
            return ResponseEntity.ok(musicService.searchAndFilterMusic(query, genre, artist, page, size));
        }
        
        // Otherwise return all music
        return ResponseEntity.ok(musicService.getAllMusic(page, size));
    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> getGenres() {
        return ResponseEntity.ok(musicService.getDistinctGenres());
    }

    @GetMapping("/artists")
    public ResponseEntity<List<String>> getArtists() {
        return ResponseEntity.ok(musicService.getDistinctArtists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<MusicModel>> getMusicById(@PathVariable Long id) {
        return ResponseEntity.ok(musicService.getMusicById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<MusicModel> addMusic(@RequestBody MusicModel music) {
        return ResponseEntity.ok(musicService.addMusic(music));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<MusicModel> updateMusic(@PathVariable Long id, @RequestBody MusicModel music) {
        return ResponseEntity.ok(musicService.updateMusic(id, music));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> deleteMusic(@PathVariable Long id) {
        musicService.deleteMusic(id);
        return ResponseEntity.ok("Music deleted successfully");
    }
}