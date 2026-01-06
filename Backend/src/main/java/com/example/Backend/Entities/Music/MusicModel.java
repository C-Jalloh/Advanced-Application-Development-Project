package com.example.Backend.Entities.Music;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "music")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 100)
    private String artist; // Keeping for backward compatibility

    @Column(length = 100)
    private String album; // Keeping for backward compatibility

    @Column(length = 100)
    private String genre; // Keeping for backward compatibility

    private Integer releaseYear;
    private Integer duration; // in seconds

    @Column(length = 500)
    private String imageUrl;

    @Column(name = "image_filename", length = 255)
    private String imageFilename;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private AlbumModel albumEntity;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "music_to_genres",
        joinColumns = @JoinColumn(name = "music_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<MusicGenreModel> genres = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Transient
    private Double rating;
}