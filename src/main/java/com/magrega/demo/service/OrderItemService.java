package com.magrega.demo.service;

import com.magrega.demo.dto.orderItem.CreateOrderItemDTO;
import com.magrega.demo.model.Order;
import com.magrega.demo.model.OrderItem;
import com.magrega.demo.model.Product;
import com.magrega.demo.repository.OrderRepo;
import com.magrega.demo.repository.ProductRepo;
import com.magrega.demo.repository.OrderItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService
{
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private ProductRepo productRepo;

    public List<OrderItem> getOrderItems()
    {
        return orderItemRepo.findAll();
    }

    public OrderItem getOrderItemById(int id)
    {
        return orderItemRepo.findById(id).orElse(null);
    }

    public OrderItem createOrUpdateOrderItem(CreateOrderItemDTO request) {

        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int requestedQty = request.getQuantity();

        if (requestedQty == 0) {
            throw new IllegalArgumentException("Quantity cannot be zero");
        }

        Optional<OrderItem> existingItem = order.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {

            OrderItem item = existingItem.get();
            int newQty = item.getQuantity() + requestedQty;

            // ❌ Prevent negative quantity
            if (newQty < 0) {
                throw new RuntimeException("Cannot reduce below zero");
            }

            // 🔥 STOCK VALIDATION (only when increasing)
            if (requestedQty > 0 && newQty > product.getQuantity()) {
                throw new RuntimeException("Not enough stock available");
            }

            // 🔥 If quantity becomes zero → remove item
            if (newQty == 0) {
                order.removeItem(item);
                orderItemRepo.delete(item);
                return null;
            }

            item.setQuantity(newQty);
            item.setSubTotal(
                    item.getUnitPrice().multiply(BigDecimal.valueOf(newQty))
            );

            orderItemRepo.save(item);
            return item;

        } else {

            // ❌ Cannot reduce something that doesn't exist
            if (requestedQty < 0) {
                throw new RuntimeException("Cannot reduce non-existing item");
            }

            // 🔥 STOCK VALIDATION (new item)
            if (requestedQty > product.getQuantity()) {
                throw new RuntimeException("Not enough stock available");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(requestedQty);
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setSubTotal(
                    product.getPrice().multiply(BigDecimal.valueOf(requestedQty))
            );

            order.addItem(orderItem);
            orderItemRepo.save(orderItem);

            // 🔥 Recalculate order total
            order.setTotalAmount(calculateOrderTotal(order));

            orderRepo.save(order);

            return orderItem;
        }
    }

    private BigDecimal calculateOrderTotal(Order order) {
        return order.getItems().stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void deleteOrderItemById(int id) {
        if (!orderItemRepo.existsById(id)) {
            throw new RuntimeException("Order item with id " + id + " not found");
        }
        orderItemRepo.deleteById(id);
    }
}
