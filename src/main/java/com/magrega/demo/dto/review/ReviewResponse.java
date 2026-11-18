package com.magrega.demo.dto.review;

import com.magrega.demo.model.Review;

import java.time.Instant;

public record ReviewResponse(
        Long id, String author, Integer rating, String comment, Instant createdAt) {

    public static ReviewResponse from(Review r) {
        return new ReviewResponse(
                r.getId(),
                r.getUser().getUsername(),   // use whichever display field your User has
                r.getRating(),
                r.getComment(),
                r.getCreatedAt());
    }
}
