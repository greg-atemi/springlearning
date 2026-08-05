package com.magrega.demo.service;

import com.magrega.demo.dto.order.CreateOrderDTO;
import com.magrega.demo.dto.order.UpdateOrderStatusDTO;
import com.magrega.demo.dto.orderItem.CreateOrderItemDTO;
import com.magrega.demo.model.*;
import com.magrega.demo.model.enums.OrderStatus;
import com.magrega.demo.model.enums.PaymentStatus;
import com.magrega.demo.repository.AddressRepo;
import com.magrega.demo.repository.OrderRepo;
import com.magrega.demo.repository.ProductRepo;
import com.magrega.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService
{
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private ProductRepo productRepo;

    public List<Order> getOrders()
    {
        return orderRepo.findAll();
    }

    public Order getOrderById(int id)
    {
        return orderRepo.findById(id).orElse(null);
    }

    public Order createOrder(CreateOrderDTO request) {

        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressRepo.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        Order order = new Order();
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setUser(user);
        order.setAddress(address);

        BigDecimal total = BigDecimal.ZERO;

        if (request.getItems() != null) {

            for (CreateOrderItemDTO itemDTO : request.getItems()) {

                Product product = productRepo.findById(itemDTO.getProductId())
                        .orElseThrow(() ->
                                new RuntimeException("Product not found: " + itemDTO.getProductId()));

                OrderItem orderItem = new OrderItem();

                orderItem.setProduct(product);
                orderItem.setQuantity(itemDTO.getQuantity());
                orderItem.setUnitPrice(product.getPrice());

                BigDecimal subtotal =
                        product.getPrice()
                                .multiply(BigDecimal.valueOf(itemDTO.getQuantity()));

                orderItem.setSubTotal(subtotal);

                order.addItem(orderItem);

                total = total.add(subtotal);
            }
        }

        order.setTotalAmount(total);

        return orderRepo.save(order);
    }

    public Order updateOrderById(Integer id, CreateOrderDTO request) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (request.getUserId() != null) {
            User user = userRepo.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            order.setUser(user);
        }

        if (request.getAddressId() != null) {
            Address address = addressRepo.findById(request.getAddressId())
                    .orElseThrow(() -> new RuntimeException("Address not found"));
            order.setAddress(address);
        }

        return orderRepo.save(order);
    }

    public void deleteOrderById(int id) {
        if (!orderRepo.existsById(id)) {
            throw new RuntimeException("Order with id " + id + " not found");
        }
        orderRepo.deleteById(id);
    }

    public Order updateOrderStatus(UpdateOrderStatusDTO request) {

        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderStatus currentOrderStatus = order.getOrderStatus();
        OrderStatus newOrderStatus = request.getOrderStatus();

        System.out.println("---------------------");
        System.out.println("CURRENT ORDER STATUS: " + currentOrderStatus);
        System.out.println("NEW ORDER STATUS: " + newOrderStatus);
        System.out.println("---------------------");

        order.setOrderStatus(
                switch (newOrderStatus) {
                    case PROCESSING -> {
                        if (currentOrderStatus == OrderStatus.PROCESSING) {
                            throw new RuntimeException("Order already PROCESSING");
                        }
                        yield OrderStatus.PROCESSING;
                    }
                    case SHIPPED -> {
                        if (currentOrderStatus == OrderStatus.SHIPPED) {
                            throw new RuntimeException("Order already Shipped");
                        }
                        yield OrderStatus.SHIPPED;
                    }
                    case DELIVERED -> {
                        if (currentOrderStatus == OrderStatus.DELIVERED) {
                            throw new RuntimeException("Order already Delivered");
                        }
                        yield OrderStatus.DELIVERED;
                    }
                    case CANCELLED -> {
                        if (currentOrderStatus == OrderStatus.CANCELLED) {
                            throw new RuntimeException("Order already cancelled");
                        }
                        yield OrderStatus.CANCELLED;
                    }
                }
        );

        return orderRepo.save(order);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepo.findByUserId(userId);
    }

    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }
}
