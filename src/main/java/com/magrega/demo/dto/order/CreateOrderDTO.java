package com.magrega.demo.dto.order;

import com.magrega.demo.dto.orderItem.CreateOrderItemDTO;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateOrderDTO {
    private UUID userId;
    private Integer addressId;
    private String paymentMethod;
    private List<CreateOrderItemDTO> items;
}