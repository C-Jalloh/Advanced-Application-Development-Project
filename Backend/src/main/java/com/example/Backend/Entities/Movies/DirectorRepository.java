package com.example.Backend.Entities.Movies;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DirectorRepository extends JpaRepository<DirectorModel, Long> {
    Optional<DirectorModel> findByName(String name);
}
