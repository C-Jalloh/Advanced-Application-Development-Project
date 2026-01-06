package com.example.Backend.Entities.Music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String imageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private Set<AlbumModel> albums = new HashSet<>();
}
