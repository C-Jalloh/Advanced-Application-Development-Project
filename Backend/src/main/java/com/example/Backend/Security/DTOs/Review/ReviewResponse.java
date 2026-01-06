package com.example.Backend.Security.DTOs.Review;


import com.example.Backend.Entities.Reviews.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class ReviewResponse {
    private Long id;
    private String username;
    private Long contentId;
    private ContentType contentType;
    private int rating;
    private String reviewText;
    private LocalDateTime createdAt;

    public ReviewResponse(Long id,String username, Long contentId, ContentType contentType, int rating, String reviewText, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.contentId = contentId;
        this.contentType = contentType;
        this.rating = rating;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
