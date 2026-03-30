package com.magrega.demo.dto.order;

import com.magrega.demo.model.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusDTO {
    private Integer orderId;
    private OrderStatus orderStatus;
}
