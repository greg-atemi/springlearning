package com.magrega.demo.service;

import com.magrega.demo.dto.payment.CreatePaymentDTO;
import com.magrega.demo.dto.payment.ApprovePaymentDTO;
import com.magrega.demo.model.Order;
import com.magrega.demo.model.OrderItem;
import com.magrega.demo.model.Payment;
import com.magrega.demo.model.Product;
import com.magrega.demo.model.enums.PaymentMethod;
import com.magrega.demo.model.enums.PaymentStatus;
import com.magrega.demo.repository.OrderRepo;
import com.magrega.demo.repository.PaymentRepo;
import com.magrega.demo.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService
{
    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;

    public List<Payment> getAllPayments()
    {
        return paymentRepo.findAll();
    }

    public Payment getPaymentById(int id)
    {
        return paymentRepo.findById(id).orElse(null);
    }

    @Transactional
    public Payment createPayment(CreatePaymentDTO request) {

        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 🚫 Prevent duplicate payment
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("Order already paid");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(PaymentMethod.MPESA);

        BigDecimal requestAmount = request.getAmount();

        if (requestAmount.compareTo(order.getTotalAmount()) != 0) {
            throw new RuntimeException("Payment amount does not match order total");
        }

        payment.setAmount(requestAmount);
        payment.setTransactionReference(request.getTransactionReference());
        payment.setPaidAt(LocalDateTime.now());

        paymentRepo.save(payment);

        // ✅ Mark order as PAID
        order.setPaymentStatus(PaymentStatus.PAID);
        orderRepo.save(order);

        // ✅ Reduce stock
        reduceInventory(order);

        return payment;
    }

    public void reduceInventory(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct(); // assume each OrderItem has a Product
            int newStock = product.getQuantity() - item.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }
            product.setQuantity(newStock);
            productRepo.save(product);
        }
    }

    @Transactional
    public Payment approvePaymentAndReduceStock(ApprovePaymentDTO request) {

        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = paymentRepo.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // 🚫 Prevent double processing
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("Payment already approved");
        }

        // ✅ Update payment
        payment.setPaidAt(LocalDateTime.now());

        // ✅ Update order
        order.setPaymentStatus(PaymentStatus.PAID);

        // ✅ Reduce stock
        reduceInventory(order);

        return paymentRepo.save(payment);
    }
}
