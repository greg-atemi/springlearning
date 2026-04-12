package com.magrega.demo.dto.orderItem;

import lombok.Data;

@Data
public class CreateOrderItemDTO {
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
}
