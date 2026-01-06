package com.example.Backend.Entities.Music;

import com.example.Backend.Entities.Reviews.ContentType;
import com.example.Backend.Entities.Reviews.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MusicService {

    private final MusicRepository musicRepository;
    private final ReviewService reviewService;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final MusicGenreRepository musicGenreRepository;

    public MusicService(MusicRepository musicRepository, ReviewService reviewService,
                        ArtistRepository artistRepository, AlbumRepository albumRepository,
                        MusicGenreRepository musicGenreRepository) {
        this.musicRepository = musicRepository;
        this.reviewService = reviewService;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.musicGenreRepository = musicGenreRepository;
    }

    public Page<MusicModel> getAllMusic(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<MusicModel> musicPage = musicRepository.findAll(pageable);
            musicPage.forEach(music -> {
                try {
                    Double averageRating = reviewService.getAverageRating(music.getId(), ContentType.MUSIC);
                    music.setRating(averageRating);
                } catch (Exception e) {
                    System.err.println("Error fetching rating for music ID " + music.getId() + ": " + e.getMessage());
                    music.setRating(0.0);
                }
            });
            return musicPage;
        } catch (Exception e) {
            System.err.println("Error in getAllMusic: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Page<MusicModel> searchAndFilterMusic(String query, String genre, String artist, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<MusicModel> musicPage = musicRepository.searchAndFilter(
                query != null && !query.isEmpty() ? query : null,
                genre != null && !genre.isEmpty() ? genre : null,
                artist != null && !artist.isEmpty() ? artist : null,
                pageable
            );
            musicPage.forEach(music -> {
                try {
                    Double averageRating = reviewService.getAverageRating(music.getId(), ContentType.MUSIC);
                    music.setRating(averageRating);
                } catch (Exception e) {
                    System.err.println("Error fetching rating for music ID " + music.getId() + ": " + e.getMessage());
                    music.setRating(0.0);
                }
            });
            return musicPage;
        } catch (Exception e) {
            System.err.println("Error in searchAndFilterMusic: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<String> getDistinctGenres() {
        return musicRepository.findDistinctGenres();
    }

    public List<String> getDistinctArtists() {
        return musicRepository.findDistinctArtists();
    }

    public Optional<MusicModel> getMusicById(Long id) {
        return musicRepository.findById(id).map(music -> {
            Double averageRating = reviewService.getAverageRating(music.getId(), ContentType.MUSIC);
            music.setRating(averageRating);
            return music;
        });
    }

    public MusicModel addMusic(MusicModel music) {
        syncNormalizedData(music);
        return musicRepository.save(music);
    }

    public MusicModel updateMusic(Long id, MusicModel updatedMusic) {
        return musicRepository.findById(id).map(music -> {
            music.setTitle(updatedMusic.getTitle());
            music.setArtist(updatedMusic.getArtist());
            music.setAlbum(updatedMusic.getAlbum());
            music.setGenre(updatedMusic.getGenre());
            music.setReleaseYear(updatedMusic.getReleaseYear());
            music.setDuration(updatedMusic.getDuration());
            music.setImageUrl(updatedMusic.getImageUrl());
            
            syncNormalizedData(music);

            if (updatedMusic.getRating() != null) {
                music.setRating(updatedMusic.getRating());
            } else {
                Double averageRating = reviewService.getAverageRating(music.getId(), ContentType.MUSIC);
                music.setRating(averageRating);
            }
            return musicRepository.save(music);
        }).orElseThrow(() -> new RuntimeException("Music not found"));
    }

    private void syncNormalizedData(MusicModel music) {
        // Sync Artist
        ArtistModel artist = null;
        if (music.getArtist() != null && !music.getArtist().isEmpty()) {
            String artistName = music.getArtist().trim();
            artist = artistRepository.findByName(artistName)
                    .orElseGet(() -> {
                        ArtistModel newArtist = new ArtistModel();
                        newArtist.setName(artistName);
                        return artistRepository.save(newArtist);
                    });
        }

        // Sync Album
        if (music.getAlbum() != null && !music.getAlbum().isEmpty() && artist != null) {
            String albumTitle = music.getAlbum().trim();
            ArtistModel finalArtist = artist;
            AlbumModel album = albumRepository.findByTitleAndArtist(albumTitle, artist)
                    .orElseGet(() -> {
                        AlbumModel newAlbum = new AlbumModel();
                        newAlbum.setTitle(albumTitle);
                        newAlbum.setArtist(finalArtist);
                        newAlbum.setReleaseYear(music.getReleaseYear());
                        return albumRepository.save(newAlbum);
                    });
            music.setAlbumEntity(album);
        }

        // Sync Genre
        if (music.getGenre() != null && !music.getGenre().isEmpty()) {
            String genreName = music.getGenre().trim();
            MusicGenreModel genre = musicGenreRepository.findByName(genreName)
                    .orElseGet(() -> {
                        MusicGenreModel newGenre = new MusicGenreModel();
                        newGenre.setName(genreName);
                        return musicGenreRepository.save(newGenre);
                    });
            
            // Clear existing genres and add the new one to keep it in sync with the string field
            music.getGenres().clear();
            music.getGenres().add(genre);
        }
    }

    public void deleteMusic(Long id) {
        musicRepository.deleteById(id);
    }
}