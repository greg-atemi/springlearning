package com.magrega.demo.dto;

import com.magrega.demo.model.enums.OrderStatus;
import com.magrega.demo.model.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDTO {
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private Integer userId;
    private Integer addressId;
}
