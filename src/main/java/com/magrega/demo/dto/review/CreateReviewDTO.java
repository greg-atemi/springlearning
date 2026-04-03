package com.magrega.demo.dto.review;

import com.magrega.demo.model.Product;

import lombok.Data;

@Data
public class CreateReviewDTO {
    private Integer userId;
    private Integer productId;
    private Integer rating;
    private String comment;
}
