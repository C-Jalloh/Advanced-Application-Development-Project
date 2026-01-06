package com.example.Backend.Entities.Playlists;



import com.example.Backend.Entities.Users.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<PlaylistModel, Long> {
    List<PlaylistModel> findByUser(UserModel user);
    List<PlaylistModel> findByIsPublicTrue();
}

