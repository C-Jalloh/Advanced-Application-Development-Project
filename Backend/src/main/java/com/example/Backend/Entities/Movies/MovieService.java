package com.example.Backend.Entities.Movies;

import com.example.Backend.Entities.Reviews.ContentType;
import com.example.Backend.Entities.Reviews.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ReviewService reviewService;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;

    public MovieService(MovieRepository movieRepository, ReviewService reviewService, 
                        GenreRepository genreRepository, DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
        this.reviewService = reviewService;
        this.genreRepository = genreRepository;
        this.directorRepository = directorRepository;
    }

    public Page<MovieModel> getAllMovies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieModel> movies = movieRepository.findAll(pageable);
        movies.forEach(movie -> {
            Double averageRating = reviewService.getAverageRating(movie.getId(), ContentType.MOVIE);
            movie.setRating(averageRating);
        });
        return movies;
    }

    public Page<MovieModel> searchAndFilterMovies(String title, String genre, Integer year, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieModel> movies = movieRepository.searchAndFilter(
            title != null && !title.isEmpty() ? title : null,
            genre != null && !genre.isEmpty() ? genre : null,
            year,
            pageable
        );
        movies.forEach(movie -> {
            Double averageRating = reviewService.getAverageRating(movie.getId(), ContentType.MOVIE);
            movie.setRating(averageRating);
        });
        return movies;
    }

    public List<String> getDistinctGenres() {
        return movieRepository.findDistinctGenres();
    }

    public List<Integer> getDistinctReleaseYears() {
        return movieRepository.findDistinctReleaseYears();
    }

    public Optional<MovieModel> getMovieById(Long id) {
        return movieRepository.findById(id).map(movie -> {
            Double averageRating = reviewService.getAverageRating(movie.getId(), ContentType.MOVIE);
            movie.setRating(averageRating);
            return movie;
        });
    }

    public MovieModel addMovie(MovieModel movie) {
        syncNormalizedData(movie);
        return movieRepository.save(movie);
    }

    public MovieModel updateMovie(Long id, MovieModel updatedMovie) {
        return movieRepository.findById(id).map(movie -> {
            movie.setTitle(updatedMovie.getTitle());
            movie.setGenre(updatedMovie.getGenre());
            movie.setDirector(updatedMovie.getDirector());
            movie.setReleaseYear(updatedMovie.getReleaseYear());
            movie.setDuration(updatedMovie.getDuration());
            movie.setDescription(updatedMovie.getDescription());
            movie.setImageUrl(updatedMovie.getImageUrl());
            
            syncNormalizedData(movie);

            if (updatedMovie.getRating() != null) {
                movie.setRating(updatedMovie.getRating());
            } else {
                Double averageRating = reviewService.getAverageRating(movie.getId(), ContentType.MOVIE);
                movie.setRating(averageRating);
            }
            return movieRepository.save(movie);
        }).orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    private void syncNormalizedData(MovieModel movie) {
        // Sync Genre
        if (movie.getGenre() != null && !movie.getGenre().isEmpty()) {
            String genreName = movie.getGenre().trim();
            GenreModel genre = genreRepository.findByName(genreName)
                    .orElseGet(() -> {
                        GenreModel newGenre = new GenreModel();
                        newGenre.setName(genreName);
                        return genreRepository.save(newGenre);
                    });
            movie.getGenres().add(genre);
        }

        // Sync Director
        if (movie.getDirector() != null && !movie.getDirector().isEmpty()) {
            String directorName = movie.getDirector().trim();
            DirectorModel director = directorRepository.findByName(directorName)
                    .orElseGet(() -> {
                        DirectorModel newDirector = new DirectorModel();
                        newDirector.setName(directorName);
                        return directorRepository.save(newDirector);
                    });
            movie.setDirectorEntity(director);
        }
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}