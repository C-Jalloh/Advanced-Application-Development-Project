package com.example.Backend.Entities.Reviews;


import com.example.Backend.Security.DTOs.Review.ReviewRequest;
import com.example.Backend.Security.DTOs.Review.ReviewResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(Principal principal, @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.addReview(principal.getName(), request));
    }

    @GetMapping("/{contentType}/{contentId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByContent(
            @PathVariable ContentType contentType, @PathVariable Long contentId) {
        return ResponseEntity.ok(reviewService.getReviewsByContent(contentId, contentType));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(Principal principal, @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId, principal.getName());
        return ResponseEntity.ok("Review deleted successfully");
    }
}

