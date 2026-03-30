package com.magrega.demo.controller;

import com.magrega.demo.dto.review.CreateReviewDTO;
import com.magrega.demo.model.Review;
import com.magrega.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController
{
    @Autowired
    ReviewService service;

    @GetMapping("/review")
    public ResponseEntity<List<Review>> getReviews(){
        return new ResponseEntity<>(service.getReviews(), HttpStatus.OK);
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable int id)
    {
        Review review = service.getReviewById(id);

        if (review != null)
        {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/review")
    public void createReview(@RequestBody CreateReviewDTO review)
    {
        service.createReview(review);
    }

//    @DeleteMapping("/category/{id}")
//    public void deleteCategory(@PathVariable int id)
//    {
//        service.deleteCategoryById(id);
//    }

//    @PutMapping("/category")
//    public void updateCategory(@RequestBody Category category)
//    {
//        service.updateCategoryById(category);
//    }
}
