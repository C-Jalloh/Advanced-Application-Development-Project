package com.example.Backend.Entities.Reviews;

import com.example.Backend.Entities.Movies.MovieModel;
import com.example.Backend.Entities.Movies.MovieRepository;
import com.example.Backend.Entities.Music.MusicModel;
import com.example.Backend.Entities.Music.MusicRepository;
import com.example.Backend.Entities.Playlists.PlaylistModel;
import com.example.Backend.Entities.Playlists.PlaylistRepository;
import com.example.Backend.Entities.Users.Role;
import com.example.Backend.Entities.Users.UserModel;
import com.example.Backend.Entities.Users.UserRepository;
import com.example.Backend.Security.DTOs.Review.ReviewRequest;
import com.example.Backend.Security.DTOs.Review.ReviewResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final MusicRepository musicRepository;
    private final PlaylistRepository playlistRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, MovieRepository movieRepository, MusicRepository musicRepository, PlaylistRepository playlistRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.musicRepository = musicRepository;
        this.playlistRepository = playlistRepository;
    }

    public ReviewResponse addReview(String username, ReviewRequest request) {
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ReviewModel review = new ReviewModel();
        review.setUser(user);
        review.setContentId(request.getContentId());
        review.setContentType(request.getContentType());
        review.setRating(request.getRating());
        review.setReviewText(request.getReviewText());

        ReviewModel savedReview = reviewRepository.save(review);
        updateContentRating(request.getContentId(),request.getContentType());
        return new ReviewResponse(
                savedReview.getId(),
                savedReview.getUser().getUsername(),
                savedReview.getContentId(),
                savedReview.getContentType(),
                savedReview.getRating(),
                savedReview.getReviewText(),
                savedReview.getCreatedAt()
        );
    }

    public Double getAverageRating(Long contentId, ContentType contentType) {
        List<ReviewModel> reviews = reviewRepository.findByContentIdAndContentType(contentId, contentType);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToDouble(ReviewModel::getRating)
                .average()
                .orElse(0.0);
    }
    private void updateContentRating(Long contentId, ContentType contentType) {
        if(contentType.equals(ContentType.MOVIE)){
            MovieModel movie = movieRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Movie not found"));
            Double averageRating = getAverageRating(contentId,contentType);
            movie.setRating(averageRating);
            movieRepository.save(movie);
        } else if(contentType.equals(ContentType.MUSIC)){
            MusicModel music = musicRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Music not found"));
            Double averageRating = getAverageRating(contentId,contentType);
            music.setRating(averageRating);
            musicRepository.save(music);
        } else if(contentType.equals(ContentType.PLAYLIST)){
            PlaylistModel playlist = playlistRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Playlist not found"));
            Double averageRating = getAverageRating(contentId,contentType);
            playlist.setRating(averageRating);
            playlistRepository.save(playlist);
        }
    }

    public List<ReviewResponse> getReviewsByContent(Long contentId, ContentType contentType) {
        List<ReviewModel> reviews = reviewRepository.findByContentIdAndContentType(contentId, contentType);
        return reviews.stream().map(review -> new ReviewResponse(
                review.getId(),
                review.getUser().getUsername(),
                review.getContentId(),
                review.getContentType(),
                review.getRating(),
                review.getReviewText(),
                review.getCreatedAt()
        )).collect(Collectors.toList());
    }

    public void deleteReview(Long reviewId, String username) {
        ReviewModel review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!review.getUser().getUsername().equals(username) && user.getRole() != Role.ADMIN) {
            throw new RuntimeException("You can only delete your own reviews!");
        }

        updateContentRating(review.getContentId(),review.getContentType());
        reviewRepository.deleteById(reviewId);
    }
}