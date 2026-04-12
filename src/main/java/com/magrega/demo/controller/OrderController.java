package com.magrega.demo.controller;

import com.magrega.demo.dto.order.CreateOrderDTO;
import com.magrega.demo.dto.order.UpdateOrderStatusDTO;
import com.magrega.demo.model.Order;
import com.magrega.demo.model.User;
import com.magrega.demo.service.OrderService;
import com.magrega.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final UserService userService;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders() {
        return new ResponseEntity<>(service.getOrders(), HttpStatus.OK);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        Order order = service.getOrderById(id);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/order/{id}")
    public void deleteOrderById(@PathVariable int id) {
        service.deleteOrderById(id);
    }

    @PostMapping("/order")
    public Order createOrder(@RequestBody CreateOrderDTO request) {
        return service.createOrder(request);
    }

    @PutMapping("/order/{id}")
    public Order updateOrder(@PathVariable Integer id,
                             @RequestBody CreateOrderDTO request) {
        return service.updateOrderById(id, request);
    }

    @PostMapping("/order/updateStatus")
    public Order updateOrderStatus(@RequestBody UpdateOrderStatusDTO request) {
        return service.updateOrderStatus(request);
    }

    @GetMapping("/orders/my")
    public ResponseEntity<List<Order>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        return new ResponseEntity<>(service.getOrdersByUserId(user.getId()), HttpStatus.OK);
    }
}