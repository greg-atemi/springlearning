package com.magrega.demo.dto.review;

import com.magrega.demo.model.Product;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateReviewDTO {
    private UUID userId;
    private Integer productId;
    private Integer rating;
    private String comment;
}
