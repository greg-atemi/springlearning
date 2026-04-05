package com.magrega.demo.dto.product;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductDTO {

    private String brand;
    private String name;
    private String description;
    private Integer categoryId;
    private BigDecimal price;
    private Date releaseDate;
    private Boolean available;
    private Integer quantity;
    private String imageUrl;
    private BigDecimal compareAtPrice;
    private Integer reviewCount;
    private BigDecimal rating;
}
