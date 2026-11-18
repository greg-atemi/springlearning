package com.magrega.demo.service;

import com.magrega.demo.dto.review.CreateReviewDTO;
import com.magrega.demo.dto.review.ReviewsResponse;
import com.magrega.demo.dto.review.ReviewResponse;
import com.magrega.demo.model.*;
import com.magrega.demo.repository.ProductRepo;
import com.magrega.demo.repository.ReviewRepo;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepo reviewRepository;
    private final ProductRepo productRepository;

    @Transactional(readOnly = true)
    public ReviewsResponse getForProduct(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        var stats = reviewRepository.getStats(productId);
        var reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId)
                .stream().map(ReviewResponse::from).toList();

        double avg = Math.round(stats.getAverage() * 10) / 10.0;
        return new ReviewsResponse(avg, stats.getTotal(), reviews);
    }

    @Transactional
    public ReviewResponse create(Integer productId, User user, CreateReviewDTO req) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (reviewRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already reviewed this product");
        }

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(req.rating());
        review.setComment(req.comment());
        return ReviewResponse.from(reviewRepository.save(review));
    }
}
