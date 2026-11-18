package com.magrega.demo.controller;

import com.magrega.demo.dto.review.CreateReviewDTO;
import com.magrega.demo.dto.review.OrderItemReviewResponse;
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
@RequestMapping("/api/order-items/{orderItemId}/review")
@RequiredArgsConstructor
public class OrderItemReviewController {

    private final ReviewService service;
    private final UserRepo userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderItemReviewResponse create(@PathVariable Integer orderItemId,
                                          @Valid @RequestBody CreateReviewDTO body,
                                          Authentication auth) {
        return service.create(orderItemId, currentUser(auth), body);
    }

    @PutMapping
    public OrderItemReviewResponse update(@PathVariable Integer orderItemId,
                                          @Valid @RequestBody CreateReviewDTO body,
                                          Authentication auth) {
        return service.update(orderItemId, currentUser(auth), body);
    }

    private User currentUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName())   // same lookup your old controller used
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }
}