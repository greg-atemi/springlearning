package com.magrega.demo.dto.order;

import lombok.Data;

@Data
public class CreateOrderDTO {
    private Integer userId;
    private Integer addressId;
}
