package com.example.Backend.Entities.Movies;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "actors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActorModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public ActorModel(String name) {
        this.name = name;
    }
}
