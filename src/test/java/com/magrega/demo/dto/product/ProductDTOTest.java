package com.magrega.demo.dto.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO();
    }

    @Test
    void testSetAndGetBrand() {
        productDTO.setBrand("Samsung");
        assertEquals("Samsung", productDTO.getBrand());
    }

    @Test
    void testSetAndGetName() {
        productDTO.setName("Galaxy S25");
        assertEquals("Galaxy S25", productDTO.getName());
    }

    @Test
    void testSetAndGetDescription() {
        productDTO.setDescription("Flagship smartphone with AI features");
        assertEquals("Flagship smartphone with AI features", productDTO.getDescription());
    }

    @Test
    void testSetAndGetCategoryId() {
        productDTO.setCategoryId(4);
        assertEquals(4, productDTO.getCategoryId());
    }

    @Test
    void testSetAndGetPrice() {
        BigDecimal price = new BigDecimal("89999.00");
        productDTO.setPrice(price);
        assertEquals(new BigDecimal("89999.00"), productDTO.getPrice());
    }

    @Test
    void testSetAndGetReleaseDate() {
        Date date = new Date();
        productDTO.setReleaseDate(date);
        assertEquals(date, productDTO.getReleaseDate());
    }

    @Test
    void testAvailableDefaultIsFalse() {
        assertFalse(productDTO.isAvailable());
    }

    @Test
    void testSetAvailableToTrue() {
        productDTO.setAvailable(true);
        assertTrue(productDTO.isAvailable());
    }

    @Test
    void testSetAvailableToFalse() {
        productDTO.setAvailable(true);
        productDTO.setAvailable(false);
        assertFalse(productDTO.isAvailable());
    }

    @Test
    void testQuantityDefaultIsZero() {
        assertEquals(0, productDTO.getQuantity());
    }

    @Test
    void testSetAndGetQuantity() {
        productDTO.setQuantity(150);
        assertEquals(150, productDTO.getQuantity());
    }

    @Test
    void testNullableStringFieldsDefaultToNull() {
        assertNull(productDTO.getBrand());
        assertNull(productDTO.getName());
        assertNull(productDTO.getDescription());
        assertNull(productDTO.getPrice());
        assertNull(productDTO.getReleaseDate());
        assertNull(productDTO.getCategoryId());
    }

    @Test
    void testEqualsAndHashCode() {
        Date date = new Date(0);

        ProductDTO dto1 = new ProductDTO();
        dto1.setBrand("Apple");
        dto1.setName("iPhone 16");
        dto1.setPrice(new BigDecimal("120000.00"));
        dto1.setAvailable(true);
        dto1.setQuantity(50);
        dto1.setReleaseDate(date);

        ProductDTO dto2 = new ProductDTO();
        dto2.setBrand("Apple");
        dto2.setName("iPhone 16");
        dto2.setPrice(new BigDecimal("120000.00"));
        dto2.setAvailable(true);
        dto2.setQuantity(50);
        dto2.setReleaseDate(date);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEqualWhenPriceDiffers() {
        ProductDTO dto1 = new ProductDTO();
        dto1.setPrice(new BigDecimal("100.00"));

        ProductDTO dto2 = new ProductDTO();
        dto2.setPrice(new BigDecimal("200.00"));

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        productDTO.setBrand("Sony");
        productDTO.setName("WH-1000XM5");
        String result = productDTO.toString();
        assertTrue(result.contains("Sony"));
        assertTrue(result.contains("WH-1000XM5"));
    }
}