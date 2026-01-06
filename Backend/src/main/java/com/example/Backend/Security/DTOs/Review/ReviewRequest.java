package com.example.Backend.Security.DTOs.Review;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import com.example.Backend.Entities.Reviews.ContentType;


public class ReviewRequest {

    private Long contentId;

    private ContentType contentType; // MOVIE or MUSIC

    @Min(1)
    @Max(5)
    private int rating; // Between 1 and 5

    @NotBlank
    private String reviewText;

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public @Min(1) @Max(5) int getRating() {
        return rating;
    }

    public void setRating(@Min(1) @Max(5) int rating) {
        this.rating = rating;
    }

    public @NotBlank String getReviewText() {
        return reviewText;
    }

    public void setReviewText(@NotBlank String reviewText) {
        this.reviewText = reviewText;
    }
}
