package com.magrega.demo.service;

import com.magrega.demo.dto.review.CreateReviewDTO;
import com.magrega.demo.model.*;
import com.magrega.demo.repository.ProductRepo;
import com.magrega.demo.repository.ReviewRepo;
import com.magrega.demo.repository.UserRepo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Service
public class ReviewService
{
    @Autowired
    private ReviewRepo reviewRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UserRepo userRepo;

    public List<Review> getReviews()
    {
        return reviewRepo.findAll();
    }

    public Review getReviewById(int id)
    {
        return reviewRepo.findById(id).orElse(null);
    }

    public Review createReview(CreateReviewDTO request) {

        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCommentedAt(LocalDateTime.now());
        return reviewRepo.save(review);
    }

//    public void updateCategoryById(Category category)
//    {
//        categoryRepo.save(category);
//    }

//    public void deleteCategoryById(int id)
//    {
//        categoryRepo.deleteById(id);
//    }
}
