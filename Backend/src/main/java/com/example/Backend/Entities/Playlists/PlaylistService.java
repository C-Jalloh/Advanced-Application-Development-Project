package com.example.Backend.Entities.Playlists;


import com.example.Backend.Entities.Music.MusicModel;
import com.example.Backend.Entities.Music.MusicRepository;
import com.example.Backend.Entities.Movies.MovieModel;
import com.example.Backend.Entities.Movies.MovieRepository;
import com.example.Backend.Entities.Users.UserModel;
import com.example.Backend.Entities.Users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final MovieRepository movieRepository;

    public PlaylistService(PlaylistRepository playlistRepository, UserRepository userRepository, MusicRepository musicRepository, MovieRepository movieRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.musicRepository = musicRepository;
        this.movieRepository = movieRepository;
    }

    public List<PlaylistModel> getUserPlaylists(String username) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return playlistRepository.findByUser(user);
    }

    public List<PlaylistModel> getPublicPlaylists() {
        return playlistRepository.findByIsPublicTrue();
    }

    public PlaylistModel getPlaylistById(String username, Long playlistId) {
        PlaylistModel playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.isPublic() && (username == null || !playlist.getUser().getUsername().equals(username))) {
            throw new RuntimeException("You do not have access to this playlist");
        }

        return playlist;
    }

    public List<MusicModel> getPlaylistMusic(String username, Long playlistId) {
        PlaylistModel playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.isPublic() && (username == null || !playlist.getUser().getUsername().equals(username))) {
            throw new RuntimeException("You do not have access to this playlist");
        }

        return playlist.getSongs();
    }

    public List<MovieModel> getPlaylistMovies(String username, Long playlistId) {
        PlaylistModel playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.isPublic() && (username == null || !playlist.getUser().getUsername().equals(username))) {
            throw new RuntimeException("You do not have access to this playlist");
        }

        return playlist.getMovies();
    }

    public PlaylistModel createPlaylist(String username, String playlistName, boolean isPublic) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PlaylistModel playlist = new PlaylistModel();
        playlist.setUser(user);
        playlist.setName(playlistName);
        playlist.setPublic(isPublic);
        return playlistRepository.save(playlist);
    }

    public PlaylistModel togglePublicStatus(String username, Long playlistId) {
        PlaylistModel playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not own this playlist");
        }

        playlist.setPublic(!playlist.isPublic());
        return playlistRepository.save(playlist);
    }

    public PlaylistModel addMusicToPlaylist(String username, Long playlistId, Long musicId) {
        PlaylistModel playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not own this playlist");
        }

        MusicModel music = musicRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music not found"));

        if (!playlist.getSongs().contains(music)) {
            playlist.getSongs().add(music);
        }
        return playlistRepository.save(playlist);
    }

    public PlaylistModel addMovieToPlaylist(String username, Long playlistId, Long movieId) {
        PlaylistModel playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not own this playlist");
        }

        MovieModel movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (!playlist.getMovies().contains(movie)) {
            playlist.getMovies().add(movie);
        }
        return playlistRepository.save(playlist);
    }

    public void removeMusicFromPlaylist(String username, Long playlistId, Long musicId) {
        PlaylistModel playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not own this playlist");
        }

        MusicModel music = musicRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music not found"));

        playlist.getSongs().remove(music);
        playlistRepository.save(playlist);
    }

    public void removeMovieFromPlaylist(String username, Long playlistId, Long movieId) {
        PlaylistModel playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not own this playlist");
        }

        MovieModel movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        playlist.getMovies().remove(movie);
        playlistRepository.save(playlist);
    }

    public void deletePlaylist(String username, Long playlistId) {
        PlaylistModel playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not own this playlist");
        }

        playlistRepository.deleteById(playlistId);
    }

    public PlaylistModel updatePlaylist(String username, Long playlistId, String name, boolean isPublic) {
        PlaylistModel playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You do not own this playlist");
        }

        playlist.setName(name);
        playlist.setPublic(isPublic);
        return playlistRepository.save(playlist);
    }
}
