package com.magrega.demo.controller;

import com.magrega.demo.dto.review.ReviewsResponse;
import com.magrega.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @GetMapping
    public ReviewsResponse list(@PathVariable Integer productId) {
        return service.getForProduct(productId);
    }
}