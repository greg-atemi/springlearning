package com.magrega.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@Entity
public class Product
{
    @Id
    private int prodId;
    private String prodName;
    private int price;
}
