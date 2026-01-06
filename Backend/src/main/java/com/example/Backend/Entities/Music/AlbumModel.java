package com.example.Backend.Entities.Music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "albums")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private Integer releaseYear;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private ArtistModel artist;

    @JsonIgnore
    @OneToMany(mappedBy = "albumEntity", cascade = CascadeType.ALL)
    private Set<MusicModel> tracks = new HashSet<>();
}
