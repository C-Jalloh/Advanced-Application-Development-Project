package com.example.Backend.Entities.Music;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MusicRepository extends JpaRepository<MusicModel, Long> {
    
    // Search by title or artist (case-insensitive)
    @Query("SELECT m FROM MusicModel m WHERE " +
           "LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.artist) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<MusicModel> searchByTitleOrArtist(@Param("query") String query, Pageable pageable);
    
    // Filter by genre
    Page<MusicModel> findByGenreContainingIgnoreCase(String genre, Pageable pageable);
    
    // Filter by artist
    Page<MusicModel> findByArtistContainingIgnoreCase(String artist, Pageable pageable);
    
    // Combined search and filter
    @Query("SELECT m FROM MusicModel m WHERE " +
           "(:query IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(m.artist) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:genre IS NULL OR LOWER(m.genre) LIKE LOWER(CONCAT('%', :genre, '%'))) AND " +
           "(:artist IS NULL OR LOWER(m.artist) LIKE LOWER(CONCAT('%', :artist, '%')))")
    Page<MusicModel> searchAndFilter(@Param("query") String query,
                                      @Param("genre") String genre,
                                      @Param("artist") String artist,
                                      Pageable pageable);
    
    // Get distinct genres for filter dropdown
    @Query("SELECT DISTINCT m.genre FROM MusicModel m WHERE m.genre IS NOT NULL ORDER BY m.genre")
    List<String> findDistinctGenres();
    
    // Get distinct artists for filter dropdown
    @Query("SELECT DISTINCT m.artist FROM MusicModel m WHERE m.artist IS NOT NULL ORDER BY m.artist")
    List<String> findDistinctArtists();
}
