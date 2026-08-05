package com.magrega.demo.controller;

import com.magrega.demo.dto.review.CreateReviewDTO;
import com.magrega.demo.filter.JwtAuthFilter;
import com.magrega.demo.model.Review;
import com.magrega.demo.service.JwtService;
import com.magrega.demo.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReviewService reviewService;

    private Review mockReview;
    private CreateReviewDTO mockRequest;

    @BeforeEach
    void setUp() {
        mockReview = new Review();
        mockReview.setId(1);
        mockReview.setRating(5);
        mockReview.setComment("Excellent phone!");

        mockRequest = new CreateReviewDTO();
        mockRequest.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mockRequest.setProductId(1);
        mockRequest.setRating(5);
        mockRequest.setComment("Excellent phone!");
    }

    @Test
    void GET_reviews_ShouldReturn200_WithReviewList() throws Exception {
        when(reviewService.getReviews()).thenReturn(List.of(mockReview));

        mockMvc.perform(get("/api/review"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].comment").value("Excellent phone!"));
    }

    @Test
    void GET_reviews_ShouldReturn200_WithEmptyList() throws Exception {
        when(reviewService.getReviews()).thenReturn(List.of());

        mockMvc.perform(get("/api/review"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void GET_reviewById_ShouldReturn200_WhenFound() throws Exception {
        when(reviewService.getReviewById(1)).thenReturn(mockReview);

        mockMvc.perform(get("/api/review/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Excellent phone!"));
    }

    @Test
    void GET_reviewById_ShouldReturn404_WhenNotFound() throws Exception {
        when(reviewService.getReviewById(99)).thenReturn(null);

        mockMvc.perform(get("/api/review/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void POST_review_ShouldReturn200_WhenCreated() throws Exception {
        when(reviewService.createReview(any(CreateReviewDTO.class)))
                .thenReturn(mockReview);

        mockMvc.perform(post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isOk());
    }
}