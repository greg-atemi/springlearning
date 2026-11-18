package com.magrega.demo.service;

import com.magrega.demo.dto.review.CreateReviewDTO;
import com.magrega.demo.dto.review.OrderItemReviewResponse;
import com.magrega.demo.dto.review.ReviewResponse;
import com.magrega.demo.dto.review.ReviewsResponse;
import com.magrega.demo.model.Order;
import com.magrega.demo.model.OrderItem;
import com.magrega.demo.model.Review;
import com.magrega.demo.model.User;
import com.magrega.demo.model.enums.OrderStatus;
import com.magrega.demo.repository.OrderItemRepo;
import com.magrega.demo.repository.ProductRepo;
import com.magrega.demo.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepo reviewRepository;
    private final ProductRepo productRepository;
    private final OrderItemRepo orderItemRepository;

    // ── Public: reviews shown on a product page ─────────────────────────────
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

    // ── Authenticated: review a purchased item ──────────────────────────────
    @Transactional
    public OrderItemReviewResponse create(Integer orderItemId, User user, CreateReviewDTO req) {
        OrderItem item = getDeliveredItemOwnedBy(orderItemId, user);

        if (reviewRepository.existsByOrderItemId(orderItemId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already reviewed this item");
        }

        Review review = new Review();
        review.setOrderItem(item);
        review.setProduct(item.getProduct());
        review.setUser(user);
        review.setRating(req.rating());
        review.setComment(blankToNull(req.comment()));
        review.setAnonymous(req.anonymous());

        try {
            return OrderItemReviewResponse.from(reviewRepository.saveAndFlush(review));
        } catch (DataIntegrityViolationException e) {
            // two simultaneous submits: the unique order_item_id constraint caught the second
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already reviewed this item");
        }
    }

    @Transactional
    public OrderItemReviewResponse update(Integer orderItemId, User user, CreateReviewDTO req) {
        Review review = reviewRepository.findByOrderItemId(orderItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        // 404 rather than 403, so other people's reviews aren't revealed
        if (!review.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }

        review.setRating(req.rating());
        review.setComment(blankToNull(req.comment()));
        review.setAnonymous(req.anonymous());
        return OrderItemReviewResponse.from(review);   // managed entity, flushed on commit
    }

    // ── Helpers ─────────────────────────────────────────────────────────────
    private OrderItem getDeliveredItemOwnedBy(Integer orderItemId, User user) {
        OrderItem item = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found"));

        Order order = item.getOrder();

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found");
        }
        if (order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can review this item once your order has been delivered");
        }
        return item;
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}