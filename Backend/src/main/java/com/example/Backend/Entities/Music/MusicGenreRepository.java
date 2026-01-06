package com.example.Backend.Entities.Music;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MusicGenreRepository extends JpaRepository<MusicGenreModel, Long> {
    Optional<MusicGenreModel> findByName(String name);
}
