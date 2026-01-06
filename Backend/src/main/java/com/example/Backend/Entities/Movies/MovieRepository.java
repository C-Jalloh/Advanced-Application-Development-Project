package com.example.Backend.Entities.Movies;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<MovieModel, Long> {
    
    // Search by title (case-insensitive)
    Page<MovieModel> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // Filter by genre
    Page<MovieModel> findByGenreContainingIgnoreCase(String genre, Pageable pageable);
    
    // Filter by release year
    Page<MovieModel> findByReleaseYear(Integer year, Pageable pageable);
    
    // Combined search and filter
    @Query("SELECT m FROM MovieModel m WHERE " +
           "(:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:genre IS NULL OR LOWER(m.genre) LIKE LOWER(CONCAT('%', :genre, '%'))) AND " +
           "(:year IS NULL OR m.releaseYear = :year)")
    Page<MovieModel> searchAndFilter(@Param("title") String title, 
                                      @Param("genre") String genre, 
                                      @Param("year") Integer year, 
                                      Pageable pageable);
    
    // Get distinct genres for filter dropdown
    @Query("SELECT DISTINCT m.genre FROM MovieModel m WHERE m.genre IS NOT NULL ORDER BY m.genre")
    List<String> findDistinctGenres();
    
    // Get distinct release years for filter dropdown
    @Query("SELECT DISTINCT m.releaseYear FROM MovieModel m WHERE m.releaseYear IS NOT NULL ORDER BY m.releaseYear DESC")
    List<Integer> findDistinctReleaseYears();
}
