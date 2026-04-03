package com.magrega.demo.service;

import com.magrega.demo.dto.review.CreateReviewDTO;
import com.magrega.demo.model.Product;
import com.magrega.demo.model.Review;
import com.magrega.demo.model.User;
import com.magrega.demo.repository.ProductRepo;
import com.magrega.demo.repository.ReviewRepo;
import com.magrega.demo.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepo reviewRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private ReviewService reviewService;

    private User mockUser;
    private Product mockProduct;
    private Review mockReview;
    private CreateReviewDTO mockRequest;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setName("Galaxy S24");

        mockReview = new Review();
        mockReview.setId(1);
        mockReview.setUser(mockUser);
        mockReview.setProduct(mockProduct);
        mockReview.setRating(5);
        mockReview.setComment("Excellent phone!");

        mockRequest = new CreateReviewDTO();
        mockRequest.setUserId(1);
        mockRequest.setProductId(1);
        mockRequest.setRating(5);
        mockRequest.setComment("Excellent phone!");
    }

    // ─────────────────────────────────────────────
    // getReviews()
    // ─────────────────────────────────────────────

    @Test
    void getReviews_ShouldReturnAllReviews() {
        when(reviewRepo.findAll()).thenReturn(List.of(mockReview));

        List<Review> result = reviewService.getReviews();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getComment()).isEqualTo("Excellent phone!");
        verify(reviewRepo, times(1)).findAll();
    }

    @Test
    void getReviews_ShouldReturnEmptyList_WhenNoReviews() {
        when(reviewRepo.findAll()).thenReturn(List.of());

        List<Review> result = reviewService.getReviews();

        assertThat(result).isEmpty();
        verify(reviewRepo, times(1)).findAll();
    }

    // ─────────────────────────────────────────────
    // getReviewById()
    // ─────────────────────────────────────────────

    @Test
    void getReviewById_ShouldReturnReview_WhenExists() {
        when(reviewRepo.findById(1)).thenReturn(Optional.of(mockReview));

        Review result = reviewService.getReviewById(1);

        assertThat(result).isNotNull();
        assertThat(result.getRating()).isEqualTo(5);
        verify(reviewRepo, times(1)).findById(1);
    }

    @Test
    void getReviewById_ShouldReturnNull_WhenNotFound() {
        when(reviewRepo.findById(99)).thenReturn(Optional.empty());

        Review result = reviewService.getReviewById(99);

        assertThat(result).isNull();
        verify(reviewRepo, times(1)).findById(99);
    }

    // ─────────────────────────────────────────────
    // createReview()
    // ─────────────────────────────────────────────

    @Test
    void createReview_ShouldCreateAndReturnReview_WhenValid() {
        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));
        when(reviewRepo.save(any(Review.class))).thenAnswer(inv -> inv.getArgument(0));

        Review result = reviewService.createReview(mockRequest);

        assertThat(result).isNotNull();
        assertThat(result.getRating()).isEqualTo(5);
        assertThat(result.getComment()).isEqualTo("Excellent phone!");
        verify(reviewRepo, times(1)).save(any(Review.class));
    }

    @Test
    void createReview_ShouldLinkUserAndProduct() {
        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));
        when(reviewRepo.save(any(Review.class))).thenAnswer(inv -> inv.getArgument(0));

        Review result = reviewService.createReview(mockRequest);

        assertThat(result.getUser()).isEqualTo(mockUser);
        assertThat(result.getProduct()).isEqualTo(mockProduct);
    }

    @Test
    void createReview_ShouldSetCommentedAt() {
        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));
        when(reviewRepo.save(any(Review.class))).thenAnswer(inv -> inv.getArgument(0));

        Review result = reviewService.createReview(mockRequest);

        assertThat(result.getCommentedAt()).isNotNull();
    }

    @Test
    void createReview_ShouldThrow_WhenUserNotFound() {
        when(userRepo.findById(99)).thenReturn(Optional.empty());
        mockRequest.setUserId(99);

        assertThatThrownBy(() -> reviewService.createReview(mockRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verify(reviewRepo, never()).save(any());
        verify(productRepo, never()).findById(any());
    }

    @Test
    void createReview_ShouldThrow_WhenProductNotFound() {
        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));
        when(productRepo.findById(99)).thenReturn(Optional.empty());
        mockRequest.setProductId(99);

        assertThatThrownBy(() -> reviewService.createReview(mockRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Product not found");

        verify(reviewRepo, never()).save(any());
    }
}