package com.example.Backend.Entities.Movies;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<GenreModel, Long> {
    Optional<GenreModel> findByName(String name);
}
