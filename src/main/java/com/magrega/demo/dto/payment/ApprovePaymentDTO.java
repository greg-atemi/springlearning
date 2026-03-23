package com.magrega.demo.dto.payment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApprovePaymentDTO {
    private Integer orderId;
    private LocalDateTime paidAt;
}
