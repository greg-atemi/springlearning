package com.magrega.demo.dto.product;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
public class ProductDTO {

    private String brand;
    private String name;
    private String description;
    private Set<Integer> categoryIds;
    private BigDecimal price;
    private Date releaseDate;
    private Boolean isAvailable = false;
    private Integer quantity = 0;
    private String imageUrl;
    private BigDecimal compareAtPrice;
    private Integer reviewCount;
    private BigDecimal rating;
}