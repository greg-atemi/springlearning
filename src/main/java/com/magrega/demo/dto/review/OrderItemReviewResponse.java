package com.magrega.demo.dto.review;

import com.magrega.demo.model.Review;

public record OrderItemReviewResponse(
        Long id,
        Integer rating,
        String comment,
        boolean anonymous) {

    public static OrderItemReviewResponse from(Review r) {
        return new OrderItemReviewResponse(
                r.getId(),
                r.getRating(),
                r.getComment(),
                r.isAnonymous());
    }
}