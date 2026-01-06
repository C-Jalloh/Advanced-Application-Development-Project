package com.example.Backend.Entities.Movies;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<Page<MovieModel>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year) {
        
        // If any search/filter parameters are provided, use search and filter
        if ((title != null && !title.isEmpty()) || 
            (genre != null && !genre.isEmpty()) || 
            year != null) {
            return ResponseEntity.ok(movieService.searchAndFilterMovies(title, genre, year, page, size));
        }
        
        // Otherwise return all movies
        return ResponseEntity.ok(movieService.getAllMovies(page, size));
    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> getGenres() {
        return ResponseEntity.ok(movieService.getDistinctGenres());
    }

    @GetMapping("/years")
    public ResponseEntity<List<Integer>> getReleaseYears() {
        return ResponseEntity.ok(movieService.getDistinctReleaseYears());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<MovieModel>> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<MovieModel> addMovie(@RequestBody MovieModel movie) {
        return ResponseEntity.ok(movieService.addMovie(movie));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<MovieModel> updateMovie(@PathVariable Long id, @RequestBody MovieModel movie) {
        return ResponseEntity.ok(movieService.updateMovie(id, movie));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Movie deleted successfully");
    }
}