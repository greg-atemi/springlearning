package com.magrega.demo.dto.review;

import java.util.List;

public record ReviewsResponse(
        double averageRating, long reviewCount, List<ReviewResponse> reviews) {}
