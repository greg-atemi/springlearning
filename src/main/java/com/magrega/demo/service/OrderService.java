package com.magrega.demo.service;

import com.magrega.demo.dto.OrderDTO;
import com.magrega.demo.model.Address;
import com.magrega.demo.model.Order;
import com.magrega.demo.model.User;
import com.magrega.demo.repository.AddressRepo;
import com.magrega.demo.repository.OrderRepo;
import com.magrega.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService
{
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;

    public List<Order> getOrders()
    {
        return orderRepo.findAll();
    }

    public Order getOrderById(int id)
    {
        return orderRepo.findById(id).orElse(null);
    }

    public Order createOrder(OrderDTO request) {

        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressRepo.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        Order order = new Order();
        order.setTotalAmount(request.getTotalAmount());
        order.setOrderStatus(request.getOrderStatus());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setUser(user);
        order.setAddress(address);

        return orderRepo.save(order);
    }

    public Order updateOrderById(Integer id, OrderDTO request) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (request.getTotalAmount() != null) {
            order.setTotalAmount(request.getTotalAmount());
        }

        if (request.getOrderStatus() != null) {
            order.setOrderStatus(request.getOrderStatus());
        }

        if (request.getPaymentStatus() != null) {
            order.setPaymentStatus(request.getPaymentStatus());
        }

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

}
