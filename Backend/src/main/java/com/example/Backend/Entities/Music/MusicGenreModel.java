package com.example.Backend.Entities.Music;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "music_genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicGenreModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
