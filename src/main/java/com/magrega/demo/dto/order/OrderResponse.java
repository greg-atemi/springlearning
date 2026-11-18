package com.magrega.demo.dto.order;

import com.magrega.demo.dto.orderItem.OrderItemResponse;
import com.magrega.demo.dto.review.OrderItemReviewResponse;
import com.magrega.demo.model.Address;
import com.magrega.demo.model.Order;
import com.magrega.demo.model.enums.OrderStatus;
import com.magrega.demo.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record OrderResponse(
        Integer id,
        BigDecimal totalAmount,
        OrderStatus orderStatus,
        PaymentStatus paymentStatus,
        LocalDateTime createdAt,
        Address address,
        List<OrderItemResponse> items) {

    public static OrderResponse from(Order o, Map<Integer, OrderItemReviewResponse> reviewsByItemId) {
        return new OrderResponse(
                o.getId(), o.getTotalAmount(), o.getOrderStatus(), o.getPaymentStatus(),
                o.getCreatedAt(), o.getAddress(),
                o.getItems().stream()
                        .map(i -> OrderItemResponse.from(i, reviewsByItemId.get(i.getId())))
                        .toList());
    }
}