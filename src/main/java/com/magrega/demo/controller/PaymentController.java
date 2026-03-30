package com.magrega.demo.controller;

import com.magrega.demo.dto.payment.ApprovePaymentDTO;
import com.magrega.demo.dto.payment.CreatePaymentDTO;
import com.magrega.demo.model.Payment;
import com.magrega.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class PaymentController
{
    @Autowired
    PaymentService service;

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments(){
        return new ResponseEntity<>(service.getAllPayments(), HttpStatus.OK);
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable int id)
    {
        Payment payment = service.getPaymentById(id);

        if (payment != null)
        {
            return new ResponseEntity<>(payment, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/payment")
    public Payment createPayment(@RequestBody CreatePaymentDTO request) {
        return service.createPayment(request);
    }

    @PostMapping("/payment/approve")
    public Payment ApprovePayment(@RequestBody ApprovePaymentDTO request) {
        return service.approvePaymentAndReduceStock(request);
    }

//    @PutMapping("/order/{id}")
//    public Order updateOrder(@PathVariable Integer id,
//                             @RequestBody CreateOrderDTO request) {
//        return service.updateOrderById(id, request);
//    }
}
