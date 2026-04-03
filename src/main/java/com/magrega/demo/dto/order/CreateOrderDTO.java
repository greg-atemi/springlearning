package com.magrega.demo.dto.order;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderDTO {
    private UUID userId;
    private Integer addressId;
}
