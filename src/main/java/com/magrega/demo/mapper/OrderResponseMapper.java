package com.magrega.demo.mapper;

import com.magrega.demo.dto.order.OrderResponse;
import com.magrega.demo.dto.review.OrderItemReviewResponse;
import com.magrega.demo.model.Order;
import com.magrega.demo.model.OrderItem;
import com.magrega.demo.repository.ReviewRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderResponseMapper {

    private final ReviewRepo reviewRepository;

    public OrderResponse toResponse(Order order) {
        return toResponses(List.of(order)).get(0);
    }

    public List<OrderResponse> toResponses(Collection<Order> orders) {
        List<Integer> itemIds = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .map(OrderItem::getId)
                .toList();

        Map<Integer, OrderItemReviewResponse> reviews = itemIds.isEmpty()
                ? Map.of()
                : reviewRepository.findByOrderItemIds(itemIds).stream()
                .collect(Collectors.toMap(
                        r -> r.getOrderItem().getId(),
                        OrderItemReviewResponse::from));

        return orders.stream().map(o -> OrderResponse.from(o, reviews)).toList();
    }
}