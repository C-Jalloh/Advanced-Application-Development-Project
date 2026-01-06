package com.example.Backend.Entities.Music;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<AlbumModel, Long> {
    Optional<AlbumModel> findByTitleAndArtist(String title, ArtistModel artist);
}
