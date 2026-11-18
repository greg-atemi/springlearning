package com.magrega.demo.dto.orderItem;

import com.magrega.demo.dto.review.OrderItemReviewResponse;
import com.magrega.demo.model.OrderItem;
import com.magrega.demo.model.Product;

import java.math.BigDecimal;

public record OrderItemResponse(
        Integer id,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal,
        Product product,
        OrderItemReviewResponse review) {   // null when the item has no review yet

    public static OrderItemResponse from(OrderItem i, OrderItemReviewResponse review) {
        return new OrderItemResponse(
                i.getId(), i.getQuantity(), i.getUnitPrice(), i.getSubTotal(),
                i.getProduct(), review);
    }
}