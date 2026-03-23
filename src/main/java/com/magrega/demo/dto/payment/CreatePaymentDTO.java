package com.magrega.demo.dto.payment;

import com.magrega.demo.model.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentDTO {
    private Integer orderId;
    private BigDecimal amount;
    private String transactionReference;
    private PaymentMethod paymentMethod;
}
