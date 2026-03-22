package com.magrega.demo.controller;

import com.magrega.demo.dto.orderItem.CreateOrderItemDTO;
import com.magrega.demo.model.OrderItem;
import com.magrega.demo.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class OrderItemController
{
    @Autowired
    OrderItemService service;

    @GetMapping("/orderItems")
    public ResponseEntity<List<OrderItem>> getOrders(){
        return new ResponseEntity<>(service.getOrderItems(), HttpStatus.OK);
    }

    @GetMapping("/orderItem/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable int id)
    {
        OrderItem orderItem = service.getOrderItemById(id);

        if (orderItem != null)
        {
            return new ResponseEntity<>(orderItem, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/orderItem/{id}")
    public void deleteOrderItemById(@PathVariable int id)
    {
        service.deleteOrderItemById(id);
    }

    @PostMapping("/orderItem")
    public OrderItem createOrderItem(@RequestBody CreateOrderItemDTO request) {
        return service.createOrUpdateOrderItem(request);
    }
}
