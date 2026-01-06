package com.example.Backend.Entities.Music;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<ArtistModel, Long> {
    Optional<ArtistModel> findByName(String name);
}
