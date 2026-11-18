package com.magrega.demo.controller;

import com.magrega.demo.dto.review.CreateReviewDTO;
import com.magrega.demo.dto.review.ReviewsResponse;
import com.magrega.demo.dto.review.ReviewResponse;
import com.magrega.demo.model.User;
import com.magrega.demo.repository.UserRepo;
import com.magrega.demo.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;
    private final UserRepo userRepository;

    @GetMapping
    public ReviewsResponse list(@PathVariable Integer productId) {
        return service.getForProduct(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse create(@PathVariable Integer productId,
                                 @Valid @RequestBody CreateReviewDTO body,
                                 Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())   // match how your JWT filter identifies users
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return service.create(productId, user, body);
    }
}