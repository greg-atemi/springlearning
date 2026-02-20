package com.magrega.demo.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Component
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String brand;
    private BigDecimal price;
    private String category;

    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean available = Boolean.FALSE;

    @JsonSetter(nulls = Nulls.SKIP)
    private Integer quantity = 0;

    private Date releaseDate;

    private String imageName;
    private String imageType;

    @Lob
    private byte[] imageData;
}
