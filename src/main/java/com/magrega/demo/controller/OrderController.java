package com.magrega.demo.controller;

import com.magrega.demo.dto.order.CreateOrderDTO;
import com.magrega.demo.model.Order;
import com.magrega.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class OrderController
{
    @Autowired
    OrderService service;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders(){
        return new ResponseEntity<>(service.getOrders(), HttpStatus.OK);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id)
    {
        Order order = service.getOrderById(id);

        if (order != null)
        {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/order/{id}")
    public void deleteOrderById(@PathVariable int id)
    {
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
}
